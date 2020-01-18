package com.ers.config;

import com.ers.dto.EvaluationResultsRequest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

/**
 * Web service test configuration.
 *
 * @author Roman Batygin
 */
@TestConfiguration
public class WebServiceTestConfiguration {

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
}