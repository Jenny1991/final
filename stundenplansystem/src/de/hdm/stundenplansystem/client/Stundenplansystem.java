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

import de.hdm.stundenplansystem.shared.VerwaltungsklasseAsync;



/**
 * Die Klasse Stundenplansystem 
 * Entry point classes define <code>onModuleLoad()</code>.
 
public class ItProjekt implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	*/
	/**
	 * EntryPoint Klasse des Projekts <b>ItProjekt</b>
	 * Diese Klasse implementiert das Interface <code>EntryPoint</code>
	   * daher ben������������tigen wir die Methode <code>public void onModuleLoad()</code>. 
	   * Sie ist das GWT-Pendant der <code>main()</code>-Methode normaler Java-Applikationen.
	 */

	public class Stundenplansystem implements EntryPoint {

		VerwaltungsklasseAsync verwaltungsSvc = null;
		
		private HTML ueberschrift = new HTML ("<h2>Herzlich Willkommen im Stundenplansystem der HdM<h2>");
		
		/*
	     * Ab hier bauen wir sukzessive den Navigator mit seinen Buttons aus.
	     */
	    final Button dozentButton = new Button ("Dozent");
	    final Button raumButton = new Button ("Raum");
	    final Button semesterverbandButton = new Button ("Semesterverband");
	    final Button lehrveranstaltungButton = new Button ("Lehrveranstaltung");
	    final Button studiengangButton = new Button ("Studiengang");
	    final Button stundenplaneintragButton = new Button ("Stundenplaneintrag");
	    final Button raumplanButton = new Button ("Raumplan");
	    final Button stundenplanButton = new Button ("Stundenplan");
	    
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
	    
	    public VerticalPanel detailsPanel = new VerticalPanel(); 
		
		@Override
		/**
		 * Initialisiert die Webseite, die beim ���������������ffnen als erstes angezeigt wird
		 */
		public void onModuleLoad() {
			
			detailsPanel.add(ueberschrift);
				
			/*
			 * Die Anwendung besteht aus zwei seperaten horizontalen Panels. Im rechten Panel wird ein Navigationsteil 
			 * mit Baumstruktur der Stamm,- und Bewegunsdaten, sowie des Reports realisiert.
			 * Im rechten Panel wird der Inhalt, einem Datenteil mit Formularen realisiert. 
		     * Daher bietet sich ein DockLayoutPanel als Container an.
		     *
		     */
		
			//NavTreeViewModel tvm = new NavTreeViewModel(df, lf, rf, zf, sgf, svf, this);
			NavTreeViewModel tvm = new NavTreeViewModel(cd, cl, cr, csg, csv, cspe, csp, df, lf, rf, sgf, svf, spef, spf, rr, rs, rsd, this);
			
			
			CellTree cellTree = new CellTree(tvm, "Root");
			
			RootLayoutPanel rlp = RootLayoutPanel.get();
			DockLayoutPanel mainPanel = new DockLayoutPanel(Unit.PX);
			rlp.add(mainPanel);
			
			final ScrollPanel navigation = new ScrollPanel();
			navigation.add(cellTree);
			
			mainPanel.addNorth(new HTML("<h1>Stundenplansystem</h1>"), 100);
			mainPanel.addWest(navigation, 300);
			mainPanel.add(detailsPanel);

			RootPanel.get("ItProjektFrame").add(rlp);
			
			
			/*
			detailsPanel.add(ueberschrift);
			
			detailsPanel.add(df);
			detailsPanel.add(lf);
			detailsPanel.add(rf);
			//detailsPanel.add(zf);
			detailsPanel.add(svf);
			detailsPanel.add(sgf);
			*/
			
			/**RootLayoutPanel rlp = RootLayoutPanel.get();
			SplitLayoutPanel mainPanel = new SplitLayoutPanel();
			rlp.add(mainPanel);
			
			VerticalPanel detailsPanel = new VerticalPanel();	
			
			ScrollPanel navigation = new ScrollPanel(new Tree());
			navigation.setHeight("300px");
			
			mainPanel.addWest(new HTML ("navigation"), 150);
			mainPanel.addEast(new HTML ("detailsPanel"), 350);
			
			RootPanel.get("ItProjektFrame").add(rlp);
			*/
				 
			
		  /**	SplitLayoutPanel s = new SplitLayoutPanel();
		 	s.addWest(new Label ("navigation"), 130);
		 	s.addEast(new Label ("details"), 300);
		 	//SplitLayoutPanel p = new SplitLayoutPanel();
		 	//s.addWest(new HTML ("details"), 200);
		 	//RootLayoutPanel rp = RootLayoutPanel.get();
		 	//rp.add(s);
		 	RootPanel.get().add(s);
		    //RootPanel.get("ItProjektFrame").add();
		     * 
		     */


		    /*
		     * Das DockLayoutPanel wird einem DIV-Element namens "Details" in der
		     * zugeh������������rigen HTML-Datei zugewiesen und erh������������lt so seinen Darstellungsort.
		     */
		    
		    
		    	    
		    /*
		     * Unter welchem Namen k������������nnen wir den Button durch die CSS-Datei des
		     * Projekts formatieren?
		     */
		    dozentButton.setStylePrimaryName("BaumButton");
		    raumButton.setStylePrimaryName("BaumButton");
		    semesterverbandButton.setStylePrimaryName("BaumButton");
		    lehrveranstaltungButton.setStylePrimaryName("BaumButton");
		    studiengangButton.setStylePrimaryName("BaumButton");
		    stundenplaneintragButton.setStylePrimaryName("BaumButton");
		    stundenplanButton.setStylePrimaryName("BaumButton");
		    raumplanButton.setStylePrimaryName("BaumButton");
		    
		    
		    
		    
		    
		    
		    /*
		     * Ab hier wird die Baumdarstellung mit den Zweigen Report, Stammdaten und Bewegungsdaten definiert
		     */
		    
		    /*Tree uebersicht = new Tree();
			
		    /*
		     * Zweig: Report mit Stundenplan und Raumplan
		     
			TreeItem report = new TreeItem();
			report.setText("Report");
			report.addItem(stundenplanButton);
			report.addItem(raumplanButton);
				
			stundenplanButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					ReportStundenplan spf = new ReportStundenplan();
					detailsPanel.clear();
					detailsPanel.add(spf);
				}
			});
			
			raumplanButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					ReportRaum rpf = new ReportRaum();
					detailsPanel.clear();
					detailsPanel.add(rpf);
				}
			});
			
			/*
			 * Zweig: Stammdaten mit Dozent, Zeitslot, Raum, Studiengang, Semesterverband, Lehrveranstaltung
			 
			
			TreeItem stammdaten = new TreeItem();
			stammdaten.setText("Stammdaten");
			stammdaten.addItem(dozentButton);
			stammdaten.addItem(zeitslotButton);
			stammdaten.addItem(raumButton);
			stammdaten.addItem(studiengangButton);
			stammdaten.addItem(semesterverbandButton);
			stammdaten.addItem(lehrveranstaltungButton);
			

			dozentButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
				DozentForm df = new DozentForm();
				detailsPanel.clear();
				detailsPanel.add(df);
				}
			});
			
			zeitslotButton.addClickHandler(new ClickHandler(){
				public void onClick(ClickEvent event) {
					ZeitslotForm zf = new ZeitslotForm();
					detailsPanel.clear();
					detailsPanel.add(zf);
				}
			});
			
			raumButton.addClickHandler(new ClickHandler(){
				public void onClick(ClickEvent event) {
					RaumForm rf = new RaumForm();
					detailsPanel.clear();
					detailsPanel.add(rf);
				}
			});
			
			studiengangButton.addClickHandler(new ClickHandler(){
				public void onClick(ClickEvent event) {
					StudiengangForm sgf = new StudiengangForm();
					detailsPanel.clear();
					detailsPanel.add(sgf);
				}
			});
			
			semesterverbandButton.addClickHandler(new ClickHandler(){
				public void onClick(ClickEvent event) {
					SemesterverbandForm svf = new SemesterverbandForm();
					detailsPanel.clear();
					detailsPanel.add(svf);
				}
			});
			
			lehrveranstaltungButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					LehrveranstaltungForm lv = new LehrveranstaltungForm();
					detailsPanel.clear();
					detailsPanel.add(lv);
				}
			});
			
			
			/*
			 * Zweig: Bewegungsdaten mit Stundeplaneintrag
			 
			
			TreeItem bewegungsdaten = new TreeItem();
			bewegungsdaten.setText("Bewegungsdaten");
			bewegungsdaten.addItem(stundenplaneintragButton);
			
			stundenplaneintragButton.addClickHandler(new ClickHandler(){
				public void onClick(ClickEvent event) {
					StundenplaneintragForm sef = new StundenplaneintragForm();
					detailsPanel.clear();
					detailsPanel.add(sef);
				}
			});
			
			
			/*
			 * Hier wird der Baum aus den drei Zweigen zusammengebaut und im Layout der Navigation zugeordnet
			 
			uebersicht.addItem(report);
			uebersicht.addItem(stammdaten);
			uebersicht.addItem(bewegungsdaten);
			

			navigation.add(uebersicht);*/
			
		}
		
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
		public void createStundenplanForm(){
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
		
		public void showStundenplanForm(){
			detailsPanel.clear();
			detailsPanel.add(spf);
		}
		
		public void showReportRaum(){
			detailsPanel.clear();
			detailsPanel.add(rr);
		}
		
		public void showReportStundenplan(){
			detailsPanel.clear();
			detailsPanel.add(rs);
		}
		
		public void showReportStundenplanDozent(){
			detailsPanel.clear();
			detailsPanel.add(rsd);
		}
		
		public void append(String text) {
		    HTML content = new HTML(text);
		    detailsPanel.add(content);		
		} 
	}
