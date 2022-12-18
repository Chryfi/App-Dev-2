package main.java.de.appdev2.server.database.tables;

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
        PreparedStatement stmt = this.db.prepare("INSERT INTO rechnung (nr, datum, offen, bestellung) VALUES (?, ?, ?, ?) RETURNING rechnung.nr");

        stmt.setInt(1, entity.getNr());
        stmt.setTimestamp(2, entity.getDatum());
        stmt.setBoolean(3, entity.getOffen());
        stmt.setBestellung(4, entity.getBestellung());

        ResultSet nr = stmt.executeQuery();

        if (!nr.next()) return false;

        entity.setNr(nr.getInt("nr"));

        return true;
    }

    public Rechnung getRechnung(int nr) throws SQLException {
        PreparedStatement stmt = this.db.prepare("SELECT * FROM ware WHERE = ?");

        stmt.setInt(1, nr);

        ResultSet set = stmt.executeQuery();

        if (!set.next()) return null;

        Rechnung rechnung = new Rechnung(set.getInt("nr"), set.getTimestamp("datum"),
                set.getBoolean("offen"), set.getBestellung("bestellung"));

        return rechnung;
    }

}
