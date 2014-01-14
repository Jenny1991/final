package de.hdm.stundenplansystem.client;

import java.util.Vector;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import de.hdm.stundenplansystem.shared.*;
import de.hdm.stundenplansystem.shared.bo.Zeitslot;

/**
 * Formular für die Darstellung des selektierten Kunden
 * 
 * @author Thies, Espich
 *
 */

public class ZeitslotForm extends Content {
	
	private final HTML ueberschrift = new HTML ("<h2>Ãœbersicht der Zeitslots<h2>");

	  final Label lbwochentag = new Label ("Wochentag"); 
	  final Label lbuhrzeit = new Label ("Uhrzeit");
	  final TextBox tbwochentag = new TextBox ();
	  final TextBox tbuhrzeit = new TextBox ();
	  			  
	  final VerwaltungsklasseAsync verwaltungsSvc = GWT.create(Verwaltungsklasse.class);
	  Zeitslot shownZeitslot = null; 
	 // NavTreeViewModel tvm = null;
	  
	  public ZeitslotForm() {
		  Grid zeitGrid = new Grid (6, 8);
		  	this.add(ueberschrift);
			this.add(zeitGrid);
		  
			Label lbmontag = new Label("Montag");
			zeitGrid.setWidget(0, 0, lbmontag);
			zeitGrid.setWidget(0, 1, tbuhrzeit);
			zeitGrid.setWidget(0, 2, tbuhrzeit);
			zeitGrid.setWidget(0, 3, tbuhrzeit);
			zeitGrid.setWidget(0, 4, tbuhrzeit);
			zeitGrid.setWidget(0, 5, tbuhrzeit);
			zeitGrid.setWidget(0, 6, tbuhrzeit);
			zeitGrid.setWidget(0, 7, tbuhrzeit);
			
			Label lbdienstag = new Label("Dienstag");
			zeitGrid.setWidget(1, 0, lbdienstag);
			zeitGrid.setWidget(0, 1, tbuhrzeit);
			
			Label lbmittwoch = new Label("Mittwoch");
			zeitGrid.setWidget(2, 0, lbmittwoch);
			zeitGrid.setWidget(0, 1, tbuhrzeit);
			
			Label lbdonnerstag = new Label("Donnerstag");
			zeitGrid.setWidget(3, 0, lbdonnerstag);
			zeitGrid.setWidget(0, 1, tbuhrzeit);
			
			Label lbfreitag = new Label("Freitag");
			zeitGrid.setWidget(4, 0, lbfreitag);
			zeitGrid.setWidget(0, 1, tbuhrzeit);
			
			Label lbsamstag = new Label("Samstag");
			zeitGrid.setWidget(5, 0, lbsamstag);
			zeitGrid.setWidget(0, 1, tbuhrzeit);			
			}

	  	public void getSelectedData(){
				verwaltungsSvc.getAllZeitslots(new AsyncCallback<Vector<Zeitslot>>(){
					@Override
					public void onFailure(Throwable caught) {
					}
					
					@Override
					public void onSuccess(Vector<Zeitslot> result) {
						if (result != null) {
							// setSelected(result);
						}
					}
				});
			}
}
			
			
	