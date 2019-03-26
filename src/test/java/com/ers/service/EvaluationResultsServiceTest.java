package com.ers.service;

import com.ers.TestHelperUtils;
import com.ers.dto.EvaluationResultsRequest;
import com.ers.dto.EvaluationResultsResponse;
import com.ers.dto.GetEvaluationResultsSimpleRequest;
import com.ers.dto.GetEvaluationResultsSimpleResponse;
import com.ers.dto.ResponseStatus;
import com.ers.mapping.ClassificationCostsReportMapperImpl;
import com.ers.mapping.ClassifierOptionsInfoMapperImpl;
import com.ers.mapping.ClassifierReportFactory;
import com.ers.mapping.ClassifierReportMapperImpl;
import com.ers.mapping.ConfusionMatrixMapperImpl;
import com.ers.mapping.EvaluationMethodMapperImpl;
import com.ers.mapping.EvaluationResultsMapperImpl;
import com.ers.mapping.InstancesMapperImpl;
import com.ers.mapping.RocCurveReportMapperImpl;
import com.ers.mapping.StatisticsReportMapperImpl;
import com.ers.model.EvaluationMethod;
import com.ers.model.EvaluationResultsInfo;
import com.ers.repository.EvaluationResultsInfoRepository;
import com.ers.repository.InstancesInfoRepository;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import java.util.UUID;

/**
 * Unit tests for checking {@link EvaluationResultsService} functionality.
 *
 * @author Roman Batygin
 */
@RunWith(SpringRunner.class)
@AutoConfigureDataJpa
@EnableJpaRepositories(basePackageClasses = EvaluationResultsInfoRepository.class)
@EntityScan(basePackageClasses = EvaluationResultsInfo.class)
@EnableConfigurationProperties
@TestPropertySource("classpath:application.properties")
@Import({EvaluationResultsMapperImpl.class, ClassificationCostsReportMapperImpl.class,
        ConfusionMatrixMapperImpl.class, EvaluationMethodMapperImpl.class,
        StatisticsReportMapperImpl.class, InstancesMapperImpl.class, RocCurveReportMapperImpl.class,
        EvaluationResultsService.class, ClassifierReportMapperImpl.class,
        ClassifierOptionsInfoMapperImpl.class, ClassifierReportFactory.class})
public class EvaluationResultsServiceTest {

    @Inject
    private EvaluationResultsService evaluationResultsService;
    @Inject
    private EvaluationResultsInfoRepository evaluationResultsInfoRepository;
    @Inject
    private InstancesInfoRepository instancesInfoRepository;

    @Before
    public void init() {
        evaluationResultsInfoRepository.deleteAll();
        instancesInfoRepository.deleteAll();
    }

    @Test
    public void testSaveEvaluationResultsReport() {
        EvaluationResultsRequest request = TestHelperUtils.buildEvaluationResultsReport(UUID.randomUUID().toString());
        EvaluationResultsResponse response = evaluationResultsService.saveEvaluationResults(request);
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(ResponseStatus.SUCCESS);
        EvaluationResultsInfo evaluationResultsInfo =
                evaluationResultsInfoRepository.findAll().stream().findFirst().orElse(null);
        Assertions.assertThat(evaluationResultsInfo).isNotNull();
        Assertions.assertThat(evaluationResultsInfo.getSaveDate()).isNotNull();
        Assertions.assertThat(evaluationResultsInfo.getRequestId()).isEqualTo(request.getRequestId());
        Assertions.assertThat(evaluationResultsInfo.getEvaluationMethod()).isEqualTo(EvaluationMethod.CROSS_VALIDATION);
        Assertions.assertThat(evaluationResultsInfo.getNumFolds().intValue()).isEqualTo(
                request.getEvaluationMethodReport().getNumFolds().intValue());
        Assertions.assertThat(evaluationResultsInfo.getNumTests().intValue()).isEqualTo(
                request.getEvaluationMethodReport().getNumTests().intValue());
        Assertions.assertThat(evaluationResultsInfo.getSeed().intValue()).isEqualTo(
                request.getEvaluationMethodReport().getSeed().intValue());
        Assertions.assertThat(evaluationResultsInfo.getConfusionMatrix()).isNotNull();
        Assertions.assertThat(evaluationResultsInfo.getStatistics()).isNotNull();
        Assertions.assertThat(evaluationResultsInfo.getClassificationCosts()).isNotNull();
        Assertions.assertThat(evaluationResultsInfo.getClassifierOptionsInfo()).isNotNull();
        Assertions.assertThat(evaluationResultsInfo.getInstances()).isNotNull();
    }

    @Test
    public void testExistingReport() {
        EvaluationResultsRequest request = TestHelperUtils.buildEvaluationResultsReport(UUID.randomUUID().toString());
        evaluationResultsService.saveEvaluationResults(request);
        EvaluationResultsResponse response = evaluationResultsService.saveEvaluationResults(request);
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(ResponseStatus.DUPLICATE_REQUEST_ID);
    }

    @Test
    public void testInvalidRequestId() {
        EvaluationResultsRequest request = new EvaluationResultsRequest();
        evaluationResultsService.saveEvaluationResults(request);
        EvaluationResultsResponse response = evaluationResultsService.saveEvaluationResults(request);
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(ResponseStatus.INVALID_REQUEST_ID);
    }

    @Test
    public void testInvalidRequestParams() {
        EvaluationResultsRequest request = new EvaluationResultsRequest();
        request.setRequestId(UUID.randomUUID().toString());
        evaluationResultsService.saveEvaluationResults(request);
        EvaluationResultsResponse response = evaluationResultsService.saveEvaluationResults(request);
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(ResponseStatus.INVALID_REQUEST_PARAMS);
    }

    @Test
    public void testDataCache() {
        EvaluationResultsRequest request = TestHelperUtils.buildEvaluationResultsReport(UUID.randomUUID().toString());
        evaluationResultsService.saveEvaluationResults(request);
        request.setRequestId(UUID.randomUUID().toString());
        EvaluationResultsResponse response = evaluationResultsService.saveEvaluationResults(request);
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(ResponseStatus.SUCCESS);
        Assertions.assertThat(instancesInfoRepository.count()).isEqualTo(1);
    }

    @Test
    public void testGetEvaluationResultsWithInvalidId() {
        GetEvaluationResultsSimpleRequest request = TestHelperUtils.buildGetEvaluationResultsRequest(null);
        GetEvaluationResultsSimpleResponse response =
                evaluationResultsService.getEvaluationResultsSimpleResponse(request);
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getRequestId()).isEqualTo(request.getRequestId());
        Assertions.assertThat(response.getStatus()).isEqualTo(ResponseStatus.INVALID_REQUEST_ID);
    }

    @Test
    public void testGetEvaluationResultsNotFound() {
        GetEvaluationResultsSimpleRequest request =
                TestHelperUtils.buildGetEvaluationResultsRequest(UUID.randomUUID().toString());
        GetEvaluationResultsSimpleResponse response =
                evaluationResultsService.getEvaluationResultsSimpleResponse(request);
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getRequestId()).isEqualTo(request.getRequestId());
        Assertions.assertThat(response.getStatus()).isEqualTo(ResponseStatus.RESULTS_NOT_FOUND);
    }

    @Test
    public void testGetEvaluationResults() {
        EvaluationResultsRequest evaluationResultsRequest =
                TestHelperUtils.buildEvaluationResultsReport(UUID.randomUUID().toString());
        EvaluationResultsResponse evaluationResultsResponse =
                evaluationResultsService.saveEvaluationResults(evaluationResultsRequest);
        Assertions.assertThat(evaluationResultsResponse).isNotNull();
        Assertions.assertThat(evaluationResultsResponse.getStatus()).isEqualTo(ResponseStatus.SUCCESS);
        GetEvaluationResultsSimpleRequest request =
                TestHelperUtils.buildGetEvaluationResultsRequest(evaluationResultsRequest.getRequestId());
        GetEvaluationResultsSimpleResponse response =
                evaluationResultsService.getEvaluationResultsSimpleResponse(request);
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getRequestId()).isEqualTo(request.getRequestId());
        Assertions.assertThat(response.getStatus()).isEqualTo(ResponseStatus.SUCCESS);
        Assertions.assertThat(response.getClassifierReport()).isNotNull();
        Assertions.assertThat(response.getEvaluationMethodReport()).isNotNull();
        Assertions.assertThat(response.getStatistics()).isNotNull();
    }

    @After
    public void doAfter() {
        evaluationResultsInfoRepository.deleteAll();
        instancesInfoRepository.deleteAll();
    }
}
