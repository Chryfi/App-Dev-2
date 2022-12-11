package main.java.de.appdev2.server.database;

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

    public Database(String databaseName, String address, String service) {
        this.databaseName = databaseName;
        this.address = address;
        this.service = service;
    }

    public void connect(String user, String password) throws SQLException {
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
