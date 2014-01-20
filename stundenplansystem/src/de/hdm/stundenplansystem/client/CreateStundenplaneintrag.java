package de.hdm.stundenplansystem.client;

import java.util.Vector;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Button;

import de.hdm.stundenplansystem.shared.*;
import de.hdm.stundenplansystem.shared.bo.Dozent;
import de.hdm.stundenplansystem.shared.bo.Lehrveranstaltung;
import de.hdm.stundenplansystem.shared.bo.Raum;
import de.hdm.stundenplansystem.shared.bo.Semesterverband;
import de.hdm.stundenplansystem.shared.bo.Studiengang;
import de.hdm.stundenplansystem.shared.bo.Stundenplaneintrag;
import de.hdm.stundenplansystem.shared.bo.Zeitslot;


	/**
	 * Hier wird ein neuer Stundenplaneintrag angelegt.
	 * 
	 * @author Thies, Espich, V. Hofmann
	 * 
	 */

public class CreateStundenplaneintrag extends Content {

		/**
		 * Jede Klasse enthältt eine Überschrift, die definiert, was der User machen kann.
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
		  
		  final ListBox listDozent = new ListBox ();
		  final ListBox listZeitslot = new ListBox ();
		  final ListBox listRaum = new ListBox ();
		  final ListBox listStudiengang = new ListBox(); 
		  final ListBox listSemesterverband = new ListBox (); 
		  final ListBox listLehrveranstaltung = new ListBox ();
		  
		  final Button speichern = new Button ("speichern");
		  
		  final VerwaltungsklasseAsync verwaltungsSvc = GWT.create(Verwaltungsklasse.class);
		  NavTreeViewModel tvm = null;
		  
		  /**
		  * Anordnen der Buttons und Labels auf den Panels
		  */
		  public void onLoad () {

				  this.add(ueberschrift);
			  	  this.add(lbdozent);
				  this.add(listDozent);
				  this.add(lbzeitslot);
				  this.add(listZeitslot);
				  this.add(lbraum);
				  this.add(listRaum);
				  this.add(lbstudiengang);
				  this.add(listStudiengang);
				  this.add(lbsemesterverband);
				  this.add(listSemesterverband);
				  this.add(lblehrveranstaltung);
				  this.add(listLehrveranstaltung);
				  this.add(speichern);
				  
				  setTvm(tvm);
				  				  
				  verwaltungsSvc.getAllDozenten(new AsyncCallback<Vector<Dozent>>() {
					@Override
					public void onFailure(Throwable caught) {	
					}
					@Override
					public void onSuccess(Vector<Dozent> result) {
						for (Dozent d : result) {
							listDozent.addItem(d.getNachname() + ", " + d.getVorname());
						}
					} 
				  });
				    
				  verwaltungsSvc.getAllZeitslots(new AsyncCallback<Vector<Zeitslot>>() {
						@Override
						public void onFailure(Throwable caught) {	
						}
						@Override
						public void onSuccess(Vector<Zeitslot> result) {
							for (Zeitslot zs : result) {
								listZeitslot.addItem(zs.getWochentag() + ", " + zs.getAnfangszeit() + ", " + zs.getEndzeit());
							}
						} 
				  });
				  
				  verwaltungsSvc.getAllRaeume(new AsyncCallback<Vector<Raum>>() {
						@Override
						public void onFailure(Throwable caught) {	
						}
						@Override
						public void onSuccess(Vector<Raum> result) {
							for (Raum r : result) {
								listRaum.addItem(r.getBezeichnung());
							}
						} 
					  });
				  
				  verwaltungsSvc.getAllStudiengaenge(new AsyncCallback<Vector<Studiengang>>() {
						@Override
						public void onFailure(Throwable caught) {	
						}
						@Override
						public void onSuccess(Vector<Studiengang> result) {
							for (Studiengang sg : result) {
								listStudiengang.addItem(sg.getBezeichnung());
							}
						} 
					  });
				  
				  verwaltungsSvc.getAllSemesterverbaende(new AsyncCallback<Vector<Semesterverband>>() {
						@Override
						public void onFailure(Throwable caught) {	
						}
						@Override
						public void onSuccess(Vector<Semesterverband> result) {
							for (Semesterverband sv : result) {
								listSemesterverband.addItem(sv.getJahrgang() + " " +  sv.getSemester());
							}
						} 
					  });
				  
				  verwaltungsSvc.getAllLehrveranstaltungen(new AsyncCallback<Vector<Lehrveranstaltung>>() {
						@Override
						public void onFailure(Throwable caught) {	
						}
						@Override
						public void onSuccess(Vector<Lehrveranstaltung> result) {
							for (Lehrveranstaltung lv : result) {
								listLehrveranstaltung.addItem(lv.getBezeichnung());
							}
						} 
					  });
				  
				  speichern.addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {
						  /**boolean allFilled = true;

						  if (listDozent.getName().isEmpty() 
								  ||listZeitslot.getName().isEmpty()
								  ||listSemesterverband.getName().isEmpty()
								  ||listStudiengang.getName().isEmpty()
								  ||listRaum.getName().isEmpty()
								  ||listLehrveranstaltung.getName().isEmpty()) {	
							  allFilled = false;
							  Window.alert ("Bitte füllen Sie alle Felder aus."); } 
						  
						  if (allFilled == true) {*/
							  int d = listDozent.getSelectedIndex();
							  int l = listLehrveranstaltung.getSelectedIndex();
							  int r = listRaum.getSelectedIndex();
							  int z = listZeitslot.getSelectedIndex();
							  int sv = listSemesterverband.getSelectedIndex();
							  int sg = listStudiengang.getSelectedIndex();
					
							 verwaltungsSvc.createStundenplaneintrag(d, l, r, z, sv, sg, new AsyncCallback<Stundenplaneintrag>(){
								 @Override
								  public void onFailure (Throwable caught) {
									  Window.alert("Der Stundenplaneintrag konnte nicht angelegt werden.");
								  }


								@Override
								public void onSuccess(Stundenplaneintrag result) {
									listDozent.setTitle("");
									listZeitslot.setTitle("");
									listSemesterverband.setTitle("");
									listStudiengang.setTitle("");
									listRaum.setTitle("");
									listLehrveranstaltung.setTitle("");
									Window.alert ("Der Stundenplaneintrag wurde erfolgreich gespeichert.");
									tvm.addStundenplaneintrag(result);									
								} 
							 }); 
						  }
					  //}
				});
				  
				  
				  
				  
				  
				  
				  				  
				  /*speichern.addClickHandler(new ClickHandler() {
					  public void onClick(ClickEvent event) {
						  addStundenplaneintrag();
					  }
					  
					  public void addStundenplaneintrag(){					  
						  final String [] d;
						  listDozent.getItemText(listDozent.getSelectedIndex());
						  final String [] r;
						  r= listRaum.getItemText(listRaum.getSelectedIndex()).split("");
						  final String [] l;
						  l = listLehrveranstaltung.getItemText(listLehrveranstaltung.getSelectedIndex()).split("");
						  final String [] sv;
						  sv = listSemesterverband.getItemText(listSemesterverband.getSelectedIndex()).split("");
						  final String [] s;
						  s = listStudiengang.getItemText(listStudiengang.getSelectedIndex()).split("");
						  final String [] z;
						  z = listZeitslot.getItemText(listZeitslot.getSelectedIndex()).split("");
						  
						  Stundenplaneintrag spe = new Stundenplaneintrag();
						  
					verwaltungsSvc.createStundenplaneintrag(d, l, r, z, sv, new AsyncCallback<Stundenplaneintrag>() {
								 @Override
								  public void onFailure (Throwable caught) {
								  }

								  @Override
								  public void onSuccess(Stundenplaneintrag result) {
									  Window.alert("Erfolgreich gespeichert");
								  }
							  }); 
					  		}
						 });*/
				  
		  }
		  


		public void setTvm(NavTreeViewModel tvm) {
				this.tvm = tvm;
			}
}
