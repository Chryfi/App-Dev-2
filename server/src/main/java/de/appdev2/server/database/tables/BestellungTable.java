package main.java.de.appdev2.server.database.tables;

import main.java.de.appdev2.server.database.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BestellungTable extends EntityTable<Bestellung> {
    public BestellungTable(Database db) {
        super(db);
    }

    @Override
    public boolean insert(Bestellung entity) throws SQLException {
        PreparedStatement stmnt = this.db.prepare("INSERT INTO bestellung (bestellnummer, lieferdatum, lieferantennr) VALUES (?,?,?)");
        stmnt.setInt(1, entity.getNr());
        stmnt.setTimestamp(2, entity.getLieferdatum());
        stmnt.setInt(3, entity.getLieferant().getNummer());//TODO austauschen

        return stmnt.executeUpdate() != 0;
    }
}
