package main.java.de.appdev2.server.datalayer.tables;

import main.java.de.appdev2.server.datalayer.Database;

import java.sql.SQLException;

/**
 * Ein Abstrahierung der Table-Klassen, um Code wiederverwendbarer und organisierter zu machen.
 * Die Subklassen enthalten notwendige Methoden, um in der Datenbanktabelle Daten zu bearbeiten und auszulesen.
 *
 * @param <T> generischer Datentyp der Entität, der bei der Implementation ersetzt wird, um Fehleingaben zu verhindern
 */
public abstract class EntityTable<T> {
    /**
     * Die Datenbank in der diese Tabelle vorhanden ist.
     */
    protected Database db;

    public EntityTable(Database db) {
        this.db = db;
    }

    public Database getDatabase() {
        return this.db;
    }

    /**
     * Füge die Entität in die Datenbank Tabelle ein.
     * @param entity repräsentiert den Entitätstypen, der bei der Erstellung des Datenmodells identifiziert wurde
     * @return true, falls es funktioniert hat. False, wenn nichts geändert wurde.
     * @throws SQLException falls ein Fehler bei den Datenbankoperationen aufgetreten ist.
     */
    public abstract boolean insert(T entity) throws SQLException;
}
