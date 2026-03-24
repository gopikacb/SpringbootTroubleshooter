package com.troubleshooter.metrics;

import java.util.ArrayList;
import java.util.List;

public class ArchitectureCommentGenerator {

    public List<AnalysisIssue> analyze(List<ComponentMetrics> metrics) {

        List<AnalysisIssue> issues = new ArrayList<>();

        for (ComponentMetrics m : metrics) {

            String name = shortName(m.getComponentName());
            double I = m.getInstability();
            double A = m.getAbstractness();
            double D = m.getDistance();

            /* Zone of Pain */
            if (I < 0.3 && A < 0.3) {

                issues.add(buildIssue(
                        name,
                        "Zone of Pain",
                        "HIGH",
                        I, A, D,
                        "Component is stable but overly concrete.",
                        "Changes are difficult because many components depend on it.",
                        List.of(
                                "Introduce interfaces or abstract classes",
                                "Apply Dependency Inversion Principle",
                                "Refactor reusable logic into abstractions",
                                "Avoid hard-coded implementations"
                        )
                ));
            }

            /* Zone of Uselessness */
            if (I > 0.7 && A > 0.7) {

                issues.add(buildIssue(
                        name,
                        "Zone of Uselessness",
                        "MEDIUM",
                        I, A, D,
                        "Component is abstract but unstable.",
                        "Abstractions are not effectively utilized.",
                        List.of(
                                "Remove unnecessary abstractions",
                                "Ensure abstractions are actually used",
                                "Stabilize dependencies",
                                "Consolidate redundant interfaces"
                        )
                ));
            }

            /* High Instability */
            if (I > 0.8) {

                issues.add(buildIssue(
                        name,
                        "High Instability",
                        "MEDIUM",
                        I, A, D,
                        "Too many outgoing dependencies.",
                        "Component is prone to break due to dependency changes.",
                        List.of(
                                "Reduce coupling",
                                "Apply Dependency Injection",
                                "Extract independent modules",
                                "Review necessity of each dependency"
                        )
                ));
            }

            /* Far from Main Sequence */
            if (D > 0.6) {

                issues.add(buildIssue(
                        name,
                        "Far from Main Sequence",
                        "HIGH",
                        I, A, D,
                        "Imbalance between abstractness and instability.",
                        "Component is either too rigid or too volatile.",
                        List.of(
                                "Balance Abstractness and Instability (A + I ≈ 1)",
                                "Introduce abstractions if too concrete",
                                "Simplify design if too abstract",
                                "Re-evaluate component responsibilities"
                        )
                ));
            }
        }

        return issues;
    }

    private AnalysisIssue buildIssue(String component,
                                     String issueType,
                                     String severity,
                                     double instability,
                                     double abstractness,
                                     double distance,
                                     String description,
                                     String impact,
                                     List<String> recommendations) {

        return new AnalysisIssue(
                component,
                issueType,
                severity,
                instability,
                abstractness,
                distance,
                description,
                impact,
                recommendations
        );
    }

    private String shortName(String name) {

        if (name.contains("."))
            return name.substring(name.lastIndexOf('.') + 1);

        return name;
    }
}