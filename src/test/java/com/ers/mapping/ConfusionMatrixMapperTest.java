package com.ers.mapping;

import com.ers.TestHelperUtils;
import com.ers.dto.ConfusionMatrixReport;
import com.ers.model.ConfusionMatrix;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

/**
 * Unit tests for checking {@link ConfusionMatrixMapper} functionality.
 *
 * @author Roman Batygin
 */
@RunWith(SpringRunner.class)
@Import(ConfusionMatrixMapperImpl.class)
public class ConfusionMatrixMapperTest {

    @Inject
    private ConfusionMatrixMapper confusionMatrixMapper;

    @Test
    public void testMapConfusionMatrixReport() {
        ConfusionMatrixReport report = TestHelperUtils.buildConfusionMatrixReport();
        ConfusionMatrix confusionMatrix = confusionMatrixMapper.map(report);
        Assertions.assertThat(confusionMatrix.getActualClass()).isEqualTo(report.getActualClass());
        Assertions.assertThat(confusionMatrix.getPredictedClass()).isEqualTo(report.getPredictedClass());
        Assertions.assertThat(confusionMatrix.getNumInstances().intValue()).isEqualTo(
                report.getNumInstances().intValue());
    }

    @Test
    public void testMapConfusionMatrix() {
        ConfusionMatrix confusionMatrix = TestHelperUtils.buildConfusionMatrix();
        ConfusionMatrixReport confusionMatrixReport = confusionMatrixMapper.map(confusionMatrix);
        Assertions.assertThat(confusionMatrixReport.getActualClass()).isEqualTo(confusionMatrix.getActualClass());
        Assertions.assertThat(confusionMatrixReport.getPredictedClass()).isEqualTo(confusionMatrix.getPredictedClass());
        Assertions.assertThat(confusionMatrixReport.getNumInstances().intValue()).isEqualTo(
                confusionMatrix.getNumInstances().intValue());
    }
}
