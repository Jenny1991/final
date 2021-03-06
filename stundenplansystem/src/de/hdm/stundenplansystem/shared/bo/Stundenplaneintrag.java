package de.hdm.stundenplansystem.shared.bo;
/**
 * Realisierung eines exemplarischen Stundenplaneintrages. Ein 
 * Stundenplaneintrag besitzt ein Dozenten, eine Lehrveranstaltung,
 *  ein Semesterverband, ein Raum, ein Zeitslot und Stundenplan hat.
 * Außerdem hat er eine Abkürzung, welche sich aus den ersten beiden 
 * Buchstaben der dazugehörigen Studiengangsbezeichnung ergibt und
 * aus der Bezeichnung der Lehrveranstaltung. Die Abkürzung wird immer 
 * dann erzeugt, wenn ein Stundenplaneintrag erstellt oder geändert wird. 
 * @author l.hofmann & holz
 * @version 1.0
 */
public class Stundenplaneintrag extends BusinessObjekt {

	private static final long serialVersionUID = 1L;

	/**
	 * Fremdschlüssel jeweilige Businessobjekte des Stundenplaneintrags
	 */
	private int dozentId;
	private int lehrveranstaltungId;
	private int raumId;
	private int zeitslotId;
	private int stundenplanId;
	private String abkuerzung;

	/**
	 * Default-Konstruktor des Stundenplaneinbtrags
	 */
	public Stundenplaneintrag() {
	}

	/**
	   * Auslesen des Dozenten-Fremdschlüssel zum Stundenplaneintrags
	   * @return int
	   */
	public int getDozentId() {
		return dozentId;
	}

	/**
	   * Setzen des Dozenten-Fremdschlüssel zum Stundenplaneintrags
	   * @param dozentId des jeweiligen Stundenplaneintrags
	   */
	public void setDozentId(int dozentId) {
		this.dozentId = dozentId;
	}

	/**
	   * Auslesen des Lehrveranstaltung-Fremdschlüssel zum Stundenplaneintrags
	   * @return int
	   */
	public int getLehrveranstaltungId() {
		return lehrveranstaltungId;
	}

	/**
	   * Setzen des Lehrveranstaltung-Fremdschlüssel zum Stundenplaneintrags
	   * @param lehrveranstaltungId des jeweiligen Stundenplaneintrags
	   */
	public void setLehrveranstaltungId(int lehrveranstaltungId) {
		this.lehrveranstaltungId = lehrveranstaltungId;
	}

	/**
	   * Auslesen des Raum-Fremdschlüssel zum Stundenplaneintrags
	   * @return int
	   */
	public int getRaumId() {
		return raumId;
	}

	/**
	   * Setzen des Raum-Fremdschlüssel zum Stundenplaneintrags
	   * @param raumId des jeweiligen Stundenplaneintrags
	   */
	public void setRaumId(int raumId) {
		this.raumId = raumId;
	}

	/**
	   * Auslesen des Zeitslot-Fremdschlüssel zum Stundenplaneintrags
	   * @return int
	   */
	public int getZeitslotId() {
		return zeitslotId;
	}

	/**
	   * Setzen des Zeitslot-Fremdschlüssel zum Stundenplaneintrags
	   * @param zeitslotId des jeweiligen Stundenplaneintrags
	   */
	public void setZeitslotId(int zeitslotId) {
		this.zeitslotId = zeitslotId;
	}

	/**
	   * Auslesen des Studenplan-Fremdschlüssel zum Stundenplaneintrags
	   * @return int
	   */
	public int getStundenplanId() {
		return stundenplanId;
	}

	/**
	   * Setzen des Stundenplan-Fremdschlüssel zum Stundenplaneintrags
	   * @param stundenplanId des jeweiligen Stundenplaneintrags
	   */
	public void setStundenplanId(int stundenplanId) {
		this.stundenplanId = stundenplanId;
	}

	/**
	   * Auslesen der Abkürzung des Stundenplaneinbtrags
	   * @return String
	   */
	public String getAbkuerzung() {
		return abkuerzung;
	}

	/**
	   * Setzen der Abkürzung des Stundenplaneintrags
	   * @param abkuerzung des jeweiligen Stundenplaneintrags
	   */
	public void setAbkuerzung(String abkuerzung) {
		this.abkuerzung = abkuerzung;
	}

}