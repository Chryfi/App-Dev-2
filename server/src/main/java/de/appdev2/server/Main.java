package main.java.de.appdev2.server;

import main.java.de.appdev2.server.application.WarenEingangImpl;
import main.java.de.appdev2.server.database.Database;
import main.java.de.appdev2.server.testing.TestDatabase;

import java.rmi.RemoteException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        Database db = new Database("AppDevDB", "localhost:5432", "postgresql");

        try {
            db.connect("appdev", "appdevpassword");
        } catch (SQLException e) {
            System.out.println("Konnte nicht zur Datenbank verbinden!");

            e.printStackTrace();

            return;
        }

        try {
            Server<WarenEingangImpl> warenKatze = new Server<>("localhost", 1239, "katze", new WarenEingangImpl(db));

            warenKatze.host();
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
