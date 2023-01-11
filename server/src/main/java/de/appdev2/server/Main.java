package main.java.de.appdev2.server;

import main.java.de.appdev2.entities.*;
import main.java.de.appdev2.server.application.WarenEingangImpl;
import main.java.de.appdev2.server.datalayer.Database;
import main.java.de.appdev2.utils.DateUtils;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        /* Starte Datenschicht */
        Database db = new Database("AppDevDB", "localhost:5432", "postgresql");

        try {
            db.connect("appdev", "appdevpassword");
        } catch (SQLException e) {
            System.out.println("Konnte nicht zur Datenbank verbinden!");

            e.printStackTrace();

            return;
        }

        insertDemoData(db);

        /* Starte Anwendungsschicht */
        try {
            Server<WarenEingangImpl> warenKatze = new Server<>("localhost", 1239, "katze", new WarenEingangImpl(db));

            warenKatze.host();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void insertDemoData(Database db) {
        Lieferant lieferant = new Lieferant("Katzenfutter XXL");
        Bestellung bestellung = new Bestellung(123401234, DateUtils.getDate(2023, 3, 2), lieferant);

        Ware fleisch = new Ware(52.99F, 0, "Wagyu Katzen Steak");
        Ware katzenbaum = new Ware(49.99F, 0, "Katzenbaum XXL");
        Ware spielzeug1 = new Ware(10.00F, 0, "Ball klein");
        Ware spielzeug2 = new Ware(15.49F, 0, "Kuscheltier");
        Ware spielzeug3 = new Ware(19.99F, 0, "vergoldete krasse Robo-Maus");
        Ware katzenbett = new Ware(37.46F, 0, "Katzenbett XXL für chonky Katzen");

        List<Ware> waren = new ArrayList<>(Arrays.asList(fleisch, katzenbaum, spielzeug1, spielzeug2, spielzeug3, katzenbett));

        WarenBestellung fleischBestellung = new WarenBestellung(fleisch, bestellung, 10, 0);
        WarenBestellung katzenbaumBestellung = new WarenBestellung(katzenbaum, bestellung, 2, 0);
        WarenBestellung spielzeug1Bestellung = new WarenBestellung(spielzeug1, bestellung, 25, 0);
        WarenBestellung spielzeug2Bestellung = new WarenBestellung(spielzeug2, bestellung, 20, 0);
        WarenBestellung spielzeug3Bestellung = new WarenBestellung(spielzeug3, bestellung, 15, 0);
        WarenBestellung katzenbettBestellung = new WarenBestellung(katzenbett, bestellung, 4, 0);

        List<WarenBestellung> wb = new ArrayList<>(Arrays.asList(fleischBestellung, katzenbaumBestellung,
                spielzeug1Bestellung, spielzeug2Bestellung, spielzeug3Bestellung, katzenbettBestellung));

        Rechnung rechnung = new Rechnung(bestellung, DateUtils.getDate(2023, 1, 19), true);

        try {
            db.getLieferantTable().insert(lieferant);

            System.out.println("Lieferant hat die Nummer: " + lieferant.getNr());

            db.getBestellungTable().insert(bestellung);

            for (Ware ware : waren) {
                db.getWareTable().insert(ware);
            }

            for (WarenBestellung warenBestellung : wb) {
                db.getWarenBestellungTable().insert(warenBestellung);
            }

            db.getRechnungTable().insert(rechnung);
        } catch (SQLException e) {
            System.out.println("Fehler beim Einfügen der Demodaten!");
            e.printStackTrace();
        }
    }
}
