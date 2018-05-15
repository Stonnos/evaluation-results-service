package com.ers.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Web - service config.
 *
 * @author Roman Batygin
 */
@Data
@ConfigurationProperties("webServiceConfig")
public class WebServiceConfig {

    /**
     * WSDL config
     */
    private WsdlConfig wsdlConfig;

    /**
     * XSD schema path
     */
    private String xsdSchema;

    /**
     * Wsdl config.
     */
    @Data
    public static class WsdlConfig {

        /**
         * Port type name
         */
        private String portTypeName;
        /**
         * Wsdl location uri
         */
        private String locationUri;
        /**
         * Target namespace
         */
        private String targetNamespace;

    }
}
