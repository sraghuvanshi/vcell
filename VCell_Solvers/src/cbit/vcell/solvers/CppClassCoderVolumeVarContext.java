package cbit.vcell.solvers;
/*�
 * (C) Copyright University of Connecticut Health Center 2001.
 * All rights reserved.
�*/
import java.util.Enumeration;
import java.util.Vector;

import org.vcell.expression.ExpressionFactory;
import org.vcell.expression.IExpression;

import cbit.vcell.math.BoundaryConditionType;
import cbit.vcell.math.CompartmentSubDomain;
import cbit.vcell.math.Constant;
import cbit.vcell.math.Equation;
import cbit.vcell.math.Function;
import cbit.vcell.math.InsideVariable;
import cbit.vcell.math.JumpCondition;
import cbit.vcell.math.MemVariable;
import cbit.vcell.math.MembraneRegionVariable;
import cbit.vcell.math.MembraneSubDomain;
import cbit.vcell.math.OutsideVariable;
import cbit.vcell.math.PdeEquation;
import cbit.vcell.math.ReservedVariable;
import cbit.vcell.math.Variable;
import cbit.vcell.math.VolVariable;
import cbit.vcell.math.VolumeRegionVariable;
import cbit.vcell.simulation.Simulation;
/**
 * This class was generated by a SmartGuide.
 * 
 */
public class CppClassCoderVolumeVarContext extends CppClassCoderAbstractVarContext {
	protected MembraneSubDomain membraneSubDomainsOwned[] = new MembraneSubDomain[0];

/**
 * VarContextCppCoder constructor comment.
 * @param name java.lang.String
 */
protected CppClassCoderVolumeVarContext(CppCoderVCell argCppCoderVCell,
												Equation argEquation,
												CompartmentSubDomain argVolumeSubDomain,
												Simulation argSimulation, 
												String argParentClass) throws Exception
{
	super(argCppCoderVCell, argEquation, argVolumeSubDomain, argSimulation, argParentClass);
	Vector membraneSubDomainOwnedList = new Vector();
	MembraneSubDomain membranes[] = argSimulation.getMathDescription().getMembraneSubDomains(argVolumeSubDomain);
	for (int i = 0; i < membranes.length; i++){
		//
		// determine membrane "owner" for reasons of code generation (owner compartment is that which has a greater priority ... now this is arbitrary)
		//
		CompartmentSubDomain inside = membranes[i].getInsideCompartment();
		CompartmentSubDomain outside = membranes[i].getOutsideCompartment();
		CompartmentSubDomain membraneOwner = null;
		if (inside.getPriority() > outside.getPriority()){
			membraneOwner = inside;
		}else if (inside.getPriority() < outside.getPriority()){
			membraneOwner = outside;
		}else{ // inside.getPriority() == outside.getPriority()
			throw new RuntimeException("CompartmentSubDomains '"+inside.getName()+"' and '"+outside.getName()+"' have same priority ("+inside.getPriority()+")");
		}
		if (membraneOwner == argVolumeSubDomain){
			membraneSubDomainOwnedList.add(membranes[i]);
		}
	}
	this.membraneSubDomainsOwned = (MembraneSubDomain[])org.vcell.util.BeanUtils.getArray(membraneSubDomainOwnedList,MembraneSubDomain.class);
}


/**
 * Insert the method's description here.
 * Creation date: (6/22/2004 3:22:18 PM)
 * @return cbit.vcell.math.CompartmentSubDomain
 */
public CompartmentSubDomain getCompartmentSubDomain() {
	return (CompartmentSubDomain)getSubDomain();
}


/**
 * Insert the method's description here.
 * Creation date: (6/22/2004 3:07:51 PM)
 * @return cbit.vcell.math.Variable[]
 */
protected Variable[] getRequiredVariables() throws Exception {

	//
	// 
	//
	Variable requiredVariables[] = super.getRequiredVariables();
	if (getEquation() instanceof PdeEquation){
		for (int i = 0;membraneSubDomainsOwned!=null && i < membraneSubDomainsOwned.length; i++){
			JumpCondition jumpCondition = membraneSubDomainsOwned[i].getJumpCondition((VolVariable)getEquation().getVariable());
			Enumeration enumJC = jumpCondition.getRequiredVariables(getSimulation().getMathDescription());
			requiredVariables = (Variable[])org.vcell.util.BeanUtils.addElements(requiredVariables,(Variable[])org.vcell.util.BeanUtils.getArray(enumJC,Variable.class));
		}
	}
	Vector uniqueVarList = new Vector();
	for (int i = 0; i < requiredVariables.length; i++){
		Variable var = requiredVariables[i];
		if (var instanceof InsideVariable){
			InsideVariable insideVar = (InsideVariable)var;
			VolVariable volVar = (VolVariable)getSimulation().getVariable(insideVar.getVolVariableName());
			if (!uniqueVarList.contains(volVar)){
				uniqueVarList.addElement(volVar);
			}	
		}else if (var instanceof OutsideVariable){
			OutsideVariable outsideVar = (OutsideVariable)var;
			VolVariable volVar = (VolVariable)getSimulation().getVariable(outsideVar.getVolVariableName());
			if (!uniqueVarList.contains(volVar)){
				uniqueVarList.addElement(volVar);
			}
		}else{
			if (!uniqueVarList.contains(var)){
				uniqueVarList.addElement(var);
			}
		}
	}

	return (Variable[])org.vcell.util.BeanUtils.getArray(uniqueVarList,Variable.class);
}


/**
 * This method was created by a SmartGuide.
 * @param out java.io.PrintWriter
 */
protected void writeConstructor(java.io.PrintWriter out) throws Exception {
	out.println(getClassName()+"::"+getClassName()+"(Feature *Afeature,CString AspeciesName)");
	out.println(": "+getParentClassName()+"(Afeature,AspeciesName)");
	out.println("{");
	try {
		IExpression ic = getEquation().getInitialExpression();
		ic.bindExpression(getSimulation());
		double value = ic.evaluateConstant();
		out.println("   initialValue = new double;");
		out.println("   *initialValue = "+value+";");
	}catch (Exception e){
		out.println("   initialValue = NULL;");
	}	
	if (getEquation() instanceof PdeEquation){
		try {
			IExpression Dexp = ((PdeEquation)getEquation()).getDiffusionExpression();
			Dexp.bindExpression(getSimulation());
			double value = Dexp.evaluateConstant();
			out.println("   diffusionRate = new double;");
			out.println("   *diffusionRate = "+value+";");
		}catch (Exception e){
			out.println("   diffusionRate = NULL;");
		}
	}else{	
		out.println("   diffusionRate = NULL;");
	}	
	out.println("");

	Variable requiredVariables[] = getRequiredVariables();
	for (int i = 0; i < requiredVariables.length; i++){
		Variable var = requiredVariables[i];
		if (var instanceof VolVariable){
			out.println("    var_"+var.getName()+" = NULL;");
		}else if (var instanceof MemVariable){
			out.println("    var_"+var.getName()+" = NULL;");
		}else if (var instanceof MembraneRegionVariable){
			out.println("    var_"+var.getName()+" = NULL;");
		}else if (var instanceof VolumeRegionVariable){
			out.println("    var_"+var.getName()+" = NULL;");
		}else if (var instanceof InsideVariable){
		}else if (var instanceof OutsideVariable){
		}else if (var instanceof ReservedVariable){
		}else if (var instanceof Constant){
		}else if (var instanceof Function){
		}else{
			throw new Exception("unknown identifier type for identifier: "+var.getName());
		}	
	}		  	
	out.println("}");
}


/**
 * This method was created by a SmartGuide.
 * @param printWriter java.io.PrintWriter
 */
public void writeDeclaration(java.io.PrintWriter out) throws Exception {
	out.println("//---------------------------------------------");
	out.println("//  class " + getClassName());
	out.println("//---------------------------------------------");

	out.println("class " + getClassName() + " : public " + getParentClassName());
	out.println("{");
	out.println(" public:");
	out.println("    "+getClassName() + "(Feature *feature, CString speciesName);");
	out.println("    virtual boolean resolveReferences(Simulation *sim);");

	BoundaryConditionType bc = null;
	int dimension = getSimulation().getMathDescription().getGeometry().getDimension();
	if (getEquation() instanceof PdeEquation){
		PdeEquation pdeEqu = (PdeEquation)getEquation();
		if (pdeEqu.getBoundaryXm()!=null){
			bc = getCompartmentSubDomain().getBoundaryConditionXm();
			if (bc.isDIRICHLET()){
				out.println("    virtual double getXmBoundaryValue(long volumeIndex);");
			}else if (bc.isNEUMANN()){
				out.println("    virtual double getXmBoundaryFlux(long volumeIndex);");
			}
		}
		if (pdeEqu.getBoundaryXp()!=null){			
			bc = getCompartmentSubDomain().getBoundaryConditionXp();
			if (bc.isDIRICHLET()){
				out.println("    virtual double getXpBoundaryValue(long volumeIndex);");
			}else if (bc.isNEUMANN()){
				out.println("    virtual double getXpBoundaryFlux(long volumeIndex);");
			}
		}
		if (pdeEqu.getVelocityX() != null) {
			out.println("\tvirtual double getConvectionVelocity_X(long index);");
		}
		if (dimension>1){
			if (pdeEqu.getBoundaryYm()!=null){
				bc = getCompartmentSubDomain().getBoundaryConditionYm();
				if (bc.isDIRICHLET()){
					out.println("    virtual double getYmBoundaryValue(long volumeIndex);");
				}else if (bc.isNEUMANN()){
					out.println("    virtual double getYmBoundaryFlux(long volumeIndex);");
				}
			}	
			if (pdeEqu.getBoundaryYp()!=null){
				bc = getCompartmentSubDomain().getBoundaryConditionYp();
				if (bc.isDIRICHLET()){
					out.println("    virtual double getYpBoundaryValue(long volumeIndex);");
				}else if (bc.isNEUMANN()){
					out.println("    virtual double getYpBoundaryFlux(long volumeIndex);");
				}
			}
			if (pdeEqu.getVelocityY() != null) {
				out.println("\tvirtual double getConvectionVelocity_Y(long index);");
			}			
		}
		if (dimension==3){	
			if (pdeEqu.getBoundaryZm()!=null){
				bc = getCompartmentSubDomain().getBoundaryConditionZm();
				if (bc.isDIRICHLET()){
					out.println("    virtual double getZmBoundaryValue(long volumeIndex);");
				}else if (bc.isNEUMANN()){
					out.println("    virtual double getZmBoundaryFlux(long volumeIndex);");
				}
			}	
			if (pdeEqu.getBoundaryZp()!=null){
				bc = getCompartmentSubDomain().getBoundaryConditionZp();
				if (bc.isDIRICHLET()){
					out.println("    virtual double getZpBoundaryValue(long volumeIndex);");
				}else if (bc.isNEUMANN()){
					out.println("    virtual double getZpBoundaryFlux(long volumeIndex);");
				}
			}
			if (pdeEqu.getVelocityZ() != null) {
				out.println("\tvirtual double getConvectionVelocity_Z(long index);");
			}			
		}	
	}		
	try {
		IExpression ic = getEquation().getInitialExpression();
		ic.bindExpression(getSimulation());
		double value = ic.evaluateConstant();
	}catch (Exception e){
		out.println("    virtual double getInitialValue(long volumeIndex);");
	}
	if (getEquation() instanceof PdeEquation){
		try {
			IExpression Dexp = ((PdeEquation)getEquation()).getDiffusionExpression();
			Dexp.bindExpression(getSimulation());
			double value = Dexp.evaluateConstant();
		}catch (Exception e){
			out.println("    virtual double getDiffusionRate(long volumeIndex);");
		}
	}
	out.println(" protected:");
	out.println("    virtual double getReactionRate(long volumeIndex);");
	out.println("    virtual void getFlux(MembraneElement *element,double *inFlux, double *outFlux);");
	out.println(" private:");
	Variable requiredVariables[] = getRequiredVariables();
	for (int i = 0; i < requiredVariables.length; i++){
		Variable var = requiredVariables[i];
		if (var instanceof VolVariable){
			out.println("    VolumeVariable      *var_"+var.getName()+";");
		}else if (var instanceof MemVariable){
			out.println("    MembraneVariable    *var_"+var.getName()+";");
		}else if (var instanceof MembraneRegionVariable){
			out.println("    MembraneRegionVariable    *var_"+var.getName()+";");
		}else if (var instanceof VolumeRegionVariable){
			out.println("    VolumeRegionVariable    *var_"+var.getName()+";");
		}else if (var instanceof ReservedVariable){
		}else if (var instanceof Constant){
		}else if (var instanceof Function){
		}else{
			throw new Exception("unknown identifier type '"+var.getClass().getName()+"' for identifier: "+var.getName());
		}	
	}		  	
	out.println("};");
}


/**
 * This method was created by a SmartGuide.
 * @param out java.io.PrintWriter
 */
protected void writeGetFlux(java.io.PrintWriter out, String functionName) throws Exception {
	//
	// Explanation of PRIORITIES and INSIDE/OUTSIDE wrt Code Generation:
	// -----------------------------------------------------------------
	//
	// due to code generation requirements, the compartment with the higher priority must be the "inside" compartment
	// and the "inside" compartment is where the flux is defined in the C++ library.
	//
	//
	// The math description specifies "inside" and "outside" compartments locally for each membrane
	//
	//     MembraneSubDomain inside_compartment outside_compartment {
	//         ...
	//     }
	//
	// which can contradict the priority-based determination of inside-outside.
	//
	// in these cases:
	//   1) the "influx" and "outflux" expressions must be reversed, and
	//   2) the var_INSIDE and var_OUTSIDE variable definitions must be exchanged (substituted)
	//
	
	out.println("void "+getClassName()+"::"+functionName+"(MembraneElement *element,double *inFlux, double *outFlux)");
	out.println("{");
	
	if (getEquation() instanceof PdeEquation){

		//
		// if zero or one membranes, write out single inFlux/outFlux expression
		//
		if (membraneSubDomainsOwned.length==0){
			out.println("   *inFlux = 0.0;");
			out.println("   *outFlux = 0.0;");
		}else if (membraneSubDomainsOwned.length==1){
			boolean bFlipInsideOutside = (membraneSubDomainsOwned[0].getOutsideCompartment() == getCompartmentSubDomain());
			out.println("   // for this membrane, MathDescription defines inside='"+membraneSubDomainsOwned[0].getInsideCompartment().getName()+"', outside='"+membraneSubDomainsOwned[0].getOutsideCompartment().getName()+"'");
			out.println("   // '"+membraneSubDomainsOwned[0].getInsideCompartment().getName()+"' has priority="+membraneSubDomainsOwned[0].getInsideCompartment().getPriority()+", "+
			                  "'"+membraneSubDomainsOwned[0].getOutsideCompartment().getName()+"' has priority="+membraneSubDomainsOwned[0].getOutsideCompartment().getPriority());
			if (bFlipInsideOutside){
				out.println("   // **** relative priorities CONTRADICT MathDescription convension (insidePriority < outsidePriority) ... must flip definitions");
			}else{
				out.println("   // :-)  Priorities are consistent (insidePriority > outsidePriority)");
			}
			out.println("");
			
			JumpCondition jumpCondition = membraneSubDomainsOwned[0].getJumpCondition((VolVariable)getVariable());
			IExpression inFluxExp = jumpCondition.getInFluxExpression();
			IExpression outFluxExp = jumpCondition.getOutFluxExpression();
			IExpression inFluxExp_substituted = getSimulation().substituteFunctions(inFluxExp).flatten();
			IExpression outFluxExp_substituted = getSimulation().substituteFunctions(outFluxExp).flatten();
			//
			// get totalExpression (composite expression to combine symbols)
			// then write out dependencies
			//
			IExpression totalExpression = ExpressionFactory.add(inFluxExp_substituted, outFluxExp_substituted);
			writeMembraneFunctionDeclarations(out,"element",totalExpression,bFlipInsideOutside,"   ");
			if (bFlipInsideOutside){
				out.println("   *inFlux = "+outFluxExp_substituted.infix_C()+";  // *****  flux convension reversed, uses 'outFlux' from MathDescription");
				out.println("   *outFlux = "+inFluxExp_substituted.infix_C()+";  // *****  flux convension reversed, uses 'inFlux' from MathDescription");
			}else{
				out.println("   *inFlux = "+inFluxExp_substituted.infix_C()+";");
				out.println("   *outFlux = "+outFluxExp_substituted.infix_C()+";");
			}
		}else if (membraneSubDomainsOwned.length>1){
			//
			// must choose which membrane at runtime
			//
			out.println("   Feature *outsideFeature = element->region->getRegionOutside()->getFeature();");
			out.println("   int outsideHandle = outsideFeature->getHandle();");
			out.println("   Feature *insideFeature = element->region->getRegionInside()->getFeature();");
			out.println("   int insideHandle = insideFeature->getHandle();");
			//out.println("   printf(\"getFlux(index=%ld, insideHandle=%d, outsideHandle=%ld), MembraneElement outside feature = '%s'\\n\",element->index,insideHandle,outsideHandle,outsideFeature->getName());");
			out.println("   switch(outsideHandle){");
			for (int i = 0; i < membraneSubDomainsOwned.length; i++){
				cbit.vcell.geometry.GeometrySpec geoSpec = getSimulation().getMathDescription().getGeometry().getGeometrySpec();
				boolean bFlipInsideOutside = (membraneSubDomainsOwned[i].getOutsideCompartment() == getCompartmentSubDomain());
				cbit.vcell.geometry.SubVolume outsideSubVolume = null;
				if (bFlipInsideOutside){
					outsideSubVolume = geoSpec.getSubVolume(membraneSubDomainsOwned[i].getInsideCompartment().getName());
				}else{
					outsideSubVolume = geoSpec.getSubVolume(membraneSubDomainsOwned[i].getOutsideCompartment().getName());
				}
				out.println("      case "+outsideSubVolume.getHandle()+": {  // for outside subVolume '"+outsideSubVolume.getName()+"'");
				out.println("         // for this membrane, MathDescription defines inside='"+membraneSubDomainsOwned[i].getInsideCompartment().getName()+"', outside='"+membraneSubDomainsOwned[i].getOutsideCompartment().getName()+"'");
				out.println("         // '"+membraneSubDomainsOwned[i].getInsideCompartment().getName()+"' has priority="+membraneSubDomainsOwned[i].getInsideCompartment().getPriority()+", "+
				                        "'"+membraneSubDomainsOwned[i].getOutsideCompartment().getName()+"' has priority="+membraneSubDomainsOwned[i].getOutsideCompartment().getPriority());
				if (bFlipInsideOutside){
					out.println("         // **** relative priorities CONTRADICT MathDescription convension (insidePriority < outsidePriority) ... must flip definitions");
				}else{
					out.println("         // :-)  Priorities are consistent (insidePriority > outsidePriority)");
				}
				out.println("");
				
				JumpCondition jumpCondition = membraneSubDomainsOwned[i].getJumpCondition((VolVariable)getVariable());
				IExpression inFluxExp = jumpCondition.getInFluxExpression();
				IExpression outFluxExp = jumpCondition.getOutFluxExpression();
				IExpression inFluxExp_substituted = getSimulation().substituteFunctions(inFluxExp).flatten();
				IExpression outFluxExp_substituted = getSimulation().substituteFunctions(outFluxExp).flatten();
				
				//
				// get totalExpression (composite expression to combine symbols)
				// then write out dependencies
				//
				IExpression totalExpression = ExpressionFactory.add(inFluxExp_substituted, outFluxExp_substituted);
				writeMembraneFunctionDeclarations(out,"element",totalExpression,bFlipInsideOutside,"         ");
				if (bFlipInsideOutside){
					out.println("         *inFlux = "+outFluxExp_substituted.infix_C()+";  // *****  flux convension reversed, uses 'outFlux' from MathDescription");
					out.println("         *outFlux = "+inFluxExp_substituted.infix_C()+";  // *****  flux convension reversed, uses 'inFlux' from MathDescription");
				}else{
					out.println("         *inFlux = "+inFluxExp_substituted.infix_C()+";");
					out.println("         *outFlux = "+outFluxExp_substituted.infix_C()+";");
				}
				out.println("         break;");
				out.println("      }");
			}
			out.println("      default: {");
			out.println("         printf(\"getFlux(index=%ld, insideHandle=%d, outsideHandle=%ld), MembraneElement outside feature = '%s'\\n\",element->index,insideHandle,outsideHandle,outsideFeature->getName());");
			out.println("         throw \"failed to match feature handle in "+getClassName()+"::"+functionName+"\";");
			out.println("      }");
			out.println("    }");
		}
	}else{
		out.println("   *inFlux = 0.0;");
		out.println("   *outFlux = 0.0;");
	}
	out.println("}");
}


/**
 * This method was created by a SmartGuide.
 * @param printWriter java.io.PrintWriter
 */
public void writeImplementation(java.io.PrintWriter out) throws Exception {
	out.println("//---------------------------------------------");
	out.println("//  class " + getClassName());
	out.println("//---------------------------------------------");
	writeConstructor(out);
	out.println("");
	writeResolveReferences(out);
	out.println("");
	writeVolumeFunction(out,"getReactionRate", getEquation().getRateExpression());
	out.println("");
	writeGetFlux(out,"getFlux");
	out.println("");
	int dimension = getSimulation().getMathDescription().getGeometry().getDimension();
	if (getEquation() instanceof PdeEquation){
		PdeEquation pde = (PdeEquation)getEquation();
		BoundaryConditionType bc = getCompartmentSubDomain().getBoundaryConditionXm();
		if (bc != null && (pde.getBoundaryXm()!=null)){
			if (bc.isDIRICHLET()){
				writeVolumeFunction(out,"getXmBoundaryValue",pde.getBoundaryXm());
			}else if (bc.isNEUMANN()){
				writeVolumeFunction(out,"getXmBoundaryFlux", pde.getBoundaryXm());
			}
		}	
		bc = getCompartmentSubDomain().getBoundaryConditionXp();
		if (bc != null && (pde.getBoundaryXp()!=null)){
			if (bc.isDIRICHLET()){
				writeVolumeFunction(out,"getXpBoundaryValue",pde.getBoundaryXp());
			}else if (bc.isNEUMANN()){
				writeVolumeFunction(out,"getXpBoundaryFlux", pde.getBoundaryXp());
			}
		}
		if (pde.getVelocityX() != null) {
			writeVolumeFunction(out,"getConvectionVelocity_X",pde.getVelocityX());
		}
		if (dimension>1){
			bc = getCompartmentSubDomain().getBoundaryConditionYm();
			if (bc != null && (pde.getBoundaryYm()!=null)){
				if (bc.isDIRICHLET()){
					writeVolumeFunction(out,"getYmBoundaryValue",pde.getBoundaryYm());
				}else if (bc.isNEUMANN()){
					writeVolumeFunction(out,"getYmBoundaryFlux", pde.getBoundaryYm());
				}
			}	
			bc = getCompartmentSubDomain().getBoundaryConditionYp();
			if (bc != null && (pde.getBoundaryYp()!=null)){
				if (bc.isDIRICHLET()){
					writeVolumeFunction(out,"getYpBoundaryValue",pde.getBoundaryYp());
				}else if (bc.isNEUMANN()){
					writeVolumeFunction(out,"getYpBoundaryFlux", pde.getBoundaryYp());
				}
			}
			if (pde.getVelocityY() != null) {
				writeVolumeFunction(out,"getConvectionVelocity_Y",pde.getVelocityY());
			}			
		}
		if (dimension==3){		
			bc = getCompartmentSubDomain().getBoundaryConditionZm();
			if (bc != null && (pde.getBoundaryZm()!=null)){
				if (bc.isDIRICHLET()){
					writeVolumeFunction(out,"getZmBoundaryValue",pde.getBoundaryZm());
				}else if (bc.isNEUMANN()){
					writeVolumeFunction(out,"getZmBoundaryFlux", pde.getBoundaryZm());
				}
			}	
			bc = getCompartmentSubDomain().getBoundaryConditionZp();
			if (bc != null && (pde.getBoundaryZp()!=null)){
				if (bc.isDIRICHLET()){
					writeVolumeFunction(out,"getZpBoundaryValue",pde.getBoundaryZp());
				}else if (bc.isNEUMANN()){
					writeVolumeFunction(out,"getZpBoundaryFlux", pde.getBoundaryZp());
				}
			}
			if (pde.getVelocityZ() != null) {
				writeVolumeFunction(out,"getConvectionVelocity_Z",pde.getVelocityZ());
			}						
		}		
	}	
	try {
		double value = getEquation().getInitialExpression().evaluateConstant();
	}catch (Exception e){
		writeVolumeFunction(out,"getInitialValue", getEquation().getInitialExpression());
	}
	if (getEquation() instanceof PdeEquation){
		PdeEquation pde = (PdeEquation)getEquation();
		try {
			double value = pde.getDiffusionExpression().evaluateConstant();
		}catch (Exception e){
			writeVolumeFunction(out,"getDiffusionRate", pde.getDiffusionExpression());
		}
	}
	out.println("");
}
}