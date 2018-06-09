package com.ers.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Application config.
 *
 * @author Roman Batygin
 */
@Data
@ConfigurationProperties("serviceConfig")
public class ServiceConfig {

    /**
     * Training data storage path
     */
    private String dataStoragePath;

    /**
     * Training data file format
     */
    private String fileFormat;

    /**
     * The best results size
     */
    private Integer resultSize;
}
