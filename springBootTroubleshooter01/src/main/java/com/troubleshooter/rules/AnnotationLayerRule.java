package com.troubleshooter.rules;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.EvaluationResult;
import com.troubleshooter.model.Violation;
import com.troubleshooter.utils.ArchUnitUtils;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.stereotype.Repository;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import java.util.List;

public class AnnotationLayerRule implements ArchitectureRule {

    @Override
    public List<Violation> evaluate(JavaClasses classes) {
		ArchRule rule = noClasses()
                .that()
                .areAnnotatedWith(RestController.class)
                .should()
                .dependOnClassesThat()
                .areAnnotatedWith(Repository.class);
		
		EvaluationResult result = rule.evaluate(classes);

		return ArchUnitUtils.mapViolations(result, "@RestController -> @Repository violation", "SPRING");
    }
}
