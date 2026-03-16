package com.troubleshooter.engine;

import com.troubleshooter.rules.ArchitectureRule;
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

                rule.check(classes);

            } catch (AssertionError e) {

                report.addViolation(e.getMessage());

            }
        }

        return report;
    }
}