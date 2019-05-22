package com.ers.model;

import com.ers.util.FieldSize;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * Roc - curve point persistence entity.
 *
 * @author Roman Batygin
 */
@Getter
@Setter
@Entity
@Table(name = "roc_curve_point_entity")
public class RocCurvePointEntity {

    @Id
    @GeneratedValue
    private Long id;

    /**
     * X value
     */
    @Column(name = "x_value", precision = FieldSize.PRECISION, scale = FieldSize.SCALE)
    private BigDecimal xValue;

    /**
     * Y value
     */
    @Column(name = "y_value", precision = FieldSize.PRECISION, scale = FieldSize.SCALE)
    private BigDecimal yValue;

    /**
     * Threshold value in point
     */
    @Column(name = "threshold_value", precision = FieldSize.PRECISION, scale = FieldSize.SCALE)
    private BigDecimal thresholdValue;
}
