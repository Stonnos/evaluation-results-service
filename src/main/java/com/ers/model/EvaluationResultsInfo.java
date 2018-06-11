package com.ers.model;

import com.ers.util.FieldSize;
import lombok.Data;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Evaluation results info persistence entity.
 *
 * @author Roman Batygin
 */
@Data
@Entity
@Table(name = "evaluation_results_info", indexes = @Index(columnList = "request_id", name = "request_id_index"))
public class EvaluationResultsInfo {

    @Id
    @GeneratedValue
    private Long id;

    /**
     * Request unique id
     */
    @Column(name = "request_id", unique = true)
    private String requestId;

    /**
     * Creation date.
     */
    @Column(name = "save_date")
    private LocalDateTime saveDate;

    /**
     * Training data info
     */
    @ManyToOne
    @JoinColumn(name = "instances_info_id")
    private InstancesInfo instances;

    /**
     * Classifier options info
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "classifier_options_info_id")
    private ClassifierOptionsInfo classifierOptionsInfo;

    /**
     * Evaluation method
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "evaluation_method")
    private EvaluationMethod evaluationMethod;

    /**
     * Folds number for k * V cross - validation method
     */
    @Column(name = "num_folds")
    private Integer numFolds;

    /**
     * Tests number for k * V cross - validation method
     */
    @Column(name = "num_tests")
    private Integer numTests;

    /**
     * Seed value using by k * V cross - validation method
     */
    private Integer seed;

    /**
     * Evaluation statistics info
     */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "numTestInstances", column = @Column(name = "num_test_instances")),
            @AttributeOverride(name = "numCorrect", column = @Column(name = "num_correct")),
            @AttributeOverride(name = "numIncorrect", column = @Column(name = "num_incorrect")),
            @AttributeOverride(name = "pctCorrect",
                    column = @Column(name = "pct_correct", precision = FieldSize.PRECISION, scale = FieldSize.SCALE)),
            @AttributeOverride(name = "pctIncorrect",
                    column = @Column(name = "pct_incorrect", precision = FieldSize.PRECISION, scale = FieldSize.SCALE)),
            @AttributeOverride(name = "meanAbsoluteError",
                    column = @Column(name = "mean_abs_error", precision = FieldSize.PRECISION,
                            scale = FieldSize.SCALE)),
            @AttributeOverride(name = "rootMeanSquaredError",
                    column = @Column(name = "root_mean_squared_error", precision = FieldSize.PRECISION,
                            scale = FieldSize.SCALE)),
            @AttributeOverride(name = "maxAuc",
                    column = @Column(name = "max_auc", precision = FieldSize.PRECISION,
                            scale = FieldSize.SCALE)),
            @AttributeOverride(name = "varianceError",
                    column = @Column(name = "variance_error", precision = FieldSize.PRECISION,
                            scale = FieldSize.SCALE)),
            @AttributeOverride(name = "confidenceIntervalLowerBound",
                    column = @Column(name = "confidence_interval_lower_bound", precision = FieldSize.PRECISION,
                            scale = FieldSize.SCALE)),
            @AttributeOverride(name = "confidenceIntervalUpperBound",
                    column = @Column(name = "confidence_interval_upper_bound", precision = FieldSize.PRECISION,
                            scale = FieldSize.SCALE))
    })
    private StatisticsInfo statistics;

    /**
     * Classification costs reports list
     */
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "evaluation_results_info_id", nullable = false)
    private List<ClassificationCostsInfo> classificationCosts;

    /**
     * Confusion matrix
     */
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "evaluation_results_info_id", nullable = false)
    private List<ConfusionMatrix> confusionMatrix;
}
