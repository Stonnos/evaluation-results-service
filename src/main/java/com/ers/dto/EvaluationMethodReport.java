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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for evaluationMethodReport complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="evaluationMethodReport"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="evaluationMethod" type="{http://schemas.xmlsoap.org/soap/envelope/}evaluationMethod"/&gt;
 *         &lt;element name="numFolds" type="{http://www.w3.org/2001/XMLSchema}integer"/&gt;
 *         &lt;element name="numTests" type="{http://www.w3.org/2001/XMLSchema}integer"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "evaluationMethodReport", propOrder = {
    "evaluationMethod",
    "numFolds",
    "numTests"
})
public class EvaluationMethodReport {

    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected EvaluationMethod evaluationMethod;
    @XmlElement(required = true)
    protected BigInteger numFolds;
    @XmlElement(required = true)
    protected BigInteger numTests;

    /**
     * Gets the value of the evaluationMethod property.
     * 
     * @return
     *     possible object is
     *     {@link EvaluationMethod }
     *     
     */
    public EvaluationMethod getEvaluationMethod() {
        return evaluationMethod;
    }

    /**
     * Sets the value of the evaluationMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link EvaluationMethod }
     *     
     */
    public void setEvaluationMethod(EvaluationMethod value) {
        this.evaluationMethod = value;
    }

    /**
     * Gets the value of the numFolds property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumFolds() {
        return numFolds;
    }

    /**
     * Sets the value of the numFolds property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumFolds(BigInteger value) {
        this.numFolds = value;
    }

    /**
     * Gets the value of the numTests property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumTests() {
        return numTests;
    }

    /**
     * Sets the value of the numTests property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumTests(BigInteger value) {
        this.numTests = value;
    }

}
