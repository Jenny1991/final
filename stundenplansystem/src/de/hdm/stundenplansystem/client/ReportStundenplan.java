package de.hdm.stundenplansystem.client;

import java.util.Vector;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;

import de.hdm.stundenplansystem.client.Content;
import de.hdm.stundenplansystem.shared.*;
import de.hdm.stundenplansystem.shared.bo.Studiengang;
import de.hdm.stundenplansystem.shared.bo.Stundenplan;
import de.hdm.stundenplansystem.shared.bo.Semesterverband;
import de.hdm.stundenplansystem.shared.report.*;

public class ReportStundenplan extends Content {

	/**
	 * Aufbau der Seite, um den Stundenplan für Studenten anzuzeigen
	 */

	HTML ueberschrift = new HTML("<h2>Stundenplan für Studenten</h2>");
	final Label lbStundenplan = new Label("Stundenplan:");
	final Label lbSemverband = new Label("Semesterverband:");
	final Label lbStudiengang = new Label("Studiengang:");
	final ListBox libStundenplan = new ListBox();
	final ListBox libSemverband = new ListBox();
	final ListBox libStudiengang = new ListBox();
	final Button anzeigen = new Button("Stundenplan anzeigen");
	final ScrollPanel panel = new ScrollPanel();
	HTML feld = new HTML();

	final VerwaltungsklasseAsync verwaltungsSvc = GWT
			.create(Verwaltungsklasse.class);
	final ReportGeneratorAsync reportSvc = GWT
			.create(ReportGenerator.class);
	
	Vector<Semesterverband> svContainer = null;
	Vector<Stundenplan> spContainer = null;
	Vector<Studiengang> sgContainer = null;
	NavTreeViewModel tvm;
	Studiengang sg;
	Integer sv;
	String test;

	public void onLoad() {

		this.add(ueberschrift);
		this.add(lbStudiengang);
		this.add(libStudiengang);
		this.add(lbSemverband);
		this.add(libSemverband);
		this.add(lbStundenplan);
		this.add(libStundenplan);
		this.add(anzeigen);
		this.add(panel);
		setTvm(tvm);

		libStudiengang.clear();
		verwaltungsSvc
				.getAllStudiengaenge(new AsyncCallback<Vector<Studiengang>>() {
					public void onFailure(Throwable T) {

					}

					public void onSuccess(
							Vector<Studiengang> studiengang) {
						sgContainer = studiengang;
						for (Studiengang sg : studiengang) {
							libStudiengang.addItem(
									sg.getBezeichnung(),
									String.valueOf(sg.getId()));
						}
						getSemverband();
					}
				});

		libStudiengang.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				getSemverband();
			}
		});

		libSemverband.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				getStundenplan();
			}
		});

		anzeigen.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				reportSvc.createStundenplanSemesterverbandReport(
						svContainer.elementAt(
								libSemverband.getSelectedIndex())
								.getId(),
						spContainer.elementAt(
								libStundenplan.getSelectedIndex())
								.getId(),
						new AsyncCallback<StundenplanSemesterverbandReport>() {

							public void onSuccess(
									StundenplanSemesterverbandReport result) {

								HTMLReportWriter writer = new HTMLReportWriter();
								writer.process(result);
								test = writer.getReportText();
								panel.add(new HTML(test));
							}

							@Override
							public void onFailure(Throwable caught) {
								caught.getMessage();
							}
						});
			}
		});
	}

	public void setTvm(NavTreeViewModel tvm) {
		this.tvm = tvm;
	}

	public void getSemverband() {
		libSemverband.clear();
		verwaltungsSvc.getSemsterverbaendeByStudiengang(
				sgContainer.elementAt(
						libStudiengang.getSelectedIndex()).getId(),
				new AsyncCallback<Vector<Semesterverband>>() {
					public void onFailure(Throwable T) {

					}

					public void onSuccess(
							Vector<Semesterverband> semesterverband) {
						svContainer = semesterverband;
						for (Semesterverband sv : semesterverband) {
							libSemverband.addItem(sv.getKuerzel()
									+ ", Semester: "
									+ String.valueOf(sv.getSemester()));
						}
						getStundenplan();
					}
				});
	}

	public void getStundenplan() {
		libStundenplan.clear();
		verwaltungsSvc.getStundenplaeneBySemesterverband(svContainer
				.elementAt(libSemverband.getSelectedIndex()).getId(),
				new AsyncCallback<Vector<Stundenplan>>() {
					public void onFailure(Throwable T) {

					}

					public void onSuccess(
							Vector<Stundenplan> stundenplaene) {
						spContainer = stundenplaene;
						for (Stundenplan sp : stundenplaene) {
							libStundenplan.addItem(
									sp.getStudienhalbjahr(),
									String.valueOf(sp.getId()));
						}
					}
				});
	}
}
