package de.hdm.stundenplansystem.client;

/**
 * @author Espich
 * 
 */

import java.util.Vector;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.stundenplansystem.shared.*;
import de.hdm.stundenplansystem.shared.bo.Dozent;
import de.hdm.stundenplansystem.shared.bo.Raum;
import de.hdm.stundenplansystem.shared.bo.Stundenplaneintrag;
import de.hdm.stundenplansystem.shared.report.HTMLReportWriter;
import de.hdm.stundenplansystem.shared.report.PlainTextReportWriter;
import de.hdm.stundenplansystem.shared.report.RaumbelegungsReport;
import de.hdm.stundenplansystem.client.*;

public class ReportRaum extends Content {

	final HTML ueberschrift = new HTML("<h2>Raumbelegungsplan</h2>");

	final ListBox libRaum = new ListBox();
	final VerwaltungsklasseAsync verwaltungsSvc = GWT
			.create(Verwaltungsklasse.class);
	final ReportGeneratorAsync reportSvc = GWT
			.create(ReportGenerator.class);
	final Button anzeigen = new Button("Raumbelegungen anzeigen");
	final VerticalPanel neuesPanel = new VerticalPanel();
	HTML feld = new HTML();

	Vector<Raum> rContainer = null;
	private NavTreeViewModel tvm;
	String test;

	public void onLoad() {
		this.add(ueberschrift);
		this.add(libRaum);
		this.add(anzeigen);
		setTvm(tvm);

		libRaum.clear();
		verwaltungsSvc
				.getAllRaeume(new AsyncCallback<Vector<Raum>>() {
					public void onFailure(Throwable T) {
					}

					public void onSuccess(Vector<Raum> raum) {
						rContainer = raum;
						for (Raum r : raum) {
							libRaum.addItem(r.getBezeichnung(),
									String.valueOf(r.getId()));
						}
					}
				});

		anzeigen.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				reportSvc.createRaumbelungsReport(rContainer
						.elementAt(libRaum.getSelectedIndex())
						.getId(),
						new AsyncCallback<RaumbelegungsReport>() {

							public void onSuccess(
									RaumbelegungsReport result) {

								HTMLReportWriter writer = new HTMLReportWriter();
								writer.process(result);
								test = writer.getReportText();
								neuesPanel.add(new HTML(test));
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
