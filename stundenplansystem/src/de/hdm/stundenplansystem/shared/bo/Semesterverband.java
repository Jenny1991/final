package de.hdm.stundenplansystem.shared.bo;
/**
 * Realisierung eines exemplarischen Semesterverbandes. Ein Objekt der Klasse 
 * Semesterverband besitzt eine Semester, eine Anzahl an Studiereden, welche zu dem
 * Semesterverband  gehören und ein Jahrgang in dem der Semesterverband das Studium
 * begonnen hat. Zudem hat jeder Semesterverband ein dazugehörigen Studiengang,
 * welcher durch einen Fremdschlüssel der Klasse Studiengang 
 * in Form einer Id (vgl. Klasse {@link Studiengang}) verbunden wird.
 * Außerdem hat er ein Kürzel, welches sich aus den ersten beiden 
 * Buchstaben der dazugehörigen Studiengangsbezeichnung ergibtwird immer dann erzeugt, 
 * wenn ein Semesterverband erstellt oder geändert wird. 
 * @author l.hofmann & holz
 * @version 1.0
 */
public class Semesterverband extends BusinessObjekt {

	private static final long serialVersionUID = 1L;

	/**
	 * Fremdschlüssel des dazugehörigen Studiengangs
	 */
	private int StudiengangId;

	/**
	 * Semesterstufe
	 */
	private int semester;

	/**
	 * Anzahl der Studierenden
	 */
	private int studierendenAnzahl;

	/**
	 * Jahr des Studienbeginns
	 */
	private String jahrgang;

	/**
	 * Kürzel des Semesterverbands
	 */
	private String kuerzel;

	/**
	 * Default-Konstruktor des Semesterverbands
	 */
	public Semesterverband() {
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
	   * @param semester des jeweiligen Semesterverbandes
	   */
	public void setSemester(int semester) {
		this.semester = semester;
	}

	/**
	   * Auslesen der Anzahl der Studierenden
	   * @return int
	   */
	public int getStudierendenAnzahl() {
		return studierendenAnzahl;
	}

	/**
	   * Setzen der Anzahl der Studierenden
	   * @param studierendenAnzahl des jeweiligen Semesterverbandes
	   */
	public void setStudierendenAnzahl(int studierendenAnzahl) {
		this.studierendenAnzahl = studierendenAnzahl;
	}
	
	/**
	   * Auslesen des Jahrgangs
	   * @return String
	   */
	public String getJahrgang() {
		return jahrgang;
	}

	/**
	   * Setzen des Jahrgangs
	   * @param jahrgang des jeweiligen Semesterverbandes
	   */
	public void setJahrgang(String jahrgang) {
		this.jahrgang = jahrgang;
	}

	/**
	   * Auslesen des Fremdschlüssel zum Studiengang
	   * @return int
	   */
	public int getStudiengangId() {
		return StudiengangId;
	}

	/**
	   * Setzen des Fremdschlüssel zum Studiengang
	   * @param studiengangId des jeweiligen Semesterverbandes
	   */
	public void setStudiengangId(int studiengangId) {
		this.StudiengangId = studiengangId;
	}

	/**
	   * Auslesen des Küerzels
	   * @return String
	   */
	public String getKuerzel() {
		return kuerzel;
	}

	/**
	   * Setzen des Kürzels
	   * @param kuerzel des jeweiligen Semesterverbandes
	   */
	public void setKuerzel(String kuerzel) {
		this.kuerzel = kuerzel;
	}

}
