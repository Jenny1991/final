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
 * Formular für die Darstellung des selektierten Studiengangs
 * 
 * @author Thies, Espich
 *
 */

public class StudiengangForm extends Content {
	
	private final HTML ueberschrift = new HTML ("<h2>Ãœbersicht der Studiengänge<h2>");
	private final HTML ueberschriftAenderung = new HTML ("<h2>Studiengang bearbeiten<h2>");

	  final Label lbbezeichnung = new Label ("Bezeichnung"); 
	  final TextBox tbbezeichnung = new TextBox ();
	  final Button bearbeiten = new Button ("Studiengang bearbeiten");
	  final Button loeschen = new Button ("Studiengang löschen");
	  final Button speichern = new Button ("Änderungen speichern");
	  			  
	  final VerwaltungsklasseAsync verwaltungsSvc = GWT.create(Verwaltungsklasse.class);
	  Studiengang shownSg = null; 
	  NavTreeViewModel tvm = null;
	  
	  public StudiengangForm() {
		  Grid studiengangGrid = new Grid (3, 2);
		    this.add(ueberschrift);
			this.add(studiengangGrid);
		  
			Label lbbezeichnung = new Label("Bezeichnung");
			studiengangGrid.setWidget(0, 0, lbbezeichnung);
			studiengangGrid.setWidget(0, 1, tbbezeichnung);
			
			Label lbfunktionen = new Label ("Funktionen");
			studiengangGrid.setWidget(1, 0, bearbeiten);
			studiengangGrid.setWidget(2, 0, loeschen);
			}
	  
		public void onLoad() {
			
			//setTvm();
			getSelectedData();
			
			bearbeiten.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					showWidget();
				}
			});
			
			  speichern.addClickHandler(new ClickHandler() {
				  public void onClick(ClickEvent event) {

					  boolean allFilled = true;
					  
					  if (tbbezeichnung.getValue().isEmpty()) {
						  allFilled = false;
					  Window.alert ("Bitte fÃ¼llen Sie alle Felder aus."); }
					  
					  if (allFilled == true) {
						  shownSg.setBezeichnung(tbbezeichnung.getValue().trim());
						  tbbezeichnung.setFocus(true);
					  
						  verwaltungsSvc.changeStudiengang(shownSg, new AsyncCallback<Studiengang> () {

							  @Override
							  public void onFailure (Throwable caught) {
								  Window.alert("Der Studiengang konnte nicht bearbeitet werden.");
							  }

							  @Override
							  public void onSuccess(Studiengang result) {
								  tbbezeichnung.setText("");
								  tvm.updateStudiengang(shownSg);
								  Window.alert ("Erfolgreich gespeichert.");
							  } 	
							});
					  }
				  }
				  }); 
			
			loeschen.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event){
					verwaltungsSvc.deleteStudiengang(shownSg, new AsyncCallback<Void>() {
						  @Override
						  public void onFailure (Throwable caught) {
							  Window.alert("Der Studiengang konnte nicht gelöscht werden." +
							  		"Er ist in ein oder mehreren Stundenplaneinträgen eingetragen");
						  }

						  @Override
						  public void onSuccess(Void result) {
							  //tvm.deleteDozent(shownSg);
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
			/*verwaltungsSvc.getStudiengangById(sgId, new AsyncCallback<Studiengang>(){
				@Override
				public void onFailure(Throwable caught) {
				}
				
				@Override
				public void onSuccess(Studiengang result) {
					if (result != null) {
						setSelected(result);
					}
				}
			});*/
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
		
		  public void showWidget(){
			  	 this.add(ueberschriftAenderung);
				  this.add(lbbezeichnung);
				  this.add(tbbezeichnung);
				  this.add(speichern);
			  }
}  
								  
								  
								  
								  
								  
								  
								  
								  
				  