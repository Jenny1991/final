package de.hdm.stundenplansystem.client;

import java.util.Vector;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;

import de.hdm.stundenplansystem.shared.ReportGenerator;
import de.hdm.stundenplansystem.shared.ReportGeneratorAsync;
import de.hdm.stundenplansystem.shared.Verwaltungsklasse;
import de.hdm.stundenplansystem.shared.VerwaltungsklasseAsync;
import de.hdm.stundenplansystem.shared.bo.Dozent;
import de.hdm.stundenplansystem.shared.report.HTMLReportWriter;
import de.hdm.stundenplansystem.shared.report.StundenplanDozentReport;

public class ReportStundenplanDozent extends Content {

	HTML ueberschrift = new HTML("<h2>Stundenplan für Dozenten</h2>");

	final ListBox libDozent = new ListBox();
	final Button anzeigen = new Button("Stundenplan anzeigen");
	final ScrollPanel panel = new ScrollPanel();
	HTML feld = new HTML();
	
	final VerwaltungsklasseAsync verwaltungsSvc = GWT
			.create(Verwaltungsklasse.class);
	final ReportGeneratorAsync reportSvc = GWT
			.create(ReportGenerator.class);

	Vector<Dozent> dContainer = null;
	NavTreeViewModel tvm;
	String test;

	public void onLoad() {
		
		this.add(ueberschrift);
		this.add(libDozent);
		this.add(anzeigen);
		this.add(panel);
		setTvm(tvm);

		libDozent.clear();
		verwaltungsSvc
				.getAllDozenten(new AsyncCallback<Vector<Dozent>>() {
					public void onFailure(Throwable T) {
					}

					public void onSuccess(Vector<Dozent> dozent) {
						dContainer = dozent;
						for (Dozent d : dozent) {
							libDozent.addItem(d.getNachname() + ", "
									+ d.getVorname(),
									String.valueOf(d.getId()));
						}
					}
				});

		anzeigen.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				reportSvc.createStundenplanDozentReport(dContainer
						.elementAt(libDozent.getSelectedIndex())
						.getId(),
						new AsyncCallback<StundenplanDozentReport>() {

							public void onSuccess(
									StundenplanDozentReport result) {

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
}
