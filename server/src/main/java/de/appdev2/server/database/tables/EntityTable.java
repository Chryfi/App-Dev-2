package main.java.de.appdev2.server.database.tables;

import main.java.de.appdev2.server.database.Database;

import java.sql.SQLException;

public abstract class EntityTable<T> {
    protected Database db;

    public EntityTable(Database db) {
        this.db = db;
    }

    public abstract boolean insert(T entity) throws SQLException;
}
