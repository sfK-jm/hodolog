package com.sfk.hodolog.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Base64;

@Data
@ConfigurationProperties(prefix = "hodolog")
public class AppConfig {

    private byte[] jwtKey;

    public void setJwtKey(String jwtKey) {
        this.jwtKey = Base64.getDecoder().decode(jwtKey);
    }

}
