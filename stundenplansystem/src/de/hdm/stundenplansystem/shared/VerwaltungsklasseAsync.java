package de.hdm.stundenplansystem.shared;

import java.util.Vector;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.hdm.stundenplansystem.shared.bo.*;

/**
 * The async counterpart of <code>Verwaltungsklasse</code>.
 */
public interface VerwaltungsklasseAsync {
	
	void init(AsyncCallback<Void> callback);

	void createLehrveranstaltung(String bezeichnung, int semester, int umfang,
			AsyncCallback<Lehrveranstaltung> callback);

	void createDozent(String vorname, String nachname,
	AsyncCallback<Dozent> callback);

	void createRaum(String bezeichnung, int kapazitaet, AsyncCallback<Raum> callback);
	
	void createSemesterverband(int studiengangId, int semester,
			int studierendenAnzahl, String jahrgang,
			AsyncCallback<Semesterverband> callback);

	void createStudiengang(String bezeichnung,
	AsyncCallback<Studiengang> callback);

	void createStundenplaneintrag(int d, int l, int r, int z, int sv, int sg,
			int sp, AsyncCallback<Stundenplaneintrag> callback);

	void createStundenplan(String studienhalbjahr, AsyncCallback<Stundenplan> callback);

	void deleteDozent(Dozent d, AsyncCallback<Boolean> callback);	
	
	void deleteLehrveranstaltung(Lehrveranstaltung l,
			AsyncCallback<Boolean> callback);

	void deleteRaum(Raum r, AsyncCallback<Boolean> callback);

	void deleteStundenplan(Stundenplan sp, AsyncCallback<Boolean> callback);

	void deleteSemesterverband(Semesterverband sv, AsyncCallback<Boolean> callback);

	void deleteStundenplaneintrag(Stundenplaneintrag s,
			AsyncCallback<Boolean> callback);

	void deleteStudiengang(Studiengang sg, AsyncCallback<Boolean> callback);

	void changeDozent(Dozent d, AsyncCallback<Void> callback);

	void changeLehrveranstaltung(Lehrveranstaltung l,
			AsyncCallback<Void> callback);

	void changeRaum(Raum r, AsyncCallback<Void> callback);

	void changeStundenplaneintrag(Stundenplaneintrag s,
			AsyncCallback<Void> callback);

	void changeStudiengang(Studiengang s, AsyncCallback<Void> callback);

	void changeSemsterverband(Semesterverband sv, AsyncCallback<Void> callback);

	void changeStundenplan(Stundenplan sp, AsyncCallback<Void> callback);

	void setDozent(Dozent d, AsyncCallback<Void> callback);

	void getDozent(AsyncCallback<Dozent> callback);

	void getAllDozenten(AsyncCallback<Vector<Dozent>> callback);

	void getAllLehrveranstaltungen(AsyncCallback<Vector<Lehrveranstaltung>> callback);

	void getAllRaeume(AsyncCallback<Vector<Raum>> callback);

	void getAllSemesterverbaende(AsyncCallback<Vector<Semesterverband>> callback);

	void getAllStudiengaenge(AsyncCallback<Vector<Studiengang>> callback);

	void getAllStundenplaene(AsyncCallback<Vector<Stundenplan>> callback);

	void getAllZeitslots(AsyncCallback<Vector<Zeitslot>> callback);
	
	void getStudiengang(AsyncCallback<Studiengang> callback);

	void setStudiengang(Studiengang s, AsyncCallback<Void> callback);

	void getDozentById(int id, AsyncCallback<Dozent> callback);

	void getLehrveranstaltungById(int id,
			AsyncCallback<Lehrveranstaltung> callback);

	void getRaumById(int id, AsyncCallback<Raum> callback);

	void getStudiengangById(int id, AsyncCallback<Studiengang> callback);

	void getSemesterverbandById(int id, AsyncCallback<Semesterverband> callback);

	void getStundenplaneintragById(int id,
			AsyncCallback<Stundenplaneintrag> callback);

	void getStundenplanById(int id, AsyncCallback<Stundenplan> callback);

	void getZeitslotById(int id, AsyncCallback<Zeitslot> callback);

	void getStundenplaneintragByDozentAndZeitslot(int dozentId, int zeitslotId,
			AsyncCallback<Stundenplaneintrag> callback);

	void getAllStundenplaneintraege(
			AsyncCallback<Vector<Stundenplaneintrag>> callback);

	void setRaum(Raum r, AsyncCallback<Void> callback);

	void setSemesterverband(Semesterverband s, AsyncCallback<Void> callback);
	
	void setZeitslot(Zeitslot z, AsyncCallback<Void> callback);

	void getZeitslot(AsyncCallback<Zeitslot> callback);
	
	void getStundenplaneintragBySemesterverbandAndZeitslot(
			int semesterverbandId, int zeitslotId,
			AsyncCallback<Stundenplaneintrag> callback);

	void getStundenplaneintragByRaumAndZeitslot(int raumId, int zeitslotId,
			AsyncCallback<Stundenplaneintrag> callback);

	void getSemsterverbaendeByStudiengang(int studiengangId,
			AsyncCallback<Vector<Semesterverband>> callback);

	void getStundenplaeneBySemesterverband(int studiengangId,
			AsyncCallback<Vector<Stundenplan>> callback);
}
