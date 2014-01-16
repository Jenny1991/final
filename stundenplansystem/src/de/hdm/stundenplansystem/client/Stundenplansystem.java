package de.hdm.stundenplansystem.client;

//import net.sourceforge.htmlunit.corejs.javascript.ast.FunctionNode.Form;
//import de.hdm.itprojekt.shared.FieldVerifier;
//import com.google.appengine.api.images.Image.Format;
import com.google.gwt.core.client.EntryPoint;

//import com.google.gwt.core.client.GWT;
//import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.cellview.client.CellTree;
//import com.google.gwt.event.dom.client.ClickEvent;
//import com.google.gwt.event.dom.client.ClickHandler;
//import com.google.gwt.user.cellview.client.CellTree;
//import com.google.gwt.user.client.Window;
//import com.google.gwt.event.dom.client.KeyCodes;
//import com.google.gwt.event.dom.client.KeyUpEvent;
//import com.google.gwt.event.dom.client.KeyUpHandler;
//import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
//import com.google.gwt.user.client.ui.DialogBox;
//import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTML;
//import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
//import com.google.gwt.user.client.ui.ScrollPanel;
//import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
//import com.google.gwt.user.client.ui.SplitLayoutPanel;
//import com.google.gwt.user.client.ui.Tree;
//import com.google.gwt.user.client.ui.TreeItem;
//import com.google.gwt.user.client.ui.Widget;
//import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.DockLayoutPanel;

//import de.hdm.stundenplansystem.client.*;
import de.hdm.stundenplansystem.shared.VerwaltungsklasseAsync;



/**
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
		
		private final HTML ueberschrift = new HTML ("<h2>Herzlich Willkommen im Stundenplansystem der HdM<h2>");
		
		
		/*
	     * Ab hier bauen wir sukzessive den Navigator mit seinen Buttons aus.
	     */
	    final Button dozentButton = new Button ("Dozent");
	    final Button zeitslotButton = new Button ("Zeitslot");
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
	    public ZeitslotForm zf = new ZeitslotForm();
	    public StudiengangForm sgf = new StudiengangForm();
	    public SemesterverbandForm svf = new SemesterverbandForm();
	    public CreateDozent cd = new CreateDozent();
	    public CreateLehrveranstaltung cl = new CreateLehrveranstaltung();
	    public CreateRaum cr = new CreateRaum();
	    public CreateSemesterverband csv = new CreateSemesterverband();
	    public CreateStudiengang csg = new CreateStudiengang();
	    public CreateStundenplaneintrag cspe = new CreateStundenplaneintrag();
	    
	    public VerticalPanel detailsPanel = new VerticalPanel(); 
		
		@Override
		/**
		 * Initialisiert die Webseite, die beim ���������������ffnen als erstes angezeigt wird
		 */
		public void onModuleLoad() {
			
			
				
			/*
			 * Die Anwendung besteht aus zwei seperaten horizontalen Panels. Im rechten Panel wird ein Navigationsteil 
			 * mit Baumstruktur der Stamm,- und Bewegunsdaten, sowie des Reports realisiert.
			 * Im rechten Panel wird der Inhalt, einem Datenteil mit Formularen realisiert. 
		     * Daher bietet sich ein DockLayoutPanel als Container an.
		     *
		     */
		
			//NavTreeViewModel tvm = new NavTreeViewModel(df, lf, rf, zf, sgf, svf, this);
			NavTreeViewModel tvm = new NavTreeViewModel(cd, cl, cr, csg,csv, this);
			
			
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
		    zeitslotButton.setStylePrimaryName("BaumButton");
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
	}


		    
		    
		    
		    
		    
		    
		    
		    /*
		     * Hinzuf������������gen des Baums zum linken Panel.
		     */

		 
		    
		    
		    /*
		     * Um das Verhalten beim draufklicken des Buttons zu realisieren, 
		     * haben wir einen ClickHandler verwendet, durch den durch klicken mit der Mouse auf den Button
		     * die onClick()-Methode aufgerufen wird.
	         * Es wurde ein separates DIV-Element namens "details" in die zugeh������������rige HTML-Datei eingef������������gt.
	         * Bevor der neue Showcase dort eingef������������gt wird, werden zun������������chst alle bisherigen
	         * Elemente dieses DIV gel������������scht.         */

		
		
		
		    
		    
		    
		    /*
			 * Auch dem Report-Generator weisen wir dieses Bank-Objekt zu. Es wird
			 * dort f������������r die Darstellung der Adressdaten des Kreditinstituts
			 * ben������������tigt.
			 
			ReportGeneratorAsync reportGenerator = ClientsideSettings
					.getReportGenerator();
			reportGenerator.setBank(bank, new SetBankCallback());
			*/
		    

	
	
	
	/**
	 * This is the entry point method.
	 
	public void onModuleLoad() {
		final Button sendButton = new Button("Send");
		final TextBox nameField = new TextBox();
		nameField.setText("GWT User");
		final Label errorLabel = new Label();

		// We can add style names to widgets
		sendButton.addStyleName("sendButton");

		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		RootPanel.get("nameFieldContainer").add(nameField);
		RootPanel.get("sendButtonContainer").add(sendButton);
		RootPanel.get("errorLabelContainer").add(errorLabel);

		// Focus the cursor on the name field when the app loads
		nameField.setFocus(true);
		nameField.selectAll();

		// Create the popup dialog box
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText("Remote Procedure Call");
		dialogBox.setAnimationEnabled(true);
		final Button closeButton = new Button("Close");
		// We can set the id of a widget by accessing its Element
		closeButton.getElement().setId("closeButton");
		final Label textToServerLabel = new Label();
		final HTML serverResponseLabel = new HTML();
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(new HTML("<b>Sending name to the server:</b>"));
		dialogVPanel.add(textToServerLabel);
		dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
		dialogVPanel.add(serverResponseLabel);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(closeButton);
		dialogBox.setWidget(dialogVPanel);

		// Add a handler to close the DialogBox
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				sendButton.setEnabled(true);
				sendButton.setFocus(true);
			}
		});

		// Create a handler for the sendButton and nameField
		class MyHandler implements ClickHandler, KeyUpHandler {
			/**
			 * Fired when the user clicks on the sendButton.
			 
			public void onClick(ClickEvent event) {
				sendNameToServer();
			}

			/**
			 * Fired when the user types in the nameField.
			 
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					sendNameToServer();
				}
			}

			/**
			 * Send the name from the nameField to the server and wait for a response.
			 
			private void sendNameToServer() {
				// First, we validate the input.
				errorLabel.setText("");
				String textToServer = nameField.getText();
				if (!FieldVerifier.isValidName(textToServer)) {
					errorLabel.setText("Please enter at least four characters");
					return;
				}

				// Then, we send the input to the server.
				sendButton.setEnabled(false);
				textToServerLabel.setText(textToServer);
				serverResponseLabel.setText("");
				greetingService.greetServer(textToServer,
						new AsyncCallback<String>() {
							public void onFailure(Throwable caught) {
								// Show the RPC error message to the user
								dialogBox
										.setText("Remote Procedure Call - Failure");
								serverResponseLabel
										.addStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML(SERVER_ERROR);
								dialogBox.center();
								closeButton.setFocus(true);
							}

							public void onSuccess(String result) {
								dialogBox.setText("Remote Procedure Call");
								serverResponseLabel
										.removeStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML(result);
								dialogBox.center();
								closeButton.setFocus(true);
							}
						});
			}
		}

		// Add a handler to send the name to the server
		MyHandler handler = new MyHandler();
		sendButton.addClickHandler(handler);
		nameField.addKeyUpHandler(handler);
		
	}*/


