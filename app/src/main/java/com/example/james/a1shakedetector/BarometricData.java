package com.example.james.a1shakedetector;

/**
 * Created by James Warne on 2/18/2016.
 * For: INFO 6120 - Ubiquitous Computing
 *
 * Project addition to satisfy 25 point
 * extra credit requirement.
 */
public class BarometricData {
    private double p;

    public BarometricData(double p) {
        this.p = p;
    }

    public void setP(double p) {
        this.p = p;
    }

    public double getP() {
        return p;
    }

    public String toString() {
        return "{" + p + " hPa - millibar}";
    }
}
