package de.hdm.stundenplansystem.client;

import java.util.Vector;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ListBox;

import de.hdm.stundenplansystem.shared.*;
import de.hdm.stundenplansystem.shared.bo.Studiengang;
import de.hdm.stundenplansystem.shared.bo.Stundenplan;
import de.hdm.stundenplansystem.shared.bo.Semesterverband;
import de.hdm.stundenplansystem.client.NavTreeViewModel;

/**
 * Hier wird ein neuer Semesterverband angelegt.
 * 
 * @author Thies, Espich
 * 
 */

public class CreateSemesterverband extends Content {

	/**
	 * Jede Klasse enth������t eine ������berschrift, die definiert, was der
	 * User machen kann.
	 */
	private final HTML ueberschrift = new HTML(
			"<h2>Neuen Semesterverband anlegen<h2>");

	/**
	 * Unter der ������berschrift tr������gt der User die Daten des neuen
	 * Semesterverbands ein.
	 */
	final Label lbjahrgang = new Label(
			"Beginn des Studiums (z.B. SS 2012 oder WS 2012/2013):");
	final Label lbstudiengang = new Label("Studiengang:");
	final Label lbsemester = new Label(
			"aktuelles Semester (z.B. 1, 2, 3, ...):");
	final Label lbanzahl = new Label("Anzahl der Studierenden:");
	final TextBox tbjahrgang = new TextBox();
	final ListBox libstudiengang = new ListBox();
	final TextBox tbsemester = new TextBox();
	final TextBox tbanzahl = new TextBox();
	final Button speichern = new Button("Eingaben speichern");

	Vector<Studiengang> sgContainer = null;

	final VerwaltungsklasseAsync verwaltungsSvc = GWT
			.create(Verwaltungsklasse.class);
	NavTreeViewModel tvm = null;

	/**
	 * Anordnen der Buttons und Labels auf den Panels
	 */
	public void onLoad() {

		this.add(ueberschrift);
		this.add(lbjahrgang);
		this.add(tbjahrgang);
		this.add(lbstudiengang);
		this.add(libstudiengang);
		this.add(lbsemester);
		this.add(tbsemester);
		this.add(lbanzahl);
		this.add(tbanzahl);
		this.add(speichern);

		setTvm(tvm);

		libstudiengang.clear();
		verwaltungsSvc
				.getAllStudiengaenge(new AsyncCallback<Vector<Studiengang>>() {
					public void onFailure(Throwable T) {

					}

					public void onSuccess(
							Vector<Studiengang> studiengaenge) {
						sgContainer = studiengaenge;
						for (Studiengang sg : studiengaenge) {
							libstudiengang.addItem(
									sg.getBezeichnung(),
									String.valueOf(sg.getId()));
						}
					}
				});

		speichern.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				boolean allFilled = true;

				if (tbjahrgang.getText().isEmpty()
						|| tbanzahl.getText().isEmpty()
						|| tbsemester.getText().isEmpty()) {
					allFilled = false;
					Window.alert("Bitte füllen Sie alle Felder aus.");
				}

				if (allFilled == true) {
					final String jahrgang = tbjahrgang.getText()
							.trim();
					final int studiengangId = sgContainer.elementAt(
							libstudiengang.getSelectedIndex())
							.getId();
					final int studierendenAnzahl = Integer
							.valueOf(tbanzahl.getValue());
					final int semester = Integer.valueOf(tbsemester
							.getText().trim());

					verwaltungsSvc.createSemesterverband(
							studiengangId, semester,
							studierendenAnzahl, jahrgang,
							new AsyncCallback<Semesterverband>() {

								@Override
								public void onFailure(Throwable caught) {
									Window.alert(caught.getMessage());
								}

								@Override
								public void onSuccess(
										Semesterverband result) {
									tbjahrgang.setText("");
									libstudiengang.clear();
									tbsemester.setText("");
									libstudiengang.clear();
									Window.alert("Erfolgreich gespeichert.");
									tvm.addSemesterverband(result);
								}
							});
				}
			}
		});
	}

	public void setTvm(NavTreeViewModel tvm) {
		this.tvm = tvm;
	}
}
