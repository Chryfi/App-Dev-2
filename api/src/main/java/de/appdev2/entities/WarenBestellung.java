package main.java.de.appdev2.entities;

import main.java.de.appdev2.server.database.tables.WarenBestellungTable;

public class WarenBestellung {

    private Bestellung bestellung;
    private Ware ware;
    private int bestellteMenge;
    private int gelieferteMenge;

    public WarenBestellung(Ware ware, Bestellung bestellung, int bestellteMenge, int gelieferteMenge){
        this.bestellteMenge = bestellteMenge;
        this.gelieferteMenge = gelieferteMenge;
        this.ware = ware;
        this.bestellung = bestellung;
    }

    public Bestellung getBestellung() {
        return this.bestellung;
    }

    public void setBestellung(Bestellung bestellung) {
        this.bestellung = bestellung;
    }

    public Ware getWare() {
        return this.ware;
    }

    public void setWare(Ware ware) {
        this.ware = ware;
    }

    public int getBestellteMenge(){
        return this.bestellteMenge;
    }

    public void setBestellteMenge(int bestellteMenge) {
        this.bestellteMenge = bestellteMenge;
    }

    public int getGelieferteMenge(){
        return this.gelieferteMenge;
    }

    public void setGelieferteMenge(int gelieferteMenge) {
        this.gelieferteMenge = gelieferteMenge;
    }
}
