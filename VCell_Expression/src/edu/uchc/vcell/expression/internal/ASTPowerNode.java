package edu.uchc.vcell.expression.internal;

/*�
 * (C) Copyright University of Connecticut Health Center 2001.
 * All rights reserved.
�*/
/* JJT: 0.2.2 */
import net.sourceforge.interval.ia_math.IAFunctionDomainException;
import net.sourceforge.interval.ia_math.IAMath;
import net.sourceforge.interval.ia_math.IANarrow;
import net.sourceforge.interval.ia_math.RealInterval;

import org.vcell.expression.DivideByZeroException;
import org.vcell.expression.ExpressionBindingException;
import org.vcell.expression.ExpressionException;
import org.vcell.expression.ExpressionTerm;
import org.vcell.expression.FunctionDomainException;
import org.vcell.expression.NameScope;

/**
 */
public class ASTPowerNode extends SimpleNode {

ASTPowerNode() {
	super(ExpressionParserTreeConstants.JJTPOWERNODE);
}
/**
 * Constructor for ASTPowerNode.
 * @param id int
 */
ASTPowerNode(int id) {
	super(id);
if (id != ExpressionParserTreeConstants.JJTPOWERNODE){ System.out.println("ASTAddNode(), id = "+id); }
}
  /**
 * This method was created by a SmartGuide.
 * @return cbit.vcell.parser.Node
 * @exception java.lang.Exception The exception description.
 * @see edu.uchc.vcell.expression.internal.Node#copyTree()
   */
public Node copyTree() {
	ASTPowerNode node = new ASTPowerNode();
	for (int i=0;i<jjtGetNumChildren();i++){
		node.jjtAddChild(jjtGetChild(i).copyTree());
	}
	return node;	
}
/**
 * This method was created by a SmartGuide.
 * @return cbit.vcell.parser.Node
 * @exception java.lang.Exception The exception description.
 * @see edu.uchc.vcell.expression.internal.Node#copyTreeBinary()
 */
public Node copyTreeBinary() {
	ASTPowerNode node = new ASTPowerNode();
	for (int i=0;i<jjtGetNumChildren();i++){
		node.jjtAddChild(jjtGetChild(i).copyTreeBinary());
	}
	return node;	
}
/**
 * This method was created by a SmartGuide.
 * @param independentVariable java.lang.String
 * @param derivativePolicy DerivativePolicy
 * @return cbit.vcell.parser.Expression
 * @throws ExpressionException
 * @exception java.lang.Exception The exception description.
 * @see edu.uchc.vcell.expression.internal.Node#differentiate(String, DerivativePolicy)
 */
public Node differentiate(String independentVariable, DerivativePolicy derivativePolicy) throws ExpressionException {
	// 
	// case of D(u^v) = v u^(v-1) D(u)  +  u^v log(u) D(v)
	//
	if (jjtGetNumChildren()!=2) throw new Error("'^' expects 2 arguments");

	// 
	// form  v pow(u,v-1) D(u)
	//
	ASTMultNode multNode1 = new ASTMultNode();
	ASTPowerNode powNode = new ASTPowerNode();
	ASTAddNode addNode = new ASTAddNode();
	addNode.jjtAddChild(jjtGetChild(1).copyTree());
	addNode.jjtAddChild(new ASTFloatNode(-1.0));
	powNode.jjtAddChild(jjtGetChild(0).copyTree());
	powNode.jjtAddChild(addNode);
	multNode1.jjtAddChild(jjtGetChild(1).copyTree());
	multNode1.jjtAddChild(powNode);
	multNode1.jjtAddChild(jjtGetChild(0).differentiate(independentVariable,derivativePolicy));
	
	// 
	// form  pow(u,v) log(u) D(v)
	//
	ASTMultNode multNode2 = new ASTMultNode();
	ASTFuncNode logNode = new ASTFuncNode();
	logNode.setFunction(ExpressionTerm.Operator.LOG);
	logNode.jjtAddChild(jjtGetChild(0).copyTree());
	multNode2.jjtAddChild(copyTree());
	multNode2.jjtAddChild(logNode);
	multNode2.jjtAddChild(jjtGetChild(1).differentiate(independentVariable,derivativePolicy));
	
	ASTAddNode fullAddNode = new ASTAddNode();
	fullAddNode.jjtAddChild(multNode1);
	fullAddNode.jjtAddChild(multNode2);
	
	return fullAddNode;
}
/**
 * Method evaluateConstant.
 * @return double
 * @throws ExpressionException
 * @see edu.uchc.vcell.expression.internal.Node#evaluateConstant()
 */
public double evaluateConstant() throws ExpressionException {
	if (jjtGetNumChildren()!=2){
		throw new ExpressionException("expecting two arguments for Power");
	}
	//
	// see if there are any constant 0.0's, if there are simplify to 0.0
	//
	ExpressionException savedException = null;
	
	Double exponentValue = null;
	try {
		exponentValue = new Double(jjtGetChild(1).evaluateConstant());
	}catch (ExpressionException e){
		savedException = e;
	}
	Double  baseValue = null;
	try {
		baseValue = new Double(jjtGetChild(0).evaluateConstant());
	}catch (ExpressionException e){
		savedException = e;
	}

	if (exponentValue!=null && exponentValue.doubleValue()==0){
		return 1.0;
	}
	if (savedException!=null){
		throw savedException;
	}else if (baseValue!=null && exponentValue!=null){
		if (baseValue.doubleValue()==0.0 && exponentValue.doubleValue()<0.0){
			String childString = infixString(LANGUAGE_DEFAULT,NAMESCOPE_DEFAULT);
			throw new DivideByZeroException("u^v and u=0 and v<0 divides by zero, expression = '"+infixString(LANGUAGE_DEFAULT,NAMESCOPE_DEFAULT)+"'");
		}else if (baseValue.doubleValue()<0.0 && exponentValue.doubleValue()!=Math.round(exponentValue.doubleValue())){
			throw new FunctionDomainException("u^v and u<0 and v not an integer: undefined, u="+baseValue.doubleValue()+", v="+exponentValue.doubleValue()+", expression='"+infixString(LANGUAGE_DEFAULT,NAMESCOPE_DEFAULT)+"'");
		}else{
			double result = Math.pow(baseValue.doubleValue(),exponentValue.doubleValue());
			if (Double.isInfinite(result) || Double.isNaN(result)){
				throw new FunctionDomainException("u^v evaluated to "+result+", u="+baseValue.doubleValue()+", v="+exponentValue.doubleValue()+", expression = '"+infixString(LANGUAGE_DEFAULT,NAMESCOPE_DEFAULT)+"'");
			}
			return result;
		}
	}else{ // should never happen
		throw new RuntimeException("unexpected error, no exception and either baseValue or exponentValue is null");
	}
}    
/**
 * Method evaluateInterval.
 * @param intervals RealInterval[]
 * @return RealInterval
 * @throws ExpressionException
 * @see edu.uchc.vcell.expression.internal.Node#evaluateInterval(RealInterval[])
 */
public RealInterval evaluateInterval(RealInterval intervals[]) throws ExpressionException {
	if (jjtGetNumChildren()!=2) throw new Error("pow() expects 2 arguments");
	try {
		setInterval(IAMath.vcell_power(jjtGetChild(0).evaluateInterval(intervals),jjtGetChild(1).evaluateInterval(intervals)),intervals);
	}catch (IAFunctionDomainException e){
		e.printStackTrace(System.out);
		throw new FunctionDomainException(e.getMessage());
	}
	return getInterval(intervals);
}    
/**
 * Method evaluateVector.
 * @param values double[]
 * @return double
 * @throws ExpressionException
 * @see edu.uchc.vcell.expression.internal.Node#evaluateVector(double[])
 */
public double evaluateVector(double values[]) throws ExpressionException {
	if (jjtGetNumChildren()!=2){
		throw new RuntimeException("ASTPowerNode@"+Integer.toHexString(hashCode())+" wrong number of arguments for '^' ("+jjtGetNumChildren()+"), expected 2");
	}
	double baseValue = jjtGetChild(0).evaluateVector(values);
	double exponentValue = jjtGetChild(1).evaluateVector(values);
	if (baseValue==0.0 && exponentValue<0.0){
		throw new DivideByZeroException("u^v and u=0 and v<0 divides by zero, expression = '"+infixString(LANGUAGE_DEFAULT,NAMESCOPE_DEFAULT)+"'");
	}else if (baseValue<0.0 && exponentValue!=Math.round(exponentValue)){
		throw new FunctionDomainException("u^v and u<0 and v not an integer: undefined, u="+baseValue+", v="+exponentValue+", expression='"+infixString(LANGUAGE_DEFAULT,NAMESCOPE_DEFAULT)+"'");
	}else{
		if (baseValue>=0.0 && exponentValue==1.0){
			return baseValue;
		}
		double result = Math.pow(baseValue,exponentValue);
		if (Double.isInfinite(result) || Double.isNaN(result)){
			throw new FunctionDomainException("u^v evaluated to "+result+", u="+baseValue+", v="+exponentValue+", expression = '"+infixString(LANGUAGE_DEFAULT,NAMESCOPE_DEFAULT)+"'");
		}
		return result;
	}
}    
/**
 * This method was created by a SmartGuide.
 * @return Node
 * @throws ExpressionException
 * @exception java.lang.Exception The exception description.
 * @see edu.uchc.vcell.expression.internal.Node#flatten()
 */
public Node flatten() throws ExpressionException {
	try {
		double value = evaluateConstant();
		return new ASTFloatNode(value);
	}catch (Exception e){}		

	ASTPowerNode powerNode = new ASTPowerNode();
	java.util.Vector tempChildren = new java.util.Vector();
	for (int i=0;i<jjtGetNumChildren();i++){
		tempChildren.addElement(jjtGetChild(i).flatten());
	}
	
	if (tempChildren.size()!=2) throw new ExpressionException("'^' expects 2 arguments");
	//
	//  b
	// a   test for b = 1
	//
	Node exponentChild = (Node)tempChildren.elementAt(1);
	Node mantissaChild = (Node)tempChildren.elementAt(0);
	if (exponentChild instanceof ASTFloatNode){
		double exponent = ((ASTFloatNode)exponentChild).value.doubleValue();
		if (exponent == 1.0){
			return mantissaChild;
		}
	}
	//
	//  b
	// a   test for b = 0
	//
	if (exponentChild instanceof ASTFloatNode){
		double exponent = ((ASTFloatNode)exponentChild).value.doubleValue();
		if (exponent == 0.0){
			return new ASTFloatNode(1.0);
		}
	}
	//
	//   w    
	//  v          v*w
	// u    --->  u
	//
	if ((mantissaChild instanceof ASTFuncNode && ((ASTFuncNode)mantissaChild).getFunction() == ExpressionTerm.Operator.POW) || mantissaChild instanceof ASTPowerNode){
		ASTMultNode newMultNode = new ASTMultNode();
		newMultNode.jjtAddChild(mantissaChild.jjtGetChild(1));
		newMultNode.jjtAddChild(exponentChild);
		ASTPowerNode newExponentNode = new ASTPowerNode();
		newExponentNode.jjtAddChild(mantissaChild.jjtGetChild(0));
		newExponentNode.jjtAddChild(newMultNode);
		return newExponentNode.flatten();
	}

	//
	//
	//
	ASTPowerNode powNode = new ASTPowerNode();
	powNode.jjtAddChild((SimpleNode)tempChildren.elementAt(0));
	powNode.jjtAddChild((SimpleNode)tempChildren.elementAt(1));

	return powNode;
		
}
/**
 * Method infixString.
 * @param lang int
 * @param nameScope NameScope
 * @return String
 * @see edu.uchc.vcell.expression.internal.Node#infixString(int, NameScope)
 */
public String infixString(int lang, NameScope nameScope){

	if (jjtGetNumChildren()!=2){
		throw new RuntimeException("there are "+jjtGetNumChildren()+" arguments for the power operator, expecting 2");
	}
	
	StringBuffer buffer = new StringBuffer();

	if (lang == LANGUAGE_DEFAULT || lang == LANGUAGE_MATLAB || lang == LANGUAGE_ECLiPSe || lang == LANGUAGE_JSCL){
		buffer.append('(');
		buffer.append(jjtGetChild(0).infixString(lang,nameScope));
		buffer.append(" ^ ");
		buffer.append(jjtGetChild(1).infixString(lang,nameScope));
		buffer.append(')');
	}else if (lang == LANGUAGE_C){
		buffer.append("pow(");
		buffer.append(jjtGetChild(0).infixString(lang,nameScope));
		buffer.append(',');
		buffer.append(jjtGetChild(1).infixString(lang,nameScope));
		buffer.append(')');
	}

	return buffer.toString();
}
/**
 * Insert the method's description here.
 * Creation date: (6/20/01 11:04:41 AM)
 * @param intervals RealInterval[]
 * @return boolean
 * @throws ExpressionBindingException
 * @see edu.uchc.vcell.expression.internal.Node#narrow(RealInterval[])
 */
public boolean narrow(RealInterval intervals[]) throws ExpressionBindingException{
	if (jjtGetNumChildren()!=2) throw new Error("power '^' expects 2 arguments");
	return IANarrow.vcell_narrow_power(getInterval(intervals),jjtGetChild(0).getInterval(intervals),jjtGetChild(1).getInterval(intervals))
			&& jjtGetChild(0).narrow(intervals)
			&& jjtGetChild(1).narrow(intervals)
			&& IANarrow.vcell_narrow_power(getInterval(intervals),jjtGetChild(0).getInterval(intervals),jjtGetChild(1).getInterval(intervals));
}
}
