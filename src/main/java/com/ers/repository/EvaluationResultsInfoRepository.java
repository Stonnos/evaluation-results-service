package com.ers.repository;

import com.ers.model.EvaluationResultsInfo;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

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
     * Finds evaluation results info by request id
     *
     * @param requestId - request id
     * @return evaluation results info
     */
    @EntityGraph(value = "evaluationResults", type = EntityGraph.EntityGraphType.FETCH)
    EvaluationResultsInfo findByRequestId(String requestId);
}
