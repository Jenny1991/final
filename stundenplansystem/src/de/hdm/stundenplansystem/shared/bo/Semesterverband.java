package de.hdm.stundenplansystem.shared.bo;

public class Semesterverband extends BusinessObjekt {

	private static final long serialVersionUID = 1L;

	/**
	 * Bezeichnung des jeweiligen Semesterverbands
	 */
	
	private Studiengang bezeichnung;
	private Stundenplan stundenplan;
	private Studiengang studiengang;
	
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
	
	public Semesterverband(){
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

	public Studiengang getBezeichnung() {
		return bezeichnung;
	}

	public void setBezeichnung(Studiengang bezeichnung) {
		this.bezeichnung = bezeichnung;
	}
	
	public void setStundenplanId (int id){
		this.studiengang.setId(id);
	}
	
	public int getStudenplanId(){
		return this.stundenplan.getId();
	}
	
	public void setStudiengangId(int id){
		this.studiengang.setId(id);
	}
	public int getStudiengangId(){
		return this.studiengang.getId();
	}
}

