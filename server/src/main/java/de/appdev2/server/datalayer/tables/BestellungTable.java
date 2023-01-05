package main.java.de.appdev2.server.datalayer.tables;

import main.java.de.appdev2.entities.Bestellung;
import main.java.de.appdev2.entities.Lieferant;
import main.java.de.appdev2.server.datalayer.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Konkrete Implementation für {@link EntityTable} für die Datenbank Tabelle "bestellung" mit dem Entitätstypen {@link Bestellung}.
 */
public class BestellungTable extends EntityTable<Bestellung> {

    public BestellungTable(Database db) {
        super(db);
    }

    @Override
    public boolean insert(Bestellung entity) throws SQLException {
        PreparedStatement stmnt = this.db.prepare("INSERT INTO bestellung (bestellnummer, lieferdatum, lieferantennr) VALUES (?,?,?)");
        stmnt.setInt(1, entity.getNr());
        stmnt.setDate(2, entity.getLieferdatum());
        stmnt.setInt(3, entity.getLieferant().getNr());

        return stmnt.executeUpdate() != 0;
    }

    /**
     * Liest die Bestellung aus der Datenbank aus.
     *
     * @param bestellnr
     * @param lieferantennr
     * @return die Bestellung oder null, wenn es keine solche Bestellung in der Datenbank gibt.
     * @throws SQLException falls ein Fehler bei den Datenbankoperationen aufgetreten ist.
     */
    public Bestellung getBestellung(int bestellnr, int lieferantennr) throws SQLException {
        PreparedStatement stmt = this.db.prepare("SELECT * FROM bestellung WHERE bestellnummer = ? AND lieferantennr = ?");
        stmt.setInt(1, bestellnr);
        stmt.setInt(2, lieferantennr);

        ResultSet set = stmt.executeQuery();

        if (!set.next()) return null;

        /* erstelle zuerst den Lieferanten, um die Assoziation zu erfüllen */
        Lieferant lieferant = this.db.getLieferantTable().getLieferant(lieferantennr);

        return new Bestellung(bestellnr, set.getDate("lieferdatum"), lieferant);
    }
}
