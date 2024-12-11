package com.sfk.hodolog.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "hodolog")
public class AppConfig {

    public String hello;
}
