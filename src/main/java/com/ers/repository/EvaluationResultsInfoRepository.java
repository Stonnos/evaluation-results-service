package com.ers.repository;

import com.ers.model.EvaluationResultsInfo;
import com.ers.projection.EvaluationResultsSimpleInfo;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository to manage with {@link EvaluationResultsInfo} persistence entity.
 *
 * @author Roman Batygin
 */
public interface EvaluationResultsInfoRepository extends JpaRepository<EvaluationResultsInfo, Long> {

    /**
     * Checks existing of row with specified request id.
     *
     * @param requestId - request id
     * @return {@code true} if there is row with specified request id
     */
    boolean existsByRequestId(String requestId);

    /**
     * Gets evaluation results simple info by request id
     *
     * @param requestId - request id
     * @return evaluation results simple info
     */
    @Query("select e.requestId as requestId, e.evaluationMethod as evaluationMethod, e.numFolds as numFolds, " +
            "e.numTests as numTests, e.seed as seed, " +
            "e.statistics as statistics, e.classifierOptionsInfo as classifierOptionsInfo " +
            "from EvaluationResultsInfo e where e.requestId = :requestId")
    EvaluationResultsSimpleInfo findEvaluationResultsSimpleInfo(@Param("requestId") String requestId);

    /**
     * Finds evaluation results info by request id
     *
     * @param requestId - request id
     * @return evaluation results info
     */
    @EntityGraph(value = "evaluationResults", type = EntityGraph.EntityGraphType.FETCH)
    EvaluationResultsInfo findByRequestId(String requestId);
}
