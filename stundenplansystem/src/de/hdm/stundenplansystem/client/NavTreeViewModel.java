package de.hdm.stundenplansystem.client;

import java.util.List;
import java.util.Vector;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;

import de.hdm.stundenplansystem.shared.Verwaltungsklasse;
import de.hdm.stundenplansystem.shared.VerwaltungsklasseAsync;
import de.hdm.stundenplansystem.shared.bo.Dozent;
import de.hdm.stundenplansystem.shared.bo.Lehrveranstaltung;
import de.hdm.stundenplansystem.shared.bo.Raum;
import de.hdm.stundenplansystem.shared.bo.Semesterverband;
import de.hdm.stundenplansystem.shared.bo.Studiengang;
import de.hdm.stundenplansystem.shared.bo.Stundenplan;
import de.hdm.stundenplansystem.shared.bo.Stundenplaneintrag;

/** 
 * Die NavTreeViewModel Klasse definiert den flexiblen Baum für die Navigation durch unser System.
 * Diese Klasse erbt von der Klasse Content und lässt sich somit 
 * unter GWT entsprechend anordnen.
 * Des Weiteren implementiert sie das Interface {@link TreeViewModel}
 * 
 * @author Thies, V. Hofmann
 * @version 1.0
 */
public class NavTreeViewModel extends Content implements
		TreeViewModel {

	/**
	 * Hier instantiieren wir alle Klassen, auf die der Baum zugreift.
	 */
	private DozentForm df;
	private LehrveranstaltungForm lf;
	private RaumForm rf;
	private SemesterverbandForm svf;
	private StudiengangForm sgf;
	private StundenplaneintragForm spef;
	private StundenplanForm spf;
	private ReportRaum rr;
	private ReportStundenplan rs;
	private ReportStundenplanDozent rsd;

	private CreateDozent cd;
	private CreateLehrveranstaltung cl;
	private CreateRaum cr;
	private CreateSemesterverband csv;
	private CreateStudiengang csg;
	private CreateStundenplaneintrag cspe;
	private CreateStundenplan csp;

	/**
	 * Referenz auf das ausgewählte BusinessObjekt-Objekt.
	 */
	private Dozent selectedDozent = null;
	private Lehrveranstaltung selectedLv = null;
	private Raum selectedRaum = null;
	private Semesterverband selectedSv = null;
	private Studiengang selectedSg = null;
	private Stundenplaneintrag selectedSpe = null;
	private Stundenplan selectedSp = null;


	/**
	 * Hier wird ein Remote Service Proxy erstellt, welches uns erlaubt 
	 * mit dem serverseitigen Verwaltungsservice zu kommunizieren.
	 */
	private VerwaltungsklasseAsync verwaltungsSvc = GWT
			.create(Verwaltungsklasse.class);
	
	/**
	 * @param <Dozent>, 
	 * @param <Lehrveranstaltung>, 
	 * @param <Raum>,
	 * @param <Semesterverband>,
	 * @param <Studiengang>,
	 * @param <Stundenplaneintrag>, 
	 * @param <Stundenplan>,
	 * @param <String>
	 * definieren den Datentyp der Liste.
	 */
	private ListDataProvider<Dozent> dozentDataProvider;
	private ListDataProvider<Lehrveranstaltung> lvDataProvider;
	private ListDataProvider<Raum> raumDataProvider;
	private ListDataProvider<Semesterverband> svDataProvider;
	private ListDataProvider<Studiengang> sgDataProvider;
	private ListDataProvider<Stundenplaneintrag> speDataProvider;
	private ListDataProvider<Stundenplan> spDataProvider;
	private ListDataProvider<String> stringDataProvider;

	/**
	 * Hier wird die EntryPoint Klasse instantiiert. 
	 */
	private Stundenplansystem sps;

	/**
	 * Hier wird das Selektionsverhalten definiert.
	 * Die Anwendung des {@link ProvidesKey} stellt den Key für die ListItems
	 * bereit, so dass Items, welche als eindeutige Items behandelt werden, eindeutige
	 * Key besitzen.
	 */
	private ProvidesKey<Object> boKeyProvider = new ProvidesKey<Object>() {
		
		/**
		 * Die Methode <code>getKey()</code> erhält den Key von einem ListItem.
		 * 
		 * @param object als ListItem
		 * @return den Key, welches das Item repräsentiert.
		 */
		public Integer getKey(Object object) {

			if (object == null) {
				return null;
			}

			else if (object instanceof String) {
				return new Integer(((String) object).hashCode());
			}

			else if (object instanceof Dozent) {
				return new Integer(((Dozent) object).getId());
			}

			else if (object instanceof Lehrveranstaltung) {
				return new Integer(
						((Lehrveranstaltung) object).getId());
			}

			else if (object instanceof Raum) {
				return new Integer(((Raum) object).getId());
			}

			else if (object instanceof Semesterverband) {
				return new Integer(((Semesterverband) object).getId());
			}

			else if (object instanceof Studiengang) {
				return new Integer(((Studiengang) object).getId());
			}

			else if (object instanceof Stundenplaneintrag) {
				return new Integer(
						((Stundenplaneintrag) object).getId());
			}

			else if (object instanceof Stundenplan) {
				return new Integer(((Stundenplan) object).getId());
			}

			else
				return null;
		}

	};

	/**
	 * Instantiiert das SingleSelectionModel.
	 */
	private SingleSelectionModel<Object> selectionModel = new SingleSelectionModel<Object>(
			boKeyProvider);

	/**
	 * Aufbau der Baumdarstellung
	 * @param cd Klasse {@link CreateDozent} wird übergeben und dem Baum 
	 * hinzugefügt
	 * @param cl Klasse {@link CreateLehrveranstaltung} wird übergeben
	 * und dem Baum hinzugefügt
	 * @param cr Klasse {@link CreateRaum} wird übergeben und dem Baum 
	 * hinzugefügt
	 * @param csg Klasse {@link CreateStudiengang} wird übergeben und dem 
	 * Baum hinzugefügt
	 * @param csv Klasse {@link CreateSemesterverband} wird übergeben und 
	 * dem Baum hinzugefügt
	 * @param cspe Klasse {@link CreateStundenplaneintrag} wird übergeben 
	 * und dem Baum hinzugefügt
	 * @param csp Klasse {@link CreateStundenplan} wird übergeben und dem 
	 * Baum hinzugefügt
	 * @param df Klasse {@link DozentForm} wird übergeben und dem Baum 
	 * hinzugefügt
	 * @param lf Klasse {@link LehrveranstaltungForm} wird übergeben und 
	 * dem Baum hinzugefügt
	 * @param rf Klasse {@link RaumForm} wird übergeben und dem Baum 
	 * hinzugefügt
	 * @param sgf Klasse {@link StudiengangForm} wird übergeben und dem 
	 * Baum hinzugefügt
	 * @param svf Klasse {@link SemesterverbandForm} wird übergeben und 
	 * dem Baum hinzugefügt
	 * @param spef Klasse {@link StundenplaneintragForm} wird übergeben 
	 * und dem Baum hinzugefügt
	 * @param spf Klasse {@link StundenplanForm} wird übergeben und dem 
	 * Baum hinzugefügt
	 * @param rr Klasse {@link ReportRaum} wird übergeben und dem Baum 
	 * hinzugefügt
	 * @param rs Klasse {@link ReportStundenplan} wird übergeben und dem 
	 * Baum hinzugefügt
	 * @param rsd Klasse {@link ReportStundenplanDozent} wird übergeben 
	 * und dem Baum hinzugefügt
	 * @param sps Klasse {@link Stundenplansystem} wird übergeben
	 */
	public NavTreeViewModel(CreateDozent cd,
			CreateLehrveranstaltung cl, CreateRaum cr,
			CreateStudiengang csg, CreateSemesterverband csv,
			CreateStundenplaneintrag cspe, CreateStundenplan csp,
			DozentForm df, LehrveranstaltungForm lf, RaumForm rf,
			StudiengangForm sgf, SemesterverbandForm svf,
			StundenplaneintragForm spef, StundenplanForm spf,
			ReportRaum rr, ReportStundenplan rs,
			ReportStundenplanDozent rsd, Stundenplansystem sps) {

		/**
		 * Zuweisung zur Baumdarstellung.
		 */
		this.cd = cd;
		cd.setTvm(this);
		this.cl = cl;
		cl.setTvm(this);
		this.cr = cr;
		cr.setTvm(this);
		this.csg = csg;
		csg.setTvm(this);
		this.csv = csv;
		csv.setTvm(this);
		this.cspe = cspe;
		cspe.setTvm(this);
		this.csp = csp;
		csp.setTvm(this);

		this.df = df;
		df.setTvm(this);
		this.lf = lf;
		lf.setTvm(this);
		this.rf = rf;
		rf.setTvm(this);
		this.sgf = sgf;
		sgf.setTvm(this);
		this.svf = svf;
		svf.setTvm(this);
		this.spef = spef;
		spef.setTvm(this);
		this.spf = spf;
		spf.setTvm(this);

		this.rr = rr;
		rr.setTvm(this);
		this.rs = rs;
		rs.setTvm(this);
		this.rsd = rsd;
		rsd.setTvm(this);

		this.sps = sps;
		
		/**
		 * Durch die Methode <code>addSelectionChangeHandler()</code> wird dem 
		 * 
		 * @param selectionModel ein ChangeHandlers zugewiesen. 
		 */
		selectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

					/**
					 * Die Methode <code>onSelectionChange()</code> definiert das
					 * auszuführende Event, beim Klicken des Items.
					 * 
					 * @param selection
					 */
					@Override
					public void onSelectionChange(
							SelectionChangeEvent event) {
						
						/**
						 * Die Methode <code>getSelectedObject()</code> selektiert das
						 * ausgewählte Item und überprüft im nachfolgenden Verlauf
						 * welches Item ausgewählt wurde und welches Event dafür
						 * ausgeführt werden soll.
						 */
						Object selection = selectionModel
								.getSelectedObject();

						if (selection instanceof String
								&& (String) selection == "Dozent anlegen") {
							setCreateDozent();
						}

						if (selection instanceof String
								&& (String) selection == "Dozent verwalten") {
							setSelectedDozent(selectedDozent);
						}

						if (selection instanceof String
								&& (String) selection == "Lehrveranstaltung anlegen") {
							setCreateLehrveranstaltung();
						}

						if (selection instanceof String
								&& (String) selection == "Lehrveranstaltung verwalten") {
							setSelectedLv(selectedLv);
						}

						if (selection instanceof String
								&& (String) selection == "Raum anlegen") {
							setCreateRaum();
						}

						if (selection instanceof String
								&& (String) selection == "Raum verwalten") {
							setSelectedRaum(selectedRaum);
						}

						if (selection instanceof String
								&& (String) selection == "Studiengang anlegen") {
							setCreateStudiengang();
						}

						if (selection instanceof String
								&& (String) selection == "Studiengang verwalten") {
							setSelectedSg(selectedSg);
						}

						if (selection instanceof String
								&& (String) selection == "Semesterverband anlegen") {
							setCreateSemesterverband();
						}

						if (selection instanceof String
								&& (String) selection == "Semesterverband verwalten") {
							setSelectedSv(selectedSv);
						}

						if (selection instanceof String
								&& (String) selection == "Stundenplaneintrag anlegen") {
							setCreateStundenplaneintrag();
						}

						if (selection instanceof String
								&& (String) selection == "Stundenplaneintrag verwalten") {
							setSelectedStundenplaneintrag(selectedSpe);
						}

						if (selection instanceof String
								&& (String) selection == "Stundenplan anlegen") {
							setCreateStudienhalbjahr();
						}

						if (selection instanceof String
								&& (String) selection == "Stundenplan verwalten") {
							setSelectedStudienhalbjahr(selectedSp);
						}

						if (selection instanceof String
								&& (String) selection == "Raumbelegungsplan") {
							setReportRaum();
						}

						if (selection instanceof String
								&& (String) selection == "Stundenplan für Studenten") {
							setReportStundenplan();
						}

						if (selection instanceof String
								&& (String) selection == "Stundenplan für Dozenten") {
							setReportStundenplanDozent();
						}

						if (selection instanceof Dozent) {
							setSelectedDozent((Dozent) selection);
						}

						if (selection instanceof Lehrveranstaltung) {
							setSelectedLv((Lehrveranstaltung) selection);
						}

						if (selection instanceof Raum) {
							setSelectedRaum((Raum) selection);
						}

						if (selection instanceof Studiengang) {
							setSelectedSg((Studiengang) selection);
						}

						if (selection instanceof Semesterverband) {
							setSelectedSv((Semesterverband) selection);
						}

						if (selection instanceof Stundenplaneintrag) {
							setSelectedStundenplaneintrag((Stundenplaneintrag) selection);
						}

						if (selection instanceof Stundenplan) {
							setSelectedStudienhalbjahr((Stundenplan) selection);
						}
					}

				});
	}

	/**
	 * Liest den gewählten Dozenten aus.
	 * 
	 * @return selectedDozent den ausgewählten Dozent
	 */
	Dozent getSelectedDozent() {
		return selectedDozent;
	}

	/**
	 * Ruft die in der EntryPoint Klasse definierte Methode 
	 * <code>createDozentForm()</code> auf 
	 * {@link Stundenplansystem}
	 */
	void setCreateDozent() {
		sps.createDozentForm();
	}

	/**
	 * Übergibt der Klasse DozentForm {@link DozentForm} den ausgewählten 
	 * Dozenten und ruft die in der Klasse {@link Stundenplansystem} definierte
	 * Methode <code>showDozentForm()</code> auf.
	 * 
	 * @param d
	 */
	void setSelectedDozent(Dozent d) {
		selectedDozent = d;
		df.setSelected(d);
		sps.showDozentForm();
	}

	/**
	 * Liest die gewählte Lehrveranstaltung aus.
	 * 
	 * @return selectedLv die ausgewählte Lehrveranstaltung
	 */
	Lehrveranstaltung getSelectedLv() {
		return selectedLv;
	}
	
	/**
	 * Ruft die in der EntryPoint Klasse definierte Methode 
	 * <code>createLvForm()</code> auf 
	 * {@link Stundenplansystem}
	 */
	void setCreateLehrveranstaltung() {
		sps.createLvForm();
	}

	/**
	 * Übergibt der Klasse LehrveranstaltungForm {@link LehrveranstaltungForm} die ausgewählte 
	 * Lehrveranstaltung und ruft die in der Klasse {@link Stundenplansystem} definierte
	 * Methode <code>showLehrveranstaltungForm()</code> auf.
	 * 
	 * @param lv
	 */
	void setSelectedLv(Lehrveranstaltung lv) {
		selectedLv = lv;
		lf.setSelected(lv);
		sps.showLehrveranstaltungForm();
	}

	/**
	 * Liest den gewählten Raum aus.
	 * 
	 * @return selectedRaum den ausgewählten Raum
	 */
	Raum getSelectedRaum() {
		return selectedRaum;
	}

	/**
	 * Ruft die in der EntryPoint Klasse definierte Methode 
	 * <code>createRaumForm()</code> auf 
	 * {@link Stundenplansystem}
	 */
	void setCreateRaum() {
		sps.createRaumForm();
	}

	/**
	 * Übergibt der Klasse RaumForm {@link RaumForm} den ausgewählten 
	 * Raum und ruft die in der Klasse {@link Stundenplansystem} definierte
	 * Methode <code>showRaumForm()</code> auf.
	 * 
	 * @param r
	 */
	void setSelectedRaum(Raum r) {
		selectedRaum = r;
		rf.setSelected(r);
		sps.showRaumForm();
	}

	/**
	 * Liest den gewählten Studiengang aus.
	 * 
	 * @return selectedSg den ausgewählten Studiengang
	 */
	Studiengang getSelectedSg() {
		return selectedSg;
	}

	/**
	 * Ruft die in der EntryPoint Klasse definierte Methode 
	 * <code>createSgForm()</code> auf 
	 * {@link Stundenplansystem}
	 */
	void setCreateStudiengang() {
		sps.createSgForm();
	}

	/**
	 * Übergibt der Klasse StudiengangForm {@link StudiengangForm} 
	 * den ausgewählten Studiengang und ruft die in der Klasse 
	 * {@link Stundenplansystem} definierte Methode 
	 * <code>showStudiengangForm()</code> auf.
	 * 
	 * @param sg
	 */
	void setSelectedSg(Studiengang sg) {
		selectedSg = sg;
		sgf.setSelected(sg);
		sps.showStudiengangForm();
	}

	/**
	 * Liest den gewählten Semesterverband aus.
	 * 
	 * @return selectedSv den ausgewählten Semesterverband
	 */
	Semesterverband getSelectedSv() {
		return selectedSv;
	}

	/**
	 * Ruft die in der EntryPoint Klasse definierte Methode 
	 * <code>createSvForm()</code> auf 
	 * {@link Stundenplansystem}
	 */
	void setCreateSemesterverband() {
		sps.createSvForm();
	}

	/**
	 * Übergibt der Klasse SemesterverbandForm {@link SemesterverbandForm} 
	 * den ausgewählten Semesterverband und ruft die in der Klasse 
	 * {@link Stundenplansystem} definierte Methode 
	 * <code>showSemesterverbandForm()</code> auf.
	 * 
	 * @param sv
	 */
	void setSelectedSv(Semesterverband sv) {
		selectedSv = sv;
		svf.setSelected(sv);
		sps.showSemesterverbandForm();
	}

	/**
	 * Liest den gewählten Stundenplaneintrag aus.
	 * 
	 * @return selectedSpe den ausgewählten Stundenplaneintrag
	 */
	Stundenplaneintrag getSelectedStundenplaneintrag() {
		return selectedSpe;
	}

	/**
	 * Ruft die in der EntryPoint Klasse definierte Methode 
	 * <code>createSpeForm()</code> auf 
	 * {@link Stundenplansystem}
	 */
	void setCreateStundenplaneintrag() {
		sps.createSpeForm();
	}

	/**
	 * Übergibt der Klasse StundenplaneintragForm {@link StundenplaneintragForm} 
	 * den ausgewählten Stundenplaneintrag und ruft die in der Klasse 
	 * {@link Stundenplansystem} definierte Methode 
	 * <code>showSpeForm()</code> auf.
	 * 
	 * @param spe
	 */
	void setSelectedStundenplaneintrag(Stundenplaneintrag spe) {
		selectedSpe = spe;
		spef.setSelected(spe);
		sps.showSpeForm();
	}

	/**
	 * Liest den gewählten Stundenplan aus.
	 * 
	 * @return selectedSp den ausgewählten Stundenplan
	 */
	Stundenplan getSelectedStudienhalbjahr() {
		return selectedSp;
	}

	/**
	 * Ruft die in der EntryPoint Klasse definierte Methode 
	 * <code>createStundenplanForm()</code> auf 
	 * {@link Stundenplansystem}
	 */
	void setCreateStudienhalbjahr() {
		sps.createStundenplanForm();
	}
	
	/**
	 * Übergibt der Klasse StundenplanForm {@link StundenplanForm} 
	 * den ausgewählten Stundenplan und ruft die in der Klasse 
	 * {@link Stundenplansystem} definierte Methode 
	 * <code>showStundenplanForm()</code> auf.
	 * 
	 * @param sp
	 */
	void setSelectedStudienhalbjahr(Stundenplan sp) {
		selectedSp = sp;
		spf.setSelected(sp);
		sps.showStundenplanForm();
	}

	/**
	 * Ruft die in der EntryPoint Klasse definierte Methode 
	 * <code>showReportRaum()</code> auf 
	 * {@link Stundenplansystem}
	 */
	void setReportRaum() {
		sps.showReportRaum();
	}

	/**
	 * Ruft die in der EntryPoint Klasse definierte Methode 
	 * <code>showReportStundenplan()</code> auf 
	 * {@link Stundenplansystem}
	 */
	void setReportStundenplan() {
		sps.showReportStundenplan();
	}

	/**
	 * Ruft die in der EntryPoint Klasse definierte Methode 
	 * <code>showReportStundenplanDozent()</code> auf 
	 * {@link Stundenplansystem}
	 */
	void setReportStundenplanDozent() {
		sps.showReportStundenplanDozent();
	}

	/**
	 * Fügt dem Baum den in der Klasse {@link CreateDozent} erstellten
	 * Dozent hinzu.
	 * 
	 * @param dozent erstellter Dozent wird der Liste hinzugefügt
	 */
	void addDozent(Dozent dozent) {
		dozentDataProvider.getList().add(dozent);
	}

	/**
	 * Fügt dem Baum den in der Klasse {@link CreateRaum} erstellten
	 * Raum hinzu.
	 * 
	 * @param raum erstellter Raum wird der Liste hinzugefügt
	 */
	void addRaum(Raum raum) {
		raumDataProvider.getList().add(raum);
	}

	/**
	 * Fügt dem Baum den in der Klasse {@link CreateStudiengang} erstellten
	 * Studiengang hinzu.
	 * 
	 * @param studiengang erstellter Studiengang wird der Liste hinzugefügt
	 */
	void addStudiengang(Studiengang studiengang) {
		sgDataProvider.getList().add(studiengang);
	}

	/**
	 * Fügt dem Baum den in der Klasse {@link CreateSemesterverband} erstellten
	 * Semesterverband hinzu.
	 * 
	 * @param semesterverband erstellter Semesterverband wird der Liste hinzugefügt
	 */
	void addSemesterverband(Semesterverband semesterverband) {
		svDataProvider.getList().add(semesterverband);
	}

	/**
	 * Fügt dem Baum den in der Klasse {@link CreateLehrveranstaltung} erstellten
	 * Lehrveranstaltung hinzu.
	 * 
	 * @param lehrveranstaltung erstellter Lehrveranstaltung wird der Liste hinzugefügt
	 */
	void addLehrveranstaltung(Lehrveranstaltung lehrveranstaltung) {
		lvDataProvider.getList().add(lehrveranstaltung);
	}

	/**
	 * Fügt dem Baum den in der Klasse {@link CreateStundenplaneintrag} erstellten
	 * Stundenplaneintrag hinzu.
	 * 
	 * @param stundenplaneintrag erstellter Stundenplaneintrag wird der Liste hinzugefügt
	 */
	void addStundenplaneintrag(Stundenplaneintrag stundenplaneintrag) {
		speDataProvider.getList().add(stundenplaneintrag);
	}

	/**
	 * Fügt dem Baum den in der Klasse {@link CreateStundenplan} erstellten
	 * Stundenplan hinzu.
	 * 
	 * @param stundenplan erstellter Stundenplan wird der Liste hinzugefügt
	 */
	void addStundenplan(Stundenplan stundenplan) {
		spDataProvider.getList().add(stundenplan);
	}

	/**
	 * Aktualisiert im Baum den in der Klasse {@link DozentForm} bearbeiteten
	 * Dozenten. Die Methode <code>refresh()</code> sorgt dafür, dass der Baum
	 * sofort aktualisiert wird.
	 * 
	 * @param dozent bearbeiteter Dozent wird in der Liste aktualisiert
	 */
	void updateDozent(Dozent dozent) {
		List<Dozent> dozentList = dozentDataProvider.getList();
		int i = 0;
		for (Dozent d : dozentList) {
			if (d.getId() == i) {
				dozentList.set(i, dozent);
				break;
			} else {
				i++;
			}
		}
		dozentDataProvider.refresh();
	}

	/**
	 * Aktualisiert im Baum den in der Klasse {@link RaumForm} bearbeiteten
	 * Raum. Die Methode <code>refresh()</code> sorgt dafür, dass der Baum
	 * sofort aktualisiert wird.
	 * 
	 * @param raum bearbeiteter Raum wird in der Liste aktualisiert
	 */
	void updateRaum(Raum raum) {
		List<Raum> raumList = raumDataProvider.getList();
		int i = 0;
		for (Raum r : raumList) {
			if (r.getId() == i) {
				raumList.set(i, raum);
				break;
			} else {
				i++;
			}
		}
		raumDataProvider.refresh();
	}

	/**
	 * Aktualisiert im Baum die in der Klasse {@link LehrveranstaltungForm} 
	 * bearbeitete Lehrveranstaltung. 
	 * Die Methode <code>refresh()</code> sorgt dafür, dass der Baum sofort 
	 * aktualisiert wird.
	 * 
	 * @param lehrveranstaltung bearbeitete Lehrveranstaltung wird in der 
	 * Liste aktualisiert
	 */
	void updateLehrveranstaltung(Lehrveranstaltung lehrveranstaltung) {
		List<Lehrveranstaltung> lvList = lvDataProvider.getList();
		int i = 0;
		for (Lehrveranstaltung lv : lvList) {
			if (lv.getId() == i) {
				lvList.set(i, lehrveranstaltung);
				break;
			} else {
				i++;
			}
		}
		lvDataProvider.refresh();
	}

	/**
	 * Aktualisiert im Baum den in der Klasse {@link StudiengangForm} 
	 * bearbeiteten Studiengang. 
	 * Die Methode <code>refresh()</code> sorgt dafür, dass der Baum sofort 
	 * aktualisiert wird.
	 * 
	 * @param studiengang bearbeiteter Studiengang wird in der 
	 * Liste aktualisiert
	 */
	void updateStudiengang(Studiengang studiengang) {
		List<Studiengang> sgList = sgDataProvider.getList();
		int i = 0;
		for (Studiengang sg : sgList) {
			if (sg.getId() == i) {
				sgList.set(i, studiengang);
				break;
			} else {
				i++;
			}
		}
		sgDataProvider.refresh();
	}

	/**
	 * Aktualisiert im Baum den in der Klasse {@link SemesterverbandForm} 
	 * bearbeiteten Semesterverband. 
	 * Die Methode <code>refresh()</code> sorgt dafür, dass der Baum sofort 
	 * aktualisiert wird.
	 * 
	 * @param semesterverband bearbeiteter Semesterverband wird in der 
	 * Liste aktualisiert
	 */
	void updateSemesterverband(Semesterverband semesterverband) {
		List<Semesterverband> svList = svDataProvider.getList();
		int i = 0;
		for (Semesterverband sv : svList) {
			if (sv.getId() == i) {
				svList.set(i, semesterverband);
				break;
			} else {
				i++;
			}
		}
		svDataProvider.refresh();
	}

	/**
	 * Aktualisiert im Baum den in der Klasse {@link StundenplaneintragForm} 
	 * bearbeiteten Stundenplaneintrag. 
	 * Die Methode <code>refresh()</code> sorgt dafür, dass der Baum sofort 
	 * aktualisiert wird.
	 * 
	 * @param stundenplaneintrag bearbeiteter Stundenplaneintrag wird in der 
	 * Liste aktualisiert
	 */
	void updateSpe(Stundenplaneintrag stundenplaneintrag) {
		List<Stundenplaneintrag> speList = speDataProvider.getList();
		int i = 0;
		for (Stundenplaneintrag spe : speList) {
			if (spe.getId() == i) {
				speList.set(i, stundenplaneintrag);
				break;
			} else {
				i++;
			}
		}
		speDataProvider.refresh();
	}

	/**
	 * Aktualisiert im Baum den in der Klasse {@link StundenplanForm} 
	 * bearbeiteten Stundenplan. 
	 * Die Methode <code>refresh()</code> sorgt dafür, dass der Baum sofort 
	 * aktualisiert wird.
	 * 
	 * @param studienhalbjahr bearbeiteter Stundenplan wird in der 
	 * Liste aktualisiert
	 */
	void updateStudienhalbjahr(Stundenplan studienhalbjahr) {
		List<Stundenplan> studienhalbjahrList = spDataProvider
				.getList();
		int i = 0;
		for (Stundenplan sp : studienhalbjahrList) {
			if (sp.getId() == i) {
				studienhalbjahrList.set(i, studienhalbjahr);
				break;
			} else {
				i++;
			}
		}
		spDataProvider.refresh();
	}

	/**
	 * Die Methode <code>remove()</code> sorgt direkt für das Löschen
	 * des ausgewählten Dozenten.
	 * 
	 * @param dozent wird aus dem Baum gelöscht
	 */
	void deleteDozent(Dozent dozent) {
		dozentDataProvider.getList().remove(dozent);
	}

	/**
	 * Die Methode <code>remove()</code> sorgt direkt für das Löschen
	 * des ausgewählten Raums.
	 * 
	 * @param raum wird aus dem Baum gelöscht
	 */
	void deleteRaum(Raum raum) {
		raumDataProvider.getList().remove(raum);
	}

	/**
	 * Die Methode <code>remove()</code> sorgt direkt für das Löschen
	 * der ausgewählten Lehrveranstaltung.
	 * 
	 * @param sehrveranstaltung wird aus dem Baum gelöscht
	 */
	void deleteLehrveranstaltung(Lehrveranstaltung lehrveranstaltung) {
		lvDataProvider.getList().remove(lehrveranstaltung);
	}

	/**
	 * Die Methode <code>remove</code> sorgt direkt für das Löschen
	 * des ausgewählten Studiengangs.
	 * @param studiengang wird aus dem Baum gelöscht
	 */
	void deleteStudiengang(Studiengang studiengang) {
		sgDataProvider.getList().remove(studiengang);
	}

	/**
	 * Die Methode <code>remove()</code> sorgt direkt für das Löschen
	 * des ausgewählten Semesterverbands.
	 * 
	 * @param semesterverband wird aus dem Baum gelöscht
	 */
	void deleteSemesterverband(Semesterverband semesterverband) {
		svDataProvider.getList().remove(semesterverband);
	}

	/**
	 * Die Methode <code>remove()</code> sorgt direkt für das Löschen
	 * des ausgewählten Stundenplaneintrag.
	 * 
	 * @param stundenplaneintrag wird aus dem Baum gelöscht
	 */
	void deleteSpe(Stundenplaneintrag stundenplaneintrag) {
		speDataProvider.getList().remove(stundenplaneintrag);
	}

	/**
	 * Die Methode <code>remove()</code> sorgt direkt für das Löschen
	 * des ausgewählten Stundenplans.
	 * 
	 * @param studienhalbjahr wird aus dem Baum gelöscht
	 */
	void deleteStudienhalbjahr(Stundenplan studienhalbjahr) {
		spDataProvider.getList().remove(studienhalbjahr);
	}

	/**
	 * Die Methode <code>getNodeInfo()</code> definiert die Kinder eines
	 * Baumknotens, wobei 
	 * 
	 * @param value der Wert im Elternknoten
	 */
	@Override
	public <T> NodeInfo<?> getNodeInfo(T value) {

		/**
		 * Hier werden der Wurzel des Baumes die drei obersten Elternknoten 
		 * zugewiesen. 
		 * Die Instantiierung der Klasse {@link StringCell} führt zur Definition
		 * der in den Knoten dargestellten Informationen.
		 * 
		 * @return DefaultNodeInfo<String> Default implementation of NodeInfo
		 */
		if (value instanceof String && (String) value == "Root") {

			stringDataProvider = new ListDataProvider<String>();

			stringDataProvider.getList().add("Stammdaten");
			stringDataProvider.getList().add("Bewegungsdaten");
			stringDataProvider.getList().add("Report");

			return new DefaultNodeInfo<String>(stringDataProvider,
					new StringCell(), selectionModel, null);
		}

		/**
		 * Hier werden dem ersten Elternknoten Stammdaten des Baumes die Kinderknoten  
		 * zugewiesen. 
		 * Die Instantiierung der Klasse {@link StringCell} führt zur Definition
		 * der in den Knoten dargestellten Informationen.
		 * 
		 * @return DefaultNodeInfo<String> Default implementation of NodeInfo
		 */
		if (value instanceof String && (String) value == "Stammdaten") {

			stringDataProvider = new ListDataProvider<String>();

			stringDataProvider.getList().add("Dozent");
			stringDataProvider.getList().add("Lehrveranstaltung");
			stringDataProvider.getList().add("Raum");
			stringDataProvider.getList().add("Studiengang");
			stringDataProvider.getList().add("Semesterverband");
			stringDataProvider.getList().add("Stundenplan");

			return new DefaultNodeInfo<String>(stringDataProvider,
					new StringCell(), selectionModel, null);
		}

		/**
		 * Hier werden dem zweiten Elternknoten Bewegungsdaten des Baumes die 
		 * Kinderknoten zugewiesen. 
		 * Die Instantiierung der Klasse {@link StringCell} führt zur Definition
		 * der in den Knoten dargestellten Informationen.
		 * 
		 * @return DefaultNodeInfo<String> Default implementation of NodeInfo
		 */
		if (value instanceof String
				&& (String) value == "Bewegungsdaten") {

			stringDataProvider = new ListDataProvider<String>();

			stringDataProvider.getList().add("Stundenplaneintrag");

			return new DefaultNodeInfo<String>(stringDataProvider,
					new StringCell(), selectionModel, null);
			}

		/**
		 * Hier werden dem dritten Elternknoten Report des Baumes die 
		 * Kinderknoten zugewiesen. 
		 * Die Instantiierung der Klasse {@link StringCell} führt zur Definition
		 * der in den Knoten dargestellten Informationen.
		 * 
		 * @return DefaultNodeInfo<String> Default implementation of NodeInfo
		 */
		if (value instanceof String && (String) value == "Report") {

			stringDataProvider = new ListDataProvider<String>();

			stringDataProvider.getList().add(
					"Stundenplan für Dozenten");
			stringDataProvider.getList().add(
					"Stundenplan für Studenten");
			stringDataProvider.getList().add("Raumbelegungsplan");

			return new DefaultNodeInfo<String>(stringDataProvider,
					new StringCell(), selectionModel, null);
		}

		/**
		 * Hier werden dem Kinderknoten Dozent des Baumes dessen 
		 * Kinderknoten anlegen und verwalten zugewiesen. 
		 * Die Instantiierung der Klasse {@link StringCell} führt zur Definition
		 * der in den Knoten dargestellten Informationen.
		 * 
		 * @return DefaultNodeInfo<String> Default implementation of NodeInfo
		 */
		if (value instanceof String && (String) value == "Dozent") {

			stringDataProvider = new ListDataProvider<String>();

			stringDataProvider.getList().add("Dozent anlegen");
			stringDataProvider.getList().add("Dozent verwalten");

			return new DefaultNodeInfo<String>(stringDataProvider,
					new StringCell(), selectionModel, null);
		}

		/**
		 * Hier werden dem Kinderknoten Lehrveranstaltung des Baumes dessen 
		 * Kinderknoten anlegen und verwalten zugewiesen. 
		 * Die Instantiierung der Klasse {@link StringCell} führt zur Definition
		 * der in den Knoten dargestellten Informationen.
		 * 
		 * @return DefaultNodeInfo<String> Default implementation of NodeInfo
		 */
		if (value instanceof String
				&& (String) value == "Lehrveranstaltung") {

			stringDataProvider = new ListDataProvider<String>();

			stringDataProvider.getList().add(
					"Lehrveranstaltung anlegen");
			stringDataProvider.getList().add(
					"Lehrveranstaltung verwalten");

			return new DefaultNodeInfo<String>(stringDataProvider,
					new StringCell(), selectionModel, null);
		}

		/**
		 * Hier werden dem Kinderknoten Semesterverband des Baumes dessen 
		 * Kinderknoten anlegen und verwalten zugewiesen. 
		 * Die Instantiierung der Klasse {@link StringCell} führt zur Definition
		 * der in den Knoten dargestellten Informationen.
		 * 
		 * @return DefaultNodeInfo<String> Default implementation of NodeInfo
		 */
		if (value instanceof String
				&& (String) value == "Semesterverband") {

			stringDataProvider = new ListDataProvider<String>();

			stringDataProvider.getList().add(
					"Semesterverband anlegen");
			stringDataProvider.getList().add(
					"Semesterverband verwalten");

			return new DefaultNodeInfo<String>(stringDataProvider,
					new StringCell(), selectionModel, null);
		}

		/**
		 * Hier werden dem Kinderknoten Studiengang des Baumes dessen 
		 * Kinderknoten anlegen und verwalten zugewiesen. 
		 * Die Instantiierung der Klasse {@link StringCell} führt zur Definition
		 * der in den Knoten dargestellten Informationen.
		 * 
		 * @return DefaultNodeInfo<String> Default implementation of NodeInfo
		 */
		if (value instanceof String
				&& (String) value == "Studiengang") {

			stringDataProvider = new ListDataProvider<String>();

			stringDataProvider.getList().add("Studiengang anlegen");
			stringDataProvider.getList().add("Studiengang verwalten");

			return new DefaultNodeInfo<String>(stringDataProvider,
					new StringCell(), selectionModel, null);
		}

		/**
		 * Hier werden dem Kinderknoten Raum des Baumes dessen 
		 * Kinderknoten anlegen und verwalten zugewiesen. 
		 * Die Instantiierung der Klasse {@link StringCell} führt zur Definition
		 * der in den Knoten dargestellten Informationen.
		 * 
		 * @return DefaultNodeInfo<String> Default implementation of NodeInfo
		 */
		if (value instanceof String && (String) value == "Raum") {

			stringDataProvider = new ListDataProvider<String>();

			stringDataProvider.getList().add("Raum anlegen");
			stringDataProvider.getList().add("Raum verwalten");

			return new DefaultNodeInfo<String>(stringDataProvider,
					new StringCell(), selectionModel, null);
		}
		
		/**
		 * Hier werden dem Kinderknoten Stundenplan des Baumes dessen 
		 * Kinderknoten anlegen und verwalten zugewiesen. 
		 * Die Instantiierung der Klasse {@link StringCell} führt zur Definition
		 * der in den Knoten dargestellten Informationen.
		 * 
		 * @return DefaultNodeInfo<String> Default implementation of NodeInfo
		 */
		if (value instanceof String
				&& (String) value == "Stundenplan") {

			stringDataProvider = new ListDataProvider<String>();

			stringDataProvider.getList().add("Stundenplan anlegen");
			stringDataProvider.getList().add("Stundenplan verwalten");

			return new DefaultNodeInfo<String>(stringDataProvider,
					new StringCell(), selectionModel, null);
		}

		/**
		 * Hier werden dem Kinderknoten Stundenplaneintrag des Baumes dessen 
		 * Kinderknoten anlegen und verwalten zugewiesen. 
		 * Die Instantiierung der Klasse {@link StringCell} führt zur Definition
		 * der in den Knoten dargestellten Informationen.
		 * 
		 * @return DefaultNodeInfo<String> Default implementation of NodeInfo
		 */
		if (value instanceof String
				&& (String) value == "Stundenplaneintrag") {

			stringDataProvider = new ListDataProvider<String>();

			stringDataProvider.getList().add(
					"Stundenplaneintrag anlegen");
			stringDataProvider.getList().add(
					"Stundenplaneintrag verwalten");

			return new DefaultNodeInfo<String>(stringDataProvider,
					new StringCell(), selectionModel, null);
		}

		/**
		 * Hier werden dem Kinderknoten Dozent verwalten des Baumes die erstellten
		 * Objekte hinzugefügt.
		 * Durch den Aufruf des 
		 *
		 * Durch den Aufruf der Methode <code>getAllDozenten()</code> rufen wir die 
		 * VerwaltungsklasseAsync auf, welche uns alle Dozenten der Datenbank über
		 * einen Callback zurückliefert. Diese Dozenten werden dann dem Baum durch 
		 * die Methode <code>add()</code> hinzugefügt.
		 * 
		 * Die Instantiierung der Klasse {@link DozentCell} führt zur Definition
		 * der in den Knoten dargestellten Informationen.
		 * 
		 * @return DefaultNodeInfo<Dozent> Default implementation of NodeInfo
		 */
		if (value instanceof String
				&& (String) value == "Dozent verwalten") {
			dozentDataProvider = new ListDataProvider<Dozent>();
			verwaltungsSvc
					.getAllDozenten(new AsyncCallback<Vector<Dozent>>() {
						public void onFailure(Throwable T) {

						}

						public void onSuccess(Vector<Dozent> dozenten) {
							for (Dozent d : dozenten) {
								dozentDataProvider.getList().add(d);
							}
						}
					});

			return new DefaultNodeInfo<Dozent>(dozentDataProvider,
					new DozentCell(), selectionModel, null);
		}

		/**
		 * Hier werden dem Kinderknoten Lehrveranstaltung verwalten des Baumes 
		 * die erstellten Objekte hinzugefügt.
		 * Durch den Aufruf der Methode <code>getAllLehrveranstaltungen()</code> 
		 * rufen wir die VerwaltungsklasseAsync auf, welche uns alle Lehrveranstaltungen
		 * der Datenbank über einen Callback zurückliefert. Diese Lehrveranstaltungen werden 
		 * dann dem Baum durch die Methode <code>add()</code> hinzugefügt.
		 * 
		 * Die Instantiierung der Klasse {@link LehrveranstaltungCell} führt zur 
		 * Definition der in den Knoten dargestellten Informationen.
		 * 
		 * @return DefaultNodeInfo<Lehrveranstaltung> Default implementation of NodeInfo
		 */
		if (value instanceof String
				&& (String) value == "Lehrveranstaltung verwalten") {
			lvDataProvider = new ListDataProvider<Lehrveranstaltung>();
			verwaltungsSvc
					.getAllLehrveranstaltungen(new AsyncCallback<Vector<Lehrveranstaltung>>() {
						public void onFailure(Throwable T) {

						}

						public void onSuccess(
								Vector<Lehrveranstaltung> lehrveranstaltungen) {
							for (Lehrveranstaltung lv : lehrveranstaltungen) {
								lvDataProvider.getList().add(lv);
							}
						}
					});

			return new DefaultNodeInfo<Lehrveranstaltung>(
					lvDataProvider, new LehrveranstaltungCell(),
					selectionModel, null);
		}

		/**
		 * Hier werden dem Kinderknoten Raum verwalten des Baumes die erstellten 
		 * Objekte hinzugefügt.
		 * Durch den Aufruf der Methode <code>getAllRaeume()</code> 
		 * rufen wir die VerwaltungsklasseAsync auf, welche uns alle Räume
		 * der Datenbank über einen Callback zurückliefert. Diese Räume werden 
		 * dann dem Baum durch die Methode <code>add()</code> hinzugefügt.
		 * 
		 * Die Instantiierung der Klasse {@link RaumCell} führt zur Definition der 
		 * in den Knoten dargestellten Informationen.
		 * 
		 * @return DefaultNodeInfo<Raum> Default implementation of NodeInfo
		 */
		if (value instanceof String
				&& (String) value == "Raum verwalten") {
			raumDataProvider = new ListDataProvider<Raum>();
			verwaltungsSvc
					.getAllRaeume(new AsyncCallback<Vector<Raum>>() {
						public void onFailure(Throwable T) {

						}

						public void onSuccess(Vector<Raum> raeume) {
							for (Raum r : raeume) {
								raumDataProvider.getList().add(r);
							}
						}
					});

			return new DefaultNodeInfo<Raum>(raumDataProvider,
					new RaumCell(), selectionModel, null);
		}

		/**
		 * Hier werden dem Kinderknoten Semesterverband verwalten des Baumes 
		 * die erstellten Objekte hinzugefügt.
		 * Durch den Aufruf der Methode <code>getAllSemesterverbaende()</code> 
		 * rufen wir die VerwaltungsklasseAsync auf, welche uns alle Semesterverbände
		 * der Datenbank über einen Callback zurückliefert. Diese Semesterverbände 
		 * werden dann dem Baum durch die Methode <code>add()</code> hinzugefügt. 
		 * 
		 * Die Instantiierung der Klasse {@link SemesterverbandCell} führt zur 
		 * Definition der in den Knoten dargestellten Informationen.
		 * 
		 * @return DefaultNodeInfo<Semesterverband> Default implementation of 
		 * NodeInfo
		 */
		if (value instanceof String
				&& (String) value == "Semesterverband verwalten") {
			svDataProvider = new ListDataProvider<Semesterverband>();
			verwaltungsSvc
					.getAllSemesterverbaende(new AsyncCallback<Vector<Semesterverband>>() {
						public void onFailure(Throwable T) {

						}

						public void onSuccess(
								Vector<Semesterverband> semesterverbaende) {
							for (Semesterverband sv : semesterverbaende) {
								svDataProvider.getList().add(sv);
							}
						}
					});

			return new DefaultNodeInfo<Semesterverband>(
					svDataProvider, new SemesterverbandCell(),
					selectionModel, null);
		}

		/**
		 * Hier werden dem Kinderknoten Studiengang verwalten des Baumes die 
		 * erstellten Objekte hinzugefügt.
		 * Durch den Aufruf der Methode <code>getAllStudiengaenge()</code> 
		 * rufen wir die VerwaltungsklasseAsync auf, welche uns alle Studiengänge
		 * der Datenbank über einen Callback zurückliefert. Diese Studiengänge 
		 * werden dann dem Baum durch die Methode <code>add()</code> hinzugefügt.
		 * 
		 * Die Instantiierung der Klasse {@link StudiengangCell} führt zur 
		 * Definition der in den Knoten dargestellten Informationen.
		 * 
		 * @return DefaultNodeInfo<Studiengang> Default implementation of 
		 * NodeInfo
		 */
		if (value instanceof String
				&& (String) value == "Studiengang verwalten") {
			sgDataProvider = new ListDataProvider<Studiengang>();
			verwaltungsSvc
					.getAllStudiengaenge(new AsyncCallback<Vector<Studiengang>>() {
						public void onFailure(Throwable T) {

						}

						public void onSuccess(
								Vector<Studiengang> studiengaenge) {
							for (Studiengang sg : studiengaenge) {
								sgDataProvider.getList().add(sg);
							}
						}
					});

			return new DefaultNodeInfo<Studiengang>(sgDataProvider,
					new StudiengangCell(), selectionModel, null);
		}

		/**
		 * Hier werden dem Kinderknoten Stundenplan verwalten des Baumes die 
		 * erstellten Objekte hinzugefügt.
		 * Durch den Aufruf der Methode <code>getAllStundenplaene()</code> 
		 * rufen wir die VerwaltungsklasseAsync auf, welche uns alle Stundenpläne
		 * der Datenbank über einen Callback zurückliefert. Diese Stundenpläne 
		 * werden dann dem Baum durch die Methode <code>add()</code> hinzugefügt.
		 * 
		 * Die Instantiierung der Klasse {@link StundenplanCell} führt zur 
		 * Definition der in den Knoten dargestellten Informationen.
		 * 
		 * @return DefaultNodeInfo<Stundenplan> Default implementation of 
		 * NodeInfo
		 */
		if (value instanceof String
				&& (String) value == "Stundenplan verwalten") {
			spDataProvider = new ListDataProvider<Stundenplan>();
			verwaltungsSvc
					.getAllStundenplaene(new AsyncCallback<Vector<Stundenplan>>() {
						public void onFailure(Throwable T) {

						}

						public void onSuccess(
								Vector<Stundenplan> studienhalbjahr) {
							for (Stundenplan sp : studienhalbjahr) {
								spDataProvider.getList().add(sp);
							}
						}
					});

			return new DefaultNodeInfo<Stundenplan>(spDataProvider,
					new StundenplanCell(), selectionModel, null);
		}

		/**
		 * Hier werden dem Kinderknoten Stundenplaneintrag verwalten des 
		 * Baumes die erstellten Objekte hinzugefügt.
		 * Durch den Aufruf der Methode <code>getAllStundenplaneintraege()</code> 
		 * rufen wir die VerwaltungsklasseAsync auf, welche uns alle Stundenplaneinträge
		 * der Datenbank über einen Callback zurückliefert. Diese Stundenplaneinträge 
		 * werden dann dem Baum durch die Methode <code>add()</code> hinzugefügt.
		 * 
		 * Die Instantiierung der Klasse {@link StundenplaneintragCell} führt 
		 * zur Definition der in den Knoten dargestellten Informationen.
		 * 
		 * @return DefaultNodeInfo<Stundenplaneintrag> Default implementation 
		 * of NodeInfo
		 */
		if (value instanceof String
				&& (String) value == "Stundenplaneintrag verwalten") {
			speDataProvider = new ListDataProvider<Stundenplaneintrag>();
			verwaltungsSvc
					.getAllStundenplaneintraege(new AsyncCallback<Vector<Stundenplaneintrag>>() {
						public void onFailure(Throwable T) {

						}

						public void onSuccess(
								Vector<Stundenplaneintrag> stundenplaneintrag) {
							for (Stundenplaneintrag spe : stundenplaneintrag) {
								speDataProvider.getList().add(spe);
							}
						}
					});

			return new DefaultNodeInfo<Stundenplaneintrag>(
					speDataProvider, new StundenplaneintragCell(),
					selectionModel, null);
		}
		return null;
	}

	/**
	 * Check if the value is known to be a leaf node.
	 * @return false
	 */
	@Override
	public boolean isLeaf(Object value) {
		return false;
	}
}
