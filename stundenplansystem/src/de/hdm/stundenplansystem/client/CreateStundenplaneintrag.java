package de.hdm.stundenplansystem.client;

import java.util.Vector;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Button;

import de.hdm.stundenplansystem.shared.*;
import de.hdm.stundenplansystem.shared.bo.Stundenplaneintrag;


	/**
	 * Hier wird ein neuer Stundenplaneintrag angelegt.
	 * 
	 * @author Thies, Espich
	 * 
	 */

public class CreateStundenplaneintrag extends Content {

		/**
		 * Jede Klasse enthï¿½t eine ï¿½berschrift, die definiert, was der User machen kann.
		 */
		private final HTML ueberschrift = new HTML ("<h2>Neuen Stundenplaneintrag anlegen<h2>");

		  /**
		   * Unter der Überschrift trägt der User die Daten des neuen Stundenplaneintrags ein. 
		   */
		  final Label lbdozent = new Label ("Dozent"); 
		  final Label lbzeitslot = new Label ("Zeitslot");
		  final Label lbraum = new Label ("Raum");
		  final Label lbstudiengang = new Label ("Studiengang");
		  final Label lbsemesterverband = new Label ("Semesterverband");
		  final Label lblehrveranstaltung = new Label ("Lehrveranstaltung");
		  final ListBox tbdozent = new ListBox ();
		  final ListBox tbzeitslot = new ListBox ();
		  final ListBox tbraum = new ListBox ();
		  final ListBox tbstudiengang = new ListBox(); 
		  final ListBox tbsemesterverband = new ListBox (); 
		  final ListBox tblehrveranstaltung = new ListBox ();
		  final Button speichern = new Button ("speichern");
		  final HorizontalPanel hPanel = new HorizontalPanel();
		  
		  final VerwaltungsklasseAsync verwaltungsSvc = GWT.create(Verwaltungsklasse.class);
		  
		  /**
		  * Anordnen der Buttons und Labels auf den Panels
		  */
		  public void onLoad () {

				  this.add(ueberschrift);
			  	  this.add(lbdozent);
				  hPanel.add(tbdozent);
				  this.add(lbzeitslot);
				  hPanel.add(tbzeitslot);
				  this.add(lbraum);
				  hPanel.add(tbraum);
				  this.add(lbstudiengang);
				  hPanel.add(tbstudiengang);
				  this.add(lbsemesterverband);
				  hPanel.add(tbsemesterverband);
				  this.add(lblehrveranstaltung);
				  hPanel.add(tblehrveranstaltung);
				  this.add(speichern);
				  				  
			/**	  verwaltungsSvc.getAllDozenten(new AsyncCallback<Vector<Dozent>>(){
					  @Override
					  public void onFailure (Throwable caught) {
					  }
					  @Override
					  public void onSuccess(Vector<Dozent> result){
						  int i = 0; 
						  while (!result.isEmpty()) {
							  tbdozent.addItem(result.get(i).getNachname() + "/" + result.get(i).getVorname());
							  i++;
						  }
					  }
				  });
				  
				  verwaltungsSvc.getAllSemesterverbaende(new AsyncCallback<Vector<Semesterverband>>(){
					  @Override
					  public void onFailure (Throwable caught) {
					  }
					  @Override
					  public void onSuccess(Vector<Semesterverband> result){
						  int i = 0; 
						  while (!result.isEmpty()) {
							  tbsemesterverband.addItem(result.get(i).getBezeichnung() + "/" + result.get(i).getJahrgang()
							  + "/" + result.get(i).getSemester() + "/" + result.get(i).getStudierendenAnzahl());
							  i++;
						  }
					  }
				  }); */
				  
				  				  
				  speichern.addClickHandler(new ClickHandler() {
					  public void onClick(ClickEvent event) {
						  addStundenplaneintrag();
					  }
					  
					  public void addStundenplaneintrag(){					  
						  final String [] d;
						  tbdozent.getItemText(tbdozent.getSelectedIndex());
						  final String [] r;
						  r= tbraum.getItemText(tbraum.getSelectedIndex()).split("");
						  final String [] l;
						  l = tblehrveranstaltung.getItemText(tblehrveranstaltung.getSelectedIndex()).split("");
						  final String [] sv;
						  sv = tbsemesterverband.getItemText(tbsemesterverband.getSelectedIndex()).split("");
						  final String [] s;
						  s = tbstudiengang.getItemText(tbstudiengang.getSelectedIndex()).split("");
						  final String [] z;
						  z = tbzeitslot.getItemText(tbzeitslot.getSelectedIndex()).split("");
						  
						  Stundenplaneintrag stdpe = new Stundenplaneintrag();
						  
					/**	  verwaltungsSvc.createStundenplaneintrag(d, l, r, z, sv, new AsyncCallback<Stundenplaneintrag>() {
								 @Override
								  public void onFailure (Throwable caught) {
								  }

								  @Override
								  public void onSuccess(Stundenplaneintrag result) {
									  Window.alert("Erfolgreich gespeichert");
								  }
							  }); */
					  		}
						 });
				  
		  }
}
