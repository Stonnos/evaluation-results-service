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

    @Test
    public void testMapStatisticsInfo() {
        StatisticsInfo statisticsInfo = TestHelperUtils.buildStatisticsInfo();
        StatisticsReport statisticsReport = statisticsReportMapper.map(statisticsInfo);
        Assertions.assertThat(statisticsReport.getNumTestInstances().intValue()).isEqualTo(
                statisticsInfo.getNumTestInstances());
        Assertions.assertThat(statisticsReport.getNumCorrect().intValue()).isEqualTo(statisticsInfo.getNumCorrect());
        Assertions.assertThat(statisticsReport.getNumIncorrect().intValue()).isEqualTo(
                statisticsInfo.getNumIncorrect());
        Assertions.assertThat(statisticsReport.getPctCorrect()).isEqualTo(statisticsInfo.getPctCorrect());
        Assertions.assertThat(statisticsReport.getPctIncorrect()).isEqualTo(statisticsInfo.getPctIncorrect());
        Assertions.assertThat(statisticsReport.getMeanAbsoluteError()).isEqualTo(statisticsInfo.getMeanAbsoluteError());
        Assertions.assertThat(statisticsReport.getRootMeanSquaredError()).isEqualTo(
                statisticsInfo.getRootMeanSquaredError());
        Assertions.assertThat(statisticsReport.getMaxAucValue()).isEqualTo(statisticsInfo.getMaxAucValue());
        Assertions.assertThat(statisticsReport.getVarianceError()).isEqualTo(statisticsInfo.getVarianceError());
        Assertions.assertThat(statisticsReport.getConfidenceIntervalLowerBound()).isEqualTo(
                statisticsInfo.getConfidenceIntervalLowerBound());
        Assertions.assertThat(statisticsReport.getConfidenceIntervalUpperBound()).isEqualTo(
                statisticsInfo.getConfidenceIntervalUpperBound());
    }
}
