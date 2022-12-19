package main.java.de.appdev2.entities;

import java.util.HashSet;
import java.util.Set;

public class Lieferant{
	
	private int nr;
	private String name;
	private final Set<Bestellung> bestellungen = new HashSet<>();
	
	public Lieferant(int nr, String name) {
		this(nr, name, new HashSet<>());
	}

	public Lieferant(int nr, String name, Set<Bestellung> bestellungen) {
		this.nr = nr;
		this.name = name;
		this.bestellungen.addAll(bestellungen);
	}

	public Set<Bestellung> getBestellungen() {
		return new HashSet<>(this.bestellungen);
	}

	public void setBestellungen(Set<Bestellung> bestellungen) {
		this.bestellungen.clear();
		this.bestellungen.addAll(bestellungen);
	}

	public void addBestellung(Bestellung bestellung) {
		this.bestellungen.add(bestellung);
	}

	public int getNr() {
		return this.nr;
	}

	public void setNr(int nr) {
		this.nr = nr;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
