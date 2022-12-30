package main.java.de.appdev2.server.testing;

import main.java.de.appdev2.entities.*;
import main.java.de.appdev2.utils.DateUtils;
import main.java.de.appdev2.utils.MathUtils;

import java.util.HashSet;
import java.util.Set;

public class DataGenerator {
    public static Set<WarenBestellung> getWarenBestellungen(Bestellung bestellung, Set<Ware> waren,
                                                            int minBestellt, int maxBestellt,
                                                            int minGeliefert, int maxGeliefert) {
        Set<WarenBestellung> warenBestellungen = new HashSet<>();

        for (Ware ware : waren) {
            int bestellt = MathUtils.randomRange(minBestellt, maxBestellt);
            int geliefert = MathUtils.randomRange(minGeliefert, maxGeliefert);
            WarenBestellung wb = new WarenBestellung(ware, bestellung, bestellt, geliefert);

            warenBestellungen.add(wb);
        }

        return warenBestellungen;
    }

    public static Set<WarenBestellung> getWarenBestellungen(Bestellung bestellung, Set<Ware> waren,
                                                            int minBestellt, int maxBestellt) {
        return getWarenBestellungen(bestellung, waren, minBestellt, maxBestellt, 0, 0);
    }

    public static Bestellung getBestellung(Lieferant lieferant, int minLieferdatum, int maxLieferdatum) {
        return new Bestellung((int) (Math.random() * 10000000), DateUtils.getRandomDate(minLieferdatum, maxLieferdatum), lieferant);
    }

    public static Rechnung getRechnung(Bestellung bestellung, int minRechnungdatum, int maxRechnungdatum, boolean offen) {
        return new Rechnung(bestellung, DateUtils.getRandomDate(minRechnungdatum, maxRechnungdatum), offen);
    }

    public static Rechnung getRechnung(Bestellung bestellung, int minRechnungdatum, int maxRechnungdatum) {
        return getRechnung(bestellung, minRechnungdatum, maxRechnungdatum, false);
    }

    public static Ware getWare(float minStueckPreis, float maxStueckPreis, int maxStueckzahl) {
        return new Ware(Math.round(MathUtils.randomRange(minStueckPreis, maxStueckPreis) * 100) / 100F,
                (int) (Math.random() * maxStueckzahl), "coole Ware");
    }

    public static Set<Ware> getWaren(int min, int max, float minStueckPreis, float maxStueckPreis, int maxStueckzahl) {
        Set<Ware> waren = new HashSet<>();

        for (int j = 0; j < MathUtils.randomRange(min, max); j++) {
            waren.add(DataGenerator.getWare(minStueckPreis, maxStueckPreis, maxStueckzahl));
        }

        return waren;
    }
}
