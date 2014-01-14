package de.hdm.stundenplansystem.shared.bo;

public class Studiengang extends BusinessObjekt {
	
	private static final long serialVersionUID = 1L;
	
	private String bezeichnung;
	
	public Studiengang(){
	}

	public String getBezeichnung() {
		return bezeichnung;
	}

	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

}
