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
import de.hdm.stundenplansystem.shared.bo.Semesterverband;
import de.hdm.stundenplansystem.client.NavTreeViewModel;


/**
 * Formular für die Darstellung des selektierten Semesterverbands
 * 
 * @author Thies, Espich
 *
 */

public class SemesterverbandForm extends Content {
	
	private final HTML ueberschrift = new HTML ("<h2>Ãœbersicht der Semesterverbände<h2>");
	private final HTML ueberschriftAenderung = new HTML ("<h2>Semesterverband bearbeiten<h2>");
	
	  final Label lbjahrgang = new Label ("Jahrgang"); 
	  final Label lbstudiengang = new Label ("Studiengang");
	  final Label lbsemester = new Label ("Semester");
	  final Label lbanzahl = new Label ("Anzahl der Studierenden");
	  final TextBox tbjahrgang = new TextBox ();
	  final TextBox tbstudiengang = new TextBox();
	  final TextBox tbsemester = new TextBox ();
	  final TextBox tbanzahl = new TextBox ();
	  final Button bearbeiten = new Button ("Semesterverband bearbeiten");
	  final Button loeschen = new Button ("Semesterverband löschen");
	  final Button speichern = new Button ("Änderungen speichern");

	  final VerwaltungsklasseAsync verwaltungsSvc = GWT.create(Verwaltungsklasse.class);
	  Semesterverband shownSv = null;
	  NavTreeViewModel tvm = null;
	  
	  public SemesterverbandForm() {
		  Grid svGrid = new Grid (6, 2);
		    this.add(ueberschrift);
			this.add(svGrid);
		  
			Label lbjahrgang = new Label("Jahrgang");
			svGrid.setWidget(0, 0, lbjahrgang);
			svGrid.setWidget(0, 1, tbjahrgang);

			Label lbstudiengang = new Label("Studiengang");
			svGrid.setWidget(1, 0, lbstudiengang);
			svGrid.setWidget(1, 1, tbstudiengang);
			
			Label lbsemester = new Label("Semester");
			svGrid.setWidget(2, 0, lbsemester);
			svGrid.setWidget(2, 1, tbsemester);

			Label lbanzahl = new Label("Anzahl der Studierenden");
			svGrid.setWidget(3, 0, lbanzahl);
			svGrid.setWidget(3, 1, tbanzahl);
			
			Label lbfunktionen = new Label ("Funktionen");
			svGrid.setWidget(4, 0, bearbeiten);
			svGrid.setWidget(5, 0, loeschen);
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
					  
					  if (tbjahrgang.getText().isEmpty());
					  if (tbanzahl.getText().isEmpty());
					  if (tbstudiengang.getText().isEmpty());
					  if (tbsemester.getText().isEmpty()); 
					  { allFilled = false;
					  Window.alert ("Bitte fÃ¼llen Sie alle Felder aus."); }
					  
					  if (allFilled == true) { 
						  shownSv.setJahrgang(tbjahrgang.getText().trim());
					      shownSv.setStudiengangId(tbstudiengang.getVisibleLength());
						  shownSv.setStudierendenAnzahl(tbanzahl.getVisibleLength());
						  shownSv.setSemester(tbsemester.getVisibleLength());
	
						  verwaltungsSvc.changeSemsterverband(shownSv, new  AsyncCallback<Semesterverband>() {

							  @Override
							  public void onFailure (Throwable caught) {
								  Window.alert("Der Semesterverband konnte nicht bearbeitet werden.");
							  }

							  @Override
							  public void onSuccess(Semesterverband result) {	
								  Window.alert ("Erfolgreich gespeichert.");
								  tbjahrgang.setText("");
								  tbstudiengang.setText("");
								  tbsemester.setVisibleLength(result.getSemester());
								  tbanzahl.setVisibleLength(result.getStudierendenAnzahl());
								  tvm.updateSemesterverband(shownSv);
							  } 	
							});
					  }
				  }
				  }); 
			
			loeschen.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event){
					verwaltungsSvc.deleteSemesterverband(shownSv, new AsyncCallback<Void>() {
						  @Override
						  public void onFailure (Throwable caught) {
							  Window.alert("Der Studiengang konnte nicht gelöscht werden." +
							  		"Er ist in ein oder mehreren Stundenplaneinträgen eingetragen");
						  }

						  @Override
						  public void onSuccess(Void result) {
							  tvm.deleteSemesterverband(shownSv);
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
			/*verwaltungsSvc.getSemesterverbandById(svId, new AsyncCallback<Semesterverband>(){
				@Override
				public void onFailure(Throwable caught) {
				}
				
				@Override
				public void onSuccess(Semesterverband result) {
					if (result != null) {
						setSelected(result);
					}
				}
			});*/
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
			//tbstudiengang.setText(shownSv.getBezeichnung());
			tbsemester.setVisibleLength(shownSv.getSemester());
		    tbanzahl.setVisibleLength(shownSv.getStudierendenAnzahl());
		}
		
		public void clearFields(){
			  tbjahrgang.setText("");
			  tbstudiengang.setText("");
			  tbsemester.setText("");
			  tbanzahl.setText("");
		}
		
		  public void showWidget(){
			  	 this.add(ueberschriftAenderung);
				  this.add(lbjahrgang);
				  this.add(tbjahrgang);
				  this.add(lbstudiengang);
				  this.add(tbstudiengang);
				  this.add(lbsemester);
				  this.add(tbsemester);
				  this.add(lbanzahl);
				  this.add(tbanzahl);
				  this.add(speichern);
			  }
}
	