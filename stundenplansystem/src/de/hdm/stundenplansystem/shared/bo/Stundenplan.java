package de.hdm.stundenplansystem.shared.bo;
/**
 * Realisierung eines exemplarischen Stundenplans. Ein Objekt der Klasse 
 * Stundenplan besitzt eine Studienhalbjahr. Zudem hat jeder Studenplan 
 * ein dazugehörigen Semesterverband,
 * welcher durch einen Fremdschlüssel der Klasse Semesterverband
 * in Form einer Id (vgl. Klasse {@link Semesterverband}) verbunden wird.
 * @author l.hofmann & holz
 * @version 1.0
 */
public class Stundenplan extends BusinessObjekt {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Studienhalbjahr des Stundenplans
	 */
	private String studienhalbjahr;

	/**
	 * Fremdschlüssel zum Semsterverband des Stundenplans
	 */
	private int semesterverbandId;

	/**
	 * Default-Konstruktor des Stundenplans
	 */
	public Stundenplan() {
	}

	/**
	   * Auslesen des Studienhalbjahr
	   * @return String
	   */
	public String getStudienhalbjahr() {
		return studienhalbjahr;
	}

	/**
	   * Setzen des Studienhalbjahres
	   * @param String studienhalbjahr des jeweiligen Stundenplans
	   * @return void
	   */
	public void setStudienhalbjahr(String studienhalbjahr) {
		this.studienhalbjahr = studienhalbjahr;
	}

	/**
	   * Auslesen des Fremdschlüssel zum Semesterverband
	   * @return int
	   */
	public int getSemesterverbandId() {
		return semesterverbandId;
	}

	/**
	   * Setzen des Fremdschlüssel zum Semesterverband
	   * @param int semesterverbandId des jeweiligen Stundenplans
	   * @return void
	   */
	public void setSemesterverbandId(int semesterverbandId) {
		this.semesterverbandId = semesterverbandId;
	}
	
	public String toString(){
		return this.studienhalbjahr;
	}

}