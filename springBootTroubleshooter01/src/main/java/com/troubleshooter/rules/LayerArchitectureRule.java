package com.troubleshooter.rules;

import com.tngtech.archunit.core.domain.JavaClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

public class LayerArchitectureRule implements ArchitectureRule {

    @Override
    public void check(JavaClasses classes) {

        layeredArchitecture()
            .consideringOnlyDependenciesInLayers()

            .layer("Controller").definedBy("..controller..")
            .layer("Service").definedBy("..service..")
            .layer("Repository").definedBy("..repository..")

            .whereLayer("Controller").mayOnlyAccessLayers("Service")
            .whereLayer("Service").mayOnlyAccessLayers("Repository")
            .whereLayer("Repository").mayNotAccessAnyLayer()

            .check(classes);
    }
}