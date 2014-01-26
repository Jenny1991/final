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
	 * Unter der Überschrift trägt der User die Daten des neuen Dozenten 
	 * in Text Boxen ein.
	 */
	final Label lbjahrgang = new Label(
			"Beginn des Studiums (z.B. SS 2012 oder WS 2012/2013):");
	final Label lbstudiengang = new Label("Studiengang:");
	final Label lbsemester = new Label(
			"aktuelles Semester (z.B. 1, 2, 3, ...):");
	final Label lbanzahl = new Label("Anzahl der Studierenden:");
	final TextBox tbjahrgang = new TextBox();
	final ListBox libstudiengang = new ListBox();
	final TextBox tbsemester = new TextBox();
	final TextBox tbanzahl = new TextBox();
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
	   * 
	   * @param ueberschrift, 
	   * @param lbjahrgang, 
	   * @param tbjahrgang, 
	   * @param lbstudiengang, 
	   * @param libstudiengang, 
	   * @param lbsemester, 
	   * @param tbsemester, 
	   * @param lbanzahl, 
	   * @param tbanzahl, 
	   * @param speichern definieren den Aufbau der Widgets den Panels
	   */
	public void onLoad() {

		this.add(ueberschrift);
		this.add(lbjahrgang);
		this.add(tbjahrgang);
		this.add(lbstudiengang);
		this.add(libstudiengang);
		this.add(lbsemester);
		this.add(tbsemester);
		this.add(lbanzahl);
		this.add(tbanzahl);
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
		libstudiengang.clear();
		verwaltungsSvc
		.getAllStudiengaenge(new AsyncCallback<Vector<Studiengang>>() {
			public void onFailure(Throwable T) {
			}

			public void onSuccess(
					Vector<Studiengang> studiengaenge) {
				sgContainer = studiengaenge;
				for (Studiengang sg : studiengaenge) {
					libstudiengang.addItem(
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

				if (tbjahrgang.getText().isEmpty()
						|| tbanzahl.getText().isEmpty()
						|| tbsemester.getText().isEmpty()) {
					allFilled = false;
					Window.alert("Bitte füllen Sie alle Felder aus.");
				}

				if (allFilled == true) {
					final String jahrgang = tbjahrgang.getText()
							.trim();
					final int studiengangId = sgContainer.elementAt(
							libstudiengang.getSelectedIndex())
							.getId();
					final int studierendenAnzahl = Integer
							.valueOf(tbanzahl.getValue());
					final int semester = Integer.valueOf(tbsemester
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
									tbjahrgang.setText("");
									libstudiengang.clear();
									tbsemester.setText("");
									tbanzahl.setText("");
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
