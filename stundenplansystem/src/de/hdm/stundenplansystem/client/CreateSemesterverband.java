package de.hdm.stundenplansystem.client;

import java.util.Vector;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ListBox;

import de.hdm.stundenplansystem.shared.*;
import de.hdm.stundenplansystem.shared.bo.Studiengang;
import de.hdm.stundenplansystem.shared.bo.Semesterverband;
import de.hdm.stundenplansystem.client.NavTreeViewModel;

/**
 * Hier wird ein neuer Semesterverband angelegt.
 * Diese Klasse erbt von der Klasse Content und lässt sich somit 
 * unter GWT entsprechend anordnen.
 * 
 * @see CreateDozent
 * @author Thies, Espich
 * @version 1.0
 */
public class CreateSemesterverband extends Content {

	/**
	 * Jede Klasse enthält eine Überschrift, die definiert, was der User machen
	 * kann.
	 */
	private final HTML ueberschrift = new HTML(
			"<h2>Neuen Semesterverband anlegen<h2>");
	
	/** 
	 * Hier werden die GWT Widgets instantiiert
	 */
	final Label lbJahrgang = new Label(
			"Beginn des Studiums (z.B. SS 2012 oder WS 2012/2013):");
	final Label lbStudiengang = new Label("Studiengang:");
	final Label lbSemester = new Label(
			"aktuelles Semester (z.B. 1, 2, 3, ...):");
	final Label lbAnzahl = new Label("Anzahl der Studierenden:");
	final TextBox tbJahrgang = new TextBox();
	final ListBox libStudiengang = new ListBox();
	final TextBox tbSemester = new TextBox();
	final TextBox tbAnzahl = new TextBox();
	final Button speichern = new Button("Eingaben speichern");

	/**
	 * Hier wird ein Vector des Objektes Studiengang festgelegt
	 */
	Vector<Studiengang> sgContainer = null;

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
		this.add(lbJahrgang);
		this.add(tbJahrgang);
		this.add(lbStudiengang);
		this.add(libStudiengang);
		this.add(lbSemester);
		this.add(tbSemester);
		this.add(lbAnzahl);
		this.add(tbAnzahl);
		this.add(speichern);
		setTvm(tvm);

		/**
		 * Durch die Methode <code>clear()</code> werden zunächst alle Elemente
		 * der List Box gelöscht. 
		 * Anschließend werden wir durch die Methode <code>getAllStudiengaenge()</code>
		 * die Verwaltungsklasse bitten, uns über einen Callback alle Studiengänge in einem Vector 
		 * zurückzuliefern. Die Studiengänge werden durch die Methode <code>addItem()</code>
		 * der List Box zugefügt.
		 */
		libStudiengang.clear();
		verwaltungsSvc
		.getAllStudiengaenge(new AsyncCallback<Vector<Studiengang>>() {
			public void onFailure(Throwable T) {
			}

			public void onSuccess(
					Vector<Studiengang> studiengaenge) {
				sgContainer = studiengaenge;
				for (Studiengang sg : studiengaenge) {
					libStudiengang.addItem(
							sg.getBezeichnung(),
							String.valueOf(sg.getId()));
				}
			}
		});

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

				if (tbJahrgang.getText().isEmpty()
						|| tbAnzahl.getText().isEmpty()
						|| tbSemester.getText().isEmpty()) {
					allFilled = false;
					Window.alert("Bitte füllen Sie alle Felder aus.");
				}

				if (allFilled == true) {
					final String jahrgang = tbJahrgang.getText()
							.trim();
					final int studiengangId = sgContainer.elementAt(
							libStudiengang.getSelectedIndex())
							.getId();
					final int studierendenAnzahl = Integer
							.valueOf(tbAnzahl.getValue());
					final int semester = Integer.valueOf(tbSemester
							.getText().trim());

					 /**
			         * Wenn die Text Boxen befüllt sind, werden wir die Verwaltungsklasse durch Methode
			         * <code>createSemesterverband()</code> bitten,
			         * einen neuen Semesterverband anzulegen und diesen uns über einen Callback
			         * zurückzuliefern.
			         */
					verwaltungsSvc.createSemesterverband(
							studiengangId, semester,
							studierendenAnzahl, jahrgang,
							new AsyncCallback<Semesterverband>() {

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
							     * Durch die Methode <code>addSemesterverband()</code> wird das erstellte
							     * Semesterverband-Objekt dem Baum hinzugefügt.
							     * 
							     * @param result der Semesterverband, der neu erstellt wurde
							     */
								@Override
								public void onSuccess(
										Semesterverband result) {
									tbJahrgang.setText("");
									libStudiengang.clear();
									tbSemester.setText("");
									tbAnzahl.setText("");
									Window.alert("Erfolgreich gespeichert.");
									tvm.addSemesterverband(result);
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
