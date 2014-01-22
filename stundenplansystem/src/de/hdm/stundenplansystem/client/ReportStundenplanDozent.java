package de.hdm.stundenplansystem.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;

import de.hdm.stundenplansystem.shared.ReportGenerator;
import de.hdm.stundenplansystem.shared.ReportGeneratorAsync;
import de.hdm.stundenplansystem.shared.Verwaltungsklasse;
import de.hdm.stundenplansystem.shared.VerwaltungsklasseAsync;


public class ReportStundenplanDozent extends Content  {
	
	final HTML ueberschrift = new HTML ("<h2>Stundenplan f�r Dozenten</h2>");
	final FlexTable tabelleSp = new FlexTable();
	final VerwaltungsklasseAsync verwaltungsSvc = GWT.create(Verwaltungsklasse.class);
	final ReportGeneratorAsync reportSvc = GWT.create(ReportGenerator.class);
	
	private NavTreeViewModel tvm;

	public void setTvm(NavTreeViewModel tvm) {
		this.tvm = tvm;
	}
	
	public void onLoad(){
		this.add(ueberschrift);
		setTvm(tvm);
	}
	
	protected void append(String text) {
	    HTML content = new HTML(text);
	    content.setStylePrimaryName("stundenplansystem-simpletext");
	    this.add(content);
	  }
	
}
