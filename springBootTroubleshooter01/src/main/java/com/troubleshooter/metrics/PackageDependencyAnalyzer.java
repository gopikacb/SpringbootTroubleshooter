package com.troubleshooter.metrics;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.tngtech.archunit.core.domain.Dependency;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.troubleshooter.utils.RootPackageDetector;

public class PackageDependencyAnalyzer {

	public Map<String, Set<String>> analyze(JavaClasses classes) {
		System.out.println("Loaded classes: " + classes.size());
		String rootPackage = RootPackageDetector.findRootPackage(classes);
		System.out.println("Detected Root Package: " + rootPackage);
		Map<String, Set<String>> dependencies = new HashMap<>();
		for (JavaClass javaClass : classes) {
			String sourcePackage = extractComponent(javaClass.getPackageName(), rootPackage);
			
			// Ignore root package
			if (sourcePackage == null) {
				continue;
			}
			
			// Ignore external packages
			if (!sourcePackage.startsWith(rootPackage)) {
				continue;
			}
			
			dependencies.putIfAbsent(sourcePackage, new HashSet<>());
			Set<Dependency> directDependencies = javaClass.getDirectDependenciesFromSelf();
			
			for (Dependency dependency : directDependencies) {
				JavaClass targetClass = dependency.getTargetClass();
				
				if (targetClass == null) {
					continue;
				}
				
				String targetPackage = extractComponent(targetClass.getPackageName(), rootPackage);
				// Ignore root package
				if (targetPackage == null) {
					continue;
				}
				// Ignore external packages
				if (!targetPackage.startsWith(rootPackage)) {
					continue;
				}
				// Avoid self-dependency
				if (!sourcePackage.equals(targetPackage)) {
					dependencies.get(sourcePackage).add(targetPackage);
				}
			}
		}
		return dependencies;
	}

	private String extractComponent(String packageName, String rootPackage) {
		if (!packageName.startsWith(rootPackage)) {
			return packageName;
		}
		String remaining = packageName.substring(rootPackage.length());
		if (remaining.startsWith(".")) {
			remaining = remaining.substring(1);
		}
		String[] parts = remaining.split("\\.");
		// root package itself
		if (parts.length == 0 || parts[0].isBlank()) {
			// Ignore root package
			return null;
		}
		// first child package
		return rootPackage + "." + parts[0];
	}
}