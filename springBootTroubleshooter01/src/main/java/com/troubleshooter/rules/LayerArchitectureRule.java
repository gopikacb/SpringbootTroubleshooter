package com.troubleshooter.rules;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.EvaluationResult;
import com.troubleshooter.model.Violation;
import com.troubleshooter.utils.ArchUnitUtils;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

//import java.util.ArrayList;
import java.util.List;

public class LayerArchitectureRule implements ArchitectureRule {

//	public void check(JavaClasses classes) {
//
//		var rule = layeredArchitecture().consideringOnlyDependenciesInLayers()
//
//				.layer("Controller").definedBy("..controller..").layer("Service").definedBy("..service..")
//				.layer("Repository").definedBy("..repository..")
//
//				.whereLayer("Controller").mayOnlyAccessLayers("Service").whereLayer("Service")
//				.mayOnlyAccessLayers("Repository").whereLayer("Repository").mayNotAccessAnyLayer();
//
////        EvaluationResult result = rule.evaluate(classes);
//
////        printReadable("Controller -> Repository violation", result);
//
//		runRule("Layered Architecture violation", "LAYER", rule, classes);
//	}

	@Override
	public List<Violation> evaluate(JavaClasses classes) {

		ArchRule rule = layeredArchitecture().consideringOnlyDependenciesInLayers().layer("Controller")
				.definedBy("..controller..").layer("Service").definedBy("..service..").layer("Repository")
				.definedBy("..repository..").whereLayer("Controller").mayOnlyAccessLayers("Service")
				.whereLayer("Service").mayOnlyAccessLayers("Repository").whereLayer("Repository")
				.mayNotAccessAnyLayer();

		EvaluationResult result = rule.evaluate(classes);

		return ArchUnitUtils.mapViolations(result, "Layer Rule", "LAYER");
	}

	

//	private void printReadable(String title, String ruleType, EvaluationResult result) {
//
//		if (!result.hasViolation()) {
//			System.out.println("[PASS] " + title);
//			return;
//		}
//
//		System.out.println("\n[FAIL] " + title);
//		System.out.println("--------------------------------------------------");
//
//		for (String detail : result.getFailureReport().getDetails()) {
//
//			String source = extractBetween(detail, "Field <", ">");
//			String target = extractBetween(detail, "has type <", ">");
//
//			if (source == null || target == null) {
//				System.out.println("Raw: " + detail);
//				continue;
//			}
//
//			String className = source.substring(0, source.lastIndexOf("."));
//			String fieldName = source.substring(source.lastIndexOf(".") + 1);
//
//			String simpleClass = ArchUnitUtils.getSimpleName(className);
//			String simpleTarget = ArchUnitUtils.getSimpleName(target);
//
//			System.out.println("Class:              " + simpleClass);
//			System.out.println("Field:              " + fieldName);
//			System.out.println("Illegal Dependency: " + simpleTarget);
//			System.out.println();
//		}
//
//		System.out.println("Suggested Fix:");
//		System.out.println("  " + getSuggestion(ruleType));
//		System.out.println("--------------------------------------------------\n");
//	}

//	private void runRule(String title, String ruleType, ArchRule rule, JavaClasses classes) {
//		EvaluationResult result = rule.evaluate(classes);
//		printReadable(title, ruleType, result);
//	}

	
}