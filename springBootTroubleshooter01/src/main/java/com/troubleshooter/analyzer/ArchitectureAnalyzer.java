package com.troubleshooter.analyzer;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;

import java.nio.file.Paths;

public class ArchitectureAnalyzer {

    public JavaClasses loadClassesFromPath(String path) {

        JavaClasses classes =
                new ClassFileImporter()
                        .importPath(Paths.get(path));

        System.out.println("Loaded classes: " + classes.size());

        return classes;
    }
}