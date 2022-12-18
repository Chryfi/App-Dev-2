package main.java.de.appdev2.entities;

import java.util.Set;

public class Lieferant{
	
	private int nr;
	private String name;
	
	public Lieferant(int nr, String name) {
		this.nr = nr;
		this.name = name;
	}

	public int getNr() {
		return nr;
	}

	public void setNr(int nr) {
		this.nr = nr;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

}
