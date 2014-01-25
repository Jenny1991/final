package de.hdm.stundenplansystem.shared.bo;

import java.sql.Time;

public class Zeitslot extends BusinessObjekt {

	private static final long serialVersionUID = 1L;

	/**
	 * Bezeichnung des Wochentags
	 */
	private String wochentag;

	/**
	 * Anfangsuhrzeit
	 */
	private Time anfangszeit;

	/**
	 * Enduhrzeit
	 */
	private Time endzeit;

	public Zeitslot() {
	}

	public String getWochentag() {
		return wochentag;
	}

	public void setWochentag(String wochentag) {
		this.wochentag = wochentag;
	}

	public Time getAnfangszeit() {
		return this.anfangszeit;
	}

	public Time getEndzeit() {
		return this.endzeit;
	}

	public void setAnfangszeit(Time anfangszeit) {
		this.anfangszeit = anfangszeit;
	}

	public void setEndzeit(Time endzeit) {
		this.endzeit = endzeit;
	}

	@Override
	public String toString() {
		return this.anfangszeit + " - " + this.endzeit;
	}
}
