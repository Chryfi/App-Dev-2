package main.java.de.appdev2.server.datalayer.tables;

import main.java.de.appdev2.entities.Ware;
import main.java.de.appdev2.entities.WarenBestellung;
import main.java.de.appdev2.server.datalayer.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Konkrete Implementation für {@link EntityTable} für die Datenbank Tabelle "ware" mit dem Entitätstypen {@link Ware}.
 */
public class WareTable extends EntityTable<Ware> {

    public WareTable(Database db) {
        super(db);
    }

    /**
     * Fügt eine Ware ein. Der Primärschlüssel muss nicht definiert werden, da er "Auto increment" ist.
     * Falls die Ware erfolgreich eingefügt werden konnte, wird der Wert des Primärschlüssels ausgelesen
     * und dem Objekt zugewiesen.
     *
     * @param entity die Ware, die eingefügt werden soll.
     * @return true, falls ein Primärschlüssel zurückgegeben wurde. false, wenn nichts aus der Datenbank zurückgegeben wurde.
     * @throws SQLException falls ein Fehler bei den Datenbankoperationen aufgetreten ist.
     */
    @Override
    public boolean insert(Ware entity) throws SQLException {
        /* gibt den Primärschlüssel des eingefügten Eintrags wieder */
        PreparedStatement stmt = this.db.prepare("INSERT INTO ware (stueckpreis, bezeichnung, stueckzahl) VALUES (?,?,?) RETURNING ware.nr");
        stmt.setFloat(1, entity.getStueckpreis());
        stmt.setString(2, entity.getBezeichnung());
        stmt.setInt(3, entity.getStueckzahl());

        ResultSet nr = stmt.executeQuery();

        if (!nr.next()) return false;

        entity.setNr(nr.getInt("nr"));

        return true;
    }

    /**
     * Liest die Waren aus der Datenbank aus und erstellt ein Waren-Objekt.
     *
     * @param nr
     * @return eine Ware wird zurückgegeben, falls vorhanden, ansonsten null.
     * @throws SQLException falls ein Fehler bei den Datenbankoperationen aufgetreten ist.
     */
    public Ware getWare(int nr) throws SQLException {
        PreparedStatement stmt = this.db.prepare("SELECT * FROM ware WHERE nr=?");

        stmt.setInt(1, nr);

        ResultSet set = stmt.executeQuery();

        if (!set.next()) return null;

        return new Ware(set.getInt("nr"), set.getFloat("stueckpreis"),
                set.getInt("stueckzahl"), set.getString("bezeichnung"));
    }

    /**
     * Setzt das Attribut stueckzahl in der angegebenen Ware in der Datenbank.
     * Wenn eine Änderung vorgenommen wurde, wird die Instanzvariable "stueckzahl" des Waren-Objekts ebenfalls gesetzt.
     * Dies stellt sicher, dass die Instanzen den Stand der Datenbank widerspiegeln.
     *
     * @param ware
     * @param stueckzahl
     * @return true, fall das Update durchgeführt werden konnte.
     * @throws SQLException falls ein Fehler bei den Datenbankoperationen aufgetreten ist.
     */
    public boolean updateStueckzahl(Ware ware, int stueckzahl) throws SQLException {
        PreparedStatement stmt = this.db.prepare("UPDATE ware SET stueckzahl=? WHERE nr=?");
        stmt.setInt(1, stueckzahl);
        stmt.setInt(2, ware.getNr());

        boolean update = stmt.executeUpdate() != 0;

        if (update) ware.setStueckzahl(stueckzahl);

        return update;
    }
}
