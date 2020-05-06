package com.ers.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Web - service config.
 *
 * @author Roman Batygin
 */
@Data
@ConfigurationProperties("web-service-config")
public class WebServiceConfig {

    /**
     * XSD schema path
     */
    private String xsdSchema;
}
