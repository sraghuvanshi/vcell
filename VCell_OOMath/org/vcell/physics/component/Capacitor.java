package org.vcell.physics.component;
import org.vcell.units.VCUnitDefinition;

import jscl.plugin.Expression;
import jscl.plugin.ParseException;


/**
 * Insert the type's description here.
 * Creation date: (1/9/2006 11:14:25 PM)
 * @author: Jim Schaff
 */
public class Capacitor extends TwoPortElectricalComponent {
/**
 * Resistor constructor comment.
 */
public Capacitor(String argName, double capacitance_pF, double initialVoltage) {
	super(argName);
	Parameter C = new Parameter("C",VCUnitDefinition.getInstance("nF"));
	addSymbol(C);
	Variable Vinit = new Variable("V(0)",VCUnitDefinition.UNIT_mV);
	addSymbol(Vinit);
	try {
		addEquation(Expression.valueOf("C*d(V(t),t) - 1000*Ip(t)"));
		addEquation(Expression.valueOf("V(0) - ("+initialVoltage+")"));
		addEquation(Expression.valueOf("C - "+capacitance_pF));
	}catch (ParseException e){
		e.printStackTrace(System.out);
		throw new RuntimeException(e.getMessage());
	}
}
}