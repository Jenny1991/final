package de.hdm.stundenplansystem.shared.bo;

public class Lehrveranstaltung extends BusinessObjekt{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Bezeichnung der Lehrveranstaltung
	 */
		private String bezeichnung;
	
	/**
	 * SemesterstufenzugehÃ¶rigkeit	
	 */
		private int semester;
		
	/**
	 * ECTS-Anzahl der Lehrveranstaltung
	 */
		private int umfang;
		
	public Lehrveranstaltung(){
	}
	
	public void setBezeichnung(String bezeichnung){
		this.bezeichnung = bezeichnung;
	}
	
	public String getBezeichnung(){
		return this.bezeichnung;
	}
	
	public int getSemester() {
		return semester;
	}

	public void setSemester(int semester) {
		this.semester = semester;
	}

	public int getUmfang() {
		return umfang;
	}

	public void setUmfang(int umfang) {
		this.umfang = umfang;
	}
	
	@Override
	public String toString(){
		return this.getId()+" "+this.bezeichnung;
	}
}
