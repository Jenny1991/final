package de.hdm.stundenplansystem.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Button;

import de.hdm.stundenplansystem.shared.*;
import de.hdm.stundenplansystem.shared.bo.Raum;
import de.hdm.stundenplansystem.client.NavTreeViewModel;

/**
 * Hier wird ein neuer Raum angelegt.
 * 
 * @author Thies, Espich
 * 
 */

public class CreateRaum extends Content {

	/**
	 * Jede Klasse enthält eine Überschrift, die definiert, was der User
	 * machen kann.
	 */
	private final HTML ueberschrift = new HTML(
			"<h2>Neuen Raum anlegen<h2>");

	/**
	 * Unter der ï¿½berschrift tragt der User die Daten des neuen Raums ein.
	 */
	final Label lbbezeichnung = new Label("Bezeichnung:");
	final Label lbkapazitaet = new Label("Kapazität des Raums:");
	final TextBox tbbezeichnung = new TextBox();
	final TextBox tbkapazitaet = new TextBox();
	final Button speichern = new Button("Eingaben speichern");

	final VerwaltungsklasseAsync verwaltungsSvc = GWT
			.create(Verwaltungsklasse.class);
	NavTreeViewModel tvm = null;

	/**
	 * Anordnen der Buttons und Labels auf den Panels
	 */
	public void onLoad() {

		this.add(ueberschrift);

		this.add(lbbezeichnung);
		this.add(tbbezeichnung);
		this.add(lbkapazitaet);
		this.add(tbkapazitaet);
		this.add(speichern);

		setTvm(tvm);

		speichern.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				boolean allFilled = true;

				if (tbbezeichnung.getValue().isEmpty()
						|| tbkapazitaet.getValue().isEmpty()) {
					allFilled = false;
					Window.alert("Bitte füllen Sie alle Felder aus.");
				}

				if (allFilled == true) {
					final String bezeichnung = tbbezeichnung
							.getValue().trim();
					final int kapazitaet = Integer
							.valueOf(tbkapazitaet.getValue());

					verwaltungsSvc.createRaum(bezeichnung,
							kapazitaet, new AsyncCallback<Raum>() {

								@Override
								public void onFailure(Throwable caught) {
									Window.alert(caught.getMessage());
								}

								@Override
								public void onSuccess(Raum result) {
									tbbezeichnung.setText("");
									tbkapazitaet.setText("");
									Window.alert("Erfolgreich gespeichert.");
									tvm.addRaum(result);
								}
							});
				}
			}
		});
	}

	public void setTvm(NavTreeViewModel tvm) {
		this.tvm = tvm;
	}

}
