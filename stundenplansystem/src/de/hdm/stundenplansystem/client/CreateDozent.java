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
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.stundenplansystem.shared.*;
import de.hdm.stundenplansystem.shared.bo.Dozent;
import de.hdm.stundenplansystem.client.NavTreeViewModel;

/**
 * Hier wird ein neuer Dozent angelegt.
 * 
 * @author Thies, Espich
 * 
 */

public class CreateDozent extends VerticalPanel {
	
	/**
    * Jede Klasse enthï¿½t eine ï¿½berschrift, die definiert, was der User machen kann. 
    */
	private final HTML ueberschrift = new HTML ("<h2>Neuen Dozenten anlegen<h2>");

	  /**
	   * Unter der ï¿½berschrift trï¿½gt der User die Daten des neuen Dozenten ein. 
	   */
	  Label lbvorname = new Label ("Vorname"); 
	  final Label lbnachname = new Label ("Nachname");
	  final TextBox tbvorname = new TextBox ();
	  final TextBox tbnachname = new TextBox ();
	  final Button speichern = new Button ("speichern");
	  
	  final VerwaltungsklasseAsync verwaltungsSvc = GWT.create(Verwaltungsklasse.class);
	  NavTreeViewModel tvm = null;
	  
	  /**
	  * Anordnen der Buttons und Labels auf den Panels
	  * 
	  * 
	  */
	 
	  
	 public void onLoad () {

		  this.add(ueberschrift);
		  this.add(lbnachname);
		  this.add(tbnachname);
		  this.add(lbvorname);
		  this.add(tbvorname);
		  this.add(speichern);	  
		  
		  setTvm(tvm);
		  //TEST
			  
				  speichern.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {

					  boolean allFilled = true;
				  
					  if (tbnachname.getValue().isEmpty() 
							  ||tbvorname.getValue().isEmpty()) {	
						  allFilled = false;
					  Window.alert ("Bitte fÃ¼llen Sie alle Felder aus."); } 
					  
					  if (allFilled == true) {	
						  String vorname = tbvorname.getValue().trim();
						  String nachname = tbnachname.getValue().trim();
				
						 verwaltungsSvc.createDozent(vorname, nachname, new AsyncCallback<Dozent>() {

							  @Override
							  public void onFailure (Throwable caught) {
								  Window.alert("Der Dozent konnte nicht angelegt werden.");
							  }

							  @Override
							  public void onSuccess(Dozent result) {
								  Window.alert ("Erfolgreich gespeichert.");
								  tvm.addDozent(result);
							  } 	
							});
					  }
				  }
				  });	  
	  }
		public void setTvm(NavTreeViewModel tvm) {
			this.tvm = tvm;
			this.clear();
		}
	}