package com.troubleshooter.ast;

import java.util.*;

public class ASTResult {

    public String className;

    public String layer;

    public boolean fieldInjection = false;

    public boolean constructorInjection = false;

    public Map<String,Integer> methodComplexity = new HashMap<>();

    public List<String> annotations = new ArrayList<>();

    public List<String> codeSmells = new ArrayList<>();
}