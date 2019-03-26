package com.ers.mapping;

import com.ers.TestHelperUtils;
import com.ers.dto.EvaluationResultsRequest;
import com.ers.model.EvaluationMethod;
import com.ers.model.EvaluationResultsInfo;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import java.util.UUID;

/**
 * Unit tests for checking {@link EvaluationResultsMapper} functionality.
 *
 * @author Roman Batygin
 */
@RunWith(SpringRunner.class)
@Import({EvaluationResultsMapperImpl.class, ClassificationCostsReportMapperImpl.class,
        ConfusionMatrixMapperImpl.class, StatisticsReportMapperImpl.class,
        EvaluationMethodMapperImpl.class, InstancesMapperImpl.class, ClassifierReportMapperImpl.class,
        RocCurveReportMapperImpl.class, ClassifierOptionsInfoMapperImpl.class, ClassifierReportFactory.class})
public class EvaluationResultsMapperTest {

    @Inject
    private EvaluationResultsMapper evaluationResultsMapper;

    @Test
    public void testMapEvaluationResultsReport() {
        EvaluationResultsRequest resultsRequest =
                TestHelperUtils.buildEvaluationResultsReport(UUID.randomUUID().toString());
        EvaluationResultsInfo evaluationResultsInfo = evaluationResultsMapper.map(resultsRequest);
        Assertions.assertThat(evaluationResultsInfo.getRequestId()).isEqualTo(resultsRequest.getRequestId());
        Assertions.assertThat(evaluationResultsInfo.getEvaluationMethod()).isEqualTo(EvaluationMethod.CROSS_VALIDATION);
        Assertions.assertThat(evaluationResultsInfo.getStatistics()).isNotNull();
        Assertions.assertThat(evaluationResultsInfo.getClassificationCosts()).isNotNull();
        Assertions.assertThat(evaluationResultsInfo.getClassificationCosts().size()).isEqualTo(
                resultsRequest.getClassificationCosts().size());
        Assertions.assertThat(evaluationResultsInfo.getConfusionMatrix()).isNotNull();
        Assertions.assertThat(evaluationResultsInfo.getConfusionMatrix().size()).isEqualTo(
                resultsRequest.getConfusionMatrix().size());
        Assertions.assertThat(evaluationResultsInfo.getClassifierOptionsInfo()).isNotNull();
        Assertions.assertThat(evaluationResultsInfo.getNumFolds().intValue()).isEqualTo(
                resultsRequest.getEvaluationMethodReport().getNumFolds().intValue());
        Assertions.assertThat(evaluationResultsInfo.getNumTests().intValue()).isEqualTo(
                resultsRequest.getEvaluationMethodReport().getNumTests().intValue());
        Assertions.assertThat(evaluationResultsInfo.getSeed().intValue()).isEqualTo(
                resultsRequest.getEvaluationMethodReport().getSeed().intValue());
    }
}
