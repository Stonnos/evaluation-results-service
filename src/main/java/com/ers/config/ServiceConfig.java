package com.ers.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Application config.
 *
 * @author Roman Batygin
 */
@Data
@ConfigurationProperties("service-config")
public class ServiceConfig {

    /**
     * The best results size
     */
    private Integer resultSize;
}
