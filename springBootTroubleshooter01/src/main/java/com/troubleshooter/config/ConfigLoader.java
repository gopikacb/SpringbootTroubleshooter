package com.troubleshooter.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.InputStream;

public class ConfigLoader {

    public static ArchitectureConfig loadConfig() throws Exception {

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        InputStream is =
                ConfigLoader.class
                        .getClassLoader()
                        .getResourceAsStream("architecture-rules.yml");

        return mapper.readValue(is, ArchitectureConfig.class);
    }
}