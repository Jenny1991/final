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
import de.hdm.stundenplansystem.client.NavTreeViewModel;
import de.hdm.stundenplansystem.shared.*;
import de.hdm.stundenplansystem.shared.bo.Lehrveranstaltung;

/**
 * Formular fÃ¼r die Darstellung der selektierten Lehrveranstaltung
 * 
 * @author Thies, Espich
 *
 */

public class LehrveranstaltungForm extends Content {

	/**
	 * Aufbau der Seite, um die Lehrveranstaltung anzuzeigen, zu lÃƒÂ¶schen und zu bearbeiten
	 */
	
	private final HTML ueberschriftAenderung = new HTML ("<h2>Lehrveranstaltung verwalten<h2>");

	final TextBox tbbezeichnung = new TextBox ();
	final TextBox tbsemester = new TextBox();
	final TextBox tbumfang = new TextBox (); 	  
	final Button loeschen = new Button ("Lehrveranstaltung löschen");
	final Button speichern = new Button ("Änderungen speichern");
	final VerwaltungsklasseAsync verwaltungsSvc = GWT.create(Verwaltungsklasse.class);
	
	Integer id;
	Lehrveranstaltung shownLv = null;
	NavTreeViewModel tvm = null;	
	
	public LehrveranstaltungForm(){
		Grid lehrGrid = new Grid (3, 5);
	    this.add(ueberschriftAenderung);
		this.add(lehrGrid);
	  
		Label lbbezeichnung = new Label("Bezeichnung");
		lehrGrid.setWidget(0, 0, lbbezeichnung);
		lehrGrid.setWidget(1, 0, tbbezeichnung);

		Label lbsemester = new Label("Semester");
		lehrGrid.setWidget(0, 1, lbsemester);
		lehrGrid.setWidget(1, 1, tbsemester);
		
		Label lbumfang = new Label("Umfang (SWS)");
		lehrGrid.setWidget(0, 2, lbumfang);
		lehrGrid.setWidget(1, 2, tbumfang);
		
		Label lbfunktionen = new Label ("Funktionen");
		lehrGrid.setWidget(0, 3, lbfunktionen);
		lehrGrid.setWidget(1, 3, speichern);
		speichern.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				changeSelectedLv();
			}
		});
		lehrGrid.setWidget(1, 4, loeschen);
		loeschen.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				deleteSelectedLv();
			}
		});
		setTvm(tvm);
		}
		
	public void getData() {		
		verwaltungsSvc.getLehrveranstaltungById(id, new AsyncCallback<Lehrveranstaltung>(){
			@Override
			public void onFailure(Throwable caught) {
			}
			
			@Override
			public void onSuccess(Lehrveranstaltung result) {
				if (result != null) {
					setSelected(result);
				}
			}
		});
	  }
	
			public void changeSelectedLv(){
		
				  boolean allFilled = true;
			  
				  if (tbbezeichnung.getValue().isEmpty() 
						  ||tbsemester.getValue().isEmpty()
				  		  ||tbumfang.getValue().isEmpty()){	
					  allFilled = false;
				  Window.alert ("Bitte füllen Sie alle Felder aus."); } 
				  
				  if (allFilled == true) {
					  shownLv.setBezeichnung(tbbezeichnung.getText().trim());
					  shownLv.setSemester(Integer.valueOf(tbsemester.getValue()));
					  shownLv.setUmfang(Integer.valueOf(tbumfang.getValue()));

					  verwaltungsSvc.changeLehrveranstaltung(shownLv, new AsyncCallback<Void>(){

						  @Override
						  public void onFailure (Throwable caught) {
							  Window.alert("Die Lehrveranstaltung konnte nicht bearbeitet werden.");
						  }

						  @Override
						  public void onSuccess(Void result) {
							  tvm.updateLehrveranstaltung(shownLv);
							  Window.alert ("Erfolgreich gespeichert.");
						  } 	
						});
				  }
			  }
	
	public void deleteSelectedLv(){
			verwaltungsSvc.deleteLehrveranstaltung(shownLv, new AsyncCallback<Boolean>() {
				  @Override
				  public void onFailure (Throwable caught) {
					  Window.alert("Die Lehrveranstaltung konnte nicht gelöscht werden." +
					  		"Sie ist in ein oder mehreren Stundenplaneinträgen vorhanden");
				  }

				  @Override
				  public void onSuccess(Boolean result) {
					  tvm.deleteLehrveranstaltung(shownLv);
					  Window.alert ("Erfolgreich gelöscht.");
				  } 	
				});
			this.clearFields();
		  }
	
	public void setTvm(NavTreeViewModel tvm) {
		this.tvm = tvm;
	}
	
	public void setSelected(Lehrveranstaltung lv){
		if (lv != null) {
			shownLv = lv;
			setFields();
		} else {
			clearFields();
		}
	}
	
	public void setFields(){
		  tbbezeichnung.setText(shownLv.getBezeichnung());
		  tbsemester.setValue(Integer.toString(shownLv.getSemester()));
		  tbumfang.setValue(Integer.toString(shownLv.getUmfang()));
	}
	
	public void clearFields(){
		  tbbezeichnung.setText("");
		  tbsemester.setText("");
		  tbumfang.setText("");
	}
}