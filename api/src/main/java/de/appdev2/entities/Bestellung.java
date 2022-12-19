package main.java.de.appdev2.entities;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

public class Bestellung {

    private int nr;
    private Timestamp lieferdatum;
    private Lieferant lieferant;
    private final Set<WarenBestellung> waren = new HashSet<>();
    private Rechnung rechnung;

    public Bestellung (int nr, Timestamp lieferdatum, Set<WarenBestellung> waren, Lieferant lieferant) {
        this(nr, lieferdatum, waren, lieferant, null);
    }

    public Bestellung (int nr, Timestamp lieferdatum, Set<WarenBestellung> waren, Lieferant lieferant, Rechnung rechnung) {
        this.nr = nr;
        this.lieferdatum = lieferdatum;
        this.waren.addAll(waren);
        this.lieferant = lieferant;
        this.rechnung = rechnung;
    }

    public Rechnung getRechnung() {
        return this.rechnung;
    }

    public void setRechnung(Rechnung rechnung) {
        this.rechnung = rechnung;
    }

    public void setLieferdatum(Timestamp lieferdatum) {
        this.lieferdatum = lieferdatum;
    }

    public int getNr() {
        return this.nr;
    }

    public Timestamp getLieferdatum() {
        return this.lieferdatum;
    }

    public void setNr(int nr) {
        this.nr = nr;
    }

    public void setWaren(Set<WarenBestellung> waren) {
        this.waren.clear();
        this.waren.addAll(waren);
    }

    public Set<WarenBestellung> getWaren() {
        return new HashSet<>(this.waren);
    }

    public Lieferant getLieferant() {
        return this.lieferant;
    }

    public void setLieferant(Lieferant lieferant) {
        this.lieferant = lieferant;
    }
}
