package com.troubleshooter.rules;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.EvaluationResult;
import com.troubleshooter.model.Violation;
import com.troubleshooter.utils.ArchUnitUtils;

import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

import java.util.Collections;
import java.util.List;

public class CycleDependencyRule implements ArchitectureRule {

//	public void check(JavaClasses classes) {
//
//	    slices()
//	        .matching("..service.(*)..")
//	        .namingSlices("Service slice: $1")
//	        .should()
//	        .beFreeOfCycles()
//	        .check(classes);
//	}
	
	@Override
	public List<Violation> evaluate(JavaClasses classes) {
		EvaluationResult result = null;
//		ArchRule rule = slices()
//		        .matching("..service.(*)..")
//		        .namingSlices("Service slice: $1")
//		        .should()
//		        .beFreeOfCycles();
		
		ArchRule rule = slices()
		        .matching("..service..")
		        .should()
		        .beFreeOfCycles();
		
		try {
		    result = rule.evaluate(classes);
		} catch (AssertionError e) {
		    if (e.getMessage().contains("failed to check any classes")) {
		        return Collections.emptyList();
		    }
		    throw e;
		}
		
//		EvaluationResult result = rule.evaluate(classes);

		return ArchUnitUtils.mapViolations(result, "Cyclic Dependency detected", "CYCLE");
	}
}