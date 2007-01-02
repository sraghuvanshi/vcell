package edu.uchc.vcell.expression.internal;
/*�
 * (C) Copyright University of Connecticut Health Center 2001.
 * All rights reserved.
�*/

import java.util.Iterator;
import java.util.Vector;

import org.jdom.Element;
import org.vcell.expression.ExpressionException;
import org.vcell.expression.ExpressionTerm;
import org.vcell.expression.IExpression;
import org.vcell.expression.LambdaFunction;
import org.vcell.expression.ExpressionTerm.Operator;

import cbit.util.xml.MathMLTags;
import cbit.util.xml.XmlUtil;
/**
 * This class was generated by a SmartGuide.
 * 
 * @author schaff
 * @version $Revision: 1.0 $
 */
public class ExpressionMathMLParser {
	private LambdaFunction lambdaFunctions[] = new LambdaFunction[0];

/**
 * Insert the method's description here.
 * Creation date: (4/1/2005 4:50:57 PM)
 * @param argLambdaFunctions LambdaFunction[]
 */
public ExpressionMathMLParser(LambdaFunction[] argLambdaFunctions) {
	this.lambdaFunctions = argLambdaFunctions;
}


/**
 * Insert the method's description here.
 * Creation date: (2/11/2002 1:34:06 PM)
 * @param mathML java.lang.String
 * @return cbit.vcell.parser.Expression
 * @throws ExpressionException
 */
public IExpression fromMathML(String mathML) throws ExpressionException {
	if (mathML== null || mathML.length()==0) {
		throw new ExpressionException("Invalid null or empty MathML string");
	}
	Element rootElement;
	try {
		rootElement = XmlUtil.stringToXML(mathML, null);
	} catch (RuntimeException e) {
		e.printStackTrace(System.out);
		throw new ExpressionException("Unable to parse the xml string.");
	}
	IExpression exp = fromMathML(rootElement);
	
	return exp;
}


	/**
	 * Method fromMathML.
	 * @param mathElement Element
	 * @return IExpression
	 * @throws ExpressionException
	 */
	public IExpression fromMathML(Element mathElement) throws ExpressionException {

		SimpleNode root = getRootNode(mathElement);
		IExpression exp = new Expression(root);
		
		return exp;	
	}


/**
 * Insert the method's description here.
 * Creation date: (4/1/2005 4:50:20 PM)
 * @return cbit.vcell.parser.LambdaFunction[]
 */
public org.vcell.expression.LambdaFunction[] getFunctions() {
	return lambdaFunctions;
}


/**
 * Insert the method's description here.
 * Creation date: (2/11/2002 2:33:36 PM)
 * @param nodeMathML org.jdom.Element
 * @return cbit.vcell.parser.SimpleNode
 * @throws ExpressionException
 */
private SimpleNode getRootNode(Element nodeMathML) throws ExpressionException {
	if (nodeMathML.getName().equals(MathMLTags.APPLY)){
		//
		// <APPLY>
		//    <CMD>
		//    <ARG1>
		//    <ARG2>
		// </APPLY>
		//
		Element applyNode = nodeMathML;
		java.util.List children = applyNode.getChildren();
		if (children.size()==0) {
			throw new IllegalArgumentException("Invalid MathML! Empty APPLY element found!");
		}
		Element operation = (Element)children.get(0); 
		SimpleNode vcellOperationNode = null;

		if (operation.getName().equals(MathMLTags.EQUAL) ||
			operation.getName().equals(MathMLTags.NOT_EQUAL) ||
			operation.getName().equals(MathMLTags.GREATER) ||
			operation.getName().equals(MathMLTags.LESS) ||
			operation.getName().equals(MathMLTags.GREATER_OR_EQUAL) ||
			operation.getName().equals(MathMLTags.LESS_OR_EQUAL)){
			//
			// EQUAL can be interpreted either as relational operator or as an Assignment operator
			//
			ExpressionTerm.Operator operator = ASTRelationalNode.getOperatorFromMathML(operation.getName());
			vcellOperationNode = new ASTRelationalNode();
			((ASTRelationalNode)vcellOperationNode).setOperator(operator);
		} else if (operation.getName().equals(MathMLTags.AND)){
			vcellOperationNode = new ASTAndNode();
		} else if (operation.getName().equals(MathMLTags.OR)){
			vcellOperationNode = new ASTOrNode();
		} else if (operation.getName().equals(MathMLTags.NOT)){
			vcellOperationNode = new ASTNotNode();
		} else if (operation.getName().equals(MathMLTags.PLUS)){
			vcellOperationNode = new ASTAddNode();
		} else if (operation.getName().equals(MathMLTags.MINUS)){
			//
			// if unary minus
			//
			if (children.size()==2){ // if only operator and one argument
				vcellOperationNode = new ASTMinusTermNode();
				vcellOperationNode.jjtAddChild(getRootNode((Element)children.get(1)));
				return vcellOperationNode;
			} else if (children.size()==3){ // operator and two arguments
				//
				// if binary minus, vcell is unary so must SUB(A,B) --> ADD(A,MINUS(B))
				//
				vcellOperationNode = new ASTAddNode();
				vcellOperationNode.jjtAddChild(getRootNode((Element)children.get(1)));
				ASTMinusTermNode minusNode = new ASTMinusTermNode();
				minusNode.jjtAddChild(getRootNode((Element)children.get(2)));
				vcellOperationNode.jjtAddChild(minusNode);
				return vcellOperationNode;
			} else{
				throw new RuntimeException("expecting either 1 or 2 arguments for "+MathMLTags.MINUS);
			}
		} else if (operation.getName().equals(MathMLTags.TIMES)){
			vcellOperationNode = new ASTMultNode();
				
		} else if (operation.getName().equals(MathMLTags.DIVIDE)){
			//
			// mathML divides are alway binary, vcell is unary so must DIVIDE(NUM,DEN) --> MULT(NUM,INV(DEN))
			//
			vcellOperationNode = new ASTMultNode();
			vcellOperationNode.jjtAddChild(getRootNode((Element)children.get(1)));
			ASTInvertTermNode invNode = new ASTInvertTermNode();
			invNode.jjtAddChild(getRootNode((Element)children.get(2)));
			vcellOperationNode.jjtAddChild(invNode);
			return vcellOperationNode;
		} else if (operation.getName().equals(MathMLTags.LOG_10)){
			//
			// doesn't have a direct mapping, but can still be supported.
			//
			// the 'if' loop handles
			//
			//  LOG_10(A) --> LN(A)/LN(10.0)
			//
			// the 'else' loop handles the logbase case
			//
			// - <apply>
			//  	  <log /> 
			//		  - <logbase>
			//		      <cn>2</cn> 
			//		    </logbase>			=> log_2 4 (log of 4 to the base 2) = LN(4)/LN(2)
			//		  - <apply>
			//			  <cn>4</cn> 
			//	  	    </apply>
			//	</apply>
			//
			vcellOperationNode = new ASTMultNode();
			if (children.size()==2){ 	// This is the case where base is 10. 
				ASTFuncNode vcellOpNode1 = new ASTFuncNode();						// LN(A)
				vcellOpNode1.setFunction(ExpressionTerm.Operator.LOG);
				vcellOpNode1.jjtAddChild(getRootNode((Element)children.get(1)));
				
				ASTFuncNode vcellOpNode2 = new ASTFuncNode();						// LN(10)
				vcellOpNode2.setFunction(ExpressionTerm.Operator.LOG);
				ASTFloatNode floatNode = new ASTFloatNode(10.0);
				vcellOpNode2.jjtAddChild(floatNode);
				ASTInvertTermNode invVcellOpNode = new ASTInvertTermNode();			//  1 / LN(10)
				invVcellOpNode.jjtAddChild(vcellOpNode2);
				
				vcellOperationNode.jjtAddChild(vcellOpNode1);						// LN(A) * (1 / LN(10)) = LOG_10 (A)
				vcellOperationNode.jjtAddChild(invVcellOpNode);
			}else{						// This is the case where the base is other than 10 or e
				ASTFuncNode vcellOpNode1 = new ASTFuncNode();						// LN(Arg)
				vcellOpNode1.setFunction(ExpressionTerm.Operator.LOG);
				vcellOpNode1.jjtAddChild(getRootNode((Element)children.get(2)));
				
				ASTFuncNode vcellOpNode2 = new ASTFuncNode();						// LN(Base)
				vcellOpNode2.setFunction(ExpressionTerm.Operator.LOG);
				Element logBaseMathMLNode = (Element)children.get(1);
				vcellOpNode2.jjtAddChild(getRootNode((Element)logBaseMathMLNode.getChildren().get(0)));
				ASTInvertTermNode invVcellOpNode = new ASTInvertTermNode();			//  1 / LN(Base)
				invVcellOpNode.jjtAddChild(vcellOpNode2);
				
				vcellOperationNode.jjtAddChild(vcellOpNode1);						// LN(Arg) * (1 / LN(Base)) = LOG_Base (Arg)
				vcellOperationNode.jjtAddChild(invVcellOpNode);
			}
			return vcellOperationNode;
		} else if (operation.getName().equals(MathMLTags.ROOT)){
			//
			// doesn't have a direct mapping, but can still be supported.
			//
			//  ROOT(A,R) --> POW(A,INV(R))
			//  <apply>
			//    <root/>
			//    <degree>...exponentExp...</degree>  (optional with 2 as default)
			//    ...baseExp...
			//  </apply>
			//
			vcellOperationNode = new ASTFuncNode();
			((ASTFuncNode)vcellOperationNode).setFunction(ExpressionTerm.Operator.POW);
			if (children.size()==2){
				vcellOperationNode.jjtAddChild(getRootNode((Element)children.get(1)));
				ASTFloatNode floatNode = new ASTFloatNode(0.5);
				vcellOperationNode.jjtAddChild(floatNode);
			}else{
				vcellOperationNode.jjtAddChild(getRootNode((Element)children.get(2)));
				ASTInvertTermNode invNode = new ASTInvertTermNode();
				ASTMultNode multNode = new ASTMultNode();
				Element degreeMathMLNode = (Element)children.get(1);
				invNode.jjtAddChild(getRootNode((Element)degreeMathMLNode.getChildren().get(0)));
				multNode.jjtAddChild(new ASTFloatNode(1.0));
				multNode.jjtAddChild(invNode);
				vcellOperationNode.jjtAddChild(multNode);
			}
			return vcellOperationNode;
		} else if (operation.getName().equals(MathMLTags.IDENTIFIER)){
			// if there is a <ci> tag right after an <apply> tag, it must (?) be a lambda function; handle accordingly
			//
			// The lambda function (say, 'f') is used as follows ...
			// 		<apply>
			//			<ci> f </ci>
			// 			<cn> 3 </cn>
			//				....
			//		</apply>
			// The 'ci' element for 'f' is followed by other 'ci' or 'cn' elements (could also be expressions - more apply nodes)
			// 

			// get the lambda function name from the Identifier node
			java.util.List lFnList = operation.getContent();
			String lFnName = ((org.jdom.Text)lFnList.get(0)).getTextTrim();

			// get the list of lambda functions filled out in SBVCTranslator, find the fn matching 'lFn'
			LambdaFunction theLambdaFn = null;
			for (int i = 0; i < lambdaFunctions.length; i++) {
				if (lambdaFunctions[i].getName().equals(lFnName)) {
					theLambdaFn = lambdaFunctions[i];
					break;
				}
			}
			
			Iterator applyNodeChildrenIterator = children.iterator();
			Expression[] argExprs = new Expression[theLambdaFn.getFunctionArgs().length];
			int count = 0;
			
			while (applyNodeChildrenIterator.hasNext()) {
				Element child = (Element)applyNodeChildrenIterator.next();
				// ignore the first child of the 'apply' node (which is the labmda function identifier/name)
				if ( ((org.jdom.Text)(child.getContent()).get(0)).getTextTrim().equals(theLambdaFn.getName())) {
					continue;
				}
				SimpleNode childNode = getRootNode(child);
				argExprs[count] = new Expression(childNode);
				count++;
			}
			Expression finalExpr = (Expression)theLambdaFn.substitute(argExprs);
			return finalExpr.getRootNode();
		} else if (ASTFuncNode.getVCellFunction(operation.getName()) != null){
			vcellOperationNode = new ASTFuncNode();
			((ASTFuncNode)vcellOperationNode).setFunction(ASTFuncNode.getVCellFunction(operation.getName()));
		} else{
			throw new ExpressionException("cannot translate "+operation.getName()+" from MathML");
		}
		//
		// MathML "APPLY" places the operator and arguments as siblings
		// VCell places arguments as children of operator
		//
		// add children to vcell operation node
		//
		for (int i = 1; i < children.size(); i++){
			Element childMathML = (Element)children.get(i);
			vcellOperationNode.jjtAddChild(getRootNode(childMathML));
		}
		return vcellOperationNode;

	}else if (nodeMathML.getName().equals(MathMLTags.PIECEWISE)){
		// 
		// <piecewise>                           This is translated to VCell as follows:
		//    <piece>                            
		//       <apply>                         <addNode>
		//         ...expression1                    <multNode>
		//       </apply>                                expression1
		//       <apply>                                 condition1
		//         ...condition1                     </multNode>
		//       </apply>                            <multNode>
		//    </piece>                                   expression2
		//    <piece>                                    condition2
		//       <apply>                             </multNode>
		//         ...expression2                    <multNode>
		//       </apply>                                expressionOtherwise
		//       <apply>                                 <relationalNode "==">
		//         ...condition2                             <andNode>"
		//       </apply>                                        condition1
		//    </piece>                                           condition2
		//    <otherwise>                                    </andNode>
		//       <apply>                                     false "0.0"
		//          ...expressionOtherwise                </relationalNode>
		//       </apply>                            </multNode>
		//    </othewise>                        </addNode>
		// </piecewise>                             
		//
		//
		//
		ASTAddNode addNode = new ASTAddNode();
		Element piecewise = nodeMathML;
		Vector conditionExpList = new Vector();
		java.util.List children = piecewise.getChildren();
		for (int i = 0; i < children.size(); i++){
			Element child = (Element)children.get(i);
			if (child.getName().equals(MathMLTags.PIECE)){
				int numChildren = child.getChildren().size();
				Element pieceExp = (Element)child.getChildren().get(0);
				Element pieceCond = (Element)child.getChildren().get(1);
				SimpleNode vcellExp = getRootNode(pieceExp);
				SimpleNode vcellCond = getRootNode(pieceCond);
				//
				// save condition for later (if "otherwise" element is used);
				//
				conditionExpList.add(vcellCond); 
				//
				// add term for this conditional expression (pieceExp * pieceCond)
				//
				ASTMultNode multNode = new ASTMultNode();
				multNode.jjtAddChild(vcellExp);
				multNode.jjtAddChild(vcellCond);
				addNode.jjtAddChild(multNode);
			}else if (child.getName().equals(MathMLTags.OTHERWISE)){
				Element pieceExp = (Element)child.getChildren().get(0);
				SimpleNode vcellExp = getRootNode(pieceExp);
				//
				// add term for this conditional expression (pieceExp * pieceCond)
				//
				ASTMultNode multNode = new ASTMultNode();
				multNode.jjtAddChild(vcellExp);
				//
				// form "otherwise" conditional expression
				//
				ASTRelationalNode equalNode = new ASTRelationalNode();
				equalNode.setOperator(Operator.EQ);
				ASTFloatNode falseNode = new ASTFloatNode(0.0);
				equalNode.jjtAddChild(falseNode);
				//
				// gather conditionals
				//
				ASTAndNode andNode = new ASTAndNode();
				for (int j = 0; j < conditionExpList.size(); j++){
					andNode.jjtAddChild((SimpleNode)conditionExpList.elementAt(j));
				}
				equalNode.jjtAddChild(andNode);
				multNode.jjtAddChild(equalNode);
				addNode.jjtAddChild(multNode);
			}
		}
		return addNode;
	} else if (nodeMathML.getName().equals(MathMLTags.CONSTANT)){
		org.jdom.Attribute typeAttribute = nodeMathML.getAttribute("type");
		if (typeAttribute == null || typeAttribute.getValue().equals("integer") || typeAttribute.getValue().equals("real")) {
			String floatString = nodeMathML.getTextTrim();
			return new ASTFloatNode(Double.parseDouble(floatString));
		} else if (typeAttribute.getValue().equals("rational")) {
			java.util.List children = nodeMathML.getContent();
			org.jdom.Text numeratorTxt = (org.jdom.Text)children.get(0);
			long numerator = Long.parseLong(numeratorTxt.getTextTrim());
			org.jdom.Text denominatorTxt = (org.jdom.Text)children.get(2);
			long denominator = Long.parseLong(denominatorTxt.getTextTrim());
			double value = new Double(((double)numerator)/denominator).doubleValue();
			return new ASTFloatNode(value);
		} else if (typeAttribute.getValue().equals("e-notation")) {
			java.util.List children = nodeMathML.getContent();
			org.jdom.Text mantissaTxt = (org.jdom.Text)children.get(0);
			double mantissa = Double.parseDouble(mantissaTxt.getTextTrim());
			org.jdom.Text exponentTxt = (org.jdom.Text)children.get(2);
			long exponent = Long.parseLong(exponentTxt.getTextTrim());
			double value = new Double(mantissa+"E"+exponent).doubleValue();
			return new ASTFloatNode(value);
		} else {
			throw new RuntimeException("MathML Parsing error : constant type "+typeAttribute.getValue() + " not supported.");
		}
	} else if (nodeMathML.getName().equals(MathMLTags.IDENTIFIER)){
		ASTIdNode idNode = new ASTIdNode();
		idNode.name = nodeMathML.getTextTrim();
		return idNode;
	} else if (nodeMathML.getName().equals(MathMLTags.FALSE)){
		ASTFloatNode falseNode = new ASTFloatNode(0.0);
		return falseNode;
	} else if (nodeMathML.getName().equals(MathMLTags.TRUE)){
		ASTFloatNode trueNode = new ASTFloatNode(1.0);
		return trueNode;
	} else if (nodeMathML.getName().equals(MathMLTags.E)){
		ASTFloatNode eNode = new ASTFloatNode(Math.E);
		return eNode;
	} else if (nodeMathML.getName().equals(MathMLTags.PI)){
		ASTFloatNode piNode = new ASTFloatNode(Math.PI);
		return piNode;
	} else if (nodeMathML.getName().equals(MathMLTags.BVAR)){
		SimpleNode bVarNode = getRootNode((Element)nodeMathML.getChildren().get(0));
		return bVarNode;
	} else if (nodeMathML.getName().equals(MathMLTags.MATH)){
		SimpleNode node = getRootNode((Element)nodeMathML.getChildren().get(0));
		return node;
	} else{
		throw new ExpressionException("node type '"+nodeMathML.getName()+"' not supported yet");
	}		
}


/**
 * Insert the method's description here.
 * Creation date: (2/11/2002 1:34:06 PM)
 * @param nodeArg org.w3c.dom.Node
 * @return cbit.vcell.parser.Expression
 * @throws ExpressionException
 */
public org.jdom.Element node2Element(org.w3c.dom.Node nodeArg) throws ExpressionException{
	if (nodeArg== null) {
		throw new IllegalArgumentException("Invalid null Node");
	}
	
	//process node
	org.jdom.Element element = null;
	
	if (nodeArg.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE){
		element = new org.jdom.Element(nodeArg.getNodeName());

		//process attributes
		org.w3c.dom.NamedNodeMap attrMap = nodeArg.getAttributes();
		if (attrMap !=null) {
			for (int i = 0; i < attrMap.getLength(); i++){
				org.w3c.dom.Node temp = attrMap.item(i);

				if (temp.getNodeType()== org.w3c.dom.Node.ATTRIBUTE_NODE) {
					org.jdom.Attribute attribute = new org.jdom.Attribute(temp.getLocalName(), temp.getNodeValue());
					element.setAttribute(attribute);
				} else {
					throw new ExpressionException("Unknown node "+temp.getNodeName()+" type "+ temp.getNodeType() + " when processing attributes!");
				}
			}
		}
		
		//process content
		//get nodes
		org.w3c.dom.NodeList nodeList = nodeArg.getChildNodes();

		if (nodeList.getLength()==1) {
			element.addContent(nodeList.item(0).getNodeValue());
		}
		else {
			for (int i = 0; i < nodeList.getLength(); i++){
				if (nodeList.item(i).getNodeType()==org.w3c.dom.Node.ELEMENT_NODE) {
					element.addContent(node2Element(nodeList.item(i)));
				}
			}
		}
	} else {
		throw new ExpressionException("Unknown node "+nodeArg.getNodeName()+" type "+ nodeArg.getNodeType());
	}
	return element;
}
}