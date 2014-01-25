package de.hdm.stundenplansystem.shared.bo;

public class Semesterverband extends BusinessObjekt {

	private static final long serialVersionUID = 1L;

	/**
	 * Bezeichnung des jeweiligen Semesterverbands
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
	 * Semesterjahrgang des Erstsemsters
	 */
	private String jahrgang;

	private String kuerzel;

	public Semesterverband() {
	}

	public int getSemester() {
		return semester;
	}

	public void setSemester(int semester) {
		this.semester = semester;
	}

	public int getStudierendenAnzahl() {
		return studierendenAnzahl;
	}

	public void setStudierendenAnzahl(int studierendenAnzahl) {
		this.studierendenAnzahl = studierendenAnzahl;
	}

	public String getJahrgang() {
		return jahrgang;
	}

	public void setJahrgang(String jahrgang) {
		this.jahrgang = jahrgang;
	}

	public int getStudiengangId() {
		return StudiengangId;
	}

	public void setStudiengangId(int studiengangId) {
		this.StudiengangId = studiengangId;
	}


	public String getKuerzel() {
		return kuerzel;
	}

	public void setKuerzel(String a) {
		this.kuerzel = a;
	}

}
