package com.ers.service;

import com.ers.TestHelperUtils;
import com.ers.dto.EvaluationResultsRequest;
import com.ers.dto.EvaluationResultsResponse;
import com.ers.dto.GetEvaluationResultsRequest;
import com.ers.dto.GetEvaluationResultsResponse;
import com.ers.dto.InstancesReport;
import com.ers.dto.ResponseStatus;
import com.ers.mapping.ClassificationCostsReportMapperImpl;
import com.ers.mapping.ClassifierOptionsInfoMapperImpl;
import com.ers.mapping.ClassifierReportFactory;
import com.ers.mapping.ClassifierReportMapperImpl;
import com.ers.mapping.ConfusionMatrixMapperImpl;
import com.ers.mapping.EvaluationMethodMapperImpl;
import com.ers.mapping.EvaluationResultsMapperImpl;
import com.ers.mapping.InstancesMapperImpl;
import com.ers.mapping.RocCurveDataMapperImpl;
import com.ers.mapping.RocCurvePointMapperImpl;
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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
        ClassifierOptionsInfoMapperImpl.class, ClassifierReportFactory.class,
        RocCurvePointMapperImpl.class, RocCurveDataMapperImpl.class})
public class EvaluationResultsServiceTest {

    private static final int NUM_THREADS = 2;

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
                evaluationResultsInfoRepository.findByRequestId(request.getRequestId());
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
        Assertions.assertThat(evaluationResultsInfo.getConfusionMatrix()).hasSameSizeAs(request.getConfusionMatrix());
        Assertions.assertThat(evaluationResultsInfo.getStatistics()).isNotNull();
        Assertions.assertThat(evaluationResultsInfo.getClassificationCosts()).isNotNull();
        Assertions.assertThat(evaluationResultsInfo.getClassificationCosts()).hasSameSizeAs(
                request.getClassificationCosts());
        Assertions.assertThat(evaluationResultsInfo.getClassifierOptionsInfo()).isNotNull();
        Assertions.assertThat(evaluationResultsInfo.getInstances()).isNotNull();
        //Assertion roc curve data
        Assertions.assertThat(evaluationResultsInfo.getRocCurveData()).isNotNull();
        Assertions.assertThat(evaluationResultsInfo.getRocCurveData()).hasSameSizeAs(request.getRocCurveData());
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
        Assertions.assertThat(instancesInfoRepository.count()).isOne();
    }

    @Test
    public void testDuplicateRequestIdInMultiThreadEnvironment() throws Exception {
        final String requestId = UUID.randomUUID().toString();
        final CountDownLatch finishedLatch = new CountDownLatch(NUM_THREADS);
        ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS);
        for (int i = 0; i < NUM_THREADS; i++) {
            executorService.submit(() -> {
                EvaluationResultsRequest request = TestHelperUtils.buildEvaluationResultsReport(requestId);
                evaluationResultsService.saveEvaluationResults(request);
                finishedLatch.countDown();
            });
        }
        finishedLatch.await();
        executorService.shutdownNow();
        Assertions.assertThat(evaluationResultsInfoRepository.count()).isOne();
    }

    @Test
    public void testDataCacheIdInMultiThreadEnvironment() throws Exception {
        final CountDownLatch finishedLatch = new CountDownLatch(NUM_THREADS);
        ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS);
        final InstancesReport instancesReport = TestHelperUtils.buildInstancesReport();
        for (int i = 0; i < NUM_THREADS; i++) {
            executorService.submit(() -> {
                EvaluationResultsRequest request =
                        TestHelperUtils.buildEvaluationResultsReport(UUID.randomUUID().toString());
                request.setInstances(instancesReport);
                evaluationResultsService.saveEvaluationResults(request);
                finishedLatch.countDown();
            });
        }
        finishedLatch.await();
        executorService.shutdownNow();
        Assertions.assertThat(evaluationResultsInfoRepository.count()).isEqualTo(2);
        Assertions.assertThat(instancesInfoRepository.count()).isOne();
    }


    @Test
    public void testGetEvaluationResultsWithInvalidId() {
        GetEvaluationResultsRequest request = TestHelperUtils.buildGetEvaluationResultsRequest(null);
        GetEvaluationResultsResponse response =
                evaluationResultsService.getEvaluationResultsResponse(request);
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getRequestId()).isEqualTo(request.getRequestId());
        Assertions.assertThat(response.getStatus()).isEqualTo(ResponseStatus.INVALID_REQUEST_ID);
    }

    @Test
    public void testGetEvaluationResultsNotFound() {
        GetEvaluationResultsRequest request =
                TestHelperUtils.buildGetEvaluationResultsRequest(UUID.randomUUID().toString());
        GetEvaluationResultsResponse response =
                evaluationResultsService.getEvaluationResultsResponse(request);
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
        GetEvaluationResultsRequest request =
                TestHelperUtils.buildGetEvaluationResultsRequest(evaluationResultsRequest.getRequestId());
        GetEvaluationResultsResponse response =
                evaluationResultsService.getEvaluationResultsResponse(request);
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getRequestId()).isEqualTo(request.getRequestId());
        Assertions.assertThat(response.getStatus()).isEqualTo(ResponseStatus.SUCCESS);
        Assertions.assertThat(response.getClassifierReport()).isNotNull();
        Assertions.assertThat(response.getEvaluationMethodReport()).isNotNull();
        Assertions.assertThat(response.getStatistics()).isNotNull();
        Assertions.assertThat(response.getClassificationCosts()).hasSameSizeAs(evaluationResultsRequest.getClassificationCosts());
        Assertions.assertThat(response.getConfusionMatrix()).hasSameSizeAs(evaluationResultsRequest.getConfusionMatrix());
        Assertions.assertThat(response.getRocCurveData()).hasSameSizeAs(evaluationResultsRequest.getRocCurveData());
        Assertions.assertThat(response.getInstances()).isNotNull();
    }

    @After
    public void doAfter() {
        evaluationResultsInfoRepository.deleteAll();
        instancesInfoRepository.deleteAll();
    }
}
