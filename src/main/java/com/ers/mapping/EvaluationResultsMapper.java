package com.ers.mapping;

import com.ers.dto.EvaluationResultsRequest;
import com.ers.dto.GetEvaluationResultsSimpleResponse;
import com.ers.model.EvaluationResultsInfo;
import com.ers.projection.EvaluationResultsSimpleInfo;
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
        ClassifierOptionsInfoMapper.class, RocCurveDataMapper.class})
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
     * Maps evaluation results simple info to evaluation results response model.
     *
     * @param evaluationResultsSimpleInfo - evaluation results simple info
     * @return evaluation results simple response
     */
    @Mappings({
            @Mapping(source = "evaluationMethod", target = "evaluationMethodReport.evaluationMethod"),
            @Mapping(source = "numFolds", target = "evaluationMethodReport.numFolds"),
            @Mapping(source = "numTests", target = "evaluationMethodReport.numTests"),
            @Mapping(source = "seed", target = "evaluationMethodReport.seed"),
            @Mapping(source = "classifierOptionsInfo", target = "classifierReport")
    })
    GetEvaluationResultsSimpleResponse map(EvaluationResultsSimpleInfo evaluationResultsSimpleInfo);
}
