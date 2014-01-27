package de.hdm.stundenplansystem.client;

import java.util.Vector;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;

import de.hdm.stundenplansystem.shared.*;
import de.hdm.stundenplansystem.shared.bo.Raum;
import de.hdm.stundenplansystem.shared.bo.Stundenplan;
import de.hdm.stundenplansystem.shared.report.HTMLReportWriter;
import de.hdm.stundenplansystem.shared.report.RaumbelegungsReport;

/**
 * Die Klasse demonstriert das Anzeigen eines Reports bezüglich 
 * Raumbelegungen. Demonstration der Nutzung des Report Generators.
 * Diese Klasse erbt von der Klasse Content und lässt sich somit 
 * unter GWT entsprechend anordnen.
 * 
 * @author Thies, Espich
 * @version 1.0
 */
public class ReportRaum extends Content {

	/**
	 * Jede Klasse enthält eine Überschrift, die definiert, was der User machen
	 * kann.
	 */
	HTML ueberschrift = new HTML("<h2>Raumbelegungsplan</h2>");

	/** 
	 * Hier werden die GWT Widgets instantiiert
	 */
	final ListBox libRaum = new ListBox();
	final ListBox libStudienhalbjahr = new ListBox();
	final Button anzeigen = new Button("Raumbelegungen anzeigen");
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
	 * Hier werden Vectoren des Raums festgelegt
	 */
	Vector<Stundenplan> sContainer = null;
	Vector<Raum> rContainer = null;
	NavTreeViewModel tvm;
	String test;

	/**
	   * Jedes GWT Widget muss die Methode <code>onLoad()</code> implementieren. 
	   * Sie gibt an, was geschehen soll, 
	   * wenn eine Widget-Instanz zur Anzeige gebracht wird.
	   * Durch die Methode <code>add()</code> werden die Widgets dem Panel hinzugefügt.
	   */
	public void onLoad() {
		
		this.add(ueberschrift);
		this.add(libStudienhalbjahr);
		this.add(libRaum);
		this.add(anzeigen);
		this.add(panel);
		setTvm(tvm);
		
		/**
		 * Durch die Methode <code>clear()</code> werden zunächst alle Elemente
		 * der List Box gelöscht. 
		 * Anschließend werden wir durch die Methode <code>getAllRaeume()</code>
		 * die Verwaltungsklasse bitten, uns über einen Callback alle Räume in einem Vector 
		 * zurückzuliefern. Die Räume werden durch die Methode <code>addItem()</code>
		 * der List Box zugefügt.
		 */
		libRaum.clear();
		verwaltungsSvc
				.getAllRaeume(new AsyncCallback<Vector<Raum>>() {
					public void onFailure(Throwable T) {
					}

					public void onSuccess(Vector<Raum> raum) {
						rContainer = raum;
						for (Raum r : raum) {
							libRaum.addItem(r.getBezeichnung(),
									String.valueOf(r.getId()));
						}
					}
				});
		
		libStudienhalbjahr.clear();
		verwaltungsSvc	
				.getAllStudienhalbjahre(new AsyncCallback<Vector<Stundenplan>>() {
					@Override
					public void onFailure(Throwable T) {
					}

					@Override
					public void onSuccess(Vector<Stundenplan> studienhalbjahr) {
						sContainer = studienhalbjahr;
//						int i = 0;
						for (Stundenplan s : studienhalbjahr) {
//							sContainer.add(i, s);
//							sContainer.set(i, s.setId(i));
//							s.setId(i);
//							sContainer.addElement(s);
							libStudienhalbjahr.addItem(s.getStudienhalbjahr(),
									String.valueOf(s.getId()));
//							i++;
						}						
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
		         * <code>createRaumbelungsReport()</code>, einen neuen 
		         * RaumbelegungsReport anzulegen und diesen uns über einen Callback
		         * zurückzuliefern.
		         */
				reportSvc.createRaumbelungsReport(rContainer
						.elementAt(libRaum.getSelectedIndex())
						.getId(),
						sContainer.elementAt(libStudienhalbjahr.getSelectedIndex()).getStudienhalbjahr(),
						new AsyncCallback<RaumbelegungsReport>() {

							/**
							 * Die Methode <code>onSuccess()</code> wird durch die GWT-RPC Runtime aufgerufen,
							 * wenn wie erwartet das Ergebnis des Funktionsaufrufs vom Server an den
							 * Client geliefert wird.
							 * Zunächst wird eine Instanz der KLasse {@link HTMLReportWriter} erzeugt, 
							 * welches daraufhin durch die Methode <code>process()</code> den Inhalt 
							 * des generierten Stundenplanreports in eine HTML einbettet. 
							 * Die Methode <code>getReportText()</code> wandelt diese in einen String um, 
							 * welcher durch die Methode <code>add()</code> als neue HTML dem 
							 * {@link ScrollPanel} hinzugefügt wird.
							 * 
							 *  @param result Report für einen Raum
					 		 */
							@Override
							public void onSuccess(
									RaumbelegungsReport result) {

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
								Window.alert(caught.getMessage());
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
}
