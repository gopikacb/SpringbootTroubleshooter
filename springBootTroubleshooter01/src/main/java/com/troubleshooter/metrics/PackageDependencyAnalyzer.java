package com.troubleshooter.metrics;

import java.util.*;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.Dependency;

public class PackageDependencyAnalyzer {

//    private static final String BASE_PACKAGE = "com.sbt.sbtTest";


    public Map<String, Set<String>> analyze(JavaClasses classes) {
    	
    	String BASE_PACKAGE = classes.stream()
                .findFirst()
                .get()
                .getPackageName()
                .split("\\.")[0];

        Map<String, Set<String>> dependencies = new HashMap<>();

        for (JavaClass javaClass : classes) {

            String sourcePackage = javaClass.getPackageName();

            // Ignore framework classes
            if (!sourcePackage.startsWith(BASE_PACKAGE)) {
                continue;
            }

            dependencies.putIfAbsent(sourcePackage, new HashSet<>());

            Set<Dependency> directDependencies =
                    javaClass.getDirectDependenciesFromSelf();

            for (Dependency dependency : directDependencies) {

                JavaClass targetClass = dependency.getTargetClass();

                if (targetClass == null) {
                    continue;
                }

                String targetPackage = targetClass.getPackageName();

                // Only include dependencies within our application
                if (!targetPackage.startsWith(BASE_PACKAGE)) {
                    continue;
                }

                if (!sourcePackage.equals(targetPackage)) {

                    dependencies
                            .get(sourcePackage)
                            .add(targetPackage);

                }
            }
        }

        return dependencies;
    }
}