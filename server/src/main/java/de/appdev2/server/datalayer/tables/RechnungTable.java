package main.java.de.appdev2.server.datalayer.tables;

import main.java.de.appdev2.entities.Bestellung;
import main.java.de.appdev2.entities.Lieferant;
import main.java.de.appdev2.entities.Rechnung;
import main.java.de.appdev2.server.datalayer.Database;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Konkrete Implementation für {@link EntityTable} für die Datenbank Tabelle "rechnung" mit dem Entitätstypen {@link Rechnung}.
 */
public class RechnungTable extends EntityTable<Rechnung> {

    public RechnungTable(Database db) {
        super(db);
    }

    /**
     * Fügt eine Rechnung ein. Der Primärschlüssel muss nicht definiert werden, da er "Auto increment" ist.
     * Falls die Rechnung erfolgreich eingefügt werden konnte,
     * wird der Wert des Primärschlüssels ausgelesen und dem Objekt zugewiesen.
     *
     * @param entity die Rechnung, die eingefügt werden soll.
     * @return true, falls ein Primärschlüssel zurückgegeben wurde. false, wenn nichts aus der Datenbank zurückgegeben wurde.
     * @throws SQLException falls ein Fehler bei den Datenbankoperationen aufgetreten ist.
     */
    @Override
    public boolean insert(Rechnung entity) throws SQLException {
        /* gibt den Primärschlüssel des eingefügten Eintrags wieder */
        PreparedStatement stmt = this.db.prepare("INSERT INTO rechnung (datum, offen, bestellnummer, lieferantennr) VALUES (?,?,?,?) RETURNING rechnung.nr");
        stmt.setDate(1, entity.getDatum());
        stmt.setBoolean(2, entity.isOffen());
        stmt.setInt(3, entity.getBestellung().getNr());
        stmt.setInt(4, entity.getBestellung().getLieferant().getNr());

        ResultSet nr = stmt.executeQuery();

        if (!nr.next()) return false;

        entity.setNr(nr.getInt("nr"));

        return true;
    }

    /**
     * Liest die Rechnung aus der Datenbank aus.
     *
     * @param nr
     * @return gibt eine Rechnung zurück, falls eine existiert, ansonsten null.
     * @throws SQLException falls ein Fehler bei den Datenbankoperationen aufgetreten ist.
     */
    public Rechnung getRechnung(int nr) throws SQLException {
        PreparedStatement stmt = this.db.prepare("SELECT * FROM rechnung WHERE nr=?");
        stmt.setInt(1, nr);

        ResultSet set = stmt.executeQuery();

        if (!set.next()) return null;

        /* Erstelle eine Bestellung, um die Assoziation zu erfüllen. */
        Bestellung bestellung = this.db.getBestellungTable().getBestellung(set.getInt("bestellnummer"), set.getInt("lieferantennr"));

        return this.createRechnung(set, bestellung);
    }

    /**
     * Liest die Rechnung, die zu der angegebenen Bestellung gehört, aus der Datenbank aus.
     *
     * @param bestellung gibt die Bestellung vor, zu der die passende Rechnung ausgelesen werden soll.
     * @return gibt eine Rechnung zurück, die der Bestellung gehört, falls eine existiert, ansonsten null.
     * @throws SQLException falls ein Fehler bei den Datenbankoperationen aufgetreten ist.
     */
    public Rechnung getRechnung(Bestellung bestellung) throws SQLException {
        PreparedStatement stmt = this.db.prepare("SELECT * FROM rechnung WHERE bestellnummer=?");
        stmt.setInt(1, bestellung.getNr());

        ResultSet set = stmt.executeQuery();

        if (!set.next()) return null;

        return this.createRechnung(set, bestellung);
    }

    /**
     * Dient der Erstellung einer Rechnung.
     *
     * @param set        die Daten aus der Datenbank.
     * @param bestellung gibt die Bestellung an, zu der eine Rechnung erstellt werden soll.
     * @return aus den Daten der Datenbank wird eine neue Rechnung erstellt.
     * @throws SQLException falls ein Fehler bei den Datenbankoperationen aufgetreten ist.
     */
    private Rechnung createRechnung(ResultSet set, Bestellung bestellung) throws SQLException {
        return new Rechnung(set.getInt("nr"), bestellung, set.getDate("datum"), set.getBoolean("offen"));
    }

    /**
     * Setzt das Attribut offen in der angegebenen Rechnung in der Datenbank.
     * Wenn eine Änderung vorgenommen wurde, wird die Instanzvariable "offen" des Rechnung-Objekts ebenfalls gesetzt.
     * Dies stellt sicher, dass die Instanzen den Stand der Datenbank widerspiegeln.
     *
     * @param rechnung die Rechnung die verändert werden soll in der Datenbank
     * @param offen
     * @return true, falls die geänderte Anzahl an Zeilen nicht 0 ist.
     * @throws SQLException falls ein Fehler bei den Datenbankoperationen aufgetreten ist.
     */
    public boolean setOffen(Rechnung rechnung, boolean offen) throws SQLException {
        PreparedStatement stmt = this.db.prepare("UPDATE rechnung SET offen=? WHERE nr=?");
        stmt.setBoolean(1, offen);
        stmt.setInt(2, rechnung.getNr());

        boolean update = stmt.executeUpdate() != 0;

        if (update) rechnung.setOffen(offen);

        return update;
    }
}