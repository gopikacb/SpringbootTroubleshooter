package com.troubleshooter.handler;


import com.troubleshooter.exception.MissingConfigurationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MissingConfigurationException.class)
    public Map<String, Object> handleMissingConfig(MissingConfigurationException ex) {
        return Map.of(
                "error", "Missing Configuration Properties",
                "missingProperties", ex.getMissingProperties()
        );
    }
}