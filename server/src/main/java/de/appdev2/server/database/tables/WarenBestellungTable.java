package main.java.de.appdev2.server.database.tables;

import main.java.de.appdev2.entities.Bestellung;
import main.java.de.appdev2.entities.Rechnung;
import main.java.de.appdev2.entities.Ware;
import main.java.de.appdev2.entities.WarenBestellung;
import main.java.de.appdev2.server.database.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

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

    public WarenBestellung getWarenBestellung(int warenNr, int bestellNr, int lieferantennr) throws SQLException {
        PreparedStatement stmt = this.db.prepare("SELECT * FROM ware_bestellung WHERE warennummer = ? AND bestellnummer = ? AND lieferantennr = ?");
        stmt.setInt(1, warenNr);
        stmt.setInt(2, bestellNr);
        stmt.setInt(3, lieferantennr);

        ResultSet set = stmt.executeQuery();

        if (!set.next()) return null;

        Ware ware = this.db.getWareTable().getWare(warenNr);
        Bestellung bestellung = this.db.getBestellungTable().getBestellung(bestellNr, lieferantennr);

        return new WarenBestellung(ware, bestellung,
                set.getInt("bestellte_menge"), set.getInt("gelieferte_menge"));
    }

    public Set<WarenBestellung> getWarenBestellungen(Bestellung bestellung) throws SQLException {
        PreparedStatement stmt = this.db.prepare("SELECT * FROM ware_bestellung WHERE bestellnummer=? AND lieferantennr=?");
        stmt.setInt(1, bestellung.getNr());
        stmt.setInt(2, bestellung.getLieferant().getNr());

        ResultSet set = stmt.executeQuery();

        Set<WarenBestellung> bestellungen = new HashSet<>();

        while (set.next()) {
            Ware ware = this.db.getWareTable().getWare(set.getInt("warennummer"));

            bestellungen.add(new WarenBestellung(ware, bestellung,
                    set.getInt("bestellte_menge"), set.getInt("gelieferte_menge")));
        }

        return bestellungen;
    }
}
