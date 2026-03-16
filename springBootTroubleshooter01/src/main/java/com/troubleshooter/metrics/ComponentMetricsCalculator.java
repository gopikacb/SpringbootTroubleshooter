package com.troubleshooter.metrics;

import java.util.*;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaModifier;

public class ComponentMetricsCalculator {

    public List<ComponentMetrics> calculate(
            Map<String, Set<String>> dependencies,
            JavaClasses classes) {

        Map<String, ComponentMetrics> metricsMap = new HashMap<>();

        // Initialize components
        dependencies.keySet()
                .forEach(pkg -> metricsMap.put(pkg, new ComponentMetrics(pkg)));

        // -----------------------------
        // Calculate Ce (Efferent Coupling)
        // -----------------------------
        dependencies.forEach((pkg, deps) -> {

            ComponentMetrics metric = metricsMap.get(pkg);

            if (metric != null) {
                metric.setEfferentCoupling(deps.size());
            }

        });

        // -----------------------------
        // Calculate Ca (Afferent Coupling)
        // -----------------------------
        dependencies.forEach((pkg, deps) -> {

            for (String dep : deps) {

                ComponentMetrics metric =
                        metricsMap.computeIfAbsent(dep, ComponentMetrics::new);

                metric.setAfferentCoupling(
                        metric.getAfferentCoupling() + 1
                );
            }

        });

        // -----------------------------
        // Calculate Abstractness (A)
        // -----------------------------
        metricsMap.values().forEach(metric -> {

            double abstractness =
                    calculateAbstractness(classes, metric.getComponentName());

            metric.setAbstractness(abstractness);

        });

        // -----------------------------
        // Calculate Instability (I)
        // -----------------------------
        metricsMap.values().forEach(ComponentMetrics::calculateInstability);

        // -----------------------------
        // Calculate Distance (D)
        // -----------------------------
        metricsMap.values().forEach(ComponentMetrics::calculateDistance);

        return new ArrayList<>(metricsMap.values());
    }

    // -----------------------------------------
    // Abstractness Calculation
    // A = (#abstract classes + interfaces) / total classes
    // -----------------------------------------
    public double calculateAbstractness(JavaClasses classes, String packageName) {

        long abstractClasses = classes.stream()
                .filter(c -> c.getPackageName().startsWith(packageName))
                .filter(c ->
                        c.getModifiers().contains(JavaModifier.ABSTRACT)
                                || c.isInterface())
                .count();

        long totalClasses = classes.stream()
                .filter(c -> c.getPackageName().startsWith(packageName))
                .count();

        if (totalClasses == 0) {
            return 0;
        }

        return (double) abstractClasses / totalClasses;
    }
}