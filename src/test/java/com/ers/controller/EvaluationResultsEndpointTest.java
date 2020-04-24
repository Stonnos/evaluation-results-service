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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ws.test.server.MockWebServiceClient;
import org.springframework.xml.transform.StringResult;
import org.springframework.xml.transform.StringSource;

import javax.inject.Inject;
import java.io.IOException;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.ws.test.server.RequestCreators.withPayload;
import static org.springframework.ws.test.server.ResponseMatchers.noFault;
import static org.springframework.ws.test.server.ResponseMatchers.payload;
import static org.springframework.ws.test.server.ResponseMatchers.validPayload;

/**
 * Tests for checking {@link EvaluationResultsEndpoint} functionality.
 *
 * @author Roman Batygin
 */
@ExtendWith(SpringExtension.class)
@Import({WebServiceTestConfiguration.class, EvaluationResultsEndpoint.class})
public class EvaluationResultsEndpointTest {

    @Inject
    private ApplicationContext applicationContext;
    @Inject
    private Jaxb2Marshaller jaxb2Marshaller;
    @MockBean
    private EvaluationResultsService evaluationResultsService;
    @MockBean
    private ClassifierOptionsRequestService classifierOptionsRequestService;

    private Resource xsdSchema = new ClassPathResource("evaluation-results.xsd");

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

    private StringSource getPayload(Object object) {
        StringResult stringResult = new StringResult();
        jaxb2Marshaller.marshal(object, stringResult);
        return new StringSource(stringResult.toString());
    }
}
