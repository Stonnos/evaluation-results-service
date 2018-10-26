package com.ers.mapping;

import com.ers.TestHelperUtils;
import com.ers.dto.ClassifierReport;
import com.ers.dto.EnsembleClassifierReport;
import com.ers.model.ClassifierOptionsInfo;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Unit tests for checking {@link ClassifierOptionsInfoMapper} functionality.
 *
 * @author Roman Batygin
 */
@RunWith(SpringRunner.class)
@Import({ClassifierOptionsInfoMapperImpl.class, ClassifierReportFactory.class})
public class ClassifierOptionsInfoMapperTest {

    private static final Map<String, String> INPUT_OPTIONS_MAP = Collections.singletonMap("D", "22");

    @Inject
    private ClassifierOptionsInfoMapper classifierOptionsInfoMapper;

    @Test
    public void testClassifierOptionsInfoWithoutClassifiers() {
        ClassifierOptionsInfo classifierOptionsInfo =
                TestHelperUtils.buildClassifierOptionsInfo(INPUT_OPTIONS_MAP,
                        Collections.emptyList());
        ClassifierReport classifierReport = classifierOptionsInfoMapper.map(classifierOptionsInfo);
        Assertions.assertThat(classifierReport.getClassifierName()).isEqualTo(
                classifierOptionsInfo.getClassifierName());
        Assertions.assertThat(classifierReport.getClassifierDescription()).isEqualTo(
                classifierOptionsInfo.getClassifierDescription());
        Assertions.assertThat(classifierReport.getInputOptionsMap()).isNotNull();
        Assertions.assertThat(classifierReport.getInputOptionsMap().getEntry().size()).isOne();
    }

    @Test
    public void testClassifierOptionsInfoWithClassifiers() {
        List<ClassifierOptionsInfo> classifierOptionsInfoList =
                Arrays.asList(TestHelperUtils.buildClassifierOptionsInfo(INPUT_OPTIONS_MAP, Collections.emptyList()),
                        TestHelperUtils.buildClassifierOptionsInfo(INPUT_OPTIONS_MAP, Collections.emptyList()));
        ClassifierOptionsInfo classifierOptionsInfo =
                TestHelperUtils.buildClassifierOptionsInfo(INPUT_OPTIONS_MAP, classifierOptionsInfoList);
        ClassifierReport classifierReport = classifierOptionsInfoMapper.map(classifierOptionsInfo);
        Assertions.assertThat(classifierReport).isInstanceOf(EnsembleClassifierReport.class);
        Assertions.assertThat(classifierReport.getClassifierName()).isEqualTo(
                classifierOptionsInfo.getClassifierName());
        Assertions.assertThat(classifierReport.getClassifierDescription()).isEqualTo(
                classifierOptionsInfo.getClassifierDescription());
        Assertions.assertThat(classifierReport.getInputOptionsMap()).isNotNull();
        Assertions.assertThat(classifierReport.getInputOptionsMap().getEntry().size()).isOne();
        EnsembleClassifierReport ensembleClassifierReport = (EnsembleClassifierReport) classifierReport;
        Assertions.assertThat(ensembleClassifierReport.getIndividualClassifiers().size()).isEqualTo(
                classifierOptionsInfoList.size());
    }
}
