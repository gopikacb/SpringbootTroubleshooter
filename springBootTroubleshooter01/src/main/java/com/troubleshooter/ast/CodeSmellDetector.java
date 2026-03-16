package com.troubleshooter.ast;

import java.util.*;

public class CodeSmellDetector {

    public List<String> detectSmells(ASTResult result) {

        List<String> smells = new ArrayList<>();

        if(result.fieldInjection)
            smells.add("Field Injection detected");

        result.methodComplexity.forEach((method, complexity) -> {

            if(complexity > 10)
                smells.add("High complexity method: " + method);
        });

        return smells;
    }
}