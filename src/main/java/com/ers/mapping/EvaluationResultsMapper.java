package com.ers.mapping;

import com.ers.dto.EvaluationResultsRequest;
import com.ers.dto.GetEvaluationResultsResponse;
import com.ers.model.EvaluationResultsInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * Implements evaluation results mapper.
 *
 * @author Roman Batygin
 */
@Mapper(uses = {ClassificationCostsReportMapper.class, ConfusionMatrixMapper.class,
        StatisticsReportMapper.class, EvaluationMethodMapper.class, ClassifierReportMapper.class,
        ClassifierOptionsInfoMapper.class, RocCurveDataMapper.class, InstancesMapper.class})
public interface EvaluationResultsMapper {

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

    /**
     * Maps evaluation results entity to response dto.
     *
     * @param evaluationResultsInfo - evaluation results info entity
     * @return evaluation results response
     */
    @Mappings({
            @Mapping(source = "evaluationMethod", target = "evaluationMethodReport.evaluationMethod"),
            @Mapping(source = "numFolds", target = "evaluationMethodReport.numFolds"),
            @Mapping(source = "numTests", target = "evaluationMethodReport.numTests"),
            @Mapping(source = "seed", target = "evaluationMethodReport.seed"),
            @Mapping(source = "classifierOptionsInfo", target = "classifierReport"),

    })
    GetEvaluationResultsResponse map(EvaluationResultsInfo evaluationResultsInfo);
}
