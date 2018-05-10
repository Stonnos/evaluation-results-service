package com.ers.mapping;

import com.ers.TestHelperUtils;
import com.ers.dto.ClassificationCostsReport;
import com.ers.model.ClassificationCostsInfo;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

/**
 * Unit tests for checking {@link ClassificationCostsReportMapper} functionality.
 *
 * @author Roman Batygin
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ClassificationCostsReportMapperTest {

    @Inject
    private ClassificationCostsReportMapper costsReportMapper;

    @Test
    public void testMapClassificationCostsReport() {
        ClassificationCostsReport costsReport = TestHelperUtils.buildClassificationCostsReport();
        ClassificationCostsInfo classificationCostsInfo = costsReportMapper.map(costsReport);
        Assertions.assertThat(classificationCostsInfo.getClassValue()).isEqualTo(costsReport.getClassValue());
        Assertions.assertThat(classificationCostsInfo.getAucValue()).isEqualTo(costsReport.getAucValue());
        Assertions.assertThat(classificationCostsInfo.getFalseNegativeRate()).isEqualTo(
                costsReport.getFalseNegativeRate());
        Assertions.assertThat(classificationCostsInfo.getFalsePositiveRate()).isEqualTo(
                costsReport.getFalsePositiveRate());
        Assertions.assertThat(classificationCostsInfo.getTrueNegativeRate()).isEqualTo(
                costsReport.getTrueNegativeRate());
        Assertions.assertThat(classificationCostsInfo.getTruePositiveRate()).isEqualTo(
                costsReport.getTruePositiveRate());
    }
}
