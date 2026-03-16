package com.troubleshooter.rules;

import com.tngtech.archunit.core.domain.JavaClasses;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

public class CycleDependencyRule implements ArchitectureRule {

    @Override
    public void check(JavaClasses classes) {

        slices()
                .matching("..service.(*)..")
                .should()
                .beFreeOfCycles()
                .allowEmptyShould(true)
                .check(classes);

    }
}