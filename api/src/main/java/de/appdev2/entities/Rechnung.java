package main.java.de.appdev2.entities;

import java.io.Serializable;
import java.sql.Date;

public class Rechnung implements Serializable {

    private Bestellung bestellung;
    private int nr;
    private Date datum;
    private boolean offen;

    /**
     * Notwendige Daten um in die Datenbank einzufügen.
     * Der Primärschlüssel hat "Auto Increment", daher muss er hier nicht definiert werden.
     *
     * @param bestellung
     * @param datum
     * @param offen
     */
    public Rechnung(Bestellung bestellung, Date datum, boolean offen) {
        this(-1, bestellung, datum, offen);
    }

    public Rechnung(int nr, Bestellung bestellung, Date datum, boolean offen) {
        this.nr = nr;
        this.datum = datum;
        this.offen = offen;
        this.bestellung = bestellung;
    }

    public Bestellung getBestellung() {
        return this.bestellung;
    }

    public void setBestellung(Bestellung bestellung) {
        this.bestellung = bestellung;
    }

    public int getNr() {
        return this.nr;
    }

    public void setNr(int nr) {
        this.nr = nr;
    }

    public Date getDatum() {
        return this.datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public boolean isOffen() {
        return this.offen;
    }

    public void setOffen(boolean offen) {
        this.offen = offen;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj) || obj instanceof Rechnung rechnung && this.bestellung.equals(rechnung.bestellung)
                && this.nr == rechnung.nr && this.datum.equals(rechnung.datum) && this.offen == rechnung.offen;
    }

    @Override
    public String toString() {
        return "Rechnung[nr = " + this.nr + ", datum = " + this.datum
                + ", offen = " + this.offen + ", bestellung = " + this.bestellung +"]";
    }
}
