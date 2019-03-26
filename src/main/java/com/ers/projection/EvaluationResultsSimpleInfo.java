package com.ers.projection;

import com.ers.model.ClassifierOptionsInfo;
import com.ers.model.EvaluationMethod;
import com.ers.model.StatisticsInfo;

/**
 * Evaluation results simple info projection interface.
 *
 * @author Roman Batygin
 */
public interface EvaluationResultsSimpleInfo {

    /**
     * Gets request id.
     *
     * @return request id
     */
    String getRequestId();

    /**
     * Gets evaluation method.
     *
     * @return evaluation method
     */
    EvaluationMethod getEvaluationMethod();

    /**
     * Gets folds number.
     *
     * @return folds number
     */
    Integer getNumFolds();

    /**
     * Gets tests number.
     *
     * @return tests number
     */
    Integer getNumTests();

    /**
     * Gets seed value.
     *
     * @return seed value
     */
    Integer getSeed();

    /**
     * Gets statistics info.
     *
     * @return statistics info
     */
    StatisticsInfo getStatistics();

    /**
     * Gets classifier options info.
     *
     * @return classifier options info
     */
    ClassifierOptionsInfo getClassifierOptionsInfo();

}
