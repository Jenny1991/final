package de.hdm.stundenplansystem.shared.bo;

public class Stundenplan extends BusinessObjekt {
	
	private static final long serialVersionUID = 1L;
	
	private String studienhalbjahr;
	
	public Stundenplan(){
	}

	public String getStudienhalbjahr() {
		return studienhalbjahr;
	}

	public void setStudienhalbjahr(String studienhalbjahr) {
		this.studienhalbjahr = studienhalbjahr;
	}

}