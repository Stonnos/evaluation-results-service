package com.ers.mapping;

import com.ers.dto.EvaluationMethod;
import com.ers.dto.EvaluationMethodReport;
import com.ers.dto.EvaluationResultsRequest;
import com.ers.model.EvaluationOption;
import com.ers.model.EvaluationResultsInfo;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

/**
 * Implements mapping evaluation results request to evaluation results database model.
 *
 * @author Roman Batygin
 */
@Mapper(uses = {ClassificationCostsReportMapper.class, ConfusionMatrixMapper.class, StatisticsReportMapper.class,
        EvaluationMethodMapper.class, InstancesMapper.class, ClassifierReportMapper.class})
public abstract class EvaluationResultsRequestMapper {

    /**
     * Maps evaluation results request to evaluation results entity.
     *
     * @param evaluationResultsRequest - evaluation results report
     * @return evaluation results entity
     */
    @Mappings( {
            @Mapping(source = "evaluationMethodReport.evaluationMethod", target = "evaluationMethod"),
            @Mapping(source = "classifierReport", target = "classifierOptionsInfo")
    })
    public abstract EvaluationResultsInfo map(EvaluationResultsRequest evaluationResultsRequest);

    @AfterMapping
    protected void mapEvaluationMethodReport(EvaluationResultsRequest evaluationResultsRequest,
                                             @MappingTarget EvaluationResultsInfo evaluationResultsInfo) {
        if (Optional.ofNullable(evaluationResultsRequest.getEvaluationMethodReport()).isPresent()) {
            EvaluationMethodReport evaluationMethodReport = evaluationResultsRequest.getEvaluationMethodReport();
            if (EvaluationMethod.CROSS_VALIDATION.equals(evaluationMethodReport.getEvaluationMethod())) {
                Map<EvaluationOption, String> evaluationOptionsMap = new EnumMap<>(EvaluationOption.class);
                putEvaluationOptionIfExists(EvaluationOption.NUM_FOLDS, evaluationMethodReport.getNumFolds(),
                        evaluationOptionsMap);
                putEvaluationOptionIfExists(EvaluationOption.NUM_TESTS, evaluationMethodReport.getNumTests(),
                        evaluationOptionsMap);
                putEvaluationOptionIfExists(EvaluationOption.SEED, evaluationMethodReport.getSeed(),
                        evaluationOptionsMap);
                evaluationResultsInfo.setEvaluationOptionsMap(evaluationOptionsMap);
            }
        }
    }

    private void putEvaluationOptionIfExists(EvaluationOption evaluationOption, Object option,
                                             Map<EvaluationOption, String> evaluationOptionsMap) {
        if (option != null) {
            evaluationOptionsMap.put(evaluationOption, String.valueOf(option));
        }
    }
}
