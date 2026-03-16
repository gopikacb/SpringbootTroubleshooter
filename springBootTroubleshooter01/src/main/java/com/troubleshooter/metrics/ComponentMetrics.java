package com.troubleshooter.metrics;

public class ComponentMetrics {

    private String componentName;

    // Robert C Martin Metrics
    private int afferentCoupling;   // Ca
    private int efferentCoupling;   // Ce

    private double instability;     // I
    private double abstractness;    // A
    private double distance;        // D

    public ComponentMetrics(String componentName) {
        this.componentName = componentName;
    }

    // ---------- Coupling ----------

    public int getAfferentCoupling() {
        return afferentCoupling;
    }

    public void setAfferentCoupling(int afferentCoupling) {
        this.afferentCoupling = afferentCoupling;
    }

    public int getEfferentCoupling() {
        return efferentCoupling;
    }

    public void setEfferentCoupling(int efferentCoupling) {
        this.efferentCoupling = efferentCoupling;
    }

    // ---------- Abstractness ----------

    public double getAbstractness() {
        return abstractness;
    }

    public void setAbstractness(double abstractness) {
        this.abstractness = abstractness;
    }

    // ---------- Instability ----------

    public double getInstability() {
        return instability;
    }

    public void calculateInstability() {

        int total = afferentCoupling + efferentCoupling;

        if (total == 0) {
            instability = 0;
        } else {
            instability = (double) efferentCoupling / total;
        }
    }

    // ---------- Distance from Main Sequence ----------

    public double getDistance() {
        return distance;
    }

    public void calculateDistance() {

        distance = Math.abs(abstractness + instability - 1);
    }

    // ---------- Component Info ----------

    public String getComponentName() {
        return componentName;
    }

    // ---------- Utility ----------

    @Override
    public String toString() {

        return "\nComponent: " + componentName +
                "\nCa (Afferent Coupling): " + afferentCoupling +
                "\nCe (Efferent Coupling): " + efferentCoupling +
                "\nInstability (I): " + String.format("%.2f", instability) +
                "\nAbstractness (A): " + String.format("%.2f", abstractness) +
                "\nDistance (D): " + String.format("%.2f", distance);
    }
}