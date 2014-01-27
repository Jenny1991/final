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
import com.google.gwt.user.client.ui.TextArea;
import de.hdm.stundenplansystem.shared.*;
import de.hdm.stundenplansystem.shared.bo.Studiengang;
import de.hdm.stundenplansystem.client.NavTreeViewModel;

/**
 * Formular, in der ein bereits bestehender Studiengang angezeigt, 
 * gelöscht und bearbeitet wird.
 * Diese Klasse erbt von der Klasse Content und lässt sich somit 
 * unter GWT entsprechend anordnen.
 * 
 * @see CreateDozent
 * @author Thies, Espich
 * @version 1.0
 */
public class StudiengangForm extends Content {

	/**
	 * Jede Klasse enthält eine Überschrift, die definiert, was der User machen
	 * kann.
	 */
	private final HTML ueberschriftAenderung = new HTML(
			"<h2>Studiengang verwalten<h2>");

	/** 
	 * Hier werden die GWT Widgets instantiiert
	 */
	final TextArea tbbezeichnung = new TextArea();
	final Button loeschen = new Button("Studiengang löschen");
	final Button speichern = new Button("Änderungen speichern");
	
	/**
	 * Hier wird ein Remote Service Proxy erstellt, welches uns erlaubt, 
	 * mit dem serverseitigen Verwaltungsservice zu kommunizieren.
	 */
	final VerwaltungsklasseAsync verwaltungsSvc = GWT
			.create(Verwaltungsklasse.class);

	Integer id;
	Studiengang shownSg = null;
	NavTreeViewModel tvm = null;

	  /**
	   * Jedes Formular wird durch einen Konstruktor dargestellt. 
	   * In diesem wird eine Instanz des GWT Widgets Grid erzeugt, dass den Aufbau
	   * des Formulars darstellt.
	   * Durch die Methode <code>add()</code> werden die Widgets dem Panel hinzugefügt.
	   * Durch die Methode <code>setWidget()</code> werden die Widgets 
	   * in den Zeilen und Spalten der Grid hinzugefügt.
	   */
	public StudiengangForm() {
		Grid studiengangGrid = new Grid(3, 2);
		this.add(ueberschriftAenderung);
		this.add(studiengangGrid);

		Label lbbezeichnung = new Label("Bezeichnung");
		studiengangGrid.setWidget(0, 0, lbbezeichnung);
		studiengangGrid.setWidget(0, 1, tbbezeichnung);

		Label lbfunktionen = new Label("Funktionen");
		studiengangGrid.setWidget(1, 0, lbfunktionen);
		studiengangGrid.setWidget(1, 1, speichern);
		
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
				changeSelectedSg();
			}
		});
		studiengangGrid.setWidget(2, 1, loeschen);
		
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
				deleteSelectedSg();
			}
		});
		setTvm(tvm);
	}

//	public void getData() {
//		verwaltungsSvc.getStudiengangById(id,
//				new AsyncCallback<Studiengang>() {
//					@Override
//					public void onFailure(Throwable caught) {
//					}
//
//					@Override
//					public void onSuccess(Studiengang sg) {
//						if (sg != null) {
//							setSelected(sg);
//						}
//					}
//				});
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

	public void changeSelectedSg() {

		boolean allFilled = true;

		if (tbbezeichnung.getValue().isEmpty()) {
			allFilled = false;
			Window.alert("Bitte füllen Sie alle Felder aus.");
		}

		if (allFilled == true) {
			shownSg.setBezeichnung(tbbezeichnung.getText().trim());
			
		    /**
	         * Wenn die Text Boxen befüllt sind, werden wir die Verwaltungsklasse durch Methode
	         * <code>changeStudiengang()</code> bitten, den ausgewählten Studiengang zu bearbeiten.
	         */
			verwaltungsSvc.changeStudiengang(shownSg,
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
			     * Durch die Methode <code>updateStudiengang()</code> wird das bearbeitete Studiengang-Objekt 
			     * dem Baum hinzugefügt.
			     * 
			     * @param result der Studiengang, der bearbeitet wurde
			     */
				@Override
				public void onSuccess(Void result) {
				tvm.updateStudiengang(shownSg);
				Window.alert("Erfolgreich gespeichert.");
				}
			});
		}
	}

	public void deleteSelectedSg() {
		
	    /**
         * Wir bitten die Verwaltungsklasse durch Methode 
         * <code>deleteStudiengang()</code> den ausgewählten Studiengang 
         * zu löschen.
         */
		verwaltungsSvc.deleteStudiengang(shownSg,
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
				     * Durch die Methode <code>deleteStudiengang()</code> wird das Studiengang-Objekt 
				     * aus dem Baum gelöscht
				     * 
				     * @param result der Studiengang, der gelöscht wurde
				     */
					@Override
					public void onSuccess(Void result) {
						tvm.deleteStudiengang(shownSg);
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
	 * @param sg der Studiengang der bearbeitet werden soll
	 */
	public void setSelected(Studiengang sg) {
		if (sg != null) {
			shownSg = sg;
			setFields();
		} else {
			clearFields();
		}
	}

	/**
	 * Hier befüllen wir die Widgets mit den Daten des gewählten Studiengangs
	 */
	public void setFields() {
		tbbezeichnung.setText(shownSg.getBezeichnung());
	}

	/**
	 * Hier löschen wir den Inhalt der Widgets
	 */
	public void clearFields() {
		tbbezeichnung.setText("");
	}
}