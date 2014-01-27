package de.hdm.stundenplansystem.shared;

import java.util.Vector;

import de.hdm.stundenplansystem.shared.bo.*;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * <p>
 * Synchrone Schnittstelle für eine RPC-fähige Klasse zur Verwaltung von Banken.
 * </p>
 * <p>
 * <code>@RemoteServiceRelativePath("greet")</code> ist bei der Adressierung des
 * aus der zugehörigen Impl-Klasse entstehenden Servlet-Kompilats behilflich. Es
 * gibt im Wesentlichen einen Teil der URL des Servlets an.
 * </p>
 * 
 * @author Thies & L.Hofmann & Holz
 */

@RemoteServiceRelativePath("greet")
public interface Verwaltungsklasse extends RemoteService {

	/**
	 * Initialisierung des Objekts. Diese Methode ist vor dem Hintergrund von
	 * GWT RPC zusätzlich zum No Argument Constructor der implementierenden
	 * Klasse {@link VerwaltungsklasseImpl} notwendig. Bitte diese Methode
	 * direkt nach der Instantiierung aufrufen.
	 * 
	 * @throws IllegalArgumentException
	 */
	public void init() throws IllegalArgumentException;

	public void setDozent(Dozent d) throws IllegalArgumentException;

	public Dozent getDozent() throws IllegalArgumentException;

	public Studiengang getStudiengang()
			throws IllegalArgumentException;

	public void setStudiengang(Studiengang s)
			throws IllegalArgumentException;

	public void setRaum(Raum r) throws IllegalArgumentException;

	public void setSemesterverband(Semesterverband s)
			throws IllegalArgumentException;

	public Zeitslot getZeitslot() throws IllegalArgumentException;

	public void setZeitslot(Zeitslot z)
			throws IllegalArgumentException;

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
	
	public Vector<Stundenplan> getAllStudienhalbjahre()
			throws IllegalArgumentException;

	public Vector<Stundenplan> getAllStundenplaene()
			throws IllegalArgumentException;

	public Vector<Semesterverband> getAllSemesterverbaende()
			throws IllegalArgumentException;

	public Vector<Studiengang> getAllStudiengaenge()
			throws IllegalArgumentException;

	public Vector<Stundenplaneintrag> getAllStundenplaneintraegeByStundenplan(
			int stundenplanId) throws IllegalArgumentException;

	public Vector<Stundenplaneintrag> getAllStundenplaneintraegeByDozent(
			int dozentId) throws IllegalArgumentException;

	public Vector<Stundenplaneintrag> getAllStundenplaneintraegeByRaum(
			int raumId) throws IllegalArgumentException;

	public Vector<Stundenplaneintrag> getAllStundenplaneintraegeByLehrveranstaltung(
			int lehrveranstaltungId) throws IllegalArgumentException;

	public Vector<Zeitslot> getFreieZeitslot(int raumId,
			int dozentId, int stundenplanId)
			throws IllegalArgumentException;

	public Semesterverband getSemesterverbandByStundenplanId(int id)
			throws IllegalArgumentException;

	public Dozent getDozentById(int id)
			throws IllegalArgumentException;

	public Zeitslot getZeitslotById(int id)
			throws IllegalArgumentException;

	public Lehrveranstaltung getLehrveranstaltungById(int id)
			throws IllegalArgumentException;

	public Raum getRaumById(int id) throws IllegalArgumentException;

	public Studiengang getStudiengangById(int id)
			throws IllegalArgumentException;

	public Semesterverband getSemesterverbandById(int id)
			throws IllegalArgumentException;

	public Stundenplaneintrag getStundenplaneintragById(int id)
			throws IllegalArgumentException;

	public Stundenplan getStundenplanById(int id)
			throws IllegalArgumentException;

	public Stundenplaneintrag getStundenplaneintragByDozentAndZeitslot(
			int dozentId, int zeitslotId, String studienhalbjahr)
			throws IllegalArgumentException;

	public Stundenplaneintrag getStundenplaneintragByRaumAndZeitslot(
			int raumId, int zeitslotId, String studienhalbjahr)
			throws IllegalArgumentException;

	public Stundenplaneintrag getStundenplaneintragBySemesterverbandAndZeitslot(
			int semesterverbandId, int zeitslotId, int stundenplanId)
			throws IllegalArgumentException;

	public Studiengang getStudiengangBySemesterverbandId(
			int semesterverbandId) throws IllegalArgumentException;

	public Vector<Semesterverband> getSemsterverbaendeByStudiengang(
			int studiengangId) throws IllegalArgumentException;

	public Vector<Stundenplan> getStundenplaeneBySemesterverband(
			int studiengangId) throws IllegalArgumentException;

	public Stundenplaneintrag createStundenplaneintrag(int d, int l,
			int r, int z, int sp) throws IllegalArgumentException;

	public Dozent createDozent(String vorname, String nachname)
			throws IllegalArgumentException;

	public Stundenplan createStundenplan(String studienhalbjahr,
			int semesterverbandId) throws IllegalArgumentException;

	public Lehrveranstaltung createLehrveranstaltung(
			String bezeichnung, int semester, int umfang)
			throws IllegalArgumentException;

	public Raum createRaum(String bezeichnung, int kapazitaet)
			throws IllegalArgumentException;

	public Semesterverband createSemesterverband(int studiengangId,
			int semester, int studierendenAnzahl, String jahrgang)
			throws IllegalArgumentException;

	public void deleteStundenplan(Stundenplan sp)
			throws IllegalArgumentException;

	public void deleteStudiengang(Studiengang sg)
			throws IllegalArgumentException;

	public void deleteStundenplaneintrag(Stundenplaneintrag s)
			throws IllegalArgumentException;

	public void deleteDozent(Dozent d)
			throws IllegalArgumentException;

	public void deleteLehrveranstaltung(Lehrveranstaltung l)
			throws IllegalArgumentException;

	public void deleteRaum(Raum r) throws IllegalArgumentException;

	public void deleteSemesterverband(Semesterverband sv)
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

	public void changeRaum(Raum r) throws IllegalArgumentException;

	public void changeSemesterverband(Semesterverband sv)
			throws IllegalArgumentException;
}
