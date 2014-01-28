package de.hdm.stundenplansystem.client;

import java.util.Vector;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import de.hdm.stundenplansystem.shared.*;
import de.hdm.stundenplansystem.shared.bo.Semesterverband;
import de.hdm.stundenplansystem.shared.bo.Studiengang;
import de.hdm.stundenplansystem.client.NavTreeViewModel;

/**
 * Formular, in der ein bereits bestehender Semesterverband angezeigt, 
 * gelöscht und bearbeitet wird.
 * Diese Klasse erbt von der Klasse Content und lässt sich somit 
 * unter GWT entsprechend anordnen.
 * 
 * @see DozentForm
 * @author Thies, Espich
 * @version 1.0
 */
public class SemesterverbandForm extends Content {

	/**
	 * Jede Klasse enthält eine Überschrift, die definiert, was der User machen
	 * kann.
	 */
	private final HTML ueberschriftAenderung = new HTML(
			"<h2>Semesterverband verwalten<h2>");

	/** 
	 * Hier werden die GWT Widgets instantiiert
	 */
	final TextBox tbjahrgang = new TextBox();
	final ListBox libstudiengang = new ListBox();
	final TextBox tbsemester = new TextBox();
	final TextBox tbanzahl = new TextBox();
	final TextBox tbkuerzel = new TextBox();
	final Button loeschen = new Button("Semesterverband löschen");
	final Button speichern = new Button("Änderungen speichern");

	/**
	 * Hier wird ein Remote Service Proxy erstellt, welches uns erlaubt, 
	 * mit dem serverseitigen Verwaltungsservice zu kommunizieren.
	 */
	final VerwaltungsklasseAsync verwaltungsSvc = GWT
			.create(Verwaltungsklasse.class);

	/**
	 * Hier wird ein Vector des Objektes Studiengang festgelegt
	 */
	Vector<Studiengang> sgContainer = null;

	Integer id;
	Semesterverband shownSv = null;
	NavTreeViewModel tvm = null;

	  /**
	   * Jedes Formular wird durch einen Konstruktor dargestellt. 
	   * In diesem wird eine Instanz des GWT Widgets Grid erzeugt, dass den Aufbau
	   * des Formulars darstellt.
	   * Durch die Methode <code>add()</code> werden die Widgets dem Panel hinzugefügt.
	   * Durch die Methode <code>setWidget()</code> werden die Widgets 
	   * in den Zeilen und Spalten der Grid hinzugefügt.
	   */
	public SemesterverbandForm() {
		Grid svGrid = new Grid(7, 2);
		this.add(ueberschriftAenderung);
		this.add(svGrid);

		Label lbjahrgang = new Label("Jahrgang");
		svGrid.setWidget(0, 0, lbjahrgang);
		svGrid.setWidget(0, 1, tbjahrgang);

		Label lbstudiengang = new Label("Studiengang");
		svGrid.setWidget(1, 0, lbstudiengang);
		svGrid.setWidget(1, 1, libstudiengang);

		Label lbsemester = new Label("Semester");
		svGrid.setWidget(3, 0, lbsemester);
		svGrid.setWidget(3, 1, tbsemester);

		Label lbanzahl = new Label("Anzahl der Studierenden");
		svGrid.setWidget(4, 0, lbanzahl);
		svGrid.setWidget(4, 1, tbanzahl);

		Label lbfunktionen = new Label("Funktionen");
		svGrid.setWidget(5, 0, lbfunktionen);
		svGrid.setWidget(5, 1, speichern);

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
				changeSelectedSv();
			}
		});
		svGrid.setWidget(6, 1, loeschen);
		
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
				deleteSelectedSv();
			}
		});
		setTvm(tvm);
	}

	/**
	 * Durch die Methode <code>getAllStudiengaenge()</code> werden wir
	 * die Verwaltungsklasse bitten, uns über einen Callback alle Studiengänge in einem Vector 
	 * zurückzuliefern. Die Studiengänge werden durch die Methode <code>addItem()</code>
	 * der List Box zugefügt.
	 */
	public void getStudiengaenge() {
		verwaltungsSvc
				.getAllStudiengaenge(new AsyncCallback<Vector<Studiengang>>() {
					@Override
					public void onFailure(Throwable caught) {
						caught.getMessage();
					}

					@Override
					public void onSuccess(
							Vector<Studiengang> studiengang) {
						sgContainer = studiengang;
						for (Studiengang sg : studiengang) {
							libstudiengang.addItem(
									sg.getBezeichnung(),
									String.valueOf(sg.getId()));
						}
					}
				});
	}

	public void changeSelectedSv() {

		boolean allFilled = true;

		if (tbjahrgang.getText().isEmpty()
				|| tbanzahl.getText().isEmpty()
				|| tbsemester.getText().isEmpty()) {
			allFilled = false;
			Window.alert("Bitte füllen Sie alle Felder aus.");
		}

		if (allFilled == true) {
			shownSv.setJahrgang(tbjahrgang.getText().trim());
			
			/**
			 * Immer abfragen, ob der Wert der ListBox ungleich 0 ist, 
			 * da bei keiner Änderung der ListBox dieser nicht gespeichert wird. 
			 */
			if (libstudiengang.getSelectedIndex() != 0)
			shownSv.setStudiengangId(sgContainer.elementAt(
					libstudiengang.getSelectedIndex()-1).getId());
			
			shownSv.setStudierendenAnzahl(Integer.valueOf(tbanzahl
					.getValue()));
			shownSv.setSemester(Integer.valueOf(tbsemester.getValue()
					.trim()));

			/**
	         * Wenn die Text Boxen befüllt sind, werden wir die Verwaltungsklasse durch Methode
	         * <code>changeSemesterverband()</code> bitten, den ausgewählten Semesterverband zu bearbeiten.
	         */
			verwaltungsSvc.changeSemesterverband(shownSv,
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
			     * Durch die Methode <code>updateSemesterverband()</code> wird das bearbeitete 
			     * Semesterverband-Objekt dem Baum hinzugefügt.
			     * 
			     * @param result der Semesterverband, der bearbeitet wurde
			     */
				@Override
				public void onSuccess(Void result) {
				tvm.updateSemesterverband(shownSv);
				Window.alert("Erfolgreich gespeichert.");
				}
			});
		}
	}

	public void deleteSelectedSv() {
		
	    /**
         * Wir bitten die Verwaltungsklasse durch Methode 
         * <code>deleteSemesterverband()</code> den ausgewählten Semesterverband 
         * zu löschen.
         */
		verwaltungsSvc.deleteSemesterverband(shownSv,
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
				     * Durch die Methode <code>deleteSemesterverband()</code> wird das Semesterverband-Objekt 
				     * aus dem Baum gelöscht
				     * 
				     * @param result der Semesterverband, der gelöscht wurde
				     */
					@Override
					public void onSuccess(Void result) {
						tvm.deleteSemesterverband(shownSv);
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
	 * @param sv der Semesterverband der bearbeitet werden soll
	 */
	public void setSelected(Semesterverband sv) {
		if (sv != null) {
			shownSv = sv;
			setFields();
		} else {
			clearFields();
		}
	}

	/**
	 * Hier befüllen wir die Widgets mit den Daten des gewählten Semesterverbands
	 */
	public void setFields() {
		this.clearFields();
		tbjahrgang.setText(shownSv.getJahrgang());
		verwaltungsSvc.getStudiengangById(shownSv.getStudiengangId(),
				new AsyncCallback<Studiengang>() {
					@Override
					public void onFailure(Throwable caught) {
						caught.getMessage();
					}

					@Override
					public void onSuccess(Studiengang result) {
						libstudiengang.addItem(result
								.getBezeichnung());
						getStudiengaenge();
					}
				});
		tbsemester.setValue(Integer.toString(shownSv.getSemester()));
		tbanzahl.setValue(Integer.toString(shownSv
				.getStudierendenAnzahl()));
	}


	/**
	 * Hier löschen wir den Inhalt der Widgets
	 */
	public void clearFields() {
		tbjahrgang.setText("");
		libstudiengang.clear();
		tbsemester.setText("");
		tbanzahl.setText("");
	}
}
