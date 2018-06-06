package com.ers.mapping;

import com.ers.TestHelperUtils;
import com.ers.dto.ClassifierReport;
import com.ers.dto.EnsembleClassifierReport;
import com.ers.model.ClassifierOptionsInfo;
import com.ers.util.FieldSize;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

/**
 * Unit tests for checking {@link ClassifierReportMapper} functionality.
 *
 * @author Roman Batygin
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ClassifierReportMapperTest {

    @Inject
    private ClassifierReportMapper classifierReportMapper;

    @Test
    public void testMapClassifierReport() {
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
        Assertions.assertThat(classifierOptionsInfo.getInputOptionsMap().size()).isEqualTo(
                classifierReport.getInputOptionsMap().getEntry().size());
        Assertions.assertThat(classifierOptionsInfo.getIndividualClassifiers()).isNullOrEmpty();
    }

    @Test
    public void testMapEnsembleClassifierReport() {
        EnsembleClassifierReport classifierReport = TestHelperUtils.buildEnsembleClassifierReport();
        ClassifierOptionsInfo classifierOptionsInfo = classifierReportMapper.map(classifierReport);
        Assertions.assertThat(classifierOptionsInfo.getClassifierName()).isEqualTo(
                classifierReport.getClassifierName());
        Assertions.assertThat(classifierOptionsInfo.getInputOptionsMap()).isNotNull();
        Assertions.assertThat(classifierOptionsInfo.getInputOptionsMap()).isNotNull();
        Assertions.assertThat(classifierOptionsInfo.getInputOptionsMap().size()).isEqualTo(
                classifierReport.getInputOptionsMap().getEntry().size());
        Assertions.assertThat(classifierOptionsInfo.getIndividualClassifiers().size()).isEqualTo
                (classifierOptionsInfo.getIndividualClassifiers().size());
    }

    @Test
    public void testMapClassifierDescription() {
        ClassifierReport classifierReport = TestHelperUtils.buildClassifierReport();
        classifierReport.setClassifierDescription(StringUtils.repeat('Q', FieldSize.CLASSIFIER_DESCRIPTION_LENGTH + 1));
        ClassifierOptionsInfo classifierOptionsInfo = classifierReportMapper.map(classifierReport);
        Assertions.assertThat(classifierOptionsInfo.getClassifierDescription()).isNotNull();
        Assertions.assertThat(classifierOptionsInfo.getClassifierDescription().length()).isEqualTo(FieldSize.CLASSIFIER_DESCRIPTION_LENGTH);
    }
}
