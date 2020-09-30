package com.ers.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Ers config.
 *
 * @author Roman Batygin
 */
@Data
@ConfigurationProperties("ers")
public class ErsConfig {

    /**
     * The best results size
     */
    private Integer resultSize;
}
