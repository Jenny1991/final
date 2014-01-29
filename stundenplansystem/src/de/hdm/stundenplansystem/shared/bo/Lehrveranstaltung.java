package de.hdm.stundenplansystem.shared.bo;
/**
 * Realisierung einer exemplarischen Lehrveranstaltung. Eine Lehrveranstaltung 
 * besitzt eine Bezeichnung und zeichnet sich durch eine gewisse
 * Anzahl an ETCS aus, welche als Umfang gespeichert werden. Desweitern 
 * gibt das Attribut Semester die jeweilige Semesterstufenzugehörigkeit an.
 * @author l.hofmann & holz
 * @version 1.0
 */
public class Lehrveranstaltung extends BusinessObjekt {

	private static final long serialVersionUID = 1L;

	/**
	 * Bezeichnung der Lehrveranstaltung
	 */
	private String bezeichnung;

	/**
	 * Semesterstufenzugehörigkeit
	 */
	private int semester;

	/**
	 * ECTS-Anzahl der Lehrveranstaltung
	 */
	private int umfang;
	
	/**
	 * Default-Konstruktor der Lehrveranstaltung
	 */
	public Lehrveranstaltung() {
	}
	
	/**
	   * Setzen der Bezeichung
	   * @param bezeichnung der jeweiligen Lehrveranstaltung
	   */
	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}
	
	/**
	   * Auslesen des Bezeichnung
	   * @return String
	   */
	public String getBezeichnung() {
		return this.bezeichnung;
	}

	/**
	   * Auslesen des Semesters
	   * @return int
	   */
	public int getSemester() {
		return semester;
	}
	
	/**
	   * Setzen des Semesters
	   * @param semester Semester der jeweiligen Lehrveranstaltung
	   */
	public void setSemester(int semester) {
		this.semester = semester;
	}

	/**
	   * Auslesen des Umfangs
	   * @return int
	   */
	public int getUmfang() {
		return umfang;
	}

	/**
	   * Setzen des Umfangs
	   * @param umfang der jeweiligen Lehrveranstaltung
	   */
	public void setUmfang(int umfang) {
		this.umfang = umfang;
	}

	/**
	   * Erzeugen einer einfachen textuellen Repräsentation der jeweiligen
	   * Lehrveranstaltungsinstanz.
	   */
	@Override
	public String toString() {
		return this.bezeichnung;
	}
}
