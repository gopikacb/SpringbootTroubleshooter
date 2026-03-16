package com.troubleshooter.metrics;

import java.util.List;

public class ArchitectureHealthCalculator {

    public int calculateScore(List<ComponentMetrics> metrics) {

        if (metrics.isEmpty()) {
            return 100;
        }

        double totalDistance = 0;

        for (ComponentMetrics m : metrics) {
            totalDistance += m.getDistance();
        }

        double avgDistance = totalDistance / metrics.size();

        double score = (1 - avgDistance) * 100;

        return (int) Math.round(score);
    }
}
