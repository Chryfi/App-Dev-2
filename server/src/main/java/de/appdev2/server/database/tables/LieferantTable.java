package main.java.de.appdev2.server.database.tables;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import main.java.de.appdev2.entities.Lieferant;
import main.java.de.appdev2.server.database.Database;


public class LieferantTable extends EntityTable<Lieferant> {

    public LieferantTable(Database db) {
        super(db);
    }

    @Override
    public boolean insert(Lieferant entity) throws SQLException {
        PreparedStatement stmt = this.db.prepare("INSERT INTO lieferant (name) VALUES (?) RETURNING lieferant.nr");

        stmt.setString(1, entity.getName());

        ResultSet nr = stmt.executeQuery();

        if (!nr.next()) return false;//Wenn Eintrag leer -> false

        entity.setNr(nr.getInt("nr"));

        return true;
    }

    public Lieferant getLieferant(int nr) throws SQLException {
        PreparedStatement stmt = this.db.prepare("SELECT * FROM lieferant WHERE nr=?");

        stmt.setInt(1, nr);

        ResultSet set = stmt.executeQuery();

        if (!set.next()) return null;

        return new Lieferant(set.getInt("nr"), set.getString("name"));
    }


}
