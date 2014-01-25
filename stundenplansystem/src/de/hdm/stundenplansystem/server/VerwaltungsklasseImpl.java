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
 * s�mtliche Applikationslogik (oder engl. Business Logic) aggregiert. Sie ist
 * wie eine Spinne, die s�mtliche Zusammenh�nge in ihrem Netz (in unserem Fall
 * die Daten der Applikation) �berblickt und f�r einen geordneten Ablauf und
 * dauerhafte Konsistenz der Daten und Abl�ufe sorgt.
 * </p>
 * <p>
 * Die Applikationslogik findet sich in den Methoden dieser Klasse. Jede dieser
 * Methoden kann als <em>Transaction Script</em> bezeichnet werden. Dieser Name
 * l�sst schon vermuten, dass hier analog zu Datenbanktransaktion pro
 * Transaktion gleiche mehrere Teilaktionen durchgef�hrt werden, die das System
 * von einem konsistenten Zustand in einen anderen, auch wieder konsistenten
 * Zustand �berf�hren. Wenn dies zwischenzeitig scheitern sollte, dann ist das
 * jeweilige Transaction Script daf�r verwantwortlich, eine Fehlerbehandlung
 * durchzuf�hren.
 * </p>
 * <p>
 * Diese Klasse steht mit einer Reihe weiterer Datentypen in Verbindung. Dies
 * sind:
 * <ol>
 * <li>{@link Verwaltungsklasse}: Dies ist das <em>lokale</em> - also
 * Server-seitige - Interface, das die im System zur Verf�gung gestellten
 * Funktionen deklariert.</li>
 * <li>{@link VerwaltungsklasseAsync}: <code>VerwaltungsklasseImpl</code> und
 * <code>Verwaltunsklasse</code> bilden nur die Server-seitige Sicht der
 * Applikationslogik ab. Diese basiert vollst�ndig auf synchronen
 * Funktionsaufrufen. Wir m�ssen jedoch in der Lage sein, Client-seitige
 * asynchrone Aufrufe zu bedienen. Dies bedingt ein weiteres Interface, das in
 * der Regel genauso benannt wird, wie das synchrone Interface, jedoch mit dem
 * zus�tzlichen Suffix "Async". Es steht nur mittelbar mit dieser Klasse in
 * Verbindung. Die Erstellung und Pflege der Async Interfaces wird durch das
 * Google Plugin semiautomatisch unterst�tzt. Weitere Informationen unter
 * {@link VerwaltungsklasseAsync}.</li>
 * <li> {@link RemoteServiceServlet}: Jede Server-seitig instantiierbare und
 * Client-seitig �ber GWT RPC nutzbare Klasse muss die Klasse
 * <code>RemoteServiceServlet</code> implementieren. Sie legt die funktionale
 * Basis f�r die Anbindung von <code>VerwaltungsklasseImpl</code> an die Runtime
 * des GWT RPC-Mechanismus.</li>
 * </ol>
 * </p>
 * <p>
 * <b>Wichtiger Hinweis:</b> Diese Klasse bedient sich sogenannter
 * Mapper-Klassen. Sie geh�ren der Datenbank-Schicht an und bilden die
 * objektorientierte Sicht der Applikationslogik auf die relationale
 * organisierte Datenbank ab.
 * </p>
 * <p>
 * Beachten Sie, dass s�mtliche Methoden, die mittels GWT RPC aufgerufen werden
 * k�nnen ein <code>throws IllegalArgumentException</code> in der
 * Methodendeklaration aufweisen. Diese Methoden d�rfen also Instanzen von
 * {@link IllegalArgumentException} auswerfen. Mit diesen Exceptions k�nnen z.B.
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
	 * Referenz auf das zugeh�rige BusinessObjekt-Objekt.
	 */
	private Dozent dozent = null;
	private Raum raum = null;
	private Semesterverband semesterverband = null;
	private Studiengang studiengang = null;
	private Lehrveranstaltung lehrveranstaltung = null;
	private Stundenplaneintrag stundenplaneintrag = null;
	private Stundenplan stundenplan = null;
	private Zeitslot zeitslot = null;

	/**
	 * Referenzen auf die DatenbankMapper, welche die BusinessObjekte-Objekte
	 * mit der Datenbank abgleicht.
	 */
	private RaumMapper raumMapper = null;
	private DozentMapper dozentMapper = null;
	private LehrveranstaltungMapper lehrveranstaltungMapper = null;
	private SemesterverbandMapper semesterverbandMapper = null;
	private StundenplaneintragMapper stundenplaneintragMapper = null;
	private StundenplanMapper stundenplanMapper = null;
	private ZeitslotMapper zeitslotMapper = null;
	private StudiengangMapper studiengangMapper = null;

	/*
	 * Da diese Klasse ein gewisse Gr��e besitzt - dies ist eigentlich ein
	 * Hinweise, dass hier eine weitere Gliederung sinnvoll ist - haben wir zur
	 * besseren �bersicht Abschnittskomentare eingef�gt. Sie leiten ein Cluster
	 * in irgeneinerweise zusammengeh�riger Methoden ein. Ein entsprechender
	 * Kommentar steht am Ende eines solchen Clusters.
	 */

	/*
	 * ***************************************************************************
	 * ABSCHNITT, Beginn: Initialisierung
	 * ***************************************
	 * ************************************
	 */
	/**
	 * <p>
	 * Ein <code>RemoteServiceServlet</code> wird unter GWT mittels
	 * <code>GWT.create(Klassenname.class)</code> Client-seitig erzeugt. Hierzu
	 * ist ein solcher No-Argument-Konstruktor anzulegen. Ein Aufruf eines
	 * anderen Konstruktors ist durch die Client-seitige Instantiierung durch
	 * <code>GWT.create(Klassenname.class)</code> nach derzeitigem Stand nicht
	 * m�glich.
	 * </p>
	 * <p>
	 * Es bietet sich also an, eine separate Instanzenmethode zu erstellen, die
	 * Client-seitig direkt nach <code>GWT.create(Klassenname.class)</code>
	 * aufgerufen wird, um eine Initialisierung der Instanz vorzunehmen.
	 * </p>
	 * 
	 * @see #init()
	 */
	public VerwaltungsklasseImpl() throws IllegalArgumentException {
	}

	/*
	 * Eine weitergehende Funktion muss der No-Argument-Constructor nicht haben.
	 * Er muss einfach vorhanden sein.
	 */

	/**
	 * Initialsierungsmethode. Siehe dazu Anmerkungen zum
	 * No-Argument-Konstruktor {@link #ReportGeneratorImpl()}. Diese Methode
	 * muss f�r jede Instanz von <code>VerwaltungsklasseImpl</code> aufgerufen
	 * werden.
	 * 
	 * @see #ReportGeneratorImpl()
	 */
	public void init() throws IllegalArgumentException {
		/*
		 * Ganz wesentlich ist, dass die BankAdministration einen vollst�ndigen
		 * Satz von Mappern besitzt, mit deren Hilfe sie dann mit der Datenbank
		 * kommunizieren kann.
		 */
		this.dozentMapper = DozentMapper.dozentMapper();
		this.lehrveranstaltungMapper = LehrveranstaltungMapper
				.lehrveranstaltungMapper();
		this.semesterverbandMapper = SemesterverbandMapper
				.semesterverbandMapper();
		this.stundenplaneintragMapper = StundenplaneintragMapper
				.stundenplaneintragMapper();
		this.stundenplanMapper = StundenplanMapper
				.stundenplanMapper();
		this.zeitslotMapper = ZeitslotMapper.zeitslotMapper();
		this.raumMapper = RaumMapper.raumMapper();
		this.studiengangMapper = StudiengangMapper
				.studiengangMapper();
	}

	/*
	 * *********************************************************************************
	 * ABSCHNITT, Beginn: der Auslesen und Setzen Methoden eines
	 * BusinessObjektes
	 * **********************************************************
	 * ***********************
	 */

	/**
	 * Auslesen eines BusinessObjekt-Objekts
	 * 
	 * @return das jeweilige BusinessObjekt-Objekt
	 * @throws IllegalArgumentException
	 */

	public Stundenplaneintrag getStundenplaneintrag()
			throws IllegalArgumentException {
		return stundenplaneintrag;
	}

	public Stundenplan getStundenplan()
			throws IllegalArgumentException {
		return stundenplan;
	}

	public Lehrveranstaltung getLehrveranstaltung()
			throws IllegalArgumentException {
		return lehrveranstaltung;
	}

	public Zeitslot getZeitslot() throws IllegalArgumentException {
		return zeitslot;
	}

	public Dozent getDozent() throws IllegalArgumentException {
		return dozent;
	}

	public Raum getRaum() throws IllegalArgumentException {
		return raum;
	}

	public Semesterverband getSemesterverband()
			throws IllegalArgumentException {
		return semesterverband;
	}

	public Studiengang getStudiengang()
			throws IllegalArgumentException {
		return studiengang;
	}

	/**
	 * Setzen des BusinessObjekt-Objekts
	 * 
	 * @return void
	 * @throws IllegalArgumentException
	 */

	public void setStundenplaneintrag(
			Stundenplaneintrag stundenplaneintrag)
			throws IllegalArgumentException {
		this.stundenplaneintrag = stundenplaneintrag;
	}

	public void setStundenplan(Stundenplan stundenplan)
			throws IllegalArgumentException {
		this.stundenplan = stundenplan;
	}

	public void setLehrveranstaltung(
			Lehrveranstaltung lehrveranstaltung)
			throws IllegalArgumentException {
		this.lehrveranstaltung = lehrveranstaltung;
	}

	public void setZeitslot(Zeitslot zeitslot)
			throws IllegalArgumentException {
		this.zeitslot = zeitslot;
	}

	public void setDozent(Dozent dozent)
			throws IllegalArgumentException {
		this.dozent = dozent;
	}

	public void setRaum(Raum raum) throws IllegalArgumentException {
		this.raum = raum;
	}

	public void setSemesterverband(Semesterverband semesterverband)
			throws IllegalArgumentException {
		this.semesterverband = semesterverband;
	}

	public void setStudiengang(Studiengang studiengang)
			throws IllegalArgumentException {
		this.studiengang = studiengang;
	}

	/*
	 * *****************************************************************************************************
	 * ABSCHNITT, Beginn: getAll-Methoden
	 * ****************************************
	 * *************************************************************
	 */

	/**
	 * Auslesen aller jeweiligen BusinessObjekte
	 * 
	 * @return Vector aus allen jeweiligen BusinessObjekten
	 * @throws IllegalArgumentException
	 */
	public Vector<Dozent> getAllDozenten()
			throws IllegalArgumentException {
		return this.dozentMapper.findAll();
	}

	public Vector<Raum> getAllRaeume()
			throws IllegalArgumentException {
		return this.raumMapper.findAll();
	}

	public Vector<Lehrveranstaltung> getAllLehrveranstaltungen()
			throws IllegalArgumentException {
		return this.lehrveranstaltungMapper.findAll();
	}

	public Vector<Semesterverband> getAllSemesterverbaende()
			throws IllegalArgumentException {
		return this.semesterverbandMapper.findAll();
	}

	public Vector<Zeitslot> getAllZeitslots()
			throws IllegalArgumentException {
		return this.zeitslotMapper.findAll();
	}

	public Vector<Stundenplan> getAllStundenplaene()
			throws IllegalArgumentException {
		return this.stundenplanMapper.findAll();
	}

	public Vector<Stundenplaneintrag> getAllStundenplaneintraege()
			throws IllegalArgumentException {
		return this.stundenplaneintragMapper.findAll();
	}

	public Vector<Studiengang> getAllStudiengaenge()
			throws IllegalArgumentException {
		return this.studiengangMapper.findAll();
	}

	/*
	 * ********************************************************************************************************
	 * ABSCHNITT, Beginn: get(All)Stundenplaneintraege-Methoden
	 * ******************
	 * ********************************************************
	 * ******************************
	 */

	/**
	 * Auslesen der Stundenplaneintr�ge der jeweiligen BusinessObjekts
	 * 
	 * @param id
	 *            des jeweiligen BusinessObjekts
	 * @return Vector aller Stundenplaneintrag-Instanzen des BusniessObjekts
	 * @throws IllegalArgumentException
	 */

	public Vector<Stundenplaneintrag> getAllStundenplaneintraegeByDozent(
			int dozentId) throws IllegalArgumentException {
		return this.stundenplaneintragMapper.findbyDozentId(dozentId);
	}

	public Vector<Stundenplaneintrag> getAllStundenplaneintraegeByLehrveranstaltung(
			int lehrveranstaltungId) throws IllegalArgumentException {
		return this.stundenplaneintragMapper
				.findbyLehrveranstaltungId(lehrveranstaltungId);
	}

	public Vector<Stundenplaneintrag> getAllStundenplaneintraegeByRaum(
			int raumId) throws IllegalArgumentException {
		return this.stundenplaneintragMapper.findbyRaumId(raumId);
	}

	public Vector<Stundenplaneintrag> getAllStundenplaneintraegeByStundenplan(
			int stundenplanId) throws IllegalArgumentException {
		return this.stundenplaneintragMapper
				.findbyStundenplanId(stundenplanId);
	}

	/*
	 * *********************************************************************************
	 * ABSCHNITT, Beginn: getBoById-Methoden
	 * *************************************
	 * ********************************************
	 */

	/**
	 * Auslesen der jeweiligen BusniessObjekts �ber ihre ID
	 * 
	 * @param id
	 *            des jeweiligen BusniessObjekts
	 * @return das jeweilige BusniessObjekt-Objekt
	 * @throws IllegalArgumentException
	 */

	public Dozent getDozentById(int id)
			throws IllegalArgumentException {
		return this.dozentMapper.findByKey(id);
	}

	public Lehrveranstaltung getLehrveranstaltungById(int id)
			throws IllegalArgumentException {
		return this.lehrveranstaltungMapper.findByKey(id);
	}

	public Raum getRaumById(int id) throws IllegalArgumentException {
		return this.raumMapper.findByKey(id);
	}

	public Studiengang getStudiengangById(int id)
			throws IllegalArgumentException {
		return this.studiengangMapper.findByKey(id);
	}

	public Semesterverband getSemesterverbandById(int id)
			throws IllegalArgumentException {
		return this.semesterverbandMapper.findByKey(id);
	}

	public Stundenplaneintrag getStundenplaneintragById(int id)
			throws IllegalArgumentException {
		return this.stundenplaneintragMapper.findByKey(id);
	}

	public Stundenplan getStundenplanById(int id)
			throws IllegalArgumentException {
		return this.stundenplanMapper.findByKey(id);
	}

	public Zeitslot getZeitslotById(int id)
			throws IllegalArgumentException {
		return this.zeitslotMapper.findByKey(id);
	}

	/*
	 * ****************************************************************************************************
	 * ABSCHNITT, Beginn: verschiedene Auslese-Methoden
	 * **************************
	 * ************************************************
	 * **************************
	 */

	/**
	 * Auslesen eines Stundenplaneintrag-Objekt �ber die ID des zugeh�rigen
	 * Raumes und Zeitslot. Ermittlung ob ein Raum zur gegebenen Zeit besetzt
	 * ist.
	 * 
	 * @param id
	 *            des jeweiligen Raumes und Zeitslot
	 * @return das jeweilige Stundenplaneintrag-Objekten
	 * @throws IllegalArgumentException
	 */

	public Stundenplaneintrag getStundenplaneintragByRaumAndZeitslot(
			int raumId, int zeitslotId) {
		return this.stundenplaneintragMapper.findByRaumAndZeitslot(
				raumId, zeitslotId);
	}

	/**
	 * Auslesen eines Stundenplaneintrag-Objekt �ber die ID des zugeh�rigen
	 * Semesterverbands, Zeitslot und Stundenplanes Ermittlung ob ein
	 * Semesterverband zur gegebenen Zeit besetzt ist.
	 * 
	 * @param id
	 *            des jeweiligen Semesterverbands, Zeitslot und Stundenplanes
	 * @return das jeweilige Stundenplaneintrag-Objekten
	 * @throws IllegalArgumentException
	 */

	public Stundenplaneintrag getStundenplaneintragBySemesterverbandAndZeitslot(
			int semesterverbandId, int zeitslotId, int stundenplanId) {
		return this.stundenplaneintragMapper
				.findbySemesterverbandZeitslotAndStundenplan(
						semesterverbandId, zeitslotId, stundenplanId);
	}

	/**
	 * Auslesen eines Stundenplaneintrag-Objekt �ber die ID des zugeh�rigen
	 * Dozenten und Zeitslots Ermittlung ob ein Dozent zur gegebenen Zeit
	 * besetzt ist.
	 * 
	 * @param id
	 *            des jeweiligen Dozenten und Zeitslots
	 * @return das jeweilige Stundenplaneintrag-Objekten
	 * @throws IllegalArgumentException
	 */

	public Stundenplaneintrag getStundenplaneintragByDozentAndZeitslot(
			int dozentId, int zeitslotId)
			throws IllegalArgumentException {

		return this.stundenplaneintragMapper.findByDozentAndZeitslot(
				dozentId, zeitslotId);
	}

	/**
	 * Auslesen eines Semesterverband-Objekt �ber die ID des zugeh�rigen
	 * Stundenplans
	 * 
	 * @param id
	 *            des jeweiligen Stundenplans
	 * @return das jeweilige Semesterverband-Objekt
	 * @throws IllegalArgumentException
	 */

	public Semesterverband getSemesterverbandByStundenplanId(int id)
			throws IllegalArgumentException {
		return this.semesterverbandMapper.findByStundenplanId(id);
	}

	/**
	 * Auslesen eines Vectors aus Semesterverband-Objekten �ber die ID des
	 * zugeh�rigen Studienganges
	 * 
	 * @param id
	 *            des jeweiligen Studienganges
	 * @return Vector aus den Semesterverbands-Objekten
	 * @throws IllegalArgumentException
	 */

	public Vector<Semesterverband> getSemsterverbaendeByStudiengang(
			int studiengangId) {
		return this.semesterverbandMapper
				.findByStudiengangId(studiengangId);
	}

	/**
	 * Auslesen eines Vectors aus Stundenplan-Objekten �ber die ID des
	 * zugeh�rigen Semesterverbands
	 * 
	 * @param id
	 *            des jeweiligen Semesterverbands
	 * @return Vector aus den Stundenplan-Objekten
	 * @throws IllegalArgumentException
	 */

	public Vector<Stundenplan> getStundenplaeneBySemesterverband(
			int semesterverbandId) {
		return this.stundenplanMapper
				.findBySemesterverband(semesterverbandId);
	}

	/**
	 * Auslesen eines Stundiengang-Objekts �ber die ID des zugeh�rigen
	 * Semesterverbands
	 * 
	 * @param id
	 *            des jeweiligen Semesterverbands
	 * @return Das je weiligeStundenplaneintrag-Objekt
	 * @throws IllegalArgumentException
	 */

	public Studiengang getStudiengangBySemesterverbandId(
			int semesterverbandId) {
		return this.studiengangMapper
				.findBySemesterverbandId(semesterverbandId);
	}

	/**
	 * Auslesen freier Zeitslot-Objekts f�r einen Stundenplaneintrag 
	 * im Bezug zur RaumID , DozentID und StundenplanID
	 * 
	 * @param id
	 *            des jeweiligen Raumes
	 * @param id
	 *            des jeweiligen Dozenten
	 * @param id
	 *            des jeweiligen Semesterverbandes
	 * @return Vector aus den Zeitslot-Objekten
	 * @throws IllegalArgumentException
	 */

	public Vector<Zeitslot> getFreieZeitslot(int raumId,
			int dozentId, int stundenplanId) {
		return this.zeitslotMapper.findFreeZeitslots(raumId,
				dozentId, stundenplanId);
	}

	/*
	 * **************************************************************
	 * ABSCHNITT, Beginn: create-Methoden
	 * **************************************************************
	 */

	/**
	 * create-Methoden zum Anlegen eines Dozenten 
	 * Anschliessende Überprüfung auf ungültige Eingabe
	 * 
	 * @param Vorname
	 *            und Nachname des jeweiligen Dozenten
	 * @return Das jeweilige Dozenten-Objekt
	 * @throws IllegalArgumentException
	 */

	public Dozent createDozent(String vorname, String nachname)
			throws IllegalArgumentException {

		if (vorname.matches("[0-9]+") || nachname.matches("[0-9]+")) 
		{

			throw new IllegalArgumentException(
					"ung�ltige Eingabe! Bitte verwenden Sie keine "
					+ "Ziffern f�r den Vor- und Nachnamen.");
		} else {

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
	 * @param Bezeichnung
	 *            , Semester und Umfang der Lehrveranstaltung
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
	 * @param Bezeichnung
	 *            des Studienganges
	 * @return Das jeweilige Raum-Objekt
	 * @throws IllegalArgumentException
	 */

	public Studiengang createStudiengang(String bezeichnung)
			throws IllegalArgumentException {

		if (bezeichnung.matches("[0-9]+")) {

			throw new IllegalArgumentException("ung�ltige Eingabe!");
		} else {
			Studiengang s = new Studiengang();

			s.setBezeichnung(bezeichnung);

			s.setId(1);

			return this.studiengangMapper.insert(s);

		}
	}

	/**
	 * create-Methoden zum Anlegen eines Stundenplaneintrages 
	 * Anschliessende Prüfung ob die ausgewählten BusniessObjekts 
	 * in der Datenbank vorhanden sind und somit verwendet werden 
	 * können.
	 * 
	 * @param die jeweiligen Fremdschlüssel der BusniessObjekts
	 * @return Das jeweilige Stundenplaneintrag-Objekt
	 * @throws IllegalArgumentException
	 */

	public Stundenplaneintrag createStundenplaneintrag(int d, int l,
			int r, int z, int sp) throws IllegalArgumentException {

		Dozent dozent = this.getDozentById(d);
		Lehrveranstaltung lehrveranstaltung = this
				.getLehrveranstaltungById(l);
		Raum raum = this.getRaumById(r);
		Zeitslot zeitslot = this.getZeitslotById(z);
		Stundenplan stundenplan = this.getStundenplanById(sp);

		if (dozent == null || lehrveranstaltung == null
				|| raum == null || zeitslot == null
				|| stundenplan == null) {

			throw new IllegalArgumentException(
					"Eines der ausgew�hlten Objekte existiert "
					+ "nicht mehr und kann somit nicht f�r ein "
					+ "Stundenplaneintrag ausgew�hlt werden.");
			
		} else {

			Stundenplaneintrag s = new Stundenplaneintrag();

			s.setDozentId(d);
			s.setLehrveranstaltungId(l);
			s.setRaumId(r);
			s.setZeitslotId(z);
			s.setStundenplanId(sp);

			int semesterverbandId = this
					.getSemesterverbandByStundenplanId(sp).getId();
			String bezeichnung = this
					.getStudiengangBySemesterverbandId(
							semesterverbandId).getBezeichnung();
			String abkuerzung = bezeichnung.substring(0, 2)
					+ " , "
					+ this.getLehrveranstaltungById(l)
							.getBezeichnung();
			s.setAbkuerzung(abkuerzung);

			s.setId(1);

			return this.stundenplaneintragMapper.insert(s);
		}
	}

	/**
	 * create-Methoden zum Anlegen eines Semesterverbands 
	 * Anschliessende Prüfung auf gültige Eingabe 
	 * (Entweder: SS 2014 Oder: WS 2014/2015)
	 * 
	 * @param Semester, Jahrgang und StudierendenAnzahl 
	 * und der Fremdschlüssel des Studienganges
	 * @return Das jeweilige Semesterverband-Objekt
	 * @throws IllegalArgumentException
	 */

	public Semesterverband createSemesterverband(int studiengangId,
			int semester, int studierendenAnzahl, String jahrgang)
			throws IllegalArgumentException {

		if (jahrgang.matches("[A-Z]+" + " " + "[0-9]+")
				|| jahrgang.matches("[A-Z]+" + " " + "[0-9]+" + "/"
						+ "[0-9]+")) {

			Semesterverband a = new Semesterverband();
			a.setStudiengangId(studiengangId);
			a.setSemester(semester);
			a.setStudierendenAnzahl(studierendenAnzahl);
			a.setJahrgang(jahrgang);

			String bezeichnung = this.getStudiengangById(
					studiengangId).getBezeichnung();
			String kuerzel = bezeichnung.substring(0, 2);
			a.setKuerzel(kuerzel);

			a.setId(1);

			return this.semesterverbandMapper.insert(a);

		} else {

			throw new IllegalArgumentException(
					"ung�ltige Eingabe des Jahrgangs!");
		}

	}

	/**
	 * create-Methoden zum Anlegen eines Stundenplans 
	 * Anschliessende Prüfung auf gültige Eingabe 
	 * (Entweder: SS 2014 Oder: WS 2014/2015)
	 * 
	 * @param Studienhalbjahr
	 *            und der Fremdschl�ssel des Semesterverbands
	 * @return Das jeweilige Stundenplan-Objekt
	 * @throws IllegalArgumentException
	 */

	public Stundenplan createStundenplan(String studienhalbjahr,
			int semesterverbandId) throws IllegalArgumentException {

		if (studienhalbjahr.matches("[A-Z]+" + " " + "[0-9]+")
				|| studienhalbjahr.matches("[A-Z]+" + " " + "[0-9]+"
						+ "/" + "[0-9]+")) {

			Stundenplan sp = new Stundenplan();

			sp.setStudienhalbjahr(studienhalbjahr);
			sp.setSemesterverbandId(semesterverbandId);

			sp.setId(1);

			return this.stundenplanMapper.insert(sp);

		} else {

			throw new IllegalArgumentException(
					"ung�ltige Eingabe im Studienhalbjahr!");
		}
	}

	/*
	 * **************************************************************
	 * ABSCHNITT, Beginn: Delete-Methoden
	 * ****************************************
	 * **************************************************************
	 */

	/**
	 * delete-Methoden zum L�schen eines BusniessObjekts 
	 * Anschliessende Pr�fung, ob f�r den jeweiligen 
	 * BO's noch Stundenplaneintr�ge vorhanden sind
	 * 
	 * @param das jeweilige BusniessObjekt-Objekt
	 * @return void
	 * @throws IllegalArgumentException
	 */

	public void deleteDozent(Dozent d)
			throws IllegalArgumentException {

		Vector<Stundenplaneintrag> dozenten = this
				.getAllStundenplaneintraegeByDozent(d.getId());

		if (dozenten.isEmpty()) {
			
			this.dozentMapper.delete(d);	
		} 
		else {
			
			throw new IllegalArgumentException(
					"Der von Ihnen ausgewählte Dozent kann nicht "
					+ "gelöscht werden, da er noch in einem "
					+ "Stundenplaneintrag verwendet wird!");
		}
	}

	public void deleteLehrveranstaltung(Lehrveranstaltung a)
			throws IllegalArgumentException {

		Vector<Stundenplaneintrag> lvs = this
				.getAllStundenplaneintraegeByLehrveranstaltung(a
						.getId());

		if (lvs.isEmpty()) {
			
			this.lehrveranstaltungMapper.delete(a);
			
		} else {
			
			throw new IllegalArgumentException(
					"Die von Ihnen ausgewählte Lehrveranstaltung "
					+ "kann nicht gelöscht werden, da er noch in "
					+ "einem Stundenplaneintrag verwendet wird!");
		}
	}

	public void deleteStundenplan(Stundenplan sp)
			throws IllegalArgumentException {
		Vector<Stundenplaneintrag> sps = this
				.getAllStundenplaneintraegeByStundenplan(sp.getId());

		if (sps.isEmpty()) {
			
			this.stundenplanMapper.delete(sp);
		} 
		else {
			
			throw new IllegalArgumentException(
					"Der von Ihnen ausgewählte Studienhalbjahr "
					+ "kann nicht gelöscht werden, da er noch in "
					+ "einem Stundenplaneintrag verwendet wird!");
		}
	}

	public void deleteRaum(Raum a) throws IllegalArgumentException {
		Vector<Stundenplaneintrag> r = this
				.getAllStundenplaneintraegeByRaum(a.getId());

		if (r.isEmpty()) {
			
			this.raumMapper.delete(a);
		} 
		else {
			
			throw new IllegalArgumentException(
					"Der von Ihnen ausgewählte Raum "
					+ "kann nicht gelöscht werden, da er noch in "
					+ "einem Stundenplaneintrag verwendet wird!");
		}
	}

	public void deleteStudiengang(Studiengang studiengang)
			throws IllegalArgumentException {

		this.studiengangMapper.delete(studiengang);
	}

	public void deleteStundenplaneintrag(Stundenplaneintrag s)
			throws IllegalArgumentException {

		this.stundenplaneintragMapper.delete(s);
	}

	public void deleteSemesterverband(Semesterverband a)
			throws IllegalArgumentException {

		this.semesterverbandMapper.delete(a);
	}

	/*
	 * **************************************************************
	 * ABSCHNITT, Beginn: Change-Methoden
	 * ****************************************
	 * **************************************************************
	 */

	/**
	 * change-Methoden zum ändern eines BusniessObjekts 
	 * Anschliessende Prüfung auf gültige Eingabe
	 * 
	 * @param das
	 *            jeweilige BusniessObjekt-Objekt
	 * @return void
	 * @throws IllegalArgumentException
	 */
	public void changeDozent(Dozent d)
			throws IllegalArgumentException {

		if (d.getVorname().matches("[0-9]+")
				|| d.getNachname().matches("[0-9]+")) {

			throw new IllegalArgumentException(
					"ung�ltige Eingabe! Bitte verwenden Sie keine"
					+ " Ziffern f�r den Vor- und Nachnamen.");
		} else {

			this.dozentMapper.update(d);
		}
	}

	public void changeSemsterverband(Semesterverband sv)
			throws IllegalArgumentException {

		if (sv.getJahrgang().matches("[A-Z]+" + " " + "[0-9]+")
				|| sv.getJahrgang().matches(
						"[A-Z]+" + " " + "[0-9]+" + "/" + "[0-9]+"))
		{

			this.semesterverbandMapper.update(sv);

		} else {

			throw new IllegalArgumentException(
					"ung�ltige Eingabe des Jahrgangs!");
		}

	}

	public void changeStudiengang(Studiengang s)
			throws IllegalArgumentException {

		if (s.getBezeichnung().matches("[0-9]+")) {

			throw new IllegalArgumentException(
					"ung�ltige Eingabe! Bitte verwenden Sie keine "
					+ "Ziffern f�r die Bezeichnung.");
		} else {

			this.studiengangMapper.update(s);
		}
	}

	public void changeStundenplan(Stundenplan sp)
			throws IllegalArgumentException {

		if (sp.getStudienhalbjahr()
				.matches("[A-Z]+" + " " + "[0-9]+")
				|| sp.getStudienhalbjahr().matches(
						"[A-Z]+" + " " + "[0-9]+" + "/" + "[0-9]+")) {

			this.stundenplanMapper.update(sp);

		} else {

			throw new IllegalArgumentException(
					"ung�ltige Eingabe des Studienhalbjahrs!");
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
