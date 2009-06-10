package cbit.vcell.math;

/*�
 * (C) Copyright University of Connecticut Health Center 2001.
 * All rights reserved.
�*/
import java.io.*;

import org.vcell.util.TokenMangler;
import org.vcell.util.document.KeyValue;
import org.vcell.util.document.Version;
import org.vcell.util.document.VersionInfo;
import org.vcell.util.document.VersionableType;

/**
 * This class was generated by a SmartGuide.
 * 
 */
public class MathInfo implements Serializable,VersionInfo {
	private KeyValue geomRef = null;
	private Version version = null;
/**
 * This method was created in VisualAge.
 * @param argGeomRef KeyValue
 * @param argVersion cbit.sql.Version
 */
public MathInfo(KeyValue argGeomRef,Version argVersion) {
	super();
	this.geomRef = argGeomRef;
	this.version = argVersion;
}
/**
 * Insert the method's description here.
 * Creation date: (1/25/01 12:24:41 PM)
 * @return boolean
 * @param object java.lang.Object
 */
public boolean equals(Object object) {
	if (object instanceof MathInfo){
		if (!getVersion().getVersionKey().equals(((MathInfo)object).getVersion().getVersionKey())){
			return false;
		}
		return true;
	}
	return false;
}
/**
 * This method was created in VisualAge.
 * @return cbit.sql.KeyValue
 */
public KeyValue getGeomRef() {
	return geomRef;
}
/**
 * This method was created in VisualAge.
 * @return java.lang.String
 */
public String getSimulationID() {
	String rawSimID = getVersion().getName()+((getVersion().getVersionKey()!=null)?("_"+getVersion().getVersionKey().toString()):"");
	return TokenMangler.fixToken(rawSimID);
}
/**
 * This method was created in VisualAge.
 * @return cbit.sql.Version
 */
public Version getVersion() {
	return version;
}
/**
 * Insert the method's description here.
 * Creation date: (1/25/01 12:28:06 PM)
 * @return int
 */
public int hashCode() {
	return getVersion().getVersionKey().hashCode();
}
/**
 * This method was created in VisualAge.
 * @return java.lang.String
 */
public String toString() {
		return "MathInfo(geometryRef="+geomRef+",Version="+version+")";
}
public VersionableType getVersionType() {
	return VersionableType.MathDescription;
}
}
