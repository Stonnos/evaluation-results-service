package com.ers.mapping;

import com.ers.TestHelperUtils;
import com.ers.dto.StatisticsReport;
import com.ers.model.StatisticsInfo;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

/**
 * Unit tests for checking {@link StatisticsReportMapper} functionality.
 *
 * @author Roman Batygin
 */
@RunWith(SpringRunner.class)
@Import(StatisticsReportMapperImpl.class)
public class StatisticsReportMapperTest {

    @Inject
    private StatisticsReportMapper statisticsReportMapper;

    @Test
    public void testStatisticsReportMap() {
        StatisticsReport report = TestHelperUtils.buildStatisticsReport();
        StatisticsInfo statisticsInfo = statisticsReportMapper.map(report);
        Assertions.assertThat(statisticsInfo.getNumTestInstances()).isEqualTo(report.getNumTestInstances().intValue());
        Assertions.assertThat(statisticsInfo.getNumCorrect()).isEqualTo(report.getNumCorrect().intValue());
        Assertions.assertThat(statisticsInfo.getNumIncorrect()).isEqualTo(report.getNumIncorrect().intValue());
        Assertions.assertThat(statisticsInfo.getPctCorrect()).isEqualTo(report.getPctCorrect());
        Assertions.assertThat(statisticsInfo.getPctIncorrect()).isEqualTo(report.getPctIncorrect());
        Assertions.assertThat(statisticsInfo.getMeanAbsoluteError()).isEqualTo(report.getMeanAbsoluteError());
        Assertions.assertThat(statisticsInfo.getRootMeanSquaredError()).isEqualTo(report.getRootMeanSquaredError());
        Assertions.assertThat(statisticsInfo.getMaxAucValue()).isEqualTo(report.getMaxAucValue());
        Assertions.assertThat(statisticsInfo.getVarianceError()).isEqualTo(report.getVarianceError());
        Assertions.assertThat(statisticsInfo.getConfidenceIntervalLowerBound()).isEqualTo(
                report.getConfidenceIntervalLowerBound());
        Assertions.assertThat(statisticsInfo.getConfidenceIntervalUpperBound()).isEqualTo(
                report.getConfidenceIntervalUpperBound());
    }
}
