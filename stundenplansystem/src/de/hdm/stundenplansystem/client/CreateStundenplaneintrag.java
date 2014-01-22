package de.hdm.stundenplansystem.client;

import java.util.Vector;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Button;

import de.hdm.stundenplansystem.shared.*;
import de.hdm.stundenplansystem.shared.bo.Dozent;
import de.hdm.stundenplansystem.shared.bo.Lehrveranstaltung;
import de.hdm.stundenplansystem.shared.bo.Raum;
import de.hdm.stundenplansystem.shared.bo.Semesterverband;
import de.hdm.stundenplansystem.shared.bo.Studiengang;
import de.hdm.stundenplansystem.shared.bo.Stundenplan;
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
		  final Label lbstudienhj = new Label ("Studienhalbjahr");
		  final Label lbsemesterverband = new Label ("Semester");
		  final Label lblehrveranstaltung = new Label ("Lehrveranstaltung");
		  final Label lbstudiengang = new Label ("Studiengang");
		  
		  final ListBox listDozent = new ListBox ();
		  final ListBox listZeitslot = new ListBox ();
		  final ListBox listRaum = new ListBox ();
		  final ListBox listStudienhj = new ListBox(); 
		  final ListBox listSemesterverband = new ListBox (); 
		  final ListBox listLehrveranstaltung = new ListBox ();
		  final ListBox listStudiengang = new ListBox ();
		  
		  Vector<Dozent> dozentenContainer = null;
		  Vector<Zeitslot> zeitslotContainer = null;
		  Vector<Raum> raumContainer = null;
		  Vector<Stundenplan> spContainer = null;
		  Vector<Semesterverband> svContainer = null;
		  Vector<Lehrveranstaltung> lvContainer = null;
		  Vector<Studiengang> sgContainer = null;
		  
		  final Button speichern = new Button ("speichern");
		  
		  final VerwaltungsklasseAsync verwaltungsSvc = GWT.create(Verwaltungsklasse.class);
		  NavTreeViewModel tvm = null;
		  
		  /**
		  * Anordnen der Buttons und Labels auf den Panels
		  */
		  public void onLoad () {

				  this.add(ueberschrift);
				  this.add(lbstudienhj);
				  this.add(listStudienhj);
				  this.add(lbstudiengang);
				  this.add(listStudiengang);
				  this.add(lbsemesterverband);
				  this.add(listSemesterverband);
				  this.add(lblehrveranstaltung);
				  this.add(listLehrveranstaltung);
			  	  this.add(lbdozent);
				  this.add(listDozent);
				  this.add(lbraum);
				  this.add(listRaum);
				  this.add(lbzeitslot);
				  this.add(listZeitslot);
				  this.add(speichern);
				  
				  setTvm(tvm);
				  listDozent.clear();
				  listZeitslot.clear();
				  listSemesterverband.clear();
				  listStudiengang.clear();
				  listRaum.clear();
				  listLehrveranstaltung.clear();
				  
				  
				  verwaltungsSvc.getAllStundenplaene(new AsyncCallback<Vector<Stundenplan>>() {
						@Override
						public void onFailure(Throwable caught) {	
						}
						@Override
						public void onSuccess(Vector<Stundenplan> result) {
							spContainer = result;
							for (Stundenplan sp : result) {
								listStudienhj.addItem(sp.getStudienhalbjahr(), String.valueOf(sp.getId()));
							}
							ladeAlleStudiengaenge();
						} 
					  });
							
				  
				  speichern.addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {

							  int d = listDozent.getSelectedIndex();
							  int l = listLehrveranstaltung.getSelectedIndex();
							  int r = listRaum.getSelectedIndex();
							  int z = listZeitslot.getSelectedIndex();
							  int sv = listSemesterverband.getSelectedIndex();
							  int sp = listStudienhj.getSelectedIndex();
					
							 verwaltungsSvc.createStundenplaneintrag(dozentenContainer.elementAt(listDozent.getSelectedIndex()).getId(), lvContainer.elementAt(listLehrveranstaltung.getSelectedIndex()).getId(), raumContainer.elementAt(listRaum.getSelectedIndex()).getId(), zeitslotContainer.elementAt(listZeitslot.getSelectedIndex()).getId(), svContainer.elementAt(listSemesterverband.getSelectedIndex()).getId(), spContainer.elementAt(listStudienhj.getSelectedIndex()).getId(), new AsyncCallback<Stundenplaneintrag>(){
								 @Override
								  public void onFailure (Throwable caught) {
									  Window.alert("Der Stundenplaneintrag konnte nicht angelegt werden.");
								  }


								@Override
								public void onSuccess(Stundenplaneintrag result) {
									Window.alert ("Der Stundenplaneintrag wurde erfolgreich gespeichert.");
									tvm.addStundenplaneintrag(result);									
								} 
							 }); 
						  }
				});				  
		  }
		  
		public void ladeAlleStudiengaenge() {
			verwaltungsSvc.getAllStudiengaenge(new AsyncCallback<Vector<Studiengang>>() {
				@Override
				public void onFailure(Throwable caught) {	
				}
				@Override
				public void onSuccess(Vector<Studiengang> result) {
					sgContainer = result;
					for (Studiengang sg : result) {
						listStudiengang.addItem(sg.getBezeichnung(), String.valueOf(sg.getId()));
					}
					ladeAlleSemester();
				} 
			  });
		}
		
		public void ladeAlleSemester() {
			 verwaltungsSvc.getAllSemesterverbaende(new AsyncCallback<Vector<Semesterverband>>() {
					@Override
					public void onFailure(Throwable caught) {	
					}
					@Override
					public void onSuccess(Vector<Semesterverband> result) {
						svContainer = result;
						for (Semesterverband sv : result) {
							listSemesterverband.addItem(String.valueOf(sv.getSemester()), String.valueOf(sv.getId()));
						}
						ladeAlleLehrveranstaltungen();
					}
				});
		}
		
		public void ladeAlleLehrveranstaltungen() {
			verwaltungsSvc.getAllLehrveranstaltungen(new AsyncCallback<Vector<Lehrveranstaltung>>() {
				@Override
				public void onFailure(Throwable caught) {	
				}
				@Override
				public void onSuccess(Vector<Lehrveranstaltung> result) {
					lvContainer = result;
					for (Lehrveranstaltung lv : result) {
						listLehrveranstaltung.addItem(lv.getBezeichnung(), String.valueOf(lv.getId()));
					}
					ladeAlleDozenten();
				} 
			  });
		}
		
		public void ladeAlleDozenten() {
			verwaltungsSvc.getAllDozenten(new AsyncCallback<Vector<Dozent>>() {
				@Override
				public void onFailure(Throwable caught) {	
				}
				@Override
				public void onSuccess(Vector<Dozent> result) {
					dozentenContainer = result;
					for (Dozent d : result) {
						listDozent.addItem(d.getNachname() + ", " + d.getVorname(), String.valueOf(d.getId()));
					}
					ladeAlleRaeume();
				} 
			  });
		}
		
		public void ladeAlleRaeume() {
			verwaltungsSvc.getAllRaeume(new AsyncCallback<Vector<Raum>>() {
				@Override
				public void onFailure(Throwable caught) {	
				}
				@Override
				public void onSuccess(Vector<Raum> result) {
					raumContainer = result;
					for (Raum r : result) {
						listRaum.addItem(r.getBezeichnung(), String.valueOf(r.getId()));
					}
					ladeAlleZeitslots();
				} 
			  });
		}
		
		public void ladeAlleZeitslots() {
			verwaltungsSvc.getAllZeitslots(new AsyncCallback<Vector<Zeitslot>>() {
				@Override
				public void onFailure(Throwable caught) {	
				}
				@Override
				public void onSuccess(Vector<Zeitslot> result) {
					zeitslotContainer = result;
					for (Zeitslot zs : result) {
						listZeitslot.addItem(zs.getWochentag() + ", " + zs.getAnfangszeit() + ", " + zs.getEndzeit(), String.valueOf(zs.getId()));
					}
				} 
		  });
		}
		  


		public void setTvm(NavTreeViewModel tvm) {
				this.tvm = tvm;
			}
}
