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
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;

import de.hdm.stundenplansystem.shared.Verwaltungsklasse;
import de.hdm.stundenplansystem.shared.VerwaltungsklasseAsync;
import de.hdm.stundenplansystem.shared.bo.Semesterverband;
import de.hdm.stundenplansystem.shared.bo.Studiengang;
import de.hdm.stundenplansystem.shared.bo.Stundenplan;
import de.hdm.stundenplansystem.client.NavTreeViewModel;

public class CreateStundenplan extends Content {

	/**
	 * Jede Klasse enthï¿½t eine ï¿½berschrift, die definiert, was der User
	 * machen kann.
	 */
	private final HTML ueberschrift = new HTML(
			"<h2>Neues Studienhalbjahr anlegen<h2>");

	/**
	 * Unter der ï¿½berschrift trï¿½gt der User die Daten des neuen Studiengangs
	 * ein.
	 */
	final Label lbhalbjahr = new Label(
			"Studienhalbjahr des Stundenplans (z.B. SS 2012 oder WS 2012/2013):");
	final TextBox tbhalbjahr = new TextBox();
	final Label lbsemverband = new Label("Semesterverband:");
	final ListBox libsemverband = new ListBox();
	final Label lbstudiengang = new Label("Studiengang:");
	final ListBox libstudiengang = new ListBox();
	final Button speichern = new Button("Eingaben speichern");

	Vector<Semesterverband> svContainer = null;
	Vector<Studiengang> sgContainer = null;

	final VerwaltungsklasseAsync verwaltungsSvc = GWT
			.create(Verwaltungsklasse.class);
	NavTreeViewModel tvm = null;

	/**
	 * Anordnen der Buttons und Labels auf den Panels
	 */
	public void onLoad() {

		this.add(ueberschrift);
		this.add(lbstudiengang);
		this.add(libstudiengang);
		this.add(lbsemverband);
		this.add(libsemverband);
		this.add(lbhalbjahr);
		this.add(tbhalbjahr);
		this.add(speichern);

		setTvm(tvm);

		//libstudiengang.clear();
		verwaltungsSvc
				.getAllStudiengaenge(new AsyncCallback<Vector<Studiengang>>() {
					public void onFailure(Throwable T) {

					}

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

		libstudiengang.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				getSemverband();
			}
		});

		

		speichern.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				boolean allFilled = true;

				if (tbhalbjahr.getValue().isEmpty()) {
					allFilled = false;
					Window.alert("Bitte füllen Sie alle Felder aus.");
				}

				if (allFilled == true) {
					final String studienhalbjahr = tbhalbjahr
							.getValue().trim();
					int semesterverbandId = svContainer.elementAt(
							libsemverband.getSelectedIndex()).getId();

					verwaltungsSvc.createStundenplan(studienhalbjahr,
							semesterverbandId,
							new AsyncCallback<Stundenplan>() {

								@Override
								public void onFailure(Throwable caught) {
									Window.alert(caught.getMessage());
								}

								@Override
								public void onSuccess(
										Stundenplan result) {
									libstudiengang.clear();
									libsemverband.clear();
									tbhalbjahr.setText("");
									Window.alert("Erfolgreich gespeichert.");
									tvm.addStundenplan(result);
								}
							});
				}
			}
		});
	}

	public void setTvm(NavTreeViewModel tvm) {
		this.tvm = tvm;
	}

	public void getSemverband() {
		libsemverband.clear();
		System.out.println("Index: " + sgContainer.elementAt(
				libstudiengang.getSelectedIndex()));
		System.out.println("Id: " + sgContainer.elementAt(
				libstudiengang.getSelectedIndex()).getId());
		verwaltungsSvc.getSemsterverbaendeByStudiengang(
				sgContainer.elementAt(
						libstudiengang.getSelectedIndex()).getId(),
				new AsyncCallback<Vector<Semesterverband>>() {
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
	
}
