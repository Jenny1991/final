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

	void createStundenplaneintrag(int d, int l, int r, int z, int sv,
			AsyncCallback<Stundenplaneintrag> callback);

	void createZeitslot(String wochentag, double anfangszeit, double endzeit,
			AsyncCallback<Zeitslot> callback);

	void deleteDozent(Dozent d, AsyncCallback<Boolean> callback);	
	
	void deleteLehrveranstaltung(Lehrveranstaltung l,
			AsyncCallback<Void> callback);

	void deleteRaum(Raum r, AsyncCallback<Void> callback);

	void deleteZeitslot(Zeitslot z, AsyncCallback<Void> callback);

	void deleteSemesterverband(Semesterverband sv, AsyncCallback<Void> callback);

	void deleteStundenplaneintrag(Stundenplaneintrag s,
			AsyncCallback<Void> callback);

	void deleteStudiengang(Studiengang sg, AsyncCallback<Void> callback);

	void changeDozent(Dozent d, AsyncCallback<Dozent> callback);

	void changeLehrveranstaltung(Lehrveranstaltung l,
			AsyncCallback<Lehrveranstaltung> callback);

	void changeRaum(Raum r, AsyncCallback<Raum> callback);

	void changeStundenplaneintrag(Stundenplaneintrag s,
			AsyncCallback<Stundenplaneintrag> callback);

	void changeStudiengang(Studiengang s, AsyncCallback<Studiengang> callback);

	void changeSemsterverband(Semesterverband sv,
			AsyncCallback<Semesterverband> callback);

	void changeZeitslot(Zeitslot z, AsyncCallback<Zeitslot> callback);

	void setDozent(Dozent d, AsyncCallback<Void> callback);

	void getDozent(AsyncCallback<Dozent> callback);

	void getAllStundenplaneintrag(Raum r, AsyncCallback<Vector<Stundenplaneintrag>> callback);

	void getAllStundenplaneintragOf(Dozent d,
			AsyncCallback<Vector<Stundenplaneintrag>> callback);

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
}
