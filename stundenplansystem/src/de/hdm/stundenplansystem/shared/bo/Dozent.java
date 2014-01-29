package de.hdm.stundenplansystem.shared.bo;
/**
 * Realisierung einer exemplarischen Dozentenklasse. Aus Gründen der Vereinfachung
 * besitzt der Dozent in diesem Demonstrator lediglich einen Vornamen und einen
 * Nachnamen.
 * 
 * @author thies & l.hofmann
 * @version 1.0
 */

public class Dozent extends BusinessObjekt {

	private static final long serialVersionUID = 1L;

	/**
	 * Vorname des Dozenten
	 */
	private String vorname;

	/**
	 * Nachname des Dozenten
	 */
	private String nachname;
	
	/**
	 * Default-Konstruktor des Dozenten
	 */
	public Dozent() {
	}

	/**
	   * Auslesen des Vornamens
	   * @return String
	   */
	public String getVorname() {
		return vorname;
	}

	/**
	   * Setzen des Vornamens
	   * @param vorname Vorname des jeweiligen Dozenten als String
	   */
	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	/**
	   * Auslesen des Nachnamens
	   * @return String
	   */
	public String getNachname() {
		return nachname;
	}
	
	/**
	   * Setzen des Nachnamens
	   * @param nachname Nachname des jeweiligen Dozenten als String
	   */
	public void setNachname(String nachname) {
		this.nachname = nachname;
	}

}