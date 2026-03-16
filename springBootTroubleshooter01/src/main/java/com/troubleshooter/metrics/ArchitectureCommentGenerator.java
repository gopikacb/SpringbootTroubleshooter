package com.troubleshooter.metrics;

import java.util.List;

public class ArchitectureCommentGenerator {

    public void generateComments(List<ComponentMetrics> metrics, int score) {

        System.out.println("\nArchitecture Analysis");
        System.out.println("----------------------------------");

        for (ComponentMetrics m : metrics) {

            String name = shortName(m.getComponentName());

            /* Zone of Pain */

            if (m.getInstability() < 0.3 && m.getAbstractness() < 0.3) {

                System.out.println(
                        name + " lies in the Zone of Pain -> stable but concrete.");
            }

            /* Zone of Uselessness */

            if (m.getInstability() > 0.7 && m.getAbstractness() > 0.7) {

                System.out.println(
                        name + " lies in the Zone of Uselessness -> abstract but unstable.");
            }

            /* High Instability */

            if (m.getInstability() > 0.8) {

                System.out.println(
                        name + " is highly unstable -> too many outgoing dependencies.");
            }

            /* Far from Main Sequence */

            if (m.getDistance() > 0.6) {

                System.out.println(
                        name + " is far from the Main Sequence -> architecture imbalance.");
            }
        }

        printScore(score);
    }

    private void printScore(int score) {

        System.out.println("\nOverall Architecture Health Score: " + score + "/100");

        if (score >= 90)
            System.out.println("Architecture Quality: Excellent");

        else if (score >= 75)
            System.out.println("Architecture Quality: Good");

        else if (score >= 60)
            System.out.println("Architecture Quality: Moderate");

        else
            System.out.println("Architecture Quality: Needs Improvement");
    }

    private String shortName(String name) {

        if (name.contains("."))
            return name.substring(name.lastIndexOf('.') + 1);

        return name;
    }
}