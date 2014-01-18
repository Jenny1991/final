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
import de.hdm.stundenplansystem.shared.bo.Studiengang;
import de.hdm.stundenplansystem.client.NavTreeViewModel;


/**
 * Formular fÃ¼r die Darstellung des selektierten Studiengangs
 * 
 * @author Thies, Espich
 *
 */

public class StudiengangForm extends Content {
	
	private final HTML ueberschriftAenderung = new HTML ("<h2>Studiengang verwalten<h2>");

	  final TextBox tbbezeichnung = new TextBox ();
	  final Button loeschen = new Button ("Studiengang löschen");
	  final Button speichern = new Button ("Änderungen speichern");	  			  
	  final VerwaltungsklasseAsync verwaltungsSvc = GWT.create(Verwaltungsklasse.class);
	  
	  Integer id;
	  Studiengang shownSg = null; 
	  NavTreeViewModel tvm = null;
	  
	  public StudiengangForm() {
		  Grid studiengangGrid = new Grid (2, 3);
		    this.add(ueberschriftAenderung);
			this.add(studiengangGrid);
		  
			Label lbbezeichnung = new Label("Bezeichnung");
			studiengangGrid.setWidget(0, 0, lbbezeichnung);
			studiengangGrid.setWidget(1, 0, tbbezeichnung);
			
			Label lbfunktionen = new Label ("Funktionen");
			studiengangGrid.setWidget(0, 1, lbfunktionen);
			studiengangGrid.setWidget(1, 1, speichern);
			speichern.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					changeSelectedSg();
				}
			});
			studiengangGrid.setWidget(1, 2, loeschen);
			loeschen.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					deleteSelectedSg();
				}
			});
			setTvm(tvm);
	  } 
	  
		public void getData() {
			verwaltungsSvc.getStudiengangById(id, new AsyncCallback<Studiengang>(){
				@Override
				public void onFailure(Throwable caught) {
				}
				
				@Override
				public void onSuccess(Studiengang sg) {
					if (sg != null) {
						setSelected(sg);
					}
				}
			});
		}

		public void setTvm(NavTreeViewModel tvm) {
			this.tvm = tvm;
		}
		
		public void changeSelectedSg(){

			boolean allFilled = true;
					  
					  if (tbbezeichnung.getValue().isEmpty()) {
						  allFilled = false;
					  Window.alert ("Bitte füllen Sie alle Felder aus."); }
					  
					  if (allFilled == true) {
						  shownSg.setBezeichnung(tbbezeichnung.getText().trim());
					  
						  verwaltungsSvc.changeStudiengang(shownSg, new AsyncCallback<Studiengang> () {

							  @Override
							  public void onFailure (Throwable caught) {
								  Window.alert("Der Studiengang konnte nicht bearbeitet werden.");
							  }

							  @Override
							  public void onSuccess(Studiengang result) {
								  tvm.updateStudiengang(shownSg);
								  Window.alert ("Erfolgreich gespeichert.");
							  } 	
							});
					  }
				  }
				 
				  public void deleteSelectedSg(){
					verwaltungsSvc.deleteStudiengang(shownSg, new AsyncCallback<Boolean>() {
						  @Override
						  public void onFailure (Throwable caught) {
						  Window.alert("Der Studiengang konnte nicht gelöscht werden." +
					  		"Er ist in ein oder mehreren Stundenplaneinträgen vorhanden");
							  }

						  @Override
						  public void onSuccess(Boolean result) {
						  tvm.deleteStudiengang(shownSg);
						  Window.alert ("Erfolgreich gelöscht.");
						  } 	
						});
			  		this.clearFields();
				  }

		public void setSelected(Studiengang sg){
			if (sg != null) {
				shownSg = sg;
				setFields();
			} else {
				clearFields();
			}
		}
		
		public void setFields(){
			tbbezeichnung.setText(shownSg.getBezeichnung());
		}
		
		public void clearFields(){
			tbbezeichnung.setText("");
		}
}	  