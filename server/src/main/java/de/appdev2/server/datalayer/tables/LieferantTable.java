package main.java.de.appdev2.server.datalayer.tables;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import main.java.de.appdev2.entities.Bestellung;
import main.java.de.appdev2.entities.Lieferant;
import main.java.de.appdev2.server.datalayer.Database;

/**
 * Konkrete Implementation für {@link EntityTable} für die Datenbank Tabelle "lieferant" mit dem Entitätstypen {@link Lieferant}.
 */
public class LieferantTable extends EntityTable<Lieferant> {

    public LieferantTable(Database db) {
        super(db);
    }

    /**
     * Füge einen Lieferanten ein. Der Primärschlüssel muss nicht definiert werden, da er "auto increment" ist.
     * Falls der Lieferant erfolgreich eingefügt werden konnte,
     * wird der Wert des Primärschlüssels ausgelesen und dem Objekt zugewiesen.
     *
     * @param entity der Lieferant, der eingefügt werden soll.
     * @return true, falls ein Primärschlüssel zurückgegeben wurde. false, wenn nichts aus der Datenbank zurückgegeben wurde.
     * @throws SQLException falls ein Fehler bei den Datenbankoperationen aufgetreten ist.
     */
    @Override
    public boolean insert(Lieferant entity) throws SQLException {
        /* gibt den Primärschlüssel des eingefügten Eintrags wieder */
        PreparedStatement stmt = this.db.prepare("INSERT INTO lieferant (name) VALUES (?) RETURNING lieferant.nr");
        stmt.setString(1, entity.getName());

        ResultSet nr = stmt.executeQuery();

        if (!nr.next()) return false;

        entity.setNr(nr.getInt("nr"));

        return true;
    }

    /**
     * Liest den Lieferanten aus der Datenbank aus.
     *
     * @param nr
     * @return gibt einen Lieferanten zurück, falls einer existiert, ansonsten null.
     * @throws SQLException falls ein Fehler bei den Datenbankoperationen aufgetreten ist.
     */
    public Lieferant getLieferant(int nr) throws SQLException {
        PreparedStatement stmt = this.db.prepare("SELECT * FROM lieferant WHERE nr=?");
        stmt.setInt(1, nr);

        ResultSet set = stmt.executeQuery();

        if (!set.next()) return null;

        return new Lieferant(set.getInt("nr"), set.getString("name"));
    }
}
