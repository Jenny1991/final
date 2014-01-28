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
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import de.hdm.stundenplansystem.client.NavTreeViewModel;
import de.hdm.stundenplansystem.shared.*;
import de.hdm.stundenplansystem.shared.bo.Lehrveranstaltung;

/**
 * Formular, in der eine bereits bestehende Lehrveranstaltung angezeigt, 
 * gelöscht und bearbeitet wird.
 * Diese Klasse erbt von der Klasse Content und lässt sich somit 
 * unter GWT entsprechend anordnen.
 * 
 * @see DozentForm
 * @author Thies, Espich
 * @version 1.0
 */
public class LehrveranstaltungForm extends Content {

	/**
	 * Jede Klasse enthält eine Überschrift, die definiert, was der User machen
	 * kann.
	 */
	private final HTML ueberschriftAenderung = new HTML(
			"<h2>Lehrveranstaltung verwalten<h2>");

	/** 
	 * Hier werden die GWT Widgets instantiiert
	 */
	final TextArea tbbezeichnung = new TextArea();
	final TextBox tbsemester = new TextBox();
	final TextBox tbumfang = new TextBox();
	final Button loeschen = new Button("Lehrveranstaltung löschen");
	final Button speichern = new Button("Änderungen speichern");
	
	/**
	 * Hier wird ein Remote Service Proxy erstellt, welches uns erlaubt, 
	 * mit dem serverseitigen Verwaltungsservice zu kommunizieren.
	 */
	final VerwaltungsklasseAsync verwaltungsSvc = GWT
			.create(Verwaltungsklasse.class);

	Integer id;
	Lehrveranstaltung shownLv = null;
	NavTreeViewModel tvm = null;

	  /**
	   * Jedes Formular wird durch einen Konstruktor dargestellt. 
	   * In diesem wird eine Instanz des GWT Widgets Grid erzeugt, dass den Aufbau
	   * des Formulars darstellt.
	   * Durch die Methode <code>add()</code> werden die Widgets dem Panel hinzugefügt.
	   * Durch die Methode <code>setWidget()</code> werden die Widgets 
	   * in den Zeilen und Spalten der Grid hinzugefügt.
	   */
	public LehrveranstaltungForm() {
		Grid lehrGrid = new Grid(5, 3);
		this.add(ueberschriftAenderung);
		this.add(lehrGrid);

		Label lbbezeichnung = new Label("Bezeichnung");
		lehrGrid.setWidget(0, 0, lbbezeichnung);
		lehrGrid.setWidget(0, 1, tbbezeichnung);

		Label lbsemester = new Label("Semester");
		lehrGrid.setWidget(1, 0, lbsemester);
		lehrGrid.setWidget(1, 1, tbsemester);

		Label lbumfang = new Label("Umfang (in ECTS)");
		lehrGrid.setWidget(2, 0, lbumfang);
		lehrGrid.setWidget(2, 1, tbumfang);

		Label lbfunktionen = new Label("Funktionen");
		lehrGrid.setWidget(3, 0, lbfunktionen);
		lehrGrid.setWidget(3, 1, speichern);
		
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
				changeSelectedLv();
			}
		});
		lehrGrid.setWidget(4, 1, loeschen);
		
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
				deleteSelectedLv();
			}
		});
		setTvm(tvm);
	}

	public void changeSelectedLv() {

		boolean allFilled = true;

		if (tbbezeichnung.getValue().isEmpty()
				|| tbsemester.getValue().isEmpty()
				|| tbumfang.getValue().isEmpty()) {
			allFilled = false;
			Window.alert("Bitte füllen Sie alle Felder aus.");
		}

		if (allFilled == true) {
			shownLv.setBezeichnung(tbbezeichnung.getText().trim());
			shownLv.setSemester(Integer.valueOf(tbsemester.getValue()));
			shownLv.setUmfang(Integer.valueOf(tbumfang.getValue()));

		    /**
	         * Wenn die Text Boxen befüllt sind, werden wir die Verwaltungsklasse durch Methode
	         * <code>changeLehrveranstaltung()</code> bitten, die ausgewählte Lehrveranstaltung zu bearbeiten.
	         */
			verwaltungsSvc.changeLehrveranstaltung(shownLv,
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
					Window.alert("Die Lehrveranstaltung konnte nicht bearbeitet werden.");
				}

				/**
			     * Die Methode <code>onSuccess()</code> wird durch die GWT-RPC Runtime aufgerufen,
			     * wenn wie erwartet das Ergebnis des Funktionsaufrufs vom Server an den
			     * Client geliefert wird.
			     * Durch die Methode <code>updateLehrveranstaltung()</code> wird das bearbeitete Lehrveranstaltung-Objekt 
			     * dem Baum hinzugefügt.
			     * 
			     * @param result die Lehrveranstaltung, die bearbeitet wurde
			     */
				@Override
				public void onSuccess(Void result) {
				tvm.updateLehrveranstaltung(shownLv);
				Window.alert("Erfolgreich gespeichert.");
				}
			});
		}
	}

	public void deleteSelectedLv() {
		/**
         * Wir bitten die Verwaltungsklasse durch Methode 
         * <code>deleteLehrveranstaltung()</code> den ausgewählte Lehrveranstaltung 
         * zu löschen.
         */
		verwaltungsSvc.deleteLehrveranstaltung(shownLv,
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
				     * Durch die Methode <code>deleteLehrveranstaltung()</code> wird das Lehrveranstaltung-Objekt 
				     * aus dem Baum gelöscht
				     * 
				     * @param result die Lehrveranstaltung, die gelöscht wurde
				     */
					@Override
					public void onSuccess(Void result) {
						tvm.deleteLehrveranstaltung(shownLv);
						Window.alert("Erfolgreich gelöscht.");
					}
				});
		this.clearFields();
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
	
	/**
	 * Bevor wir das Objekt bearbeiten fragen wir, 
	 * ob wir einen brauchbaren Wert zurückerhalten haben, 
	 * bevor wir diesen benutzen.
	 * Anschließend definierten wir, was als nächstes zu tun ist.
	 * 
	 * @param lv die Lehrveranstaltung die bearbeitet werden soll
	 */
	public void setSelected(Lehrveranstaltung lv) {
		if (lv != null) {
			shownLv = lv;
			setFields();
		} else {
			clearFields();
		}
	}

	/**
	 * Hier befüllen wir die Widgets mit den Daten der gewählten Lehrveranstaltung
	 */
	public void setFields() {
		tbbezeichnung.setText(shownLv.getBezeichnung());
		tbsemester.setValue(Integer.toString(shownLv.getSemester()));
		tbumfang.setValue(Integer.toString(shownLv.getUmfang()));
	}

	/**
	 * Hier löschen wir den Inhalt der Widgets
	 */
	public void clearFields() {
		tbbezeichnung.setText("");
		tbsemester.setText("");
		tbumfang.setText("");
	}
}