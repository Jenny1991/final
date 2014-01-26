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
import de.hdm.stundenplansystem.shared.bo.Dozent;
import de.hdm.stundenplansystem.client.NavTreeViewModel;

/**
 * Hier wird ein neuer Dozent angelegt.
 * Diese Klasse erbt von der Klasse Content und lässt sich somit 
 * unter GWT entsprechend anordnen.
 * 
 * @author Thies, Espich
 * @version 1.0
 */
public class CreateDozent extends Content {

	/**
	 * Jede Klasse enthält eine Überschrift, die definiert, was der User machen
	 * kann.
	 */
	private final HTML ueberschrift = new HTML(
			"<h2>Neuen Dozenten anlegen<h2>");

	/**
	 * Unter der Überschrift trägt der User die Daten des neuen Dozenten 
	 * in Text Boxen ein.
	 */
	Label lbvorname = new Label("Vorname:");
	final Label lbNachname = new Label("Nachname:");
	final TextBox tbVorname = new TextBox();
	final TextBox tbNachname = new TextBox();
	final Button speichern = new Button("Eingaben speichern");

	/**
	 * Hier wird ein Remote Service Proxy erstellt, welches uns erlaubt, 
	 * mit dem serverseitigen Verwaltungsservice zu kommunizieren.
	 */
	final VerwaltungsklasseAsync verwaltungsSvc = GWT
			.create(Verwaltungsklasse.class);
	NavTreeViewModel tvm = null;
	
	  /**
	   * Jedes GWT Widget muss die Methode <code>onLoad()</code> implementieren. 
	   * Sie gibt an, was geschehen soll, 
	   * wenn eine Widget-Instanz zur Anzeige gebracht wird.
	   * Durch die Methode <code>add()</code> werden die Widgets dem Panel hinzugefügt.
	   */
	public void onLoad() {

		this.add(ueberschrift);
		this.add(lbvorname);
		this.add(tbVorname);
		this.add(lbNachname);
		this.add(tbNachname);
		this.add(speichern);
		setTvm(tvm);

		/**
		 * Beim Betätigen des Speicher-Buttons wird die Methode <code>addClickHandler()</code> 
		 * aufgerufen. Dabei wird ein Interface {@link ClickHandler} erzeugt, 
		 * das durch eine anonyme Klasse implementiert und durch new instantiiert wird. 
		 * Dieses Interface verlangt genau eine Methode <code>onClick()</code>, die 
		 * ein Objekt vom Typ ClickEvent {@link ClickEvent} erzeugt.
		 * 
		 * @param event wird abhängig vom Eventtyp {@link ClickEvent} definiert
		 * 
		 * Anschließend wird festgelegt, was passiert wenn der Speicher-Button gedrückt wurde.
		 */
		speichern.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				boolean allFilled = true;

				if (tbNachname.getValue().isEmpty()
						|| tbVorname.getValue().isEmpty()) {
					allFilled = false;
					Window.alert("Bitte füllen Sie alle Felder aus.");
				}

				if (allFilled == true) {
					String vorname = tbVorname.getValue().trim();
					String nachname = tbNachname.getValue().trim();
					
				    /**
			         * Wenn die Text Boxen befüllt sind, werden wir die Verwaltungsklasse durch Methode
			         * <code>createDozent()</code> bitten,
			         * einen neuen Dozenten anzulegen und diesen uns über einen Callback
			         * zurückzuliefern.
			         */
					verwaltungsSvc.createDozent(vorname, nachname,
							new AsyncCallback<Dozent>() {

								/**
								 * Die Methode <code>onFailure()</code> wird durch die GWT-RPC Runtime aufgerufen,
								 * wenn es zu einem Problem während des Aufrufs
								 * oder der Server-seitigen Abbarbeitung kam.
								 * Falls etwas schief geht, erscheint ein Fenster in dem der Fehler dargestellt wird.
								 * 
								 * @param caught Fehler der während der RPC-Runtime auftritt
								 */
								@Override
								public void onFailure(Throwable caught) {
									Window.alert(caught.getMessage());
								}
								
								/**
							     * Die Methode <code>onSuccess()</code> wird durch die GWT-RPC Runtime aufgerufen,
							     * wenn wie erwartet das Ergebnis des Funktionsaufrufs vom Server an den
							     * Client geliefert wird.
							     * Durch die Methode <code>addDozent()</code> wird das erstellte Dozenten-Objekt 
							     * dem Baum hinzugefügt.
							     * 
							     * @param result der Dozent, der neu erstellt wurde
							     */
								@Override
								public void onSuccess(Dozent result) {
									tbVorname.setText("");
									tbNachname.setText("");
									Window.alert("Erfolgreich gespeichert.");
									tvm.addDozent(result);
								}
							});
				}
			}
		});
	}

	/**
	 * Die Methode <code>setTvm()</code> sorgt dafür, 
	 * dass die Klasse {@link NavTreeViewModel} auf diese Klasse zugreifen kann
	 * 
	 * @param tvm Instanz der Klasse {@link NavTreeViewModel}
	 */
	public void setTvm(NavTreeViewModel tvm) {
		this.tvm = tvm;
	}
}