package de.hdm.stundenplansystem.client;

import com.google.gwt.core.client.GWT;
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
import de.hdm.stundenplansystem.shared.bo.Dozent;
import de.hdm.stundenplansystem.client.NavTreeViewModel;

/**
 * Formular, in der ein bereits bestehender Dozent angezeigt, 
 * gelöscht und bearbeitet wird.
 * Diese Klasse erbt von der Klasse Content und lässt sich somit 
 * unter GWT entsprechend anordnen.
 * 
 * @see CreateDozent
 * @author Thies, Espich
 * @version 1.0
 */
public class DozentForm extends Content {

	/**
	 * Jede Klasse enthält eine Überschrift, die definiert, was der User machen
	 * kann.
	 */
	private final HTML ueberschriftAenderung = new HTML(
			"<h2>Dozenten verwalten<h2>");

	/** 
	 * Hier werden die GWT Widgets instantiiert
	 */
	final TextBox tbvorname = new TextBox();
	final TextBox tbnachname = new TextBox();
	final Button loeschen = new Button("Dozent löschen");
	final Button speichern = new Button("Änderungen speichern");
	
	/**
	 * Hier wird ein Remote Service Proxy erstellt, welches uns erlaubt, 
	 * mit dem serverseitigen Verwaltungsservice zu kommunizieren.
	 */
	final VerwaltungsklasseAsync verwaltungsSvc = GWT
			.create(Verwaltungsklasse.class);

	Integer id;
	Dozent shownDozent = null;
	NavTreeViewModel tvm = null;

	  /**
	   * Jedes Formular wird durch einen Konstruktor dargestellt. 
	   * In diesem wird eine Instanz des GWT Widgets Grid erzeugt, dass den Aufbau
	   * des Formulars darstellt.
	   * Durch die Methode <code>add()</code> werden die Widgets dem Panel hinzugefügt.
	   * Durch die Methode <code>setWidget()</code> werden die Widgets 
	   * in den Zeilen und Spalten der Grid hinzugefügt.
	   */
	public DozentForm() {
		Grid dozentGrid = new Grid(4, 2);
		this.add(ueberschriftAenderung);
		this.add(dozentGrid);

		Label lbvorname = new Label("Vorname");
		dozentGrid.setWidget(0, 0, lbvorname);
		dozentGrid.setWidget(0, 1, tbvorname);

		Label lbnachname = new Label("Nachname");
		dozentGrid.setWidget(1, 0, lbnachname);
		dozentGrid.setWidget(1, 1, tbnachname);

		Label lbfunktionen = new Label("Funktionen");
		dozentGrid.setWidget(2, 0, lbfunktionen);
		dozentGrid.setWidget(2, 1, speichern);
		
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
				changeSelectedDozent();
			}
		});
		dozentGrid.setWidget(3, 1, loeschen);
		
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
				deleteSelectedDozent();
			}
		});
		setTvm(tvm);
	}

//	public void getData() {
//		verwaltungsSvc.getDozentById(id, new AsyncCallback<Dozent>() {
//			@Override
//			public void onFailure(Throwable caught) {
//			}
//
//			@Override
//			public void onSuccess(Dozent d) {
//				if(d != null) {
//					setSelected(d);
//				}
//			};
//		});
//	}

	/**
	 * Die Methode <code>setTvm()</code> sorgt dafür, 
	 * dass die Klasse {@link NavTreeViewModel} auf diese Klasse zugreifen kann
	 * 
	 * @param tvm Instanz der Klasse {@link NavTreeViewModel}
	 */
	public void setTvm(NavTreeViewModel tvm) {
		this.tvm = tvm;
	}

	public void changeSelectedDozent() {

		boolean allFilled = true;

		if(tbnachname.getValue().isEmpty()
				|| tbvorname.getValue().isEmpty()) {
			allFilled = false;
			Window.alert("Bitte füllen Sie alle Felder aus.");
		}

		if(allFilled == true) {
			shownDozent.setNachname(tbnachname.getText().trim());
			shownDozent.setVorname(tbvorname.getText().trim());

		    /**
	         * Wenn die Text Boxen befüllt sind, werden wir die Verwaltungsklasse durch Methode
	         * <code>changeDozent()</code> bitten, den ausgewählten Dozent zu bearbeiten.
	         */
			verwaltungsSvc.changeDozent(shownDozent,
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
					     * Durch die Methode <code>updateDozent()</code> wird das bearbeitete Dozenten-Objekt 
					     * dem Baum hinzugefügt.
					     * 
					     * @param result der Dozent, der bearbeitet wurde
					     */
						@Override
						public void onSuccess(Void result) {
							tvm.updateDozent(shownDozent);
							Window.alert("Erfolgreich gespeichert.");

						}
					});
		}
	}

	public void deleteSelectedDozent() {
		
	    /**
         * Wir bitten die Verwaltungsklasse durch Methode 
         * <code>deleteDozent()</code> den ausgewählten Dozenten 
         * zu löschen.
         */
		verwaltungsSvc.deleteDozent(shownDozent,
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
				     * Durch die Methode <code>deleteDozent()</code> wird das Dozenten-Objekt 
				     * aus dem Baum gelöscht
				     * 
				     * @param result der Dozent, der gelöscht wurde
				     */
					@Override
					public void onSuccess(Void result) {
						tvm.deleteDozent(shownDozent);
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
	 * @param d der Dozent der bearbeitet werden soll
	 */
	public void setSelected(Dozent d) {
		if(d != null) {
			shownDozent = d;
			setFields();
		} else {
			clearFields();
		}
	}

	/**
	 * Hier befüllen wir die Widgets mit den Daten des gewählten Dozenten
	 */
	public void setFields() {
		tbvorname.setText(shownDozent.getVorname());
		tbnachname.setText(shownDozent.getNachname());
	}

	/**
	 * Hier löschen wir den Inhalt der Widgets
	 */
	public void clearFields() {
		tbvorname.setText("");
		tbnachname.setText("");
	}
}