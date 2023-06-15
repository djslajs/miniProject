package com.miniproject.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

@Data
@ConfigurationProperties( prefix = "cho")
public class AppConfig {
//    public final static String KEY = "GNestrg5aea6Wyt+k31NyjOrpszmlVsnVOjwQ3wZNjs=";
    public List<String> hello;
    public String KEY;

//    public Map< String, String> mapTest;

    public MapTest mapTest;

    @Data
    private static class MapTest {
        public String name;
        public String resp;
    }
}
