package main.java.de.appdev2.server;

import main.java.de.appdev2.entities.*;
import main.java.de.appdev2.server.database.Database;
import main.java.de.appdev2.utils.DateUtils;

import java.sql.SQLException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws SQLException {
        Database db = new Database("appdev2", "localhost:5432", "postgresql");

        db.connect("appdev", "appdevpassword");

        generateTestData(db);

        /*Rechnung rechnung = db.getRechnungTable().getRechnung(2);

        System.out.println(rechnung);*/
    }

    public static void generateTestData(Database db) throws SQLException {
        int count = 15;
        int maxWaren = 5;
        int lieferantenCount = 10;

        List<Ware> waren = new ArrayList<>();
        List<Bestellung> bestellungen = new ArrayList<>();
        List<WarenBestellung> warenBestellungen = new ArrayList<>();
        List<Lieferant> lieferanten = new ArrayList<>();
        List<Rechnung> rechnungen = new ArrayList<>();

        for (int i = 0; i < lieferantenCount; i++) {
            lieferanten.add(new Lieferant("keine Ahnung"));
        }

        for (int i = 0; i < count; i++) {
            /*
             * Erstelle eine Bestellung mit einer zufälligen Anzahl an Waren
             * und einem zufälligen Lieferanten
             */
            int warenCount = (int) (Math.random() * maxWaren);
            Lieferant lieferant = lieferanten.get((int) (Math.random() * lieferantenCount));
            Bestellung bestellung = new Bestellung((int) (Math.random() * 100000), DateUtils.getRandomDate(2022, 2025), lieferant);

            /* erstelle Waren und teile sie der Bestellung zu */
            for (int j = 0; j < warenCount; j++) {
                Ware ware = new Ware(Math.round(Math.random() * 20 * 100) / 100, (int) (Math.random() * 100), "coole Ware" + warenCount);
                int bestellt = (int) (Math.random() * 50);
                WarenBestellung wb = new WarenBestellung(ware, bestellung, bestellt, (int) (Math.round(Math.random() * bestellt)));

                waren.add(ware);
                warenBestellungen.add(wb);
            }

            if (Math.random() > 0.5) {
                Rechnung rechnung = new Rechnung(bestellung, DateUtils.getRandomDate(2022, 2030), Math.random() > 0.5);

                rechnungen.add(rechnung);
            }

            bestellungen.add(bestellung);
        }

        /* Einfügen in die Datenbank */
        for (Ware ware : waren) {
            db.getWareTable().insert(ware);
        }

        for (Lieferant lieferant : lieferanten) {
            db.getLieferantTable().insert(lieferant);
        }

        for (Bestellung bestellung : bestellungen) {
            db.getBestellungTable().insert(bestellung);
        }

        for (WarenBestellung bestellung : warenBestellungen) {
            db.getWarenBestellungTable().insert(bestellung);
        }

        for (Rechnung rechnung : rechnungen) {
            db.getRechnungTable().insert(rechnung);
        }



        /* Testen auf Gleichheit mit Datenbank Daten */
        for (Ware ware : waren) {

        }
    }
}
