package com.troubleshooter.model;

public class Violation {

    private String ruleName;
    private String ruleType;
    private String className;
    private String fieldName;
    private String dependency;
    private String message;

    public Violation(String ruleName, String ruleType,
                     String className, String fieldName,
                     String dependency, String message) {
        this.ruleName = ruleName;
        this.ruleType = ruleType;
        this.className = className;
        this.fieldName = fieldName;
        this.dependency = dependency;
        this.message = message;
    }

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public String getRuleType() {
		return ruleType;
	}

	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getDependency() {
		return dependency;
	}

	public void setDependency(String dependency) {
		this.dependency = dependency;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


}