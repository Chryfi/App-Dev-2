package main.java.de.appdev2.entities;

public class WarenBestellung {

    private Ware ware;
    private int bestellteMenge;
    private int gelieferteMenge;


    public Ware getWare() {
        return ware;
    }

    public void setWare(Ware ware) {
        this.ware = ware;
    }

    public WarenBestellung(int bestellteMenge, int gelieferteMenge){
        this.bestellteMenge = bestellteMenge;
        this.gelieferteMenge = gelieferteMenge;
    }

    public int getBestellteMenge(){
        return bestellteMenge;
    }

    public void setBestellteMenge(int bestellteMenge) {
        this.bestellteMenge = bestellteMenge;
    }

    public int getGelieferteMenge(){
        return gelieferteMenge;
    }
    public void setGelieferteMenge(int gelieferteMenge) {
        this.gelieferteMenge = gelieferteMenge;
    }

}
