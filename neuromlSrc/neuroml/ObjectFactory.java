//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.1-b02-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.11.09 at 03:55:47 PM EST 
//


package neuroml;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the neuroml package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Neuroml_QNAME = new QName("http://morphml.org/neuroml/schema", "neuroml");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: neuroml
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link NeuroMLLevel3 .Cells }
     * 
     */
    public NeuroMLLevel3 .Cells createNeuroMLLevel3Cells() {
        return new NeuroMLLevel3 .Cells();
    }

    /**
     * Create an instance of {@link Level3Biophysics }
     * 
     */
    public Level3Biophysics createLevel3Biophysics() {
        return new Level3Biophysics();
    }

    /**
     * Create an instance of {@link Level3Cell }
     * 
     */
    public Level3Cell createLevel3Cell() {
        return new Level3Cell();
    }

    /**
     * Create an instance of {@link NeuroMLLevel3 }
     * 
     */
    public NeuroMLLevel3 createNeuroMLLevel3() {
        return new NeuroMLLevel3();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NeuroMLLevel3 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://morphml.org/neuroml/schema", name = "neuroml")
    public JAXBElement<NeuroMLLevel3> createNeuroml(NeuroMLLevel3 value) {
        return new JAXBElement<NeuroMLLevel3>(_Neuroml_QNAME, NeuroMLLevel3 .class, null, value);
    }

}
