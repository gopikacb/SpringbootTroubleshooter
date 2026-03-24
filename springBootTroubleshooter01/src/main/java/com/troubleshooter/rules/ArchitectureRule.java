package com.troubleshooter.rules;

import java.util.List;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.troubleshooter.model.Violation;

public interface ArchitectureRule {

    List<Violation> evaluate(JavaClasses classes);

}