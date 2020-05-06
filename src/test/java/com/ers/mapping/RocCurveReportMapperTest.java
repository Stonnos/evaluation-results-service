package com.ers.mapping;

import com.ers.TestHelperUtils;
import com.ers.dto.RocCurveReport;
import com.ers.model.RocCurveInfo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.inject.Inject;

/**
 * Unit tests for checking {@link RocCurveReportMapper} functionality.
 *
 * @author Roman Batygin
 */
@ExtendWith(SpringExtension.class)
@Import(RocCurveReportMapperImpl.class)
public class RocCurveReportMapperTest {

    @Inject
    private RocCurveReportMapper rocCurveReportMapper;

    @Test
    public void testRocCurveReportMap() {
        RocCurveReport rocCurveReport = TestHelperUtils.buildRocCurveReport();
        RocCurveInfo rocCurveInfo = rocCurveReportMapper.map(rocCurveReport);
        Assertions.assertThat(rocCurveInfo.getAucValue()).isEqualTo(rocCurveReport.getAucValue());
        Assertions.assertThat(rocCurveInfo.getSpecificity()).isEqualTo(rocCurveReport.getSpecificity());
        Assertions.assertThat(rocCurveInfo.getSensitivity()).isEqualTo(rocCurveReport.getSensitivity());
        Assertions.assertThat(rocCurveInfo.getThresholdValue()).isEqualTo(rocCurveReport.getThresholdValue());
    }

    @Test
    public void testRocCurveInfoMap() {
        RocCurveInfo rocCurveInfo = TestHelperUtils.buildRocCurveInfo();
        RocCurveReport rocCurveReport = rocCurveReportMapper.map(rocCurveInfo);
        Assertions.assertThat(rocCurveReport.getAucValue()).isEqualTo(rocCurveInfo.getAucValue());
        Assertions.assertThat(rocCurveReport.getSpecificity()).isEqualTo(rocCurveInfo.getSpecificity());
        Assertions.assertThat(rocCurveReport.getSensitivity()).isEqualTo(rocCurveInfo.getSensitivity());
        Assertions.assertThat(rocCurveReport.getThresholdValue()).isEqualTo(rocCurveInfo.getThresholdValue());
    }
}
