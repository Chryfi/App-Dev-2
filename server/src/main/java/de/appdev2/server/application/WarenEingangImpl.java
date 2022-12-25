package main.java.de.appdev2.server.application;

import main.java.de.appdev2.entities.Bestellung;
import main.java.de.appdev2.entities.WarenBestellung;
import main.java.de.appdev2.exceptions.DataBaseException;
import main.java.de.appdev2.exceptions.IllegalInputException;
import main.java.de.appdev2.server.database.Database;
import main.java.de.appdev2.server.database.tables.WarenBestellungTable;
import main.java.de.appdev2.service.IWarenEingang;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.*;

//TODO WarenEingangs Prozess Status hier speichern damit Client nicht einfach eine andere Methode aufrufen kann und sofort Waren annimmt???
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
    public boolean checkQualitaet(Map<WarenBestellung, Boolean> qualitaet) throws RemoteException, IllegalInputException, DataBaseException {
        for (Map.Entry<WarenBestellung, Boolean> entry : qualitaet.entrySet()) {
            Boolean quality = entry.getValue();
            WarenBestellung wb = entry.getKey();

            if (quality == null) {
                throw new IllegalInputException("Der Boolean Wert von der Warenbestellung" + wb + " ist null!");
            } else if (!quality) {
                try {
                    this.annahmeVerweigern(qualitaet.keySet());
                } catch (SQLException e) {
                    throw new DataBaseException("Heute fühlt sich die Datenbank als Katze und nicht als Datenbank, deshalb geht nicht!");
                }

                return false;
            }
        }

        return true;
    }

    public void annahmeVerweigern(Set<WarenBestellung> warenBestellungen) throws SQLException{
        for (WarenBestellung entry : warenBestellungen) {
            entry.setGelieferteMenge(0);
            this.db.getWarenBestellungTable().setGelieferteMenge(entry, 0);
        }
    }

    @Override
    public void warenAnnahme(Map<WarenBestellung, Integer> stueckzahlen) throws RemoteException {
        //TODO
    }
}
