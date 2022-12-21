package main.java.de.appdev2.server;

import main.java.de.appdev2.entities.*;
import main.java.de.appdev2.server.database.Database;
import main.java.de.appdev2.server.database.TestDatabase;
import main.java.de.appdev2.utils.DateUtils;

import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;

public class Main {
    public static void main(String[] args) throws SQLException {
        Database db = new Database("AppDevDB", "localhost:5432", "postgresql");

        db.connect("appdev", "appdevpassword");

        TestDatabase testDatabase = new TestDatabase(db);

        testDatabase.testRandomData();

        /*Rechnung rechnung = db.getRechnungTable().getRechnung(2);

        System.out.println(rechnung);*/
    }
}
