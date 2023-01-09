package main.java.de.appdev2.client.dialogue;

import main.java.de.appdev2.entities.WarenBestellung;
import main.java.de.appdev2.exceptions.DataBaseException;
import main.java.de.appdev2.exceptions.IllegalInputException;
import main.java.de.appdev2.service.IWarenEingang;
import main.java.de.appdev2.utils.MathUtils;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Klasse, die die Nutzereingaben und Ausgaben für den Wareneingangsprozess implementiert.
 */
public class WarenEingangDialogue {
    private IWarenEingang service;

    /* Dialog Klassen für den Eingabeprozess */
    private DialogueInteger<?> bestellungInput;
    private DialogueInteger<?> lieferantInput;
    private DialogueMap<WarenBestellung, Boolean> qualityInput;
    private DialogueMap<WarenBestellung, Integer> stueckzahl;

    /**
     * Legt fest, ob der ganze Stacktrace einer Exception ausgegeben werden soll oder nur die Nachricht.
     */
    private boolean debug;

    public WarenEingangDialogue(IWarenEingang service) {
        this(service, false);
    }

    public WarenEingangDialogue(IWarenEingang service, boolean debug) {
        this.service = service;
        this.debug = debug;

        this.bestellungInput = new DialogueInteger("Geben Sie eine Bestellnummer ein:",
                "Ihre Eingabe ist in einem falschen Format!", 0);
        this.lieferantInput = new DialogueInteger("Geben Sie eine Lieferantennummer ein:",
                "Ihre Eingabe ist in einem falschen Format!", 0);


        DialogueBoolean<WarenBestellung> qualityInput = new DialogueBoolean<>((wb) -> {
            return "Geben Sie die Qualität für die Ware: " + wb.getWare().getNr()
                    + " | " + wb.getWare().getBezeichnung() + " ein.\ngut / schlecht";
        }, (obj) -> "Falsche Eingabe: gut oder schlecht", new HashSet<>(Arrays.asList("gut")), new HashSet<>(Arrays.asList("gut", "schlecht")));

        this.qualityInput = new DialogueMap<>("Geben Sie die Qualität für die Waren ein.", qualityInput);


        DialogueInteger<WarenBestellung> stueckzahlInput = new DialogueInteger<>((wb) -> {
            return "Geben Sie die gelieferte Stückzahl für die Ware " + wb.getWare().getNr() + " ein.";
        }, (wb) -> "Falsche Eingabe. Geben Sie erneut die Stückzahl für die Ware " + wb.getWare().getNr() + " ein.", 0);

        this.stueckzahl = new DialogueMap<>("Geben Sie die Stückzahlen für die Waren ein.", stueckzahlInput);
    }

    /**
     * Startet den Ablauf.
     */
    public void run() {
        Set<WarenBestellung> warenBestellungen;
        boolean qualityCheck;
        Map<WarenBestellung, Integer> stueckzahl;
        /* Linie um die verschiedenen Ausgaben-Abschnitte zu trennen */
        String line = (new String(new char[50])).replace('\0', '―');

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

        System.out.println(line);

        /*
         * Gebe die Qualität für die Waren ein.
         * Falls der Server IllegalInputException wirft, muss nochmal eingegeben werden.
         */
        this.qualityInput.setKeys(warenBestellungen);
        while (true) {
            try {
                qualityCheck = this.service.checkQualitaet(this.qualityInput.input());
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

        System.out.println(line);

        /*
         * Gebe die Stückzahlen für die Waren ein und nehme die Waren an.
         * Falls der Server IllegalInputException wirft, muss nochmal eingegeben werden.
         */
        this.stueckzahl.setKeys(warenBestellungen);
        while (true) {
            try {
                this.service.warenAnnahme(stueckzahl = this.stueckzahl.input());
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

        System.out.println(line);

        /* Gebe den Preis und die Annahme-Art aus */

        float preisSus = 0;
        boolean teilAnnahme = false;
        for (Map.Entry<WarenBestellung, Integer> geliefert : stueckzahl.entrySet()) {
            WarenBestellung wb = geliefert.getKey();
            Integer gelieferteMenge = geliefert.getValue();

            if (gelieferteMenge < wb.getBestellteMenge()) {
                teilAnnahme = true;
            }

            preisSus += wb.getWare().getStueckpreis() * gelieferteMenge;
        }

        preisSus = MathUtils.round(preisSus, 2);

        if (teilAnnahme) {
            System.out.println("Teilannahme ausgeführt.");
            System.out.println("Teilzahlung veranlasst. Gesamtpreis: " + preisSus + "€");
        } else {
            System.out.println("Annahme ausgeführt.");
            System.out.println("Zahlung veranlasst. Gesamtpreis: " + preisSus + "€");
        }
    }

    /**
     * @param e
     */
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
