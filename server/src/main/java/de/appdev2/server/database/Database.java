package main.java.de.appdev2.server.database;

import main.java.de.appdev2.entities.Bestellung;
import main.java.de.appdev2.server.database.tables.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Database {
    private final String databaseName;
    private final String address;
    private final String service;
    private Connection connection;
    private String user;
    private String password;

    private final WareTable waren;
    private final LieferantTable lieferanten;
    private final BestellungTable bestellungen;
    private final RechnungTable rechnungen;
    private final WarenBestellungTable warenBestellungen;

    public Database(String databaseName, String address, String service) {
        this.databaseName = databaseName;
        this.address = address;
        this.service = service;

        this.waren = new WareTable(this);
        this.rechnungen = new RechnungTable(this);
        this.bestellungen = new BestellungTable(this);
        this.warenBestellungen = new WarenBestellungTable(this);
        this.lieferanten = new LieferantTable(this);
    }

    public LieferantTable getLieferantTable() {
        return this.lieferanten;
    }

    public BestellungTable getBestellungTable() {
        return this.bestellungen;
    }

    public RechnungTable getRechnungTable() {
        return this.rechnungen;
    }

    public WarenBestellungTable getWarenBestellungTable() {
        return this.warenBestellungen;
    }

    public WareTable getWareTable() {
        return this.waren;
    }

    public String getDatabaseName() {
        return this.databaseName;
    }

    public String getAddress() {
        return this.address;
    }

    public String getService() {
        return this.service;
    }

    public void connect(String user, String password) throws SQLException {
        this.disconnect();
        this.user = user;
        this.password = password;

        this.connection = DriverManager.getConnection("jdbc:" + this.service + "://" + this.address + "/" + this.databaseName, user, password);
    }

    public boolean isConnected() throws SQLException {
        return this.connection != null && !this.connection.isClosed();
    }

    public void disconnect() throws SQLException {
        if (this.connection == null) return;

        this.connection.close();
        this.connection = null;
    }

    public PreparedStatement prepare(String query) throws SQLException {
        if (!this.isConnected()) {
            this.connect(this.user, this.password);
        }

        return this.connection.prepareStatement(query);
    }
}
