/*
 * Copyright (C) 1999-2011 University of Connecticut Health Center
 *
 * Licensed under the MIT License (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *  http://www.opensource.org/licenses/mit-license.php
 */

package org.vcell.util;

/**
 * This class was generated by a SmartGuide.
 * 
 */
public class CoordinateIndex implements java.io.Serializable,org.vcell.util.Matchable {
	public int x = 0;
	public int y = 0;
	public int z = 0;
/**
 * This method was created in VisualAge.
 */
public CoordinateIndex() {
}
/**
 * This method was created in VisualAge.
 * @param x int
 * @param y int
 * @param z int
 */
public CoordinateIndex(int x, int y, int z) {
	this.x = x;
	this.y = y;
	this.z = z;
}
/**
 * Insert the method's description here.
 * Creation date: (10/5/2004 8:06:35 AM)
 */
public boolean compareEqual(org.vcell.util.Matchable obj) {
	if (obj == null) {
		return false;
	}
	if (!(obj instanceof CoordinateIndex)) {
		return false;
	}
	CoordinateIndex coord = (CoordinateIndex) obj;
	if (x != coord.x || y != coord.y || z != coord.z) {
		return false;
	}
	return true;
}
/**
 * Insert the method's description here.
 * Creation date: (9/1/00 12:09:57 PM)
 * @return java.lang.String
 */
public String toString() {
	return "X="+x+" Y="+y+" Z="+z;
}
}
