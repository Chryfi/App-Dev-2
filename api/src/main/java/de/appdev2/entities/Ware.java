package main.java.de.appdev2.entities;

public class Ware {
    private int nr;
    private float stueckpreis;
    private int stueckzahl;
    private String bezeichnung;

    public Ware(int nr, float stueckpreis, int stueckzahl, String bezeichnung) {
        this.nr = nr;
        this.stueckpreis = stueckpreis;
        this.stueckzahl = stueckzahl;
        this.bezeichnung = bezeichnung;
    }

    public int getNr() {
        return nr;
    }

    public void setNr(int nr) {
        this.nr = nr;
    }

    public float getStueckpreis() {
        return stueckpreis;
    }

    public void setStueckpreis(float stueckpreis) {
        this.stueckpreis = stueckpreis;
    }

    public int getStueckzahl() {
        return stueckzahl;
    }

    public void setStueckzahl(int stueckzahl) {
        this.stueckzahl = stueckzahl;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }
}
