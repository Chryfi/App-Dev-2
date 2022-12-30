package main.java.de.appdev2.utils;

public class MathUtils {
    public static double randomRange(double min, double max) {
        return Math.random() * (max - min) + min;
    }

    public static int randomRange(int min, int max) {
        return (int) Math.round(Math.random() * (max - min) + min);
    }
}
