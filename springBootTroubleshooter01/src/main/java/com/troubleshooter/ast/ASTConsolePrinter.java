package com.troubleshooter.ast;

import java.util.*;

public class ASTConsolePrinter {

    public void printResults(List<ASTResult> results) {

        System.out.println("\nAST Analysis");
        System.out.println("-----------------------------------");

        for(ASTResult r : results){

            System.out.println("Class: " + r.className);

            if(r.fieldInjection)
                System.out.println("  Field Injection detected");

            if(r.constructorInjection)
                System.out.println("  Constructor Injection used");

            r.methodComplexity.forEach((method, complexity)->{

                System.out.println("  Method: " + method +
                        " | Complexity: " + complexity);
            });

            System.out.println();
        }
    }
}