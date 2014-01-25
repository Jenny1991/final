package de.hdm.stundenplansystem.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;

/**
 * Stundenplansystem ist die Hauptklasse des Projekts <b>stundenplansystem</b>
 * 
 * Diese Klasse implementiert das Interface <code>EntryPoint</code> Zunächst
 * werden alle Panels der Klasse instantiiert.
 * 
 */

public class Stundenplansystem implements EntryPoint {

	final ScrollPanel navigation = new ScrollPanel();
	public ScrollPanel detailsPanel = new ScrollPanel();

	/**
	 * Jede Klasse enthält eine Überschrift, die definiert, was der User machen
	 * kann.
	 */
	private HTML ueberschritgesamt = new HTML(
			"<h1>Stundenplansystem</h1>");
	private HTML ueberschrift = new HTML(
			"<h2>Herzlich Willkommen im Stundenplansystem der HdM<h2>");

	/**
	 * Hier bauen wir sukzessive den Navigator mit seinen Buttons aus.
	 */
	final Button dozentButton = new Button("Dozent");
	final Button raumButton = new Button("Raum");
	final Button semesterverbandButton = new Button("Semesterverband");
	final Button lehrveranstaltungButton = new Button(
			"Lehrveranstaltung");
	final Button studiengangButton = new Button("Studiengang");
	final Button stundenplaneintragButton = new Button(
			"Stundenplaneintrag");
	final Button raumplanButton = new Button("Raumplan");
	final Button stundenplanButton = new Button("Stundenplan");

	/**
	 * Hier instantiieren wir alle Klassen.
	 */
	public DozentForm df = new DozentForm();
	public LehrveranstaltungForm lf = new LehrveranstaltungForm();
	public RaumForm rf = new RaumForm();
	public StudiengangForm sgf = new StudiengangForm();
	public SemesterverbandForm svf = new SemesterverbandForm();
	public StundenplaneintragForm spef = new StundenplaneintragForm();
	public StundenplanForm spf = new StundenplanForm();
	public ReportRaum rr = new ReportRaum();
	public ReportStundenplan rs = new ReportStundenplan();
	public ReportStundenplanDozent rsd = new ReportStundenplanDozent();

	public CreateDozent cd = new CreateDozent();
	public CreateLehrveranstaltung cl = new CreateLehrveranstaltung();
	public CreateRaum cr = new CreateRaum();
	public CreateSemesterverband csv = new CreateSemesterverband();
	public CreateStudiengang csg = new CreateStudiengang();
	public CreateStundenplaneintrag cspe = new CreateStundenplaneintrag();
	public CreateStundenplan csp = new CreateStundenplan();

	/**
	 * Wir benötigen die Methode <code>public void onModuleLoad()</code>, die
	 * durch das EntryPoint Interface vorgegeben ist. Diese ist das GWT-Pendant
	 * der <code>main()</code>-Methode normaler Java-Applikationen.
	 */
	public void onModuleLoad() {

		/**
		 * Die Anwendung besteht aus zwei seperaten horizontalen Panels, sowie
		 * einem vertikalen Panel. Daher bietet sich ein DockLayoutPanel als
		 * Container an. Wir erstellen eine Instanz des DockLayoutPanels,
		 * welches durch die Methode <code>add()</code> dem RootPanel zugeordnet
		 * wird und somit den Aufbau des Projekts definiert. Durch die Methode
		 * <code>addNorth()</code> bekommt unser Projekt die Überschrift
		 * "Stundenplansystem".
		 * 
		 * @param ueberschriftgesamt
		 *            Überschrift der Klasse, die im oberen Teil des Panels,
		 *            welches 100 Pixel beträgt, erscheint. Die Methode
		 *            <code>addWest()</code> fügt im linken Teil des Panels den
		 *            Baum zur Navigation hinzu.
		 * @param navigation
		 *            Instanz der Klasse Scroll Panel, welches 300 Pixel breit
		 *            ist Die Methode <code>add()</code> fügt im rechten Teil
		 *            des Panels die Instanz des Vertical Panels hinzu, die den
		 *            Inhalt, einem Datenteil mit Formularen, realisiert.
		 * @param detailsPanel
		 *            Instanz der Klasse Vertical Panel Im linken Panel wird ein
		 *            Navigationsteil mit Baumstruktur der Stamm,- und
		 *            Bewegungsdaten, sowie des Reports realisiert. Zunächst
		 *            erstellen wir eine Instanz der Klasse NavTreeModel, das
		 *            Inhalt des Baums definiert. Anschließend erstellen wir
		 *            eine Instanz der Klasse CellTree. Diese lässt die
		 *            Hierarchie der Knoten (nodes) des Baums zu, so kann es
		 *            Leafs und Children geben.
		 * @param tvm
		 *            Instanz der Klasse NavTreeModel
		 * @param "Root" Definiert den Wurzelknoten des Baums Wir weisen dem
		 *        Scroll Panel durch die Methode <code>add()</code> die Instanz
		 *        der Klasse CellTree zu. Im rechten Panel wir zunächst nur die
		 *        Überschrift der jeweiligen Seite durch die Methode
		 *        <code>add()</code> zugewiesen.
		 */
		RootLayoutPanel rlp = RootLayoutPanel.get();
		DockLayoutPanel mainPanel = new DockLayoutPanel(Unit.PX);
		rlp.add(mainPanel);

		mainPanel.addNorth(ueberschritgesamt, 100);
		mainPanel.addWest(navigation, 300);
		mainPanel.add(detailsPanel);

		RootPanel.get("ItProjektFrame").add(rlp);

		NavTreeViewModel tvm = new NavTreeViewModel(cd, cl, cr, csg,
				csv, cspe, csp, df, lf, rf, sgf, svf, spef, spf, rr,
				rs, rsd, this);

		CellTree cellTree = new CellTree(tvm, "Root");
		navigation.add(cellTree);

		detailsPanel.add(ueberschrift);

		/**
		 * Hier wird definiert unter welchem Namen wir Buttons durch die
		 * CSS-Datei des Projekts formatieren können.
		 */
		dozentButton.setStylePrimaryName("BaumButton");
		raumButton.setStylePrimaryName("BaumButton");
		semesterverbandButton.setStylePrimaryName("BaumButton");
		lehrveranstaltungButton.setStylePrimaryName("BaumButton");
		studiengangButton.setStylePrimaryName("BaumButton");
		stundenplaneintragButton.setStylePrimaryName("BaumButton");
		stundenplanButton.setStylePrimaryName("BaumButton");
		raumplanButton.setStylePrimaryName("BaumButton");
	}

	/**
	 * Hier weisen wir dem rechten Panel durch die Methode <code>add()</code>den
	 * Datenteil zum Erstellen sowie die Formulare für das ausgwählte Objekt zu.
	 * Die Methode <code>clear()</code> löscht zuvor den Inhalt des gesamten
	 * Panels damit neue Klassen angezeigt werden
	 */

	public void createDozentForm() {
		detailsPanel.clear();
		detailsPanel.add(cd);
	}

	public void createLvForm() {
		detailsPanel.clear();
		detailsPanel.add(cl);
	}

	public void createRaumForm() {
		detailsPanel.clear();
		detailsPanel.add(cr);
	}

	public void createSvForm() {
		detailsPanel.clear();
		detailsPanel.add(csv);
	}

	public void createSgForm() {
		detailsPanel.clear();
		detailsPanel.add(csg);
	}

	public void createSpeForm() {
		detailsPanel.clear();
		detailsPanel.add(cspe);
	}

	public void createStundenplanForm() {
		detailsPanel.clear();
		detailsPanel.add(csp);
	}

	public void showDozentForm() {
		detailsPanel.clear();
		detailsPanel.add(df);
	}

	public void showLehrveranstaltungForm() {
		detailsPanel.clear();
		detailsPanel.add(lf);
	}

	public void showRaumForm() {
		detailsPanel.clear();
		detailsPanel.add(rf);
	}

	public void showStudiengangForm() {
		detailsPanel.clear();
		detailsPanel.add(sgf);
	}

	public void showSemesterverbandForm() {
		detailsPanel.clear();
		detailsPanel.add(svf);
	}

	public void showSpeForm() {
		detailsPanel.clear();
		detailsPanel.add(spef);
	}

	public void showStundenplanForm() {
		detailsPanel.clear();
		detailsPanel.add(spf);
	}

	public void showReportRaum() {
		detailsPanel.clear();
		detailsPanel.add(rr);
	}

	public void showReportStundenplan() {
		detailsPanel.clear();
		detailsPanel.add(rs);
	}

	public void showReportStundenplanDozent() {
		detailsPanel.clear();
		detailsPanel.add(rsd);
	}
}
