package com.ers.model;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

/**
 * Roc curve data persistence entity.
 *
 * @author Roman Batygin
 */
@Data
@Entity
@Table(name = "roc_curve_data_entity")
public class RocCurveDataEntity {

    @Id
    @GeneratedValue
    private Long id;

    /**
     * Class value
     */
    @Column(name = "class_value")
    private String classValue;

    /**
     * Roc curve points list
     */
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "roc_curve_data_id", nullable = false)
    private List<RocCurvePointEntity> points;
}
