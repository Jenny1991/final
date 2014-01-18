package de.hdm.stundenplansystem.shared;

import java.util.Vector;

import de.hdm.stundenplansystem.shared.bo.*;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


@RemoteServiceRelativePath("greet")
public interface Verwaltungsklasse extends RemoteService {
	
	public void init() throws IllegalArgumentException;
	
	public void setDozent(Dozent d) throws IllegalArgumentException;
	
	public Dozent getDozent() throws IllegalArgumentException;
	
	public Studiengang getStudiengang() throws IllegalArgumentException;
	
	public void setStudiengang(Studiengang s) throws IllegalArgumentException;
	
	public Studiengang createStudiengang(String bezeichnung)
			throws IllegalArgumentException;
	
	public Vector<Stundenplaneintrag> getAllStundenplaneintrag(Raum r)
			throws IllegalArgumentException;
	
	public Vector<Dozent> getAllDozenten() 
			throws IllegalArgumentException;
	
	public Vector<Lehrveranstaltung> getAllLehrveranstaltungen() 
			throws IllegalArgumentException;
	
	public Vector<Raum> getAllRaeume() 
			throws IllegalArgumentException;
	
	public Vector<Zeitslot> getAllZeitslots() 
			throws IllegalArgumentException;
	
	public Vector<Stundenplan> getAllStundenplaene() 
			throws IllegalArgumentException;
	
	public Vector<Semesterverband> getAllSemesterverbaende() 
			throws IllegalArgumentException;
	
	public Vector<Studiengang> getAllStudiengaenge() 
			throws IllegalArgumentException;
	  
	
	public Vector<Stundenplaneintrag> getAllStundenplaneintragOf(Dozent d)
		      throws IllegalArgumentException;
	
	public Dozent getDozentById(int id) throws IllegalArgumentException;
	
	public Lehrveranstaltung getLehrveranstaltungById(int id) throws IllegalArgumentException;
	
	public Raum getRaumById(int id)throws IllegalArgumentException;
	
	public Studiengang getStudiengangById(int id)throws IllegalArgumentException;
	
	public Semesterverband getSemesterverbandById(int id)throws IllegalArgumentException;
	
	public Stundenplaneintrag getStundenplaneintragById(int id)throws IllegalArgumentException;
	
	public Stundenplaneintrag createStundenplaneintrag(int d, int l, int r, 
			int z, int sv)
					throws IllegalArgumentException;
	
	public Dozent createDozent(String vorname, String nachname);
	
	public Zeitslot createZeitslot(String wochentag, double anfangszeit, double endzeit)
			throws IllegalArgumentException;
	
	public Lehrveranstaltung createLehrveranstaltung(String bezeichnung, int semester, int umfang)
			throws IllegalArgumentException;
	
	public Raum createRaum(String bezeichnung, int kapazitaet)
			throws IllegalArgumentException;
	
	public Semesterverband createSemesterverband(int studiengangId, int semester, int studierendenAnzahl, String jahrgang)
			throws IllegalArgumentException;
	
	public boolean deleteZeitslot(Zeitslot z)
			throws IllegalArgumentException;
	
	public boolean deleteStudiengang(Studiengang sg)
			throws IllegalArgumentException;
	
	public boolean deleteStundenplaneintrag(Stundenplaneintrag s)
			throws IllegalArgumentException;
	
	public boolean deleteDozent(Dozent d) 
			throws IllegalArgumentException;
	
	public boolean deleteLehrveranstaltung(Lehrveranstaltung l) 
			throws IllegalArgumentException;
	
	public boolean deleteRaum(Raum r) 
			throws IllegalArgumentException;
	
	public boolean deleteSemesterverband(Semesterverband sv) 
			throws IllegalArgumentException;
	
	public Studiengang changeStudiengang(Studiengang s)
			throws IllegalArgumentException;
	
	public Zeitslot changeZeitslot(Zeitslot z)
			throws IllegalArgumentException;
	
	public Stundenplaneintrag changeStundenplaneintrag(Stundenplaneintrag s)
			throws IllegalArgumentException;
	
	public Dozent changeDozent(Dozent d)
			throws IllegalArgumentException;
	
	public Lehrveranstaltung changeLehrveranstaltung(Lehrveranstaltung l)
			throws IllegalArgumentException;
	
	public Raum changeRaum(Raum r)
			throws IllegalArgumentException;
	
	public Semesterverband changeSemsterverband(Semesterverband sv)
			throws IllegalArgumentException;
}


