package com.troubleshooter.ast;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.stmt.*;

import java.io.File;
import java.nio.file.Files;
import java.util.*;

public class ASTAnalyzer {

	public List<ASTResult> analyzeSource(String sourcePath, Set<String> availableProperties) throws Exception {

		List<ASTResult> results = new ArrayList<>();

		File sourceDir = new File(sourcePath);

		System.out.println("Scanning source directory: " + sourcePath);

		Files.walk(sourceDir.toPath()).filter(path -> path.toString().endsWith(".java")).forEach(path -> {

			try {

				CompilationUnit cu = StaticJavaParser.parse(path);

				cu.findAll(ClassOrInterfaceDeclaration.class)
						.forEach(clazz -> results.add(analyzeClass(clazz, availableProperties)));

			} catch (Exception e) {

				System.out.println("Error parsing file: " + path);
				e.printStackTrace();
			}

		});

		System.out.println("AST Classes analyzed: " + results.size());

		return results;
	}

	private ASTResult analyzeClass(ClassOrInterfaceDeclaration clazz, Set<String> availableProperties) {

		ASTResult result = new ASTResult();

		result.className = clazz.getNameAsString();

		detectLayer(clazz, result);

		boolean isSpringComponent = result.layer != null;

		if (isSpringComponent) {
			detectInjection(clazz, result);
		}

		analyzeMethods(clazz, result);

		detectAnnotations(clazz, result);

		detectCodeSmells(clazz, result);
		
		detectValueAnnotations(clazz, result, availableProperties);

		return result;
	}

	/* Detect Spring Layers */

	private void detectLayer(ClassOrInterfaceDeclaration clazz, ASTResult result) {

		clazz.getAnnotations().forEach(annotation -> {

			String name = annotation.getNameAsString();

			switch (name) {

			case "RestController":
				result.layer = "Controller";
				break;

			case "Service":
				result.layer = "Service";
				break;

			case "Repository":
				result.layer = "Repository";
				break;

			case "Component":
				result.layer = "Component";
				break;
			}

		});
	}

	private void detectValueAnnotations(ClassOrInterfaceDeclaration clazz, ASTResult result,
			Set<String> availableProperties) {

		clazz.findAll(FieldDeclaration.class).forEach(field -> {

			field.getAnnotations().forEach(annotation -> {

				if (annotation.getNameAsString().equals("Value")) {

					String expression = annotation.toString();
					String key = extractPropertyKey(expression);

					if (key != null && !availableProperties.contains(key)) {

						String fieldName = field.getVariables().get(0).getNameAsString();

						int line = annotation.getBegin().map(p -> p.line).orElse(-1);

						result.missingProperties.add(new MissingProperty(result.className, fieldName, line, key));

						System.out.println("Missing @Value: " + result.className + "." + fieldName + " (line " + line
								+ ") -> " + key);
					}
				}
			});
		});

		/* Constructor params */

		clazz.getConstructors().forEach(constructor -> {

			constructor.getParameters().forEach(param -> {

				param.getAnnotations().forEach(annotation -> {

					if (annotation.getNameAsString().equals("Value")) {

						String expression = annotation.toString();
						String key = extractPropertyKey(expression);

						if (key != null && !availableProperties.contains(key)) {

							int line = annotation.getBegin().map(p -> p.line).orElse(-1);

							result.missingProperties
									.add(new MissingProperty(result.className, param.getNameAsString(), line, key));

							System.out.println("Missing @Value: " + result.className + "." + param.getNameAsString()
									+ " (line " + line + ") -> " + key);
						}
					}
				});
			});
		});
	}
	
	
	private String extractPropertyKey(String expression) {

	    if (expression == null) return null;

	    if (expression.contains("${") && expression.contains("}")) {

	        int start = expression.indexOf("${") + 2;
	        int end = expression.indexOf("}");

	        String inner = expression.substring(start, end);

	        int colonIndex = inner.indexOf(":");

	        if (colonIndex != -1) {
	            return inner.substring(0, colonIndex);
	        }

	        return inner;
	    }

	    return null;
	}

	/* Injection Detection */

	private void detectInjection(ClassOrInterfaceDeclaration clazz, ASTResult result) {

		clazz.findAll(FieldDeclaration.class).forEach(field -> {

			field.getAnnotations().forEach(annotation -> {

				if (annotation.getNameAsString().equals("Autowired")) {
					result.fieldInjection = true;
				}

			});

		});

		clazz.getConstructors().forEach(constructor -> {

			if (constructor.getParameters().size() > 0) {
				result.constructorInjection = true;
			}

		});
	}

	/* Method Analysis */

	private void analyzeMethods(ClassOrInterfaceDeclaration clazz, ASTResult result) {

		clazz.findAll(MethodDeclaration.class).forEach(method -> {

			int complexity = method.findAll(IfStmt.class).size() + method.findAll(ForStmt.class).size()
					+ method.findAll(WhileStmt.class).size() + method.findAll(SwitchStmt.class).size()
					+ method.findAll(CatchClause.class).size() + 1;

			result.methodComplexity.put(method.getNameAsString(), complexity);

		});
	}

	/* Custom Annotation Processing */

	private void detectAnnotations(ClassOrInterfaceDeclaration clazz, ASTResult result) {

		clazz.findAll(AnnotationExpr.class).forEach(annotation -> {

			result.annotations.add(annotation.getNameAsString());

		});
	}

	/* Code Smell Detection */

	private void detectCodeSmells(ClassOrInterfaceDeclaration clazz, ASTResult result) {

		int methodCount = clazz.getMethods().size();

		if (methodCount > 20) {
			result.codeSmells.add("God Class (too many methods)");
		}

		result.methodComplexity.forEach((method, complexity) -> {

			if (complexity > 10) {
				result.codeSmells.add("High complexity method: " + method);
			}

		});

		if (result.fieldInjection) {
			result.codeSmells.add("Field Injection detected (prefer constructor injection)");
		}
	}
}