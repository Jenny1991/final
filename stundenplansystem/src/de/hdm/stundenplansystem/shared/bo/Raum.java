package de.hdm.stundenplansystem.shared.bo;
/**
 * Realisierung eines exemplarischen Raumes. Ein Objekt der Klasse Raum 
 * besitzt eine Bezeichnung und eine Kapazität, die in Form eines Integer die Anzahl
 * der Studierenden ausdrückt, welche in dem Raum Platz nehmen können.
 * @author l.hofmann & holz
 * @version 1.0
 */
public class Raum extends BusinessObjekt {

	private static final long serialVersionUID = 1L;

	/**
	 * Bezeichnung des Raumes
	 */
	private String bezeichung;

	/**
	 * Kapazität des Raumes;
	 */
	private int kapazitaet;
	
	/**
	 * Default-Konstruktor des Raumes
	 */
	public Raum() {
	}

	/**
	   * Auslesen des Bezeichnung
	   * @return String
	   */
	public String getBezeichnung() {
		return bezeichung;
	}

	/**
	   * Setzen der Bezeichung
	   * @param String bezeichnung des jeweiligen Raumes
	   * @return void
	   */
	public void setBezeichnung(String name) {
		this.bezeichung = name;
	}

	/**
	   * Auslesen des Kapazität
	   * @return int
	   */
	public int getKapazitaet() {
		return kapazitaet;
	}

	/**
	   * Setzen der Kapazität
	   * @param int kapazität des jeweiligen Raumes
	   * @return void
	   */
	public void setKapazitaet(int kapazitaet) {
		this.kapazitaet = kapazitaet;
	}

	/**
	   * Erzeugen einer einfachen textuellen Repräsentation der jeweiligen
	   * Rauminstanz.
	   */
	@Override
	public String toString() {
		return this.bezeichung;
	}

}
