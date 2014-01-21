package de.hdm.stundenplansystem.shared.bo;

public class Stundenplan extends BusinessObjekt {
	
	private static final long serialVersionUID = 1L;
	
	private String studienhalbjahr;
	
	private int semesterverbandId;
	
	public Stundenplan(){
	}

	public String getStudienhalbjahr() {
		return studienhalbjahr;
	}

	public void setStudienhalbjahr(String studienhalbjahr) {
		this.studienhalbjahr = studienhalbjahr;
	}

	public int getSemesterverbandId() {
		return semesterverbandId;
	}

	public void setSemesterverbandId(int semesterverbandId) {
		this.semesterverbandId = semesterverbandId;
	}
	
	

}