package main.java.de.appdev2.client;

import main.java.de.appdev2.client.dialogue.DialogueString;
import main.java.de.appdev2.client.dialogue.DialogueInteger;
import main.java.de.appdev2.entities.WarenBestellung;

import main.java.de.appdev2.exceptions.DataBaseException;
import main.java.de.appdev2.exceptions.IllegalInputException;
import main.java.de.appdev2.service.IWarenEingang;

import java.rmi.RemoteException;
import java.util.*;

public class Main  {
    public static void main(String[] args) {
        Client<IWarenEingang> warenEingang = new Client<>("localhost", 1239, "katze");

        IWarenEingang service;

        if (!warenEingang.connectToServer() || (service = warenEingang.getStub()) == null) {
            System.out.println("Service konnte nicht verbinden!");

            return;
        }

        //I/O
        DialogueInteger bestellInput = new DialogueInteger("Gibt eine Bestellnummer ein:",
                "Deine Eingabe ist im einem falschen Format!", 0);

        DialogueInteger lieferantenInput = new DialogueInteger("Gibt eine Lieferantennummer ein:",
                "Deine Eingabe ist im einem falschen Format!", 0);

        int bestellnr = bestellInput.input();
        int lieferantennr = lieferantenInput.input();

        Set<WarenBestellung> warenBestellungen = null;

        try {
            warenBestellungen = service.checkBestellung(bestellnr, lieferantennr);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (DataBaseException e) {
            System.out.println("Ein Datenbank Fehler ist aufgetreten");
            System.out.println(e.getMessage());

            return;
        }

        if (warenBestellungen == null) {
            System.out.println("Bestellung existiert nicht. Warenannahme wird verweigert.");

            return;
        }
        else {
            System.out.println("Bestellung gefunden. Geben Sie die Qualität für die Waren ein. MIAU");
        }

        Map<WarenBestellung, Boolean> quality = new HashMap<>();

        for (WarenBestellung wb : warenBestellungen) {
            String prompt = "Gib die Qualität für die Ware: "
                    + wb.getWare().getNr() + " | " + wb.getWare().getBezeichnung() + " ein.\ngut / schlecht";

            DialogueString dialogue = new DialogueString(prompt, "Falsche Eingabe: gut oder schlecht.", "gut", "schlecht");

            quality.put(wb, dialogue.input().equals("gut"));
        }

        boolean qualityCheck = false;
        try {
            qualityCheck = service.checkQualitaet(quality);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (IllegalInputException e) {
            //TODO while schleife
            System.out.println("Falsche Eingabe:");
            System.out.println(e.getMessage());
        } catch (DataBaseException e) {
            System.out.println("Ein Datenbank Fehler ist aufgetreten");
            System.out.println(e.getMessage());
        }

        if (!qualityCheck) {
            System.out.println("Annahme verweigert, da die Qualität nicht ausreichend ist!");

            return;
        }

        Map<WarenBestellung, Integer> stueckzahl  = new HashMap<>();

        for (WarenBestellung wb: warenBestellungen) {
            DialogueInteger stueckzahlInput = new DialogueInteger("Gib die gelieferte Stückzahl für Ware " + wb.getWare().getNr() + " ein.",
                            "Falsche Eingabe.", 0);

            stueckzahl.put(wb, stueckzahlInput.input());
        }

        try {
            service.warenAnnahme(stueckzahl);

            System.out.println("Annahme ausgeführt.");
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (IllegalInputException e) {
            //TODO while Schleife
            System.out.println("Falsche Eingabe:");
            System.out.println(e.getMessage());
        } catch (DataBaseException e) {
            System.out.println("Ein Datenbank Fehler ist aufgetreten");
            System.out.println(e.getMessage());
        }
    }
}
