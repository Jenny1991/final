package de.hdm.stundenplansystem.client;

import java.util.ArrayList;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.stundenplansystem.shared.VerwaltungsklasseAsync;
import de.hdm.stundenplansystem.shared.bo.Dozent;
import de.hdm.stundenplansystem.shared.bo.Stundenplaneintrag;
import de.hdm.stundenplansystem.shared.Verwaltungsklasse;
import de.hdm.stundenplansystem.client.*;



public class StundenplaneintragForm extends Content {

		/**
		 * Aufbau der Seite, um Stundenplaneintr��ge anzuzeigen, zu l����schen und zu bearbeiten
		 */
	
	private final HTML ueberschriftAenderung = new HTML ("<h2>Stundenplaneintrag bearbeiten<h2>");
	
		final ListBox libzeitslot = new ListBox();
		final ListBox libdozent = new ListBox();
		final ListBox liblehrveranstaltung = new ListBox();
		final ListBox libraum = new ListBox();
		final ListBox libstudiengang = new ListBox();
		final ListBox libsemesterverband = new ListBox();
		final Button speichern = new Button ("Änderungen speichern");
		final Button loeschen = new Button("Stundenplaneintrag löschen");
				
		final VerwaltungsklasseAsync verwaltungsSvc = GWT.create(Verwaltungsklasse.class);
		Stundenplaneintrag shownSpe = null; 
		NavTreeViewModel tvm = null;
		  
		  public StundenplaneintragForm() {
			  Grid speGrid = new Grid (8, 2);
			    this.add(ueberschriftAenderung);
				this.add(speGrid);
			  
				Label lbZeitslot = new Label("Zeitslot");
				speGrid.setWidget(0, 0, lbZeitslot);
				speGrid.setWidget(0, 1, libzeitslot);

				Label lbDozent = new Label("Dozent");
				speGrid.setWidget(1, 0, lbDozent);
				speGrid.setWidget(1, 1, libdozent);
				
				Label lbLehrveranstaltung = new Label ("Lehrveranstaltung");
				speGrid.setWidget(2, 0, lbLehrveranstaltung);
				speGrid.setWidget(2, 1, liblehrveranstaltung);
				
				Label lbRaum = new Label ("Raum");
				speGrid.setWidget(3, 0, lbRaum);
				speGrid.setWidget(3, 1, libraum);
				
				Label lbStudiengang = new Label ("Studiengang");
				speGrid.setWidget(4, 0, lbStudiengang);
				speGrid.setWidget(4, 1, libstudiengang);
				
				Label lbSemesterverband = new Label ("Semesterverband");
				speGrid.setWidget(5, 0, lbSemesterverband);
				speGrid.setWidget(5, 1, libsemesterverband);
				
				Label lbFunktionen = new Label ("Funktionen");
				speGrid.setWidget(6, 0, lbFunktionen);
				speGrid.setWidget(6, 1, speichern);
				speGrid.setWidget(7, 1, loeschen);
				}
		  
			public void onLoad() {
				
				setTvm(tvm);
			
			/*speichern.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
				this.add(createSpe);
				}
			});
			
			bearbeiten.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					this.add(changeSpe);
				}
			});*/
			
			loeschen.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					deleteSelectedSpe();
				}
			});
			
		}
			
			
			public void deleteSelectedSpe(){
				verwaltungsSvc.deleteStundenplaneintrag(shownSpe, new AsyncCallback<Boolean>() {
				  public void onFailure (Throwable caught) {
					  Window.alert("Der Dozent konnte nicht gelöscht werden." +
					  		"Er ist in ein oder mehreren Stundenplaneinträgen eingetragen");
				  }

				  public void onSuccess(Boolean result) {
					  tvm.deleteSpe(shownSpe);
					  Window.alert ("Erfolgreich gelöscht.");
				  } 	
				});
				this.clearFields();
		  }
			
		public void showWidget() {
		
		}
		
		public void setTvm(NavTreeViewModel tvm) {
			this.tvm = tvm;
		}
		
		public void setSelected(Stundenplaneintrag spe){
			if (spe != null) {
				shownSpe = spe;
				setFields();
			} else {
				clearFields();
			}
		} 
		
		public void setFields(){
			//tbZeitslot.setText(shownZeitslot.getVorname());
			//tbDozent.setText(shownDozent.getNachname() + shownDozent.);
		}
		
		public void clearFields(){
			//tbvorname.setText("");
			//tbnachname.setText("");
		}
		
		
		/**public Stundenplaneintrag updateFlexTable (Stundenplaneintrag result) {
			for (int i = 0; i < getAllStundenplaneintrag.size(); i++) { //getAllDozent wird noch als Methode oder Klasse ben����tigt
				tabelleSpe.addItem(getAllStundenplaneintrag.get(i).getVorname());
				
			}
		}
	*/

}




