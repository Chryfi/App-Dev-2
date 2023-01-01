package main.java.de.appdev2.client.dialogue;

import main.java.de.appdev2.entities.WarenBestellung;
import main.java.de.appdev2.exceptions.DataBaseException;
import main.java.de.appdev2.exceptions.IllegalInputException;
import main.java.de.appdev2.service.IWarenEingang;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WarenEingangDialogue {
    private IWarenEingang service;
    private DialogueInteger<?> bestellungInput;
    private DialogueInteger<?> lieferantInput;
    private DialogueMap<WarenBestellung, Boolean> qualityInput;
    private DialogueMap<WarenBestellung, Integer> stueckzahl;
    private boolean debug;

    public WarenEingangDialogue(IWarenEingang service) {
        this(service, false);
    }

    public WarenEingangDialogue(IWarenEingang service, boolean debug) {
        this.service = service;
        this.debug = debug;

        this.bestellungInput = new DialogueInteger("Gibt eine Bestellnummer ein:",
                "Deine Eingabe ist im einem falschen Format!", 0);
        this.lieferantInput = new DialogueInteger("Gibt eine Lieferantennummer ein:",
                "Deine Eingabe ist im einem falschen Format!", 0);


        DialogueBoolean<WarenBestellung> qualityInput = new DialogueBoolean<>((wb) -> {
            return "Gib die Qualität für die Ware: " + wb.getWare().getNr()
                    + " | " + wb.getWare().getBezeichnung() + " ein.\ngut / schlecht";
        }, (obj) -> "Falsche Eingabe: gut oder schlecht", new HashSet<>(Arrays.asList("gut")), new HashSet<>(Arrays.asList("gut", "schlecht")));

        this.qualityInput = new DialogueMap<>("Geben Sie die Qualität für die Waren ein.", qualityInput);


        DialogueInteger<WarenBestellung> stueckzahlInput = new DialogueInteger<>((wb) -> {
            return "Geben Sie die gelieferte Stückzahl für Ware " + wb.getWare().getNr() + " ein.";
        }, (wb) -> "Falsche Eingabe. Geben Sie erneut die Stückzahl für die Ware " + wb.getWare().getNr() + " ein.", 0);

        this.stueckzahl = new DialogueMap<>("Geben Sie die Stückzahlen für die Waren ein.", stueckzahlInput);
    }

    public void run() {
        Set<WarenBestellung> warenBestellungen;

        /*
         * Gebe die Bestellnr und Lieferantennr ein um zu prüfen, ob sie existiert.
         */
        int bestellnr = this.bestellungInput.input();
        int lieferantnr = this.lieferantInput.input();

        try {
            warenBestellungen = this.service.checkBestellung(bestellnr, lieferantnr);
        } catch (RemoteException e) {
            this.outputRemoteException(e);
            return;
        } catch (DataBaseException e) {
            this.outputDatabaseException(e);
            return;
        }

        if (warenBestellungen == null) {
            System.out.println("Bestellung existiert nicht. Warenannahme wird verweigert.");
            return;
        }


        /*
         * Gebe die Qualität für die Waren ein.
         * Falls der Server IllegalInputException wirft, muss nochmal eingegeben werden.
         */
        this.qualityInput.setKeys(warenBestellungen);
        boolean qualityCheck;
        while (true) {
            try {
                Map<WarenBestellung, Boolean> quality = this.qualityInput.input();
                qualityCheck = this.service.checkQualitaet(quality);

                break;
            } catch (RemoteException e) {
                this.outputRemoteException(e);
                return;
            } catch (IllegalInputException e) { //beginne den Eingabeprozess erneut
                this.outputIllegalInputException(e);
            } catch (DataBaseException e) {
                this.outputDatabaseException(e);
                return;
            }
        }

        if (!qualityCheck) {
            System.out.println("Annahme verweigert, da die Qualität nicht ausreichend ist!");
            return;
        }


        /*
         * Gebe die Stückzahlen für die Waren ein und nehme die Waren an.
         * Falls der Server IllegalInputException wirft, muss nochmal eingegeben werden.
         */
        this.stueckzahl.setKeys(warenBestellungen);
        while (true) {
            try {
                Map<WarenBestellung, Integer> stueckzahl = this.stueckzahl.input();

                this.service.warenAnnahme(stueckzahl);
                System.out.println("Annahme ausgeführt.");

                break;
            } catch (RemoteException e) {
                this.outputRemoteException(e);
                return;
            } catch (IllegalInputException e) { //beginne den Eingabeprozess erneut
                this.outputIllegalInputException(e);
            } catch (DataBaseException e) {
                this.outputDatabaseException(e);
                return;
            }
        }
    }

    private void outputDatabaseException(DataBaseException e) {
        System.out.println("Ein Datenbank Fehler ist aufgetreten");
        this.printException(e);
    }

    private void outputIllegalInputException(IllegalInputException e) {
        System.out.println("Der Server hat eine falsche Eingabe erkannt:");
        System.out.println(e.getMessage());
        System.out.println("\nEingabeprozess wird neugestartet...\n");
    }

    private void outputRemoteException(RemoteException e) {
        System.out.println("Ein Netzwerk Fehler ist aufgetreten.");
        this.printException(e);
    }

    private void printException(Exception e) {
        if (this.debug) {
            e.printStackTrace();
        } else {
            System.out.println("Fehlernachricht: " + e.getMessage());
        }
    }
}