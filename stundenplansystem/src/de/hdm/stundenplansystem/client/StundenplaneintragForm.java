package de.hdm.stundenplansystem.client;

import java.util.Vector;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;

import de.hdm.stundenplansystem.shared.VerwaltungsklasseAsync;
import de.hdm.stundenplansystem.shared.bo.Dozent;
import de.hdm.stundenplansystem.shared.bo.Lehrveranstaltung;
import de.hdm.stundenplansystem.shared.bo.Raum;
import de.hdm.stundenplansystem.shared.bo.Semesterverband;
import de.hdm.stundenplansystem.shared.bo.Studiengang;
import de.hdm.stundenplansystem.shared.bo.Stundenplan;
import de.hdm.stundenplansystem.shared.bo.Stundenplaneintrag;
import de.hdm.stundenplansystem.shared.bo.Zeitslot;
import de.hdm.stundenplansystem.shared.Verwaltungsklasse;

/**
 * Klasse, in der ein bereits bestehender Stundenplaneintrag angezeigt, 
 * gelöscht und bearbeitet wird.
 * Diese Klasse erbt von der Klasse Content und lässt sich somit 
 * unter GWT entsprechend anordnen.
 * 
 * @see StundenplanForm
 * @author Thies, Espich, Schmieder, V. Hofmann
 * @version 1.0
 */
public class StundenplaneintragForm extends Content {

	/**
	 * Jede Klasse enthält eine Überschrift, die definiert, was der User
	 * machen kann.
	 */
	private final HTML ueberschriftAenderung = new HTML(
			"<h2>Stundenplaneintrag bearbeiten<h2>");
	
	/**
	 * Unter der Überschrift wählt der User die Daten des
	 * zu bearbeitenden Stundenplaneintrags mit Hilfe von ListBoxen.
	 */
	final ListBox listDozent = new ListBox();
	final ListBox listZeitslot = new ListBox();
	final ListBox listRaum = new ListBox();
	final ListBox listStudienhj = new ListBox();
	final ListBox listSemesterverband = new ListBox();
	final ListBox listLehrveranstaltung = new ListBox();
	final ListBox listStudiengang = new ListBox();
	final Button speichern = new Button("Änderungen speichern");
	final Button loeschen = new Button("Stundenplaneintrag löschen");

	/**
	 * Hier werden Vectoren des Business Objekte festgelegt
	 */
	Vector<Studiengang> sgContainer = null;
	Vector<Semesterverband> svContainer = null;
	Vector<Stundenplan> spContainer = null;
	Vector<Lehrveranstaltung> lvContainer = null;
	Vector<Dozent> dozentContainer = null;
	Vector<Raum> raumContainer = null;
	Vector<Zeitslot> zsContainer = null;

	/**
	 * Hier wird ein Remote Service Proxy erstellt, welches uns erlaubt, 
	 * mit dem serverseitigen Verwaltungsservice zu kommunizieren.
	 */
	final VerwaltungsklasseAsync verwaltungsSvc = GWT
			.create(Verwaltungsklasse.class);

	Semesterverband aktSv = null;
	Studiengang aktSg = null;
	Stundenplaneintrag shownSpe = null;
	NavTreeViewModel tvm = null;
	int aktraumid;
	int aktdozentid;

	  /**
	   * Jedes Formular wird durch einen Konstruktor dargestellt. 
	   * In diesem wird eine Instanz des GWT Widgets Grid erzeugt, dass den Aufbau
	   * des Formulars darstellt.
	   * Durch die Methode <code>add()</code> werden die Widgets dem Panel hinzugefügt.
	   * Durch die Methode <code>setWidget()</code> werden die Widgets 
	   * in den Zeilen und Spalten der Grid hinzugefügt.
	   */
	public StundenplaneintragForm() {
		Grid speGrid = new Grid(9, 2);
		this.add(ueberschriftAenderung);
		this.add(speGrid);

		Label lbStudiengang = new Label("Studiengang");
		speGrid.setWidget(0, 0, lbStudiengang);
		speGrid.setWidget(0, 1, listStudiengang);

		Label lbSemesterverband = new Label("Semesterverband");
		speGrid.setWidget(1, 0, lbSemesterverband);
		speGrid.setWidget(1, 1, listSemesterverband);

		Label lbStundenplan = new Label("Studienhalbjahr");
		speGrid.setWidget(2, 0, lbStundenplan);
		speGrid.setWidget(2, 1, listStudienhj);

		Label lbLehrveranstaltung = new Label("Lehrveranstaltung");
		speGrid.setWidget(3, 0, lbLehrveranstaltung);
		speGrid.setWidget(3, 1, listLehrveranstaltung);

		Label lbDozent = new Label("Dozent");
		speGrid.setWidget(4, 0, lbDozent);
		speGrid.setWidget(4, 1, listDozent);

		Label lbRaum = new Label("Raum");
		speGrid.setWidget(5, 0, lbRaum);
		speGrid.setWidget(5, 1, listRaum);

		Label lbZeitslot = new Label("Zeitslot");
		speGrid.setWidget(6, 0, lbZeitslot);
		speGrid.setWidget(6, 1, listZeitslot);

		Label lbFunktionen = new Label("Funktionen");
		speGrid.setWidget(7, 0, lbFunktionen);
		speGrid.setWidget(7, 1, speichern);
		speichern.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				changeSelectedSpe();
			}
		});

		speGrid.setWidget(8, 1, loeschen);
		loeschen.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				deleteSelectedSpe();
			}
		});
		setTvm(tvm);
				
		listStudiengang.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				listSemesterverband.clear();
				getSemverbandChange();
			}
		});
		
		listDozent.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				listZeitslot.clear();
				getZeitslots();
				
			}
		});
		
		listRaum.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				listZeitslot.clear();
				getZeitslots();
			}
		});
		
	}

	public void changeSelectedSpe() {
		
		/**
		 * Immer abfragen, ob der Wert der ListBox ungleich 0 ist, 
		 * da bei keiner Änderung der ListBox dieser nicht gespeichert wird. 
		 */		
//		if (listStudienhj.getSelectedIndex() != 0)
		shownSpe.setStundenplanId(spContainer.elementAt(
				listStudienhj.getSelectedIndex()).getId());
		if (listLehrveranstaltung.getSelectedIndex() != 0)
		shownSpe.setLehrveranstaltungId(lvContainer.elementAt(
				listLehrveranstaltung.getSelectedIndex() - 1).getId());
		if (listDozent.getSelectedIndex() != 0)
		shownSpe.setDozentId(dozentContainer.elementAt(
				listDozent.getSelectedIndex() - 1).getId());
		if (listRaum.getSelectedIndex() != 0)
		shownSpe.setRaumId(raumContainer.elementAt(
				listRaum.getSelectedIndex() - 1).getId());
		if (listZeitslot.getSelectedIndex() != 0)
		shownSpe.setZeitslotId(zsContainer.elementAt(
				listZeitslot.getSelectedIndex() - 1).getId());

		verwaltungsSvc.changeStundenplaneintrag(shownSpe,
				new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert(caught.getMessage());
					}

					@Override
					public void onSuccess(Void result) {
						Window.alert("Erfolgreich gespeichert.");
						tvm.updateSpe(shownSpe);

					}
				});
	}
	
	public void deleteSelectedSpe() {
		verwaltungsSvc.deleteStundenplaneintrag(shownSpe,
				new AsyncCallback<Void>() {
					
					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Der Stundenplaneintrag konnte nicht gelöscht werden!");
					}

					@Override
					public void onSuccess(Void result) {
						tvm.deleteSpe(shownSpe);
						Window.alert("Erfolgreich gelöscht.");
					}
				});
		this.clearFields();
	}

	/**
	 * Die Methode <code>setTvm()</code> sorgt dafür, 
	 * dass die Klasse {@link NavTreeViewModel} auf diese Klasse zugreifen kann
	 * 
	 * @param tvm Instanz der Klasse {@link NavTreeViewModel}
	 */
	public void setTvm(NavTreeViewModel tvm) {
		this.tvm = tvm;
	}

	public void setSelected(Stundenplaneintrag spe) {
		if (spe != null) {
			shownSpe = spe;
			setFields();
		} else {
			clearFields();
		}
	}

	/**
	 * Ab hier befüllen wir die Widgets mit den Daten des gewählten Stundenplans
	 * Zunächst wird die ListBox des Semesterverbands befüllt.
	 * Anschließend holen wir uns den Studiengang der zu diesem Semesterverband 
	 * gehört. Danach werden beide ListBoxen wieder mit allen Elementen des Semesterverbandes
	 * sowie des Studiengangs befüllt
	 */
	public void setFields() {
		this.clearFields();
		verwaltungsSvc.getStundenplanById(
				shownSpe.getStundenplanId(), 
				new AsyncCallback<Stundenplan>() {
					@Override
					public void onFailure(Throwable caught) {
						caught.getMessage();
					}

					@Override
					public void onSuccess(Stundenplan result) {
						listStudienhj.addItem(result.getStudienhalbjahr());
						getNextListSv();
					}
				});
	}
		
	public void getNextListSv() {
		verwaltungsSvc.getSemesterverbandByStundenplanId(
				shownSpe.getStundenplanId(), 
				new AsyncCallback<Semesterverband>(){
					@Override
					public void onFailure(Throwable caught) {
						caught.getMessage();
					}

					@Override
					public void onSuccess(Semesterverband result) {
						listSemesterverband.addItem(result.getKuerzel()
								+ ", Semester: "
								+ String.valueOf(result.getSemester()));
						aktSv = result;
						getNextListSg();
					}
				});
	}
		
	public void getNextListSg() {
		verwaltungsSvc.getStudiengangBySemesterverbandId(
				aktSv.getId(),
				new AsyncCallback<Studiengang>() {
					@Override
					public void onFailure(Throwable caught) {
						caught.getMessage();
					}

					@Override
					public void onSuccess(Studiengang result) {
						listStudiengang.addItem(result
								.getBezeichnung());
						aktSg = result;
						getNextListLv();
					}
				});
	}

	public void getNextListLv() {
		verwaltungsSvc.getLehrveranstaltungById(
				shownSpe.getLehrveranstaltungId(),
				new AsyncCallback<Lehrveranstaltung>() {
					@Override
					public void onFailure(Throwable caught) {
						caught.getMessage();
					}

					@Override
					public void onSuccess(Lehrveranstaltung result) {
						listLehrveranstaltung.addItem(result
								.getBezeichnung());
						getNextListDozent();
					}
				});
	}

	public void getNextListDozent() {
		verwaltungsSvc.getDozentById(shownSpe.getDozentId(),
				new AsyncCallback<Dozent>() {
					@Override
					public void onFailure(Throwable caught) {
						caught.getMessage();
					}

					@Override
					public void onSuccess(Dozent result) {
						listDozent.addItem(result.getNachname() + ", "
								+ result.getVorname());
						getNextListRaum();
					}
				});
	}

	public void getNextListRaum() {
		verwaltungsSvc.getRaumById(shownSpe.getRaumId(),
				new AsyncCallback<Raum>() {
					@Override
					public void onFailure(Throwable caught) {
						caught.getMessage();
					}

					@Override
					public void onSuccess(Raum result) {
						listRaum.addItem(result.getBezeichnung());
						getNextListZs();
					}
				});
	}

	public void getNextListZs() {
		verwaltungsSvc.getZeitslotById(shownSpe.getZeitslotId(),
				new AsyncCallback<Zeitslot>() {
					@Override
					public void onFailure(Throwable caught) {
						caught.getMessage();
					}

					@Override
					public void onSuccess(Zeitslot result) {
						listZeitslot.addItem(result.getWochentag()
								+ ", " + result.getAnfangszeit()
								+ ", " + result.getEndzeit());
						getStudiengaenge();
					}
				});
	}

	/**
	 * Hier löschen wir den Inhalt der Widgets
	 */
	public void clearFields() {
		listZeitslot.clear();
		listDozent.clear();
		listLehrveranstaltung.clear();
		listStudiengang.clear();
		listRaum.clear();
		listSemesterverband.clear();
		listStudienhj.clear();
	}

	public void getStudiengaenge() {
		verwaltungsSvc
				.getAllStudiengaenge(new AsyncCallback<Vector<Studiengang>>() {
					@Override
					public void onFailure(Throwable caught) {
						caught.getMessage();
					}

					@Override
					public void onSuccess(
							Vector<Studiengang> studiengang) {
						sgContainer = studiengang;
						for (Studiengang sg : studiengang) {
							listStudiengang.addItem(
									sg.getBezeichnung(),
									String.valueOf(sg.getId()));
						}
						getSemverband();
					}
				});
	}

	public void getSemverband() {
		listStudienhj.clear();
		verwaltungsSvc.getSemsterverbaendeByStudiengang(
				aktSg.getId(),
				new AsyncCallback<Vector<Semesterverband>>() {
					@Override
					public void onFailure(Throwable T) {
					}

					@Override
					public void onSuccess(
							Vector<Semesterverband> semesterverband) {
						svContainer = semesterverband;
						for (Semesterverband sv : semesterverband) {
							listSemesterverband.addItem(sv.getKuerzel()
									+ ", Semester: "
									+ String.valueOf(sv.getSemester()));
						}
						getStundenplan();
					}
				});
		
		listSemesterverband.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				listStudienhj.clear();
				getChangeStundenplan();
			}
		});
	}

	public void getStundenplan() {
		verwaltungsSvc.getStundenplaeneBySemesterverband(svContainer
				.elementAt(listSemesterverband.getSelectedIndex()).getId(),
				new AsyncCallback<Vector<Stundenplan>>() {
					@Override
					public void onFailure(Throwable T) {
					}

					@Override
					public void onSuccess(
							Vector<Stundenplan> stundenplaene) {
						spContainer = stundenplaene;
						for (Stundenplan sp : stundenplaene) {
							listStudienhj.addItem(
									sp.getStudienhalbjahr(),
									String.valueOf(sp.getId()));
						}
						getLehrveranstaltungen();
					}
				});
	}

	public void getChangeStundenplan() {
		listStudienhj.clear();
		verwaltungsSvc.getStundenplaeneBySemesterverband(svContainer
				.elementAt(listSemesterverband.getSelectedIndex()).getId(),
				new AsyncCallback<Vector<Stundenplan>>() {
					@Override
					public void onFailure(Throwable T) {
					}

					@Override
					public void onSuccess(
							Vector<Stundenplan> stundenplaene) {
						spContainer = stundenplaene;
						for (Stundenplan sp : stundenplaene) {
							listStudienhj.addItem(
									sp.getStudienhalbjahr(),
									String.valueOf(sp.getId()));
						}
					}
				});
	}
	
	public void getLehrveranstaltungen() {
		verwaltungsSvc
				.getAllLehrveranstaltungen(new AsyncCallback<Vector<Lehrveranstaltung>>() {
					@Override
					public void onFailure(Throwable caught) {
						caught.getMessage();
					}

					@Override
					public void onSuccess(
							Vector<Lehrveranstaltung> lehrveranstaltung) {
						lvContainer = lehrveranstaltung;
						for (Lehrveranstaltung lv : lehrveranstaltung) {
							listLehrveranstaltung.addItem(lv
									.getBezeichnung());
						}
						getDozenten();
					}
				});
	}

	public void getDozenten() {
		verwaltungsSvc
				.getAllDozenten(new AsyncCallback<Vector<Dozent>>() {
					@Override
					public void onFailure(Throwable caught) {
						caught.getMessage();
					}

					@Override
					public void onSuccess(Vector<Dozent> dozent) {
						dozentContainer = dozent;
						for (Dozent d : dozent) {
							listDozent.addItem(d.getNachname() + ", "
									+ d.getVorname());
							}
						getRaeume();
					}
				});
	}

	public void getRaeume() {
		verwaltungsSvc
				.getAllRaeume(new AsyncCallback<Vector<Raum>>() {
					@Override
					public void onFailure(Throwable caught) {
						caught.getMessage();
					}

					@Override
					public void onSuccess(Vector<Raum> raum) {
						raumContainer = raum;
						for (Raum r : raum) {
							listRaum.addItem(r.getBezeichnung());
						}
						getZeitslots();
					}
				});
	}

	public void getZeitslots() {
		
		if(listRaum.getSelectedIndex() != 0)
		aktraumid = raumContainer.elementAt(listRaum.getSelectedIndex()-1).getId();
		else
		aktraumid = raumContainer.elementAt(listRaum.getSelectedIndex()).getId();
		
		if(listDozent.getSelectedIndex() != 0)
		aktdozentid = dozentContainer.elementAt(listDozent.getSelectedIndex()-1).getId();
		else
		aktdozentid = dozentContainer.elementAt(listDozent.getSelectedIndex()).getId();
		
		
		verwaltungsSvc
				.getFreieZeitslot(aktraumid,
				aktdozentid,
				spContainer.elementAt(
						listStudienhj.getSelectedIndex()).getStudienhalbjahr(), 
				spContainer.elementAt(
						listStudienhj.getSelectedIndex()).getId(), 
						new AsyncCallback<Vector<Zeitslot>>() {
					@Override
					public void onFailure(Throwable caught) {
						caught.getMessage();
					}

					@Override
					public void onSuccess(Vector<Zeitslot> zeitslot) {
						zsContainer = zeitslot;
						for (Zeitslot z : zeitslot) {
							listZeitslot.addItem(z.getWochentag()
									+ ", " + z.getAnfangszeit()
									+ ", " + z.getEndzeit());
						}
					}
				});
	}
	
	
	
	/**
	 * Diese Methode wird nur aufgerufen, wenn der ChangeHandler aktiviert wird
	 */
	public void getSemverbandChange() {
		listStudienhj.clear();
		verwaltungsSvc.getSemsterverbaendeByStudiengang(
				sgContainer.elementAt(
						listStudiengang.getSelectedIndex()-1).getId(),
				new AsyncCallback<Vector<Semesterverband>>() {
					@Override
					public void onFailure(Throwable T) {
					}
					
					@Override
					public void onSuccess(
							Vector<Semesterverband> semesterverband) {
						svContainer = semesterverband;
						for (Semesterverband sv : semesterverband) {
							listSemesterverband.addItem(sv.getKuerzel()
									+ ", Semester: "
									+ String.valueOf(sv.getSemester()));
						}
						getStundenplanChange();
					}
				});
		
		}
	
	public void getStundenplanChange() {
		verwaltungsSvc.getStundenplaeneBySemesterverband(svContainer
				.elementAt(listSemesterverband.getSelectedIndex()).getId(),
				new AsyncCallback<Vector<Stundenplan>>() {
					@Override
					public void onFailure(Throwable T) {
					}

					@Override
					public void onSuccess(
							Vector<Stundenplan> stundenplaene) {
						spContainer = stundenplaene;
						for (Stundenplan sp : stundenplaene) {
							listStudienhj.addItem(
									sp.getStudienhalbjahr(),
									String.valueOf(sp.getId()));
						}
						
					}
				});
	}
	
	
	
}