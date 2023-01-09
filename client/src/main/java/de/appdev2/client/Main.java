package main.java.de.appdev2.client;

import main.java.de.appdev2.client.dialogue.WarenEingangDialogue;

import main.java.de.appdev2.service.IWarenEingang;

public class Main {
    public static void main(String[] args) {
        Client<IWarenEingang> warenEingang = new Client<>("localhost", 1239, "katze");

        IWarenEingang service;

        /* stellt Verbindung mit Server her */
        if (!warenEingang.connectToServer() || (service = warenEingang.getStub()) == null) {
            System.out.println("Service konnte nicht verbinden!");

            return;
        }

        /* startet die Nutzereingaben */
        WarenEingangDialogue warenEingangDialogue = new WarenEingangDialogue(service);
        warenEingangDialogue.run();
    }
}
