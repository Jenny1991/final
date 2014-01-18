package de.hdm.stundenplansystem.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

import de.hdm.stundenplansystem.shared.Verwaltungsklasse;
import de.hdm.stundenplansystem.shared.VerwaltungsklasseAsync;
import de.hdm.stundenplansystem.shared.bo.Stundenplan;

public class CreateStundenplan extends Content{
	
	  /**
	   * Jede Klasse enthï¿½t eine ï¿½berschrift, die definiert, was der User machen kann.
		   */
	private final HTML ueberschrift = new HTML ("<h2>Neues Studienhalbjahr anlegen<h2>");

	  /**
	   * Unter der ï¿½berschrift trï¿½gt der User die Daten des neuen Studiengangs ein. 
	   */
	  final Label lbhalbjahr = new Label ("Studienhalbjahr"); 
	  final TextBox tbhalbjahr = new TextBox ();
	  final Button speichern = new Button ("speichern");
	  
	  final VerwaltungsklasseAsync verwaltungsSvc = GWT.create(Verwaltungsklasse.class);
	  NavTreeViewModel tvm = null;

	  /**
	  * Anordnen der Buttons und Labels auf den Panels
	  */
	  public void onLoad () {

		  this.add(ueberschrift);
		  this.add(lbhalbjahr);
		  this.add(tbhalbjahr);
		  this.add(speichern);
		  
		  setTvm(tvm);
		  	
		  speichern.addClickHandler(new ClickHandler() {
			  public void onClick(ClickEvent event) {

				  boolean allFilled = true;
				  
				  	if (tbhalbjahr.getValue().isEmpty())  
				  	{ allFilled = false;
					  Window.alert ("Bitte f�llen Sie alle Felder aus."); }
				  	
				  	if (allFilled == true){
				  		final String studienhalbjahr = tbhalbjahr.getValue().trim();
				  
					  verwaltungsSvc.createStundenplan(studienhalbjahr, new AsyncCallback<Stundenplan>() {

						  @Override
						  public void onFailure (Throwable caught) {
							  Window.alert("Der Studiengang konnte nicht angelegt werden.");
						  }

						  @Override
						  public void onSuccess(Stundenplan result) {
							  tbhalbjahr.setText("");
							  Window.alert ("Erfolgreich gespeichert.");
							  tvm.addStundenplan(result);
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
