package de.hdm.stundenplansystem.client;

import java.util.Vector;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;

import de.hdm.stundenplansystem.shared.Verwaltungsklasse;
import de.hdm.stundenplansystem.shared.VerwaltungsklasseAsync;
import de.hdm.stundenplansystem.shared.bo.Semesterverband;
import de.hdm.stundenplansystem.shared.bo.Studiengang;
import de.hdm.stundenplansystem.shared.bo.Stundenplan;
import de.hdm.stundenplansystem.client.NavTreeViewModel;

/**
 * Hier wird ein neuer Stundenplan angelegt.
 * Diese Klasse erbt von der Klasse Content und lässt sich somit 
 * unter GWT entsprechend anordnen.
 * 
 * @see CreateDozent
 * @author Thies, Espich
 * @version 1.0
 */
public class CreateStundenplan extends Content {

	/**
	 * Jede Klasse enthält eine Überschrift, die definiert, was der User machen
	 * kann.
	 */
	private final HTML ueberschrift = new HTML(
			"<h2>Neues Studienhalbjahr anlegen<h2>");

	/** 
	 * Hier werden die GWT Widgets instantiiert
	 */
	final Label lbHalbjahr = new Label(
			"Studienhalbjahr des Stundenplans (z.B. SS 2012 oder WS 2012/2013):");
	final TextBox tbHalbjahr = new TextBox();
	final Label lbSemverband = new Label("Semesterverband:");
	final ListBox libSemverband = new ListBox();
	final Label lbStudiengang = new Label("Studiengang:");
	final ListBox libStudiengang = new ListBox();
	final Button speichern = new Button("Eingaben speichern");

	/**
	 * Hier wird ein Vector des Objektes Studiengang und 
	 * ein Vector des Objetkes Semesterverband festgelegt
	 */
	Vector<Semesterverband> svContainer = null;
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
		this.add(lbStudiengang);
		this.add(libStudiengang);
		this.add(lbSemverband);
		this.add(libSemverband);
		this.add(lbHalbjahr);
		this.add(tbHalbjahr);
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
							Vector<Studiengang> studiengang) {
						sgContainer = studiengang;
						for (Studiengang sg : studiengang) {
							libStudiengang.addItem(
									sg.getBezeichnung(),
									String.valueOf(sg.getId()));
						}
						getSemverband();
					}
				});

		/**
		 * Die Methode <code>addChangeHandler()</code> wird aufgerufen, wenn das Element der ListBox gändert wird.
		 * Dabei wird ein Interface {@link ChangeHandler} erzeugt, das durch eine anonyme Klasse implementiert und durch
		 * new instantiiert wird. Dieses Interface verlangt genau eine Methode <code>onChange()</code>, die 
		 * ein Objekt vom Typ ChangeEvent {@link ChangeEvent} erzeugt.
		 * 
		 * @param event wird abhängig vom Eventtyp {@link ChangeEvent} definiert
		 * 
		 * Anschließend wird festgelegt, was passiert wenn der das Element der ListBox sich ändert.
		 */
		libStudiengang.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				getSemverband();
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

				if (tbHalbjahr.getValue().isEmpty()) {
					allFilled = false;
					Window.alert("Bitte füllen Sie alle Felder aus.");
				}

				if (allFilled == true) {
					final String studienhalbjahr = tbHalbjahr
							.getValue().trim();
					int semesterverbandId = svContainer.elementAt(
							libSemverband.getSelectedIndex()).getId();

					 /**
			         * Wenn die Text Boxen befüllt sind, werden wir die Verwaltungsklasse durch Methode
			         * <code>createStundenplan()</code> bitten,
			         * einen neuen Stundenplan anzulegen und diesen uns über einen Callback
			         * zurückzuliefern.
			         */
					verwaltungsSvc.createStundenplan(studienhalbjahr,
							semesterverbandId,
							new AsyncCallback<Stundenplan>() {

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
							     * Durch die Methode <code>addStundenplan()</code> wird das erstellte Stundenplan-Objekt 
							     * dem Baum hinzugefügt.
							     * 
							     * @param result der Stundenplan, der neu erstellt wurde
							     */
								@Override
								public void onSuccess(
										Stundenplan result) {
									libStudiengang.clear();
									libSemverband.clear();
									tbHalbjahr.setText("");
									Window.alert("Erfolgreich gespeichert.");
									tvm.addStundenplan(result);
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
	
	/**
	 * Durch die Methode <code>clear()</code> werden zunächst alle Elemente
	 * der List Box gelöscht. 
	 * Anschließend werden wir durch die Methode <code>getSemsterverbaendeByStudiengang()</code>
	 * die Verwaltungsklasse bitten, uns über einen Callback alle Semesterverbände, die zu dem
	 * davor ausgwählten Studiengang gehören, in einem Vector 
	 * zurückzuliefern. Die Semesterverbände werden durch die Methode <code>addItem()</code>
	 * der List Box zugefügt.
	 */
	public void getSemverband() {
		libSemverband.clear();
		verwaltungsSvc.getSemsterverbaendeByStudiengang(
				sgContainer.elementAt(
						libStudiengang.getSelectedIndex()).getId(),
				new AsyncCallback<Vector<Semesterverband>>() {
					public void onFailure(Throwable caught) {
						caught.getMessage();
					}

					public void onSuccess(
							Vector<Semesterverband> semesterverband) {
						svContainer = semesterverband;
						for (Semesterverband sv : semesterverband) {
							libSemverband.addItem(sv.getJahrgang()
									+ ", Semester: "
									+ String.valueOf(sv.getSemester()));
						}

					}
				});
	}
	
}
