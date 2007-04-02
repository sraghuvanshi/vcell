package cbit.vcell.solvers;
/*�
 * (C) Copyright University of Connecticut Health Center 2001.
 * All rights reserved.
�*/
import java.util.*;

import cbit.vcell.field.FieldDataIdentifierSpec;
import cbit.vcell.field.FieldFunctionArguments;
import cbit.vcell.math.*;
import cbit.vcell.parser.*;
import cbit.vcell.solver.*;
/**
 * This class was generated by a SmartGuide.
 * 
 */
public abstract class CppClassCoderAbstractVarContext extends CppClassCoder {
	private Simulation simulation = null;
	private SubDomain subDomain = null;
	private Variable variable = null;
	private Equation equation = null;

/**
 * VarContextCppCoder constructor comment.
 * @param name java.lang.String
 */
protected CppClassCoderAbstractVarContext(CppCoderVCell cppCoderVCell, 
											Equation argEquation, 
											SubDomain argSubDomain, 
											Simulation argSimulation, 
											String argParentClass) throws Exception
{
	super(cppCoderVCell,argParentClass+argSubDomain.getName()+argEquation.getVariable().getName(), argParentClass);
	this.equation = argEquation;
	this.variable = argEquation.getVariable();
	this.simulation = argSimulation;
	this.subDomain = argSubDomain;
}


/**
 * Insert the method's description here.
 * Creation date: (6/22/2004 3:32:57 PM)
 * @return cbit.vcell.math.Equation
 */
public Equation getEquation() {
	return equation;
}


/**
 * Insert the method's description here.
 * Creation date: (6/22/2004 3:07:51 PM)
 * @return cbit.vcell.math.Variable[]
 */
protected Variable[] getRequiredVariables() throws Exception {

	//
	// default implementation (need to override in VolumeVarContext)
	//
	Enumeration enum1 = equation.getRequiredVariables(simulation);

	Vector uniqueVarList = new Vector();
	while (enum1.hasMoreElements()) {
		Variable var = (Variable)enum1.nextElement();
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

	return (Variable[])cbit.util.BeanUtils.getArray(uniqueVarList,Variable.class);
}


/**
 * Insert the method's description here.
 * Creation date: (6/22/2004 3:33:38 PM)
 * @return cbit.vcell.solver.Simulation
 */
public Simulation getSimulation() {
	return simulation;
}


/**
 * Insert the method's description here.
 * Creation date: (6/22/2004 3:22:51 PM)
 * @return cbit.vcell.math.SubDomain
 */
public SubDomain getSubDomain() {
	return subDomain;
}


/**
 * This method was created by a SmartGuide.
 * @return cbit.vcell.math.Variable
 */
public Variable getVariable() {
	return variable;
}


/**
 * Insert the method's description here.
 * Creation date: (6/22/2004 5:34:49 PM)
 * @return boolean
 */
protected final boolean isFlippedInsideOutside(MembraneSubDomain membraneSubDomain) {
	CompartmentSubDomain inside = membraneSubDomain.getInsideCompartment();
	CompartmentSubDomain outside = membraneSubDomain.getOutsideCompartment();
	if (inside.getPriority() > outside.getPriority()){
		return false;
	}else if (inside.getPriority() < outside.getPriority()){
		return true;
	}else{ // inside.getPriority() == outside.getPriority()
		throw new RuntimeException("CompartmentSubDomains '"+inside.getName()+"' and '"+outside.getName()+"' have same priority ("+inside.getPriority()+")");
	}
}


/**
 * This method was created by a SmartGuide.
 * @param out java.io.PrintWriter
 */
protected void writeContourFunction(java.io.PrintWriter out, String functionName, Expression exp) throws Exception {

	if (exp == null){
		return;
	}	

	out.println("double "+getClassName()+"::"+functionName+"(ContourElement *contourElement)");
	out.println("{");

	Expression exp2 = simulation.substituteFunctions(exp).flatten();
	writeContourFunctionDeclarations(out,"contourElement",exp2);

	out.println("   return "+exp2.infix_C()+";");
		
	out.println("}");
	out.println("");
}


/**
 * This method was created by a SmartGuide.
 * @param out java.io.PrintWriter
 */
protected void writeContourFunctionDeclarations(java.io.PrintWriter out, String contourElementString, Expression exp) throws Exception {

	boolean wc_defined = false;
	Enumeration enum1 = simulation.getRequiredVariables(exp);
	
	while (enum1.hasMoreElements()){
		Variable var = (Variable)enum1.nextElement();
		if (var instanceof VolVariable){
			out.println("   double "+var.getName()+" = var_"+var.getName()+"->getOld("+contourElementString+"->volumeIndex);");
		}else if (var instanceof FilamentVariable){
			out.println("   double "+var.getName()+" = var_"+var.getName()+"->getOld("+contourElementString+"->index);");
		}else if (var instanceof ReservedVariable){
			//
			// define reserved symbols (x,y,z,t)
			//
			ReservedVariable rv = (ReservedVariable)var;
			if (rv.isTIME()){
				out.println("   double t = sim->getTime_sec();");
			}else if (rv.isX()){
				if (!wc_defined){
					out.println("   WorldCoord wc_begin = "+contourElementString+"->wc_begin;");
					out.println("   WorldCoord wc_end = "+contourElementString+"->wc_end;");
					out.println("   WorldCoord wc;");
					out.println("	wc.x = (wc_begin.x+wc_end.x)/2.0;");
					out.println("	wc.y = (wc_begin.y+wc_end.y)/2.0;");
					out.println("	wc.x = (wc_begin.z+wc_end.z)/2.0;");
					wc_defined = true;
				}	
				out.println("   double x = wc.x;");
			}else if (rv.isY()){
				if (!wc_defined){
					out.println("   WorldCoord wc_begin = "+contourElementString+"->wc_begin;");
					out.println("   WorldCoord wc_end = "+contourElementString+"->wc_end;");
					out.println("   WorldCoord wc;");
					out.println("	wc.x = (wc_begin.x+wc_end.x)/2.0;");
					out.println("	wc.y = (wc_begin.y+wc_end.y)/2.0;");
					out.println("	wc.x = (wc_begin.z+wc_end.z)/2.0;");
					wc_defined = true;
				}	
				out.println("   double y = wc.y;");
			}else if (rv.isZ()){
				if (!wc_defined){
					out.println("   WorldCoord wc_begin = "+contourElementString+"->wc_begin;");
					out.println("   WorldCoord wc_end = "+contourElementString+"->wc_end;");
					out.println("   WorldCoord wc;");
					out.println("	wc.x = (wc_begin.x+wc_end.x)/2.0;");
					out.println("	wc.y = (wc_begin.y+wc_end.y)/2.0;");
					out.println("	wc.x = (wc_begin.z+wc_end.z)/2.0;");
					wc_defined = true;
				}	
				out.println("   double z = wc.z;");
			}		
		}		
	}

	writeFieldFunctionDeclarations(out, exp, contourElementString);
}


/**
 * This method was created by a SmartGuide.
 * @param out java.io.PrintWriter
 */
private final void writeFieldFunctionDeclarations(java.io.PrintWriter out, Expression exp, String indexString) throws Exception {

	if (exp == null){
		throw new Exception("null expression");
	}

	FieldFunctionArguments[] fieldFuncArgs = exp.getFieldFunctionArguments();

	for (int i = 0; fieldFuncArgs != null && i < fieldFuncArgs.length; i ++) {
		String localvarname = FieldDataIdentifierSpec.getLocalVariableName_C(fieldFuncArgs[i]);
		String globalvarname = FieldDataIdentifierSpec.getGlobalVariableName_C(fieldFuncArgs[i]);
		out.println("\tdouble " + localvarname + " = " + globalvarname + "[" + indexString + "];");	
	}
}


/**
 * This method was created by a SmartGuide.
 * @param out java.io.PrintWriter
 */
protected final void writeMembraneFunction(java.io.PrintWriter out, String functionName, Expression exp, boolean bFlipInsideOutside) throws Exception {

	if (exp == null){
		return;
	}	

	out.println("double "+getClassName()+"::"+functionName+"(MembraneElement *memElement)");
	out.println("{");

	Expression exp2 = simulation.substituteFunctions(exp).flatten();
	writeMembraneFunctionDeclarations(out,"memElement",exp2,bFlipInsideOutside,"   ");

	out.println("   return "+exp2.infix_C()+";");
		
	out.println("}");
	out.println("");
}


/**
 * This method was created by a SmartGuide.
 * @param out java.io.PrintWriter
 */
protected final void writeMembraneFunctionDeclarations(java.io.PrintWriter out, String membraneElementString, Expression exp, boolean bFlipInsideOutside, String pad) throws Exception {

	boolean wc_defined = false;
	Enumeration enum1 = simulation.getRequiredVariables(exp);

	while (enum1.hasMoreElements()){
		Variable var = (Variable)enum1.nextElement();
		if (var instanceof OutsideVariable){
			String outsideVarName = var.getName();
			String volVar = ((OutsideVariable)var).getVolVariableName();
			if (bFlipInsideOutside){
				out.println(pad+"double "+outsideVarName+" = mesh->getInsideOld(var_"+volVar+","+membraneElementString+"); // ***** definition reversed");
			}else{
				out.println(pad+"double "+outsideVarName+" = mesh->getOutsideOld(var_"+volVar+","+membraneElementString+");");
			}
		}if (var instanceof InsideVariable){
			String insideVarName = var.getName();
			String volVar = ((InsideVariable)var).getVolVariableName();
			if (bFlipInsideOutside){
				out.println(pad+"double "+insideVarName+" = mesh->getOutsideOld(var_"+volVar+","+membraneElementString+"); // ***** definition reversed");
			}else{
				out.println(pad+"double "+insideVarName+" = mesh->getInsideOld(var_"+volVar+","+membraneElementString+");");
			}
		}else if (var instanceof MemVariable){
			out.println(pad+"double "+var.getName()+" = var_"+var.getName()+"->getOld("+membraneElementString+"->index);");
		}else if (var instanceof MembraneRegionVariable){
			out.println(pad+"double "+var.getName()+" = var_"+var.getName()+"->getOld("+membraneElementString+"->region->getId());");
		}else if (var instanceof VolumeRegionVariable){
			throw new RuntimeException("can't deal with VolumeRegionVariable in MembraneFunctionDeclaration(), no Inside/Outside specifications");
		}else if (var instanceof ReservedVariable){
			//
			// define reserved symbols (x,y,z,t)
			//
			ReservedVariable rv = (ReservedVariable)var;
			if (rv.isTIME()){
				out.println(pad+"double t = sim->getTime_sec();");
			}else if (rv.isX()){
				if (!wc_defined){
					out.println(pad+"WorldCoord wc = mesh->getMembraneWorldCoord("+membraneElementString+");");
					wc_defined = true;
				}	
				out.println(pad+"double x = wc.x;");
			}else if (rv.isY()){
				if (!wc_defined){
					out.println(pad+"WorldCoord wc = mesh->getMembraneWorldCoord("+membraneElementString+");");
					wc_defined = true;
				}	
				out.println(pad+"double y = wc.y;");
			}else if (rv.isZ()){
				if (!wc_defined){
					out.println(pad+"WorldCoord wc = mesh->getMembraneWorldCoord("+membraneElementString+");");
					wc_defined = true;
				}	
				out.println(pad+"double z = wc.z;");
			}		
		}		
	}

	writeFieldFunctionDeclarations(out, exp, membraneElementString);
}


/**
 * This method was created by a SmartGuide.
 * @param out java.io.PrintWriter
 */
protected final void writeMembraneRegionFunction(java.io.PrintWriter out, String functionName, Expression exp) throws Exception {

	if (exp == null){
		return;
	}	

	out.println("double "+getClassName()+"::"+functionName+"(MembraneRegion *memRegion)");
	out.println("{");

	Expression exp2 = simulation.substituteFunctions(exp).flatten();
	writeMembraneRegionFunctionDeclarations(out,"memRegion",exp2);

	out.println("   return "+exp2.infix_C()+";");
		
	out.println("}");
	out.println("");
}


/**
 * This method was created by a SmartGuide.
 * @param out java.io.PrintWriter
 */
protected final void writeMembraneRegionFunctionDeclarations(java.io.PrintWriter out, String membraneRegionString, Expression exp) throws Exception {

	boolean wc_defined = false;
	MathDescription mathDesc = simulation.getMathDescription();
	Enumeration enum1 = simulation.getRequiredVariables(exp);
	
	while (enum1.hasMoreElements()){
		Variable var = (Variable)enum1.nextElement();
		if (var instanceof VolumeRegionVariable){
			throw new ExpressionException("cannot use variable '"+var.getName()+"' of type "+var.getClass().getName()+" within a membraneRegion rate (until region enumeration is implemented)");
		}else if (var instanceof MembraneRegionVariable){
			out.println("   double "+var.getName()+" = var_"+var.getName()+"->getOld("+membraneRegionString+"->getId());");
		}else if ((var instanceof MemVariable) ||
				(var instanceof VolVariable) ||
				(var instanceof OutsideVariable) ||
				(var instanceof InsideVariable)){
			throw new ExpressionException("cannot use variable '"+var.getName()+"' of type "+var.getClass().getName()+" within a membraneRegion rate");
		}else if (var instanceof ReservedVariable){
			//
			// define reserved symbols (x,y,z,t)
			//
			ReservedVariable rv = (ReservedVariable)var;
			if (rv.isTIME()){
				out.println("   double t = sim->getTime_sec();");
			}else if (rv.isX() || rv.isY() || rv.isZ()){
				throw new ExpressionException("cannot use coordinate '"+var.getName()+"' of type "+var.getClass().getName()+" within a membraneRegion rate");
			}		
		}		
	}
	writeFieldFunctionDeclarations(out, exp, membraneRegionString);
}


/**
 * This method was created by a SmartGuide.
 * @param out java.io.PrintWriter
 */
protected final void writeResolveReferences(java.io.PrintWriter out) throws Exception {
	out.println("boolean "+getClassName()+"::resolveReferences(Simulation *sim)");
	out.println("{");
	out.println("   if (!"+getParentClassName()+"::resolveReferences(sim)){");
	out.println("      return FALSE;");
	out.println("   }");
	out.println("   ASSERTION(sim);");
	out.println("");
	Variable[] requiredVariables = getRequiredVariables();
	Vector volVarList = new Vector();
	for (int i = 0; i < requiredVariables.length; i++){
		Variable var = requiredVariables[i];
		if (var instanceof VolVariable){
			out.println("   var_"+var.getName()+" = (VolumeVariable*)sim->getVariableFromName(\""+var.getName()+"\");");
			out.println("   if (var_"+var.getName()+"==NULL){");
			out.println("      printf(\"could not resolve '"+var.getName()+"'\\n\");");
			out.println("      return FALSE;");
			out.println("   }");
			out.println("");
		}else if (var instanceof MemVariable){	
			out.println("   var_"+var.getName()+" = (MembraneVariable*)sim->getVariableFromName(\""+var.getName()+"\");");
			out.println("   if (var_"+var.getName()+"==NULL){");
			out.println("      printf(\"could not resolve '"+var.getName()+"'\\n\");");
			out.println("      return FALSE;");
			out.println("   }");
			out.println("");
		}else if (var instanceof MembraneRegionVariable){	
			out.println("   var_"+var.getName()+" = (MembraneRegionVariable*)sim->getVariableFromName(\""+var.getName()+"\");");
			out.println("   if (var_"+var.getName()+"==NULL){");
			out.println("      printf(\"could not resolve '"+var.getName()+"'\\n\");");
			out.println("      return FALSE;");
			out.println("   }");
			out.println("");
		}else if (var instanceof VolumeRegionVariable){	
			out.println("   var_"+var.getName()+" = (VolumeRegionVariable*)sim->getVariableFromName(\""+var.getName()+"\");");
			out.println("   if (var_"+var.getName()+"==NULL){");
			out.println("      printf(\"could not resolve '"+var.getName()+"'\\n\");");
			out.println("      return FALSE;");
			out.println("   }");
			out.println("");
		}	
	}		  	
	out.println("   return TRUE;");
	out.println("}");
	out.println("");
}


/**
 * This method was created by a SmartGuide.
 * @param out java.io.PrintWriter
 */
protected final void writeVolumeConstantFunction(java.io.PrintWriter out, String functionName, Expression exp) throws Exception {

	if (exp == null){
		return;
	}	

	out.println("double "+getClassName()+"::"+functionName+"()");
	out.println("{");

	exp.bindExpression(simulation);
	Expression exp2 = simulation.substituteFunctions(exp).flatten();
	try {
		double constant = exp2.evaluateConstant();
	} catch (Exception ex) {
		throw new RuntimeException("Not a constant: " + exp.infix());
	}
	out.println("\treturn "+exp2.infix_C()+";");
		
	out.println("}");
	out.println("");
}


/**
 * This method was created by a SmartGuide.
 * @param out java.io.PrintWriter
 */
protected final void writeVolumeFunction(java.io.PrintWriter out, String functionName, Expression exp) throws Exception {

	if (exp == null){
		return;
	}	

	out.println("double "+getClassName()+"::"+functionName+"(long volumeIndex)");
	out.println("{");

	exp.bindExpression(simulation);
	Expression exp2 = simulation.substituteFunctions(exp).flatten();
	writeVolumeFunctionDeclarations(out,exp2,"volumeIndex");

	out.println("   return "+exp2.infix_C()+";");
		
	out.println("}");
	out.println("");
}


/**
 * This method was created by a SmartGuide.
 * @param out java.io.PrintWriter
 */
private final void writeVolumeFunctionDeclarations(java.io.PrintWriter out, Expression exp, String volumeIndexString) throws Exception {

	if (exp == null){
		throw new Exception("null expression");
	}	

	boolean wc_defined = false;
	Enumeration enum1 = simulation.getRequiredVariables(exp);

	while (enum1.hasMoreElements()){
		Variable var = (Variable)enum1.nextElement();
		if (var instanceof VolVariable){
			out.println("   double "+var.getName()+" = var_"+var.getName()+"->getOld("+volumeIndexString+");");
		//}else if (var instanceof VolumeRegionVariable){
			//out.println("   double "+var.getName()+" = var_"+var.getName()+"->getOld("+volumeElementString+"->region->getIndex("+volumeElementString+"->index));");
		}else if (var instanceof MemVariable){
			throw new Exception("membrane variable not defined at a boundary condition");
		}else if (var instanceof ReservedVariable){
			//
			// define reserved symbols (x,y,z,t)
			//
			ReservedVariable rv = (ReservedVariable)var;
			if (rv.isTIME()){
				out.println("   double t = sim->getTime_sec();");
			}else if (rv.isX()){
				if (!wc_defined){
					out.println("   WorldCoord wc = mesh->getVolumeWorldCoord("+volumeIndexString+");");
					wc_defined = true;
				}	
				out.println("   double x = wc.x;");
			}else if (rv.isY()){
				if (!wc_defined){
					out.println("   WorldCoord wc = mesh->getVolumeWorldCoord("+volumeIndexString+");");
					wc_defined = true;
				}	
				out.println("   double y = wc.y;");
			}else if (rv.isZ()){
				if (!wc_defined){
					out.println("   WorldCoord wc = mesh->getVolumeWorldCoord("+volumeIndexString+");");
					wc_defined = true;
				}	
				out.println("   double z = wc.z;");
			}		
		}		
	}

	writeFieldFunctionDeclarations(out, exp, volumeIndexString);
}


/**
 * This method was created by a SmartGuide.
 * @param out java.io.PrintWriter
 */
protected final void writeVolumeRegionFunction(java.io.PrintWriter out, String functionName, Expression exp) throws Exception {

	if (exp == null){
		return;
	}	

	out.println("double "+getClassName()+"::"+functionName+"(VolumeRegion *volumeRegion)");
	out.println("{");

	exp.bindExpression(getSimulation());
	Expression exp2 = getSimulation().substituteFunctions(exp).flatten();
	writeVolumeRegionFunctionDeclarations(out,exp2,"volumeRegion");

	out.println("   return "+exp2.infix_C()+";");
		
	out.println("}");
	out.println("");
}


/**
 * This method was created by a SmartGuide.
 * @param out java.io.PrintWriter
 */
protected final void writeVolumeRegionFunctionDeclarations(java.io.PrintWriter out, Expression exp, String volumeRegionString) throws Exception {

	if (exp == null){
		throw new Exception("null expression");
	}	

	boolean wc_defined = false;
	String regionIndexString = "regionIndex";
	out.println("	long "+regionIndexString+" = "+volumeRegionString+"->getId();");
	Enumeration enum1 = getSimulation().getRequiredVariables(exp);

	while (enum1.hasMoreElements()){
		Variable var = (Variable)enum1.nextElement();
		if (var instanceof VolVariable){
			out.println("   double "+var.getName()+" = var_"+var.getName()+"->getOld("+regionIndexString+");");
		}else if (var instanceof MemVariable){
			throw new Exception("membrane variable not defined at a boundary condition");
		}else if (var instanceof ReservedVariable){
			//
			// define reserved symbols (x,y,z,t)
			//
			ReservedVariable rv = (ReservedVariable)var;
			if (rv.isTIME()){
				out.println("   double t = sim->getTime_sec();");
			}else{
				throw new RuntimeException("unexpected spatial reserved variable "+rv.getName()+" in UniformRate Expression");
			}
		}		
	}

	writeFieldFunctionDeclarations(out, exp, volumeRegionString);
}
}