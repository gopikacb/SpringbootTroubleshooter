package com.troubleshooter.config;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

public class PropertyLoader {

    public static Set<String> loadProperties(String projectPath) {

        Set<String> properties = new HashSet<>();

        try {

            // properties file
            File propFile = new File(projectPath + "/application.properties");

            if (propFile.exists()) {
                Properties props = new Properties();
                props.load(new FileInputStream(propFile));
                properties.addAll(props.stringPropertyNames());
            }

            // yaml file
            File yamlFile = new File(projectPath + "/application.yml");

            if (yamlFile.exists()) {
                Yaml yaml = new Yaml();
                Map<String, Object> map = yaml.load(new FileInputStream(yamlFile));
                flatten("", map, properties);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return properties;
    }

    private static void flatten(String prefix,
                                Map<String, Object> map,
                                Set<String> result) {

        for (Map.Entry<String, Object> entry : map.entrySet()) {

            String key = prefix.isEmpty()
                    ? entry.getKey()
                    : prefix + "." + entry.getKey();

            if (entry.getValue() instanceof Map) {
                flatten(key, (Map<String, Object>) entry.getValue(), result);
            } else {
                result.add(key);
            }
        }
    }
}