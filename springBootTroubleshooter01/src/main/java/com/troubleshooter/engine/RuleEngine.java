package com.troubleshooter.engine;

import com.troubleshooter.rules.ArchitectureRule;
import com.troubleshooter.model.Violation;
import com.troubleshooter.report.ArchitectureReport;
import com.tngtech.archunit.core.domain.JavaClasses;

import java.util.List;

public class RuleEngine {

    private List<ArchitectureRule> rules;

    public RuleEngine(List<ArchitectureRule> rules) {
        this.rules = rules;
    }

    
    public ArchitectureReport execute(JavaClasses classes) {

        ArchitectureReport report = new ArchitectureReport();

        for (ArchitectureRule rule : rules) {

            try {
                List<Violation> violations = rule.evaluate(classes);

                if (violations.isEmpty()) {
                    System.out.println("[PASS] " + rule.getClass().getSimpleName());
                    System.out.println("-----------------------------------");

                } else {
                    System.out.println("[FAIL] " + rule.getClass().getSimpleName());

                    for (Violation v : violations) {
                        System.out.println("Class:      " + v.getClassName());
                        System.out.println("Field:      " + v.getFieldName());
                        System.out.println("Dependency: " + v.getDependency());
                        System.out.println("Fix:        " + v.getMessage());
                        System.out.println("-----------------------------------");
                    }
                }

                report.addViolations(violations);

            } catch (Exception e) {
                System.out.println("[ERROR] Rule execution failed: " + rule.getClass().getSimpleName());
                System.out.println("Reason: " + e.getMessage());
            }
        }

        return report;
    }
}