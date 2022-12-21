package main.java.de.appdev2.server.application;

import main.java.de.appdev2.entities.Bestellung;
import main.java.de.appdev2.entities.WarenBestellung;
import main.java.de.appdev2.exceptions.DataBaseException;
import main.java.de.appdev2.server.database.Database;
import main.java.de.appdev2.server.database.tables.WarenBestellungTable;
import main.java.de.appdev2.service.IWarenEingang;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

public class WarenEingangImpl extends UnicastRemoteObject implements IWarenEingang {
    private Database db;

    public WarenEingangImpl(Database db) throws RemoteException {
        this.db = db;
    }

    @Override
    public Set<WarenBestellung> checkBestellung(int bestellNr, int lieferantenNr) throws RemoteException, DataBaseException {
        Bestellung bestellung;

        try {
            bestellung = this.db.getBestellungTable().getBestellung(bestellNr, lieferantenNr);
        } catch (SQLException e) {
            e.printStackTrace();

            throw new DataBaseException("Etwas ist schiefgelaufen beim Abfragen der Bestellung!");
        }

        if (bestellung == null) {
            return null;
        }

        Set<WarenBestellung> warenBestellungen;

        try {
            warenBestellungen = db.getWarenBestellungTable().getWarenBestellungen(bestellung);
        } catch (SQLException e) {
            e.printStackTrace();

            throw new DataBaseException("Etwas ist schiefgelaufen beim Abfragen der Waren Beziehungen!");
        }

        return warenBestellungen;
    }

    @Override
    public boolean checkQualitaet(Map<WarenBestellung, Boolean> qualitaet) throws RemoteException {
        return true; //TODO
    }

    @Override
    public void warenAnnahme(Map<WarenBestellung, Integer> stueckzahlen) throws RemoteException {
        //TODO
    }
}
