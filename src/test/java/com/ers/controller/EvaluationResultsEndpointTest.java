package com.ers.controller;

import com.ers.TestHelperUtils;
import com.ers.config.WebServiceTestConfiguration;
import com.ers.dto.ClassificationCostsReport;
import com.ers.dto.ClassifierOptionsRequest;
import com.ers.dto.ClassifierOptionsResponse;
import com.ers.dto.ConfusionMatrixReport;
import com.ers.dto.EvaluationMethod;
import com.ers.dto.EvaluationResultsRequest;
import com.ers.dto.EvaluationResultsResponse;
import com.ers.dto.GetEvaluationResultsRequest;
import com.ers.dto.GetEvaluationResultsResponse;
import com.ers.dto.ResponseStatus;
import com.ers.service.ClassifierOptionsRequestService;
import com.ers.service.EvaluationResultsService;
import com.google.common.collect.ImmutableList;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ws.test.server.MockWebServiceClient;
import org.springframework.xml.transform.StringResult;
import org.springframework.xml.transform.StringSource;

import javax.inject.Inject;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.ws.test.server.RequestCreators.withPayload;
import static org.springframework.ws.test.server.ResponseMatchers.clientOrSenderFault;
import static org.springframework.ws.test.server.ResponseMatchers.noFault;
import static org.springframework.ws.test.server.ResponseMatchers.payload;
import static org.springframework.ws.test.server.ResponseMatchers.validPayload;

/**
 * Tests for checking {@link EvaluationResultsEndpoint} functionality.
 *
 * @author Roman Batygin
 */
@ExtendWith(SpringExtension.class)
@TestPropertySource("classpath:application.properties")
@Import({WebServiceTestConfiguration.class, EvaluationResultsEndpoint.class})
public class EvaluationResultsEndpointTest {

    private static final int STRING_LENGTH = 256;
    private static final BigDecimal NEGATIVE_VALUE = BigDecimal.valueOf(-1L);

    private static final List<String> CLASSIFIER_FIELDS_NULL_TEST =
            ImmutableList.of("classifierName", "options");
    private static final List<String> INSTANCES_FIELDS_NULL_TEST =
            ImmutableList.of("xmlInstances", "relationName", "numInstances", "numAttributes", "numClasses",
                    "className");
    private static final List<String> EVALUATION_METHOD_REPORT_FIELDS_NULL_TEST =
            ImmutableList.of("evaluationMethod");
    private static final List<String> STATISTICS_FIELDS_NULL_TEST =
            ImmutableList.of("numTestInstances", "numCorrect", "numIncorrect", "pctCorrect", "pctIncorrect");

    private static final List<String> CLASSIFIER_FIELDS_EMPTY_TEST =
            ImmutableList.of("classifierName", "options");
    private static final List<String> INSTANCES_FIELDS_EMPTY_TEST =
            ImmutableList.of("xmlInstances", "relationName", "className");

    private static final List<String> CLASSIFIER_FIELDS_LARGE_TEST =
            ImmutableList.of("classifierName", "classifierDescription");
    private static final List<String> INSTANCES_FIELDS_LARGE_TEST =
            ImmutableList.of("relationName", "className");

    private static final List<String> STATISTICS_PERCENTAGE_FIELDS_BOUNDS_TEST =
            ImmutableList.of("pctCorrect", "pctIncorrect");
    private static final List<String> STATISTICS_DECIMAL_FIELDS_BOUNDS_TEST =
            ImmutableList.of("meanAbsoluteError", "rootMeanSquaredError", "maxAucValue", "varianceError",
                    "confidenceIntervalLowerBound", "confidenceIntervalUpperBound");

    @Inject
    private ApplicationContext applicationContext;
    @Inject
    private Jaxb2Marshaller jaxb2Marshaller;
    @MockBean
    private EvaluationResultsService evaluationResultsService;
    @MockBean
    private ClassifierOptionsRequestService classifierOptionsRequestService;

    private final Resource xsdSchema = new ClassPathResource("evaluation-results.xsd");

    private final PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();

    private MockWebServiceClient mockClient;

    @BeforeEach
    public void init() {
        mockClient = MockWebServiceClient.createClient(applicationContext);
    }

    @Test
    public void testSaveEvaluationReport() throws IOException {
        EvaluationResultsRequest evaluationResultsRequest =
                TestHelperUtils.buildEvaluationResultsReport(UUID.randomUUID().toString());
        StringSource request = getPayload(evaluationResultsRequest);
        EvaluationResultsResponse evaluationResultsResponse = new EvaluationResultsResponse();
        evaluationResultsResponse.setRequestId(evaluationResultsRequest.getRequestId());
        evaluationResultsResponse.setStatus(ResponseStatus.SUCCESS);
        StringSource response = getPayload(evaluationResultsResponse);
        when(evaluationResultsService.saveEvaluationResults(any(EvaluationResultsRequest.class))).thenReturn(
                evaluationResultsResponse);
        sendRequestTest(request, response);
    }

    @Test
    public void testSaveEvaluationReportWithInvalidRequestId() {
        EvaluationResultsRequest evaluationResultsRequest = TestHelperUtils.buildEvaluationResultsReport("test-uuid");
        sendRequestTestWithFaultAsExpected(evaluationResultsRequest);
    }

    @Test
    public void testSaveEvaluationReportWithEmptyClassifierReportFields() {
        internalTestEmptyFields(CLASSIFIER_FIELDS_EMPTY_TEST, EvaluationResultsRequest::getClassifierReport);
    }

    @Test
    public void testSaveEvaluationReportWithEmptyInstancesReportFields() {
        internalTestEmptyFields(INSTANCES_FIELDS_EMPTY_TEST, EvaluationResultsRequest::getInstances);
    }

    @Test
    public void testSaveEvaluationReportWithLargeClassifierReportFields() {
        internalTestLargeFields(CLASSIFIER_FIELDS_LARGE_TEST, EvaluationResultsRequest::getClassifierReport);
    }

    @Test
    public void testSaveEvaluationReportWithLargeInstancesReportFields() {
        internalTestLargeFields(INSTANCES_FIELDS_LARGE_TEST, EvaluationResultsRequest::getInstances);
    }

    @Test
    public void testSaveEvaluationReportWithNotValidPercentageFields() {
        internalTestFieldsWithConstraints(STATISTICS_PERCENTAGE_FIELDS_BOUNDS_TEST,
                EvaluationResultsRequest::getStatistics, NEGATIVE_VALUE);
        internalTestFieldsWithConstraints(STATISTICS_PERCENTAGE_FIELDS_BOUNDS_TEST,
                EvaluationResultsRequest::getStatistics, BigDecimal.valueOf(101L));
    }

    @Test
    public void testSaveEvaluationReportWithNotValidDecimalFields() {
        internalTestFieldsWithConstraints(STATISTICS_DECIMAL_FIELDS_BOUNDS_TEST,
                EvaluationResultsRequest::getStatistics, NEGATIVE_VALUE);
        internalTestFieldsWithConstraints(STATISTICS_DECIMAL_FIELDS_BOUNDS_TEST,
                EvaluationResultsRequest::getStatistics, BigDecimal.valueOf(1.01d));
    }

    @Test
    public void testSaveEvaluationReportWithNullInstancesReportFields() {
        internalTestNullFields(INSTANCES_FIELDS_NULL_TEST, EvaluationResultsRequest::getInstances);
    }

    @Test
    public void testSaveEvaluationReportWithNullClassifierReportFields() {
        internalTestNullFields(CLASSIFIER_FIELDS_NULL_TEST, EvaluationResultsRequest::getClassifierReport);
    }

    @Test
    public void testSaveEvaluationReportWithNullEvaluationMethodReportFields() {
        internalTestNullFields(EVALUATION_METHOD_REPORT_FIELDS_NULL_TEST,
                EvaluationResultsRequest::getEvaluationMethodReport);
    }

    @Test
    public void testSaveEvaluationReportWithNullStatisticsReportFields() {
        internalTestNullFields(STATISTICS_FIELDS_NULL_TEST, EvaluationResultsRequest::getStatistics);
    }

    @Test
    public void testSaveEvaluationReportWithNullClassificationCostsRecord() {
        EvaluationResultsRequest evaluationResultsRequest =
                TestHelperUtils.buildEvaluationResultsReport(UUID.randomUUID().toString());
        evaluationResultsRequest.getClassificationCosts().add(new ClassificationCostsReport());
        sendRequestTestWithFaultAsExpected(evaluationResultsRequest);
    }

    @Test
    public void testSaveEvaluationReportWithNullConfusionMatrixRecord() {
        EvaluationResultsRequest evaluationResultsRequest =
                TestHelperUtils.buildEvaluationResultsReport(UUID.randomUUID().toString());
        evaluationResultsRequest.getConfusionMatrix().add(new ConfusionMatrixReport());
        sendRequestTestWithFaultAsExpected(evaluationResultsRequest);
    }

    @Test
    public void testGetEvaluationResultsReport() throws IOException {
        GetEvaluationResultsRequest getEvaluationResultsRequest =
                TestHelperUtils.buildGetEvaluationResultsRequest(UUID.randomUUID().toString());
        StringSource request = getPayload(getEvaluationResultsRequest);
        GetEvaluationResultsResponse getEvaluationResultsResponse =
                TestHelperUtils.buildGetEvaluationResultsResponse(getEvaluationResultsRequest.getRequestId());
        StringSource response = getPayload(getEvaluationResultsResponse);
        when(evaluationResultsService.getEvaluationResultsResponse(any(GetEvaluationResultsRequest.class))).thenReturn(
                getEvaluationResultsResponse);
        sendRequestTest(request, response);
    }

    @Test
    public void testFindClassifierOptions() throws IOException {
        ClassifierOptionsRequest classifierOptionsRequest =
                TestHelperUtils.createClassifierOptionsRequest(EvaluationMethod.CROSS_VALIDATION);
        StringSource request = getPayload(classifierOptionsRequest);
        ClassifierOptionsResponse classifierOptionsResponse = new ClassifierOptionsResponse();
        classifierOptionsResponse.setRequestId(UUID.randomUUID().toString());
        classifierOptionsResponse.setStatus(ResponseStatus.SUCCESS);
        when(classifierOptionsRequestService.findClassifierOptions(any(ClassifierOptionsRequest.class))).thenReturn(
                classifierOptionsResponse);
        StringSource response = getPayload(classifierOptionsResponse);
        sendRequestTest(request, response);
    }

    private void sendRequestTest(StringSource request, StringSource response) throws IOException {
        mockClient.sendRequest(withPayload(request))
                .andExpect(noFault())
                .andExpect(payload(response))
                .andExpect(validPayload(xsdSchema));
    }

    private void sendRequestTestWithFaultAsExpected(EvaluationResultsRequest evaluationResultsRequest) {
        StringSource request = getPayload(evaluationResultsRequest);
        mockClient.sendRequest(withPayload(request)).andExpect(clientOrSenderFault());
    }

    private StringSource getPayload(Object object) {
        StringResult stringResult = new StringResult();
        jaxb2Marshaller.marshal(object, stringResult);
        return new StringSource(stringResult.toString());
    }

    private <T> void internalTestEmptyFields(List<String> testFields,
                                             Function<EvaluationResultsRequest, T> targetFunction) {
        internalTestFieldsWithConstraints(testFields, targetFunction, StringUtils.EMPTY);
    }

    private <T> void internalTestNullFields(List<String> testFields,
                                            Function<EvaluationResultsRequest, T> targetFunction) {
        internalTestFieldsWithConstraints(testFields, targetFunction, null);
    }

    private <T> void internalTestLargeFields(List<String> testFields,
                                             Function<EvaluationResultsRequest, T> targetFunction) {
        internalTestFieldsWithConstraints(testFields, targetFunction, StringUtils.repeat('Q', STRING_LENGTH));
    }

    private <T> void internalTestFieldsWithConstraints(List<String> testFields,
                                                       Function<EvaluationResultsRequest, T> targetFunction,
                                                       Object value) {
        for (String field : testFields) {
            try {
                EvaluationResultsRequest evaluationResultsRequest =
                        TestHelperUtils.buildEvaluationResultsReport(UUID.randomUUID().toString());
                T target = targetFunction.apply(evaluationResultsRequest);
                propertyUtilsBean.setProperty(target, field, value);
                sendRequestTestWithFaultAsExpected(evaluationResultsRequest);
            } catch (Exception ex) {
                throw new IllegalStateException(ex);
            }
        }
    }
}
