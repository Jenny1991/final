package de.hdm.stundenplansystem.client;

import java.util.ArrayList;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.stundenplansystem.shared.VerwaltungsklasseAsync;
import de.hdm.stundenplansystem.shared.bo.Dozent;
import de.hdm.stundenplansystem.shared.Verwaltungsklasse;
import de.hdm.stundenplansystem.client.*;



public class StundenplaneintragForm extends Content {

		/**
		 * Aufbau der Seite, um Stundenplaneinträge anzuzeigen, zu lÃ¶schen und zu bearbeiten
		 */
	
	private final HTML ueberschrift = new HTML ("<h2>Ãœbersicht der Stundenplaneintr�ge<h2>");
	private final HTML ueberschriftAenderung = new HTML ("<h2>Stundenplaneintrag bearbeiten<h2>");
	
		final TextBox tbZeitslot = new TextBox();
		final TextBox tbDozent = new TextBox();
		final TextBox tbLehrveranstaltung = new TextBox();
		final TextBox tbRaum = new TextBox();
		final TextBox tbStudiengang = new TextBox();
		final TextBox tbSemesterverband = new TextBox();
		final Button speichern = new Button ("Änderungen speichern");
		final Button bearbeiten = new Button("Stundenplaneintrag bearbeiten");
		final Button loeschen = new Button("Stundenplaneintrag lÃ¶schen");
				
		final VerwaltungsklasseAsync verwaltungsSvc = GWT.create(Verwaltungsklasse.class);
		Dozent shownSpe = null; 
		NavTreeViewModel tvm = null;
		  
		  public StundenplaneintragForm() {
			  Grid speGrid = new Grid (8, 2);
			    this.add(ueberschrift);
				this.add(speGrid);
			  
				Label lbZeitslot = new Label("Zeitslot");
				speGrid.setWidget(0, 0, lbZeitslot);
				speGrid.setWidget(0, 1, tbZeitslot);

				Label lbDozent = new Label("Dozent");
				speGrid.setWidget(1, 0, lbDozent);
				speGrid.setWidget(1, 1, tbDozent);
				
				Label lbLehrveranstaltung = new Label ("Lehrveranstaltung");
				speGrid.setWidget(2, 0, lbLehrveranstaltung);
				speGrid.setWidget(2, 1, tbLehrveranstaltung);
				
				Label lbRaum = new Label ("Raum");
				speGrid.setWidget(3, 0, lbRaum);
				speGrid.setWidget(3, 1, tbRaum);
				
				Label lbStudiengang = new Label ("Studiengang");
				speGrid.setWidget(4, 0, lbStudiengang);
				speGrid.setWidget(4, 1, tbStudiengang);
				
				Label lbSemesterverband = new Label ("Semesterverband");
				speGrid.setWidget(5, 0, lbSemesterverband);
				speGrid.setWidget(5, 1, tbSemesterverband);
				
				Label lbFunktionen = new Label ("Funktionen");
				speGrid.setWidget(6, 0, lbFunktionen);
				speGrid.setWidget(6, 1, bearbeiten);
				speGrid.setWidget(7, 1, loeschen);
				}
		  
			public void onLoad() {
			
			/**createSpeButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
				this.add(createSpe);
				}
			});
			
			changeSpeButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					this.add(changeSpe);
				}
			});*/
			
		}
			
		public void showWidget() {
		
		}
		
		
		/**public Stundenplaneintrag updateFlexTable (Stundenplaneintrag result) {
			for (int i = 0; i < getAllStundenplaneintrag.size(); i++) { //getAllDozent wird noch als Methode oder Klasse benÃ¶tigt
				tabelleSpe.addItem(getAllStundenplaneintrag.get(i).getVorname());
				
			}
		}
	*/

}




