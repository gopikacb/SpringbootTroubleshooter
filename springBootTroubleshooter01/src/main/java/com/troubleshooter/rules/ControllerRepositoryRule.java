package com.troubleshooter.rules;

import com.tngtech.archunit.core.domain.JavaClasses;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

public class ControllerRepositoryRule implements ArchitectureRule {

    
    public void check(JavaClasses classes) {

        noClasses()
            .that()
            .resideInAPackage("..controller..")
            .should()
            .dependOnClassesThat()
            .resideInAPackage("..repository..")
            .check(classes);
    }

}