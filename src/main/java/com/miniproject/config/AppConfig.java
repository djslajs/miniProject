package com.miniproject.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Base64;

@Data
@ConfigurationProperties( prefix = "cho")
public class AppConfig {
    private byte[] KEY;

    public void setKEY(String KEY) {
        this.KEY = Base64.getDecoder().decode( KEY);
    }

    public byte[] getKEY() {
        return KEY;
    }
}
