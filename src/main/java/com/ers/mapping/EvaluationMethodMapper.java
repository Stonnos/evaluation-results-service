package com.ers.mapping;

import com.ers.model.EvaluationMethod;
import org.mapstruct.Mapper;

/**
 * Evaluation method mapper.
 *
 * @author Roman Batygin
 */
@Mapper
public interface EvaluationMethodMapper {

    /**
     * Maps evaluation method dto to evaluation method.
     *
     * @param evaluationMethod - evaluation method dto
     * @return evaluation method
     */
    EvaluationMethod map(com.ers.dto.EvaluationMethod evaluationMethod);

    /**
     * Maps evaluation method to dto model.
     *
     * @param evaluationMethod - evaluation method
     * @return evaluation method dto enum
     */
    com.ers.dto.EvaluationMethod map(EvaluationMethod evaluationMethod);
}
