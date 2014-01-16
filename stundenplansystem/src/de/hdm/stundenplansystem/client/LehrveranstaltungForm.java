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
 * Formular für die Darstellung der selektierten Lehrveranstaltung
 * 
 * @author Thies, Espich
 *
 */

public class LehrveranstaltungForm extends Content {

	/**
	 * Aufbau der Seite, um die Lehrveranstaltung anzuzeigen, zu lÃ¶schen und zu bearbeiten
	 */
	
	private final HTML ueberschrift = new HTML ("<h2>Ãœbersicht der Lehrveranstaltungen<h2>");
	private final HTML ueberschriftAenderung = new HTML ("<h2>Lehrveranstaltung bearbeiten<h2>");

	final Label lbbezeichnung = new Label ("Bezeichnung"); 
	final Label lbsemester = new Label ("Semester");
	final Label lbumfang = new Label ("Umfang");
	final TextBox tbbezeichnung = new TextBox ();
	final TextBox tbsemester = new TextBox();
	final TextBox tbumfang = new TextBox (); 	  
	final Button bearbeiten = new Button ("Lehrveranstaltung bearbeiten");
	final Button loeschen = new Button ("Lehrveranstaltung löschen");
	final Button speichern = new Button ("Änderungen speichern");
	
	final VerwaltungsklasseAsync verwaltungsSvc = GWT.create(Verwaltungsklasse.class);
	Lehrveranstaltung shownLv = null;
	NavTreeViewModel tvm = null;	
	
	public LehrveranstaltungForm(){
		Grid lehrGrid = new Grid (5, 3);
	    this.add(ueberschrift);
		this.add(lehrGrid);
	  
		Label lbbezeichnung = new Label("Bezeichnung");
		lehrGrid.setWidget(0, 0, lbbezeichnung);
		lehrGrid.setWidget(0, 1, tbbezeichnung);

		Label lbsemester = new Label("Semester");
		lehrGrid.setWidget(1, 0, lbsemester);
		lehrGrid.setWidget(1, 1, tbsemester);
		
		Label lbumfang = new Label("Umfang");
		lehrGrid.setWidget(2, 0, lbumfang);
		lehrGrid.setWidget(2, 2, tbumfang);
		
		Label lbfunktionen = new Label ("Funktionen");
		lehrGrid.setWidget(3, 0, bearbeiten);
		lehrGrid.setWidget(4, 0, loeschen);
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
			  
				  if (tbbezeichnung.getValue().isEmpty() 
						  ||tbsemester.getValue().isEmpty()
				  		  ||tbumfang.getValue().isEmpty()){	
					  allFilled = false;
				  Window.alert ("Bitte fÃ¼llen Sie alle Felder aus."); } 
				  
				  if (allFilled == true) {
					  Lehrveranstaltung lv = new Lehrveranstaltung();
					  lv.setBezeichnung(tbbezeichnung.getText().trim());
					  lv.setSemester(tbsemester.getVisibleLength());
					  lv.setUmfang(tbumfang.getVisibleLength());
					  tbbezeichnung.setFocus(true);
					  tbsemester.setFocus(true);
					  tbumfang.setFocus(true);

					  verwaltungsSvc.changeLehrveranstaltung(lv, new AsyncCallback<Lehrveranstaltung>(){

						  @Override
						  public void onFailure (Throwable caught) {
							  Window.alert("Die Lehrveranstaltung konnte nicht bearbeitet werden.");
						  }

						  @Override
						  public void onSuccess(Lehrveranstaltung result) {
							  Window.alert ("Erfolgreich gespeichert.");
							  tbbezeichnung.setText("");
							  tbsemester.setVisibleLength(result.getSemester());
							  tbumfang.setVisibleLength(result.getUmfang());
							  tvm.updateLehrveranstaltung(shownLv);
						  } 	
						});
				  }
			  }
			  }); 
		
		loeschen.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event){
				verwaltungsSvc.deleteLehrveranstaltung(shownLv, new AsyncCallback<Void>() {
					  @Override
					  public void onFailure (Throwable caught) {
						  Window.alert("Die Lehrveranstaltung konnte nicht gelöscht werden." +
						  		"Sie ist in ein oder mehreren Stundenplaneinträgen eingetragen");
					  }

					  @Override
					  public void onSuccess(Void result) {
						  tvm.deleteLehrveranstaltung(shownLv);
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
		/*verwaltungsSvc.getLehrveranstaltungById(lId, new AsyncCallback<Lehrveranstaltung>(){
			@Override
			public void onFailure(Throwable caught) {
			}
			
			@Override
			public void onSuccess(Lehrveranstaltung result) {
				if (result != null) {
					setSelected(result);
				}
			}
		});*/
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
		  tbsemester.setVisibleLength(shownLv.getSemester());
		  tbumfang.setVisibleLength(shownLv.getUmfang());
	}
	
	public void clearFields(){
		  tbbezeichnung.setText("");
		  tbsemester.setText("");
		  tbumfang.setText("");
	}
	
	  public void showWidget(){
		  this.add(ueberschriftAenderung);
		  this.add(lbbezeichnung);
		  this.add(tbbezeichnung);
		  this.add(lbsemester);
		  this.add(tbsemester);
		  this.add(lbumfang);
		  this.add(tbumfang);
		  this.add(speichern);
		  }
}