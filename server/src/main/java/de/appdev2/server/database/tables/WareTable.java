package main.java.de.appdev2.server.database.tables;

import main.java.de.appdev2.entities.Ware;
import main.java.de.appdev2.server.database.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WareTable extends EntityTable<Ware> {

    public WareTable(Database db) {
        super(db);
    }

    @Override
    public boolean insert(Ware entity) throws SQLException {
        PreparedStatement stmt = this.db.prepare("INSERT INTO ware (stueckpreis, bezeichnung, stueckzahl) VALUES (?,?,?) RETURNING ware.nr");

        stmt.setFloat(1, entity.getStueckpreis());
        stmt.setString(2, entity.getBezeichnung());
        stmt.setInt(3, entity.getStueckzahl());

        ResultSet nr = stmt.executeQuery();

        if (!nr.next()) return false;

        entity.setNr(nr.getInt("nr"));

        return true;
    }

    public Ware getWare(int nr) throws SQLException {
        PreparedStatement stmt = this.db.prepare("SELECT * FROM ware WHERE=?");

        stmt.setInt(1, nr);

        ResultSet set = stmt.executeQuery();

        if (!set.next()) return null;

        return new Ware(set.getInt("nr"), set.getFloat("stueckpreis"),
                set.getInt("stueckzahl"), set.getString("bezeichnung"));
    }
}
