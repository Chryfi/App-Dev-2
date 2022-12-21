package main.java.de.appdev2.utils;

import java.sql.Date;
import java.time.LocalDate;

public class DateUtils {

    /**
     * Erstelle ein Datum mit den gegebenen Daten.
     *
     * @param year  das Jahr, beginnend bei 0, anstelle von javas 1900
     * @param month Monat von 1 bis 12
     * @param day   Tag von 1 bis 31
     * @return Datum
     */
    public static Date getDate(int year, int month, int day) {
        return new Date(year - 1900, month - 1, day);
    }

    /**
     * Generiere ein zufälliges Datum zwischen minYear und maxYear
     *
     * @param minYear untere Grenze für das zufällige Jahr
     * @param maxYear obere Grenze für das zufällige Jahr
     * @return zufälliges Datum
     */
    public static Date getRandomDate(int minYear, int maxYear) {
        int year = minYear + (int) (Math.random() * maxYear);
        int month = (int) Math.round(Math.random() * 12);
        int day = (int) Math.round(Math.random() * 31);
        return getDate(year, month, day);
    }
}
