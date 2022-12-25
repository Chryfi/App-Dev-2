package main.java.de.appdev2.server;

import main.java.de.appdev2.server.database.Database;
import main.java.de.appdev2.server.database.TestDatabase;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        Database db = new Database("AppDevDB", "localhost:5432", "postgresql");

        db.connect("appdev", "appdevpassword");

        TestDatabase testDatabase = new TestDatabase(db);

        testDatabase.generateRandomData();
        testDatabase.testInsertion();
        testDatabase.testSetGelieferteMenge();

        /*Rechnung rechnung = db.getRechnungTable().getRechnung(2);

        System.out.println(rechnung);*/
    }
}
