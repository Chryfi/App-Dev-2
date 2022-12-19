package main.java.de.appdev2.entities;

import java.sql.Timestamp;

public class Rechnung {

    private int nr;
    private Timestamp datum;
    private boolean offen;
    private Bestellung bestellung;

    public Rechnung(int nr, Timestamp datum, boolean offen, Bestellung bestellung) {
        this.nr = nr;
        this.datum = datum;
        this.offen = offen;
        this.bestellung = bestellung;
    }

    public int getNr() {
        return this.nr;
    }

    public void setNr(int nr) {
        this.nr = nr;
    }

    public Timestamp getDatum() {
        return this.datum;
    }

    public void setDatum(Timestamp datum) {
        this.datum = datum;
    }

    public boolean getOffen() {
        return this.offen;
    }

    public void setOffen(boolean offen) {
        this.offen = offen;
    }

    public Bestellung getBestellung() {
        return bestellung;
    }

    public void setBestellung(Bestellung bestellung) {
        this.bestellung = bestellung;
    }
}
