package com.ers.mapping;

import com.ers.TestHelperUtils;
import com.ers.dto.EvaluationResultsRequest;
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
 * Unit tests for checking {@link EvaluationResultsRequestMapper} functionality.
 *
 * @author Roman Batygin
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class EvaluationResultsRequestMapperTest {

    @Inject
    private EvaluationResultsRequestMapper evaluationResultsRequestMapper;

    @Test
    public void testMapEvaluationResultsReport() {
        EvaluationResultsRequest resultsRequest =
                TestHelperUtils.buildEvaluationResultsReport(UUID.randomUUID().toString());
        EvaluationResultsInfo evaluationResultsInfo = evaluationResultsRequestMapper.map(resultsRequest);
        Assertions.assertThat(evaluationResultsInfo.getRequestId()).isEqualTo(resultsRequest.getRequestId());
        Assertions.assertThat(evaluationResultsInfo.getEvaluationMethod()).isEqualTo(EvaluationMethod.CROSS_VALIDATION);
        Assertions.assertThat(evaluationResultsInfo.getStatistics()).isNotNull();
        Assertions.assertThat(evaluationResultsInfo.getInstances()).isNotNull();
        Assertions.assertThat(evaluationResultsInfo.getClassificationCosts()).isNotNull();
        Assertions.assertThat(evaluationResultsInfo.getClassificationCosts().size()).isEqualTo(
                resultsRequest.getClassificationCosts().size());
        Assertions.assertThat(evaluationResultsInfo.getConfusionMatrix()).isNotNull();
        Assertions.assertThat(evaluationResultsInfo.getConfusionMatrix().size()).isEqualTo(
                resultsRequest.getConfusionMatrix().size());
        Assertions.assertThat(evaluationResultsInfo.getClassifierOptionsInfo()).isNotNull();
    }
}
