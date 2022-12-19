package main.java.de.appdev2.entities;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Ware {
    private int nr;
    private float stueckpreis;
    private int stueckzahl;
    private String bezeichnung;
    private final Set<WarenBestellung> bestellungen = new HashSet<>();

    public Ware(int nr, float stueckpreis, int stueckzahl, String bezeichnung) {
        this(nr, stueckpreis, stueckzahl, bezeichnung, new HashSet<>());
    }

    public Ware(int nr, float stueckpreis, int stueckzahl, String bezeichnung, Set<WarenBestellung> bestellungen) {
        this.nr = nr;
        this.stueckpreis = stueckpreis;
        this.stueckzahl = stueckzahl;
        this.bezeichnung = bezeichnung;
        this.bestellungen.addAll(bestellungen);
    }

    public Set<WarenBestellung> getBestellungen() {
        return new HashSet<>(this.bestellungen);
    }

    public void addBestellung(WarenBestellung bestellung) {
        this.bestellungen.add(bestellung);
    }

    public void setBestellungen(Set<WarenBestellung> bestellungen) {
        this.bestellungen.clear();
        this.bestellungen.addAll(bestellungen);
    }

    public int getNr() {
        return this.nr;
    }

    public void setNr(int nr) {
        this.nr = nr;
    }

    public float getStueckpreis() {
        return this.stueckpreis;
    }

    public void setStueckpreis(float stueckpreis) {
        this.stueckpreis = stueckpreis;
    }

    public int getStueckzahl() {
        return this.stueckzahl;
    }

    public void setStueckzahl(int stueckzahl) {
        this.stueckzahl = stueckzahl;
    }

    public String getBezeichnung() {
        return this.bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }
}
