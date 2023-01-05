package main.java.de.appdev2.server.datalayer;

import main.java.de.appdev2.server.datalayer.tables.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Eine Datenbank-Klasse, die die Verbindung mittels JDBC regelt.
 * Funktionalitäten von JDBC werden hier eingekapselt z.B. wird {@link #connection} nicht dem System offengelegt.
 * Die Klasse steuert und verwaltet den Zugriff auf die Datenbank so, dass z.B. nur preparedStatements benutzt werden können.
 * <br><br>
 * Die Datenbank-Klasse besitzt die Tabellen der AppDevDB Datenbank, z.B. {@link #waren} repräsentiert die Tabelle "waren".
 */
public class Database {
    private final String databaseName;
    /**
     * Adresse in Form von "ip:port"
     */
    private final String address;
    /**
     * Der Datenbanktreiber z.B. postgresql, mariadb...
     */
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

    /**
     * Verbindet zu der Datenbank, mit der vorher definierten Adresse und dem angegebenen User und Passwort.
     * Falls eine Verbindung vorher existierte, wird diese getrennt.
     *
     * @param user legt den Nutzer der Datenbank fest
     * @param password legt das entsprechende Passwort zum User fest
     * @throws SQLException kann geworfen werden, wenn die Verbindung getrennt {@link #disconnect()} wird (falls bereits verbunden),
     *                      oder wenn eine Verbindung zur Datenbank mittels {@link DriverManager#getConnection(String, String, String)} hergestellt wird.
     */
    public void connect(String user, String password) throws SQLException {
        /* schließe die vorherige Verbindung, falls vorhanden */
        this.disconnect();
        this.user = user;
        this.password = password;

        this.connection = DriverManager.getConnection("jdbc:" + this.service + "://" + this.address + "/" + this.databaseName, user, password);
    }

    /**
     * @return true, wenn verbunden ist.
     * @throws SQLException falls ein Fehler beim Aufrufen von {@link Connection#isClosed()} aufgetreten ist.
     */
    public boolean isConnected() throws SQLException {
        return this.connection != null && !this.connection.isClosed();
    }

    /**
     * Trennt die Verbindung zur Datenbank, falls {@link #connection} != null.
     * Setzt am Ende {@link #connection} auf null.
     *
     * @throws SQLException falls ein Fehler beim Aufrufen von {@link Connection#close()} aufgetreten ist.
     */
    public void disconnect() throws SQLException {
        if (this.connection == null) return;

        this.connection.close();
        this.connection = null;
    }

    /**
     * Erstelle ein {@link PreparedStatement} basierend auf der angegebenen query.
     * Falls die Datenbank nicht verbunden ist, wird versucht die Verbindung wiederherzustellen.
     * @param query
     * @return ein {@link PreparedStatement} mit der definierten query.
     * @throws SQLException falls ein Fehler beim Aufbau/Wiederverbinden mit der Datenbank auftritt oder ein Fehler beim
     *                      Aufruf von {@link Connection#prepareStatement(String)} auftritt
     */
    public PreparedStatement prepare(String query) throws SQLException {
        if (!this.isConnected()) {
            this.connect(this.user, this.password);
        }

        return this.connection.prepareStatement(query);
    }
}
