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

@SuppressWarnings("serial")
public class VerwaltungsklasseImpl extends RemoteServiceServlet implements Verwaltungsklasse {

	/**
	 * Standard StundenplaneintragID
	 */
	private static final long serialVersionUID = 7027992284251455305L;

	private RaumMapper raumMapper = null;
	
	private DozentMapper dozentMapper = null;
	
	private LehrveranstaltungMapper lehrveranstaltungMapper = null;
	
	private SemesterverbandMapper semesterverbandMapper = null;
	
	private StundenplaneintragMapper stundenplaneintragMapper = null;
	
	private StundenplanMapper stundenplanMapper = null;
	
	private ZeitslotMapper zeitslotMapper = null;
	
	private StudiengangMapper studiengangMapper = null;
	
	private Dozent dozent = null;
	private Raum raum = null;
	private Semesterverband semesterverband = null;
	private Studiengang studiengang = null;
	private Lehrveranstaltung lehrveranstaltung = null;
	private Stundenplaneintrag stundenplaneintrag = null;
	private Stundenplan stundenplan = null;
	
	
	public Stundenplaneintrag getStundenplaneintrag() {
		return stundenplaneintrag;
	}



	public void setStundenplaneintrag(Stundenplaneintrag stundenplaneintrag) {
		this.stundenplaneintrag = stundenplaneintrag;
	}



	public Stundenplan getStundenplan() {
		return stundenplan;
	}



	public void setStundenplan(Stundenplan stundenplan) {
		this.stundenplan = stundenplan;
	}



	public void setLehrveranstaltung(Lehrveranstaltung lehrveranstaltung) {
		this.lehrveranstaltung = lehrveranstaltung;
	}



	public Lehrveranstaltung getLehrveranstaltung() {
		return lehrveranstaltung;
	}
	

	
	public Dozent getDozent() {
		return dozent;
	}

	public void setDozent(Dozent dozent) throws IllegalArgumentException {
		this.dozent = dozent;
	}

	public Raum getRaum() throws IllegalArgumentException {
		return raum;
	}

	public void setRaum(Raum raum) throws IllegalArgumentException {
		this.raum = raum;
	}

	public Semesterverband getSemesterverband() throws IllegalArgumentException {
		return semesterverband;
	}

	public void setSemesterverband(Semesterverband semesterverband) throws IllegalArgumentException {
		this.semesterverband = semesterverband;
	}

	public Studiengang getStudiengang() throws IllegalArgumentException {
		return studiengang;
	}
	
	public void setStudiengang(Studiengang studiengang) throws IllegalArgumentException {
		this.studiengang = studiengang;
	}

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
	
	/**
	 * Hier werden alle Stundenplaneintraege des Dozenten d in einen Vector gepackt 
	 */
	
	public Vector<Stundenplaneintrag> getAllStundenplaneintragOf(Dozent d)
		      throws IllegalArgumentException {
		
		Vector<Stundenplaneintrag> dVektor = null;
		
		/**
		 * Hier mï¿½ssen wir alle Stundenplaneintraege des Dozenten in den Vector reinspeichern.
		 */
			
		 	dVektor = this.stundenplaneintragMapper.findByDozentOrderByAnfangszeit(d.getId());
		
		return dVektor;
	}
	
	public Vector<Stundenplaneintrag> getAllStundenplaneintrag(Raum r)
			throws IllegalArgumentException {
		
		Vector<Stundenplaneintrag> rVektor = null;
		
	 	rVektor = this.stundenplaneintragMapper.findByRaumOrderByAnfangszeit(r.getId());
	
		return rVektor;
	}
	
	
	
	/**
	 * Auslesen aller Dozenten
	 */
	public Vector<Dozent> getAllDozenten() throws IllegalArgumentException {
	    return this.dozentMapper.findAll();
	  }
	
	
	/**
	 * Auslesen aller Rï¿½ume
	 */
	
	public Vector<Raum> getAllRaeume() throws IllegalArgumentException {
	    return this.raumMapper.findAll();
	  }
	
	
	/**
	 * Auslesen aller Lehrveranstaltungen
	 */
	public Vector<Lehrveranstaltung> getAllLehrveranstaltungen() throws IllegalArgumentException {
	    return this.lehrveranstaltungMapper.findAll();
	  }
	
	
	
	/**
	 * Auslesen aller Semesterverbï¿½nde
	 */
	
	public Vector<Semesterverband> getAllSemesterverbaende() throws IllegalArgumentException {
	    return this.semesterverbandMapper.findAll();
	  }
	
	/**
	 * Auslesen aller Zeitslots
	 */
	
	public Vector<Zeitslot> getAllZeitslots() throws IllegalArgumentException {
	    return this.zeitslotMapper.findAll();
	  }
	
	/**
	 * Auslesen aller Stundenplï¿½ne
	 */
	
	public Vector<Stundenplan> getAllStundenplaene() throws IllegalArgumentException {
	    return this.stundenplanMapper.findAll();
	  }
	
	/**
	 * Auslesen aller Stundenplaneintrï¿½ge
	 */
	
	public Vector<Stundenplaneintrag> getAllStundenplaneintraege() throws IllegalArgumentException {
	    return this.stundenplaneintragMapper.findAll();
	  }
	
	/**
	 * Auslesen aller Studiengaenge
	 */
	
	public Vector<Studiengang> getAllStudiengaenge() throws IllegalArgumentException {
	    return this.studiengangMapper.findAll();
	  }
	
	public Dozent createDozent(String vorname, String nachname)
			throws IllegalArgumentException {
		Dozent a = new Dozent();
		a.setVorname(vorname);
		a.setNachname(nachname);
		
		a.setId(1);
		
		dozentMapper.insert(a);
		return null;
	}
	
	public Lehrveranstaltung createLehrveranstaltung(
			String bezeichnung, int semester, int umfang)
			throws IllegalArgumentException {
		Lehrveranstaltung a = new Lehrveranstaltung();
		a.setBezeichnung(bezeichnung);
		a.setSemester(semester);
		a.setUmfang(umfang);
		
		a.setId(1);
		
		lehrveranstaltungMapper.insert(a);
		return null;
	}		
	
	public Raum createRaum(String bezeichnung, int kapazitaet)
			throws IllegalArgumentException {
		Raum a = new Raum();
		a.setBezeichnung(bezeichnung);
		a.setKapazitaet(kapazitaet);
		
		a.setId(1);
		
		raumMapper.insert(a);
		return null;
	}

	public Studiengang createStudiengang(String bezeichnung)
			throws IllegalArgumentException {
		
		Studiengang s = new Studiengang();
		
		s.setBezeichnung(bezeichnung);
		
		s.setId(1);
		
		studiengangMapper.insert(s);
		return null;
	}

	public Stundenplaneintrag createStundenplaneintrag(Dozent d,
			Lehrveranstaltung l, Raum r, Zeitslot z, Semesterverband sv) 
					throws IllegalArgumentException {
		
		Stundenplaneintrag s = new Stundenplaneintrag();
		
		s.setDozent(d);
		s.setLehrveranstaltung(l);
		s.setRaum(r);
		s.setZeitslot(z);
		s.setSemesterverband(sv);
		
		s.setId(1);
		
		stundenplaneintragMapper.insert(s);
		return null;
	}

	@Override
	public Semesterverband createSemesterverband(Studiengang bezeichnung,
			int semester, int studierendenAnzahl, String jahrgang)
			throws IllegalArgumentException {
		Semesterverband a = new Semesterverband();
		a.setBezeichnung(bezeichnung);
		a.setSemester(semester);
		a.setStudierendenAnzahl(studierendenAnzahl);
		a.setJahrgang(jahrgang);
		
		a.setId(1);
		
		semesterverbandMapper.insert(a);
		return null;
	}

	public Zeitslot createZeitslot(String wochentag, double anfangszeit,
			double endzeit) throws IllegalArgumentException {
		
		Zeitslot z = new Zeitslot();
		
		z.setWochentag(wochentag);
		z.setAnfangszeit(anfangszeit);
		z.setEndzeit(endzeit);
		
		z.setId(1);
		
		zeitslotMapper.insert(z);
		return null;
	}

	public void deleteDozent(Dozent d) throws IllegalArgumentException {
		
		Vector<Stundenplaneintrag> dozenten = this.getAllStundenplaneintragOf(d);

		    if (dozenten != null) {
		    	//Bildschirmmeldung: Dozent kann nicht geloescht werden, da noch Stundenplaneintrï¿½ï¿½ge
		    } else {
		   	this.dozentMapper.delete(d);
		    }
	}
	
	public void deleteLehrveranstaltung(Lehrveranstaltung a)
			throws IllegalArgumentException {
		//Objekt abspeichern in die Datenbank, hier muss man den jeweiligen Mapper returnen
	}

	public void deleteZeitslot(Zeitslot z) throws IllegalArgumentException {
		//Objekt abspeichern in die Datenbank, hier muss man den jeweiligen Mapper returnen
	}

	public void deleteStudiengang(Studiengang studiengang)
			throws IllegalArgumentException {
		//Objekt abspeichern in die Datenbank, hier muss man den jeweiligen Mapper returnen
	}

	public void deleteStundenplaneintrag(Stundenplaneintrag s)
			throws IllegalArgumentException {
		//Objekt abspeichern in die Datenbank, hier muss man den jeweiligen Mapper returnen
	}

	public void deleteRaum(Raum a) throws IllegalArgumentException {
		//Objekt abspeichern in die Datenbank, hier muss man den jeweiligen Mapper returnen
	}

	public void deleteSemesterverband(Semesterverband a)
			throws IllegalArgumentException {
		//Objekt abspeichern in die Datenbank, hier muss man den jeweiligen Mapper returnen
	}

	public Dozent changeDozent(Dozent d) throws IllegalArgumentException {
		 this.dozentMapper.update(d);
		return null;
	}

	public Stundenplaneintrag changeStundenplaneintrag(Stundenplaneintrag s)
			throws IllegalArgumentException {
		this.stundenplaneintragMapper.update(s);
		return null;
	}

	public Lehrveranstaltung changeLehrveranstaltung(Lehrveranstaltung l)
			throws IllegalArgumentException {
		this.lehrveranstaltungMapper.update(l);
		return null;
	}

	public Raum changeRaum(Raum r) throws IllegalArgumentException {
		this.raumMapper.update(r);
		return null;
	}

	public Semesterverband changeSemsterverband(Semesterverband sv)
			throws IllegalArgumentException {
		this.semesterverbandMapper.update(sv);
		return null;
	}

	public Studiengang changeStudiengang(Studiengang s)
			throws IllegalArgumentException {
		this.studiengangMapper.update(s);
		return null;
	}

	public Zeitslot changeZeitslot(Zeitslot z) throws IllegalArgumentException {
		this.zeitslotMapper.update(z);
		return null;
	}

}
