package com.ers.mapping;

import com.ers.dto.ConfusionMatrixReport;
import com.ers.model.ConfusionMatrix;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;

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
    Set<ConfusionMatrix> map(List<ConfusionMatrixReport> confusionMatrixReports);
}
