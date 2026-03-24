package com.troubleshooter.report;

import java.util.List;

import com.troubleshooter.metrics.AnalysisIssue;

public class ArchitectureReportPrinter {

    public void print(List<AnalysisIssue> issues, int score) {

        System.out.println("\nArchitecture Analysis");
        System.out.println("==================================================");

        for (AnalysisIssue issue : issues) {

            System.out.println("\n--------------------------------------------------");
            System.out.println("Component : " + issue.getComponentName());
            System.out.println("Issue     : " + issue.getIssueType());
            System.out.println("Severity  : " + issue.getSeverity());

            System.out.println(String.format(
                    "Metrics   : Instability=%.2f | Abstractness=%.2f | Distance=%.2f",
                    issue.getInstability(),
                    issue.getAbstractness(),
                    issue.getDistance()));

            System.out.println("Problem   : " + issue.getDescription());
            System.out.println("Impact    : " + issue.getImpact());

            System.out.println("Recommendations:");
            issue.getRecommendations().forEach(r -> System.out.println("  - " + r));
        }

        System.out.println("\nOverall Score: " + score);
    }
}
