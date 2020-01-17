package com.ers.service;

import com.ers.TestHelperUtils;
import com.ers.dto.ClassifierOptionsRequest;
import com.ers.dto.ClassifierOptionsResponse;
import com.ers.dto.EvaluationMethod;
import com.ers.dto.ResponseStatus;
import com.ers.exception.DataNotFoundException;
import com.ers.mapping.ClassifierOptionsInfoMapper;
import com.ers.mapping.ClassifierOptionsInfoMapperImpl;
import com.ers.mapping.ClassifierReportFactory;
import com.ers.model.ClassifierOptionsInfo;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;

/**
 * Unit tests for checking {@link ClassifierOptionsRequestService} functionality.
 *
 * @author Roman Batygin
 */
@RunWith(SpringRunner.class)
@Import({ClassifierReportFactory.class, ClassifierOptionsInfoMapperImpl.class})
public class ClassifierOptionsRequestServiceTest {

    @Inject
    private ClassifierOptionsInfoMapper classifierOptionsInfoMapper;

    @Mock
    private ClassifierOptionsService classifierOptionsService;

    private ClassifierOptionsRequestService classifierOptionsRequestService;

    @Before
    public void init() {
        classifierOptionsRequestService =
                new ClassifierOptionsRequestService(classifierOptionsService, classifierOptionsInfoMapper);
    }

    @Test
    public void testNullInstancesReport() {
        ClassifierOptionsRequest request =
                TestHelperUtils.createClassifierOptionsRequest(EvaluationMethod.CROSS_VALIDATION);
        request.setInstances(null);
        ClassifierOptionsResponse response = classifierOptionsRequestService.findClassifierOptions(request);
        assertResponse(response, ResponseStatus.INVALID_REQUEST_PARAMS);
    }

    @Test
    public void testEmptyXmlInstances() {
        ClassifierOptionsRequest request =
                TestHelperUtils.createClassifierOptionsRequest(EvaluationMethod.CROSS_VALIDATION);
        request.getInstances().setXmlInstances(StringUtils.EMPTY);
        ClassifierOptionsResponse response = classifierOptionsRequestService.findClassifierOptions(request);
        assertResponse(response, ResponseStatus.INVALID_REQUEST_PARAMS);
    }

    @Test
    public void testNullEvaluationMethodReport() {
        ClassifierOptionsRequest request =
                TestHelperUtils.createClassifierOptionsRequest(EvaluationMethod.CROSS_VALIDATION);
        request.setEvaluationMethodReport(null);
        ClassifierOptionsResponse response = classifierOptionsRequestService.findClassifierOptions(request);
        assertResponse(response, ResponseStatus.INVALID_REQUEST_PARAMS);
    }

    @Test
    public void testResultsNotFoundStatus() {
        ClassifierOptionsRequest request =
                TestHelperUtils.createClassifierOptionsRequest(EvaluationMethod.CROSS_VALIDATION);
        when(classifierOptionsService.findBestClassifierOptions(request)).thenReturn(Collections.emptyList());
        ClassifierOptionsResponse response = classifierOptionsRequestService.findClassifierOptions(request);
        assertResponse(response, ResponseStatus.RESULTS_NOT_FOUND);
    }

    @Test
    public void testDataNotFoundStatus() {
        ClassifierOptionsRequest request =
                TestHelperUtils.createClassifierOptionsRequest(EvaluationMethod.CROSS_VALIDATION);
        when(classifierOptionsService.findBestClassifierOptions(request)).thenThrow(
                new DataNotFoundException("Not found"));
        ClassifierOptionsResponse response = classifierOptionsRequestService.findClassifierOptions(request);
        assertResponse(response, ResponseStatus.DATA_NOT_FOUND);
    }

    @Test
    public void testSuccessStatus() {
        ClassifierOptionsRequest request =
                TestHelperUtils.createClassifierOptionsRequest(EvaluationMethod.CROSS_VALIDATION);
        List<ClassifierOptionsInfo> expected = Collections.singletonList(TestHelperUtils.buildClassifierOptionsInfo());
        when(classifierOptionsService.findBestClassifierOptions(request)).thenReturn(expected);
        ClassifierOptionsResponse response = classifierOptionsRequestService.findClassifierOptions(request);
        assertResponse(response, ResponseStatus.SUCCESS);
        Assertions.assertThat(response.getClassifierReports()).hasSameSizeAs(expected);
    }

    @Test
    public void testErrorStatus() {
        ClassifierOptionsRequest request =
                TestHelperUtils.createClassifierOptionsRequest(EvaluationMethod.CROSS_VALIDATION);
        when(classifierOptionsService.findBestClassifierOptions(request)).thenThrow(new IllegalStateException());
        ClassifierOptionsResponse response = classifierOptionsRequestService.findClassifierOptions(request);
        assertResponse(response, ResponseStatus.ERROR);
    }

    private void assertResponse(ClassifierOptionsResponse response, ResponseStatus expected) {
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getRequestId()).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(expected);
    }
}
