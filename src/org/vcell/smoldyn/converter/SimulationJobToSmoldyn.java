package org.vcell.smoldyn.converter;


import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import org.vcell.smoldyn.model.Geometryable;
import org.vcell.smoldyn.model.Model;
import org.vcell.smoldyn.model.Model.Dimensionality;
import org.vcell.smoldyn.model.SpeciesState.StateType;
import org.vcell.smoldyn.model.util.Point;
import org.vcell.smoldyn.model.util.Triangle;
import org.vcell.smoldyn.model.util.Point.PointFactory;
import org.vcell.smoldyn.simulation.Simulation;
import org.vcell.smoldyn.simulationsettings.InternalSettings;
import org.vcell.smoldyn.simulationsettings.SimulationSettings;
import org.vcell.smoldyn.simulationsettings.SmoldynTime;
import org.vcell.smoldyn.simulationsettings.VCellObservationEvent.VCellEventType;
import org.vcell.smoldyn.simulationsettings.util.EventTiming;
import org.vcell.solver.smoldyn.SmoldynSimulationOptions;
import org.vcell.util.BeanUtils;
import org.vcell.util.Coordinate;
import org.vcell.util.Extent;
import org.vcell.util.ISize;
import cbit.vcell.geometry.Geometry;
import cbit.vcell.geometry.SubVolume;
import cbit.vcell.geometry.surface.GeometricRegion;
import cbit.vcell.geometry.surface.GeometrySurfaceDescription;
import cbit.vcell.geometry.surface.Node;
import cbit.vcell.geometry.surface.Polygon;
import cbit.vcell.geometry.surface.Surface;
import cbit.vcell.geometry.surface.SurfaceCollection;
import cbit.vcell.geometry.surface.SurfaceGeometricRegion;
import cbit.vcell.geometry.surface.VolumeGeometricRegion;
import cbit.vcell.math.Action;
import cbit.vcell.math.CompartmentSubDomain;
import cbit.vcell.math.MacroscopicRateConstant;
import cbit.vcell.math.MathDescription;
import cbit.vcell.math.MembraneSubDomain;
import cbit.vcell.math.ParticleJumpProcess;
import cbit.vcell.math.ParticleProbabilityRate;
import cbit.vcell.math.ParticleProperties;
import cbit.vcell.math.SubDomain;
import cbit.vcell.math.Variable;
import cbit.vcell.math.ParticleProperties.ParticleInitialCondition;
import cbit.vcell.parser.Expression;
import cbit.vcell.solver.OutputTimeSpec;
import cbit.vcell.solver.SimulationJob;
import cbit.vcell.solver.TimeBounds;
import cbit.vcell.solver.TimeStep;
import cbit.vcell.solver.UniformOutputTimeSpec;



/**
 * @author mfenwick
 *
 */
public class SimulationJobToSmoldyn {

	private MathDescription mathd;
	private Simulation smoldynsimulation;
	private Model smoldynmodel;
	private SimulationJob vcellSimJob;
	private SimulationSettings smoldynsimulationsettings;
	private File outputFile;
	
	
	public SimulationJobToSmoldyn(SimulationJob vcellSimJob, File outputFile) throws Exception {
		this.vcellSimJob = vcellSimJob;
		this.outputFile = outputFile;
		this.mathd = vcellSimJob.getSimulation().getMathDescription();
		
		try {
			final int vcelldimensions = vcellSimJob.getSimulation().getMathDescription().getGeometry().getDimension();
			if(vcelldimensions != 3) {
				ConversionUtilities.throwRuntimeException("vcellsmoldyn needs a three dimensional model to work properly (received " + 
						vcelldimensions + ")");
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		this.smoldynmodel = new Model(Dimensionality.three, 
				new org.vcell.smoldyn.model.Geometry(ConversionUtilities.getBoundariesFromVCell(mathd.getGeometry())));
		smoldynsimulationsettings = new SimulationSettings();
		this.smoldynsimulation = new Simulation(smoldynmodel, smoldynsimulationsettings);
		this.convert();
	}
	
	
	private void convert() {
		this.setSmoldynGeometry();
		this.setSpecies();
		this.setSpeciesStates();
		this.setParticlesInitialConditions();
		this.setReactions();
		this.setTime();
		this.setOutput();
		this.setInternalSettings();
	}

	private Simulation getSimulation() {
		return this.smoldynsimulation;
	}
	
	public static Simulation convertSimulationJob(SimulationJob vcellSimJob, File outputFile) {
		SimulationJobToSmoldyn s;
		try {
			s = new SimulationJobToSmoldyn(vcellSimJob, outputFile);
			return s.getSimulation();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	private void setSmoldynGeometry() {
		Geometry origvcellgeometry = vcellSimJob.getSimulation().getMathDescription().getGeometry();
		Geometry vcellGeometry = null;
		// clone and resample geometry
		try {
			vcellGeometry = (Geometry) BeanUtils.cloneSerializable(origvcellgeometry);
			GeometrySurfaceDescription geoSurfaceDesc = vcellGeometry.getGeometrySurfaceDescription();
			ISize newSize = vcellSimJob.getSimulation().getMeshSpecification().getSamplingSize();
			geoSurfaceDesc.setVolumeSampleSize(newSize);
			geoSurfaceDesc.updateAll();		
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		
		Geometryable smoldyngeometry = smoldynmodel.getGeometry();

		GeometricRegion[] geometricRegions = vcellGeometry.getGeometrySurfaceDescription().getGeometricRegions();
		ArrayList<SurfaceGeometricRegion> surfaceRegions = new ArrayList<SurfaceGeometricRegion>();
		ArrayList<VolumeGeometricRegion> volumeRegions = new ArrayList<VolumeGeometricRegion>();
		for (int i = 0; i < geometricRegions.length; i++) {
			if (geometricRegions[i] instanceof SurfaceGeometricRegion){
				surfaceRegions.add((SurfaceGeometricRegion)geometricRegions[i]);
			}else if (geometricRegions[i] instanceof VolumeGeometricRegion){
				volumeRegions.add((VolumeGeometricRegion)geometricRegions[i]);
			} else {
				ConversionUtilities.printWarning("huh?");
			}
		}
		
		for(SurfaceGeometricRegion sgr : surfaceRegions.toArray(new SurfaceGeometricRegion [surfaceRegions.size()])) {
			addSmoldynSurface(sgr, vcellGeometry, smoldyngeometry);			
		}
		for(VolumeGeometricRegion vgr : volumeRegions.toArray(new VolumeGeometricRegion [volumeRegions.size()])) {
			addSmoldynCompartment(vgr, vcellGeometry, smoldyngeometry);			
		}
	}

	private void addSmoldynSurface(GeometricRegion gr, Geometry vcellgeometry, Geometryable smoldyngeometry) {
		GeometrySurfaceDescription geoSurfaceDescription = vcellgeometry.getGeometrySurfaceDescription();
		smoldyngeometry.addSurface(gr.getName());
		VolumeGeometricRegion insideVolRegion = (VolumeGeometricRegion) gr.getAdjacentGeometricRegions()[0];
		org.vcell.smoldyn.model.Surface smoldynsurface = smoldyngeometry.getSurface(gr.getName());
		addPanels(smoldynsurface, insideVolRegion.getRegionID(), geoSurfaceDescription);
		ConversionUtilities.printWarning("added surface of name: " + gr.getName());
	}
	
	private void addPanels(org.vcell.smoldyn.model.Surface smoldynsurface, Integer volRegionID, 
			GeometrySurfaceDescription geometrySurfaceDescription){
		SurfaceCollection surfaceCollection = geometrySurfaceDescription.getSurfaceCollection();
		for(int j = 0; j < surfaceCollection.getSurfaceCount(); j++) {
			Surface surface = surfaceCollection.getSurfaces(j);
			for(int k = 0; k < surface.getPolygonCount(); k++) {
				Polygon polygon = surface.getPolygons(k);
				Node [] nodes = polygon.getNodes();
				Boolean interior = null;
				if(surface.getInteriorRegionIndex() == volRegionID) {
					interior = true;
				} else if(surface.getExteriorRegionIndex() == volRegionID) {
					interior = false;
				}
				if (interior == true) {
					addTriangle(smoldynsurface, new Node [] {nodes[0], nodes[1], nodes[2]}, interior);
					if(nodes.length == 4) {
						addTriangle(smoldynsurface, new Node [] {nodes[0], nodes[2], nodes[3]}, interior);
					}
				} else if (interior == false) {
					addTriangle(smoldynsurface, new Node [] {nodes[2], nodes[1], nodes[0]}, interior);
					if(nodes.length == 4) {
						addTriangle(smoldynsurface, new Node [] {nodes[3], nodes[2], nodes[0]}, interior);
					}
				} else {//interior is null
					ConversionUtilities.throwRuntimeException("neither interior nor exterior???");						
				}
			}
		}
	}
	
	private void addTriangle(org.vcell.smoldyn.model.Surface smoldynsurface, Node [] nodes, Boolean interior) {
		Point [] points = new Point [3];
		PointFactory pf = this.smoldynmodel.getPointFactory();
		points[0] = pf.getNewPoint(nodes[0].getX(), nodes[0].getY(), nodes[0].getZ());
		points[1] = pf.getNewPoint(nodes[1].getX(), nodes[1].getY(), nodes[1].getZ());
		points[2] = pf.getNewPoint(nodes[2].getX(), nodes[2].getY(), nodes[2].getZ());
		smoldynsurface.addPanel(new Triangle(null, points[0], points[1], points[2]));
	}
	
	private void addSmoldynCompartment(VolumeGeometricRegion volumegr, Geometry vcellgeometry, Geometryable smoldyngeometry) {
		GeometrySurfaceDescription geoSurfaceDescription = vcellgeometry.getGeometrySurfaceDescription();
		SubVolume subVolume = (SubVolume)geoSurfaceDescription.getGeometryClass(volumegr);
		GeometricRegion[] adjacentRegions = volumegr.getAdjacentGeometricRegions();
		ArrayList<String> boundingsurfacenames = new ArrayList<String>();
		for (int j = 0; j < adjacentRegions.length; j++) {
			if (adjacentRegions[j] instanceof SurfaceGeometricRegion){
				boundingsurfacenames.add(adjacentRegions[j].getName()/*surfaceClass.getName()*/);
			}
		}
		Coordinate coord = ConversionUtilities.getAnyCoordinate(vcellgeometry, volumegr);
		smoldyngeometry.addCompartment(subVolume.getName(), boundingsurfacenames.toArray(new String [boundingsurfacenames.size()]), 
				new Point [] {convertCoordinateToPoint(coord)});
	}	
	
	private Point convertCoordinateToPoint(Coordinate coordinate) {
		PointFactory pf = this.smoldynmodel.getPointFactory();
		Point point = pf.getNewPoint(coordinate.getX(), coordinate.getY(), coordinate.getZ());
		return point;
	}

	
	private void setSpecies() {
		Enumeration<Variable> variables = mathd.getVariables();
		while(variables.hasMoreElements()) {
			smoldynmodel.addSpecies(variables.nextElement().getName());
		}
	}
	
	
	private void setSpeciesStates() {
		//TODO for surfaces
	}
	
	/**
	 * 
	 */
	private void setParticlesInitialConditions() {
		Enumeration<SubDomain> subdomains = mathd.getSubDomains();
		while(subdomains.hasMoreElements()) {
			SubDomain subdomain = subdomains.nextElement();
			if(subdomain instanceof MembraneSubDomain) {
				ConversionUtilities.printWarning("sorry, translation of surface molecules is not supported");
				continue;
			}
			setInitialConditionsDiffusion(subdomain.getParticleProperties(), subdomain.getName());
		}
	}
	
	/**
	 * 
	 * @param props
	 * @param subdomainname
	 */
	private void setInitialConditionsDiffusion(List<ParticleProperties> props, String subdomainname) {
		for(ParticleProperties partprops : props) {
			try {
				this.smoldynmodel.addSpeciesState(partprops.getVariable().getName(), StateType.solution, 
					Double.valueOf(partprops.getDiffusion().infix()));
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
			setInitialLocations(partprops.getParticleInitialConditions(), subdomainname, partprops.getVariable().getName());
		}
	}
	
	/**
	 * 
	 * @param init
	 * @param subdomainname
	 * @param variablename
	 */
	private void setInitialLocations(ArrayList<ParticleInitialCondition> init, String subdomainname, String variablename) {
		PointFactory pf = this.smoldynmodel.getPointFactory();
		for(ParticleInitialCondition partinit : init) {
			double whatever = Double.valueOf(partinit.getCount().infix());
			this.smoldynmodel.addVolumeMolecule(subdomainname, variablename, pf.getNewPoint(getDouble(partinit.getLocationX()), 
				getDouble(partinit.getLocationY()), getDouble(partinit.getLocationZ())), (int) whatever);
		}
	}
	
	private Double getDouble(Expression e) {
		if(e == null) {
			return null;
		}
		String s = e.infix();
		if(s.equals("u")) {
			return null;
		}
		return Double.valueOf(s);
	}
	
	
	private void setReactions() {
		Enumeration<SubDomain> subdomains = mathd.getSubDomains();
		while(subdomains.hasMoreElements()) {
			SubDomain subdomain = subdomains.nextElement();
			for(ParticleJumpProcess pjp : subdomain.getParticleJumpProcesses().toArray(
					new ParticleJumpProcess [subdomain.getParticleJumpProcesses().size()])) {
				String name = pjp.getName();
				String [] reactants = new String [2], products = new String [2];
				int reactindex = 0, productindex = 0;
				for(Action a : pjp.getActions().toArray(new Action [pjp.getActions().size()])) {
					if(a.getOperation().equals(Action.ACTION_CREATE)) {
						products[reactindex++] = a.getVar().getName();
					} else if(a.getOperation().equals(Action.ACTION_DESTROY)) {
						reactants[productindex++] = a.getVar().getName();
					} else {
						ConversionUtilities.printWarning("skipping action due to problem (unexpected operation): " + a.getOperation());
					}
				}
				double rateconstant = 0;
				try {
					ParticleProbabilityRate ppr = pjp.getParticleProbabilityRate();
					if(ppr instanceof MacroscopicRateConstant) {
						rateconstant = ((MacroscopicRateConstant) ppr).getExpression().evaluateConstant();
					} else {
						ConversionUtilities.throwRuntimeException("particle probability rate not supported");
					}
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
				if(subdomain instanceof CompartmentSubDomain) {
					String compartname = subdomain.getName();
					smoldynmodel.addVolumeReaction(name, compartname, reactants[0], reactants[1], products[0], products[1], 
							(float) rateconstant);
				} else if (subdomain instanceof MembraneSubDomain){
					String surfacename = subdomain.getName();
					smoldynmodel.addSurfaceReaction(name, surfacename, reactants[0], StateType.back, reactants[1], StateType.back, 
							products[0], StateType.back, products[1], StateType.back, rateconstant);
				} else {
					ConversionUtilities.printWarning("I have something weird, it's a " + subdomain.getClass());
				}
			}
		}
	}
	
	
	
	private void setTime() {
		TimeBounds timeBounds = vcellSimJob.getSimulation().getSolverTaskDescription().getTimeBounds();
		TimeStep timeStep = vcellSimJob.getSimulation().getSolverTaskDescription().getTimeStep();		
		SmoldynTime smoldynTime = new SmoldynTime(timeBounds.getStartingTime(), timeBounds.getEndingTime(), timeStep.getDefaultTimeStep());
		smoldynsimulationsettings.setSmoldyntime(smoldynTime);
	}
	
	private void setOutput() {
		TimeBounds timeBounds = vcellSimJob.getSimulation().getSolverTaskDescription().getTimeBounds();
		double timestep = vcellSimJob.getSimulation().getSolverTaskDescription().getTimeStep().getDefaultTimeStep();
		OutputTimeSpec ots = vcellSimJob.getSimulation().getSolverTaskDescription().getOutputTimeSpec();
		if (!ots.isUniform()) {
			ConversionUtilities.throwRuntimeException("Smoldyn solver expecting a uniform output option");
		}
		
		smoldynsimulationsettings.addFilehandle(outputFile.getName());
		
		final double saveInterval = ((UniformOutputTimeSpec)ots).getOutputTimeStep();
		final double start = timeBounds.getStartingTime();
		final double stop = timeBounds.getEndingTime();
//		smoldynsimulationsettings.addObservationEvent(new EventTiming(start, stop, saveInterval), 
//				EventType.SAVE_SIMULATION_STATE, outputFile.getName());
		smoldynsimulationsettings.addVCellObservationEvent(new EventTiming(start, stop, timestep), 
				VCellEventType.PRINT_PROGRESS);
		smoldynsimulationsettings.addVCellObservationEvent(new EventTiming(start, stop, saveInterval), 
				VCellEventType.WRITE_OUTPUT);
	}
	
	private void setInternalSettings() {
		SmoldynSimulationOptions ssoptions = vcellSimJob.getSimulation().getSolverTaskDescription().getSmoldynSimulationOptions();
		InternalSettings internalsettings = new InternalSettings(ssoptions.getRandomSeed(), ssoptions.getAccuracy(),
				setMeshsize(vcellSimJob.getSimulation().getMeshSpecification().getSamplingSize()), ssoptions.getGaussianTableSize());
		this.smoldynsimulationsettings.setInternalSettings(internalsettings);
	}
	
	private double setMeshsize(ISize isize) {
		Extent extent = mathd.getGeometry().getExtent();
		this.smoldynsimulationsettings.setBoxes(isize.getX(), isize.getY(), isize.getZ());
		double xsize = ConversionUtilities.getBoxsize(isize.getX(), extent.getX()), 
			ysize = ConversionUtilities.getBoxsize(isize.getY(), extent.getY()), 
			zsize = ConversionUtilities.getBoxsize(isize.getZ(), extent.getZ());
		try {
			if((xsize != ysize) || (ysize != zsize)) {
				ConversionUtilities.throwRuntimeException("Smoldyn only supports one box size, which must be equal for all dimensions");
			}
		} catch(RuntimeException e) {
			e.printStackTrace();
		}
		return xsize;
	}
}
