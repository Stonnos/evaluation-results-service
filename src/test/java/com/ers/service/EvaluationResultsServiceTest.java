package com.ers.service;

import com.ers.TestHelperUtils;
import com.ers.dto.EvaluationResultsReport;
import com.ers.dto.EvaluationResultsResponse;
import com.ers.dto.ResponseStatus;
import com.ers.model.EvaluationMethod;
import com.ers.model.EvaluationResultsInfo;
import com.ers.repository.EvaluationResultsInfoRepository;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import java.util.UUID;

/**
 * Unit tests for checking {@link EvaluationResultsService} functionality.
 *
 * @author Roman Batygin
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class EvaluationResultsServiceTest {

    @Inject
    private EvaluationResultsService evaluationResultsService;
    @Inject
    private EvaluationResultsInfoRepository evaluationResultsInfoRepository;

    @Before
    public void init() {
        evaluationResultsInfoRepository.deleteAll();
    }

    @Test
    public void testSaveEvaluationResultsReport() {
        EvaluationResultsReport report = TestHelperUtils.buildEvaluationResultsReport(UUID.randomUUID().toString());
        EvaluationResultsResponse response = evaluationResultsService.saveEvaluationResults(report);
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(ResponseStatus.SUCCESS);
        EvaluationResultsInfo evaluationResultsInfo =
                evaluationResultsInfoRepository.findAll().stream().findFirst().orElse(null);
        Assertions.assertThat(evaluationResultsInfo).isNotNull();
        Assertions.assertThat(evaluationResultsInfo.getSaveDate()).isNotNull();
        Assertions.assertThat(evaluationResultsInfo.getRequestId()).isEqualTo(report.getRequestId());
        Assertions.assertThat(evaluationResultsInfo.getClassifierName()).isEqualTo(
                report.getClassifierReport().getClassifierName());
        Assertions.assertThat(evaluationResultsInfo.getEvaluationMethod()).isEqualTo(EvaluationMethod.CROSS_VALIDATION);
        Assertions.assertThat(evaluationResultsInfo.getEvaluationOptionsMap()).isNotNull();
        Assertions.assertThat(evaluationResultsInfo.getConfusionMatrix()).isNotNull();
        Assertions.assertThat(evaluationResultsInfo.getStatistics()).isNotNull();
        Assertions.assertThat(evaluationResultsInfo.getClassificationCosts()).isNotNull();
    }

    @Test
    public void testExistingReport() {
        EvaluationResultsReport report = TestHelperUtils.buildEvaluationResultsReport(UUID.randomUUID().toString());
        evaluationResultsService.saveEvaluationResults(report);
        EvaluationResultsResponse response = evaluationResultsService.saveEvaluationResults(report);
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(ResponseStatus.DUPLICATE_REQUEST_ID);
    }

    @Test
    public void testInvalidRequestId() {
        EvaluationResultsReport report = new EvaluationResultsReport();
        evaluationResultsService.saveEvaluationResults(report);
        EvaluationResultsResponse response = evaluationResultsService.saveEvaluationResults(report);
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(ResponseStatus.INVALID_REQUEST_ID);
    }
}
