package com.troubleshooter.report;

import java.util.ArrayList;
import java.util.List;

import com.troubleshooter.model.Violation;

public class ArchitectureReport {

	private List<Violation> violations = new ArrayList<>();

    public void addViolations(List<Violation> list) {
        this.violations.addAll(list);
    }

    public List<Violation> getViolations() {
        return violations;
    }

    public int getViolationCount() {
        return violations.size();
    }

//    public void printReport() {
//
//        System.out.println("\nArchitecture Health Report");
//        System.out.println("----------------------------");
//
//        if (violations.isEmpty()) {
//            System.out.println("No violations detected");
//        } else {
//
//            System.out.println("Total Violations: " + violations.size());
//
//            System.out.println("\nViolations:");
//
//            for (String v : violations) {
//                System.out.println(v + "\n");
//            }
//        }
//    }
}