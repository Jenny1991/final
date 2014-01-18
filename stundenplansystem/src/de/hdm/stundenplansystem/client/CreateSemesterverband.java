package de.hdm.stundenplansystem.client;

import java.util.Vector;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ListBox;

import de.hdm.stundenplansystem.shared.*;
import de.hdm.stundenplansystem.shared.bo.Studiengang;
import de.hdm.stundenplansystem.shared.bo.Stundenplan;
import de.hdm.stundenplansystem.shared.bo.Semesterverband;
import de.hdm.stundenplansystem.client.NavTreeViewModel;

	/**
	 * Hier wird ein neuer Semesterverband angelegt.
	 * 
	 * @author Thies, Espich
	 * 
	 */

	public class CreateSemesterverband extends Content {
		
		  /**
		   * Jede Klasse enthï¿½t eine ï¿½berschrift, die definiert, was der User machen kann.
		   */
		private final HTML ueberschrift = new HTML ("<h2>Neuen Semesterverband anlegen<h2>");

		  /**
		   * Unter der ï¿½berschrift trï¿½gt der User die Daten des neuen Semesterverbands ein. 
		   */
		  final Label lbjahrgang = new Label ("Jahrgang"); 
		  final Label lbstudiengang = new Label ("Studiengang");
		  final Label lbstundenplan = new Label ("Stundenplan");
		  final Label lbsemester = new Label ("Semster");
		  final Label lbanzahl = new Label ("Anzahl");
		  final TextBox tbjahrgang = new TextBox ();
		  final ListBox libstudiengang = new ListBox();
		  final ListBox libstundenplan = new ListBox();
		  final TextBox tbsemester = new TextBox ();
		  final TextBox tbanzahl = new TextBox ();
		  final Button speichern = new Button ("speichern");
		  
		  final VerwaltungsklasseAsync verwaltungsSvc = GWT.create(Verwaltungsklasse.class);
		  NavTreeViewModel tvm = null;

		  /**
		  * Anordnen der Buttons und Labels auf den Panels
		  */
		  public void onLoad () {
			  
			  this.clear();
			  this.add(ueberschrift);			  
			  this.add(lbjahrgang);
			  this.add(tbjahrgang);
			  this.add(lbstudiengang);
			  this.add(libstudiengang);
			  this.add(lbstundenplan);
			  this.add(libstundenplan);
			  this.add(lbsemester);
			  this.add(tbsemester);
			  this.add(lbanzahl);
			  this.add(tbanzahl);
			  this.add(speichern);
			  
			  
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
			  
			  libstundenplan.clear();
			  verwaltungsSvc.getAllStundenplaene(new AsyncCallback<Vector<Stundenplan>>() {
				  public void onFailure(Throwable T){
					  
				  }
				  
				  public void onSuccess(Vector<Stundenplan> stundenplaene){
				  	for (Stundenplan sp : stundenplaene){
				  		libstundenplan.addItem(sp.getStudienhalbjahr(), String.valueOf(sp.getId()));
				  	}
			  }
			  });
			  
			  setTvm(tvm);
			  
				  speichern.addClickHandler(new ClickHandler() {
					  public void onClick(ClickEvent event) {

						  boolean allFilled = true;
						  
						  if (tbjahrgang.getText().isEmpty() || tbanzahl.getText().isEmpty() || tbsemester.getText().isEmpty()) 
						  { allFilled = false;
						  Window.alert ("Bitte f�llen Sie alle Felder aus."); }
						  
						  if (allFilled == true) { 
							  final String jahrgang = tbjahrgang.getText().trim();
							  final int studiengangId  = Integer.valueOf(libstudiengang.getValue(libstudiengang.getTabIndex()));
							  final int stundenplanId = Integer.valueOf(libstundenplan.getValue(libstundenplan.getTabIndex()));
							  final int studierendenAnzahl = tbanzahl.getVisibleLength();
							  final int semester = Integer.valueOf(tbsemester.getText().trim());
			
							  verwaltungsSvc.createSemesterverband(stundenplanId, studiengangId, semester, studierendenAnzahl, jahrgang, new AsyncCallback<Semesterverband>() {

								  @Override
								  public void onFailure (Throwable caught) {
									  Window.alert("Der Semesterverband konnte nicht angelegt werden.");
								  }

								  @Override
								  public void onSuccess(Semesterverband result) {
									  
//									  tbjahrgang.setText(result.getJahrgang);
//									  tbstudiengang.setText(result.getBezeichnung);
//									  tbsemester.setVisibleLength(result.getSemester);
//									  tbanzahl.setVisibleLength(result.getStudierendenAnzahl);
									  tvm.addSemesterverband(result);
									  Window.alert ("Erfolgreich gespeichert.");
								  } 	
								}); 
						  }
					  }
					  });
		  }
			public void setTvm(NavTreeViewModel tvm) {
				this.tvm = tvm;
			}
	}  
	
