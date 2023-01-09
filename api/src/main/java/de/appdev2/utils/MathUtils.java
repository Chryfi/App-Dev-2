package main.java.de.appdev2.utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Eine Utility Klasse für Mathematik Methoden.
 */
public class MathUtils {
    /**
     * @param min
     * @param max
     * @return eine zufällige Zahl zwischen min und max.
     */
    public static double randomRange(double min, double max) {
        return Math.random() * (max - min) + min;
    }

    /**
     * @param min
     * @param max
     * @return eine zufällige Zahl zwischen min und max.
     */
    public static int randomRange(int min, int max) {
        return (int) Math.round(Math.random() * (max - min) + min);
    }

    /**
     * Rundet eine Zahl auf eine gegebene Nachkommastelle.
     *
     * @param num
     * @param digit
     * @return eine gerundete Zahl
     */
    public static double round(double num, int digit) {
        double digits = Math.pow(10, digit);
        return (double) Math.round(num * digits) / digits;
    }

    /**
     * Rundet eine Zahl auf eine gegebene Nachkommastelle.
     *
     * @param num
     * @param digit
     * @return eine gerundete Zahl
     */
    public static float round(float num, int digit) {
        return (float) round((double) num, digit);
    }
}
