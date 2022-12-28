package main.java.de.appdev2.server.database.tables;

import main.java.de.appdev2.entities.Bestellung;
import main.java.de.appdev2.entities.Rechnung;
import main.java.de.appdev2.server.database.Database;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RechnungTable extends EntityTable<Rechnung> {

    public RechnungTable(Database db) {
        super(db);
    }

    public boolean insert(Rechnung entity) throws SQLException {
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

    public Rechnung getRechnung(int nr) throws SQLException {
        PreparedStatement stmt = this.db.prepare("SELECT * FROM rechnung WHERE nr=?");

        stmt.setInt(1, nr);

        ResultSet set = stmt.executeQuery();

        if (!set.next()) return null;

        Bestellung bestellung = this.db.getBestellungTable().getBestellung(set.getInt("bestellnummer"), set.getInt("lieferantennr"));

        return this.createRechnung(set, bestellung);
    }

    public Rechnung getRechnung(Bestellung bestellung) throws SQLException {
        PreparedStatement stmt = this.db.prepare("SELECT * FROM rechnung WHERE bestellnummer=?");
        stmt.setInt(1, bestellung.getNr());

        ResultSet set = stmt.executeQuery();

        if (!set.next()) return null;

        return this.createRechnung(set, bestellung);
    }

    private Rechnung createRechnung(ResultSet set, Bestellung bestellung) throws SQLException {
        return new Rechnung(set.getInt("nr"), bestellung, set.getDate("datum"), set.getBoolean("offen"));
    }

    public boolean setOffen(Rechnung rechnung, boolean offen) throws SQLException {
        PreparedStatement stmt = this.db.prepare("UPDATE rechnung SET offen=? WHERE nr=?");
        stmt.setBoolean(1, offen);
        stmt.setInt(2, rechnung.getNr());

        boolean update = stmt.executeUpdate() != 0;

        if (update) rechnung.setOffen(offen);

        return update;
    }
}
