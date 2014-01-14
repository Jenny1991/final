package de.hdm.stundenplansystem.shared.bo;

public class Zeitslot extends BusinessObjekt {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Bezeichnung des Wochentags
	 */
	private String wochentag;
	
	/**
	 * Anfangsuhrzeit 
	 */
	private double anfangszeit;
	
	/**
	 * Enduhrzeit
	 */
	private double endzeit;
	
	public Zeitslot(){
		
	}
	
	public Zeitslot(String wochentag, double anfangszeit, double endzeit){
		this.wochentag = wochentag;
		this.anfangszeit = anfangszeit;
		this.endzeit = endzeit;
	}

	public String getWochentag() {
		return wochentag;
	}

	public void setWochentag(String wochentag) {
		this.wochentag = wochentag;
	}

	public double getAnfangszeit() {
		return anfangszeit;
	}

	public void setAnfangszeit(double anfangszeit) {
		this.anfangszeit = anfangszeit;
	}

	public double getEndzeit() {
		return endzeit;
	}

	public void setEndzeit(double endzeit) {
		this.endzeit = endzeit;
	}
	
}
