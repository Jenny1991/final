package de.hdm.stundenplansystem.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Button;

import de.hdm.stundenplansystem.shared.*;
import de.hdm.stundenplansystem.shared.bo.Lehrveranstaltung;
import de.hdm.stundenplansystem.client.NavTreeViewModel;

	/**
	 * Hier wird eine neue Lehrveranstaltung angelegt.
	 * 
	 * @author Thies, Espich
	 * 
	 */

	public class CreateLehrveranstaltung extends Content {

		  /**
		   * Jede Klasse enthï¿½t eine ï¿½berschrift, die definiert, was der User machen kann.
		   */
		private final HTML ueberschrift = new HTML ("<h2>Neue Lehrveranstaltung anlegen<h2>");

		   /**
		   * Unter der ï¿½berschrift trï¿½gt der User die Daten der neuen Lehrveranstaltung ein. 
		   */
		  final Label lbbezeichnung = new Label ("Bezeichnung"); 
		  final Label lbsemester = new Label ("Semester");
		  final Label lbumfang = new Label ("Umfang");
		  final TextBox tbbezeichnung = new TextBox ();
		  final TextBox tbsemester = new TextBox();
		  final TextBox tbumfang = new TextBox (); 	  
		  final Button speichern = new Button ("speichern");
		  
		  final VerwaltungsklasseAsync verwaltungsSvc = GWT.create(Verwaltungsklasse.class);
		  NavTreeViewModel tvm = null;
		  
		  /**
		  * Anordnen der Buttons und Labels auf den Panels
		  */
		  public void onLoad () {
			  		
			  	  this.add(ueberschrift);
				  this.add(lbbezeichnung);
				  this.add(tbbezeichnung);
				  this.add(lbsemester);
				  this.add(tbsemester);
				  this.add(lbumfang);
				  this.add(tbumfang);
				  this.add(speichern);
				  
				  setTvm(tvm);				  
				  
				  speichern.addClickHandler(new ClickHandler() {
					  public void onClick(ClickEvent event) {

						  boolean allFilled = true;
						  
						  if (tbbezeichnung.getValue().isEmpty()
								  || tbsemester.getValue().isEmpty()
								  || tbumfang.getValue().isEmpty())
						  {	allFilled = false;
						  Window.alert("Bitte f�llen Sie alle Felder aus."); }
						 
						  if (allFilled == true) {
							  final String bezeichnung = tbbezeichnung.getValue().trim();
							  final int umfang = Integer.valueOf(tbumfang.getText());
							  final int semester = Integer.valueOf(tbsemester.getText());
						
							  verwaltungsSvc.createLehrveranstaltung(bezeichnung, semester, umfang, new AsyncCallback <Lehrveranstaltung>() {

								  @Override
								  public void onFailure (Throwable caught) {
									  Window.alert("Die Lehrveranstaltung konnte nicht angelegt werden.");
								  }

								  @Override
								  public void onSuccess(Lehrveranstaltung result) {
									  tbbezeichnung.setText("");
									  tbumfang.setText("");
									  tbsemester.setText("");
								  	  Window.alert ("Erfolgreich gespeichert.");
									  tvm.addLehrveranstaltung(result);
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
