package de.hdm.stundenplansystem.shared.bo;

public class Stundenplaneintrag extends BusinessObjekt {

	private static final long serialVersionUID = 1L;
	
	/**
	 * jeweilige Businessobjekte der Stundenplaneintraege
	 */
	private int dozentId;
	private int lehrveranstaltungId;
	private int raumId;
	private int zeitslotId;
	private int semesterverbandId;
	private int stundenplanId;
	private String abkuerzung;
	
	public Stundenplaneintrag(){
	}

	public int getDozentId() {
		return dozentId;
	}

	public void setDozentId(int dozentId) {
		this.dozentId = dozentId;
	}

	public int getLehrveranstaltungId() {
		return lehrveranstaltungId;
	}

	public void setLehrveranstaltungId(int lehrveranstaltungId) {
		this.lehrveranstaltungId = lehrveranstaltungId;
	}

	public int getRaumId() {
		return raumId;
	}

	public void setRaumId(int raumId) {
		this.raumId = raumId;
	}

	public int getZeitslotId() {
		return zeitslotId;
	}

	public void setZeitslotId(int zeitslotId) {
		this.zeitslotId = zeitslotId;
	}

	public int getSemesterverbandId() {
		return semesterverbandId;
	}

	public void setSemesterverbandId(int semesterverbandId) {
		this.semesterverbandId = semesterverbandId;
	}

	public int getStundenplanId() {
		return stundenplanId;
	}

	public void setStundenplanId(int stundenplanId) {
		this.stundenplanId = stundenplanId;
	}

	public String getAbkuerzung() {
		return abkuerzung;
	}

	public void setAbkuerzung(String abkuerzung) {
		this.abkuerzung = abkuerzung;
	}


}