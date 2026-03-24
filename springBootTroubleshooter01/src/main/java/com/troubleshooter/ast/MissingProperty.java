package com.troubleshooter.ast;

public class MissingProperty {

    private String className;
    private String fieldName;
    private int lineNumber;
    private String propertyKey;

    public MissingProperty(String className, String fieldName, int lineNumber, String propertyKey) {
        this.className = className;
        this.fieldName = fieldName;
        this.lineNumber = lineNumber;
        this.propertyKey = propertyKey;
    }

    public String getClassName() {
        return className;
    }

    public String getFieldName() {
        return fieldName;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public String getPropertyKey() {
        return propertyKey;
    }

    @Override
    public String toString() {
        return className + "." + fieldName +
                " (line " + lineNumber + ") -> " + propertyKey;
    }
}