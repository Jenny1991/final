package de.hdm.stundenplansystem.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import de.hdm.stundenplansystem.shared.*;
import de.hdm.stundenplansystem.shared.bo.Raum;
import de.hdm.stundenplansystem.client.NavTreeViewModel;

/**
 * Formular, in der ein bereits bestehender Raum angezeigt, 
 * gelöscht und bearbeitet wird.
 * Diese Klasse erbt von der Klasse Content und lässt sich somit 
 * unter GWT entsprechend anordnen.
 * 
 * @see DozentForm
 * @author Thies, Espich
 * @version 1.0
 */
public class RaumForm extends Content {

	/**
	 * Jede Klasse enthält eine Überschrift, die definiert, was der User machen
	 * kann.
	 */
	private final HTML ueberschriftAenderung = new HTML(
			"<h2>Raum verwalten<h2>");

	/** 
	 * Hier werden die GWT Widgets instantiiert
	 */
	final TextBox tbbezeichnung = new TextBox();
	final TextBox tbkapazitaet = new TextBox();
	final Button speichern = new Button("Änderungen speichern");
	final Button loeschen = new Button("Raum löschen");
	
	/**
	 * Hier wird ein Remote Service Proxy erstellt, welches uns erlaubt, 
	 * mit dem serverseitigen Verwaltungsservice zu kommunizieren.
	 */
	final VerwaltungsklasseAsync verwaltungsSvc = GWT
			.create(Verwaltungsklasse.class);

	Integer id;
	Raum shownRaum = null;
	NavTreeViewModel tvm = null;

	  /**
	   * Jedes Formular wird durch einen Konstruktor dargestellt. 
	   * In diesem wird eine Instanz des GWT Widgets Grid erzeugt, dass den Aufbau
	   * des Formulars darstellt.
	   * Durch die Methode <code>add()</code> werden die Widgets dem Panel hinzugefügt.
	   * Durch die Methode <code>setWidget()</code> werden die Widgets 
	   * in den Zeilen und Spalten der Grid hinzugefügt.
	   */
	public RaumForm() {
		Grid raumGrid = new Grid(4, 2);
		this.add(ueberschriftAenderung);
		this.add(raumGrid);

		Label lbbezeichnung = new Label("Bezeichnung");
		raumGrid.setWidget(0, 0, lbbezeichnung);
		raumGrid.setWidget(0, 1, tbbezeichnung);

		Label lbkapazitaet = new Label("Kapazität");
		raumGrid.setWidget(1, 0, lbkapazitaet);
		raumGrid.setWidget(1, 1, tbkapazitaet);

		Label lbfunktionen = new Label("Funktionen");
		raumGrid.setWidget(2, 0, lbfunktionen);
		raumGrid.setWidget(2, 1, speichern);
		
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
				changeSelectedRaum();
			}
		});
		raumGrid.setWidget(3, 1, loeschen);
		
		/**
		 * Beim Betätigen des Lösch-Buttons wird die Methode <code>addClickHandler()</code> 
		 * aufgerufen. Dabei wird ein Interface {@link ClickHandler} erzeugt, 
		 * das durch eine anonyme Klasse implementiert und durch new instantiiert wird. 
		 * Dieses Interface verlangt genau eine Methode <code>onClick()</code>, die 
		 * ein Objekt vom Typ ClickEvent {@link ClickEvent} erzeugt.
		 * 
		 * @param event wird abhängig vom Eventtyp {@link ClickEvent} definiert
		 * 
		 * Anschließend wird festgelegt, was passiert wenn der Lösch-Button gedrückt wurde.
		 */
		loeschen.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				deleteSelectedRaum();
			}
		});
		setTvm(tvm);
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

	public void changeSelectedRaum() {

		boolean allFilled = true;

		if (tbbezeichnung.getValue().isEmpty()
				|| tbkapazitaet.getValue().isEmpty()) {
			allFilled = false;
			Window.alert("Bitte füllen Sie alle Felder aus.");
		}

		if (allFilled == true) {
			shownRaum.setBezeichnung(tbbezeichnung.getText().trim());
			shownRaum.setKapazitaet(Integer.valueOf(tbkapazitaet
					.getText()));

			/**
	         * Wenn die Text Boxen befüllt sind, werden wir die Verwaltungsklasse durch Methode
	         * <code>changeRaum()</code> bitten, den ausgewählten Raum zu bearbeiten.
	         */
			verwaltungsSvc.changeRaum(shownRaum,
					new AsyncCallback<Void>() {

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
				Window.alert("Der Raum konnte nicht bearbeitet werden.");
				}

				/**
			     * Die Methode <code>onSuccess()</code> wird durch die GWT-RPC Runtime aufgerufen,
			     * wenn wie erwartet das Ergebnis des Funktionsaufrufs vom Server an den
			     * Client geliefert wird.
			     * Durch die Methode <code>updateRaum()</code> wird das bearbeitete Raum-Objekt 
			     * dem Baum hinzugefügt.
			     * 
			     * @param result der Raum, der bearbeitet wurde
			     */
				@Override
				public void onSuccess(Void result) {
				tvm.updateRaum(shownRaum);
				Window.alert("Erfolgreich gespeichert.");
				}
			});
		}
	}

	public void deleteSelectedRaum() {
		
	    /**
         * Wir bitten die Verwaltungsklasse durch Methode 
         * <code>deleteRaum()</code> den ausgewählten Raum 
         * zu löschen.
         */
		verwaltungsSvc.deleteRaum(shownRaum,
				new AsyncCallback<Void>() {
			
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
				     * Durch die Methode <code>deleteRaum()</code> wird das Raum-Objekt 
				     * aus dem Baum gelöscht
				     * 
				     * @param result der Raum, der gelöscht wurde
				     */
					@Override
					public void onSuccess(Void result) {
						tvm.deleteRaum(shownRaum);
						Window.alert("Erfolgreich gelöscht.");
					}
				});
		this.clearFields();
	}

	/**
	 * Bevor wir das Objekt bearbeiten fragen wir, 
	 * ob wir einen brauchbaren Wert zurückerhalten haben, 
	 * bevor wir diesen benutzen.
	 * Anschließend definierten wir, was als nächstes zu tun ist.
	 * 
	 * @param r der Raum der bearbeitet werden soll
	 */
	public void setSelected(Raum r) {
		if (r != null) {
			shownRaum = r;
			setFields();
		} else {
			clearFields();
		}
	}

	/**
	 * Hier befüllen wir die Widgets mit den Daten des gewählten Raums
	 */
	public void setFields() {
		tbbezeichnung.setText(shownRaum.getBezeichnung());
		tbkapazitaet.setText(Integer.toString(shownRaum
				.getKapazitaet()));
	}

	/**
	 * Hier löschen wir den Inhalt der Widgets
	 */
	public void clearFields() {
		tbbezeichnung.setText("");
		tbkapazitaet.setText("");
	}
}
