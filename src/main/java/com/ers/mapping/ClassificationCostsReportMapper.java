package com.ers.mapping;

import com.ers.dto.ClassificationCostsReport;
import com.ers.model.ClassificationCostsInfo;
import com.ers.model.EvaluationResultsInfo;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * Classification costs report mapper.
 *
 * @author Roman Batygin
 */
@Mapper
public interface ClassificationCostsReportMapper {

    /**
     * Maps classification costs report to classification costs info entity.
     *
     * @param classificationCostsReport -  classification costs report
     * @return classification costs info entity
     */
    ClassificationCostsInfo map(ClassificationCostsReport classificationCostsReport);

    /**
     * Maps classification costs reports to classification costs info list.
     *
     * @param classificationCostsReports -  classification costs reports list
     * @return classification costs info list
     */
    List<ClassificationCostsInfo> mapList(List<ClassificationCostsReport> classificationCostsReports);

}
