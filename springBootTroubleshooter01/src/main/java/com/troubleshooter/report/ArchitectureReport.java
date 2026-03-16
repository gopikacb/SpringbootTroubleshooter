package com.troubleshooter.report;

import java.util.ArrayList;
import java.util.List;

public class ArchitectureReport {

    private List<String> violations = new ArrayList<>();

    public void addViolation(String violation) {
        violations.add(violation);
    }

    public int getViolationCount() {
        return violations.size();
    }

    public void printReport() {

        System.out.println("\nArchitecture Health Report");
        System.out.println("----------------------------");

        if (violations.isEmpty()) {
            System.out.println("No violations detected");
        } else {

            System.out.println("Total Violations: " + violations.size());

            System.out.println("\nViolations:");

            for (String v : violations) {
                System.out.println(v);
            }
        }
    }
}