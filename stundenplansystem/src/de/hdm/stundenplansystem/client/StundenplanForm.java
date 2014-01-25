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
import com.google.gwt.user.client.ui.TextBox;
import de.hdm.stundenplansystem.shared.*;
import de.hdm.stundenplansystem.shared.bo.Semesterverband;
import de.hdm.stundenplansystem.shared.bo.Studiengang;
import de.hdm.stundenplansystem.shared.bo.Stundenplan;
import de.hdm.stundenplansystem.client.NavTreeViewModel;

/**
 * Formular für die Darstellung des selektierten Kunden
 * 
 * @author Thies, Espich
 */

public class StundenplanForm extends Content {

	private final HTML ueberschrift = new HTML(
			"<h2>Übersicht der Stundenpläne pro Studienhalbjahre<h2>");

	final TextBox tbhalbjahr = new TextBox();
	final ListBox libsemverband = new ListBox();
	final ListBox libstudiengang = new ListBox();
	final Button loeschen = new Button("Studienhalbjahr löschen");
	final Button speichern = new Button("Änderungen speichern");
	final VerwaltungsklasseAsync verwaltungsSvc = GWT
			.create(Verwaltungsklasse.class);

	Vector<Semesterverband> svContainer = null;
	Vector<Studiengang> sgContainer = null;

	Integer id;
	Stundenplan shownSp = null;
	NavTreeViewModel tvm = null;

	public StundenplanForm() {
		Grid stGrid = new Grid(5, 2);
		this.add(ueberschrift);
		this.add(stGrid);

		Label lbstudiengang = new Label("Studiengang:");
		stGrid.setWidget(0, 0, lbstudiengang);
		stGrid.setWidget(0, 1, libstudiengang);

		Label lbsemverband = new Label("Semesterverband:");
		stGrid.setWidget(1, 0, lbsemverband);
		stGrid.setWidget(1, 1, libsemverband);

		Label lbhalbjahr = new Label("Studienhalbjahr:");
		stGrid.setWidget(2, 0, lbhalbjahr);
		stGrid.setWidget(2, 1, tbhalbjahr);

		Label lbfunktionen = new Label("Funktionen:");
		stGrid.setWidget(3, 0, lbfunktionen);
		stGrid.setWidget(3, 1, speichern);
		speichern.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				changeSelectedHj();
			}
		});
		stGrid.setWidget(4, 1, loeschen);
		loeschen.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				deleteSelectedHj();
			}
		});
		setTvm(tvm);
	}

	public void onLoad() {
		verwaltungsSvc.getStundenplanById(id,
				new AsyncCallback<Stundenplan>() {
					@Override
					public void onFailure(Throwable caught) {
					}

					@Override
					public void onSuccess(Stundenplan sp) {
						if (sp != null) {
							setSelected(sp);
						}
					};
				});

		libstudiengang.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				getSemverband();
			}
		});

	}

	public void setFields() {
		this.clearFields();
		tbhalbjahr.setText(shownSp.getStudienhalbjahr());
		verwaltungsSvc.getSemesterverbandById(
				shownSp.getSemesterverbandId(),
				new AsyncCallback<Semesterverband>() {
					@Override
					public void onFailure(Throwable caught) {
						caught.getMessage();
					}

					@Override
					public void onSuccess(Semesterverband result) {
						libsemverband.addItem(result.getJahrgang()
								+ ", Semester: "
								+ String.valueOf(result.getSemester()));
						getNextListSg();
					}
				});
	}

	public void getNextListSg() {
		verwaltungsSvc.getStudiengangBySemesterverbandId(
				shownSp.getSemesterverbandId(),
				new AsyncCallback<Studiengang>() {
					@Override
					public void onFailure(Throwable caught) {
						caught.getMessage();
					}

					@Override
					public void onSuccess(Studiengang result) {
						libstudiengang.addItem(result
								.getBezeichnung());
						getStudiengaenge();
					}
				});
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
							libstudiengang.addItem(
									sg.getBezeichnung(),
									String.valueOf(sg.getId()));
						}
						getSemverband();
					}
				});
	}

	public void getSemverband() {
		verwaltungsSvc
				.getAllSemesterverbaende(new AsyncCallback<Vector<Semesterverband>>() {
					public void onFailure(Throwable caught) {
						caught.getMessage();
					}

					public void onSuccess(
							Vector<Semesterverband> semesterverband) {
						svContainer = semesterverband;
						for (Semesterverband sv : semesterverband) {
							libsemverband.addItem(sv.getJahrgang()
									+ ", Semester: "
									+ String.valueOf(sv.getSemester()));
						}
					}
				});
	}

	public void changeSelectedHj() {

		boolean allFilled = true;

		if (tbhalbjahr.getValue().isEmpty()) {
			allFilled = false;
			Window.alert("Bitte füllen Sie alle Felder aus.");
		}

		if (allFilled == true) {
			shownSp.setStudienhalbjahr(tbhalbjahr.getText().trim());
			shownSp.setSemesterverbandId(svContainer.elementAt(
					libsemverband.getSelectedIndex() - 1).getId());

			verwaltungsSvc.changeStundenplan(shownSp,
					new AsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert(caught.getMessage());
						}

						@Override
						public void onSuccess(Void result) {
							tvm.updateStudienhalbjahr(shownSp);
							Window.alert("Erfolgreich gespeichert.");

						}
					});
		}
	}

	public void deleteSelectedHj() {
		verwaltungsSvc.deleteStundenplan(shownSp,
				new AsyncCallback<Void>() {
					public void onFailure(Throwable caught) {
						Window.alert(caught.getMessage());
					}

					public void onSuccess(Void result) {
						tvm.deleteStudienhalbjahr(shownSp);
						Window.alert("Erfolgreich gelöscht.");
					}
				});
		this.clearFields();
	}

	public void setTvm(NavTreeViewModel tvm) {
		this.tvm = tvm;
	}

	public void setSelected(Stundenplan sp) {
		if (sp != null) {
			shownSp = sp;
			setFields();
		} else {
			clearFields();
		}
	}

	public void clearFields() {
		libstudiengang.clear();
		libsemverband.clear();
		tbhalbjahr.setText("");
	}
}
