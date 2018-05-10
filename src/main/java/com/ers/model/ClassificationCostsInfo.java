package com.ers.model;

import com.ers.util.FieldSize;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * Classification results report persistence entity.
 *
 * @author Roman Batygin
 */
@Data
@Entity
@Table(name = "classification_costs_info")
public class ClassificationCostsInfo {

    @Id
    @GeneratedValue
    private Long id;

    /**
     * Class value
     */
    @Column(name = "class_value")
    private String classValue;

    /**
     * TP rate
     */
    @Column(name = "tp_rate", precision = FieldSize.PRECISION, scale = FieldSize.SCALE)
    private BigDecimal truePositiveRate;

    /**
     * FP rate
     */
    @Column(name = "fp_rate", precision = FieldSize.PRECISION, scale = FieldSize.SCALE)
    private BigDecimal falsePositiveRate;

    /**
     * TN rate
     */
    @Column(name = "tn_rate", precision = FieldSize.PRECISION, scale = FieldSize.SCALE)
    private BigDecimal trueNegativeRate;

    /**
     * FN rate
     */
    @Column(name = "fn_rate", precision = FieldSize.PRECISION, scale = FieldSize.SCALE)
    private BigDecimal falseNegativeRate;

    /**
     * AUC value
     */
    @Column(name = "auc_value", precision = FieldSize.PRECISION, scale = FieldSize.SCALE)
    private BigDecimal aucValue;
}
