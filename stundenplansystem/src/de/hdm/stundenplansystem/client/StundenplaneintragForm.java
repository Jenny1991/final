package de.hdm.stundenplansystem.client;

import java.util.Vector;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;



import de.hdm.stundenplansystem.shared.VerwaltungsklasseAsync;
import de.hdm.stundenplansystem.shared.bo.Dozent;
import de.hdm.stundenplansystem.shared.bo.Lehrveranstaltung;
import de.hdm.stundenplansystem.shared.bo.Raum;
import de.hdm.stundenplansystem.shared.bo.Semesterverband;
import de.hdm.stundenplansystem.shared.bo.Studiengang;
import de.hdm.stundenplansystem.shared.bo.Stundenplan;
import de.hdm.stundenplansystem.shared.bo.Stundenplaneintrag;
import de.hdm.stundenplansystem.shared.bo.Zeitslot;
import de.hdm.stundenplansystem.shared.Verwaltungsklasse;


public class StundenplaneintragForm extends Content {

		/**
		 * Aufbau der Seite, um Stundenplaneinträge anzuzeigen, zu löschen und zu bearbeiten
		 */
	
	private final HTML ueberschriftAenderung = new HTML ("<h2>Stundenplaneintrag bearbeiten<h2>");
	
		final ListBox libzeitslot = new ListBox();
		final ListBox libdozent = new ListBox();
		final ListBox liblehrveranstaltung = new ListBox();
		final ListBox libraum = new ListBox();
		final ListBox libstudiengang = new ListBox();
		final ListBox libsemesterverband = new ListBox();
		final ListBox libstudienhj = new ListBox();
		final Button speichern = new Button ("Änderungen speichern");
		final Button loeschen = new Button("Stundenplaneintrag löschen");
		
		Vector<Studiengang> sgContainer = null;
		Vector<Semesterverband> svContainer = null;
		Vector<Stundenplan> spContainer = null;
		Vector<Lehrveranstaltung> lvContainer = null;
		Vector<Dozent> dozentContainer = null;
		Vector<Raum> raumContainer = null;
		Vector<Zeitslot> zsContainer = null;
		
		
		final VerwaltungsklasseAsync verwaltungsSvc = GWT.create(Verwaltungsklasse.class);
		
		Integer id;
		Stundenplaneintrag shownSpe = null; 
		NavTreeViewModel tvm = null;
		  
		  public StundenplaneintragForm() {
			  Grid speGrid = new Grid (9, 2);
			    this.add(ueberschriftAenderung);
				this.add(speGrid);
				
				Label lbStudiengang = new Label ("Studiengang");
				speGrid.setWidget(0, 0, lbStudiengang);
				speGrid.setWidget(0, 1, libstudiengang);
				
				Label lbSemesterverband = new Label ("Semesterverband");
				speGrid.setWidget(1, 0, lbSemesterverband);
				speGrid.setWidget(1, 1, libsemesterverband);
				
				Label lbStundenplan = new Label ("Studienhalbjahr");
				speGrid.setWidget(2, 0, lbStundenplan);
				speGrid.setWidget(2, 1, libstudienhj);
				
				Label lbLehrveranstaltung = new Label ("Lehrveranstaltung");
				speGrid.setWidget(3, 0, lbLehrveranstaltung);
				speGrid.setWidget(3, 1, liblehrveranstaltung);

				Label lbDozent = new Label("Dozent");
				speGrid.setWidget(4, 0, lbDozent);
				speGrid.setWidget(4, 1, libdozent);
				
				Label lbRaum = new Label ("Raum");
				speGrid.setWidget(5, 0, lbRaum);
				speGrid.setWidget(5, 1, libraum);
				
				Label lbZeitslot = new Label("Zeitslot");
				speGrid.setWidget(6, 0, lbZeitslot);
				speGrid.setWidget(6, 1, libzeitslot);
				
				
				Label lbFunktionen = new Label ("Funktionen");
				speGrid.setWidget(7, 0, lbFunktionen);
				speGrid.setWidget(7, 1, speichern);
				speichern.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
					changeSelectedSpe();
					}
				});
				
				speGrid.setWidget(8, 1, loeschen);
				loeschen.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						deleteSelectedSpe();
					}
				});
				
				setTvm(tvm);		
			
			this.clearFields();
			
			libstudiengang.addChangeHandler(new ChangeHandler() {
				@Override
				public void onChange(ChangeEvent event) {
					getSemesterverbaende();	
				}
			  });
		  
		  libsemesterverband.addChangeHandler(new ChangeHandler() {
			  @Override
			  public void onChange(ChangeEvent event) {
				  getStundenplaene();
			  }
		  });
		  
		  libraum.addChangeHandler(new ChangeHandler() {
			  @Override 
			  public void onChange(ChangeEvent event) {
				  getZeitslots();
			  }
		  });
			
		}
			
			public void getData() {
				verwaltungsSvc.getStundenplaneintragById(id, new AsyncCallback<Stundenplaneintrag>(){
					@Override
					public void onFailure(Throwable caught) {
					}
					
					@Override
					public void onSuccess(Stundenplaneintrag result) {
						if (result != null) {
							setSelected(result);
						}
					}
				});
			  }
			
			
			public void deleteSelectedSpe(){
				verwaltungsSvc.deleteStundenplaneintrag(shownSpe, new AsyncCallback<Boolean>() {
				  public void onFailure (Throwable caught) {
					  Window.alert("Der Dozent konnte nicht gelöscht werden." +
					  		"Er ist in ein oder mehreren Stundenplaneinträgen eingetragen");
				  }

				  public void onSuccess(Boolean result) {
					  tvm.deleteSpe(shownSpe);
					  Window.alert ("Erfolgreich gelöscht.");
				  } 	
				});
				this.clearFields();
		  }
			
			
		public void changeSelectedSpe(){
				  //shownSpe.setStudiengangId(sgContainer.elementAt(libstudiengang.getSelectedIndex()-1).getId());
				  shownSpe.setSemesterverbandId(svContainer.elementAt(libsemesterverband.getSelectedIndex()-1).getId());
				  shownSpe.setStundenplanId(spContainer.elementAt(libstudienhj.getSelectedIndex()-1).getId());
				  shownSpe.setLehrveranstaltungId(lvContainer.elementAt(liblehrveranstaltung.getSelectedIndex()-1).getId());
				  shownSpe.setDozentId(dozentContainer.elementAt(libdozent.getSelectedIndex()-1).getId());
				  shownSpe.setRaumId(raumContainer.elementAt(libraum.getSelectedIndex()-1).getId());
				  shownSpe.setZeitslotId(zsContainer.elementAt(libzeitslot.getSelectedIndex()-1).getId());

				  verwaltungsSvc.changeStundenplaneintrag(shownSpe, new AsyncCallback<Void>() {

					  @Override
					  public void onFailure (Throwable caught) {
						  Window.alert("Der Semesterverband konnte nicht angelegt werden.");
					  }
					  @Override
					  public void onSuccess(Void result) {
						  Window.alert ("Erfolgreich gespeichert.");
						  tvm.updateSpe(shownSpe);
						  
					  } 	
				}); 
		}
		
		public void setTvm(NavTreeViewModel tvm) {
			this.tvm = tvm;
		}
		
		public void setSelected(Stundenplaneintrag spe){
			if (spe != null) {
				shownSpe = spe;
				setFields();
			} else {
				clearFields();
			}
		} 
		
		public void setFields(){
			verwaltungsSvc.getStudiengangBySemesterverbandId(shownSpe.getSemesterverbandId(), new AsyncCallback<Studiengang>() {
				@Override
				  public void onFailure (Throwable caught) {
					caught.getMessage();
				  }
				  @Override
				  public void onSuccess(Studiengang result) {
					libstudiengang.addItem(result.getBezeichnung());
					getStudiengaenge();
				  } 	
			});
		}
				
			public void getNextListLv() {
				verwaltungsSvc.getLehrveranstaltungById(shownSpe.getLehrveranstaltungId(), new AsyncCallback<Lehrveranstaltung>() {
					@Override
					  public void onFailure (Throwable caught) {
						caught.getMessage();
					  }

					  @Override
					  public void onSuccess(Lehrveranstaltung result) {
						liblehrveranstaltung.addItem(result.getBezeichnung());
						getLehrveranstaltungen();
					  } 	
				});
				}
			
			
			public void getNextListSv() {
			verwaltungsSvc.getSemesterverbandById(shownSpe.getSemesterverbandId(), new AsyncCallback<Semesterverband>() {
				@Override
				  public void onFailure (Throwable caught) {
					caught.getMessage();
				  }

				  @Override
				  public void onSuccess(Semesterverband result) {
					libsemesterverband.addItem(result.getKuerzel() + String.valueOf(result.getSemester()));
					getSemesterverbaende();
				  } 	
			});
		}
			
			public void getNextListSp() {
			verwaltungsSvc.getStundenplanById(shownSpe.getStundenplanId(), new AsyncCallback<Stundenplan>() {
				@Override
				  public void onFailure (Throwable caught) {
					caught.getMessage();
				  }

				  @Override
				  public void onSuccess(Stundenplan result) {
					libstudienhj.addItem(String.valueOf(result.getStudienhalbjahr()));
					getStundenplaene();
				  } 	
			});
			}
			
			
			public void getNextListDozent() {
			verwaltungsSvc.getDozentById(shownSpe.getDozentId(), new AsyncCallback<Dozent>() {
				@Override
				  public void onFailure (Throwable caught) {
					caught.getMessage();
				  }

				  @Override
				  public void onSuccess(Dozent result) {
					libdozent.addItem(result.getNachname() + ", " +  result.getVorname());
					getDozenten();
				  } 	
			});
			}
			
			public void getNextListRaum() {
			verwaltungsSvc.getRaumById(shownSpe.getRaumId(), new AsyncCallback<Raum>() {
				@Override
				  public void onFailure (Throwable caught) {
					caught.getMessage();
				  }

				  @Override
				  public void onSuccess(Raum result) {
					libraum.addItem(result.getBezeichnung());
					getRaeume();
				  } 	
			});
			}
			
			public void getNextListZs() {
			verwaltungsSvc.getZeitslotById(shownSpe.getZeitslotId(), new AsyncCallback<Zeitslot>() {
				@Override
				  public void onFailure (Throwable caught) {
					caught.getMessage();
				  }

				  @Override
				  public void onSuccess(Zeitslot result) {
					libzeitslot.addItem(result.getWochentag() + ", " + result.getAnfangszeit() + ", " + result.getEndzeit());
					getZeitslots();
				  } 	
			});
		}
			
		
		public void clearFields(){
			libzeitslot.clear();
			libdozent.clear();
			liblehrveranstaltung.clear();
			libraum.clear();
			libstudiengang.clear();
			libsemesterverband.clear();
			libstudienhj.clear();
		}
		
		public void getStudiengaenge(){
			verwaltungsSvc.getAllStudiengaenge(new AsyncCallback<Vector<Studiengang>> () {
				@Override
				  public void onFailure (Throwable caught) {
					caught.getMessage();
				  }

				  @Override
				  public void onSuccess(Vector<Studiengang> studiengang) {
					  sgContainer = studiengang;
					  	for (Studiengang sg : studiengang){
					  		libstudiengang.addItem(sg.getBezeichnung(), String.valueOf(sg.getId()));
					  	}
					  	getNextListLv();
				  } 	
			}); 
			}
		
		public void getSemesterverbaende() {
			verwaltungsSvc.getAllSemesterverbaende(new AsyncCallback<Vector<Semesterverband>> () {
				@Override
				  public void onFailure (Throwable caught) {
					caught.getMessage();
				  }

				  @Override
				  public void onSuccess(Vector<Semesterverband> semesterverband) {
					  svContainer = semesterverband;
					  	for (Semesterverband sv : semesterverband){
					  		libsemesterverband.addItem(sv.getKuerzel() + " " + String.valueOf(sv.getSemester()));
					  	}
					  	getNextListSp();
				  } 	
			});
		}
		
		public void getStundenplaene() {
			verwaltungsSvc.getAllStundenplaene(new AsyncCallback<Vector<Stundenplan>> () {
				@Override
				  public void onFailure (Throwable caught) {
					caught.getMessage();
				  }

				  @Override
				  public void onSuccess(Vector<Stundenplan> stundenplan) {
					  spContainer = stundenplan;
					  	for (Stundenplan sp : stundenplan){
					  		libstudienhj.addItem(String.valueOf(sp.getStudienhalbjahr()));
					  	}
				  } 	
			});
		}
		
		public void getLehrveranstaltungen() {
			verwaltungsSvc.getAllLehrveranstaltungen(new AsyncCallback<Vector<Lehrveranstaltung>> () {
				@Override
				  public void onFailure (Throwable caught) {
					caught.getMessage();
				  }

				  @Override
				  public void onSuccess(Vector<Lehrveranstaltung> lehrveranstaltung) {
					  lvContainer = lehrveranstaltung;
					  	for (Lehrveranstaltung lv : lehrveranstaltung){
					  		liblehrveranstaltung.addItem(lv.getBezeichnung());
					  	}
					  	getNextListDozent();
				  } 	
			});
		}
		
		public void getDozenten() {
			verwaltungsSvc.getAllDozenten(new AsyncCallback<Vector<Dozent>> () {
				@Override
				  public void onFailure (Throwable caught) {
					caught.getMessage();
				  }

				  @Override
				  public void onSuccess(Vector<Dozent> dozent) {
					  dozentContainer = dozent;
					  	for (Dozent d : dozent){
					  		libdozent.addItem(d.getNachname() + ", " + d.getVorname());
					  	}
					  	getNextListRaum();
				  } 	
			});
		}
		
		public void getRaeume() {
			verwaltungsSvc.getAllRaeume(new AsyncCallback<Vector<Raum>> () {
				@Override
				  public void onFailure (Throwable caught) {
					caught.getMessage();
				  }

				  @Override
				  public void onSuccess(Vector<Raum> raum) {
					  raumContainer = raum;
					  	for (Raum r : raum){
					  		libraum.addItem(r.getBezeichnung());
					  	}
					  	getNextListZs();
				  } 	
			});
		}
		
		public void getZeitslots() {
			verwaltungsSvc.getAllZeitslots(new AsyncCallback<Vector<Zeitslot>> () {
				@Override
				  public void onFailure (Throwable caught) {
					caught.getMessage();
				  }

				  @Override
				  public void onSuccess(Vector<Zeitslot> zeitslot) {
					  zsContainer = zeitslot;
					  	for (Zeitslot z : zeitslot){
					  		libzeitslot.addItem(z.getWochentag() + ", " + z.getAnfangszeit() + ", " + z.getEndzeit());
					  	}
					  	getNextListSv();
				  } 	
			});
		}

}




