//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.1-b02-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.11.09 at 03:55:47 PM EST 
//


package neuroml.morph;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;


/**
 * <p>Java class for SpineShape.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="SpineShape">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="mushroom"/>
 *     &lt;enumeration value="stubby"/>
 *     &lt;enumeration value="thin"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlEnum
public enum SpineShape {

    @XmlEnumValue("mushroom")
    MUSHROOM("mushroom"),
    @XmlEnumValue("stubby")
    STUBBY("stubby"),
    @XmlEnumValue("thin")
    THIN("thin");
    private final String value;

    SpineShape(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SpineShape fromValue(String v) {
        for (SpineShape c: SpineShape.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
