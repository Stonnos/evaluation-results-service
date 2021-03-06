package com.ers.mapping;

import com.ers.dto.ClassifierReport;
import com.ers.dto.EnsembleClassifierReport;
import com.ers.dto.InputOptionsMap;
import com.ers.model.ClassifierOptionsInfo;
import org.mapstruct.AfterMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Implements mapping classifier options info to dto model.
 *
 * @author Roman Batygin
 */
@Mapper(uses = ClassifierReportFactory.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class ClassifierOptionsInfoMapper {

    /**
     * Maps classifier options info to dto model.
     *
     * @param classifierOptionsInfo - classifier options info
     * @return classifier options report
     */
    @Mapping(target = "inputOptionsMap", ignore = true)
    public abstract ClassifierReport map(ClassifierOptionsInfo classifierOptionsInfo);

    /**
     * Maps classifier options info list to dto models list.
     *
     * @param classifierOptionsInfoList - classifier options info list
     * @return classifier options dto list
     */
    public abstract List<ClassifierReport> map(List<ClassifierOptionsInfo> classifierOptionsInfoList);

    @AfterMapping
    protected void mapInputOptions(ClassifierOptionsInfo classifierOptionsInfo,
                                   @MappingTarget ClassifierReport classifierReport) {
        if (!CollectionUtils.isEmpty(classifierOptionsInfo.getInputOptionsMap())) {
            classifierReport.setInputOptionsMap(new InputOptionsMap());
            classifierOptionsInfo.getInputOptionsMap().forEach((key, value) -> {
                InputOptionsMap.Entry entry = new InputOptionsMap.Entry();
                entry.setKey(key);
                entry.setValue(value);
                classifierReport.getInputOptionsMap().getEntry().add(entry);
            });
        }
    }

    @AfterMapping
    protected void mapClassifiers(ClassifierOptionsInfo classifierOptionsInfo,
                                  @MappingTarget ClassifierReport classifierReport) {
        if (classifierReport instanceof EnsembleClassifierReport) {
            EnsembleClassifierReport ensembleClassifierReport = (EnsembleClassifierReport) classifierReport;
            classifierOptionsInfo.getIndividualClassifiers().forEach(
                    classifierOptions -> ensembleClassifierReport.getIndividualClassifiers().add(
                            map(classifierOptions)));
        }
    }
}
