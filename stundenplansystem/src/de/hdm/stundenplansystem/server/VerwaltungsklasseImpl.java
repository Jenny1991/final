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
 * 
 * @author L.Hofmann & Holz
 *
 */

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
	private Zeitslot zeitslot = null;
	
	
	
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
	
	public Zeitslot getZeitslot(){
		return zeitslot;
	}

	public void setZeitslot(Zeitslot zeitslot) throws IllegalArgumentException {
		this.zeitslot = zeitslot;
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
	 * Auslesen aller Dozenten
	 */
	public Vector<Dozent> getAllDozenten() throws IllegalArgumentException {
	    return this.dozentMapper.findAll();
	  }
	
	
	/**
	 * Auslesen aller R�ume
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
	 * Auslesen aller Semesterverb�nde
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
	 * Auslesen aller Stundenpl�ne
	 */
	
	public Vector<Stundenplan> getAllStundenplaene() throws IllegalArgumentException {
	    return this.stundenplanMapper.findAll();
	  }
	
	/**
	 * Auslesen aller Stundenplaneintr�ge
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
	
	
	/**
	 * Auslesen der jeweiligen BO's �ber ihre ID
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
	
	/**
	 * Auslesen der Semesterverb�nde eines Studiengangs
	 */
	
	public Vector<Semesterverband> getSemsterverbaendeByStudiengang(int studiengangId){
		return this.semesterverbandMapper.findByStudiengangId(studiengangId);
	}
	
	public Vector<Stundenplan> getStundenplaeneBySemesterverband (int semesterverbandId){
		return this.stundenplanMapper.findBySemesterverband(semesterverbandId);
	}
	
	/**
	 * Auslesen eines Stundenplaneintrages eines Dozenten zu einem bestimmten Zeitslot
	 */
	
	public Stundenplaneintrag getStundenplaneintragByDozentAndZeitslot(int dozentId, int zeitslotId){
		return this.stundenplaneintragMapper.findByDozentAndZeitslot(dozentId, zeitslotId);
	}
	
	/**
	 * Auslesen eines Stundenplaneintrages eines Raumes zu einem bestimmten Zeitslot
	 */
	
	public Stundenplaneintrag getStundenplaneintragByRaumAndZeitslot(int raumId, int zeitslotId){
		return this.stundenplaneintragMapper.findByRaumAndZeitslot(raumId, zeitslotId);	
	}
	
	/**
	 * Auslesen eines Stundenplaneintrages eines Semesterverbandes zu einem bestimmten Zeitslot
	 */
	
	public Stundenplaneintrag getStundenplaneintragBySemesterverbandAndZeitslot(int semesterverbandId, int zeitslotId, int stundenplanId){
	return this.stundenplaneintragMapper.findbySemesterverbandZeitslotAndStundenplan(semesterverbandId, zeitslotId, stundenplanId);
	}
	
	/**
	 * Auslesen eines Studiengangs �ber die SemesterverbandId
	 */
	
	public Studiengang getStudiengangBySemesterverbandId(int SemesterverbandId){
		return this.studiengangMapper.findBySemesterverbandId(SemesterverbandId);
	}
	
	/**
	 * Anschlie�end folgen alle create-Methoden der BO's
	 */
	
	public Dozent createDozent(String vorname, String nachname)
			throws IllegalArgumentException {
		
	if (vorname.matches("[A-Z]+" + "[a-z]+") || nachname.matches("[A-Z]+" + "[a-z]+")){
		
		Dozent a = new Dozent();
		a.setVorname(vorname);
		a.setNachname(nachname);
	
		a.setId(1);
		return this.dozentMapper.insert(a);	
		}
	else {		
		throw new IllegalArgumentException("ung�ltige Eingabe!");	
		}
	}
	
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
	
	public Raum createRaum(String bezeichnung, int kapazitaet)
			throws IllegalArgumentException {
		Raum a = new Raum();
		a.setBezeichnung(bezeichnung);
		a.setKapazitaet(kapazitaet);
		
		a.setId(1);

		return this.raumMapper.insert(a);
	}

	public Studiengang createStudiengang(String bezeichnung)
			throws IllegalArgumentException {
		
		if (bezeichnung.matches("[A-Z]+" + "[a-z]+")){
			Studiengang s = new Studiengang();
			
			s.setBezeichnung(bezeichnung);
			
			s.setId(1);
			
			return this.studiengangMapper.insert(s);
		}
		else {		
			throw new IllegalArgumentException("ung�ltige Eingabe!");	
		}
	}

	public Stundenplaneintrag createStundenplaneintrag(int d,
		int l, int r, int z, int sv, int sp) 
					throws IllegalArgumentException {
		
		Stundenplaneintrag s = new Stundenplaneintrag();
		
		s.setDozentId(d);
		s.setLehrveranstaltungId(l);
		s.setRaumId(r);
		s.setZeitslotId(z);
		s.setSemesterverbandId(sv);
		s.setStundenplanId(sp);
		
		s.setId(1);
		
		return this.stundenplaneintragMapper.insert(s);
	}

	public Semesterverband createSemesterverband(int studiengangId,
			int semester, int studierendenAnzahl, String jahrgang)
			throws IllegalArgumentException {
		
		Semesterverband a = new Semesterverband();
		a.setStudiengangId(studiengangId);
		a.setSemester(semester);
		a.setStudierendenAnzahl(studierendenAnzahl);
		a.setJahrgang(jahrgang);
		
		String bezeichnung = this.getStudiengangById(studiengangId).getBezeichnung();
		String kuerzel = bezeichnung.substring(0, 1);
		a.setKuerzel(kuerzel);
		
		
		
		
		a.setId(1);
		
		return this.semesterverbandMapper.insert(a);
	}

	public Stundenplan createStundenplan(String studienhalbjahr, int semesterverbandId) throws IllegalArgumentException {
		
		Stundenplan sp = new Stundenplan();
		
		sp.setStudienhalbjahr(studienhalbjahr);
		sp.setSemesterverbandId(semesterverbandId);
		
		sp.setId(1);
		
		return this.stundenplanMapper.insert(sp);
	}
	
	/**
	 * Anschlie�end folgen alle delete-Methoden der BO's
	 */

	public boolean deleteDozent(Dozent d) throws IllegalArgumentException {
		
//		Vector<Stundenplaneintrag> dozenten = this.getAllStundenplaneintragOf(d);
//
//		    if (dozenten != null) {
//		    	return false;
//		    } else {
		   	this.dozentMapper.delete(d);
		   	return true;
//		    }
	}
	
	public boolean deleteLehrveranstaltung(Lehrveranstaltung a)
			throws IllegalArgumentException {
		
//		Vector<Stundenplaneintrag> lvs = this.getAllStundenplaneintragOf(a);
//
//		    if (lvs != null) {
//		    	return false;
//		    } else {
		   	this.lehrveranstaltungMapper.delete(a);
		   	return true;
//		    }
	}

	public boolean deleteStundenplan(Stundenplan sp) throws IllegalArgumentException {
//		Vector<Stundenplaneintrag> sps = this.getAllStundenplaneintragOf(sp);
//
//		    if (sps != null) {
//		    	return false;
//		    } else {
		   	this.stundenplanMapper.delete(sp);
		   	return true;
//		    }	
		   	}

	public boolean deleteStudiengang(Studiengang studiengang)
			throws IllegalArgumentException {
//		Vector<Stundenplaneintrag> sgs = this.getAllStundenplaneintragOf(studiengang);
//
//		    if (sgs != null) {
//		    	return false;
//		    } else {
		   	this.studiengangMapper.delete(studiengang);
		   	return true;
//		    }
	}

	public boolean deleteStundenplaneintrag(Stundenplaneintrag s)
			throws IllegalArgumentException {
//		Vector<Stundenplaneintrag> lvs = this.getAllStundenplaneintragOf(a);
//
//		    if (lvs != null) {
//		    	return false;
//		    } else {
		   	this.stundenplaneintragMapper.delete(s);
		   	return true;
//		    }	
		   	}

	public boolean deleteRaum(Raum a) throws IllegalArgumentException {
//		Vector<Stundenplaneintrag> r = this.getAllStundenplaneintragOf(a);
//
//		    if (r != null) {
//		    	return false;
//		    } else {
		   	this.raumMapper.delete(a);
		   	return true;
//		    }
	}

	public boolean deleteSemesterverband(Semesterverband a)
			throws IllegalArgumentException {
//		Vector<Stundenplaneintrag> svs = this.getAllStundenplaneintragOf(a);
//
//		    if (svs != null) {
//		    	return false;
//		    } else {
		   	this.semesterverbandMapper.delete(a);
		   	return true;
//		    }
	}
	
	/**
	 * Anschlie�end folgen alle Change-Methoden der BO's
	 */

	public void changeDozent(Dozent d) throws IllegalArgumentException {
		 this.dozentMapper.update(d);
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

	public void changeSemsterverband(Semesterverband sv)
			throws IllegalArgumentException {
		this.semesterverbandMapper.update(sv);
	}

	public void changeStudiengang(Studiengang s)
			throws IllegalArgumentException {
		this.studiengangMapper.update(s);
	}

	public void changeStundenplan(Stundenplan sp) throws IllegalArgumentException {
		this.stundenplanMapper.update(sp);
	}

}
