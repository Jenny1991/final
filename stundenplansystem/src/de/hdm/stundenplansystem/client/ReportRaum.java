package de.hdm.stundenplansystem.client;

/**
 * @author V. Hofmann
 *
 */


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

public class ReportRaum extends Content {


		/**
		 * Aufbau der Seite, um den Raumplan anzuzeigen
		 */
		
		//final Label flexTable = new Label();
		//private VerticalPanel detailsPanel = new VerticalPanel();
		
		//final TextBox nachnameTextBox = new TextBox();
		//final TextBox vornameTextBox = new TextBox();
		final FlexTable tabelleRp = new FlexTable();
	//	final Button createRpButton = new Button ("Raumplan anlegen");
	//	final Button changeRpButton = new Button("Raumplan bearbeiten");
		//final Button deleteRpButton = new Button("Raumplan lÃ¶schen");
		
		//final CreateRaum createRaum = new CreateRaum();
		//final ChangeRaum changeRaum = new ChangeRaum();
		//final DeleteRaum deleteRaum = new DeleteRaum();
		
		// final VerwaltungsklasseAsync verwaltungsSvc = GWT.create(Verwaltungsklasse.class);
		 
		
		//final Label valueLabel = new Label();
		


		
		public void onLoad() {
			
			showWidget();
		
			
		//int row = tabelleDozent.getRowCount();
			
			
			tabelleRp.setText(0, 0, "Bezeichnung");
			tabelleRp.setCellPadding(10);
			tabelleRp.setText(0, 1, "KapazitÃ¤t");
			//tabelleRp.setText(0, 3, "Funktionen");
		//	tabelleRp.setWidget(1, 4, deleteRpButton);
			//tabelleRp.setWidget(1, 5, changeRpButton);
			
			/**tabelleRaum.setText(1, 0, "Thies");
			tabelleRaum.setText(1, 1, "Peter");
			tabelleRaum.setText(2, 0, "Rathke");
			tabelleRaum.setText(2, 1, "Christian");
			
			createRpButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
				this.add(createRp);
				}
			});
			
			changeRpButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					this.add(changeRp);
				}
			});*/
			
		}
			
		public void showWidget() {
			
			this.add(tabelleRp);
		//	this.add(createRpButton);
		//	this.add(changeRpButton);
		//	this.add(deleteRpButton);
			//this.add(changeRp);
			//this.add(createRp);
			//this.add(deleteRp);
		}
		
		
		/**public Raumplan updateFlexTable (Raumplan result) {
			for (int i = 0; i < getRaumplan.size(); i++) { //getAllDozent wird noch als Methode oder Klasse benÃ¶tigt
				tabelleRaum.addItem(getRaumplan.get(i).getVorname());
				
			}
		}
	*/

}
