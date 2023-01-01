package main.java.de.appdev2.server.application;

import main.java.de.appdev2.entities.Bestellung;
import main.java.de.appdev2.entities.Rechnung;
import main.java.de.appdev2.entities.Ware;
import main.java.de.appdev2.entities.WarenBestellung;
import main.java.de.appdev2.exceptions.DataBaseException;
import main.java.de.appdev2.exceptions.IllegalInputException;
import main.java.de.appdev2.server.database.Database;
import main.java.de.appdev2.service.IWarenEingang;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.*;

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
            warenBestellungen = this.db.getWarenBestellungTable().getWarenBestellungen(bestellung);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataBaseException("Etwas ist beim Abfragen der Waren Beziehungen schiefgelaufen!");
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
                    e.printStackTrace();
                    throw new DataBaseException("Etwas ist beim Verweigern in der Datenbank schiefgelaufen!");
                }

                return false;
            }
        }

        return true;
    }

    public void annahmeVerweigern(Set<WarenBestellung> warenBestellungen) throws SQLException, DataBaseException {
        for (WarenBestellung entry : warenBestellungen) {
            entry.setGelieferteMenge(0);

            if (!this.db.getWarenBestellungTable().setGelieferteMenge(entry, 0)) {
                throw new DataBaseException("Datenbank hat die gelieferte Menge nicht gesetzt.");
            }
        }
    }

    @Override
    public void warenAnnahme(Map<WarenBestellung, Integer> stueckzahlen) throws RemoteException, IllegalInputException, DataBaseException {
        for (Map.Entry<WarenBestellung, Integer> entry : stueckzahlen.entrySet()) {
            Integer geliefert = entry.getValue();
            WarenBestellung wb = entry.getKey();

            if (geliefert == null) {
                throw new IllegalInputException("Gelieferte Menge kann nicht = \"null\" sein! Geben Sie 0 ein.");
            }

            try {
                if (!this.db.getWarenBestellungTable().setGelieferteMenge(wb, geliefert)) {
                    throw new DataBaseException("Die Datenbank hat die gelieferte Menge nicht aktualisiert.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new DataBaseException("Etwas ist beim setzen der gelieferten Menge schiefgelaufen.");
            }

            try {
                /*
                 * Zur Sicherheit wird aus der Datenbank die Stückzahl von der bereits existierenden Ware abgerufen,
                 * falls der Client die Stückzahl im Objekt verändert hat.
                 */
                Ware ware = this.db.getWareTable().getWare(wb.getWare().getNr());

                if (!this.db.getWareTable().updateStueckzahl(ware,ware.getStueckzahl() + geliefert)) {
                    throw new DataBaseException("Die Datenbank hat die Waren Stückzahl nicht aktualisiert.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new DataBaseException("Etwas ist beim setzen der Waren Stückzahl schiefgelaufen!");
            }

            try {
                Rechnung rechnung = this.db.getRechnungTable().getRechnung(wb.getBestellung());

                if (!this.db.getRechnungTable().setOffen(rechnung, false)) {
                    throw new DataBaseException("Die Datenbank hat die Rechnung nicht aktualisiert.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new DataBaseException("Etwas ist beim Aktualisieren der Rechnung schiefgelaufen!");
            }
        }
    }
}
