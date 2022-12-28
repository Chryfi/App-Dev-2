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
     * Prüfe, ob die Bestellung vom Lieferanten existiert.
     * @param bestellNr
     * @param lieferantenNr
     * @return ein Set mit den Waren und Bestellungen der gesuchten Bestellnummer und Lieferantennummer.
     *         Wenn keine Bestellung existiert, wird null zurückgegeben.
     * @throws RemoteException
     * @throws DataBaseException Falls ein Fehler bei Datenbank Operationen auftritt.
     */
    Set<WarenBestellung> checkBestellung(int bestellNr, int lieferantenNr) throws RemoteException, DataBaseException;

    /**
     * Prüfe ob die Qualität der gegebenen Waren ausreichend ist. Null Werte für Booleans sind hier nicht zulässig.
     * @param qualitaet eine map mit den Waren Bestellungen und der jeweiligen Qualität: true = Qualität gut, false = Qualität schlecht.
     * @return true wenn die Qualitaet ausreichend ist und mit dem Wareneingangsprozess fortgefahren werden kann.
     *         Bei false wird die Annahme verweigert.
     * @throws RemoteException
     * @throws IllegalInputException Falls eine Eingabe falsch ist z.B. null für Boolean
     * @throws DataBaseException Falls ein Fehler bei Datenbank Operationen auftritt.
     */
    boolean checkQualitaet(Map<WarenBestellung, Boolean> qualitaet) throws RemoteException, IllegalInputException, DataBaseException;

    /**
     * Nehme die Waren an und trage die gelieferten Stückzahlen ein.
     * @param stueckzahlen eine map mit den Waren Bestellungen und der jeweiligen tatsächlich gelieferten Stückzahl.
     * @throws RemoteException
     * @throws IllegalInputException Falls eine Eingabe falsch ist z.B. "null" für Integer
     * @throws DataBaseException Falls ein Fehler bei Datenbank Operationen auftritt.
     */
    void warenAnnahme(Map<WarenBestellung, Integer> stueckzahlen) throws RemoteException, IllegalInputException, DataBaseException;

}
