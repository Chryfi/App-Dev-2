package main.java.de.appdev2.utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class MathUtils {
    public static double randomRange(double min, double max) {
        return Math.random() * (max - min) + min;
    }

    public static int randomRange(int min, int max) {
        return (int) Math.round(Math.random() * (max - min) + min);
    }

    public static double round(double num, int digit) {
        double digits = Math.pow(10, digit);
        return (double) Math.round(num * digits) / digits;
    }

    public static float round(float num, int digit) {
        return (float) round((double) num, digit);
    }
}
