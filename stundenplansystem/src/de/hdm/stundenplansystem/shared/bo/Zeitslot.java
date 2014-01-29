package de.hdm.stundenplansystem.shared.bo;

import java.sql.Time;
/**
 * Realisierung eines exemplarischen Zeitslots. Ein Objekt der Klasse 
 * Zeitslot besitzt einen Wochentag, eine Anfangszeit und eine Endzeit.
 * @author l.hofmann & holz
 * @version 1.0
 */
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
	
	/**
	 * Default-Konstruktor des Zeitslots
	 */
	public Zeitslot() {
	}

	/**
	   * Auslesen des Wochentages des Zeitslots
	   * @return String
	   */
	public String getWochentag() {
		return wochentag;
	}

	/**
	   * Setzen des Wochentags des Zeitslots
	   * @param wochentag des jeweiligen Zeitslots
	   */
	public void setWochentag(String wochentag) {
		this.wochentag = wochentag;
	}

	/**
	   * Auslesen der Anfangszeit des Zeitslots
	   * @return Time
	   */
	public Time getAnfangszeit() {
		return this.anfangszeit;
	}

	/**
	   * Auslesen der Endzeit des Zeitslots
	   * @return Time
	   */
	public Time getEndzeit() {
		return this.endzeit;
	}

	/**
	   * Setzen der Anfangszeit des Zeitslots
	   * @param anfangszeit des jeweiligen Zeitslots
	   */
	public void setAnfangszeit(Time anfangszeit) {
		this.anfangszeit = anfangszeit;
	}

	/**
	   * Setzen der Endzeit des Zeitslots
	   * @param endzeit des jeweiligen Zeitslots
	   */
	public void setEndzeit(Time endzeit) {
		this.endzeit = endzeit;
	}

	/**
	   * Erzeugen einer einfachen textuellen Repräsentation der jeweiligen
	   * Zeitslotinstanz.
	   */
	@Override
	public String toString() {
		return this.anfangszeit + " - " + this.endzeit;
	}
}
