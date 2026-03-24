package com.troubleshooter.metrics;

import java.util.List;

public class AnalysisIssue {

    private String componentName;
    private String issueType;
    private String severity;
    private double instability;
    private double abstractness;
    private double distance;
    private String description;
    private String impact;
    private List<String> recommendations;

    // Constructor
    public AnalysisIssue(String componentName, String issueType, String severity,
                         double instability, double abstractness, double distance,
                         String description, String impact, List<String> recommendations) {
        this.componentName = componentName;
        this.issueType = issueType;
        this.severity = severity;
        this.instability = instability;
        this.abstractness = abstractness;
        this.distance = distance;
        this.description = description;
        this.impact = impact;
        this.recommendations = recommendations;
    }

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	public String getIssueType() {
		return issueType;
	}

	public void setIssueType(String issueType) {
		this.issueType = issueType;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public double getInstability() {
		return instability;
	}

	public void setInstability(double instability) {
		this.instability = instability;
	}

	public double getAbstractness() {
		return abstractness;
	}

	public void setAbstractness(double abstractness) {
		this.abstractness = abstractness;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImpact() {
		return impact;
	}

	public void setImpact(String impact) {
		this.impact = impact;
	}

	public List<String> getRecommendations() {
		return recommendations;
	}

	public void setRecommendations(List<String> recommendations) {
		this.recommendations = recommendations;
	}

    
}
