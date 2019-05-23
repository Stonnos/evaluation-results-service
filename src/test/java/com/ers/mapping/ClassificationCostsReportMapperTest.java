package com.ers.mapping;

import com.ers.TestHelperUtils;
import com.ers.dto.ClassificationCostsReport;
import com.ers.dto.RocCurveReport;
import com.ers.model.ClassificationCostsInfo;
import com.ers.model.RocCurveInfo;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

/**
 * Unit tests for checking {@link ClassificationCostsReportMapper} functionality.
 *
 * @author Roman Batygin
 */
@RunWith(SpringRunner.class)
@Import({ClassificationCostsReportMapperImpl.class, RocCurveReportMapperImpl.class})
public class ClassificationCostsReportMapperTest {

    @Inject
    private ClassificationCostsReportMapper costsReportMapper;

    @Test
    public void testMapClassificationCostsReport() {
        ClassificationCostsReport costsReport = TestHelperUtils.buildClassificationCostsReport();
        ClassificationCostsInfo classificationCostsInfo = costsReportMapper.map(costsReport);
        Assertions.assertThat(classificationCostsInfo.getClassValue()).isEqualTo(costsReport.getClassValue());
        RocCurveInfo rocCurveInfo = classificationCostsInfo.getRocCurveInfo();
        Assertions.assertThat(rocCurveInfo.getAucValue()).isEqualTo(costsReport.getRocCurve().getAucValue());
        Assertions.assertThat(rocCurveInfo.getSpecificity()).isEqualTo(costsReport.getRocCurve().getSpecificity());
        Assertions.assertThat(rocCurveInfo.getSensitivity()).isEqualTo(costsReport.getRocCurve().getSensitivity());
        Assertions.assertThat(rocCurveInfo.getThresholdValue()).isEqualTo(
                costsReport.getRocCurve().getThresholdValue());
        Assertions.assertThat(classificationCostsInfo.getFalseNegativeRate()).isEqualTo(
                costsReport.getFalseNegativeRate());
        Assertions.assertThat(classificationCostsInfo.getFalsePositiveRate()).isEqualTo(
                costsReport.getFalsePositiveRate());
        Assertions.assertThat(classificationCostsInfo.getTrueNegativeRate()).isEqualTo(
                costsReport.getTrueNegativeRate());
        Assertions.assertThat(classificationCostsInfo.getTruePositiveRate()).isEqualTo(
                costsReport.getTruePositiveRate());
    }

    @Test
    public void testMapClassificationCostsInfo() {
        ClassificationCostsInfo classificationCostsInfo = TestHelperUtils.buildClassificationCostsInfo();
        ClassificationCostsReport costsReport = costsReportMapper.map(classificationCostsInfo);
        Assertions.assertThat(costsReport.getClassValue()).isEqualTo(classificationCostsInfo.getClassValue());
        RocCurveReport rocCurveReport = costsReport.getRocCurve();
        Assertions.assertThat(rocCurveReport.getAucValue()).isEqualTo(
                classificationCostsInfo.getRocCurveInfo().getAucValue());
        Assertions.assertThat(rocCurveReport.getSpecificity()).isEqualTo(
                classificationCostsInfo.getRocCurveInfo().getSpecificity());
        Assertions.assertThat(rocCurveReport.getSensitivity()).isEqualTo(
                classificationCostsInfo.getRocCurveInfo().getSensitivity());
        Assertions.assertThat(rocCurveReport.getThresholdValue()).isEqualTo(
                classificationCostsInfo.getRocCurveInfo().getThresholdValue());
        Assertions.assertThat(costsReport.getFalseNegativeRate()).isEqualTo(
                classificationCostsInfo.getFalseNegativeRate());
        Assertions.assertThat(costsReport.getFalsePositiveRate()).isEqualTo(
                classificationCostsInfo.getFalsePositiveRate());
        Assertions.assertThat(costsReport.getTrueNegativeRate()).isEqualTo(
                classificationCostsInfo.getTrueNegativeRate());
        Assertions.assertThat(costsReport.getTruePositiveRate()).isEqualTo(
                classificationCostsInfo.getTruePositiveRate());
    }
}
