package com.troubleshooter.rules;

import com.tngtech.archunit.core.domain.JavaClasses;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.stereotype.Repository;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

public class AnnotationLayerRule implements ArchitectureRule {

    @Override
    public void check(JavaClasses classes) {

        noClasses()
                .that()
                .areAnnotatedWith(RestController.class)
                .should()
                .dependOnClassesThat()
                .areAnnotatedWith(Repository.class)
                .check(classes);
    }
}
