//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.05.11 at 10:08:16 AM NOVT 
//


package com.ers.dto;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for instancesReport complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="instancesReport"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="xmlData" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="relationName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="numInstances" type="{http://www.w3.org/2001/XMLSchema}integer"/&gt;
 *         &lt;element name="numAttributes" type="{http://www.w3.org/2001/XMLSchema}integer"/&gt;
 *         &lt;element name="numClasses" type="{http://www.w3.org/2001/XMLSchema}integer"/&gt;
 *         &lt;element name="className" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "instancesReport", propOrder = {
    "xmlData",
    "relationName",
    "numInstances",
    "numAttributes",
    "numClasses",
    "className"
})
public class InstancesReport {

    @XmlElement(required = true)
    protected String xmlData;
    @XmlElement(required = true)
    protected String relationName;
    @XmlElement(required = true)
    protected BigInteger numInstances;
    @XmlElement(required = true)
    protected BigInteger numAttributes;
    @XmlElement(required = true)
    protected BigInteger numClasses;
    @XmlElement(required = true)
    protected String className;

    /**
     * Gets the value of the xmlData property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXmlData() {
        return xmlData;
    }

    /**
     * Sets the value of the xmlData property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXmlData(String value) {
        this.xmlData = value;
    }

    /**
     * Gets the value of the relationName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRelationName() {
        return relationName;
    }

    /**
     * Sets the value of the relationName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRelationName(String value) {
        this.relationName = value;
    }

    /**
     * Gets the value of the numInstances property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumInstances() {
        return numInstances;
    }

    /**
     * Sets the value of the numInstances property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumInstances(BigInteger value) {
        this.numInstances = value;
    }

    /**
     * Gets the value of the numAttributes property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumAttributes() {
        return numAttributes;
    }

    /**
     * Sets the value of the numAttributes property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumAttributes(BigInteger value) {
        this.numAttributes = value;
    }

    /**
     * Gets the value of the numClasses property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumClasses() {
        return numClasses;
    }

    /**
     * Sets the value of the numClasses property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumClasses(BigInteger value) {
        this.numClasses = value;
    }

    /**
     * Gets the value of the className property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClassName() {
        return className;
    }

    /**
     * Sets the value of the className property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClassName(String value) {
        this.className = value;
    }

}
