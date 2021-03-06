/*
 * Copyright (C) 1999-2011 University of Connecticut Health Center
 *
 * Licensed under the MIT License (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *  http://www.opensource.org/licenses/mit-license.php
 */

package cbit.vcell.math;

import org.vcell.util.Matchable;

/**
 * This class was generated by a SmartGuide.
 * 
 */
public class ParameterVariable extends Variable {
/**
 * Constant constructor comment.
 * @param name java.lang.String
 */
public ParameterVariable(String name) {
	super(name,null);
	if (name==null){
		throw new IllegalArgumentException("name is null");
	}
}
/**
 * This method was created in VisualAge.
 * @return boolean
 * @param obj Matchable
 */
public boolean compareEqual(Matchable obj, boolean bIgnoreMissingDomain) {
	if (!(obj instanceof ParameterVariable)){
		return false;
	}
	if (!compareEqual0(obj, bIgnoreMissingDomain)){
		return false;
	}

	return true;
}
/**
 * This method was created by a SmartGuide.
 * @return java.lang.String
 */
public String getVCML() {
	return "Parameter  "+getName();
}
/**
 * This method was created in VisualAge.
 * @return java.lang.String
 */
public String toString() {
	return "Parameter("+hashCode()+") <"+getName()+">";
}
}
