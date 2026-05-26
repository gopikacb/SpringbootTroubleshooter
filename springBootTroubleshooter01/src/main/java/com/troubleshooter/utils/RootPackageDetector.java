package com.troubleshooter.utils;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;

public class RootPackageDetector {

	public static String findRootPackage(JavaClasses classes) {

		List<String> springBootPackages = classes.stream()

				// ignore tests
				.filter(c -> !isTestClass(c))

				.filter(c -> c.isAnnotatedWith(SpringBootApplication.class))

				.map(JavaClass::getPackageName)

				.distinct()

				.sorted(Comparator.comparingInt(String::length))

				.collect(Collectors.toList());

		if (!springBootPackages.isEmpty()) {

			return springBootPackages.get(0);
		}

		return findCommonRootPackage(classes.stream()

				.filter(c -> !isTestClass(c))

				.map(JavaClass::getPackageName)

				.distinct()

				.collect(Collectors.toList()));
	}

	private static String findCommonRootPackage(List<String> packages) {

		if (packages.isEmpty()) {
			return "";
		}

		String[] root = packages.get(0).split("\\.");

		for (int i = 1; i < packages.size(); i++) {

			String[] current = packages.get(i).split("\\.");

			int minLength = Math.min(root.length, current.length);

			int j = 0;

			while (j < minLength && root[j].equals(current[j])) {

				j++;
			}

			root = java.util.Arrays.copyOf(root, j);

			if (root.length == 0) {
				break;
			}
		}

		return String.join(".", root);
	}

	/**
	 * Ignore tests
	 */
	private static boolean isTestClass(JavaClass javaClass) {

		String name = javaClass.getSimpleName();

		return name.endsWith("Test") || name.endsWith("Tests");
	}
}