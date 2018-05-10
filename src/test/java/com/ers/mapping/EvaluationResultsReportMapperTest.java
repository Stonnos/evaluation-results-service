package com.ers.mapping;

import com.ers.TestHelperUtils;
import com.ers.dto.EvaluationResultsReport;
import com.ers.model.EvaluationMethod;
import com.ers.model.EvaluationResultsInfo;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import java.util.UUID;

/**
 * Unit tests for checking {@link EvaluationResultsReportMapper} functionality.
 *
 * @author Roman Batygin
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class EvaluationResultsReportMapperTest {

    @Inject
    private EvaluationResultsReportMapper evaluationResultsReportMapper;

    @Test
    public void testMapEvaluationResultsReport() {
        EvaluationResultsReport resultsReport =
                TestHelperUtils.buildEvaluationResultsReport(UUID.randomUUID().toString());
        EvaluationResultsInfo evaluationResultsInfo = evaluationResultsReportMapper.map(resultsReport);
        Assertions.assertThat(evaluationResultsInfo.getRequestId()).isEqualTo(resultsReport.getRequestId());
        Assertions.assertThat(evaluationResultsInfo.getEvaluationMethod()).isEqualTo(EvaluationMethod.CROSS_VALIDATION);
        Assertions.assertThat(evaluationResultsInfo.getClassifierName()).isEqualTo(
                resultsReport.getClassifierReport().getClassifierName());
        Assertions.assertThat(evaluationResultsInfo.getStatistics()).isNotNull();
        Assertions.assertThat(evaluationResultsInfo.getInputOptionsMap()).isNotNull();
        Assertions.assertThat(evaluationResultsInfo.getInstances()).isNotNull();
        Assertions.assertThat(evaluationResultsInfo.getClassificationCosts()).isNotNull();
        Assertions.assertThat(evaluationResultsInfo.getClassificationCosts().size()).isEqualTo(
                resultsReport.getClassificationCosts().size());
        Assertions.assertThat(evaluationResultsInfo.getConfusionMatrix()).isNotNull();
        Assertions.assertThat(evaluationResultsInfo.getConfusionMatrix().size()).isEqualTo(
                resultsReport.getConfusionMatrix().size());
        Assertions.assertThat(evaluationResultsInfo.getInputOptionsMap()).isNotNull();
        Assertions.assertThat(evaluationResultsInfo.getInputOptionsMap().size()).isEqualTo(
                resultsReport.getClassifierReport().getInputOptionsMap().getEntry().size());
    }
}
