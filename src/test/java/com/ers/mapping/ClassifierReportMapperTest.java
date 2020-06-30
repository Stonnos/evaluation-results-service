package com.ers.mapping;

import com.ers.TestHelperUtils;
import com.ers.dto.ClassifierReport;
import com.ers.dto.EnsembleClassifierReport;
import com.ers.model.ClassifierOptionsInfo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.inject.Inject;

/**
 * Unit tests for checking {@link ClassifierReportMapper} functionality.
 *
 * @author Roman Batygin
 */
@ExtendWith(SpringExtension.class)
@Import(ClassifierReportMapperImpl.class)
class ClassifierReportMapperTest {

    @Inject
    private ClassifierReportMapper classifierReportMapper;

    @Test
    void testMapClassifierReport() {
        ClassifierReport classifierReport = TestHelperUtils.buildClassifierReport();
        ClassifierOptionsInfo classifierOptionsInfo = classifierReportMapper.map(classifierReport);
        Assertions.assertThat(classifierOptionsInfo.getClassifierName()).isEqualTo(
                classifierReport.getClassifierName());
        Assertions.assertThat(classifierOptionsInfo.getOptions()).isEqualTo(
                classifierReport.getOptions());
        Assertions.assertThat(classifierOptionsInfo.getClassifierDescription()).isEqualTo(
                classifierReport.getClassifierDescription());
        Assertions.assertThat(classifierOptionsInfo.getInputOptionsMap()).isNotNull();
        Assertions.assertThat(classifierOptionsInfo.getInputOptionsMap()).isNotNull();
        Assertions.assertThat(classifierOptionsInfo.getInputOptionsMap()).hasSameSizeAs(
                classifierReport.getInputOptionsMap().getEntry());
        Assertions.assertThat(classifierOptionsInfo.getIndividualClassifiers()).isNullOrEmpty();
    }

    @Test
    void testMapEnsembleClassifierReport() {
        EnsembleClassifierReport classifierReport = TestHelperUtils.buildEnsembleClassifierReport();
        ClassifierOptionsInfo classifierOptionsInfo = classifierReportMapper.map(classifierReport);
        Assertions.assertThat(classifierOptionsInfo.getClassifierName()).isEqualTo(
                classifierReport.getClassifierName());
        Assertions.assertThat(classifierOptionsInfo.getInputOptionsMap()).isNotNull();
        Assertions.assertThat(classifierOptionsInfo.getInputOptionsMap()).isNotNull();
        Assertions.assertThat(classifierOptionsInfo.getInputOptionsMap()).hasSameSizeAs(
                classifierReport.getInputOptionsMap().getEntry());
        Assertions.assertThat(classifierOptionsInfo.getIndividualClassifiers()).hasSameSizeAs(
                (classifierOptionsInfo.getIndividualClassifiers()));
    }
}
