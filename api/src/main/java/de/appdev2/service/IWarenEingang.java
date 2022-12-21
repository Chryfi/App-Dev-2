package main.java.de.appdev2.service;

import main.java.de.appdev2.entities.WarenBestellung;
import main.java.de.appdev2.exceptions.DataBaseException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.Set;

/**
 * Prozess für den Wareneingang.
 */
public interface IWarenEingang extends Remote {

    /**
     * Prüfe ob die Bestellung vom Lieferanten existiert
     * @param bestellNr
     * @param lieferantenNr
     * @return ein Set mit den Waren und Bestellungen der gesuchten Bestellnummer und Lieferantennummer.
     *         Wenn keine Bestellung existiert, wird null zurückgegeben.
     * @throws RemoteException
     */
    Set<WarenBestellung> checkBestellung(int bestellNr, int lieferantenNr) throws RemoteException, DataBaseException;

    /**
     * Prüfe ob die Qualitaet der gegebenen Waren ausreichend ist.
     * @param qualitaet
     * @return true wenn die Qualitaet ausreichend ist und mit dem Wareneingangsprozess fortgefahren werden kann.
     *         Bei false wird die Annahme verweigert.
     * @throws RemoteException
     */
    boolean checkQualitaet(Map<WarenBestellung, Boolean> qualitaet) throws RemoteException;

    /**
     * Nehme die Waren an und trage die gelieferten Stückzahlen ein.
     * @param stueckzahlen
     * @throws RemoteException
     */
    void warenAnnahme(Map<WarenBestellung, Integer> stueckzahlen) throws RemoteException;

}
