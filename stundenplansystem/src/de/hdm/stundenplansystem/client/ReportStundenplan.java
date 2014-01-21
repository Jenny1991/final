package de.hdm.stundenplansystem.client;

import java.util.List;
import java.util.Vector;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ButtonBase;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.stundenplansystem.client.Content;
import de.hdm.stundenplansystem.shared.*;
import de.hdm.stundenplansystem.shared.bo.Dozent;
import de.hdm.stundenplansystem.shared.bo.Studiengang;
import de.hdm.stundenplansystem.shared.bo.Stundenplan;
import de.hdm.stundenplansystem.shared.bo.Semesterverband;
import de.hdm.stundenplansystem.shared.report.*;

public class ReportStundenplan extends Content {
	
	Vector<Semesterverband> svContainer = null;
	Vector<Stundenplan> spContainer = null;
	Vector<Studiengang> sgContainer = null;


	/**
	 * Aufbau der Seite, um den Stundenplan für Studenten anzuzeigen
	 */
	
	final HTML ueberschrift = new HTML ("<h2>Stundenplan für Studenten</h2>");
	final FlexTable flexSv = new FlexTable();
	final ListBox libstundenplan = new ListBox();
	final ListBox libsemverband = new ListBox();
	final ListBox libstudiengang = new ListBox();
	final Button anzeigen = new Button("Stundenplan anzeigen");
	final VerticalPanel neuesPanel = new VerticalPanel(); 

	final VerwaltungsklasseAsync verwaltungsSvc = GWT.create(Verwaltungsklasse.class);
	final ReportGeneratorAsync reportSvc = GWT.create(ReportGenerator.class);

	private NavTreeViewModel tvm;
	Studiengang sg;
	Integer sv;
	
	public void onLoad() {
		
		this.add(ueberschrift);
		this.add(libstudiengang);
		this.add(libsemverband);
		this.add(libstundenplan);
		this.add(anzeigen);
		setTvm(tvm);
		
		  libstudiengang.clear();
		  verwaltungsSvc.getAllStudiengaenge(new AsyncCallback<Vector<Studiengang>>() {
			  public void onFailure(Throwable T){
				  
			  }
			  
			  public void onSuccess(Vector<Studiengang> studiengang){
				sgContainer = studiengang;
			  	for (Studiengang sg : studiengang){
			  		libstudiengang.addItem(sg.getBezeichnung(), String.valueOf(sg.getId()));
			  	}
			  	getSemverband();
		  }
	});

		  anzeigen.addClickHandler(new ClickHandler() {
			  public void onClick(ClickEvent event) {				 

				  reportSvc.createStundenplanSemesterverbandReport(svContainer.elementAt(libsemverband.getSelectedIndex()).getId(), spContainer.elementAt(libstundenplan.getSelectedIndex()).getId(), new AsyncCallback<String>() {
		
					  public void onSuccess(String result){

						  HTML plan = new HTML(result);
						  neuesPanel.add(plan);					  
						  
					  }
			
					  @Override
					  public void onFailure (Throwable caught) {
						  caught.getMessage();
					  }
				  });
			  }
		  });
		  }
		  
	public void setTvm(NavTreeViewModel tvm) {
		this.tvm = tvm;
	}
	  public void getSemverband(){
	  libsemverband.clear();
	  verwaltungsSvc.getSemsterverbaendeByStudiengang(sgContainer.elementAt(libstudiengang.getSelectedIndex()).getId(), new AsyncCallback<Vector<Semesterverband>>() {
		  public void onFailure(Throwable T){
			  
		  }
		  
		  public void onSuccess(Vector<Semesterverband> semesterverband){
			svContainer = semesterverband;
		  	for (Semesterverband sv : semesterverband){
		  		libsemverband.addItem(sv.getJahrgang(), String.valueOf(sv.getId()));
//		  		libsemverband.addItem(String.valueOf(sv.getSemester())); 
		  	}
		  	getStundenplan();
	  }
	  }); 
	  }


		public void getStundenplan(){  
		libstundenplan.clear();
		  verwaltungsSvc.getStundenplaeneBySemesterverband(svContainer.elementAt(libsemverband.getSelectedIndex()).getId(), new AsyncCallback<Vector<Stundenplan>>() {
			  public void onFailure(Throwable T){
				  
			  }
			  
			  public void onSuccess(Vector<Stundenplan> stundenplaene){
				spContainer = stundenplaene;
			  	for (Stundenplan sp : stundenplaene){
			  		libstundenplan.addItem(sp.getStudienhalbjahr(), String.valueOf(sp.getId()));
			  	}
		  }
		  });
		}
}


