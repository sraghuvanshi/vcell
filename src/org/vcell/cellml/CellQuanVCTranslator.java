package org.vcell.cellml;
import cbit.vcell.parser.Expression;
import cbit.vcell.parser.ExpressionException;
import cbit.vcell.parser.ExpressionMathMLParser;
import cbit.vcell.parser.MathMLTags;
import cbit.vcell.server.PropertyLoader;
import cbit.vcell.xml.XmlParseException;
import cbit.vcell.xml.NameList;
import cbit.vcell.xml.NameManager;
import cbit.util.xml.XmlUtil;
import cbit.util.TokenMangler;
import cbit.vcell.xml.XMLTags;

import org.jdom.Comment;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.filter.ContentFilter;
import org.jdom.filter.ElementFilter;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * Implementation of the translation from a CellML Quantitative model to a VCML math model. The actual mapping is (ignoring possible mangling):
 model.name = MathModel.Name
 model.name = MathModel.MathDescription.Name
 model.name = MathModel.Geometry.SubVolume.Name
 model.name = MathModel.MathDescription.CompartmentSubDomain.Name   //only one compartment
 model.component.math.apply.apply.ci.Text = MathModel.MathDescription.CompartmentSubDomain.OdeEquation.Name
 defaults ("Unknown") = MathModel.MathDescription.CompartmentSubDomain.OdeEquation.SolutionType
 model.component.math.apply.[cn | apply] = MathModel.MathDescription.CompartmentSubDomain.OdeEquation.Rate.Text     //differential equation math
 model.component.variable.initial_amount = MathModel.MathDescription.CompartmentSubDomain.OdeEquation.Initial
 [model.component.math.apply.apply.ci.Text | model.component.variable.name] = MathModel.MathDescription.VolumeVariable.Name
 [model.component.math.apply.ci.Text | model.component.variable.name] = MathModel.MathDescription.Function.Name
 model.component.math.apply.[apply | ci] = MathModel.MathDescription.Function.Text
 
 - MathModel.Geometry: default values
 - MathModel.MathDescription.CompartmentSubDomain.BoundaryType: default values
 - Special treatment for rate functions
 * Creation date: (9/23/2003 12:03:24 PM)
 * @author: Rashad Badrawi
 */
public class CellQuanVCTranslator extends Translator {

	public static final String GEOM_NAME = "Default";
	public static final String SUBVOL_NAME = "Compartmental";

	protected NameManager nm;
	protected NameList nl;
	protected Namespace sNamespace, tNamespace, sAttNamespace, mathns;

	protected CellQuanVCTranslator() { 

		//sNamespace = Namespace.getNamespace(CELLML_NS_PREFIX, CELLML_NS);
		sNamespace = Namespace.getNamespace(CELLMLTags.CELLML_NS);
		sAttNamespace = Namespace.getNamespace("");                   //dummy NS  
		tNamespace = Namespace.getNamespace(XMLTags.VCML_NS);
		mathns = Namespace.getNamespace(MATHML_NS);
		nm = new NameManager();
		nl = new NameList();
		schemaLocation = CELLMLTags.CELLML_NS + " " + PropertyLoader.getProperty(PropertyLoader.cellmlSchemaUrlProperty, Translator.DEF_CELLML_SL);
		schemaLocationPropName = XmlUtil.NS_SCHEMA_LOC_PROP_NAME;
    } 


    //defaults?
    protected void addBoundaryTypes(Element csd) {

		Element bt = new Element(XMLTags.BoundaryTypeTag, tNamespace);
		String boundTypes [] = {XMLTags.BoundaryAttrValueXm, XMLTags.BoundaryAttrValueXp, XMLTags.BoundaryAttrValueYm, 
								XMLTags.BoundaryAttrValueYp, XMLTags.BoundaryAttrValueZm, XMLTags.BoundaryAttrValueZp};
		String temp = "Value";
		for (int i = 0; i < boundTypes.length; i++) {
			bt.setAttribute(XMLTags.BoundaryAttrTag, boundTypes[i]);
			bt.setAttribute(XMLTags.BoundaryTypeAttrTag, temp);
			csd.addContent((Element)bt.clone());
		}
	}


//we pick the variable name by reading through the math ml. 
//redundancy in picking up the variable name with the volume variable?
	protected void addCompartmentSubDomain(Element mathDesc) {

	    Element csd = new Element(XMLTags.CompartmentSubDomainTag, tNamespace);
	    csd.setAttribute(XMLTags.NameTag, sRoot.getAttributeValue(CELLMLTags.name, sAttNamespace));
	    addBoundaryTypes(csd);
		JDOMTreeWalker walker = new JDOMTreeWalker(sRoot, new ElementFilter(CELLMLTags.COMPONENT));
	    Element comp, math;
	    String compName, varName, mangledName;
	    while (walker.hasNext()) {
			comp = (Element)walker.next();
			compName = comp.getAttributeValue(CELLMLTags.name, sAttNamespace);
			Iterator i = comp.getChildren(CELLMLTags.MATH, mathns).iterator();
			while (i.hasNext()) {
				math = (Element)i.next();
				Element apply, apply2, apply3, diff, ci;
				//allow multiple 'apply' children.
				Iterator j = math.getChildren(MathMLTags.APPLY, mathns).iterator();
				while (j.hasNext()) { 
					apply = (Element)j.next();
					ArrayList list = new ArrayList(apply.getChildren());
					if (list.size() < 3)
						continue;
					if (!((Element)list.get(0)).getName().equals(MathMLTags.EQUAL))
						continue; 
					apply2 = (Element)list.get(1);
					if (!apply2.getName().equals(MathMLTags.APPLY))
						continue;
					ArrayList list2 = new ArrayList(apply2.getChildren());
					if (list2.size() < 3)
						continue;
					if (!((Element)list2.get(0)).getName().equals(MathMLTags.DIFFERENTIAL))
						continue;
					ci = (Element)list2.get(2);    //skip the time variable
					varName = ci.getTextTrim();
					apply3 = (Element)list.get(2);       //can be a constant
					
					mangledName = nm.getMangledName(compName, varName);
					Element ode = new Element(XMLTags.OdeEquationTag, tNamespace);
					ode.setAttribute(XMLTags.NameTag, mangledName);
					ode.setAttribute(XMLTags.SolutionTypeTag, XMLTags.UnknownTypeTag);           
					Element rate = new Element(XMLTags.RateTag, tNamespace);
					Element trimmedMath = new Element(CELLMLTags.MATH, mathns).addContent((Element)apply3.detach());
					fixMathMLBug(trimmedMath);
					Expression exp = null;
					try {
						exp = (new ExpressionMathMLParser(null)).fromMathML(trimmedMath);
						exp = processMathExp(comp, exp);
						exp = exp.flatten();
						nl.mangleString(exp.infix());
						rate.setText(exp.infix());
					} catch (ExpressionException e) {
						e.printStackTrace(System.out);
						throw new RuntimeException(e.getMessage());
					}

					Element initial = new Element(XMLTags.InitialTag, tNamespace);
					initial.setText(getInitial(comp, varName));     //the same var name

					ode.addContent(rate);
					ode.addContent(initial);
					csd.addContent(ode);
				}
			}
	    }
	    mathDesc.addContent(csd);
    }


    private void addFunction(Element mathDesc, Element temp, Element comp, String mangledName) {
 
	    String expStr = null;                                               //force an exception 
	    Element function = new Element(XMLTags.FunctionTag, tNamespace);
		function.setAttribute(XMLTags.NameTag, mangledName);
		Element parent = temp.getParent();
		Element sibling = parent.getChild(MathMLTags.APPLY, mathns);   
		if (sibling == null) {                          //check if its value is assigned to another variable (i.e. A = B)
			ArrayList list = new ArrayList(parent.getChildren(MathMLTags.IDENTIFIER, mathns));
			if (list.size() == 2) {
				expStr = ((Element)list.get(1)).getTextTrim();
			}
			if (expStr == null || expStr.length() == 0) {
				expStr = parent.getChildText(MathMLTags.CONSTANT, mathns);
			}
			if (expStr == null || expStr.length() == 0) {                         //check if 'piecewise'
				sibling = parent.getChild(MathMLTags.PIECEWISE, mathns);
			}
		} 
		if (sibling != null) { 
			Element trimmedMath = new Element(CELLMLTags.MATH, mathns).addContent((Element)sibling.detach());
			fixMathMLBug(trimmedMath); 
			Expression exp = null;
			try {
				exp = (new ExpressionMathMLParser(null)).fromMathML(trimmedMath);
			} catch (ExpressionException e) {
				e.printStackTrace(System.out);
				throw new RuntimeException(e.getMessage());
			}
			exp = processMathExp(comp, exp);
			expStr = exp.infix();
			nl.mangleString(expStr);
		}
		function.setText(expStr);
		mathDesc.addContent(function);
    }


	//use a dummy compartmental geometry
	protected void addGeometry() {

		Element geom = new Element(XMLTags.GeometryTag, tNamespace);
		geom.setAttribute(XMLTags.NameTag, GEOM_NAME);
		geom.setAttribute(XMLTags.DimensionAttrTag, "0");
		
		Element extent = new Element(XMLTags.ExtentTag, tNamespace);
		String temp = "10.0";
		extent.setAttribute(XMLTags.XAttrTag, temp);
		extent.setAttribute(XMLTags.YAttrTag, temp);
		extent.setAttribute(XMLTags.ZAttrTag, temp);
		geom.addContent(extent);
		
		Element origin = new Element(XMLTags.OriginTag, tNamespace);
		temp = "0.0";
		origin.setAttribute(XMLTags.XAttrTag, temp);
		origin.setAttribute(XMLTags.YAttrTag, temp);
		origin.setAttribute(XMLTags.ZAttrTag, temp);
		geom.addContent(origin);

		Element subVol = new Element(XMLTags.SubVolumeAttrTag, tNamespace);
		subVol.setAttribute(XMLTags.NameTag, sRoot.getAttributeValue(CELLMLTags.name, sAttNamespace));
		subVol.setAttribute(XMLTags.HandleAttrTag, "0");
		subVol.setAttribute(XMLTags.TypeAttrTag, SUBVOL_NAME);
		geom.addContent(subVol);
		
		tRoot.addContent(geom);
	}


    protected void addMathDescription() {

	    Element mathDesc = new Element(XMLTags.MathDescriptionTag, tNamespace);
	    mathDesc.setAttribute(XMLTags.NameTag, sRoot.getAttributeValue(CELLMLTags.name, sAttNamespace));
	    processVariables(mathDesc);
	  	addCompartmentSubDomain(mathDesc);
	  	tRoot.addContent(mathDesc);
    }


	protected void addMathModel() {

		trimAndMangleSource();                                         
		Comment comment = new Comment("VCML math model created from a CELLML Quantitative model");
		tRoot = new Element(XMLTags.MathModelTag, tNamespace);
		tRoot.addContent(comment);
		tRoot.setAttribute(XMLTags.NameTag, sRoot.getAttributeValue(CELLMLTags.name, sAttNamespace));
		addVarsAndConns();
		addGeometry();
		addMathDescription();
	}


	private void addRateFunction (Element mathDesc, Element varRef, String compName, String mangledName) {

	    Element role;  
		String stoich = null, roleAtt = null;
		//can only imply the delta function if the stoichiometry attribute is present also
		//not sure if there can be more than one, if not, could just pass in the 'role' element itself.
		Iterator i = varRef.getChildren(CELLMLTags.ROLE, sNamespace).iterator();
		while (i.hasNext()) {
			role = (Element)i.next();
			stoich = role.getAttributeValue(CELLMLTags.stoichiometry, sAttNamespace);
			roleAtt = role.getAttributeValue(CELLMLTags.role, sAttNamespace);
			if (stoich != null)
				break;
		}
		if (stoich != null) {
		//Can only imply the function if a variable_ref with a role of "rate" exists
			JDOMTreeWalker reactionWalker = new JDOMTreeWalker(varRef.getParent(), new ElementFilter(CELLMLTags.ROLE));
			Element rateRole = reactionWalker.getMatchingElement(CELLMLTags.role, sAttNamespace, CELLMLTags.rateRole);
			if (rateRole != null) {
				String rateVarName = rateRole.getParent().getAttributeValue(CELLMLTags.variable, sAttNamespace);
				Element function = new Element(XMLTags.FunctionTag, tNamespace);
				function.setAttribute(XMLTags.NameTag, mangledName);   
				StringBuffer formula = new StringBuffer("(");
				if (roleAtt.equals(CELLMLTags.productRole))
					formula.append("-(");
				formula.append(stoich + "*" + nm.getMangledName(compName, rateVarName));
				if (roleAtt.equals(CELLMLTags.productRole))
					formula.append(")");
				formula.append(")");
				function.setText(formula.toString());
				mathDesc.addContent(function);
			}
		}
    }


//add all the components and variables to the name manager, and resolve all connections 
    protected void addVarsAndConns() { 
 
	    JDOMTreeWalker walker = new JDOMTreeWalker(sRoot, new ElementFilter(CELLMLTags.VARIABLE));
		Element temp;
		String firstKey, secondKey;
		while (walker.hasNext()) {
	    	temp = (Element)walker.next();
	    	firstKey = temp.getParent().getAttributeValue(CELLMLTags.name, sAttNamespace);
	    	secondKey = temp.getAttributeValue(CELLMLTags.name, sAttNamespace);
	    	nm.add(firstKey, secondKey); 
		}

		walker = new JDOMTreeWalker(sRoot, new ElementFilter(CELLMLTags.CONNECTION));
		Element mapComp, mapVar;
		String comp1, comp2, var1, var2;
		boolean flag;

		while (walker.hasNext()) {
	    	temp = (Element)walker.next();
	    	mapComp = temp.getChild(CELLMLTags.MAP_COMP, sNamespace);
	    	comp1 = mapComp.getAttributeValue(CELLMLTags.comp1, sAttNamespace);
	    	comp2 = mapComp.getAttributeValue(CELLMLTags.comp2, sAttNamespace);
	    	Iterator i = temp.getChildren(CELLMLTags.MAP_VAR, sNamespace).iterator();
	    	while (i.hasNext()) {
				mapVar = (Element)i.next();
				var1 = mapVar.getAttributeValue(CELLMLTags.var1, sAttNamespace);
				var2 = mapVar.getAttributeValue(CELLMLTags.var2, sAttNamespace);
				flag = isVisible(comp1, var1);
				boolean connected;
				if (flag) {  	//component1/variable1 is the "output" variable so connect component2/variable2 to it
		    		connected = nm.connect(comp2, var2, comp1, var1);
				} else {                 //connect to component2/variable2 if its the output
					flag = isVisible(comp2, var2);
					if (flag) {
		  		    	connected = nm.connect(comp1, var1, comp2, var2);
					} else {               //or allow connections to remain chained (in case the model defines groups)  
						flag = isMiddleComp(comp1, var1);
						if (flag) {
	  		    			connected = nm.connect(comp2, var2, comp1, var1);
						} else {
		  		    		connected = nm.connect(comp1, var1, comp2, var2);	
						}
					}
				}
				if (!connected)
					System.err.println("Error: Unable to connect variables: " + comp1 + " " + var1 + ", " + comp2 + " " + var2);
	    	}
		}
		nm.generateMangledNames();
		//System.out.println(nm.dumpNames());
    }


    //temporary method. 
	private void fixMathMLBug(Element math) {
		
		ArrayList l = new ArrayList();
		Iterator walker = new JDOMTreeWalker(math, new ContentFilter(ContentFilter.ELEMENT));
		while (walker.hasNext())
			l.add(walker.next());
      	Element temp;
      	for (int i = 0; i < l.size(); i++) {     
	      	temp = (Element)l.get(i);
	      	if (temp.getName().equals(MathMLTags.CONSTANT)) {
		    	ArrayList atts = new ArrayList(temp.getAttributes());
				org.jdom.Attribute att;
				for (int j = 0; j < atts.size(); j++) {
					att = (org.jdom.Attribute)atts.get(j);
					temp.removeAttribute(att.getName(), sNamespace);
				}
				temp.removeNamespaceDeclaration(sNamespace);
	      	} 
      	}
	}


    private String getInitial(Element comp, String variableName) {

		String initial = "0.0"; 
		Element temp;
		
		JDOMTreeWalker walker = new JDOMTreeWalker(comp, new ElementFilter(CELLMLTags.VARIABLE));
		temp = walker.getMatchingElement(CELLMLTags.name, sAttNamespace, variableName);
		if (temp != null) {
			initial = temp.getAttributeValue(CELLMLTags.initial_value, sAttNamespace);
			if (initial == null || initial.length() == 0)
				initial = "0.0";
		}
		
		return initial;
    }


    private Element getMatchingVarRef(Element comp, String varName) { 

		JDOMTreeWalker walker = new JDOMTreeWalker(comp, new ElementFilter(CELLMLTags.ROLE));
		Element temp = walker.getMatchingElement(CELLMLTags.delta_variable, sAttNamespace, varName);
		if (temp != null) {
			return temp.getParent();
		} else {
			return null;
		}
    } 

 
	private Element getMathElementIdentifier(Element comp, String varName, String elementName) {
 
		JDOMTreeWalker i = new JDOMTreeWalker(comp, new ElementFilter(CELLMLTags.MATH));
		Element math, apply, eq, apply2, diff, target = null;
		while (i.hasNext()) {
			math = (Element)i.next();
			//this first 'apply' loop because some components define all the vars under one 'math' element.
			Iterator m = math.getChildren(MathMLTags.APPLY, mathns).iterator();
			while (m.hasNext()) {
				apply = (Element)m.next();
				Iterator j = apply.getChildren().iterator();        //this element can also have an ID. 
				eq = (Element)j.next();                             //element ordering
				if (eq == null || !eq.getName().equals(MathMLTags.EQUAL))
					continue;
				while (j.hasNext()) {
					if (elementName.equals(MathMLTags.IDENTIFIER)) {             //one extra iteration
						target = (Element)j.next();
						//System.out.println("target:" + target.getName() + " " + target.getTextTrim() + " " + varName);
						if (!(target.getName().equals(MathMLTags.IDENTIFIER) && target.getTextTrim().equals(varName)))    //do the first 'ci' only
							target = null;
						break;
					} else if (elementName.equals(MathMLTags.DIFFERENTIAL)) {
						apply2 = (Element)j.next();
						if (!apply2.getName().equals(MathMLTags.APPLY))
							continue;
						Iterator k = apply2.getChildren().iterator();
						diff = (Element)k.next();
						if (!diff.getName().equals(MathMLTags.DIFFERENTIAL))
							continue;
						while (k.hasNext()) {
							target = (Element)k.next();
							if (target.getName().equals(MathMLTags.IDENTIFIER) && target.getTextTrim().equals(varName))
								break;
							else
								target = null;                      //need another break ?
						}
					}
				}
				if (target != null)                       //target found, don't overwrite it!
					break;
			}
			if (target != null)
				break;
		}
		
		return target;
    }


	//Used to ensure that when reaction parameters are used more than once they have unique identifers
	private String getUniqueParamName(String name) {
 
		String newName = new String(name);
		String temp = nl.getMangledName(name, "dummy");
		while (temp.length() > 0) {
			//the name has already been used, so append an '_' and try again
			newName = newName + "_";
			temp = nl.getMangledName(newName, "dummy");
		}
		//we have a name which has not been used, so add it to the hashtable and write it out/return it
		nl.setMangledName(name, "dummy", newName);                   

		return newName;
    }


	private boolean isMiddleComp(String comp, String var) {
 
		boolean flag = false;
		String privateInterface; 

		JDOMTreeWalker walker = new JDOMTreeWalker(sRoot, new ElementFilter(CELLMLTags.COMPONENT));
		Element matchingComp = walker.getMatchingElement(CELLMLTags.name, sAttNamespace, comp);
		if (matchingComp != null) {
			walker = new JDOMTreeWalker(matchingComp, new ElementFilter(CELLMLTags.VARIABLE));
			Element matchingVar = walker.getMatchingElement(CELLMLTags.name, sAttNamespace, var);
			if (matchingVar != null) {
				privateInterface = matchingVar.getAttributeValue(CELLMLTags.private_interface, sAttNamespace);
		    	if (CELLMLTags.outInterface.equals(privateInterface)) 
					flag = true;
			}
		}

		return flag;
	}


	private boolean isTimeVar(String varName) { 

		boolean flag = false;
		JDOMTreeWalker walker = new JDOMTreeWalker(sRoot, new ElementFilter(MathMLTags.BVAR));
		while (walker.hasNext()) {
			Element bvar = (Element)walker.next();
			String timeVar = bvar.getChildText(MathMLTags.IDENTIFIER, mathns);
			if (varName.trim().equals(timeVar.trim())) {
				flag = true;
				break;
			}
		}

		return flag;
	}


    //the absence of the 'optional' interface atts will result in a 'true' return.
	private boolean isVisible(String comp, String var) {
  
		boolean flag = false;
		String publicInterface, privateInterface;

		JDOMTreeWalker walker = new JDOMTreeWalker(sRoot, new ElementFilter(CELLMLTags.COMPONENT));
		Element matchingComp = walker.getMatchingElement(CELLMLTags.name, sAttNamespace, comp);
		if (matchingComp != null) {
			walker = new JDOMTreeWalker(matchingComp, new ElementFilter(CELLMLTags.VARIABLE));
			Element matchingVar = walker.getMatchingElement(CELLMLTags.name, sAttNamespace, var);
			if (matchingVar != null) {
				publicInterface = matchingVar.getAttributeValue(CELLMLTags.public_interface, sAttNamespace);
				if (publicInterface == null)
					publicInterface = "";
				privateInterface = matchingVar.getAttributeValue(CELLMLTags.private_interface, sAttNamespace);
				if (privateInterface == null)
					privateInterface = "";
		    	if (!publicInterface.equals(CELLMLTags.inInterface) && !privateInterface.equals(CELLMLTags.inInterface)) 
					flag = true;
			}
		}

		return flag;
	}


   	//post-process a mathematical expression string to handle the variable mapping
	private Expression processMathExp(Element comp, Expression exp) {

		try {
			String [] symbols = exp.getSymbols();
			String mName;
			JDOMTreeWalker walker;
			Element temp;
			if (symbols == null)
				return exp;
			for (int i = 0; i < symbols.length; i++) {
				walker = new JDOMTreeWalker(comp, new ElementFilter(CELLMLTags.VARIABLE));  
				temp = walker.getMatchingElement(CELLMLTags.name, sAttNamespace, symbols[i]);
				if (temp != null) {
					mName = nm.getMangledName(comp.getAttributeValue(CELLMLTags.name, sAttNamespace), symbols[i]);
					if (!mName.equals(symbols[i])) {
						exp = exp.getSubstitutedExpression(new Expression(symbols[i]), new Expression(mName));
					}
				}
				//added for a temporary fix for time:
				if (symbols[i].equals(CELLMLTags.timeVarCell)) {
					exp = exp.getSubstitutedExpression(new Expression(symbols[i]), new Expression(CELLMLTags.timeVar));
				}
			}
		} catch (ExpressionException e) {
			e.printStackTrace();
			return exp;
		}

		return exp;
	}


    protected void processVariables(Element mathDesc) { 

	    JDOMTreeWalker walker = new JDOMTreeWalker(sRoot, new ElementFilter(CELLMLTags.COMPONENT));
	    Element comp, var, varRef, temp;
	    String compName, varName, publicIn, privateIn, mangledName; 
	    while (walker.hasNext()) {
			comp = (Element)walker.next();
			compName = comp.getAttributeValue(CELLMLTags.name, sAttNamespace);
			JDOMTreeWalker varWalker = new JDOMTreeWalker(comp, new ElementFilter(CELLMLTags.VARIABLE));
			while (varWalker.hasNext()) {
				var = (Element)varWalker.next();
				publicIn = var.getAttributeValue(CELLMLTags.public_interface, sAttNamespace);
				privateIn = var.getAttributeValue(CELLMLTags.private_interface, sAttNamespace);
				if ( (publicIn != null && publicIn.equals(CELLMLTags.inInterface)) ||
					 (privateIn != null && privateIn.equals(CELLMLTags.inInterface)) )
					continue;
				varName = var.getAttributeValue(CELLMLTags.name, sAttNamespace);
			    mangledName = nm.getMangledName(compName, varName);
				String uniqueVarName = getUniqueParamName(mangledName);
				System.out.println("Vars: " + compName + "." + varName + " " + mangledName + " " + uniqueVarName);
			    //declare any differential free variables as VolumeVariable's
			    temp = getMathElementIdentifier(comp, varName, MathMLTags.DIFFERENTIAL);
			    if (temp != null) {                                        //formula ignored at this point
					Element volVar = new Element(XMLTags.VolumeVariableTag, tNamespace);
					volVar.setAttribute(XMLTags.NameTag, mangledName);
					mathDesc.addContent(volVar);
					continue;
			    }
			      //any LHS variables of an eq operator are functions
			    temp = getMathElementIdentifier(comp, varName, MathMLTags.IDENTIFIER);
				if (temp != null) {
					addFunction(mathDesc, temp, comp, mangledName);
					continue;
				}
				//Handle delta variables as a special case function
				varRef = getMatchingVarRef(comp, varName);
				if (varRef != null) {
					addRateFunction(mathDesc, varRef, compName, uniqueVarName);    //2 functions with same name?
					continue;
				}
				//check if its the time variable, if not add as a constant.
				//no handling for a specific value for the time var.
				if (!isTimeVar(varName)) {
					Element constant = new Element(XMLTags.ConstantTag, tNamespace);
					constant.setAttribute(XMLTags.NameTag, uniqueVarName);
					constant.setText(getInitial(comp, varName));                 //some redundancy in this call
					mathDesc.addContent(constant);
				}
			}
	    }
    }


	protected void translate() {

		addMathModel();	
	}


	private void trimAndMangleSource() {

		String elements [] = { CELLMLTags.MODEL, CELLMLTags.COMPONENT, CELLMLTags.VARIABLE, CELLMLTags.REACTION, 
							   CELLMLTags.VAR_REF, CELLMLTags.ROLE, CELLMLTags.CONNECTION, CELLMLTags.MAP_COMP, 
							   CELLMLTags.MAP_VAR, CELLMLTags.UNITS, CELLMLTags.UNIT};
		TransFilter ts = new TransFilter(elements, null, TransFilter.QUANCELLVC_MANGLE);
		ts.filter(sRoot);
	}
}