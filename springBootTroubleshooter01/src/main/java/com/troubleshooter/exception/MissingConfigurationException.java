package com.troubleshooter.exception;

import java.util.List;

public class MissingConfigurationException extends RuntimeException {

    private final List<String> missingProperties;

    public MissingConfigurationException(List<String> missingProperties) {
        super("Missing configuration properties: " + missingProperties);
        this.missingProperties = missingProperties;
    }

    public List<String> getMissingProperties() {
        return missingProperties;
    }
}