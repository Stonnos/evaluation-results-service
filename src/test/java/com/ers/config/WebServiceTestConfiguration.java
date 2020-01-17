package com.ers.config;

import com.ers.controller.EvaluationResultsEndpoint;
import com.ers.dto.EvaluationResultsRequest;
import com.ers.service.ClassifierOptionsRequestService;
import com.ers.service.EvaluationResultsService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

/**
 * Web service test configuration.
 *
 * @author Roman Batygin
 */
@TestConfiguration
public class WebServiceTestConfiguration {

    @MockBean
    private EvaluationResultsService evaluationResultsService;
    @MockBean
    private ClassifierOptionsRequestService classifierOptionsRequestService;

    /**
     * Creates jaxb2 marshaller bean.
     *
     * @return jaxb2 marshaller bean
     */
    @Bean
    public Jaxb2Marshaller jaxb2Marshaller() {
        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setContextPath(EvaluationResultsRequest.class.getPackage().getName());
        return jaxb2Marshaller;
    }

    /**
     * Creates evaluation results endpoint bean.
     *
     * @return evaluation results endpoint bean
     */
    @Bean
    public EvaluationResultsEndpoint evaluationResultsEndpoint() {
        return new EvaluationResultsEndpoint(evaluationResultsService, classifierOptionsRequestService);
    }
}