package main.java.de.appdev2.server.datalayer.tables;

import main.java.de.appdev2.entities.Bestellung;
import main.java.de.appdev2.entities.Rechnung;
import main.java.de.appdev2.entities.Ware;
import main.java.de.appdev2.entities.WarenBestellung;
import main.java.de.appdev2.server.datalayer.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * Konkrete Implementation für {@link EntityTable} für die Datenbank Tabelle "ware_bestellung" mit dem Entitätstypen {@link WarenBestellung}.
 */
public class WarenBestellungTable extends EntityTable<WarenBestellung> {

    public WarenBestellungTable(Database db) {
        super(db);
    }

    @Override
    public boolean insert(WarenBestellung entity) throws SQLException {
        PreparedStatement stmt = this.db.prepare(
                "INSERT INTO ware_bestellung (warennummer, bestellnummer, lieferantennr, bestellte_menge, gelieferte_menge) VALUES (?,?,?,?,?)");
        stmt.setInt(1, entity.getWare().getNr());
        stmt.setInt(2, entity.getBestellung().getNr());
        stmt.setInt(3, entity.getBestellung().getLieferant().getNr());
        stmt.setInt(4, entity.getBestellteMenge());
        stmt.setInt(5, entity.getGelieferteMenge());

        return stmt.executeUpdate() != 0;
    }

    /**
     * Liest eine WarenBestellung aus der Datenbank aus
     *
     * @param warenNr
     * @param bestellNr
     * @param lieferantennr
     * @return eine WarenBestellung wird zurückgegeben, falls vorhanden, ansonsten null
     * @throws SQLException falls ein Fehler bei den Datenbankoperationen aufgetreten ist.
     */
    public WarenBestellung getWarenBestellung(int warenNr, int bestellNr, int lieferantennr) throws SQLException {
        PreparedStatement stmt = this.db.prepare("SELECT * FROM ware_bestellung WHERE warennummer = ? AND bestellnummer = ? AND lieferantennr = ?");
        stmt.setInt(1, warenNr);
        stmt.setInt(2, bestellNr);
        stmt.setInt(3, lieferantennr);

        ResultSet set = stmt.executeQuery();

        if (!set.next()) return null;

        /* erstelle die Ware und die Bestellung um die Assoziation zu erfüllen */
        Ware ware = this.db.getWareTable().getWare(warenNr);
        Bestellung bestellung = this.db.getBestellungTable().getBestellung(bestellNr, lieferantennr);

        return this.createWarenBestellung(set, ware, bestellung);
    }

    /**
     * Liest alle WarenBestellungen aus, die zu der angegebenen Bestellung gehören.
     *
     * @param bestellung die Bestellung zu der die WarenBestellungen gehören.
     * @return ein Set mit WarenBestellungen. Doppelte Werte machen hier fachlich keinen Sinn, daher Set.
     * @throws SQLException falls ein Fehler bei den Datenbankoperationen aufgetreten ist.
     */
    public Set<WarenBestellung> getWarenBestellungen(Bestellung bestellung) throws SQLException {
        PreparedStatement stmt = this.db.prepare("SELECT * FROM ware_bestellung WHERE bestellnummer=? AND lieferantennr=?");
        stmt.setInt(1, bestellung.getNr());
        stmt.setInt(2, bestellung.getLieferant().getNr());

        ResultSet set = stmt.executeQuery();

        Set<WarenBestellung> bestellungen = new HashSet<>();

        while (set.next()) {
            Ware ware = this.db.getWareTable().getWare(set.getInt("warennummer"));

            bestellungen.add(this.createWarenBestellung(set, ware, bestellung));
        }

        return bestellungen;
    }

    /**
     * Dient der Erstellung eines WarenBestellung-Objekts.
     *
     * @param set die Daten aus der Datenbank.
     * @param bestellung gibt die Bestellung an, die die Ware bestellt hat.
     * @param ware gibt die Ware an, die bestellt wurde.
     * @return aus den Daten der Datenbank wird eine WarenBestellung erzeugt.
     * @throws SQLException falls ein Fehler bei den Datenbankoperationen aufgetreten ist.
     */
    private WarenBestellung createWarenBestellung(ResultSet set, Ware ware, Bestellung bestellung) throws SQLException {
        return new WarenBestellung(ware, bestellung, set.getInt("bestellte_menge"), set.getInt("gelieferte_menge"));
    }

    /**
     * Setzt die gelieferte Menge der angegebenen WarenBestellung in der Datenbank.
     * Wenn eine Änderung in der Datenbank ausgeführt werden konnte, wird die Instanzvariable "gelieferteMenge"
     * des WarenBestellung-Objekts ebenfalls gesetzt. Dies stellt sicher, dass die Instanzen
     * den Stand der Datenbank widerspiegeln.
     *
     * @param warenBestellung
     * @return true, wenn das Update durchgeführt werden konnte.
     * @param gelieferteMenge
     * @throws SQLException falls ein Fehler bei den Datenbankoperationen aufgetreten ist.
     */
    public boolean setGelieferteMenge(WarenBestellung warenBestellung, int gelieferteMenge) throws SQLException {
        PreparedStatement stmt = this.db.prepare("UPDATE ware_bestellung SET gelieferte_menge = ?" +
                                                       "WHERE warennummer = ? AND bestellnummer = ? AND lieferantennr = ?");
        stmt.setInt(1, gelieferteMenge);
        stmt.setInt(2, warenBestellung.getWare().getNr());
        stmt.setInt(3, warenBestellung.getBestellung().getNr());
        stmt.setInt(4, warenBestellung.getBestellung().getLieferant().getNr());

        boolean update = stmt.executeUpdate() != 0;

        if (update) warenBestellung.setGelieferteMenge(gelieferteMenge);

        return update;
    }
}
