package com.ers.controller;

import com.ers.TestHelperUtils;
import com.ers.config.WebServiceTestConfiguration;
import com.ers.dto.ClassifierOptionsRequest;
import com.ers.dto.ClassifierOptionsResponse;
import com.ers.dto.EvaluationMethod;
import com.ers.dto.EvaluationResultsRequest;
import com.ers.dto.EvaluationResultsResponse;
import com.ers.dto.GetEvaluationResultsRequest;
import com.ers.dto.GetEvaluationResultsResponse;
import com.ers.dto.ResponseStatus;
import com.ers.service.ClassifierOptionsRequestService;
import com.ers.service.EvaluationResultsService;
import com.google.common.collect.ImmutableList;
import io.micrometer.core.instrument.MeterRegistry;
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
import java.util.Collections;
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
class EvaluationResultsEndpointTest {

    private static final int STRING_LENGTH = 256;
    private static final BigDecimal NEGATIVE_VALUE = BigDecimal.valueOf(-1L);

    /**
     * Required fields for tests
     */
    private static final List<String> EVALUATION_REQUEST_NULL_TEST =
            ImmutableList.of("requestId", "instances", "classifierReport", "evaluationMethodReport", "statistics");
    private static final List<String> CLASSIFIER_FIELDS_NULL_TEST =
            ImmutableList.of("classifierName", "options");
    private static final List<String> INSTANCES_FIELDS_NULL_TEST =
            ImmutableList.of("xmlInstances", "relationName", "numInstances", "numAttributes", "numClasses",
                    "className");
    private static final List<String> EVALUATION_METHOD_REPORT_FIELDS_NULL_TEST =
            ImmutableList.of("evaluationMethod");
    private static final List<String> STATISTICS_FIELDS_NULL_TEST =
            ImmutableList.of("numTestInstances", "numCorrect", "numIncorrect", "pctCorrect", "pctIncorrect");
    private static final List<String> CLASSIFICATION_COSTS_FIELDS_NULL_TEST =
            ImmutableList.of("classValue", "truePositiveRate", "falsePositiveRate", "trueNegativeRate",
                    "falseNegativeRate", "rocCurve");
    private static final List<String> CONFUSION_MATRIX_FIELDS_NULL_TEST =
            ImmutableList.of("actualClass", "predictedClass", "numInstances");
    private static final List<String> ROC_CURVE_FIELDS_NULL_TEST =
            ImmutableList.of("aucValue", "specificity", "sensitivity", "thresholdValue");
    private static final List<String> INPUT_OPTIONS_MAP_NULL_TEST =
            ImmutableList.of("key", "value");

    /**
     * Not empty string fields for tests
     */
    private static final List<String> CLASSIFIER_FIELDS_EMPTY_TEST =
            ImmutableList.of("classifierName", "options");
    private static final List<String> INSTANCES_FIELDS_EMPTY_TEST =
            ImmutableList.of("xmlInstances", "relationName", "className");
    private static final List<String> CLASSIFICATION_COSTS_FIELDS_EMPTY_TEST =
            ImmutableList.of("classValue");
    private static final List<String> CONFUSION_MATRIX_FIELDS_EMPTY_TEST =
            ImmutableList.of("actualClass", "predictedClass");
    private static final List<String> INPUT_OPTIONS_MAP_EMPTY_TEST =
            ImmutableList.of("key", "value");

    /**
     * Not large string fields to tests
     */
    private static final List<String> CLASSIFIER_FIELDS_LARGE_TEST =
            ImmutableList.of("classifierName", "classifierDescription");
    private static final List<String> INSTANCES_FIELDS_LARGE_TEST =
            ImmutableList.of("relationName", "className");
    private static final List<String> CLASSIFICATION_COSTS_FIELDS_LARGE_TEST =
            ImmutableList.of("classValue");
    private static final List<String> CONFUSION_MATRIX_FIELDS_LARGE_TEST =
            ImmutableList.of("actualClass", "predictedClass");
    private static final List<String> INPUT_OPTIONS_MAP_LARGE_TEST =
            ImmutableList.of("key", "value");

    /**
     * Decimal fields to tests
     */
    private static final List<String> STATISTICS_PERCENTAGE_FIELDS_BOUNDS_TEST =
            ImmutableList.of("pctCorrect", "pctIncorrect");
    private static final List<String> STATISTICS_DECIMAL_FIELDS_BOUNDS_TEST =
            ImmutableList.of("meanAbsoluteError", "rootMeanSquaredError", "maxAucValue", "varianceError",
                    "confidenceIntervalLowerBound", "confidenceIntervalUpperBound");
    private static final List<String> CLASSIFICATION_COSTS_FIELDS_BOUNDS_TEST =
            ImmutableList.of("truePositiveRate", "falsePositiveRate", "trueNegativeRate", "falseNegativeRate");
    private static final List<String> ROC_CURVE_FIELDS_BOUNDS_TEST =
            ImmutableList.of("aucValue", "specificity", "sensitivity", "thresholdValue");

    @Inject
    private ApplicationContext applicationContext;
    @Inject
    private Jaxb2Marshaller jaxb2Marshaller;
    @MockBean
    private EvaluationResultsService evaluationResultsService;
    @MockBean
    private ClassifierOptionsRequestService classifierOptionsRequestService;
    @MockBean
    private MeterRegistry meterRegistry;

    private final Resource xsdSchema = new ClassPathResource("evaluation-results.xsd");

    private final PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();

    private MockWebServiceClient mockClient;

    @BeforeEach
    void init() {
        mockClient = MockWebServiceClient.createClient(applicationContext);
    }

    @Test
    void testSaveEvaluationReport() throws IOException {
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
    void testSaveEvaluationReportWithNullFields() {
        internalTestNullFields(EVALUATION_REQUEST_NULL_TEST, Function.identity());
    }

    @Test
    void testSaveEvaluationReportWithInvalidRequestId() {
        internalTestFieldsWithConstraints(Collections.singletonList("requestId"), Function.identity(), "test-uuid");
    }

    @Test
    void testSaveEvaluationReportWithInvalidNumFolds() {
        internalTestFieldsWithConstraints(Collections.singletonList("numFolds"),
                EvaluationResultsRequest::getEvaluationMethodReport, BigDecimal.ONE.toBigInteger());
    }

    @Test
    void testSaveEvaluationReportWithInvalidNumTests() {
        internalTestFieldsWithConstraints(Collections.singletonList("numTests"),
                EvaluationResultsRequest::getEvaluationMethodReport, BigDecimal.ZERO.toBigInteger());
    }

    @Test
    void testSaveEvaluationReportWithInvalidNumInstances() {
        internalTestFieldsWithConstraints(Collections.singletonList("numInstances"),
                EvaluationResultsRequest::getInstances, BigDecimal.ONE.toBigInteger());
    }

    @Test
    void testSaveEvaluationReportWithInvalidNumAttributes() {
        internalTestFieldsWithConstraints(Collections.singletonList("numAttributes"),
                EvaluationResultsRequest::getInstances, BigDecimal.ONE.toBigInteger());
    }

    @Test
    void testSaveEvaluationReportWithInvalidNumClasses() {
        internalTestFieldsWithConstraints(Collections.singletonList("numClasses"),
                EvaluationResultsRequest::getInstances, BigDecimal.ONE.toBigInteger());
    }

    @Test
    void testSaveEvaluationReportWithInvalidNumTestInstances() {
        internalTestFieldsWithConstraints(Collections.singletonList("numTestInstances"),
                EvaluationResultsRequest::getStatistics, BigDecimal.ONE.toBigInteger());
    }

    @Test
    void testSaveEvaluationReportWithInvalidNumCorrect() {
        internalTestFieldsWithConstraints(Collections.singletonList("numCorrect"),
                EvaluationResultsRequest::getStatistics, NEGATIVE_VALUE.toBigInteger());
    }

    @Test
    void testSaveEvaluationReportWithInvalidNumIncorrect() {
        internalTestFieldsWithConstraints(Collections.singletonList("numIncorrect"),
                EvaluationResultsRequest::getStatistics, NEGATIVE_VALUE.toBigInteger());
    }

    @Test
    void testSaveEvaluationReportWithInvalidConfusionMatrixNumInstances() {
        internalTestFieldsWithConstraints(Collections.singletonList("numInstances"),
                request -> request.getConfusionMatrix().iterator().next(), NEGATIVE_VALUE.toBigInteger());
    }

    @Test
    void testSaveEvaluationReportWithEmptyClassifierReportFields() {
        internalTestEmptyFields(CLASSIFIER_FIELDS_EMPTY_TEST, EvaluationResultsRequest::getClassifierReport);
    }

    @Test
    void testSaveEvaluationReportWithEmptyInstancesReportFields() {
        internalTestEmptyFields(INSTANCES_FIELDS_EMPTY_TEST, EvaluationResultsRequest::getInstances);
    }

    @Test
    void testSaveEvaluationReportWithLargeClassifierReportFields() {
        internalTestLargeFields(CLASSIFIER_FIELDS_LARGE_TEST, EvaluationResultsRequest::getClassifierReport);
    }

    @Test
    void testSaveEvaluationReportWithLargeInstancesReportFields() {
        internalTestLargeFields(INSTANCES_FIELDS_LARGE_TEST, EvaluationResultsRequest::getInstances);
    }

    @Test
    void testSaveEvaluationReportWithNotValidPercentageFields() {
        internalTestFieldsWithConstraints(STATISTICS_PERCENTAGE_FIELDS_BOUNDS_TEST,
                EvaluationResultsRequest::getStatistics, NEGATIVE_VALUE);
        internalTestFieldsWithConstraints(STATISTICS_PERCENTAGE_FIELDS_BOUNDS_TEST,
                EvaluationResultsRequest::getStatistics, BigDecimal.valueOf(101L));
    }

    @Test
    void testSaveEvaluationReportWithNotValidStatisticsDecimalFields() {
        internalTestFieldsWithConstraints(STATISTICS_DECIMAL_FIELDS_BOUNDS_TEST,
                EvaluationResultsRequest::getStatistics, NEGATIVE_VALUE);
        internalTestFieldsWithConstraints(STATISTICS_DECIMAL_FIELDS_BOUNDS_TEST,
                EvaluationResultsRequest::getStatistics, BigDecimal.valueOf(1.01d));
    }

    @Test
    void testSaveEvaluationReportWithNullInstancesReportFields() {
        internalTestNullFields(INSTANCES_FIELDS_NULL_TEST, EvaluationResultsRequest::getInstances);
    }

    @Test
    void testSaveEvaluationReportWithNullClassifierReportFields() {
        internalTestNullFields(CLASSIFIER_FIELDS_NULL_TEST, EvaluationResultsRequest::getClassifierReport);
    }

    @Test
    void testSaveEvaluationReportWithNullEvaluationMethodReportFields() {
        internalTestNullFields(EVALUATION_METHOD_REPORT_FIELDS_NULL_TEST,
                EvaluationResultsRequest::getEvaluationMethodReport);
    }

    @Test
    void testSaveEvaluationReportWithNullStatisticsReportFields() {
        internalTestNullFields(STATISTICS_FIELDS_NULL_TEST, EvaluationResultsRequest::getStatistics);
    }

    @Test
    void testSaveEvaluationReportWithNullClassificationCostsRecordFields() {
        internalTestNullFields(CLASSIFICATION_COSTS_FIELDS_NULL_TEST,
                (request) -> request.getClassificationCosts().iterator().next());
    }

    @Test
    void testSaveEvaluationReportWithNullConfusionMatrixRecordFields() {
        internalTestNullFields(CONFUSION_MATRIX_FIELDS_NULL_TEST,
                (request) -> request.getConfusionMatrix().iterator().next());
    }

    @Test
    void testSaveEvaluationReportWithEmptyClassificationCostsRecordFields() {
        internalTestEmptyFields(CLASSIFICATION_COSTS_FIELDS_EMPTY_TEST,
                (request) -> request.getClassificationCosts().iterator().next());
    }

    @Test
    void testSaveEvaluationReportWithEmptyConfusionMatrixRecordFields() {
        internalTestEmptyFields(CONFUSION_MATRIX_FIELDS_EMPTY_TEST,
                (request) -> request.getConfusionMatrix().iterator().next());
    }

    @Test
    void testSaveEvaluationReportWithLargeClassificationCostsRecordFields() {
        internalTestLargeFields(CLASSIFICATION_COSTS_FIELDS_LARGE_TEST,
                (request) -> request.getClassificationCosts().iterator().next());
    }

    @Test
    void testSaveEvaluationReportWithLargeConfusionMatrixRecordFields() {
        internalTestLargeFields(CONFUSION_MATRIX_FIELDS_LARGE_TEST,
                (request) -> request.getConfusionMatrix().iterator().next());
    }

    @Test
    void testSaveEvaluationReportWithNotValidClassificationCostsRecordDecimalFields() {
        internalTestFieldsWithConstraints(CLASSIFICATION_COSTS_FIELDS_BOUNDS_TEST,
                (request) -> request.getClassificationCosts().iterator().next(), NEGATIVE_VALUE);
        internalTestFieldsWithConstraints(CLASSIFICATION_COSTS_FIELDS_BOUNDS_TEST,
                (request) -> request.getClassificationCosts().iterator().next(), BigDecimal.valueOf(1.01d));
    }

    @Test
    void testSaveEvaluationReportWithNullRocCurveReportFields() {
        internalTestNullFields(ROC_CURVE_FIELDS_NULL_TEST,
                (request) -> request.getClassificationCosts().iterator().next().getRocCurve());
    }

    @Test
    void testSaveEvaluationReportWithNotValidRocCurveReportDecimalFields() {
        internalTestFieldsWithConstraints(ROC_CURVE_FIELDS_BOUNDS_TEST,
                (request) -> request.getClassificationCosts().iterator().next().getRocCurve(), NEGATIVE_VALUE);
        internalTestFieldsWithConstraints(ROC_CURVE_FIELDS_BOUNDS_TEST,
                (request) -> request.getClassificationCosts().iterator().next().getRocCurve(),
                BigDecimal.valueOf(1.01d));
    }

    @Test
    void testSaveEvaluationReportWithNullInputOptionsMapFields() {
        internalTestNullFields(INPUT_OPTIONS_MAP_NULL_TEST,
                (request) -> request.getClassifierReport().getInputOptionsMap().getEntry().iterator().next());
    }

    @Test
    void testSaveEvaluationReportWithEmptyInputOptionsMapFields() {
        internalTestEmptyFields(INPUT_OPTIONS_MAP_EMPTY_TEST,
                (request) -> request.getClassifierReport().getInputOptionsMap().getEntry().iterator().next());
    }

    @Test
    void testSaveEvaluationReportWitLargeInputOptionsMapFields() {
        internalTestLargeFields(INPUT_OPTIONS_MAP_LARGE_TEST,
                (request) -> request.getClassifierReport().getInputOptionsMap().getEntry().iterator().next());
    }

    @Test
    void testGetEvaluationResultsReport() throws IOException {
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
    void testFindClassifierOptions() throws IOException {
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
