package com.ers.service;

import com.ers.model.EvaluationResultsSortEntity;
import com.ers.repository.EvaluationResultsSortRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Sort field service.
 *
 * @author Roman Batygin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SortFieldService {

    private final EvaluationResultsSortRepository evaluationResultsSortRepository;

    /**
     * Gets evaluation results sort fields.
     *
     * @return evaluation results sort fields
     */
    public Sort getEvaluationResultsSort() {
        List<EvaluationResultsSortEntity> evaluationResultsSortEntityList =
                evaluationResultsSortRepository.findByOrderByFieldOrder();
        if (CollectionUtils.isEmpty(evaluationResultsSortEntityList)) {
            throw new IllegalStateException("Expected at least one evaluation results sort fields!");
        }
        Sort.Order[] orders = evaluationResultsSortEntityList.stream().map(evaluationResultsSortEntity ->
                evaluationResultsSortEntity.isAscending() ? Sort.Order.asc(evaluationResultsSortEntity.getFieldName()) :
                        Sort.Order.desc(evaluationResultsSortEntity.getFieldName())
        ).toArray(Sort.Order[]::new);
        return Sort.by(orders);
    }
}
