package com.ers.repository;

import com.ers.model.ClassifierOptionsInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository to manage with {@link ClassifierOptionsInfo} persistence entity.
 *
 * @author Roman Batygin
 */
public interface ClassifierOptionsInfoRepository extends JpaRepository<ClassifierOptionsInfo, Long> {

    /**
     * Finds the best classifiers options for specified training data with K * V folds cross - validation method
     * using maximum accuracy criteria.
     *
     * @param instancesId - training data id
     * @param numFolds    - folds number
     * @param numTests    - tests number
     * @param seed        - seed value for random generator
     * @param pageable    - pageable object
     * @return the best classifiers options list
     */
    @Query("select co from EvaluationResultsInfo evr join evr.classifierOptionsInfo co " +
            "where evr.instances.id = :instancesId and evr.evaluationMethod = 'CROSS_VALIDATION' " +
            "and evr.numFolds = :numFolds and evr.numTests = :numTests and evr.seed = :seed " +
            "order by evr.statistics.pctCorrect desc, evr.statistics.maxAucValue desc, " +
            "evr.statistics.varianceError asc")
    List<ClassifierOptionsInfo> findTopClassifierOptionsByCrossValidation(
            @Param("instancesId") Long instancesId, @Param("numFolds") Integer numFolds,
            @Param("numTests") Integer numTests, @Param("seed") Integer seed, Pageable pageable);

    /**
     * Finds the best classifiers options for specified training data with TRAINING_DATA evaluation method.
     * using maximum accuracy criteria.
     *
     * @param instancesId - training data id
     * @param pageable    - pageable object
     * @return the best classifiers options list
     */
    @Query("select co from EvaluationResultsInfo evr join evr.classifierOptionsInfo co " +
            "where evr.instances.id = :instancesId and evr.evaluationMethod = 'TRAINING_DATA' " +
            "order by evr.statistics.pctCorrect desc, evr.statistics.maxAucValue desc")
    List<ClassifierOptionsInfo> findTopClassifierOptions(@Param("instancesId") Long instancesId,
                                                         Pageable pageable);
}
