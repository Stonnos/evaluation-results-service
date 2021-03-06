package com.ers.mapping;

import com.ers.TestHelperUtils;
import com.ers.dto.EvaluationMethod;
import com.ers.dto.EvaluationResultsRequest;
import com.ers.model.EvaluationResultsInfo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.inject.Inject;
import java.util.UUID;

/**
 * Unit tests for checking {@link EvaluationResultsMapper} functionality.
 *
 * @author Roman Batygin
 */
@ExtendWith(SpringExtension.class)
@Import({EvaluationResultsMapperImpl.class, ClassificationCostsReportMapperImpl.class,
        ConfusionMatrixMapperImpl.class, StatisticsReportMapperImpl.class,
        InstancesMapperImpl.class, ClassifierReportMapperImpl.class,
        RocCurveReportMapperImpl.class, ClassifierOptionsInfoMapperImpl.class, ClassifierReportFactory.class})
class EvaluationResultsMapperTest {

    @Inject
    private EvaluationResultsMapper evaluationResultsMapper;

    @Test
    void testMapEvaluationResultsReport() {
        EvaluationResultsRequest resultsRequest =
                TestHelperUtils.buildEvaluationResultsReport(UUID.randomUUID().toString());
        EvaluationResultsInfo evaluationResultsInfo = evaluationResultsMapper.map(resultsRequest);
        Assertions.assertThat(evaluationResultsInfo.getRequestId()).isEqualTo(resultsRequest.getRequestId());
        Assertions.assertThat(evaluationResultsInfo.getEvaluationMethod()).isEqualTo(EvaluationMethod.CROSS_VALIDATION);
        Assertions.assertThat(evaluationResultsInfo.getStatistics()).isNotNull();
        Assertions.assertThat(evaluationResultsInfo.getClassificationCosts()).isNotNull();
        Assertions.assertThat(evaluationResultsInfo.getClassificationCosts()).hasSameSizeAs(
                resultsRequest.getClassificationCosts());
        Assertions.assertThat(evaluationResultsInfo.getConfusionMatrix()).isNotNull();
        Assertions.assertThat(evaluationResultsInfo.getConfusionMatrix()).hasSameSizeAs(
                resultsRequest.getConfusionMatrix());
        Assertions.assertThat(evaluationResultsInfo.getClassifierOptionsInfo()).isNotNull();
        Assertions.assertThat(evaluationResultsInfo.getNumFolds().intValue()).isEqualTo(
                resultsRequest.getEvaluationMethodReport().getNumFolds().intValue());
        Assertions.assertThat(evaluationResultsInfo.getNumTests().intValue()).isEqualTo(
                resultsRequest.getEvaluationMethodReport().getNumTests().intValue());
        Assertions.assertThat(evaluationResultsInfo.getSeed().intValue()).isEqualTo(
                resultsRequest.getEvaluationMethodReport().getSeed().intValue());
    }
}
