package main.java.de.appdev2.server.database.tables;

import main.java.de.appdev2.entities.Lieferant;

import java.sql.Timestamp;
import java.util.HashSet;

public class Bestellung {
    private int nr;
    private Timestamp lieferdatum;

    private Lieferant lieferant;
    private Set<WarenBestellung> waren;

    public Bestellung(int nr, Timestamp lieferdatum, Set<Warenbestellung> waren, Lieferant lieferant){
        this.nr = nr;
        this.lieferdatum = lieferdatum;
        this.waren = waren;
        this.lieferant = lieferant;
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
        this.waren = waren;
    }

    public Set<WarenBestellung> getWaren() {
        return new HashSet<>(this.waren);
    }

    public Lieferant getLieferant() {
        return lieferant;
    }

    public void setLieferant(Lieferant lieferant) {
        this.lieferant = lieferant;
    }
}
