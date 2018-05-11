//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.05.11 at 10:08:16 AM NOVT 
//


package com.ers.dto;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="requestId" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="instances" type="{http://schemas.xmlsoap.org/soap/envelope/}instancesReport"/&gt;
 *         &lt;element name="classifierReport" type="{http://schemas.xmlsoap.org/soap/envelope/}classifierReport"/&gt;
 *         &lt;element name="evaluationMethodReport" type="{http://schemas.xmlsoap.org/soap/envelope/}evaluationMethodReport"/&gt;
 *         &lt;element name="statistics" type="{http://schemas.xmlsoap.org/soap/envelope/}statisticsReport"/&gt;
 *         &lt;element name="classificationCosts" type="{http://schemas.xmlsoap.org/soap/envelope/}classificationCostsReport" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="confusionMatrix" type="{http://schemas.xmlsoap.org/soap/envelope/}confusionMatrixReport" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "requestId",
    "instances",
    "classifierReport",
    "evaluationMethodReport",
    "statistics",
    "classificationCosts",
    "confusionMatrix"
})
@XmlRootElement(name = "evaluationResultsReport")
public class EvaluationResultsReport {

    @XmlElement(required = true)
    protected String requestId;
    @XmlElement(required = true)
    protected InstancesReport instances;
    @XmlElement(required = true)
    protected ClassifierReport classifierReport;
    @XmlElement(required = true)
    protected EvaluationMethodReport evaluationMethodReport;
    @XmlElement(required = true)
    protected StatisticsReport statistics;
    protected List<ClassificationCostsReport> classificationCosts;
    protected List<ConfusionMatrixReport> confusionMatrix;

    /**
     * Gets the value of the requestId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * Sets the value of the requestId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestId(String value) {
        this.requestId = value;
    }

    /**
     * Gets the value of the instances property.
     * 
     * @return
     *     possible object is
     *     {@link InstancesReport }
     *     
     */
    public InstancesReport getInstances() {
        return instances;
    }

    /**
     * Sets the value of the instances property.
     * 
     * @param value
     *     allowed object is
     *     {@link InstancesReport }
     *     
     */
    public void setInstances(InstancesReport value) {
        this.instances = value;
    }

    /**
     * Gets the value of the classifierReport property.
     * 
     * @return
     *     possible object is
     *     {@link ClassifierReport }
     *     
     */
    public ClassifierReport getClassifierReport() {
        return classifierReport;
    }

    /**
     * Sets the value of the classifierReport property.
     * 
     * @param value
     *     allowed object is
     *     {@link ClassifierReport }
     *     
     */
    public void setClassifierReport(ClassifierReport value) {
        this.classifierReport = value;
    }

    /**
     * Gets the value of the evaluationMethodReport property.
     * 
     * @return
     *     possible object is
     *     {@link EvaluationMethodReport }
     *     
     */
    public EvaluationMethodReport getEvaluationMethodReport() {
        return evaluationMethodReport;
    }

    /**
     * Sets the value of the evaluationMethodReport property.
     * 
     * @param value
     *     allowed object is
     *     {@link EvaluationMethodReport }
     *     
     */
    public void setEvaluationMethodReport(EvaluationMethodReport value) {
        this.evaluationMethodReport = value;
    }

    /**
     * Gets the value of the statistics property.
     * 
     * @return
     *     possible object is
     *     {@link StatisticsReport }
     *     
     */
    public StatisticsReport getStatistics() {
        return statistics;
    }

    /**
     * Sets the value of the statistics property.
     * 
     * @param value
     *     allowed object is
     *     {@link StatisticsReport }
     *     
     */
    public void setStatistics(StatisticsReport value) {
        this.statistics = value;
    }

    /**
     * Gets the value of the classificationCosts property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the classificationCosts property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getClassificationCosts().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ClassificationCostsReport }
     * 
     * 
     */
    public List<ClassificationCostsReport> getClassificationCosts() {
        if (classificationCosts == null) {
            classificationCosts = new ArrayList<ClassificationCostsReport>();
        }
        return this.classificationCosts;
    }

    /**
     * Gets the value of the confusionMatrix property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the confusionMatrix property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getConfusionMatrix().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ConfusionMatrixReport }
     * 
     * 
     */
    public List<ConfusionMatrixReport> getConfusionMatrix() {
        if (confusionMatrix == null) {
            confusionMatrix = new ArrayList<ConfusionMatrixReport>();
        }
        return this.confusionMatrix;
    }

}
