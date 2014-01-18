package de.hdm.stundenplansystem.client;

import com.google.gwt.core.client.GWT;
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
import de.hdm.stundenplansystem.shared.bo.Dozent;
import de.hdm.stundenplansystem.client.NavTreeViewModel;

/**
 * Formular fï¿½ï¿½r die Darstellung des selektierten Kunden
 * 
 * @author Thies, Espich
 *
 */

public class DozentForm extends Content {
	
	private final HTML ueberschriftAenderung = new HTML ("<h2>Dozenten verwalten<h2>");

	  final TextBox tbvorname = new TextBox ();
	  final TextBox tbnachname = new TextBox ();
	  final Button loeschen = new Button ("Dozent löschen");
	  final Button speichern = new Button ("Änderungen speichern");
	  final VerwaltungsklasseAsync verwaltungsSvc = GWT.create(Verwaltungsklasse.class);
      public CreateDozent cd = new CreateDozent();

	  Integer id;
	  Dozent shownDozent = null; 
	  NavTreeViewModel tvm = null;
	  
	  public DozentForm() {
		  Grid dozentGrid = new Grid (2, 4);
		    this.add(ueberschriftAenderung);
			this.add(dozentGrid);
		  
			Label lbvorname = new Label("Vorname");
			dozentGrid.setWidget(0, 0, lbvorname);
			dozentGrid.setWidget(1, 0, tbvorname);

			Label lbnachname = new Label("Nachname");
			dozentGrid.setWidget(0, 1, lbnachname);
			dozentGrid.setWidget(1, 1, tbnachname);
			
			Label lbfunktionen = new Label ("Funktionen");
			dozentGrid.setWidget(0, 2, lbfunktionen);
			dozentGrid.setWidget(1, 2, speichern);
			speichern.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					changeSelectedDozent();
				}
			});
			dozentGrid.setWidget(1, 3, loeschen);
			loeschen.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					deleteSelectedDozent();
				}
			});
			setTvm(tvm);
	  } 
	  
	  
		public void onLoad() {

				verwaltungsSvc.getDozentById(id, new AsyncCallback<Dozent>(){
					@Override
					public void onFailure(Throwable caught) {
					}
					
					@Override
					public void onSuccess(Dozent d) {
						if (d != null) {
							setSelected(d);
						}
					};		
				});
			}

		public void setTvm(NavTreeViewModel tvm) {
			this.tvm = tvm;
		}
		
		public void changeSelectedDozent(){
			  
			  boolean allFilled = true;
			  
			  if (tbnachname.getValue().isEmpty() 
					  ||tbvorname.getValue().isEmpty()) {	
				  allFilled = false;
			  Window.alert ("Bitte füllen Sie alle Felder aus."); } 
			  
			  if (allFilled == true) {
				  shownDozent.setNachname(tbnachname.getText().trim());
				  shownDozent.setVorname(tbvorname.getText().trim());		
				  
				  verwaltungsSvc.changeDozent(shownDozent, new  AsyncCallback<Dozent> () {

					  @Override
					  public void onFailure (Throwable caught) {
						  Window.alert("Der Dozent konnte nicht bearbeitet werden.");
					  }

					  @Override
					  public void onSuccess(Dozent result) {
						  tvm.updateDozent(shownDozent);
						  Window.alert ("Erfolgreich gespeichert.");
						  
					  } 	
					});
			  }
		  }			
		
		public void deleteSelectedDozent(){
			verwaltungsSvc.deleteDozent(shownDozent, new AsyncCallback<Boolean>() {
			  public void onFailure (Throwable caught) {
				  Window.alert("Der Dozent konnte nicht gelöscht werden." +
				  		"Er ist in ein oder mehreren Stundenplaneinträgen eingetragen");
			  }

			  public void onSuccess(Boolean result) {
				  tvm.deleteDozent(shownDozent);
				  Window.alert ("Erfolgreich gelöscht.");
			  } 	
			});
			this.clearFields();
	  }
	
		public void setSelected(Dozent d){
			if (d != null) {
				shownDozent = d;
				setFields();
			} else {
				clearFields();
			}
		} 
		
		public void setFields(){
			tbvorname.setText(shownDozent.getVorname());
			tbnachname.setText(shownDozent.getNachname());
		}
		
		public void clearFields(){
			tbvorname.setText("");
			tbnachname.setText("");
		}
}