package com.troubleshooter.metrics;

import java.util.*;

public class MetricsGraphPrinter {

    private static final int WIDTH = 50;
    private static final int HEIGHT = 10;

    public void printGraph(List<ComponentMetrics> metrics) {

        char[][] grid = new char[HEIGHT + 1][WIDTH + 1];

        for (char[] row : grid)
            Arrays.fill(row, ' ');

        Map<Character, ComponentMetrics> legend = new LinkedHashMap<>();

        char id = 'A';

        for (ComponentMetrics m : metrics) {

            int x = (int) (m.getInstability() * WIDTH);
            int y = HEIGHT - (int) (m.getAbstractness() * HEIGHT);

            if (x > WIDTH) x = WIDTH;
            if (y < 0) y = 0;

            char marker = id++;

            grid[y][x] = marker;

            legend.put(marker, m);
        }

        /* Draw Main Sequence Line (A + I = 1) */

        for (int i = 0; i <= WIDTH; i++) {

            int y = HEIGHT - (int) ((1 - ((double) i / WIDTH)) * HEIGHT);

            if (y >= 0 && y <= HEIGHT && grid[y][i] == ' ')
                grid[y][i] = '.';
        }

        System.out.println("\nArchitecture Metrics Graph (Abstractness vs Instability)\n");

        for (int i = 0; i <= HEIGHT; i++) {

            double a = 1.0 - ((double) i / HEIGHT);

            System.out.printf("%.1f | ", a);

            for (int j = 0; j <= WIDTH; j++)
                System.out.print(grid[i][j]);

            System.out.println();
        }

        System.out.println("    +" + "-".repeat(WIDTH));
        System.out.println("      0.0      0.2      0.4      0.6      0.8      1.0");
        System.out.println("                     Instability\n");

        printLegend(legend);
    }

    private void printLegend(Map<Character, ComponentMetrics> legend) {

        System.out.println("Legend");
        System.out.println("-----------------------------------------------");

        for (Map.Entry<Character, ComponentMetrics> e : legend.entrySet()) {

            ComponentMetrics m = e.getValue();

            System.out.printf(
                    "%s  %-20s (A=%.2f I=%.2f D=%.2f)\n",
                    e.getKey(),
                    shortName(m.getComponentName()),
                    m.getAbstractness(),
                    m.getInstability(),
                    m.getDistance());
        }
    }

    private String shortName(String name) {

        if (name.contains("."))
            return name.substring(name.lastIndexOf('.') + 1);

        return name;
    }
}