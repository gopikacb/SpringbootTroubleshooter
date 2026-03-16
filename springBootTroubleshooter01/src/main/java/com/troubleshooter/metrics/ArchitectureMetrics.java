package com.troubleshooter.metrics;

public class ArchitectureMetrics {

    public static int calculateScore(int violations) {

        int score = 100;

        score -= violations * 5;

        if (score < 0) {
            score = 0;
        }

        return score;
    }
}