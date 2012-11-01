/*
 * Copyright (C) 1999-2011 University of Connecticut Health Center
 *
 * Licensed under the MIT License (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *  http://www.opensource.org/licenses/mit-license.php
 */

package cbit.vcell.solvers;

import java.util.*;
import cbit.vcell.math.*;
import cbit.vcell.messaging.server.SimulationTask;
import cbit.vcell.solver.*;
/**
 * This class was generated by a SmartGuide.
 * 
 */
public class CppClassCoderVCellModel extends CppClassCoder {
/**
 * VarContextCppCoder constructor comment.
 * @param name java.lang.String
 */
protected CppClassCoderVCellModel(CppCoderVCell cppCoderVCell, SimulationTask simTask) 
{
	super(simTask, cppCoderVCell,"UserVCellModel", "VCellModel");
}
/**
 * This method was created by a SmartGuide.
 * @param out java.io.PrintWriter
 */
protected void writeConstructor(java.io.PrintWriter out) throws Exception {
	out.println(getClassName()+"::"+getClassName()+"()");
	out.println(": "+getParentClassName()+"()");
 	out.println("{");
 	out.println("\tstring featurename;");
 	//
 	// add 'Features' to VCellModel
 	//
 	Enumeration enum1 = cppCoder.getCppClassCoders();
 	while (enum1.hasMoreElements()){
 		CppClassCoder coder = (CppClassCoder)enum1.nextElement();
 		if (coder instanceof CppClassCoderFeature){
 			CppClassCoderFeature featureClassCoder = (CppClassCoderFeature)coder;
 			
 			//
 			// calculate a priority based on level of nesting 
 			//
 			CompartmentSubDomain subDomain = featureClassCoder.getCompartmentSubDomain();
 			out.println("\tfeaturename=\""+subDomain.getName()+"\";");
			out.println("\taddFeature(new "+featureClassCoder.getClassName()+"(featurename,"+subDomain.getPriority()+"));");
		}
	}
 	//
 	// add 'Contours' to VCellModel (if any)
 	//
 	cbit.vcell.geometry.FilamentGroup fg = simTask.getSimulation().getMathDescription().getGeometry().getGeometrySpec().getFilamentGroup();
  	for (int i=0;i<fg.getFilamentCount();i++){
	  	out.println("\taddContour(new Contour("+i+"));  // for Filament "+fg.getFilamentNames()[i]);
  	}	 	
	out.println("}");
}
/**
 * This method was created by a SmartGuide.
 * @param printWriter java.io.PrintWriter
 */
public void writeDeclaration(java.io.PrintWriter out) {
	out.println("//---------------------------------------------");
	out.println("//  class " + getClassName());
	out.println("//---------------------------------------------");

	out.println("class " + getClassName() + " : public " + getParentClassName());
	out.println("{");
	out.println("public:");
	out.println("\t"+getClassName() + "();");
	out.println("};");
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
}
}
