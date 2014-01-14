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
import de.hdm.stundenplansystem.shared.bo.Raum;
import de.hdm.stundenplansystem.client.NavTreeViewModel;

	/**
	 * Hier wird ein neuer Raum angelegt.
	 * 
	 * @author Thies, Espich
	 * 
	 */

	public class CreateRaum extends Content {
		
		  /**
		   * Jede Klasse enthï¿½t eine ï¿½berschrift, die definiert, was der User machen kann.
		   */
		private final HTML ueberschrift = new HTML ("<h2>Neuen Raum anlegen<h2>");

		  /**
		   * Unter der ï¿½berschrift tragt der User die Daten des neuen Raums ein. 
		   */
		  final Label lbbezeichnung = new Label ("Bezeichnung"); 
		  final Label lbkapazitaet = new Label ("KapazitÃ¤t");
		  final TextBox tbbezeichnung = new TextBox ();
		  final TextBox tbkapazitaet = new TextBox ();
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
				  this.add(lbkapazitaet);
				  this.add(tbkapazitaet);
				  this.add(speichern);
					  
				  speichern.addClickHandler(new ClickHandler() {
					  public void onClick(ClickEvent event) {

						  boolean allFilled = true;
						  
						  if (tbbezeichnung.getValue().isEmpty() 
								  || tbkapazitaet.getValue().isEmpty())
						  {	allFilled = false;
						  Window.alert ("Bitte fÃ¼llen Sie alle Felder aus."); }
						  
						  if (allFilled == true) { 
							  final String bezeichnung = tbbezeichnung.getValue().trim();
							  final int kapazitaet = tbkapazitaet.getVisibleLength();

							  verwaltungsSvc.createRaum(bezeichnung, kapazitaet, new AsyncCallback<Raum>() {

								  @Override
								  public void onFailure (Throwable caught) {
									  Window.alert("Der Raum konnte nicht angelegt werden.");
								  }

								  @Override
								  public void onSuccess(Raum result) {
									  tbbezeichnung.setText(result.getBezeichnung());
									  tbkapazitaet.setVisibleLength(result.getKapazitaet());
									  tvm.addRaum(result);
									  Window.alert ("Erfolgreich gespeichert.");
								  } 	
								});
						  }
					  }
					  });
		  }

}
