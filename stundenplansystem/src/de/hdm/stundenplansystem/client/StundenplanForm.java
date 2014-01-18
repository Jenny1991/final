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
import de.hdm.stundenplansystem.shared.bo.Stundenplan;
import de.hdm.stundenplansystem.client.NavTreeViewModel;


/**
 * Formular fÃ¼r die Darstellung des selektierten Kunden
 * 
 * @author Thies, Espich
 *
 */

public class StundenplanForm extends Content {
	
	private final HTML ueberschrift = new HTML ("<h2>Übersicht der Studienhalbjahre<h2>");

	  final TextBox tbhalbjahr = new TextBox ();	  			  
	  final Button loeschen = new Button ("Studienhalbjahr löschen");
	  final Button speichern = new Button ("Änderungen speichern");
	  final VerwaltungsklasseAsync verwaltungsSvc = GWT.create(Verwaltungsklasse.class);
	  
	  Integer id;
	  Stundenplan shownSp = null; 
	  NavTreeViewModel tvm = null;
	  
	  public StundenplanForm() {
		  Grid stGrid = new Grid (2, 3);
		  	this.add(ueberschrift);
			this.add(stGrid);
		  
			Label lbhalbjahr = new Label("Studienhalbjahr");
			stGrid.setWidget(0, 0, lbhalbjahr);
			stGrid.setWidget(1, 0, tbhalbjahr);
			
			Label lbfunktionen = new Label ("Funktionen");
			stGrid.setWidget(0, 1, lbfunktionen);
			stGrid.setWidget(1, 1, speichern);
			speichern.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					changeSelectedHj();
				}
			});
			stGrid.setWidget(1, 2, loeschen);
			loeschen.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					deleteSelectedHj();
				}
			});
			setTvm(tvm);
	  } 
	  
	  
		public void getData() {
				verwaltungsSvc.getStundenplanById(id, new AsyncCallback<Stundenplan>(){
					@Override
					public void onFailure(Throwable caught) {
					}
					
					@Override
					public void onSuccess(Stundenplan sp) {
						if (sp != null) {
							setSelected(sp);
						}
					};		
				});
			}

		public void setTvm(NavTreeViewModel tvm) {
			this.tvm = tvm;
		}
		
		public void changeSelectedHj(){
			  
			  boolean allFilled = true;
			  
			  if (tbhalbjahr.getValue().isEmpty())
			  { allFilled = false;
			  Window.alert ("Bitte füllen Sie alle Felder aus."); } 
			  
			  if (allFilled == true) {
				  shownSp.setStudienhalbjahr(tbhalbjahr.getText().trim());
				  
				  verwaltungsSvc.changeStundenplan(shownSp, new  AsyncCallback<Void> () {

					  @Override
					  public void onFailure (Throwable caught) {
						  Window.alert("Das Studienhalbjahr konnte nicht bearbeitet werden.");
					  }

					  @Override
					  public void onSuccess(Void result) {
						  tvm.updateStudienhalbjahr(shownSp);
						  Window.alert ("Erfolgreich gespeichert.");
						  
					  } 	
					});
			  }
		  }			
		
		public void deleteSelectedHj(){
			verwaltungsSvc.deleteStundenplan(shownSp, new AsyncCallback<Boolean>() {
			  public void onFailure (Throwable caught) {
				  Window.alert("Das Studienhalbjahr konnte nicht gelöscht werden." +
				  		"Es ist in ein oder mehreren Stundenplaneinträgen eingetragen");
			  }

			  public void onSuccess(Boolean result) {
				  tvm.deleteStudienhalbjahr(shownSp);
				  Window.alert ("Erfolgreich gelöscht.");
			  } 	
			});
			this.clearFields();
	  }
	
		public void setSelected(Stundenplan sp){
			if (sp != null) {
				shownSp = sp;
				setFields();
			} else {
				clearFields();
			}
		} 
		
		public void setFields(){
			tbhalbjahr.setText(shownSp.getStudienhalbjahr());
		}
		
		public void clearFields(){
			tbhalbjahr.setText("");
		}
}
			
			
	