package com.troubleshooter.utils;

import java.util.ArrayList;
import java.util.List;

import com.tngtech.archunit.lang.EvaluationResult;
import com.troubleshooter.model.Violation;

public class ArchUnitUtils {

	public static String getSimpleName(String fqcn) {

		if (fqcn == null || !fqcn.contains(".")) {
			return fqcn;
		}

		return fqcn.substring(fqcn.lastIndexOf('.') + 1);
	}

	public static List<Violation> mapViolations(EvaluationResult result, String ruleName, String ruleType) {

		List<Violation> list = new ArrayList<>();

		for (String detail : result.getFailureReport().getDetails()) {

			String className = "Unknown";
			String fieldName = "-";
			String dependency = "Unknown";

			/*
			 * FIELD dependency format
			 *
			 * Field <A.b> has type <C>
			 */
			String source = extractBetween(detail, "Field <", ">");

			String target = extractBetween(detail, "has type <", ">");

			/*
			 * METHOD dependency format
			 */
			if (source == null) {

				source = extractBetween(detail, "Method <", ">");
			}

			/*
			 * CLASS access format
			 *
			 * accesses class <X>
			 */
			if (target == null) {

				target = extractBetween(detail, "accesses class <", ">");
			}

			/*
			 * Parse source
			 */
			if (source != null) {

				int lastDot = source.lastIndexOf(".");

				if (lastDot > 0) {

					className = getSimpleName(source.substring(0, lastDot));

					fieldName = source.substring(lastDot + 1);

				} else {

					className = getSimpleName(source);
				}
			}

			/*
			 * Parse target
			 */
			if (target != null) {

				dependency = getSimpleName(target);
			}

			list.add(new Violation(ruleName, ruleType, className, fieldName, dependency, getSuggestion(ruleType)));
		}

		return list;
	}

	private static String getSuggestion(String ruleType) {

		if (ruleType == null) {

			return "Refactor code to comply with architectural constraints.";
		}

		switch (ruleType.toUpperCase()) {

		case "LAYER":
			return "Follow defined layer boundaries. Avoid accessing restricted layers directly.";

		case "CYCLE":
			return "Break cyclic dependencies by introducing abstractions or redesigning module interactions.";

		case "SPRING":
			return "Ensure proper usage of Spring annotations and dependency injection.";

		default:
			return "Refactor code to comply with architectural constraints.";
		}
	}

	private static String extractBetween(String text, String start, String end) {

		int i = text.indexOf(start);

		if (i == -1) {
			return null;
		}

		int j = text.indexOf(end, i + start.length());

		if (j == -1) {
			return null;
		}

		return text.substring(i + start.length(), j);
	}
}