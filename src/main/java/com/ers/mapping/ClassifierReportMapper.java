package com.ers.mapping;

import com.ers.dto.ClassifierReport;
import com.ers.dto.EnsembleClassifierReport;
import com.ers.dto.InputOptionsMap;
import com.ers.model.ClassifierOptionsInfo;
import com.ers.util.FieldSize;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Implements mapping classifier report to classifier options info entity.
 *
 * @author Roman Batygin
 */
@Mapper
public abstract class ClassifierReportMapper {

    /**
     * Maps classifier report to classifier options info entity.
     *
     * @param classifierReport - classifier report
     * @return classifier options info entity
     */
    @Mappings({
            @Mapping(target = "inputOptionsMap", ignore = true),
            @Mapping(source = "classifierDescription", target = "classifierDescription", qualifiedByName = "truncateClassifierDescription")
    })
    public abstract ClassifierOptionsInfo map(ClassifierReport classifierReport);

    @AfterMapping
    protected void mapInputOptions(ClassifierReport classifierReport,
                                   @MappingTarget ClassifierOptionsInfo classifierOptionsInfo) {
        if (Optional.ofNullable(classifierReport.getInputOptionsMap()).map(
                InputOptionsMap::getEntry).isPresent()) {
            Map<String, String> inputOptionsMap = new HashMap<>();
            classifierReport.getInputOptionsMap().getEntry().forEach(
                    entry -> inputOptionsMap.put(entry.getKey(), entry.getValue()));
            classifierOptionsInfo.setInputOptionsMap(inputOptionsMap);
        }
    }

    /**
     * Truncate classifier description value if its length is greater than 255.
     *
     * @param classifierDescription - string value
     * @return truncated string
     */
    @Named("truncateClassifierDescription")
    protected String truncateClassifierDescription(String classifierDescription) {
        return !StringUtils.isEmpty(classifierDescription) && classifierDescription.length() > FieldSize.CLASSIFIER_DESCRIPTION_LENGTH ? classifierDescription.substring(0, FieldSize.CLASSIFIER_DESCRIPTION_LENGTH) : classifierDescription;
    }

    /**
     * Maps individual classifiers reports for ensemble algorithms.
     *
     * @param classifierReport      - classifier report
     * @param classifierOptionsInfo classifier options info entity
     */
    @AfterMapping
    protected void mapClassifiers(ClassifierReport classifierReport,
                                  @MappingTarget ClassifierOptionsInfo classifierOptionsInfo) {
        if (classifierReport instanceof EnsembleClassifierReport) {
            EnsembleClassifierReport ensembleClassifierReport = (EnsembleClassifierReport) classifierReport;
            List<ClassifierReport> classifierReports = ensembleClassifierReport.getIndividualClassifiers();
            List<ClassifierOptionsInfo> individualClassifiers = new ArrayList<>(classifierReports.size());
            classifierReports.forEach(c -> individualClassifiers.add(map(c)));
            classifierOptionsInfo.setIndividualClassifiers(individualClassifiers);
        }
    }
}
