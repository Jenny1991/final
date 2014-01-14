package de.hdm.stundenplansystem.shared.bo;

public class Dozent extends BusinessObjekt {
	
	private static final long serialVersionUID = 1L;
	
/**
 * Vorname des Dozenten
 */
	private String vorname;
	
/**
 * Nachname des Dozenten
 */
	private String nachname;
	
public Dozent(){	
}	

public String getVorname() {
	return vorname;
}

public void setVorname(String vorname) {
	this.vorname = vorname;
}

public String getNachname() {
	return nachname;
}

public void setNachname(String nachname) {
	this.nachname = nachname;
}

}