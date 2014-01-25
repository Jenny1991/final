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
 * @author Thies, V.Hofmann, Espich
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
	 * anzulegenden Stundenplaneintrags mit Hilfe von List Boxen.
	 */
	final ListBox libZeitslot = new ListBox();
	final ListBox libDozent = new ListBox();
	final ListBox libLehrveranstaltung = new ListBox();
	final ListBox libRaum = new ListBox();
	final ListBox libStudiengang = new ListBox();
	final ListBox libSemesterverband = new ListBox();
	final ListBox libStudienhj = new ListBox();
	final Button speichern = new Button("Änderungen speichern");
	final Button loeschen = new Button("Stundenplaneintrag löschen");

	Vector<Studiengang> sgContainer = null;
	Vector<Semesterverband> svContainer = null;
	Vector<Stundenplan> spContainer = null;
	Vector<Lehrveranstaltung> lvContainer = null;
	Vector<Dozent> dozentContainer = null;
	Vector<Raum> raumContainer = null;
	Vector<Zeitslot> zsContainer = null;

	final VerwaltungsklasseAsync verwaltungsSvc = GWT
			.create(Verwaltungsklasse.class);

	Integer id;
	Stundenplaneintrag shownSpe = null;
	NavTreeViewModel tvm = null;

	public StundenplaneintragForm() {
		this.clear();
		Grid speGrid = new Grid(9, 2);
		this.add(ueberschriftAenderung);
		this.add(speGrid);

		Label lbStudiengang = new Label("Studiengang");
		speGrid.setWidget(0, 0, lbStudiengang);
		speGrid.setWidget(0, 1, libStudiengang);

		Label lbSemesterverband = new Label("Semesterverband");
		speGrid.setWidget(1, 0, lbSemesterverband);
		speGrid.setWidget(1, 1, libSemesterverband);

		Label lbStundenplan = new Label("Studienhalbjahr");
		speGrid.setWidget(2, 0, lbStundenplan);
		speGrid.setWidget(2, 1, libStudienhj);

		Label lbLehrveranstaltung = new Label("Lehrveranstaltung");
		speGrid.setWidget(3, 0, lbLehrveranstaltung);
		speGrid.setWidget(3, 1, libLehrveranstaltung);

		Label lbDozent = new Label("Dozent");
		speGrid.setWidget(4, 0, lbDozent);
		speGrid.setWidget(4, 1, libDozent);

		Label lbRaum = new Label("Raum");
		speGrid.setWidget(5, 0, lbRaum);
		speGrid.setWidget(5, 1, libRaum);

		Label lbZeitslot = new Label("Zeitslot");
		speGrid.setWidget(6, 0, lbZeitslot);
		speGrid.setWidget(6, 1, libZeitslot);

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

		this.clearFields();

		libStudiengang.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				getSemesterverbaende();
			}
		});

		libSemesterverband.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				getStundenplaene();
			}
		});

		libRaum.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				getZeitslots();
			}
		});

	}

	public void getData() {
		verwaltungsSvc.getStundenplaneintragById(id,
				new AsyncCallback<Stundenplaneintrag>() {
					@Override
					public void onFailure(Throwable caught) {
					}

					@Override
					public void onSuccess(Stundenplaneintrag result) {
						if (result != null) {
							setSelected(result);
						}
					}
				});
	}

	public void deleteSelectedSpe() {
		verwaltungsSvc.deleteStundenplaneintrag(shownSpe,
				new AsyncCallback<Void>() {
					public void onFailure(Throwable caught) {
						Window.alert("Der Stundenplaneintrag konnte nicht gelöscht werden!");
					}

					public void onSuccess(Void result) {
						tvm.deleteSpe(shownSpe);
						Window.alert("Erfolgreich gelöscht.");
					}
				});
		this.clearFields();
	}

	public void changeSelectedSpe() {
		shownSpe.setSemesterverbandId(svContainer.elementAt(
				libSemesterverband.getSelectedIndex() - 1).getId());
		shownSpe.setStundenplanId(spContainer.elementAt(
				libStudienhj.getSelectedIndex() - 1).getId());
		shownSpe.setLehrveranstaltungId(lvContainer.elementAt(
				libLehrveranstaltung.getSelectedIndex() - 1).getId());
		shownSpe.setDozentId(dozentContainer.elementAt(
				libDozent.getSelectedIndex() - 1).getId());
		shownSpe.setRaumId(raumContainer.elementAt(
				libRaum.getSelectedIndex() - 1).getId());
		shownSpe.setZeitslotId(zsContainer.elementAt(
				libZeitslot.getSelectedIndex() - 1).getId());

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
						libStudienhj.addItem(result.getStudienhalbjahr());
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
						libSemesterverband.addItem(result.getKuerzel()
								+ ", Semester: "
								+ String.valueOf(result.getSemester()));
						getNextListSg();
					}
				});
	}
		
	public void getNextListSg() {
		verwaltungsSvc.getStudiengangBySemesterverbandId(
				shownSpe.getSemesterverbandId(),
				new AsyncCallback<Studiengang>() {
					@Override
					public void onFailure(Throwable caught) {
						caught.getMessage();
					}

					@Override
					public void onSuccess(Studiengang result) {
						libStudiengang.addItem(result
								.getBezeichnung());
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
						libLehrveranstaltung.addItem(result
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
						libDozent.addItem(result.getNachname() + ", "
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
						libRaum.addItem(result.getBezeichnung());
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
						libZeitslot.addItem(result.getWochentag()
								+ ", " + result.getAnfangszeit()
								+ ", " + result.getEndzeit());
						getStudiengaenge();
					}
				});
	}

	public void clearFields() {
		libZeitslot.clear();
		libDozent.clear();
		libLehrveranstaltung.clear();
		libRaum.clear();
		libStudiengang.clear();
		libSemesterverband.clear();
		libStudienhj.clear();
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
							libStudiengang.addItem(
									sg.getBezeichnung(),
									String.valueOf(sg.getId()));
						}
						getSemesterverbaende();
					}
				});
	}

	public void getSemesterverbaende() {
		verwaltungsSvc
				.getAllSemesterverbaende(new AsyncCallback<Vector<Semesterverband>>() {
					@Override
					public void onFailure(Throwable caught) {
						caught.getMessage();
					}

					@Override
					public void onSuccess(
							Vector<Semesterverband> semesterverband) {
						svContainer = semesterverband;
						for (Semesterverband sv : semesterverband) {
							libSemesterverband.addItem(sv
									.getKuerzel()
									+ " "
									+ String.valueOf(sv.getSemester()));
						}
						getStundenplaene();
					}
				});
	}

	public void getStundenplaene() {
		verwaltungsSvc
				.getAllStundenplaene(new AsyncCallback<Vector<Stundenplan>>() {
					@Override
					public void onFailure(Throwable caught) {
						caught.getMessage();
					}

					@Override
					public void onSuccess(
							Vector<Stundenplan> stundenplan) {
						spContainer = stundenplan;
						for (Stundenplan sp : stundenplan) {
							libStudienhj.addItem(String.valueOf(sp
									.getStudienhalbjahr()));
						}
						getLehrveranstaltungen();
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
							libLehrveranstaltung.addItem(lv
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
							libDozent.addItem(d.getNachname() + ", "
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
							libRaum.addItem(r.getBezeichnung());
						}
						getZeitslots();
					}
				});
	}

	public void getZeitslots() {
		verwaltungsSvc
				.getAllZeitslots(new AsyncCallback<Vector<Zeitslot>>() {
					@Override
					public void onFailure(Throwable caught) {
						caught.getMessage();
					}

					@Override
					public void onSuccess(Vector<Zeitslot> zeitslot) {
						zsContainer = zeitslot;
						for (Zeitslot z : zeitslot) {
							libZeitslot.addItem(z.getWochentag()
									+ ", " + z.getAnfangszeit()
									+ ", " + z.getEndzeit());
						}
					}
				});
	}

}
