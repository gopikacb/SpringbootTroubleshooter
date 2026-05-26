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
		int score = 100;

		// softer penalty
		score -= (int) (avgDistance * 30);

		// healthy floor
		if (score < 50) {
			score = 50;
		}

		return score;
	}
}