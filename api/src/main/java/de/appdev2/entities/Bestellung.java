package main.java.de.appdev2.entities;

import java.io.Serializable;
import java.sql.Date;

/**
 * Repräsentiert die Datenbank Entität Bestellung
 */
public class Bestellung implements Serializable {

    private int nr;
    private Date lieferdatum;
    private Lieferant lieferant;

    public Bestellung(int nr, Date lieferdatum, Lieferant lieferant) {
        this.nr = nr;
        this.lieferdatum = lieferdatum;
        this.lieferant = lieferant;
    }

    public int getNr() {
        return this.nr;
    }

    public Date getLieferdatum() {
        return this.lieferdatum;
    }

    public void setLieferdatum(Date lieferdatum) {
        this.lieferdatum = lieferdatum;
    }

    public Lieferant getLieferant() {
        return this.lieferant;
    }

    @Override
    public String toString() {
        return "Bestellung[nr = " + this.nr + ", lieferdatum = " + this.lieferdatum + ", lieferant = " + this.lieferant + "]";
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj) || obj instanceof Bestellung bestellung && this.nr == bestellung.nr
                && this.lieferdatum.equals(bestellung.lieferdatum) && this.lieferant.equals(bestellung.lieferant);
    }
}
