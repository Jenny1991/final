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
import com.google.gwt.user.client.ui.RadioButton;

import de.hdm.stundenplansystem.client.Content;
import de.hdm.stundenplansystem.shared.*;
import de.hdm.stundenplansystem.shared.bo.Studiengang;
import de.hdm.stundenplansystem.shared.bo.Stundenplan;
import de.hdm.stundenplansystem.shared.bo.Semesterverband;
import de.hdm.stundenplansystem.shared.report.*;

public class ReportStundenplan extends Content {
	/**
	 * Aufbau der Seite, um den Stundenplan für Studenten anzuzeigen
	 */
	
	final HTML ueberschrift = new HTML ("<h2>Stundenplan für Studenten</h2>");
	final HTML feld = new HTML();
	final FlexTable flexSv = new FlexTable();
	final ListBox libstundenplan = new ListBox();
	final ListBox libsemverband = new ListBox();
	final Button anzeigen = new Button("Stundenplan anzeigen");

	final VerwaltungsklasseAsync verwaltungsSvc = GWT.create(Verwaltungsklasse.class);
	final ReportGeneratorAsync reportSvc = GWT.create(ReportGenerator.class);

	private NavTreeViewModel tvm;
	Studiengang sg;
	Integer sv;
	
	public void onLoad() {
		
		this.add(ueberschrift);	
		this.add(libsemverband);
		this.add(libstundenplan);
		this.add(anzeigen);
		setTvm(tvm);
		
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
		
		  libsemverband.clear();
		  verwaltungsSvc.getAllSemesterverbaende(new AsyncCallback<Vector<Semesterverband>>() {
			  public void onFailure(Throwable T){
				  
			  }
			  
			  public void onSuccess(Vector<Semesterverband> semesterverband){
			  	for (Semesterverband sv : semesterverband){
			  		libsemverband.addItem(sv.getJahrgang(), String.valueOf(sv.getId()));
//			  		libsemverband.addItem(String.valueOf(sv.getSemester())); 
//			  		libsemverband.addItem(sg.getBezeichnung(), String.valueOf(sg.getId()));
			  	}
		  }
		  });
		  
		  anzeigen.addClickHandler(new ClickHandler() {
			  public void onClick(ClickEvent event) {				 
				  	reportSvc.setSemesterverband(sv, new AsyncCallback<Void>(){
				  
					  @Override
					  public void onFailure (Throwable caught) {
					  }

					  @Override
					  public void onSuccess(Void result) {
				  		loadData();
					  } 	
					}); 
			  }
		  });  
		  }
		  
	public void loadData(){
		this.add(flexSv);
		
		reportSvc.createStundenplanSemesterverbandReport(sv, new AsyncCallback<StundenplanSemesterverbandReport>() {
		
		public void onSuccess(StundenplanSemesterverbandReport result){				
			  }
			
			  @Override
			  public void onFailure (Throwable caught) {
			  }
		
		});
		
}

	public void setTvm(NavTreeViewModel tvm) {
		this.tvm = tvm;
	}
	
}



