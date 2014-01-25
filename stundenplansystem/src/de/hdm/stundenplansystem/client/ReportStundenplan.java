package de.hdm.stundenplansystem.client;

import java.util.Vector;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;

import de.hdm.stundenplansystem.client.Content;
import de.hdm.stundenplansystem.shared.*;
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
	
	HTML ueberschrift = new HTML ("<h2>Stundenplan für Studenten</h2>");
	final Label lbstundenplan = new Label ("Stundenplan:");
	final Label lbsemverband = new Label ("Semesterverband:");
	final Label lbstudiengang = new Label ("Studiengang:");
	final ListBox libstundenplan = new ListBox();
	final ListBox libsemverband = new ListBox();
	final ListBox libstudiengang = new ListBox();
	final Button anzeigen = new Button("Stundenplan anzeigen");
	final ScrollPanel neuesPanel = new ScrollPanel(); 
	HTML feld = new HTML ();

	final VerwaltungsklasseAsync verwaltungsSvc = GWT.create(Verwaltungsklasse.class);
	final ReportGeneratorAsync reportSvc = GWT.create(ReportGenerator.class);

	private NavTreeViewModel tvm;
	Studiengang sg;
	Stundenplansystem stundenplansystem = null;
	Integer sv;
	String test;
	
	public void onLoad() {
		
		this.add(ueberschrift);
		this.add(lbstudiengang);
		this.add(libstudiengang);
		this.add(lbsemverband);
		this.add(libsemverband);
		this.add(lbstundenplan);
		this.add(libstundenplan);
		this.add(anzeigen);
		this.add(neuesPanel);
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
		  
		  libstudiengang.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				getSemverband();	
			}
		  });
		  
		  libsemverband.addChangeHandler(new ChangeHandler() {
				
				@Override
				public void onChange(ChangeEvent event) {
					getStundenplan();	
				}
			});
		  
		  anzeigen.addClickHandler(new ClickHandler() {			  
			  public void onClick(ClickEvent event) {	
			
				  reportSvc.createStundenplanSemesterverbandReport(svContainer.elementAt(libsemverband.getSelectedIndex()).getId(), spContainer.elementAt(libstundenplan.getSelectedIndex()).getId(), new AsyncCallback<StundenplanSemesterverbandReport>() {
		
					  public void onSuccess(StundenplanSemesterverbandReport result){
						  
					      HTMLReportWriter writer = new HTMLReportWriter();
					      writer.process(result);
					      test = writer.getReportText();
				          neuesPanel.add(new HTML(test));							  
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
		  		libsemverband.addItem(sv.getJahrgang() + ", " + String.valueOf(sv.getSemester())); 
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


