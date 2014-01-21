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
	
	public void setRaum (Raum r) throws IllegalArgumentException;
	
	public void setSemesterverband (Semesterverband s) throws IllegalArgumentException;
	
	public Studiengang createStudiengang(String bezeichnung)
			throws IllegalArgumentException;
	
	public Vector<Stundenplaneintrag> getAllStundenplaneintraege()
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
	
	public Dozent getDozentById(int id) throws IllegalArgumentException;
	
	public Zeitslot getZeitslotById(int id) throws IllegalArgumentException;
	
	public Lehrveranstaltung getLehrveranstaltungById(int id) throws IllegalArgumentException;
	
	public Raum getRaumById(int id)throws IllegalArgumentException;
	
	public Studiengang getStudiengangById(int id)throws IllegalArgumentException;
	
	public Semesterverband getSemesterverbandById(int id)throws IllegalArgumentException;
	
	public Stundenplaneintrag getStundenplaneintragById(int id)throws IllegalArgumentException;
	
	public Stundenplan getStundenplanById(int id) throws IllegalArgumentException;
	
	public Stundenplaneintrag getStundenplaneintragByDozentAndZeitslot(int dozentId, int zeitslotId) throws IllegalArgumentException;
	
	public Stundenplaneintrag getStundenplaneintragByRaumAndZeitslot(int raumId, int zeitslotId) throws IllegalArgumentException;
	
	public Stundenplaneintrag getStundenplaneintragBySemesterverbandAndZeitslot(int semesterverbandId, int zeitslotId) throws IllegalArgumentException;
	
	public Vector<Semesterverband> getSemsterverbaendeByStudiengang(int studiengangId)  throws IllegalArgumentException;
	
	public Vector<Stundenplan> getStundenplaeneBySemesterverband(int studiengangId)  throws IllegalArgumentException;
	
	public Stundenplaneintrag createStundenplaneintrag(int d, int l, int r, 
			int z, int sv, int sg, int sp)
					throws IllegalArgumentException;
	
	public Dozent createDozent(String vorname, String nachname)
			throws IllegalArgumentException;
	
	public Stundenplan createStundenplan(String studienhalbjahr)
			throws IllegalArgumentException;
	
	public Lehrveranstaltung createLehrveranstaltung(String bezeichnung, int semester, int umfang)
			throws IllegalArgumentException;
	
	public Raum createRaum(String bezeichnung, int kapazitaet)
			throws IllegalArgumentException;
	
	public Semesterverband createSemesterverband(int studiengangId, int semester, int studierendenAnzahl, String jahrgang)
			throws IllegalArgumentException;
	
	public boolean deleteStundenplan(Stundenplan sp)
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
	
	public void changeStudiengang(Studiengang s)
			throws IllegalArgumentException;
	
	public void changeStundenplan(Stundenplan sp)
			throws IllegalArgumentException;
	
	public void changeStundenplaneintrag(Stundenplaneintrag s)
			throws IllegalArgumentException;
	
	public void changeDozent(Dozent d)
			throws IllegalArgumentException;
	
	public void changeLehrveranstaltung(Lehrveranstaltung l)
			throws IllegalArgumentException;
	
	public void changeRaum(Raum r)
			throws IllegalArgumentException;
	
	public void changeSemsterverband(Semesterverband sv)
			throws IllegalArgumentException;

	public Zeitslot getZeitslot() throws IllegalArgumentException;

	public void setZeitslot(Zeitslot z) throws IllegalArgumentException;
}


