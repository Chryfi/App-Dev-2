package main.java.de.appdev2.entities;

import java.io.Serializable;

/**
 * Repräsentiert die Datenbank Beziehung WarenBestellung
 */
public class WarenBestellung implements Serializable {

    private Bestellung bestellung;
    private Ware ware;
    private int bestellteMenge;
    private int gelieferteMenge;

    public WarenBestellung(Ware ware, Bestellung bestellung, int bestellteMenge, int gelieferteMenge) {
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

    public int getBestellteMenge() {
        return this.bestellteMenge;
    }

    public void setBestellteMenge(int bestellteMenge) {
        this.bestellteMenge = bestellteMenge;
    }

    public int getGelieferteMenge() {
        return this.gelieferteMenge;
    }

    public void setGelieferteMenge(int gelieferteMenge) {
        this.gelieferteMenge = gelieferteMenge;
    }

    @Override
    public String toString() {
        return "WarenBestellung[bestellung = " + this.bestellung + ", ware = " + this.ware
                + ", bestellteMenge = " + this.bestellteMenge + ", gelieferteMenge = " + this.gelieferteMenge + "]";
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj) || obj instanceof WarenBestellung wb && this.bestellung.equals(wb.bestellung)
                && this.ware.equals(wb.ware) && this.bestellteMenge == wb.bestellteMenge
                && this.gelieferteMenge == wb.gelieferteMenge;
    }
}
