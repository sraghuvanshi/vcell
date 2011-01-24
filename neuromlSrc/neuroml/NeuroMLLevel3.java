//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.1-b02-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.11.09 at 03:55:47 PM EST 
//


package neuroml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import neuroml.channel.ChannelML;
import neuroml.meta.Annotation;
import neuroml.meta.Authors;
import neuroml.meta.LengthUnits;
import neuroml.meta.NeuronDBReference;
import neuroml.meta.Properties;
import neuroml.meta.Publication;
import neuroml.meta.VolumeUnits;
import neuroml.network.Populations;
import neuroml.network.Projections;


/**
 * Description of neuronal models, including biophysics and channel mechanisms, and network connections (NeuroML Level 3).
 * 
 * <p>Java class for NeuroMLLevel3 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NeuroMLLevel3">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;group ref="{http://morphml.org/metadata/schema}metadata"/>
 *         &lt;group ref="{http://morphml.org/metadata/schema}referencedata"/>
 *         &lt;element name="cells" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="cell" type="{http://morphml.org/neuroml/schema}Level3Cell" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="channels" type="{http://morphml.org/channelml/schema}ChannelML" minOccurs="0"/>
 *         &lt;group ref="{http://morphml.org/networkml/schema}CoreNetworkElements" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="lengthUnits" use="required" type="{http://morphml.org/metadata/schema}LengthUnits" />
 *       &lt;attribute name="volumeUnits" type="{http://morphml.org/metadata/schema}VolumeUnits" default="cubic_millimetre" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NeuroMLLevel3", propOrder = {
    "notes",
    "properties",
    "annotation",
    "group",
    "authorList",
    "publication",
    "neuronDBref",
    "cells",
    "channels",
    "populations",
    "projections"
})
public class NeuroMLLevel3 {

    @XmlElement(namespace = "http://morphml.org/metadata/schema")
    protected String notes;
    @XmlElement(namespace = "http://morphml.org/metadata/schema")
    protected Properties properties;
    @XmlElement(namespace = "http://morphml.org/metadata/schema")
    protected Annotation annotation;
    @XmlElement(namespace = "http://morphml.org/metadata/schema")
    protected List<String> group;
    @XmlElement(namespace = "http://morphml.org/metadata/schema")
    protected Authors authorList;
    @XmlElement(namespace = "http://morphml.org/metadata/schema")
    protected List<Publication> publication;
    @XmlElement(namespace = "http://morphml.org/metadata/schema")
    protected NeuronDBReference neuronDBref;
    protected NeuroMLLevel3 .Cells cells;
    protected ChannelML channels;
    @XmlElement(namespace = "http://morphml.org/networkml/schema")
    protected Populations populations;
    @XmlElement(namespace = "http://morphml.org/networkml/schema")
    protected Projections projections;
    @XmlAttribute
    protected String name;
    @XmlAttribute(required = true)
    protected LengthUnits lengthUnits;
    @XmlAttribute
    protected VolumeUnits volumeUnits;

    /**
     * Gets the value of the notes property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Sets the value of the notes property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNotes(String value) {
        this.notes = value;
    }

    /**
     * Gets the value of the properties property.
     * 
     * @return
     *     possible object is
     *     {@link Properties }
     *     
     */
    public Properties getProperties() {
        return properties;
    }

    /**
     * Sets the value of the properties property.
     * 
     * @param value
     *     allowed object is
     *     {@link Properties }
     *     
     */
    public void setProperties(Properties value) {
        this.properties = value;
    }

    /**
     * Gets the value of the annotation property.
     * 
     * @return
     *     possible object is
     *     {@link Annotation }
     *     
     */
    public Annotation getAnnotation() {
        return annotation;
    }

    /**
     * Sets the value of the annotation property.
     * 
     * @param value
     *     allowed object is
     *     {@link Annotation }
     *     
     */
    public void setAnnotation(Annotation value) {
        this.annotation = value;
    }

    /**
     * Gets the value of the group property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the group property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGroup().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getGroup() {
        if (group == null) {
            group = new ArrayList<String>();
        }
        return this.group;
    }

    /**
     * Gets the value of the authorList property.
     * 
     * @return
     *     possible object is
     *     {@link Authors }
     *     
     */
    public Authors getAuthorList() {
        return authorList;
    }

    /**
     * Sets the value of the authorList property.
     * 
     * @param value
     *     allowed object is
     *     {@link Authors }
     *     
     */
    public void setAuthorList(Authors value) {
        this.authorList = value;
    }

    /**
     * Gets the value of the publication property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the publication property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPublication().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Publication }
     * 
     * 
     */
    public List<Publication> getPublication() {
        if (publication == null) {
            publication = new ArrayList<Publication>();
        }
        return this.publication;
    }

    /**
     * Gets the value of the neuronDBref property.
     * 
     * @return
     *     possible object is
     *     {@link NeuronDBReference }
     *     
     */
    public NeuronDBReference getNeuronDBref() {
        return neuronDBref;
    }

    /**
     * Sets the value of the neuronDBref property.
     * 
     * @param value
     *     allowed object is
     *     {@link NeuronDBReference }
     *     
     */
    public void setNeuronDBref(NeuronDBReference value) {
        this.neuronDBref = value;
    }

    /**
     * Gets the value of the cells property.
     * 
     * @return
     *     possible object is
     *     {@link NeuroMLLevel3 .Cells }
     *     
     */
    public NeuroMLLevel3 .Cells getCells() {
        return cells;
    }

    /**
     * Sets the value of the cells property.
     * 
     * @param value
     *     allowed object is
     *     {@link NeuroMLLevel3 .Cells }
     *     
     */
    public void setCells(NeuroMLLevel3 .Cells value) {
        this.cells = value;
    }

    /**
     * Gets the value of the channels property.
     * 
     * @return
     *     possible object is
     *     {@link ChannelML }
     *     
     */
    public ChannelML getChannels() {
        return channels;
    }

    /**
     * Sets the value of the channels property.
     * 
     * @param value
     *     allowed object is
     *     {@link ChannelML }
     *     
     */
    public void setChannels(ChannelML value) {
        this.channels = value;
    }

    /**
     * Gets the value of the populations property.
     * 
     * @return
     *     possible object is
     *     {@link Populations }
     *     
     */
    public Populations getPopulations() {
        return populations;
    }

    /**
     * Sets the value of the populations property.
     * 
     * @param value
     *     allowed object is
     *     {@link Populations }
     *     
     */
    public void setPopulations(Populations value) {
        this.populations = value;
    }

    /**
     * Gets the value of the projections property.
     * 
     * @return
     *     possible object is
     *     {@link Projections }
     *     
     */
    public Projections getProjections() {
        return projections;
    }

    /**
     * Sets the value of the projections property.
     * 
     * @param value
     *     allowed object is
     *     {@link Projections }
     *     
     */
    public void setProjections(Projections value) {
        this.projections = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the lengthUnits property.
     * 
     * @return
     *     possible object is
     *     {@link LengthUnits }
     *     
     */
    public LengthUnits getLengthUnits() {
        return lengthUnits;
    }

    /**
     * Sets the value of the lengthUnits property.
     * 
     * @param value
     *     allowed object is
     *     {@link LengthUnits }
     *     
     */
    public void setLengthUnits(LengthUnits value) {
        this.lengthUnits = value;
    }

    /**
     * Gets the value of the volumeUnits property.
     * 
     * @return
     *     possible object is
     *     {@link VolumeUnits }
     *     
     */
    public VolumeUnits getVolumeUnits() {
        if (volumeUnits == null) {
            return VolumeUnits.CUBIC_MILLIMETRE;
        } else {
            return volumeUnits;
        }
    }

    /**
     * Sets the value of the volumeUnits property.
     * 
     * @param value
     *     allowed object is
     *     {@link VolumeUnits }
     *     
     */
    public void setVolumeUnits(VolumeUnits value) {
        this.volumeUnits = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="cell" type="{http://morphml.org/neuroml/schema}Level3Cell" maxOccurs="unbounded"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "cell"
    })
    public static class Cells {

        @XmlElement(required = true)
        protected List<Level3Cell> cell;

        /**
         * Gets the value of the cell property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the cell property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getCell().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Level3Cell }
         * 
         * 
         */
        public List<Level3Cell> getCell() {
            if (cell == null) {
                cell = new ArrayList<Level3Cell>();
            }
            return this.cell;
        }

    }
    

//NEUROML PLUGIN INSERTED CODE
//   From File : C:\Developer\eclipse\workspace\neuroml-api\conf/jaxb\neuroml.NeuroMLLevel3


	/** Get the list of cells in this NeuroML file */
	public List<Level3Cell> getCellList()
	{
		NeuroMLLevel3.Cells cells = getCells();
		if (cells == null) {
			neuroml.ObjectFactory ofac = new neuroml.ObjectFactory();
			cells = ofac.createNeuroMLLevel3Cells();
			setCells(cells);						
		}
		return cells.getCell();	
	}
	
	/** Get the list of populations in this NeuroML file */		
	public List<neuroml.network.Population> getPopulationList()
	{
		neuroml.network.Populations pops = getPopulations();
		if (pops == null) {
			neuroml.network.ObjectFactory ofac = new neuroml.network.ObjectFactory();
			pops = ofac.createPopulations();
			setPopulations(pops);						
		}
		return pops.getPopulation();
	}
	
	/** Get the list of projections in this NeuroML file */		
	public List<neuroml.network.Projection> getProjectionList()
	{
		neuroml.network.Projections projs = getProjections();
		if (projs == null) {
			neuroml.network.ObjectFactory ofac = new neuroml.network.ObjectFactory();
			projs = ofac.createProjections();
			setProjections(projs);						
		}
		return projs.getProjection();
	}

}
