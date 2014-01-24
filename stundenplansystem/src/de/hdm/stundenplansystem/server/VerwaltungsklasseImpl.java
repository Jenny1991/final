package de.hdm.stundenplansystem.server;

import java.util.Vector;

import de.hdm.stundenplansystem.server.db.DozentMapper;
import de.hdm.stundenplansystem.server.db.LehrveranstaltungMapper;
import de.hdm.stundenplansystem.server.db.RaumMapper;
import de.hdm.stundenplansystem.server.db.SemesterverbandMapper;
import de.hdm.stundenplansystem.server.db.StudiengangMapper;
import de.hdm.stundenplansystem.server.db.StundenplanMapper;
import de.hdm.stundenplansystem.server.db.StundenplaneintragMapper;
import de.hdm.stundenplansystem.server.db.ZeitslotMapper;
import de.hdm.stundenplansystem.shared.Verwaltungsklasse;
import de.hdm.stundenplansystem.shared.bo.*;


import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * <p>
 * Implementierungsklasse des Interface <code>Verwaltungsklasse</code>. Diese
 * Klasse ist <em>die</em> Klasse, die neben {@link ReportGeneratorImpl}
 * sämtliche Applikationslogik (oder engl. Business Logic) aggregiert. Sie ist
 * wie eine Spinne, die sämtliche Zusammenhänge in ihrem Netz (in unserem Fall
 * die Daten der Applikation) überblickt und für einen geordneten Ablauf und
 * dauerhafte Konsistenz der Daten und Abläufe sorgt.
 * </p>
 * <p>
 * Die Applikationslogik findet sich in den Methoden dieser Klasse. Jede dieser
 * Methoden kann als <em>Transaction Script</em> bezeichnet werden. Dieser Name
 * lässt schon vermuten, dass hier analog zu Datenbanktransaktion pro
 * Transaktion gleiche mehrere Teilaktionen durchgeführt werden, die das System
 * von einem konsistenten Zustand in einen anderen, auch wieder konsistenten
 * Zustand überführen. Wenn dies zwischenzeitig scheitern sollte, dann ist das
 * jeweilige Transaction Script dafür verwantwortlich, eine Fehlerbehandlung
 * durchzuführen.
 * </p>
 * <p>
 * Diese Klasse steht mit einer Reihe weiterer Datentypen in Verbindung. Dies
 * sind:
 * <ol>
 * <li>{@link Verwaltungsklasse}: Dies ist das <em>lokale</em> - also
 * Server-seitige - Interface, das die im System zur Verfügung gestellten
 * Funktionen deklariert.</li>
 * <li>{@link VerwaltungsklasseAsync}: <code>VerwaltungsklasseImpl</code> und
 * <code>Verwaltunsklasse</code> bilden nur die Server-seitige Sicht der
 * Applikationslogik ab. Diese basiert vollständig auf synchronen
 * Funktionsaufrufen. Wir müssen jedoch in der Lage sein, Client-seitige
 * asynchrone Aufrufe zu bedienen. Dies bedingt ein weiteres Interface, das in
 * der Regel genauso benannt wird, wie das synchrone Interface, jedoch mit dem
 * zusätzlichen Suffix "Async". Es steht nur mittelbar mit dieser Klasse in
 * Verbindung. Die Erstellung und Pflege der Async Interfaces wird durch das
 * Google Plugin semiautomatisch unterstützt. Weitere Informationen unter
 * {@link VerwaltungsklasseAsync}.</li>
 * <li> {@link RemoteServiceServlet}: Jede Server-seitig instantiierbare und
 * Client-seitig über GWT RPC nutzbare Klasse muss die Klasse
 * <code>RemoteServiceServlet</code> implementieren. Sie legt die funktionale
 * Basis für die Anbindung von <code>VerwaltungsklasseImpl</code> an die Runtime
 * des GWT RPC-Mechanismus.</li>
 * </ol>
 * </p>
 * <p>
 * <b>Wichtiger Hinweis:</b> Diese Klasse bedient sich sogenannter
 * Mapper-Klassen. Sie gehören der Datenbank-Schicht an und bilden die
 * objektorientierte Sicht der Applikationslogik auf die relationale
 * organisierte Datenbank ab.
 * </p>
 * <p>
 * Beachten Sie, dass sämtliche Methoden, die mittels GWT RPC aufgerufen werden
 * können ein <code>throws IllegalArgumentException</code> in der
 * Methodendeklaration aufweisen. Diese Methoden dürfen also Instanzen von
 * {@link IllegalArgumentException} auswerfen. Mit diesen Exceptions können z.B.
 * Probleme auf der Server-Seite in einfacher Weise auf die Client-Seite
 * transportiert und dort systematisch in einem Catch-Block abgearbeitet werden.
 * </p>
 * 
 * @see Verwaltungsklasse
 * @see VerwaltungsklasseAsync
 * @see RemoteServiceServlet
 * @author Thies & L.Hofmann & Holz
 */

@SuppressWarnings("serial")
public class VerwaltungsklasseImpl extends RemoteServiceServlet 
implements Verwaltungsklasse {

	
	/**
	 * Standard StundenplaneintragID
	 */
	private static final long serialVersionUID = 7027992284251455305L;

	
	/**
	 * Instanzen der jeweiligen Mappern 
	 */
	private RaumMapper raumMapper = null;
	private DozentMapper dozentMapper = null;
	private LehrveranstaltungMapper lehrveranstaltungMapper = null;
	private SemesterverbandMapper semesterverbandMapper = null;
	private StundenplaneintragMapper stundenplaneintragMapper = null;
	private StundenplanMapper stundenplanMapper = null;
	private ZeitslotMapper zeitslotMapper = null;
	private StudiengangMapper studiengangMapper = null;
	
	
	/**
	 * Instanzen der jeweiligen Businessonjekte
	 */
	private Dozent dozent = null;
	private Raum raum = null;
	private Semesterverband semesterverband = null;
	private Studiengang studiengang = null;
	private Lehrveranstaltung lehrveranstaltung = null;
	private Stundenplaneintrag stundenplaneintrag = null;
	private Stundenplan stundenplan = null;
	private Zeitslot zeitslot = null;
	
	/*
	 **********************************************************************************
	 * ABSCHNITT, Beginn: Initialisierung
	 **********************************************************************************
	 */
	
	/**
	   * getter eines BusinessObjekt-Objekts
	   * 
	   * @return das jeweilige BusinessObjekt-Objekt
	   * @throws IllegalArgumentException
	   */
	
	public Stundenplaneintrag getStundenplaneintrag() throws IllegalArgumentException {
		return stundenplaneintrag;
	}
	
	public Stundenplan getStundenplan() throws IllegalArgumentException {
		return stundenplan;
	}
	
	public Lehrveranstaltung getLehrveranstaltung()  throws IllegalArgumentException {
		return lehrveranstaltung;
	}
	
	public Zeitslot getZeitslot()  throws IllegalArgumentException {
		return zeitslot;
	}
	
	public Dozent getDozent()  throws IllegalArgumentException {
		return dozent;
	}
	
	public Raum getRaum() throws IllegalArgumentException {
		return raum;
	}
	
	public Semesterverband getSemesterverband() throws IllegalArgumentException {
		return semesterverband;
	}

	public Studiengang getStudiengang() throws IllegalArgumentException {
		return studiengang;
	}
	
	/**
	   * setter des Stundenplaneintrag-Objekts
	   * 
	   * @return void
	   * @throws IllegalArgumentException
	   */

	public void setStundenplaneintrag(Stundenplaneintrag stundenplaneintrag) throws IllegalArgumentException {
		this.stundenplaneintrag = stundenplaneintrag;
	}

	public void setStundenplan(Stundenplan stundenplan)  throws IllegalArgumentException {
		this.stundenplan = stundenplan;
	}

	public void setLehrveranstaltung(Lehrveranstaltung lehrveranstaltung)  throws IllegalArgumentException {
		this.lehrveranstaltung = lehrveranstaltung;
	}

	public void setZeitslot(Zeitslot zeitslot) throws IllegalArgumentException {
		this.zeitslot = zeitslot;
	}

	public void setDozent(Dozent dozent) throws IllegalArgumentException {
		this.dozent = dozent;
	}

	public void setRaum(Raum raum) throws IllegalArgumentException {
		this.raum = raum;
	}

	public void setSemesterverband(Semesterverband semesterverband) throws IllegalArgumentException {
		this.semesterverband = semesterverband;
	}

	
	public void setStudiengang(Studiengang studiengang) throws IllegalArgumentException {
		this.studiengang = studiengang;
	}
	
	/*
	 *****************************************************************************************************
	 * ABSCHNITT, Beginn: Initialisierung
	 *****************************************************************************************************
	 */

	public VerwaltungsklasseImpl() throws IllegalArgumentException {
		
	  }
	
	public void init() throws IllegalArgumentException {
		
		this.dozentMapper = DozentMapper.dozentMapper();
		this.lehrveranstaltungMapper = LehrveranstaltungMapper.lehrveranstaltungMapper();
		this.semesterverbandMapper = SemesterverbandMapper.semesterverbandMapper();
		this.stundenplaneintragMapper = StundenplaneintragMapper.stundenplaneintragMapper();
		this.stundenplanMapper = StundenplanMapper.stundenplanMapper();
		this.zeitslotMapper = ZeitslotMapper.zeitslotMapper();
		this.raumMapper = RaumMapper.raumMapper();
		this.studiengangMapper = StudiengangMapper.studiengangMapper();
	}
	
	/*
	 ******************************************************************************************************
	 * ABSCHNITT, Beginn: getAll-Methoden
	 ******************************************************************************************************
	 */
	
	/**
	   * Auslesen aller jeweiligen BusinessObjekte
	   * 
	   * @return Vector aus allen jeweiligen BusinessObjekten
	   * @throws IllegalArgumentException
	   */
	public Vector<Dozent> getAllDozenten() throws IllegalArgumentException {
	    return this.dozentMapper.findAll();
	  }
	
	public Vector<Raum> getAllRaeume() throws IllegalArgumentException {
	    return this.raumMapper.findAll();
	  }
	
	public Vector<Lehrveranstaltung> getAllLehrveranstaltungen() throws IllegalArgumentException {
	    return this.lehrveranstaltungMapper.findAll();
	  }
	
	public Vector<Semesterverband> getAllSemesterverbaende() throws IllegalArgumentException {
	    return this.semesterverbandMapper.findAll();
	  }
	
	public Vector<Zeitslot> getAllZeitslots() throws IllegalArgumentException {
	    return this.zeitslotMapper.findAll();
	  }
	
	public Vector<Stundenplan> getAllStundenplaene() throws IllegalArgumentException {
	    return this.stundenplanMapper.findAll();
	  }
	
	public Vector<Stundenplaneintrag> getAllStundenplaneintraege() throws IllegalArgumentException {
	    return this.stundenplaneintragMapper.findAll();
	  }
	
	public Vector<Studiengang> getAllStudiengaenge() throws IllegalArgumentException {
	    return this.studiengangMapper.findAll();
	  }
	
	/*
	 *********************************************************************************************************
	 * ABSCHNITT, Beginn: get(All)Stundenplaneintraege-Methoden
	 *********************************************************************************************************
	 */
	
	/**
	   * Auslesen der Stundenplaneinträge der jeweiligen BO's
	   * 
	   * @param id des jeweiligen BO's
	   * @return Vector aller Stundenplaneintrag-Instanzen des BO's
	   * @throws IllegalArgumentException
	   */
	
	public Vector<Stundenplaneintrag> getAllStundenplaneintraegeByDozent(int dozentId) throws IllegalArgumentException {
		return this.stundenplaneintragMapper.findbyDozentId(dozentId);
	}
	
	public Vector<Stundenplaneintrag> getAllStundenplaneintraegeByLehrveranstaltung(int lehrveranstaltungId) throws IllegalArgumentException {
		return this.stundenplaneintragMapper.findbyLehrveranstaltungId(lehrveranstaltungId);
	}
	
	public Vector<Stundenplaneintrag> getAllStundenplaneintraegeByRaum(int raumId) throws IllegalArgumentException {
		return this.stundenplaneintragMapper.findbyRaumId(raumId);
	}
	
	public Vector<Stundenplaneintrag> getAllStundenplaneintraegeByStundenplan(int stundenplanId) throws IllegalArgumentException {
		return this.stundenplaneintragMapper.findbyStundenplanId(stundenplanId);
	}
	
	/*
	 **********************************************************************************
	 * ABSCHNITT, Beginn: getBoById-Methoden
	 **********************************************************************************
	 */	

	/**
	   * Auslesen der jeweiligen BO's über ihre ID
	   * 
	   * @param id des jeweiligen BO's
	   * @return das jeweilige BO-Objekt
	   * @throws IllegalArgumentException
	   */
	
	public Dozent getDozentById(int id) throws IllegalArgumentException {
		return this.dozentMapper.findByKey(id);
	}
	
	public Lehrveranstaltung getLehrveranstaltungById(int id)throws IllegalArgumentException {
		return this.lehrveranstaltungMapper.findByKey(id);
	}
	
	public Raum getRaumById(int id)throws IllegalArgumentException {
		return this.raumMapper.findByKey(id);
	}
	
	public Studiengang getStudiengangById(int id)throws IllegalArgumentException {
		return this.studiengangMapper.findByKey(id);
	}
	
	public Semesterverband getSemesterverbandById(int id)throws IllegalArgumentException {
		return this.semesterverbandMapper.findByKey(id);
	}
	
	public Stundenplaneintrag getStundenplaneintragById(int id)throws IllegalArgumentException {
		return this.stundenplaneintragMapper.findByKey(id);
	}
	
	public Stundenplan getStundenplanById(int id)throws IllegalArgumentException {
		return this.stundenplanMapper.findByKey(id);
	}
	
	public Zeitslot getZeitslotById(int id)throws IllegalArgumentException {
		return this.zeitslotMapper.findByKey(id);
	}
	
	/*
	 *****************************************************************************************************
	 * ABSCHNITT, Beginn: verschiedene Getter-Methoden
	 *****************************************************************************************************
	 */	
		
	/**
	   * Auslesen eines Stundenplaneintrag-Objekt über die ID des zugehörigen Raumes und Zeitslot.
	   * Ermittlung ob ein Raum zur gegebenen Zeit besetzt ist.
	   * 
	   * @param id des jeweiligen Raumes und Zeitslot
	   * @return Stundenplaneintrag-Objekten
	   * @throws IllegalArgumentException
	   */
	
	public Stundenplaneintrag getStundenplaneintragByRaumAndZeitslot(int raumId, int zeitslotId){
		return this.stundenplaneintragMapper.findByRaumAndZeitslot(raumId, zeitslotId);	
	}
	
	/**
	   * Auslesen eines Stundenplaneintrag-Objekt über die ID des zugehörigen Semesterverbands, Zeitslot und Stundenplanes
	   * Ermittlung ob ein Semesterverband zur gegebenen Zeit besetzt ist.
	   * 
	   * @param id des jeweiligen Semesterverbands, Zeitslot und Stundenplanes
	   * @return Stundenplaneintrag-Objekten
	   * @throws IllegalArgumentException
	   */
	
	public Stundenplaneintrag getStundenplaneintragBySemesterverbandAndZeitslot(
			int semesterverbandId, int zeitslotId, int stundenplanId){
	return this.stundenplaneintragMapper.findbySemesterverbandZeitslotAndStundenplan(semesterverbandId, zeitslotId, stundenplanId);
	}
	
	/**
	   * Auslesen eines Stundenplaneintrag-Objekt über die ID des zugehörigen Dozenten und Zeitslots
	   * Ermittlung ob ein Dozent zur gegebenen Zeit besetzt ist.
	   * 
	   * @param id des jeweiligen Dozenten und Zeitslots
	   * @return Stundenplaneintrag-Objekten
	   * @throws IllegalArgumentException
	   */
	
	public Stundenplaneintrag getStundenplaneintragByDozentAndZeitslot(
			int dozentId, int zeitslotId) throws IllegalArgumentException {
		
		return this.stundenplaneintragMapper.findByDozentAndZeitslot(dozentId, zeitslotId);
	}
	
	/**
	   * Auslesen eines Semesterverband-Objekt über die ID des zugehörigen Stundenplans
	   * 
	   * @param id des jeweiligen Stundenplans
	   * @return das Semesterverband-Objekt
	   * @throws IllegalArgumentException
	   */
	
	public Semesterverband getSemesterverbandByStundenplanId (int id) throws IllegalArgumentException {
		return this.semesterverbandMapper.findByStundenplanId(id);
	}
	
	/**
	   * Auslesen eines Vectors aus Semesterverband-Objekten über die ID des zugehörigen Studienganges
	   * 
	   * @param id des jeweiligen Studienganges
	   * @return Vector aus den Semesterverbands-Objekten
	   * @throws IllegalArgumentException
	   */
	
	public Vector<Semesterverband> getSemsterverbaendeByStudiengang(int studiengangId){
		return this.semesterverbandMapper.findByStudiengangId(studiengangId);
	}
	
	/**
	   * Auslesen eines Vectors aus Stundenplan-Objekten über die ID des zugehörigen Semesterverbands
	   * 
	   * @param id des jeweiligen Semesterverbands
	   * @return Vector aus den Stundenplan-Objekten
	   * @throws IllegalArgumentException
	   */
	
	public Vector<Stundenplan> getStundenplaeneBySemesterverband (int semesterverbandId){
		return this.stundenplanMapper.findBySemesterverband(semesterverbandId);
	}
	
	/**
	   * Auslesen eines Stundiengang-Objekts über die ID des zugehörigen Semesterverbands
	   * 
	   * @param id des jeweiligen Semesterverbands
	   * @return Stundenplaneintrag-Objekten
	   * @throws IllegalArgumentException
	   */
	
	public Studiengang getStudiengangBySemesterverbandId(int SemesterverbandId){
		return this.studiengangMapper.findBySemesterverbandId(SemesterverbandId);
	}
	
	/*
	 *********************************************************************************************************************
	 * ABSCHNITT, Beginn: create-Methoden
	 *********************************************************************************************************************
	 */	
	
	/**
	   * create-Methoden zum Anlegen eines Dozenten
	   * Anschliessende Überprüfung auf ungültige Eingabe 
	   * 
	   * @param Vorname und Nachname des jeweiligen Dozenten
	   * @return Das jeweilige Dozenten-Objekt
	   * @throws IllegalArgumentException
	   */
	
	public Dozent createDozent(String vorname, String nachname)
			throws IllegalArgumentException {
		
	if (vorname.matches("[0-9]+" ) || nachname.matches("[0-9]+")){
		
		throw new IllegalArgumentException("ungültige Eingabe!");
		}
	else {		
			
		Dozent a = new Dozent();
		a.setVorname(vorname);
		a.setNachname(nachname);
	
		a.setId(1);
		return this.dozentMapper.insert(a);	
		}
	}
	
	/**
	   * create-Methoden zum Anlegen einer Lehrveranstaltung
	   * 
	   * @param Bezeichnung, Semester und Umfang der Lehrveranstaltung
	   * @return Das jeweilige Lehrveranstaltungs-Objekt
	   * @throws IllegalArgumentException
	   */
	
	public Lehrveranstaltung createLehrveranstaltung(
			String bezeichnung, int semester, int umfang)
			throws IllegalArgumentException {

		Lehrveranstaltung a = new Lehrveranstaltung();
		a.setBezeichnung(bezeichnung);
		a.setSemester(semester);
		a.setUmfang(umfang);
		
		a.setId(1);
		
		return this.lehrveranstaltungMapper.insert(a);
		}
	
	/**
	   * create-Methoden zum Anlegen eines Raumes
	   * 
	   * @param Bezeichnung und Kapazität des Raumes
	   * @return Das jeweilige Raum-Objekt
	   * @throws IllegalArgumentException
	   */
	
	public Raum createRaum(String bezeichnung, int kapazitaet)
			throws IllegalArgumentException {
		Raum a = new Raum();
		a.setBezeichnung(bezeichnung);
		a.setKapazitaet(kapazitaet);
		
		a.setId(1);

		return this.raumMapper.insert(a);
	}
	
	/**
	   * create-Methoden zum Anlegen eines Studienganges
	   * Anschliessende Prüfung auf gültige Eingabe
	   * 
	   * @param Bezeichnung des Studienganges
	   * @return Das jeweilige Raum-Objekt
	   * @throws IllegalArgumentException
	   */

	public Studiengang createStudiengang(String bezeichnung)
			throws IllegalArgumentException {
		
		if (bezeichnung.matches("[0-9]+")){
			
			throw new IllegalArgumentException("ungültige Eingabe!");
		}
		else {	
			Studiengang s = new Studiengang();
			
			s.setBezeichnung(bezeichnung);
			
			s.setId(1);
			
			return this.studiengangMapper.insert(s);
				
		}
	}
	
	/**
	   * create-Methoden zum Anlegen eines Stundenplaneintrages
	   * Anschliessende Prüfung ob die ausgewählten BO's in der Datenbank vorhanden sind 
	   * und somit verwendet werden können.
	   * 
	   * @param die jeweiligen Fremdschlüssel der BO's
	   * @return Das jeweilige Stundenplaneintrag-Objekt
	   * @throws IllegalArgumentException
	   */

	public Stundenplaneintrag createStundenplaneintrag(int d,int l, int r, int z, int sp) 
					throws IllegalArgumentException {
		
	Dozent dozent = this.getDozentById(d);
	Lehrveranstaltung lehrveranstaltung = this.getLehrveranstaltungById(l);	
	Raum raum = this.getRaumById(r);
	Zeitslot zeitslot = this.getZeitslotById(z);
	Stundenplan stundenplan = this.getStundenplanById(sp);
	
		
	if( dozent == null || lehrveranstaltung == null || raum == null || zeitslot == null || stundenplan == null){
		
		throw new IllegalArgumentException("Eines der ausgewählten Objekte existiert nicht mehr+"
				+ "und kann somit nicht für ein Stundenplaneintrag ausgewählt werden.");
		
	} 
	else {
			
		Stundenplaneintrag s = new Stundenplaneintrag();
		
		s.setDozentId(d);
		s.setLehrveranstaltungId(l);
		s.setRaumId(r);
		s.setZeitslotId(z);
		s.setStundenplanId(sp);
		
		int semesterverbandId = this.getSemesterverbandByStundenplanId(sp).getId();
		String bezeichnung = this.getStudiengangBySemesterverbandId(semesterverbandId).getBezeichnung();
		String abkuerzung = bezeichnung.substring(0, 2)+" , "+this.getLehrveranstaltungById(l).getBezeichnung();
		s.setAbkuerzung(abkuerzung);
		
		s.setId(1);
		
		return this.stundenplaneintragMapper.insert(s);
		}
	}
	
	/**
	   * create-Methoden zum Anlegen eines Semesterverbands
	   * Anschliessende Prüfung auf gültige Eingabe (Entweder: SS 2014 Oder: WS 2014/2015)
	   * 
	   * @param Semester, Jahrgang und StudierendenAnzahl und der Fremdschlüssel des Studienganges
	   * @return Das jeweilige Semesterverband-Objekt
	   * @throws IllegalArgumentException
	   */

	public Semesterverband createSemesterverband(int studiengangId,
			int semester, int studierendenAnzahl, String jahrgang)
			throws IllegalArgumentException {
		
		if (jahrgang.matches("[A-Z]+"+" "+"[0-9]+") || jahrgang.matches("[A-Z]+"+" "+"[0-9]+"+"/"+"[0-9]+")){
		
		Semesterverband a = new Semesterverband();
		a.setStudiengangId(studiengangId);
		a.setSemester(semester);
		a.setStudierendenAnzahl(studierendenAnzahl);
		a.setJahrgang(jahrgang);
		
		String bezeichnung = this.getStudiengangById(studiengangId).getBezeichnung();
		String kuerzel = bezeichnung.substring(0, 2);
		a.setKuerzel(kuerzel);
		
		
		a.setId(1);
		
		return this.semesterverbandMapper.insert(a);
		
		} else {
			
			throw new IllegalArgumentException("ungültige Eingabe des jahrgangs!");
		}
		
	}
	
	/**
	   * create-Methoden zum Anlegen eines Stundenplans
	   * Anschliessende Prüfung auf gültige Eingabe (Entweder: SS 2014 Oder: WS 2014/2015)
	   * 
	   * @param Studienhalbjahr und der Fremdschlüssel des Semesterverbands
	   * @return Das jeweilige Stundenplan-Objekt
	   * @throws IllegalArgumentException
	   */

	public Stundenplan createStundenplan(String studienhalbjahr, int semesterverbandId) throws IllegalArgumentException {
		
		if (studienhalbjahr.matches("[A-Z]+"+" "+"[0-9]+") || studienhalbjahr.matches("[A-Z]+"+" "+"[0-9]+"+"/"+"[0-9]+")){
			
		
		Stundenplan sp = new Stundenplan();
		
		sp.setStudienhalbjahr(studienhalbjahr);
		sp.setSemesterverbandId(semesterverbandId);
		
		sp.setId(1);
		
		return this.stundenplanMapper.insert(sp);
		
	} else {
		
		throw new IllegalArgumentException("ungültige Eingabe!");
	}
	}
	
	/*
	 *****************************************************************************************************
	 * ABSCHNITT, Beginn: Delete-Methoden
	 *****************************************************************************************************
	 */	
	
	/**
	   * delete-Methoden zum Löschen eines BO's
	   * Anschliessende Prüfung, ob für den jeweiligen BO's noch Stundenplaneinträge vorhanden sind
	   * 
	   * @param das jeweilige BO-Objekt
	   * @return boolean
	   * @throws IllegalArgumentException
	   */

	public boolean deleteDozent(Dozent d) throws IllegalArgumentException {
		
		Vector<Stundenplaneintrag> dozenten = this.getAllStundenplaneintraegeByDozent(d.getId());

		    if (dozenten != null) {
		    	return false;
		    } else {
		   	this.dozentMapper.delete(d);
		   	return true;
	    }
	}
	
	public boolean deleteLehrveranstaltung(Lehrveranstaltung a)
			throws IllegalArgumentException {
		
		Vector<Stundenplaneintrag> lvs = this.getAllStundenplaneintraegeByLehrveranstaltung(a.getId());

		    if (lvs != null) {
		    	return false;
		    } else {
		   	this.lehrveranstaltungMapper.delete(a);
		   	return true;
	    }
	}

	public boolean deleteStundenplan(Stundenplan sp) throws IllegalArgumentException {
	Vector<Stundenplaneintrag> sps = this.getAllStundenplaneintraegeByStundenplan(sp.getId());

	    if (sps != null) {
	    	return false;
	    } else {
		   	this.stundenplanMapper.delete(sp);
		   	return true;
	    }	
		   	}

	public boolean deleteRaum(Raum a) throws IllegalArgumentException {
	Vector<Stundenplaneintrag> r = this.getAllStundenplaneintraegeByRaum(a.getId());

		    if (r != null) {
	    	return false;
	    } else {
		   	this.raumMapper.delete(a);
		   	return true;
	    }
	}
	
	public boolean deleteStudiengang(Studiengang studiengang)
			throws IllegalArgumentException {

		   	this.studiengangMapper.delete(studiengang);
		   	return true;
	}

	public boolean deleteStundenplaneintrag(Stundenplaneintrag s)
			throws IllegalArgumentException {
		
		   	this.stundenplaneintragMapper.delete(s);
		   	return true;	
		   	}

	public boolean deleteSemesterverband(Semesterverband a)
			throws IllegalArgumentException {

		   	this.semesterverbandMapper.delete(a);
		   	return true;
	}
	
	/*
	 *****************************************************************************************************
	 * ABSCHNITT, Beginn: Change-Methoden
	 *****************************************************************************************************
	 */	
	
	/**
	   * change-Methoden zum Ändern eines BO's
	   * Anschliessende Prüfung auf gültige Eingabe 
	   * 
	   * @param das jeweilige BO-Objekt
	   * @return void
	   * @throws IllegalArgumentException
	   */
	public void changeDozent(Dozent d) throws IllegalArgumentException {
		
		if (d.getVorname().matches("[0-9]+" ) || d.getNachname().matches("[0-9]+")){
			
			throw new IllegalArgumentException("ungültige Eingabe!");
			}
		else {		
				
		this.dozentMapper.update(d);}
	}
	

	public void changeSemsterverband(Semesterverband sv)
			throws IllegalArgumentException {
		
		
		if (sv.getJahrgang().matches("[A-Z]+"+" "+"[0-9]+") || sv.getJahrgang().matches("[A-Z]+"+" "+"[0-9]+"+"/"+"[0-9]+")){
			
			this.semesterverbandMapper.update(sv);
			
			} else {
				
				throw new IllegalArgumentException("ungültige Eingabe des jahrgangs!");
			}
		
	}

	public void changeStudiengang(Studiengang s)
			throws IllegalArgumentException {
		
		if (s.getBezeichnung().matches("[0-9]+")){
			
			throw new IllegalArgumentException("ungültige Eingabe!");
		}
		else {	
		
		this.studiengangMapper.update(s);
		}
	}

	public void changeStundenplan(Stundenplan sp) throws IllegalArgumentException {
		
		if (sp.getStudienhalbjahr().matches("[A-Z]+"+" "+"[0-9]+") || sp.getStudienhalbjahr().matches("[A-Z]+"+" "+"[0-9]+"+"/"+"[0-9]+")){
			
			this.stundenplanMapper.update(sp);
			
		} else {
			
			throw new IllegalArgumentException("ungültige Eingabe!");
		}
		
	}

	public void changeStundenplaneintrag(Stundenplaneintrag s)
			throws IllegalArgumentException {
		this.stundenplaneintragMapper.update(s);
	}

	public void changeLehrveranstaltung(Lehrveranstaltung l)
			throws IllegalArgumentException {
		this.lehrveranstaltungMapper.update(l);
	}

	public void changeRaum(Raum r) throws IllegalArgumentException {
		this.raumMapper.update(r);
	}

}
