package com.troubleshooter.report;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.troubleshooter.metrics.ComponentMetrics;
import com.troubleshooter.ast.ASTResult;

public class HtmlReportGenerator {

    public void generateReport(
            List<ComponentMetrics> metrics,
            List<ASTResult> astResults,
            int score) throws IOException {

        try (FileWriter writer = new FileWriter("ArchitectureHealthReport.html")) {

            writer.write(buildHeader());

            writer.write("<h1>Architecture Health Report</h1>");
            writer.write("<h2>Architecture Health Score: " + score + "/100</h2>");

            writer.write(buildMetricsTable(metrics));

            writer.write(buildGraph(metrics));

            writer.write(buildASTSection(astResults));

            writer.write(buildFooter());
        }

        System.out.println("HTML Report generated: ArchitectureHealthReport.html");
    }

    /* ---------------- HEADER ---------------- */

    private String buildHeader() {

        return "<!DOCTYPE html>\n"
                + "<html>\n"
                + "<head>\n"
                + "<title>Architecture Health Report</title>\n"
                + "<script src=\"https://cdn.jsdelivr.net/npm/chart.js\"></script>\n"
                + "<style>\n"
                + "body { font-family: Arial; margin: 40px; }\n"
                + "table { border-collapse: collapse; width: 70%; }\n"
                + "th, td { border:1px solid #ddd; padding:8px; }\n"
                + "th { background-color:#f2f2f2; }\n"
                + "canvas { margin-top:40px; }\n"
                + "</style>\n"
                + "</head>\n"
                + "<body>\n";
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

        String[] colors = {
                "red","blue","green","orange",
                "purple","brown","black","cyan"
        };

        int index = 0;

        for (ComponentMetrics m : metrics) {

            if (index > 0) {
                datasets.append(",");
            }

            datasets.append("{");
            datasets.append("label:'").append(m.getComponentName()).append("',");
            datasets.append("data:[{x:").append(m.getInstability())
                    .append(",y:").append(m.getAbstractness()).append("}],");
            datasets.append("backgroundColor:'")
                    .append(colors[index % colors.length]).append("',");
            datasets.append("pointRadius:8");
            datasets.append("}");

            index++;
        }

        return "<h2>Architecture Metrics Graph (Abstractness vs Instability)</h2>\n"
                + "<canvas id=\"chart\" width=\"900\" height=\"450\"></canvas>\n"
                + "<script>\n"
                + "const ctx = document.getElementById('chart');\n"
                + "new Chart(ctx,{type:'scatter',data:{datasets:["
                + datasets
                + ",{label:'Main Sequence',type:'line',data:[{x:0,y:1},{x:1,y:0}],"
                + "borderColor:'black',borderDash:[5,5],fill:false}]},"
                + "options:{scales:{"
                + "x:{min:0,max:1,title:{display:true,text:'Instability'}},"
                + "y:{min:0,max:1,title:{display:true,text:'Abstractness'}}"
                + "}}});\n"
                + "</script>\n";
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

                html.append("<li>Method: ")
                        .append(method)
                        .append(" | Complexity: ")
                        .append(complexity)
                        .append("</li>");
            });

            r.codeSmells.forEach(smell -> {

                html.append("<li style='color:red'>Code Smell: ")
                        .append(smell)
                        .append("</li>");
            });

            html.append("</ul>");
        }

        return html.toString();
    }

    /* ---------------- FOOTER ---------------- */

    private String buildFooter() {

        return "</body>\n</html>";
    }
}