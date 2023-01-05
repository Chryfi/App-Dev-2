package main.java.de.appdev2.service;

import main.java.de.appdev2.entities.WarenBestellung;
import main.java.de.appdev2.exceptions.DataBaseException;
import main.java.de.appdev2.exceptions.IllegalInputException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

/**
 * Prozess für den Wareneingang.
 */
public interface IWarenEingang extends Remote {

    /**
     * Prüft, ob die Bestellung vom Lieferanten existiert.
     *
     * @param bestellNr
     * @param lieferantenNr
     * @return ein Set mit den WarenBestellungen der gesuchten Bestellnummer und Lieferantennummer.
     *         Wenn keine Bestellung existiert, wird null zurückgegeben.
     * @throws RemoteException
     * @throws DataBaseException Falls ein Fehler bei Datenbankoperationen auftritt.
     */
    Set<WarenBestellung> checkBestellung(int bestellNr, int lieferantenNr) throws RemoteException, DataBaseException;

    /**
     * Prüft, ob die Qualität der gegebenen Waren ausreichend ist. Null Werte für Booleans sind hier nicht zulässig.
     *
     * @param qualitaet eine Map mit den WarenBestellungen und der jeweiligen Qualität:
     *                  true = Qualität ist gut, false = Qualität ist schlecht.
     * @return true, wenn die Qualität ausreichend ist und mit dem Wareneingangsprozess fortgefahren werden kann.
     *         Bei false wird die Annahme verweigert.
     * @throws RemoteException
     * @throws IllegalInputException Falls eine Eingabe falsch ist, z.B. null für Boolean.
     * @throws DataBaseException Falls ein Fehler bei Datenbankoperationen auftritt.
     */
    boolean checkQualitaet(Map<WarenBestellung, Boolean> qualitaet) throws RemoteException, IllegalInputException, DataBaseException;

    /**
     * Nimmt die Waren an, trägt die gelieferten Stückzahlen bei den WarenBestellungen ein und updatet die Stückzahlen der Waren.
     * Am Ende wird der Status der Rechnung auf offen=false gesetzt.
     *
     * @param stueckzahlen eine Map mit den WarenBestellungen und der jeweiligen tatsächlich gelieferten Stückzahl.
     * @throws RemoteException
     * @throws IllegalInputException Falls eine Eingabe falsch ist z.B. "null" für Integer
     * @throws DataBaseException Falls ein Fehler bei Datenbankoperationen auftritt.
     */
    void warenAnnahme(Map<WarenBestellung, Integer> stueckzahlen) throws RemoteException, IllegalInputException, DataBaseException;

}
