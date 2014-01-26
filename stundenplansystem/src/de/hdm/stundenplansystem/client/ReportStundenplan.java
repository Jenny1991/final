package de.hdm.stundenplansystem.client;

import java.util.Vector;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;

import de.hdm.stundenplansystem.client.Content;
import de.hdm.stundenplansystem.shared.*;
import de.hdm.stundenplansystem.shared.bo.Studiengang;
import de.hdm.stundenplansystem.shared.bo.Stundenplan;
import de.hdm.stundenplansystem.shared.bo.Semesterverband;
import de.hdm.stundenplansystem.shared.report.*;

/**
 * Die Klasse demonstriert das Anzeigen eines Reports bezüglich 
 * Stundenplänen für Semesterverbände. Demonstration der Nutzung des Report Generators.
 * Diese Klasse erbt von der Klasse Content und lässt sich somit 
 * unter GWT entsprechend anordnen.
 * 
 * @see ReportRaum
 * @author Thies, Espich
 * @version 1.0
 */
public class ReportStundenplan extends Content {

	/**
	 * Jede Klasse enthält eine Überschrift, die definiert, was der User machen
	 * kann.
	 */
	HTML ueberschrift = new HTML("<h2>Stundenplan für Studenten</h2>");

	/** 
	 * Hier werden die GWT Widgets instantiiert
	 */
	final Label lbStundenplan = new Label("Stundenplan:");
	final Label lbSemverband = new Label("Semesterverband:");
	final Label lbStudiengang = new Label("Studiengang:");
	final ListBox libStundenplan = new ListBox();
	final ListBox libSemverband = new ListBox();
	final ListBox libStudiengang = new ListBox();
	final Button anzeigen = new Button("Stundenplan anzeigen");
	final ScrollPanel panel = new ScrollPanel();
	HTML feld = new HTML();

	/**
	 * Hier wird zwei Remote Service Proxy erstellt, welches uns erlaubt, 
	 * mit dem serverseitigen Verwaltungsservice
	 * sowie dem Reportservice zu kommunizieren.
	 */
	final VerwaltungsklasseAsync verwaltungsSvc = GWT
			.create(Verwaltungsklasse.class);
	final ReportGeneratorAsync reportSvc = GWT
			.create(ReportGenerator.class);
	
	/**
	 * Hier werden Vectoren des Semestervedbands, Stundenplans
	 * und Studiengangs festgelegt
	 */
	Vector<Semesterverband> svContainer = null;
	Vector<Stundenplan> spContainer = null;
	Vector<Studiengang> sgContainer = null;
	NavTreeViewModel tvm;
	Studiengang sg;
	Integer sv;
	String test;

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
		this.add(lbStundenplan);
		this.add(libStundenplan);
		this.add(anzeigen);
		this.add(panel);
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

		libSemverband.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				getStundenplan();
			}
		});

		/**
		 * Beim Betätigen des Anzeige-Buttons wird die Methode <code>addClickHandler()</code> 
		 * aufgerufen. Dabei wird ein Interface {@link ClickHandler} erzeugt, 
		 * das durch eine anonyme Klasse implementiert und durch new instantiiert wird. 
		 * Dieses Interface verlangt genau eine Methode <code>onClick()</code>, die 
		 * ein Objekt vom Typ ClickEvent {@link ClickEvent} erzeugt.
		 * 
		 * @param event wird abhängig vom Eventtyp {@link ClickEvent} definiert
		 * 
		 * Anschließend wird festgelegt, was passiert wenn der Anzeige-Button gedrückt wurde.
		 */
		anzeigen.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				 /**
		         * Wir bitten den ReportGenerator durch Methode
		         * <code>createStundenplanSemesterverbandReport()</code>, einen neuen 
		         * Report für Semesterverbände anzulegen und diesen uns über einen Callback
		         * zurückzuliefern.
		         */
				reportSvc.createStundenplanSemesterverbandReport(
						svContainer.elementAt(
								libSemverband.getSelectedIndex())
								.getId(),
						spContainer.elementAt(
								libStundenplan.getSelectedIndex())
								.getId(),
						new AsyncCallback<StundenplanSemesterverbandReport>() {

							/**
							 * Die Methode <code>onSuccess()</code> wird durch die GWT-RPC Runtime aufgerufen,
							 * wenn wie erwartet das Ergebnis des Funktionsaufrufs vom Server an den
							 * Client geliefert wird.
							 * 
							 *  @param result Report für einen Semesterveband
							 */
							@Override
							public void onSuccess(
									StundenplanSemesterverbandReport result) {

								HTMLReportWriter writer = new HTMLReportWriter();
								writer.process(result);
								test = writer.getReportText();
								panel.add(new HTML(test));
							}

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
								caught.getMessage();
							}
						});
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
					public void onFailure(Throwable T) {

					}

					public void onSuccess(
							Vector<Semesterverband> semesterverband) {
						svContainer = semesterverband;
						for (Semesterverband sv : semesterverband) {
							libSemverband.addItem(sv.getKuerzel()
									+ ", Semester: "
									+ String.valueOf(sv.getSemester()));
						}
						getStundenplan();
					}
				});
	}

	/**
	 * Durch die Methode <code>clear()</code> werden zunächst alle Elemente
	 * der List Box gelöscht. 
	 * Anschließend werden wir durch die Methode <code>getStundenplaeneBySemesterverband()</code>
	 * die Verwaltungsklasse bitten, uns über einen Callback alle Stundenpläne, die zu dem
	 * davor ausgwählten Semesterverband gehören, in einem Vector 
	 * zurückzuliefern. Die Stundenpläne werden durch die Methode <code>addItem()</code>
	 * der List Box zugefügt.
	 */
	public void getStundenplan() {
		libStundenplan.clear();
		verwaltungsSvc.getStundenplaeneBySemesterverband(svContainer
				.elementAt(libSemverband.getSelectedIndex()).getId(),
				new AsyncCallback<Vector<Stundenplan>>() {
					public void onFailure(Throwable T) {

					}

					public void onSuccess(
							Vector<Stundenplan> stundenplaene) {
						spContainer = stundenplaene;
						for (Stundenplan sp : stundenplaene) {
							libStundenplan.addItem(
									sp.getStudienhalbjahr(),
									String.valueOf(sp.getId()));
						}
					}
				});
	}
}
