package de.hdm.stundenplansystem.client;

import java.util.Vector;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Button;

import de.hdm.stundenplansystem.shared.*;
import de.hdm.stundenplansystem.shared.bo.Dozent;
import de.hdm.stundenplansystem.shared.bo.Lehrveranstaltung;
import de.hdm.stundenplansystem.shared.bo.Raum;
import de.hdm.stundenplansystem.shared.bo.Semesterverband;
import de.hdm.stundenplansystem.shared.bo.Studiengang;
import de.hdm.stundenplansystem.shared.bo.Stundenplan;
import de.hdm.stundenplansystem.shared.bo.Stundenplaneintrag;
import de.hdm.stundenplansystem.shared.bo.Zeitslot;

/**
 * Klasse, in der ein neuer Stundenplaneintrag angelegt wird.
 * Diese Klasse erbt von der Klasse Content und lässt sich somit 
 * unter GWT entsprechend anordnen.
 * 
 * @see CreateStundenplan
 * @author Thies, Espich, V. Hofmann
 * @version 1.0
 */

public class CreateStundenplaneintrag extends Content {

	/** 
	 * Hier werden die GWT Widgets instantiiert
	 */
	private final HTML ueberschrift = new HTML(
			"<h2>Neuen Stundenplaneintrag anlegen<h2>");

	/**
	 * Unter der Überschrift wählt der User die Daten des
	 * anzulegenden Stundenplaneintrags mit Hilfe von List Boxen.
	 */
	final Label lbDozent = new Label("Dozent:");
	final Label lbZeitslot = new Label("Zeitslot:");
	final Label lbRaum = new Label("Raum:");
	final Label lbStudienhj = new Label("Studienhalbjahr des Stundenplans:");
	final Label lbSemesterverband = new Label("Semester:");
	final Label lbLehrveranstaltung = new Label("Lehrveranstaltung:");
	final Label lbStudiengang = new Label("Studiengang:");

	final ListBox listDozent = new ListBox();
	final ListBox listZeitslot = new ListBox();
	final ListBox listRaum = new ListBox();
	final ListBox listStudienhj = new ListBox();
	final ListBox listSemesterverband = new ListBox();
	final ListBox listLehrveranstaltung = new ListBox();
	final ListBox listStudiengang = new ListBox();
	final Button speichern = new Button("speichern");

	/**
	 * Hier werden Vectoren des Business Objekte festgelegt
	 */
	Vector<Dozent> dozentenContainer = null;
	Vector<Zeitslot> zeitslotContainer = null;
	Vector<Raum> raumContainer = null;
	Vector<Stundenplan> spContainer = null;
	Vector<Semesterverband> svContainer = null;
	Vector<Lehrveranstaltung> lvContainer = null;
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
		this.add(listStudiengang);
		this.add(lbSemesterverband);
		this.add(listSemesterverband);
		this.add(lbStudienhj);
		this.add(listStudienhj);
		this.add(lbLehrveranstaltung);
		this.add(listLehrveranstaltung);
		this.add(lbDozent);
		this.add(listDozent);
		this.add(lbRaum);
		this.add(listRaum);
		this.add(lbZeitslot);
		this.add(listZeitslot);
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
		listStudiengang.clear();
		verwaltungsSvc
				.getAllStudiengaenge(new AsyncCallback<Vector<Studiengang>>() {
					@Override
					public void onFailure(Throwable caught) {
					}

					@Override
					public void onSuccess(Vector<Studiengang> result) {
						sgContainer = result;
						for (Studiengang sg : result) {
							listStudiengang.addItem(
									sg.getBezeichnung(),
									String.valueOf(sg.getId()));
						}
						getSemesterverband();
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

				int d = listDozent.getSelectedIndex();
				int l = listLehrveranstaltung.getSelectedIndex();
				int r = listRaum.getSelectedIndex();
				int z = listZeitslot.getSelectedIndex();
				int sp = listStudienhj.getSelectedIndex();

				 /**
		         * Wenn die Text Boxen befüllt sind, werden wir die Verwaltungsklasse durch Methode
		         * <code>createStundenplaneintrag()</code> bitten,
		         * einen neuen Stundenplaneintrag anzulegen und diesen uns über einen Callback
		         * zurückzuliefern.
		         */
				verwaltungsSvc.createStundenplaneintrag(
						dozentenContainer.elementAt(d).getId(),
						lvContainer.elementAt(l).getId(),
						raumContainer.elementAt(r).getId(),
						zeitslotContainer.elementAt(z).getId(),
						spContainer.elementAt(sp).getId(),
						new AsyncCallback<Stundenplaneintrag>() {
							
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
						     * Durch die Methode <code>addStundenplaneintrag()</code> wird das erstellte 
						     * Stundenplaneintrag-Objekt dem Baum hinzugefügt.
						     * 
						     * @param result der Stundenplaneintrag, der neu erstellt wurde
						     */
							@Override
							public void onSuccess(
									Stundenplaneintrag result) {
								Window.alert("Der Stundenplaneintrag wurde erfolgreich gespeichert.");
								tvm.addStundenplaneintrag(result);
							}
						});
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
		listStudiengang.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				getSemesterverband();
			}
		});

		listSemesterverband.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				getStundenplan();
			}
		});

		listRaum.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				ladeAlleZeitslots();
			}
		});
		
		listDozent.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				ladeAlleZeitslots();
			}
		});
		
		listStudienhj.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				ladeAlleZeitslots();
			}
		});
		
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
	public void getSemesterverband() {
		listSemesterverband.clear();
		verwaltungsSvc.getSemsterverbaendeByStudiengang(
				sgContainer.elementAt(
						listStudiengang.getSelectedIndex()).getId(),
				new AsyncCallback<Vector<Semesterverband>>() {
					public void onFailure(Throwable caught) {
					}

					public void onSuccess(
							Vector<Semesterverband> semesterverband) {
						svContainer = semesterverband;
						for (Semesterverband sv : semesterverband) {
							listSemesterverband.addItem(sv.getKuerzel()
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
		listStudienhj.clear();
		verwaltungsSvc.getStundenplaeneBySemesterverband(svContainer
				.elementAt(listSemesterverband.getSelectedIndex())
				.getId(), new AsyncCallback<Vector<Stundenplan>>() {
			public void onFailure(Throwable caught) {
			}

			public void onSuccess(Vector<Stundenplan> stundenplaene) {
				spContainer = stundenplaene;
				for (Stundenplan sp : stundenplaene) {
					listStudienhj.addItem(sp.getStudienhalbjahr(),
							String.valueOf(sp.getId()));
				}
				ladeAlleLehrveranstaltungen();
			}
		});
	}

	/**
	 * Durch die Methode <code>clear()</code> werden zunächst alle Elemente
	 * der List Box gelöscht. 
	 * Anschließend werden wir durch die Methode <code>getAllLehrveranstaltungen()</code>
	 * die Verwaltungsklasse bitten, uns über einen Callback alle Lehrveranstaltungen in einem Vector 
	 * zurückzuliefern. Die Lehrveranstaltungen werden durch die Methode <code>addItem()</code>
	 * der List Box zugefügt.
	 */
	public void ladeAlleLehrveranstaltungen() {
		listLehrveranstaltung.clear();
		verwaltungsSvc
				.getAllLehrveranstaltungen(new AsyncCallback<Vector<Lehrveranstaltung>>() {
					@Override
					public void onFailure(Throwable caught) {
					}

					@Override
					public void onSuccess(
							Vector<Lehrveranstaltung> result) {
						lvContainer = result;
						for (Lehrveranstaltung lv : result) {
							listLehrveranstaltung.addItem(
									lv.getBezeichnung(),
									String.valueOf(lv.getId()));
						}
						ladeAlleDozenten();
					}
				});
	}

	/**
	 * Durch die Methode <code>clear()</code> werden zunächst alle Elemente
	 * der List Box gelöscht. 
	 * Anschließend werden wir durch die Methode <code>getAllDozenten()</code>
	 * die Verwaltungsklasse bitten, uns über einen Callback alle Dozenten in einem Vector 
	 * zurückzuliefern. Die Dozenten werden durch die Methode <code>addItem()</code>
	 * der List Box zugefügt.
	 */
	public void ladeAlleDozenten() {
		listDozent.clear();
		verwaltungsSvc
				.getAllDozenten(new AsyncCallback<Vector<Dozent>>() {
					@Override
					public void onFailure(Throwable caught) {
					}

					@Override
					public void onSuccess(Vector<Dozent> result) {
						dozentenContainer = result;
						for (Dozent d : result) {
							listDozent.addItem(d.getNachname() + ", "
									+ d.getVorname(),
									String.valueOf(d.getId()));
						}
						ladeAlleRaeume();
					}
				});
	}

	/**
	 * Durch die Methode <code>clear()</code> werden zunächst alle Elemente
	 * der List Box gelöscht. 
	 * Anschließend werden wir durch die Methode <code>getAllRaeume()</code>
	 * die Verwaltungsklasse bitten, uns über einen Callback alle Räume in einem Vector 
	 * zurückzuliefern. Die Räume werden durch die Methode <code>addItem()</code>
	 * der List Box zugefügt.
	 */
	public void ladeAlleRaeume() {
		listRaum.clear();
		verwaltungsSvc
				.getAllRaeume(new AsyncCallback<Vector<Raum>>() {
					@Override
					public void onFailure(Throwable caught) {
					}

					@Override
					public void onSuccess(Vector<Raum> result) {
						raumContainer = result;
						for (Raum r : result) {
							listRaum.addItem(r.getBezeichnung(),
									String.valueOf(r.getId()));
						}
						ladeAlleZeitslots();
					}
				});
	}

	/**
	 * Durch die Methode <code>clear()</code> werden zunächst alle Elemente
	 * der List Box gelöscht. 
	 * Anschließend werden wir durch die Methode <code>getFreieZeitslot()</code>
	 * die Verwaltungsklasse bitten, uns über einen Callback alle Zeitslots, die den
	 * davor eingegebenen Anforderungen entspricht, in einem Vector 
	 * zurückzuliefern. Die Zeitslots werden durch die Methode <code>addItem()</code>
	 * der List Box zugefügt.
	 */
	public void ladeAlleZeitslots() {
		listZeitslot.clear();
		verwaltungsSvc.getFreieZeitslot(
				raumContainer.elementAt(listRaum.getSelectedIndex())
						.getId(),
				dozentenContainer.elementAt(
						listDozent.getSelectedIndex()).getId(),
				spContainer.elementAt(
						listStudienhj.getSelectedIndex()).getStudienhalbjahr(),
				spContainer.elementAt(
						listStudienhj.getSelectedIndex()).getId(),
				new AsyncCallback<Vector<Zeitslot>>() {
					@Override
					public void onFailure(Throwable caught) {
					}

					@Override
					public void onSuccess(Vector<Zeitslot> result) {
						zeitslotContainer = result;
						for (Zeitslot zs : result) {
							listZeitslot.addItem(zs.getWochentag()
									+ ", " + zs.getAnfangszeit()
									+ ", " + zs.getEndzeit(),
									String.valueOf(zs.getId()));
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
