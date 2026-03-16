package com.troubleshooter.cli;

import com.troubleshooter.analyzer.ArchitectureAnalyzer;
import com.troubleshooter.engine.RuleEngine;
import com.troubleshooter.rules.AnnotationLayerRule;
import com.troubleshooter.rules.ControllerRepositoryRule;
import com.troubleshooter.rules.LayerArchitectureRule;
import com.troubleshooter.rules.CycleDependencyRule;
import com.troubleshooter.report.ArchitectureReport;
import com.troubleshooter.metrics.ArchitectureCommentGenerator;
import com.troubleshooter.metrics.ArchitectureHealthCalculator;
import com.troubleshooter.metrics.ArchitectureMetrics;
import com.troubleshooter.metrics.ComponentMetrics;
import com.troubleshooter.metrics.ComponentMetricsCalculator;
import com.troubleshooter.metrics.MetricsGraphPrinter;
import com.troubleshooter.metrics.PackageDependencyAnalyzer;
import com.tngtech.archunit.core.domain.JavaClasses;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class ArchitectureScannerCLI {

    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("Usage: java -jar tool.jar <base-package>");
            return;
        }

//        String basePackage = args[0];

        ArchitectureAnalyzer analyzer = new ArchitectureAnalyzer();

        JavaClasses classes = analyzer.loadClassesFromPath(args[0]);
        
        RuleEngine engine = new RuleEngine(
        		Arrays.asList(
        		        new ControllerRepositoryRule(),
        		        new LayerArchitectureRule(),
        		        new CycleDependencyRule(),
        		        new AnnotationLayerRule()
        		)
        );

        ArchitectureReport report = engine.execute(classes);

        report.printReport();

        int score = ArchitectureMetrics.calculateScore(report.getViolationCount());

        System.out.println("\nArchitecture Score: " + score + "/100");
        
        
        PackageDependencyAnalyzer packageDependencyAnalyzer = new PackageDependencyAnalyzer();

        Map<String, Set<String>> dependencies =
        		packageDependencyAnalyzer.analyze(classes);

        ComponentMetricsCalculator calculator =
                new ComponentMetricsCalculator();

        List<ComponentMetrics> metrics =
                calculator.calculate(dependencies, classes);
        
        System.out.println("\n------------------------------------------------------------------------------------------");

        
        System.out.println("\nComponent Metrics (Robert C Martin)");
        System.out.println("-------------------------------------------------------------");
        System.out.printf("%-35s %-4s %-4s %-8s %-8s %-8s\n",
                "Component", "Ca", "Ce", "Instability", "Abstract", "Distance");

        for (ComponentMetrics m : metrics) {

            System.out.printf("%-35s %-4d %-4d %-8.2f %-8.2f %-8.2f\n",
                    m.getComponentName(),
                    m.getAfferentCoupling(),
                    m.getEfferentCoupling(),
                    m.getInstability(),
                    m.getAbstractness(),
                    m.getDistance());
        }
        
        
        /* ---------- New Architecture Analysis ---------- */
        
        System.out.println("\n------------------------------------------------------------------------------------------");


        ArchitectureHealthCalculator healthCalculator =
                new ArchitectureHealthCalculator();

        int healthScore = healthCalculator.calculateScore(metrics);

        MetricsGraphPrinter graphPrinter =
                new MetricsGraphPrinter();

        graphPrinter.printGraph(metrics);

        ArchitectureCommentGenerator commentGenerator =
                new ArchitectureCommentGenerator();

        commentGenerator.generateComments(metrics, healthScore);

    }
}