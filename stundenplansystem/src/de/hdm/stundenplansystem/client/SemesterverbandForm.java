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
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import de.hdm.stundenplansystem.shared.*;
import de.hdm.stundenplansystem.shared.bo.Lehrveranstaltung;
import de.hdm.stundenplansystem.shared.bo.Semesterverband;
import de.hdm.stundenplansystem.shared.bo.Studiengang;
import de.hdm.stundenplansystem.client.NavTreeViewModel;


/**
 * Formular fÃ¼r die Darstellung des selektierten Semesterverbands
 * 
 * @author Thies, Espich
 *
 */

public class SemesterverbandForm extends Content {
	
	private final HTML ueberschriftAenderung = new HTML ("<h2>Semesterverband verwalten<h2>");

	  final TextBox tbjahrgang = new TextBox ();
	  final ListBox libstudiengang = new ListBox();
	  final TextBox tbsemester = new TextBox ();
	  final TextBox tbanzahl = new TextBox ();
	  final Button loeschen = new Button ("Semesterverband löschen");
	  final Button speichern = new Button ("Änderungen speichern");
	  final VerwaltungsklasseAsync verwaltungsSvc = GWT.create(Verwaltungsklasse.class);
	  
	  Integer id;
	  Semesterverband shownSv = null;
	  NavTreeViewModel tvm = null;
	  
	  public SemesterverbandForm() {
		  Grid svGrid = new Grid (2, 6);
		    this.add(ueberschriftAenderung);
			this.add(svGrid);
		  
			Label lbjahrgang = new Label("Jahrgang");
			svGrid.setWidget(0, 0, lbjahrgang);
			svGrid.setWidget(1, 0, tbjahrgang);

			Label lbstudiengang = new Label("Studiengang");
			svGrid.setWidget(0, 1, lbstudiengang);
			svGrid.setWidget(1, 1, libstudiengang);
			
			Label lbsemester = new Label("Semester");
			svGrid.setWidget(0, 2, lbsemester);
			svGrid.setWidget(1, 2, tbsemester);

			Label lbanzahl = new Label("Anzahl der Studierenden");
			svGrid.setWidget(0, 3, lbanzahl);
			svGrid.setWidget(1, 3, tbanzahl);
			
			Label lbfunktionen = new Label ("Funktionen");
			svGrid.setWidget(0, 4, lbfunktionen);
			svGrid.setWidget(1, 4, speichern);
			speichern.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					changeSelectedSv();
				}
			});
			svGrid.setWidget(1, 5, loeschen);
			loeschen.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					deleteSelectedSv();
				}
			});
			setTvm(tvm);
			
			  libstudiengang.clear();			  
			  verwaltungsSvc.getAllStudiengaenge(new AsyncCallback<Vector<Studiengang>>() {
				  public void onFailure(Throwable T){
					  
				  }
				  
				  public void onSuccess(Vector<Studiengang> studiengaenge){
				  	for (Studiengang sg : studiengaenge){
				  		libstudiengang.addItem(sg.getBezeichnung(), String.valueOf(sg.getId()));
				  	}
			  }
			  });
			}
			
		public void getData() {		
			verwaltungsSvc.getSemesterverbandById(id, new AsyncCallback<Semesterverband>(){
				@Override
				public void onFailure(Throwable caught) {
				}
				
				@Override
				public void onSuccess(Semesterverband result) {
					if (result != null) {
						setSelected(result);
					}
				}
			});
		  }

			public void changeSelectedSv(){
			
				  boolean allFilled = true;
				  
				  if (tbjahrgang.getText().isEmpty() 
						  || tbanzahl.getText().isEmpty() 
						  || tbsemester.getText().isEmpty()) 
				  { allFilled = false;
				  Window.alert ("Bitte füllen Sie alle Felder aus."); }
				  
				  if (allFilled == true) { 
					  shownSv.setJahrgang(tbjahrgang.getText().trim());
					  shownSv.setStudiengangId(Integer.valueOf(libstudiengang.getValue(libstudiengang.getTabIndex())));
					  shownSv.setStudierendenAnzahl(Integer.valueOf(tbanzahl.getValue()));
					  shownSv.setSemester(Integer.valueOf(tbsemester.getValue().trim()));
	
					  verwaltungsSvc.changeSemsterverband(shownSv, new AsyncCallback<Void>() {

						  @Override
						  public void onFailure (Throwable caught) {
							  Window.alert("Der Semesterverband konnte nicht angelegt werden.");
						  }

						  @Override
						  public void onSuccess(Void result) {
							  tvm.updateSemesterverband(shownSv);
							  Window.alert ("Erfolgreich gespeichert.");
						  } 	
						}); 
				  }
			  }
			
			public void deleteSelectedSv(){
					verwaltungsSvc.deleteSemesterverband(shownSv, new AsyncCallback<Boolean>() {
						  @Override
						  public void onFailure (Throwable caught) {
							  Window.alert("Der Studiengang konnte nicht gelÃ¶scht werden." +
							  		"Er ist in ein oder mehreren StundenplaneintrÃ¤gen eingetragen");
						  }

						  @Override
						  public void onSuccess(Boolean result) {
							  tvm.deleteSemesterverband(shownSv);
							  Window.alert ("Erfolgreich gelöscht.");
						  } 	
					});
				this.clearFields();
			  }
		
		public void setTvm(NavTreeViewModel tvm) {
			this.tvm = tvm;
		}
		
		public void setSelected(Semesterverband sv){
			if (sv != null) {
				shownSv = sv;
				setFields();
			} else {
				clearFields();
			}
		}
		
		public void setFields(){
			tbjahrgang.setText(shownSv.getJahrgang());
			tbsemester.setVisibleLength(shownSv.getSemester());
		    tbanzahl.setVisibleLength(shownSv.getStudierendenAnzahl());
		}
		
		public void clearFields(){
			  tbjahrgang.setText("");
			  tbsemester.setText("");
			  tbanzahl.setText("");
		}
}
	