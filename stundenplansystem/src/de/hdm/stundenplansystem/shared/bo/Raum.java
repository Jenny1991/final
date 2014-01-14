package de.hdm.stundenplansystem.shared.bo;

public class Raum extends BusinessObjekt {
	
	private static final long serialVersionUID = 1L;
		
	/**
	 * Bezeichnung des Raumes
	 */
		private String bezeichung;
		
	/**
	 * Kapazit?t des Raumes;
	 */
		private int kapazitaet;
		
	public Raum(){
	}

	public String getBezeichnung() {
		return bezeichung;
	}

	public void setBezeichnung(String name) {
		this.bezeichung = name;
	}

	public int getKapazitaet() {
		return kapazitaet;
	}

	public void setKapazitaet(int kapazitaet) {
		this.kapazitaet = kapazitaet;
	}
	
	
	
}

