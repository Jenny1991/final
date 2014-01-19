package de.hdm.stundenplansystem.client;

/**
 * @author Espich
 * 
 */

import java.util.Vector;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.FlexTable;

import de.hdm.stundenplansystem.shared.*;
import de.hdm.stundenplansystem.shared.bo.Raum;
import de.hdm.stundenplansystem.shared.bo.Stundenplaneintrag;
import de.hdm.stundenplansystem.shared.report.RaumbelegungsReport;
import de.hdm.stundenplansystem.client.*;

public class ReportRaum extends Content {

	final FlexTable flexRaum = new FlexTable();
	final ListBox libraum = new ListBox();
	final VerwaltungsklasseAsync verwaltungsSvc = GWT.create(Verwaltungsklasse.class);
	final ReportGeneratorAsync reportSvc = GWT.create(ReportGenerator.class);
	final HTML ueberschrift = new HTML ("<h2>Raumbelegungsplan</h2>");
	Raum r;
	private NavTreeViewModel tvm;
	
	public void onLoad(){
		
		this.add(ueberschrift);
		this.add(libraum);		
		setTvm(tvm);
		
		libraum.clear();			  
		  verwaltungsSvc.getAllRaeume(new AsyncCallback<Vector<Raum>>() {
			  public void onFailure(Throwable T){ 
					Window.alert("Es sind keine Räume in der Datenbank vorhanden.");
			  }
			  
			  public void onSuccess(Vector<Raum> raum){
			  	for (Raum r : raum){
			  		libraum.addItem(r.getBezeichnung(), String.valueOf(r.getId()));
			  	}
		  }
		  });
//		getData();
	}

	public void setTvm(NavTreeViewModel tvm) {
		this.tvm = tvm;
	}
	
//		public void getData(){	
//			this.add(flexRaum);
//			
//			reportSvc.createRaumbelungsReport(r, new AsyncCallback<RaumbelegungsReport>() {
//
//				public void onSuccess(RaumbelegungsReport result){
//					if (result != null){
//				
//						flexRaum.setText(0, 0, "Zeit");
//						flexRaum.setText(1, 0, "08:15 - 09:45 Uhr");
//						flexRaum.setText(2, 0, "10:00 - 11:30 Uhr");
//						flexRaum.setText(3, 0, "11:45 - 13:15 Uhr");
//						flexRaum.setText(4, 0, "13:25 - 14:15 Uhr");
//						flexRaum.setText(5, 0, "14:15 - 15:45 Uhr");
//						flexRaum.setText(6, 0, "16:00 - 17:30 Uhr");
//						flexRaum.setText(7, 0, "17:45 - 19:15 Uhr");
//						flexRaum.setText(0, 1, "Montag");
//						flexRaum.setText(0, 2, "Dienstag");
//						flexRaum.setText(0, 3, "Mittowch");
//						flexRaum.setText(0, 4, "Donnerstag");
//						flexRaum.setText(0, 5, "Freitag");
//						flexRaum.setText(0, 6, "Samstag");
//				
//					int row = 1; 
//						for (int i=0; i<result..size(); i++) {
//							flexRaum.setText(row, 1, String.valueOf(result.get(i).getLehrveranstaltungId())); 
//						}
//					}
//				}
//
//				@Override
//				public void onFailure(Throwable caught) {					
//				}
//			});
//		} 

}