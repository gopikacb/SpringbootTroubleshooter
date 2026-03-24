package com.troubleshooter.report;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.troubleshooter.metrics.AnalysisIssue;
import com.troubleshooter.metrics.ComponentMetrics;
import com.troubleshooter.ast.ASTResult;
import com.troubleshooter.ast.MissingProperty;

public class HtmlReportGenerator {

	public void generateReport(List<ComponentMetrics> metrics, List<ASTResult> astResults, List<AnalysisIssue> issues,
			int score) throws IOException {

		try (FileWriter writer = new FileWriter("ArchitectureHealthReport.html")) {

			writer.write(buildHeader());

			writer.write("<h1>Architecture Health Report</h1>");
			writer.write("<h2>\n Architecture Health Score: " + score + "/100</h2>");

			writer.write(buildSummarySection(issues));

			writer.write(buildMetricsAndGraphSection(metrics));

			writer.write(buildIssuesSection(issues));
			
			writer.write(buildASTSection(astResults));


			writer.write(buildFooter());
		}

		System.out.println("HTML Report generated: ArchitectureHealthReport.html");
	}

	/* ---------------- HEADER ---------------- */

	private String buildHeader() {

		return "<!DOCTYPE html>\n" + "<html>\n" + "<head>\n" + "<title>Architecture Health Report</title>\n"
				+ "<script src=\"https://cdn.jsdelivr.net/npm/chart.js\"></script>\n" + "<style>\n"
				+ "body { font-family: Arial; margin: 40px; }\n" + "table { border-collapse: collapse; width: 70%; }\n"
				+ "th, td { border:1px solid #ddd; padding:8px; }\n" + "th { background-color:#f2f2f2; }\n"
				+ "canvas { margin-top:40px; }\n" + ".container { display:flex; gap:40px; align-items:flex-start; }\n"
				+ ".box { flex:1; }\n" + ".container { display:flex; gap:40px; align-items:flex-start; }\n"
				+ ".card { flex:1; padding:15px; border:1px solid #ddd; border-radius:8px; background:#ffffff; box-shadow:0 2px 5px rgba(0,0,0,0.1); min-height:450px; }\n"
				+ "</style>\n" + "</head>\n" + "<body>\n";
	}

	private String buildMetricsAndGraphSection(List<ComponentMetrics> metrics) {

		StringBuilder html = new StringBuilder();

		html.append("<div class='container'>");

		// Metrics Card
		html.append("<div class='card'>");
		html.append(buildMetricsTable(metrics));
		html.append("</div>");

		// Graph Card
		html.append("<div class='card'>");
		html.append(buildGraph(metrics));
		html.append("</div>");

		html.append("</div>");

		return html.toString();
	}

	private String buildSummarySection(List<AnalysisIssue> issues) {

		long high = issues.stream().filter(i -> "HIGH".equals(i.getSeverity())).count();

		long medium = issues.stream().filter(i -> "MEDIUM".equals(i.getSeverity())).count();

		long low = issues.stream().filter(i -> "LOW".equals(i.getSeverity())).count();

		StringBuilder html = new StringBuilder();

		html.append("<h2>Issue Summary</h2>");

		html.append("<div style='display:flex; gap:20px; margin-bottom:20px;'>");

		html.append(buildSummaryCard("HIGH", high, "red"));
		html.append(buildSummaryCard("MEDIUM", medium, "orange"));
		html.append(buildSummaryCard("LOW", low, "green"));

		html.append("</div>");

		return html.toString();
	}

	private String buildSummaryCard(String label, long count, String color) {

		return "<div style='padding:15px 25px; border-radius:8px; " + "background-color:#f9f9f9; border-left:6px solid "
				+ color + ";'>" + "<h3 style='margin:0; color:" + color + ";'>" + label + "</h3>"
				+ "<p style='font-size:20px; margin:5px 0 0 0;'>" + count + "</p>" + "</div>";
	}

	/* ---------------- METRICS TABLE ---------------- */

	private String buildMetricsTable(List<ComponentMetrics> metrics) {

		StringBuilder html = new StringBuilder();

		html.append("<h2>Component Metrics</h2>");
		html.append("<table>");
		html.append("<tr>");
		html.append("<th>Component</th>");
		html.append("<th>Ca</th>");
		html.append("<th>Ce</th>");
		html.append("<th>Instability</th>");
		html.append("<th>Abstractness</th>");
		html.append("<th>Distance</th>");
		html.append("</tr>");

		for (ComponentMetrics m : metrics) {

			html.append("<tr>");
			html.append("<td>").append(m.getComponentName()).append("</td>");
			html.append("<td>").append(m.getAfferentCoupling()).append("</td>");
			html.append("<td>").append(m.getEfferentCoupling()).append("</td>");
			html.append("<td>").append(String.format("%.2f", m.getInstability())).append("</td>");
			html.append("<td>").append(String.format("%.2f", m.getAbstractness())).append("</td>");
			html.append("<td>").append(String.format("%.2f", m.getDistance())).append("</td>");
			html.append("</tr>");
		}

		html.append("</table>");

		return html.toString();
	}

	/* ---------------- GRAPH ---------------- */

	private String buildGraph(List<ComponentMetrics> metrics) {

		StringBuilder datasets = new StringBuilder();

		String[] colors = { "red", "blue", "green", "orange", "purple", "brown", "black", "cyan" };

		int index = 0;

		for (ComponentMetrics m : metrics) {

			if (index > 0) {
				datasets.append(",");
			}

			datasets.append("{");
			datasets.append("label:'").append(m.getComponentName()).append("',");
			datasets.append("data:[{x:").append(m.getInstability()).append(",y:").append(m.getAbstractness())
					.append("}],");
			datasets.append("backgroundColor:'").append(colors[index % colors.length]).append("',");
			datasets.append("pointRadius:8");
			datasets.append("}");

			index++;
		}

		return "<h2>Architecture Metrics Graph (Abstractness vs Instability)</h2>\n"
				+ "<div style='position:relative; height:400px;'>\n" + "<canvas id=\"chart\"></canvas>\n" + "</div>\n"
				+ "<script>\n" + "const ctx = document.getElementById('chart');\n"
				+ "new Chart(ctx,{type:'scatter',data:{datasets:[" + datasets
				+ ",{label:'Main Sequence',type:'line',data:[{x:0,y:1},{x:1,y:0}],"
				+ "borderColor:'black',borderDash:[5,5],fill:false}]}," + "options:{" + "responsive:true,"
				+ "maintainAspectRatio:false," + "plugins:{legend:{position:'top'}}," + "scales:{"
				+ "x:{min:0,max:1,title:{display:true,text:'Instability'}},"
				+ "y:{min:0,max:1,title:{display:true,text:'Abstractness'}}" + "}" + "}" + "});\n" + "</script>\n";
	}

	/* ---------------- AST ANALYSIS ---------------- */

	private String buildASTSection(List<ASTResult> astResults) {

		StringBuilder html = new StringBuilder();

		html.append("<h2>AST Analysis</h2>");

		for (ASTResult r : astResults) {

			html.append("<h3>").append(r.className).append("</h3>");
			html.append("<ul>");

			if (r.layer != null) {
				html.append("<li>Layer: ").append(r.layer).append("</li>");
			}

			if (r.fieldInjection) {
				html.append("<li>Field Injection detected</li>");
			}

			if (r.constructorInjection) {
				html.append("<li>Constructor Injection used</li>");
			}

			r.methodComplexity.forEach((method, complexity) -> {

				html.append("<li>Method: ").append(method).append(" | Complexity: ").append(complexity).append("</li>");
			});

			r.codeSmells.forEach(smell -> {

				html.append("<li style='color:red'>Code Smell: ").append(smell).append("</li>");
			});

			/* 🔥 NEW: Missing @Value properties */

			if (r.missingProperties != null && !r.missingProperties.isEmpty()) {

				html.append("<li><b>Missing @Value Properties:</b></li>");
				html.append("<ul>");

				for (MissingProperty mp : r.missingProperties) {

					html.append("<li style='color:red'>").append(mp.getClassName()).append(".")
							.append(mp.getFieldName()).append(" (line ").append(mp.getLineNumber()).append(") → ")
							.append(mp.getPropertyKey()).append("</li>");
				}

				html.append("</ul>");
			}
			
			
			if (r.valueMisuses != null && !r.valueMisuses.isEmpty()) {

			    html.append("<li><b>@Value Misuse (Non-Spring Bean):</b></li>");
			    html.append("<ul>");

			    r.valueMisuses.forEach(m -> {

			        html.append("<li style='color:orange'>")
			                .append(m.getClassName())
			                .append(".")
			                .append(m.getFieldName())
			                .append(" (line ")
			                .append(m.getLineNumber())
			                .append(") → @Value will NOT be injected")
			                .append("</li>");
			    });

			    html.append("</ul>");
			}

			html.append("</ul>");
		}

		return html.toString();
	}
	/* ---------------- FOOTER ---------------- */

	private String buildFooter() {

		return "</body>\n</html>";
	}

	private String buildIssuesSection(List<AnalysisIssue> issues) {

		StringBuilder html = new StringBuilder();

		html.append("<h2>Architecture Issues</h2>");

		if (issues == null || issues.isEmpty()) {
			html.append("<p style='color:green;'>No architectural issues detected 🎉</p>");
			return html.toString();
		}

		for (AnalysisIssue issue : issues) {

			String color = "black";

			if ("HIGH".equals(issue.getSeverity()))
				color = "red";
			else if ("MEDIUM".equals(issue.getSeverity()))
				color = "orange";
			else if ("LOW".equals(issue.getSeverity()))
				color = "green";

			html.append("<div style='border:1px solid #ddd; padding:15px; margin:10px 0; border-left:6px solid ")
					.append(color).append("; border-radius:6px;'>");

			html.append("<h3>").append(issue.getComponentName()).append(" - ").append(issue.getIssueType()).append(" (")
					.append(issue.getSeverity()).append(")").append("</h3>");

			html.append("<p><b>Metrics:</b> Instability=").append(String.format("%.2f", issue.getInstability()))
					.append(" | Abstractness=").append(String.format("%.2f", issue.getAbstractness()))
					.append(" | Distance=").append(String.format("%.2f", issue.getDistance())).append("</p>");

			html.append("<p><b>Problem:</b> ").append(issue.getDescription()).append("</p>");

			html.append("<p><b>Impact:</b> ").append(issue.getImpact()).append("</p>");

			html.append("<p><b>Recommendations:</b></p><ul>");

			for (String rec : issue.getRecommendations()) {
				html.append("<li>").append(rec).append("</li>");
			}

			html.append("</ul>");
			html.append("</div>");
		}

		return html.toString();
	}
}