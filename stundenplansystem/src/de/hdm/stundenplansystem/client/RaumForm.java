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
 * Formular fÃ¼r die Darstellung des selektierten Raums
 * 
 * @author Thies, Espich
 *
 */

public class RaumForm extends Content {
	
	private final HTML ueberschriftAenderung = new HTML ("<h2>Raum verwalten<h2>");

	  final TextBox tbbezeichnung = new TextBox ();
	  final TextBox tbkapazitaet = new TextBox ();
	  final Button speichern = new Button ("Änderungen speichern");
	  final Button loeschen = new Button ("Raum löschen");	  			  
	  final VerwaltungsklasseAsync verwaltungsSvc = GWT.create(Verwaltungsklasse.class);
	  
	  Integer id;
	  Raum shownRaum = null; 
	  NavTreeViewModel tvm = null;
	  
	  public RaumForm() {
		  Grid raumGrid = new Grid (4, 2);
		    this.add(ueberschriftAenderung);
			this.add(raumGrid);
		  
			Label lbbezeichnung = new Label("Bezeichnung");
			raumGrid.setWidget(0, 0, lbbezeichnung);
			raumGrid.setWidget(0, 1, tbbezeichnung);

			Label lbkapazitaet = new Label("Kapazität");
			raumGrid.setWidget(1, 0, lbkapazitaet);
			raumGrid.setWidget(1, 1, tbkapazitaet);
			
			Label lbfunktionen = new Label ("Funktionen");
			raumGrid.setWidget(2, 0, lbfunktionen);
			raumGrid.setWidget(2, 1, speichern);
			speichern.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					changeSelectedRaum();
				}
			});
			raumGrid.setWidget(3, 1, loeschen);
			loeschen.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					deleteSelectedRaum();
				}
			});
			setTvm(tvm);
	  } 
	  
		public void getData() {
			verwaltungsSvc.getRaumById(id, new AsyncCallback<Raum>(){
				@Override
				public void onFailure(Throwable caught) {
				}
				
				@Override
				public void onSuccess(Raum r) {
					if (r != null) {
						setSelected(r);
					}
				}
			});
		}

		public void setTvm(NavTreeViewModel tvm) {
			this.tvm = tvm;
		}
		
		public void changeSelectedRaum() {
			
			boolean allFilled = true;
			  
			  if (tbbezeichnung.getValue().isEmpty() 
					  || tbkapazitaet.getValue().isEmpty())
			  {	allFilled = false;
			  Window.alert ("Bitte füllen Sie alle Felder aus."); }
			  
			  if (allFilled == true) { 
					  shownRaum.setBezeichnung(tbbezeichnung.getText().trim());
					  shownRaum.setKapazitaet(Integer.valueOf(tbkapazitaet.getText()));

					  verwaltungsSvc.changeRaum(shownRaum, new  AsyncCallback<Void>() {

						  @Override
						  public void onFailure (Throwable caught) {
							  Window.alert("Der Raum konnte nicht bearbeitet werden.");
						  }

						  @Override
						  public void onSuccess(Void result) {
							  tvm.updateRaum(shownRaum);
							  Window.alert ("Erfolgreich gespeichert.");
						  } 	
						});
				  }
		  }
		
		public void deleteSelectedRaum() {
			verwaltungsSvc.deleteRaum(shownRaum, new AsyncCallback<Boolean>() {
				  @Override
				  public void onFailure (Throwable caught) {
					  Window.alert("Der Raum konnte nicht gelöscht werden." +
					  		"Er ist in ein oder mehreren Stundenplaneinträgen vorhanden");
				  }

				  @Override
				  public void onSuccess(Boolean result) {
					  tvm.deleteRaum(shownRaum);
					  Window.alert ("Erfolgreich gelöscht.");
				  } 	
				});
		this.clearFields();
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
			tbkapazitaet.setText(Integer.toString(shownRaum.getKapazitaet()));
		}
		
		public void clearFields(){
			tbbezeichnung.setText("");
			tbkapazitaet.setText("");
		}
}


