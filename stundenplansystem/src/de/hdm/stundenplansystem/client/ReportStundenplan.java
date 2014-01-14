package de.hdm.stundenplansystem.client;

import com.google.gwt.user.client.ui.FlexTable;

import de.hdm.stundenplansystem.client.Content;

public class ReportStundenplan extends Content {
	/**
	 * Aufbau der Seite, um den Raum anzuzeigen, zu lÃ¶schen und zu bearbeiten
	 */
	
	//final Label flexTable = new Label();
	//private VerticalPanel detailsPanel = new VerticalPanel();
	
	//final TextBox nachnameTextBox = new TextBox();
	//final TextBox vornameTextBox = new TextBox();
	final FlexTable tabelleSp = new FlexTable();
	
	//final CreateStundenplan createSp = new CreateStundenplan();
	//final ChangeStundenplan changeSp = new ChangeStundenplan();
	//final DeleteStundenplan deleteSp = new DeleteStundenplan();
	
	// final VerwaltungsklasseAsync verwaltungsSvc = GWT.create(Verwaltungsklasse.class);
	 
	
	//final Label valueLabel = new Label();
	


	
	public void onLoad() {
		
		showWidget();
	
		
	//int row = tabelleDozent.getRowCount();
		
		
		tabelleSp.setText(0, 0, "Bezeichnung");
		tabelleSp.setCellPadding(10);
		tabelleSp.setText(0, 1, "KapazitÃ¤t");
	//	tabelleSp.setText(0, 3, "Funktionen");
	//	tabelleSp.setWidget(1, 4, deleteSpButton);
	//	tabelleSp.setWidget(1, 5, changeSpButton);
		

		
		/**
		 * createSpButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
			this.add(createSp);
			}
		});
		
		changeSpButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				this.add(changeSp);
			}
		});*/
		
	}
		
	public void showWidget() {
		
		this.add(tabelleSp);
		//this.add(changeSp);
		//this.add(createSp);
		//this.add(deleteSp);
	}
	
	
	/**public Stundenplan updateFlexTable (Stundenplan result) {
		for (int i = 0; i < getAllStundenplan.size(); i++) { //getAllDozent wird noch als Methode oder Klasse benÃ¶tigt
			tabelleSp.addItem(getAllStundenplan.get(i).getVorname());
			
		}
	}
*/

}



