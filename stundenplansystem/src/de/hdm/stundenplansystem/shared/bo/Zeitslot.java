package de.hdm.stundenplansystem.shared.bo;

import java.util.Date;

public class Zeitslot extends BusinessObjekt {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Bezeichnung des Wochentags
	 */
	private String wochentag;
	
	/**
	 * Anfangsuhrzeit 
	 */
	private Date anfangszeit;
	
	/**
	 * Enduhrzeit
	 */
	private Date endzeit;
	
	public Zeitslot(){
	}
	
	public String getWochentag() {
		return wochentag;
	}

	public void setWochentag(String wochentag) {
		this.wochentag = wochentag;
	}
	
	public Date getAnfangszeit(){
		return this.anfangszeit;
	}
	
	public Date getEndzeit(){
		return this.endzeit;
	}

	
//	@SuppressWarnings("deprecation")
//	public String getAnfangsuhrzeit() {
//		return this.anfangszeit.getHours()+":"+this.anfangszeit.getMinutes();
//	}

	public void setAnfangszeit(Date anfangszeit) {
		this.anfangszeit = anfangszeit;
	}

//	@SuppressWarnings("deprecation")
//	public String getEndzeit() { 
//		return this.endzeit.getHours()+":"+this.endzeit.getMinutes();
//	}
	
	

	public void setEndzeit(Date endzeit) {
		this.endzeit = endzeit;
	}

	@Override
	public String toString() {
		return this.anfangszeit+" - "+this.endzeit;
	}
}
