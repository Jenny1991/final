package de.hdm.stundenplansystem.client;

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
import de.hdm.stundenplansystem.shared.bo.Raum;
import de.hdm.stundenplansystem.client.NavTreeViewModel;

/**
 * Formular für die Darstellung des selektierten Raums
 * 
 * @author Thies, Espich
 *
 */

public class RaumForm extends Content {
	
	private final HTML ueberschrift = new HTML ("<h2>Ãœbersicht der Räume<h2>");
	private final HTML ueberschriftAenderung = new HTML ("<h2>Raum bearbeiten<h2>");

	  final Label lbbezeichnung = new Label ("Bezeichnung"); 
	  final Label lbkapazitaet = new Label ("Kapazität");
	  final TextBox tbbezeichnung = new TextBox ();
	  final TextBox tbkapazitaet = new TextBox ();
	  final Button speichern = new Button ("Änderungen speichern");
	  final Button loeschen = new Button ("Raum löschen");
	  final Button bearbeiten = new Button ("Raum speichern");
	  			  
	  final VerwaltungsklasseAsync verwaltungsSvc = GWT.create(Verwaltungsklasse.class);
	  Raum shownRaum = null; 
	  NavTreeViewModel tvm = null;
	  
	  public RaumForm() {
		  Grid raumGrid = new Grid (4, 2);
		    this.add(ueberschrift);
			this.add(raumGrid);
		  
			Label lbbezeichnung = new Label("Vorname");
			raumGrid.setWidget(0, 0, lbbezeichnung);
			raumGrid.setWidget(0, 1, tbbezeichnung);

			Label lbkapazitaet = new Label("Nachname");
			raumGrid.setWidget(1, 0, lbkapazitaet);
			raumGrid.setWidget(1, 1, tbkapazitaet);
			
			Label lbfunktionen = new Label ("Funktionen");
			raumGrid.setWidget(2, 0, bearbeiten);
			raumGrid.setWidget(3, 0, loeschen);
			}
	  
		public void onLoad() {
			
			setTvm(tvm);
			getSelectedData();
			
			bearbeiten.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					showWidget();
				}
			});
			
			speichern.addClickHandler(new ClickHandler(){
				  public void onClick(ClickEvent event) {			

					  boolean allFilled = true;
						  
						  if (tbbezeichnung.getText().isEmpty());
						  if (tbkapazitaet.getText().isEmpty());
						  {	allFilled = false;
						  Window.alert ("Bitte fÃ¼llen Sie alle Felder aus."); }
						  
						  if (allFilled == true) { 
							  Raum r = new Raum();
							  r.setBezeichnung(tbbezeichnung.getText().trim());
							  r.setKapazitaet(tbkapazitaet.getVisibleLength());

							  verwaltungsSvc.changeRaum(r, new  AsyncCallback<Raum>() {

								  @Override
								  public void onFailure (Throwable caught) {
									  Window.alert("Der Raum konnte nicht bearbeitet werden.");
								  }

								  @Override
								  public void onSuccess(Raum result) {
									  Window.alert ("Erfolgreich gespeichert.");
									  tbbezeichnung.setText("");
									  tbkapazitaet.setVisibleLength(result.getKapazitaet());
									  tvm.updateRaum(shownRaum);
								  } 	
								});
						  }
				  }
				  }); 
			
			loeschen.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event){
					verwaltungsSvc.deleteRaum(shownRaum, new AsyncCallback<Void>() {
						  @Override
						  public void onFailure (Throwable caught) {
							  Window.alert("Der Raum konnte nicht gelöscht werden." +
							  		"Er ist in ein oder mehreren Stundenplaneinträgen eingetragen");
						  }

						  @Override
						  public void onSuccess(Void result) {
							  tvm.deleteRaum(shownRaum);
							  Window.alert ("Erfolgreich gelöscht.");
						  } 	
						});
				  }
			});
	  		this.clear();
		  }

		public void setTvm(NavTreeViewModel tvm) {
			this.tvm = tvm;
		}
		
		public void getSelectedData(){
			/*verwaltungsSvc.getRaumById(rId, new AsyncCallback<Raum>(){
				@Override
				public void onFailure(Throwable caught) {
				}
				
				@Override
				public void onSuccess(Raum result) {
					if (result != null) {
						setSelected(result);
					}
				}
			});*/
		}
		
		public void setSelected(Raum r){
			if (r != null) {
				shownRaum = r;
				setFields();
			} else {
				clearFields();
			}
		}
		
		public void setFields(){
			tbbezeichnung.setText(shownRaum.getBezeichnung());
			tbkapazitaet.setVisibleLength(shownRaum.getKapazitaet());
		}
		
		public void clearFields(){
			tbbezeichnung.setText("");
			tbkapazitaet.setText("");
		}
		
		  public void showWidget(){
			  	 this.add(ueberschriftAenderung);
				  this.add(lbbezeichnung);
				  this.add(tbbezeichnung);
				  this.add(lbkapazitaet);
				  this.add(tbkapazitaet);
				  this.add(speichern);
			  }
}


