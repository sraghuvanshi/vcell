/*
 * Copyright (C) 1999-2011 University of Connecticut Health Center
 *
 * Licensed under the MIT License (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *  http://www.opensource.org/licenses/mit-license.php
 */

package cbit.vcell.mapping;

import java.util.Vector;

import cbit.vcell.geometry.SubVolume;
import cbit.vcell.geometry.SurfaceClass;
import cbit.vcell.model.DistributedKinetics;
import cbit.vcell.model.Feature;
import cbit.vcell.model.Flux;
import cbit.vcell.model.FluxReaction;
import cbit.vcell.model.LumpedKinetics;
import cbit.vcell.model.Membrane;
import cbit.vcell.model.Product;
import cbit.vcell.model.Reactant;
import cbit.vcell.model.ReactionParticipant;
import cbit.vcell.model.ReactionStep;
import cbit.vcell.model.SimpleReaction;
import cbit.vcell.model.SpeciesContext;
import cbit.vcell.model.Kinetics.KineticsParameter;
import cbit.vcell.parser.Expression;
/**
 * This type was created in VisualAge.
 */
public class MembraneStructureAnalyzer extends StructureAnalyzer {
	private SurfaceClass surfaceClass = null;
	private ResolvedFlux resolvedFluxes[] = null;
/**
 * MembraneStructureAnalyzer constructor comment.
 * @param mathMapping cbit.vcell.mapping.MathMapping
 * @param subVolume cbit.vcell.geometry.SubVolume
 */
public MembraneStructureAnalyzer(MathMapping mathMapping, SurfaceClass surfaceClass) {
	super(mathMapping);
	this.surfaceClass = surfaceClass;
}

/**
 * This method was created in VisualAge.
 * @return cbit.vcell.model.Membrane
 */
public SurfaceClass getSurfaceClass() {
	return surfaceClass;
}

/**
 * This method was created in VisualAge.
 * @return UndefinedObject[]
 */
public ResolvedFlux[] getResolvedFluxes() {
	return resolvedFluxes;
}
/**
 * This method was created in VisualAge.
 */
public void refresh() {
	super.refresh();
	try {
		refreshResolvedFluxes();
	}catch (Exception e){
		e.printStackTrace(System.out);
		throw new RuntimeException(e.getMessage());
	}
}
/**
 * This method was created in VisualAge.
 */
private void refreshResolvedFluxes() throws Exception {

//System.out.println("MembraneStructureAnalyzer.refreshResolvedFluxes()");

	GeometryContext geoContext = mathMapping.getSimulationContext().getGeometryContext();
	Vector<ResolvedFlux> resolvedFluxList = new Vector<ResolvedFlux>();

	//
	// for each reaction, get all fluxReactions associated with this membrane
	//
	Vector<FluxReaction> fluxList = new Vector<FluxReaction>();
	ReactionSpec reactionSpecs[] = mathMapping.getSimulationContext().getReactionContext().getReactionSpecs();
	for (int j=0;j<reactionSpecs.length;j++){
		if (reactionSpecs[j].isExcluded()){
			continue;
		}
		ReactionStep rs = reactionSpecs[j].getReactionStep();
		if (rs.getStructure()!=null && geoContext.getStructureMapping(rs.getStructure()).getGeometryClass()==surfaceClass){
			if (rs instanceof FluxReaction){
				fluxList.addElement((FluxReaction)rs);
			}
		}
	}

	//
	// for each FluxReaction, add fluxes to "flux" if these fluxes are resolved
	//
	for (int i=0;i<fluxList.size();i++){
		FluxReaction fr = fluxList.elementAt(i);
		Flux[] fluxes = fr.getFluxes();
		for (int j = 0; j < fluxes.length; j++) {
			ResolvedFlux rf = null;
			SpeciesContext speciesContext = fluxes[j].getSpeciesContext();
			for (int k=0;k<resolvedFluxList.size();k++){
				ResolvedFlux rf_tmp = resolvedFluxList.elementAt(k);
				if (rf_tmp.getSpeciesContext() == fluxes[j].getSpeciesContext()){
					rf = rf_tmp;
				}
			}
			//
			// if speciesContext is not "fixed" and is mapped to a volume, add flux to ResolvedFlux
			//
			StructureMapping structureMapping = mathMapping.getSimulationContext().getGeometryContext().getStructureMapping(fluxes[j].getStructure());
			if (structureMapping.getGeometryClass()==surfaceClass){
				// flux within surface
				continue;
			}
			if (structureMapping.getGeometryClass() instanceof SubVolume && surfaceClass.isAdjacentTo((SubVolume)structureMapping.getGeometryClass())){
				SpeciesContextSpec speciesContextSpec = mathMapping.getSimulationContext().getReactionContext().getSpeciesContextSpec(speciesContext);
				if (!speciesContextSpec.isConstant()){
					if (rf == null){
						rf = new ResolvedFlux(speciesContext);
						resolvedFluxList.addElement(rf);
					}
					FeatureMapping featureMapping = (FeatureMapping)structureMapping;
					Expression insideFluxCorrection = Expression.invert(new Expression(featureMapping.getVolumePerUnitVolumeParameter(), mathMapping.getNameScope()));
					//
					// add flux term to ResolvedFlux.inFlux
					//
					if (fr.getKinetics() instanceof DistributedKinetics){
						KineticsParameter reactionRateParameter = ((DistributedKinetics)fr.getKinetics()).getReactionRateParameter();
						Expression correctedReactionRate = Expression.mult(new Expression(reactionRateParameter, mathMapping.getNameScope()),insideFluxCorrection);
						if (((Membrane)fr.getStructure()).getInsideFeature() == fluxes[j].getStructure()) {
							if (rf.getFlux().isZero()){
								rf.setFlux(correctedReactionRate.flatten());
							}else{
								rf.setFlux(Expression.add(rf.getFlux(),correctedReactionRate.flatten()));
							}
						} else {
							if (rf.getFlux().isZero()){
								rf.setFlux(Expression.negate(correctedReactionRate).flatten());
							} else {
								rf.setFlux(Expression.add(rf.getFlux(),Expression.negate(correctedReactionRate).flatten()));
							}
						}
					}else if (fr.getKinetics() instanceof LumpedKinetics){
						throw new RuntimeException("Lumped Kinetics for fluxes not yet supported");
					}else{
						throw new RuntimeException("unexpected Kinetic type in MembraneStructureAnalyzer.refreshResolvedFluxes()");
					}
					rf.getFlux().bindExpression(mathMapping);
				}
			}
		}
	}	
	//
	// for each reaction, incorporate all reactionSteps involving binding with volumetric species
	//
	for (int i=0;i<reactionSpecs.length;i++){
		if (reactionSpecs[i].isExcluded()){
			continue;
		}
		ReactionStep rs = reactionSpecs[i].getReactionStep();
		if (rs.getStructure()!=null && geoContext.getStructureMapping(rs.getStructure()).getGeometryClass()==surfaceClass){
			if (rs instanceof SimpleReaction){
				SimpleReaction sr = (SimpleReaction)rs;
				ReactionParticipant rp_Array[] = sr.getReactionParticipants();
				for (int k = 0; k < rp_Array.length; k++) {
					if (rp_Array[k] instanceof Reactant || rp_Array[k] instanceof Product){
						SpeciesContextSpec scs = mathMapping.getSimulationContext().getReactionContext().getSpeciesContextSpec(rp_Array[k].getSpeciesContext());
						StructureMapping sm = mathMapping.getSimulationContext().getGeometryContext().getStructureMapping(rp_Array[k].getStructure());
						//
						// for volume species that are not "fixed", add fluxes to "ResolvedFlux"
						//

						if (rs.getStructure() instanceof Membrane) {
							if (sm.getStructure() instanceof Feature && !scs.isConstant()){
								//
								// for each Reactant or Product binding to this membrane...
								//
	
								//
								// get ResolvedFlux for this species
								//
								ResolvedFlux rf = null;
								for (int j=0;j<resolvedFluxList.size();j++){
									ResolvedFlux rf_tmp = (ResolvedFlux)resolvedFluxList.elementAt(j);
									if (rf_tmp.getSpeciesContext() == rp_Array[k].getSpeciesContext()){
										rf = rf_tmp;
									}
								}
								if (rf == null){
									rf = new ResolvedFlux(rp_Array[k].getSpeciesContext());
									resolvedFluxList.addElement(rf);
								}
								
								if (sm.getGeometryClass() instanceof SubVolume && surfaceClass.isAdjacentTo((SubVolume)sm.getGeometryClass())) {
									//
									// for binding on inside or outside, add to ResolvedFlux.flux
									//
									FeatureMapping featureMapping = (FeatureMapping)sm;								
									Expression kmole = new Expression(mathMapping.getSimulationContext().getModel().getKMOLE(), mathMapping.getNameScope());
									Expression volFract = new Expression(featureMapping.getVolumePerUnitVolumeParameter(), mathMapping.getNameScope());
									Expression fluxCorrection = Expression.div(kmole, volFract).flatten(); 
									Expression reactionRateExpression = sr.getReactionRateExpression(rp_Array[k]).renameBoundSymbols(mathMapping.getNameScope());
									if (rf.getFlux().isZero()){
										rf.setFlux(Expression.mult(fluxCorrection,reactionRateExpression));
									}else{
										rf.setFlux(Expression.add(rf.getFlux(),Expression.mult(fluxCorrection,reactionRateExpression)));
									}
									rf.getFlux().bindExpression(mathMapping);
								} else if (sm.getGeometryClass() == getSurfaceClass()) {
									throw new Exception("In Application '" + mathMapping.getSimulationContext().getName() + "', membrane reaction with reactant in volume mapped to surface not yet implemented.");
								} else {
									String structureName = ((rs.getStructure()!=null)?(rs.getStructure().getName()):("<null>"));
									throw new Exception("In Application '" + mathMapping.getSimulationContext().getName() + "', SpeciesContext '"+rp_Array[k].getSpeciesContext().getName()+"' is not mapped adjacent to structure '"+structureName+"' but reacts there");
								}
							}
						} else {							
							throw new Exception("In Application '" + mathMapping.getSimulationContext().getName() + "', volume reaction mapped to surface not yet implemented.");
						}
					}
				}					
			}
		}
	}

	
	//
	// copy Vector into resolvedFluxes[] array
	//
	if (resolvedFluxList.size()>0){
		resolvedFluxes = new ResolvedFlux[resolvedFluxList.size()];
		resolvedFluxList.copyInto(resolvedFluxes);
	}else{
		resolvedFluxes=null;
	}
}
/**
 * Build list of structures (just one membrane) that are mapped to this volume subdomain
 */
public void refreshStructures() {
	structures = mathMapping.getSimulationContext().getGeometryContext().getStructuresFromGeometryClass(surfaceClass);
}
}
