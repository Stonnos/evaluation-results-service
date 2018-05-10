package com.ers.mapping;

import com.ers.dto.ClassifierReport;
import com.ers.dto.EvaluationMethod;
import com.ers.dto.EvaluationMethodReport;
import com.ers.dto.EvaluationResultsReport;
import com.ers.model.EvaluationOption;
import com.ers.model.EvaluationResultsInfo;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Implements mapping evaluation results report to evaluation results database model.
 *
 * @author Roman Batygin
 */
@Mapper(uses = {ClassificationCostsReportMapper.class, ConfusionMatrixMapper.class, StatisticsReportMapper.class,
        EvaluationMethodMapper.class, InstancesMapper.class})
public abstract class EvaluationResultsReportMapper {

    /**
     * Maps evaluation results report to evaluation results entity.
     *
     * @param evaluationResultsReport - evaluation results report
     * @return evaluation results entity
     */
    @Mappings(
            @Mapping(source = "evaluationMethodReport.evaluationMethod", target = "evaluationMethod")
    )
    public abstract EvaluationResultsInfo map(EvaluationResultsReport evaluationResultsReport);

    @AfterMapping
    protected void mapClassifierReport(EvaluationResultsReport evaluationResultsReport,
                                       @MappingTarget EvaluationResultsInfo evaluationResultsInfo) {
        if (Optional.ofNullable(evaluationResultsReport.getClassifierReport()).isPresent()) {
            ClassifierReport classifierReport = evaluationResultsReport.getClassifierReport();
            evaluationResultsInfo.setClassifierName(classifierReport.getClassifierName());
            if (Optional.ofNullable(classifierReport.getInputOptionsMap()).map(
                    ClassifierReport.InputOptionsMap::getEntry).isPresent()) {
                Map<String, String> inputOptionsMap = new HashMap<>();
                classifierReport.getInputOptionsMap().getEntry().forEach(
                        entry -> inputOptionsMap.put(entry.getKey(), entry.getValue()));
                evaluationResultsInfo.setInputOptionsMap(inputOptionsMap);
            }
        }
    }

    @AfterMapping
    protected void mapEvaluationMethodReport(EvaluationResultsReport evaluationResultsReport,
                                             @MappingTarget EvaluationResultsInfo evaluationResultsInfo) {
        if (Optional.ofNullable(evaluationResultsReport.getEvaluationMethodReport()).isPresent()) {
            EvaluationMethodReport evaluationMethodReport = evaluationResultsReport.getEvaluationMethodReport();
            if (EvaluationMethod.CROSS_VALIDATION.equals(evaluationMethodReport.getEvaluationMethod())) {
                Map<EvaluationOption, String> evaluationOptionsMap = new EnumMap<>(EvaluationOption.class);
                if (evaluationMethodReport.getNumFolds() != null) {
                    evaluationOptionsMap.put(EvaluationOption.NUM_FOLDS,
                            String.valueOf(evaluationMethodReport.getNumFolds()));
                }
                if (evaluationMethodReport.getNumTests() != null) {
                    evaluationOptionsMap.put(EvaluationOption.NUM_TESTS,
                            String.valueOf(evaluationMethodReport.getNumTests()));
                }
                evaluationResultsInfo.setEvaluationOptionsMap(evaluationOptionsMap);
            }
        }
    }
}
