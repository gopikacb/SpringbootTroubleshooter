package com.troubleshooter.rules;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.troubleshooter.model.Violation;

public class LayerArchitectureRule implements ArchitectureRule {

	@Override
	public List<Violation> evaluate(JavaClasses classes) {

		List<Violation> violations = new ArrayList<>();

		for (JavaClass sourceClass : classes) {

			String sourceLayer = detectLayer(sourceClass);

			if (sourceLayer == null) {
				continue;
			}

			Set<JavaClass> dependencies = sourceClass.getDirectDependenciesFromSelf().stream()
					.map(d -> d.getTargetClass()).filter(classes::contains)
					.collect(java.util.stream.Collectors.toSet());

			for (JavaClass targetClass : dependencies) {

				// Ignore self dependency
				if (sourceClass.equals(targetClass)) {
					continue;
				}

				String targetLayer = detectLayer(targetClass);

				if (targetLayer == null) {
					continue;
				}

				boolean violation = isViolation(sourceLayer, targetLayer);

				if (violation) {

					Violation v = new Violation();

					v.setRuleName("Layer Rule");

					v.setRuleType("LAYER");

					v.setClassName(sourceClass.getSimpleName());

					v.setFieldName(sourceLayer);

					v.setDependency(targetClass.getSimpleName() + " (" + targetLayer + ")");

					v.setMessage("Follow defined layer boundaries. Avoid accessing restricted layers directly.");

					violations.add(v);
				}
			}
		}

		return removeDuplicates(violations);
	}

	/**
	 * Detect layer using annotations first, then package fallback.
	 */
	private String detectLayer(JavaClass javaClass) {

		// -------------------------
		// Annotation based
		// -------------------------

		if (javaClass.isAnnotatedWith(Controller.class) || javaClass.isAnnotatedWith(RestController.class)) {

			return "Controller";
		}

		if (javaClass.isAnnotatedWith(Service.class)) {

			return "Service";
		}

		if (javaClass.isAnnotatedWith(Repository.class)) {

			return "Repository";
		}

		// -------------------------
		// Package fallback
		// -------------------------

		String packageName = javaClass.getPackageName().toLowerCase();

		if (packageName.contains(".controller")) {

			return "Controller";
		}

		if (packageName.contains(".service")) {

			return "Service";
		}

		if (packageName.contains(".repository")) {

			return "Repository";
		}

		if (packageName.contains(".dao")) {

			return "Repository";
		}

		return null;
	}

	/**
	 * Layer dependency rules
	 */
	private boolean isViolation(String sourceLayer, String targetLayer) {

		switch (sourceLayer) {

		case "Controller":

			return !targetLayer.equals("Service");

		case "Service":

			return targetLayer.equals("Controller");

		case "Repository":

			return targetLayer.equals("Controller") || targetLayer.equals("Service");

		default:
			return false;
		}
	}

	private List<Violation> removeDuplicates(List<Violation> violations) {

		List<Violation> unique = new ArrayList<>();

		List<String> seen = new ArrayList<>();

		for (Violation violation : violations) {

			String key = violation.getClassName() + "-" + violation.getDependency();

			if (!seen.contains(key)) {

				seen.add(key);
				unique.add(violation);
			}
		}

		return unique;
	}
}