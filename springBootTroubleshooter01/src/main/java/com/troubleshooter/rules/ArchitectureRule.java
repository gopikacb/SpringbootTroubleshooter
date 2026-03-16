package com.troubleshooter.rules;

import com.tngtech.archunit.core.domain.JavaClasses;

public interface ArchitectureRule {

    void check(JavaClasses classes);

}