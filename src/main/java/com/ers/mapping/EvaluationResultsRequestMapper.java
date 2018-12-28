package com.ers.mapping;

import com.ers.dto.EvaluationResultsRequest;
import com.ers.model.EvaluationResultsInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * Implements mapping evaluation results request to evaluation results database model.
 *
 * @author Roman Batygin
 */
@Mapper(uses = {ClassificationCostsReportMapper.class, ConfusionMatrixMapper.class, StatisticsReportMapper.class,
        EvaluationMethodMapper.class, ClassifierReportMapper.class})
public interface EvaluationResultsRequestMapper {

    /**
     * Maps evaluation results request to evaluation results entity.
     *
     * @param evaluationResultsRequest - evaluation results report
     * @return evaluation results entity
     */
    @Mappings({
            @Mapping(source = "evaluationMethodReport.evaluationMethod", target = "evaluationMethod"),
            @Mapping(source = "evaluationMethodReport.numFolds", target = "numFolds"),
            @Mapping(source = "evaluationMethodReport.numTests", target = "numTests"),
            @Mapping(source = "evaluationMethodReport.seed", target = "seed"),
            @Mapping(source = "classifierReport", target = "classifierOptionsInfo"),
            @Mapping(target = "instances", ignore = true)
    })
    EvaluationResultsInfo map(EvaluationResultsRequest evaluationResultsRequest);

}
