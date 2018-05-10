package com.ers.mapping;

import com.ers.dto.ConfusionMatrixReport;
import com.ers.model.ConfusionMatrix;
import com.ers.model.EvaluationResultsInfo;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.ArrayList;
import java.util.List;

/**
 * Confusion matrix mapper.
 *
 * @author Roman Batygin
 */
@Mapper
public interface ConfusionMatrixMapper {

    /**
     * Maps confusion matrix report to confusion matrix entity.
     *
     * @param confusionMatrixReport - confusion matrix report
     * @return confusion matrix entity
     */
    ConfusionMatrix map(ConfusionMatrixReport confusionMatrixReport);

    /**
     * Maps confusion matrix reports to confusion matrices entities.
     *
     * @param confusionMatrixReports - confusion matrix reports
     * @return confusion matrices entities
     */
    List<ConfusionMatrix> mapList(List<ConfusionMatrixReport> confusionMatrixReports);
}
