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
import de.hdm.stundenplansystem.shared.bo.Studiengang;
import de.hdm.stundenplansystem.client.NavTreeViewModel;

/**
 * Hier wird ein neuer Studiengang angelegt.
 * 
 * @author Thies, Espich
 * 
 */

public class CreateStudiengang extends Content{
	
	  /**
	   * Jede Klasse enthï¿½t eine ï¿½berschrift, die definiert, was der User machen kann.
		   */
	private final HTML ueberschrift = new HTML ("<h2>Neuen Studiengang anlegen<h2>");

	  /**
	   * Unter der ï¿½berschrift trï¿½gt der User die Daten des neuen Studiengangs ein. 
	   */
	  final Label lbbezeichnung = new Label ("Bezeichnung"); 
	  final TextBox tbbezeichnung = new TextBox ();
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
		  this.add(speichern);
		  	
		  speichern.addClickHandler(new ClickHandler() {
			  public void onClick(ClickEvent event) {

				  boolean allFilled = true;
				  
				  	if (tbbezeichnung.getValue().isEmpty())  
				  	{ allFilled = false;
					  Window.alert ("Bitte fÃ¼llen Sie alle Felder aus."); }
				  	
				  	if (allFilled == true){
				  		final String bezeichnung = tbbezeichnung.getValue().trim();
				  
					  verwaltungsSvc.createStudiengang(bezeichnung, new AsyncCallback<Studiengang>() {

						  @Override
						  public void onFailure (Throwable caught) {
							  Window.alert("Der Studiengang konnte nicht angelegt werden.");
						  }

						  @Override
						  public void onSuccess(Studiengang result) {
							  tbbezeichnung.setText(result.getBezeichnung());
							  tvm.addStudiengang(result);
							  Window.alert ("Erfolgreich gespeichert.");
						  } 	
						});
				  }
			  }
		  });
	  }
}
