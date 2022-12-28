package main.java.de.appdev2.entities;

import java.io.Serializable;

public class Lieferant implements Serializable {
	
	private int nr;
	private String name;

	/**
	 * Notwendige Daten um in die Datenbank einzufügen.
	 * Der Primärschlüssel hat "Auto Increment", daher muss er hier nicht definiert werden.
	 * @param name
	 */
	public Lieferant(String name) {
		this(-1, name);
	}

	public Lieferant(int nr, String name) {
		this.nr = nr;
		this.name = name;
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

	@Override
	public String toString() {
		return "Lieferant[nr = " + this.nr + ", name = " + this.name +"]";
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj) || obj instanceof Lieferant lieferant && this.nr == lieferant.nr
				&& this.name.equals(lieferant.name);
	}
}
