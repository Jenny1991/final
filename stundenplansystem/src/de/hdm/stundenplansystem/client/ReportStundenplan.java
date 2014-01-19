package de.hdm.stundenplansystem.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RadioButton;

import de.hdm.stundenplansystem.client.Content;
import de.hdm.stundenplansystem.shared.*;



public class ReportStundenplan extends Content {
	/**
	 * Aufbau der Seite, um den Raum anzuzeigen, zu lÃ¶schen und zu bearbeiten
	 */
	
	final HTML ueberschrift = new HTML ("<h2>Stundenplan f�r Dozenten oder Studenten</h2>");
	final FlexTable tabelleSp = new FlexTable();
	final RadioButton rbd = new RadioButton("Dozenten", "Dozent");
	final RadioButton rbs = new RadioButton("Studenten", "Studenten");
	final VerwaltungsklasseAsync verwaltungsSvc = GWT.create(Verwaltungsklasse.class);
	final ReportGeneratorAsync reportSvc = GWT.create(ReportGenerator.class);
//Test
	private NavTreeViewModel tvm;
	
	public void onLoad() {
		
		this.add(ueberschrift);		
		this.add(rbd);
		this.add(rbs);		
		setTvm(tvm);
		
	}
	
	

	public void setTvm(NavTreeViewModel tvm) {
		this.tvm = tvm;
	}
	
}



