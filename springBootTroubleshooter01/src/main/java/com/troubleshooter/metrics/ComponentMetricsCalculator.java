package com.troubleshooter.metrics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaModifier;

public class ComponentMetricsCalculator {
	
	public int minimumPackageSize = 1;

	public List<ComponentMetrics> calculate(Map<String, Set<String>> dependencies, JavaClasses classes) {

		Map<String, ComponentMetrics> metricsMap = new HashMap<>();

		// Initialize components
		// Ignore unwanted packages
		dependencies.keySet().stream().filter(pkg -> !shouldIgnorePackage(pkg))
				.filter(pkg -> hasMinimumClasses(pkg, classes))
				.forEach(pkg -> metricsMap.put(pkg, new ComponentMetrics(pkg)));


		// Calculate Ce (Efferent Coupling)
		dependencies.forEach((pkg, deps) -> {
			// Ignore unwanted packages
			if (shouldIgnorePackage(pkg) || !hasMinimumClasses(pkg, classes)) {
				return;
			}

			ComponentMetrics metric = metricsMap.get(pkg);

			if (metric != null) {
				long validDependencies = deps.stream().filter(dep -> !shouldIgnorePackage(dep)).count();
				metric.setEfferentCoupling((int) validDependencies);
			}
		});


		// Calculate Ca (Afferent Coupling)
		dependencies.forEach((pkg, deps) -> {
			if (shouldIgnorePackage(pkg) || !hasMinimumClasses(pkg, classes)) {
				return;
			}

			for (String dep : deps) {
				// Ignore irrelevant packages
				if (shouldIgnorePackage(dep) || !hasMinimumClasses(dep, classes)) {
					continue;
				}

				ComponentMetrics metric = metricsMap.computeIfAbsent(dep, ComponentMetrics::new);
				metric.setAfferentCoupling(metric.getAfferentCoupling() + 1);
			}
		});


		// Calculate Abstractness (A)
		metricsMap.values().forEach(metric -> {
			double abstractness = calculateAbstractness(classes, metric.getComponentName());
			metric.setAbstractness(abstractness);
		});


		// Calculate Instability (I)
		metricsMap.values().forEach(ComponentMetrics::calculateInstability);

		// Calculate Distance (D)
		metricsMap.values().forEach(ComponentMetrics::calculateDistance);
		return new ArrayList<>(metricsMap.values());
	}

	// Ignore framework/support packages
	private boolean shouldIgnorePackage(String packageName) {
		String lower = packageName.toLowerCase();

		return lower.contains(".config") || lower.contains(".dto") || lower.contains(".model")
				|| lower.contains(".entity") || lower.contains(".exception") || lower.contains(".constant")
				|| lower.contains(".constants") || lower.contains(".util") || lower.contains(".utils")
				|| lower.contains(".handler");
	}

	// Ignore tiny packages
	// Metrics unreliable for small packages
	private boolean hasMinimumClasses(String packageName, JavaClasses classes) {
		long classCount = classes.stream().filter(c -> c.getPackageName().startsWith(packageName)).count();
		return classCount >= minimumPackageSize;
	}

	// Abstractness Calculation
	// A = abstract classes / total classes
	public double calculateAbstractness(JavaClasses classes, String packageName) {
		long abstractClasses = classes.stream().filter(c -> c.getPackageName().startsWith(packageName))
				.filter(c -> c.getModifiers().contains(JavaModifier.ABSTRACT) || c.isInterface()).count();
		long totalClasses = classes.stream().filter(c -> c.getPackageName().startsWith(packageName)).count();
		if (totalClasses == 0) {
			return 0;
		}
		return (double) abstractClasses / totalClasses;
	}
}