package main.java.de.appdev2.entities;

import java.io.Serializable;

public class Ware implements Serializable {

    private int nr;
    private float stueckpreis;
    private int stueckzahl;
    private String bezeichnung;

    /**
     * Notwendige Daten um in die Datenbank einzufügen.
     * Der Primärschlüssel hat "Auto Increment", daher muss er hier nicht definiert werden.
     *
     * @param stueckpreis
     * @param stueckzahl
     * @param bezeichnung
     */
    public Ware(float stueckpreis, int stueckzahl, String bezeichnung) {
        this(-1, stueckpreis, stueckzahl, bezeichnung);
    }

    public Ware(int nr, float stueckpreis, int stueckzahl, String bezeichnung) {
        this.nr = nr;
        this.stueckpreis = stueckpreis;
        this.stueckzahl = stueckzahl;
        this.bezeichnung = bezeichnung;
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

    @Override
    public String toString() {
        return "Ware[nr = " + this.nr + ", stückpreis = " + this.stueckpreis + ", stückzahl = "
                + this.stueckzahl + ", bezeichnung = " + this.bezeichnung + "]";
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj) || obj instanceof Ware ware && this.stueckpreis == ware.stueckpreis
                && this.bezeichnung.equals(ware.bezeichnung) && this.nr == ware.nr && this.stueckzahl == ware.stueckzahl;
    }
}
