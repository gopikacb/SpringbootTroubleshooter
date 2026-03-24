package com.troubleshooter.ast;

public class ValueMisuse {

    private String className;
    private String fieldName;
    private int lineNumber;

    public ValueMisuse(String className, String fieldName, int lineNumber) {
        this.className = className;
        this.fieldName = fieldName;
        this.lineNumber = lineNumber;
    }

    public String getClassName() { return className; }
    public String getFieldName() { return fieldName; }
    public int getLineNumber() { return lineNumber; }
}