package com.troubleshooter.rules;

import com.tngtech.archunit.core.domain.Dependency;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.EvaluationResult;
import com.troubleshooter.model.Violation;

import java.util.*;

import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

public class CycleDependencyRule implements ArchitectureRule {

	@Override
	public List<Violation> evaluate(JavaClasses classes) {

		List<Violation> finalViolations = new ArrayList<>();

		List<Violation> packageViolations = detectPackageCycles(classes);

		List<Violation> classViolations = detectClassCycles(classes);

		/*
		 * Suppress package-level duplicates if class-level cycles exist
		 */
		if (classViolations.isEmpty()) {

			finalViolations.addAll(packageViolations);
		}

		finalViolations.addAll(classViolations);

		return removeDuplicateViolations(finalViolations);
	}

	/*
	 * PACKAGE LEVEL CYCLES
	 */
	private List<Violation> detectPackageCycles(JavaClasses classes) {

		try {

			ArchRule rule = slices().matching("..(*)..").namingSlices("Package: $1").should().beFreeOfCycles();

			EvaluationResult result = rule.evaluate(classes);

			List<Violation> violations = new ArrayList<>();

			if (result.hasViolation()) {

				Violation violation = new Violation();

				violation.setClassName("PACKAGE_CYCLE");

				violation.setFieldName("Cyclic Package Dependency");

				violation.setDependency(extractPackageCycle(result));

				violation.setMessage("Refactor package dependencies to remove cyclic references.");

				violations.add(violation);
			}

			return violations;

		} catch (AssertionError e) {

			if (e.getMessage() != null && e.getMessage().contains("failed to check any classes")) {

				return Collections.emptyList();
			}

			throw e;
		}
	}

	/*
	 * CLASS LEVEL CYCLES
	 */
	private List<Violation> detectClassCycles(JavaClasses classes) {

		List<Violation> violations = new ArrayList<>();

		Map<JavaClass, Set<JavaClass>> graph = new HashMap<>();

		/*
		 * Build graph
		 */
		for (JavaClass javaClass : classes) {

			// Ignore test classes
			String className = javaClass.getSimpleName();

			if (className.endsWith("Test") || className.endsWith("Tests")) {

				continue;
			}

			Set<JavaClass> dependencies = new HashSet<>();

			for (Dependency dependency : javaClass.getDirectDependenciesFromSelf()) {

				JavaClass target = dependency.getTargetClass();

				/*
				 * Internal classes only Ignore self-cycle
				 */
				if (classes.contains(target) && !javaClass.equals(target)) {

					dependencies.add(target);
				}
			}

			graph.put(javaClass, dependencies);
		}

		Set<JavaClass> visited = new HashSet<>();

		Set<JavaClass> recursionStack = new HashSet<>();

		for (JavaClass javaClass : graph.keySet()) {

			detectCycleDFS(javaClass, graph, visited, recursionStack, new ArrayList<>(), violations);
		}

		return violations;
	}

	/*
	 * DFS cycle detection
	 */
	private void detectCycleDFS(JavaClass current, Map<JavaClass, Set<JavaClass>> graph, Set<JavaClass> visited,
			Set<JavaClass> recursionStack, List<JavaClass> path, List<Violation> violations) {

		if (recursionStack.contains(current)) {

			int cycleStart = path.indexOf(current);

			if (cycleStart != -1) {

				List<JavaClass> cycle = path.subList(cycleStart, path.size());

				// Ignore fake cycles
				if (cycle.size() < 2) {
					return;
				}

				StringBuilder cyclePath = new StringBuilder();

				for (JavaClass cls : cycle) {

					cyclePath.append(cls.getSimpleName()).append(" --̥> ");
				}

				cyclePath.append(current.getSimpleName());

				Violation violation = new Violation();

				violation.setClassName(current.getSimpleName());

				violation.setFieldName("CLASS_CYCLE");

				violation.setDependency(cyclePath.toString());

				violation
						.setMessage("Break circular dependency using redesign, events, interfaces, or lazy injection.");

				violations.add(violation);
			}

			return;
		}

		if (visited.contains(current)) {

			return;
		}

		visited.add(current);
		recursionStack.add(current);
		path.add(current);

		for (JavaClass dependency : graph.getOrDefault(current, Collections.emptySet())) {

			detectCycleDFS(dependency, graph, visited, recursionStack, path, violations);
		}

		recursionStack.remove(current);

		path.remove(path.size() - 1);
	}

	private String extractPackageCycle(EvaluationResult result) {

		return result.getFailureReport().toString().replace("\n", " ").replaceAll("\\s+", " ").trim();
	}

	private List<Violation> removeDuplicateViolations(List<Violation> violations) {

		Map<String, Violation> unique = new LinkedHashMap<>();

		for (Violation violation : violations) {

			unique.putIfAbsent(violation.getDependency(), violation);
		}

		return new ArrayList<>(unique.values());
	}
}