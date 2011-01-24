//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.1-b02-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.11.09 at 03:55:47 PM EST 
//


package neuroml.bio;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * Specific axial resistance of a group of sections
 * 
 * <p>Java class for SpecAxialResistance complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SpecAxialResistance">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="parameter" type="{http://morphml.org/biophysics/schema}UnnamedParameter" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="variableParameter" type="{http://morphml.org/biophysics/schema}VariableParameter" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SpecAxialResistance", propOrder = {
    "parameter",
    "variableParameter"
})
public class SpecAxialResistance {

    protected List<UnnamedParameter> parameter;
    protected List<VariableParameter> variableParameter;

    /**
     * Gets the value of the parameter property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the parameter property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getParameter().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UnnamedParameter }
     * 
     * 
     */
    public List<UnnamedParameter> getParameter() {
        if (parameter == null) {
            parameter = new ArrayList<UnnamedParameter>();
        }
        return this.parameter;
    }

    /**
     * Gets the value of the variableParameter property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the variableParameter property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getVariableParameter().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link VariableParameter }
     * 
     * 
     */
    public List<VariableParameter> getVariableParameter() {
        if (variableParameter == null) {
            variableParameter = new ArrayList<VariableParameter>();
        }
        return this.variableParameter;
    }

}
