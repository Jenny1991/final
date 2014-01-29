package de.hdm.stundenplansystem.shared.bo;
/**
 * Realisierung eines exemplarischen Studiengangs. Ein Objekt der Klasse 
 * Studiengang besitzt, aus Gründen der Vereinfachung,
 * in diesem Demonstrator lediglich eine Bezeichnung.
 * @author l.hofmann & holz
 * @version 1.0
 */
public class Studiengang extends BusinessObjekt {

	private static final long serialVersionUID = 1L;

	/**
	 * Bezeichnung des Studiengang
	 */
	private String bezeichnung;

	/**
	 * Default-Konstruktor des Studiengangs
	 */
	public Studiengang() {
	}

	/**
	   * Auslesen des Bezeichnung
	   * @return String
	   */
	public String getBezeichnung() {
		return bezeichnung;
	}

	/**
	   * Setzen der Bezeichung
	   * @param bezeichnung des jeweiligen Studiengangs
	   */
	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

}
