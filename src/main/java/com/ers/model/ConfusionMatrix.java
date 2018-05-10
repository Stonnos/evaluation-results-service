package com.ers.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Confusion matrix persistence entity.
 *
 * @author Roman Batygin
 */
@Data
@Entity
@Table(name = "confusion_matrix")
public class ConfusionMatrix {

    @Id
    @GeneratedValue
    private Long id;

    /**
     * Actual class value
     */
    @Column(name = "actual_class")
    private String actualClass;

    /**
     * Expected class value
     */
    @Column(name = "predicted_class")
    private String predictedClass;

    /**
     * Instances number
     */
    @Column(name = "numInstances")
    private Integer numInstances;
}
