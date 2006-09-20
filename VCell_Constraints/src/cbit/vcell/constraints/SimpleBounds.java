package cbit.vcell.constraints;

/*�
 * (C) Copyright University of Connecticut Health Center 2001.
 * All rights reserved.
�*/
/**
 * Insert the type's description here.
 * Creation date: (6/25/01 4:13:23 PM)
 * @author: Jim Schaff
 */
import net.sourceforge.interval.ia_math.RealInterval;
import cbit.vcell.parser.SymbolTableEntry;

public class SimpleBounds extends AbstractConstraint {
	private RealInterval fieldBounds = null;
	private String fieldIdentifier = null;
/**
 * SimpleBounds constructor comment.
 */
public SimpleBounds(String argIdentifier, RealInterval argBounds, int argConstraintType, String argDescription) {
	super(argConstraintType, argDescription);
	this.fieldIdentifier = argIdentifier;
	this.fieldBounds = argBounds;
}
/**
 * Gets the bounds property (net.sourceforge.interval.ia_math.RealInterval) value.
 * @return The bounds property value.
 * @see #setBounds
 */
public net.sourceforge.interval.ia_math.RealInterval getBounds() {
	return fieldBounds;
}
/**
 * Insert the method's description here.
 * Creation date: (6/25/01 4:21:53 PM)
 * @return cbit.vcell.parser.SymbolTableEntry
 */
public String getIdentifier() {
	return fieldIdentifier;
}
/**
 * Sets the bounds property (net.sourceforge.interval.ia_math.RealInterval) value.
 * @param bounds The new value for the property.
 * @exception java.beans.PropertyVetoException The exception description.
 * @see #getBounds
 */
public void setBounds(net.sourceforge.interval.ia_math.RealInterval bounds) throws java.beans.PropertyVetoException {
	net.sourceforge.interval.ia_math.RealInterval oldValue = fieldBounds;
	fireVetoableChange("bounds", oldValue, bounds);
	fieldBounds = bounds;
	firePropertyChange("bounds", oldValue, bounds);
}
/**
 * Insert the method's description here.
 * Creation date: (9/18/2003 4:55:48 PM)
 * @return java.lang.String
 */
public String toString() {
	return super.toString() + " : "+getIdentifier()+" = "+getBounds();
}
}
