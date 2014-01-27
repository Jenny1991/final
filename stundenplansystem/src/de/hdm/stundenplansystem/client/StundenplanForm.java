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
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import de.hdm.stundenplansystem.shared.*;
import de.hdm.stundenplansystem.shared.bo.Semesterverband;
import de.hdm.stundenplansystem.shared.bo.Studiengang;
import de.hdm.stundenplansystem.shared.bo.Stundenplan;
import de.hdm.stundenplansystem.client.NavTreeViewModel;

/**
 * Formular, in der ein bereits bestehender Stundenplan angezeigt, 
 * gelöscht und bearbeitet wird.
 * Diese Klasse erbt von der Klasse Content und lässt sich somit 
 * unter GWT entsprechend anordnen.
 * 
 * @see CreateDozent
 * @author Thies, Espich
 * @version 1.0
 */
public class StundenplanForm extends Content {

	/**
	 * Jede Klasse enthält eine Überschrift, die definiert, was der User machen
	 * kann.
	 */
	private final HTML ueberschrift = new HTML(
			"<h2>Übersicht der Stundenpläne pro Studienhalbjahre<h2>");

	/** 
	 * Hier werden die GWT Widgets instantiiert
	 */
	final TextBox tbhalbjahr = new TextBox();
	final ListBox libsemverband = new ListBox();
	final ListBox libstudiengang = new ListBox();
	final Button loeschen = new Button("Studienhalbjahr löschen");
	final Button speichern = new Button("Änderungen speichern");
	
	/**
	 * Hier wird ein Remote Service Proxy erstellt, welches uns erlaubt, 
	 * mit dem serverseitigen Verwaltungsservice zu kommunizieren.
	 */
	final VerwaltungsklasseAsync verwaltungsSvc = GWT
			.create(Verwaltungsklasse.class);

	/**
	 * Hier wird ein Vector des Objektes Studiengang und 
	 * ein Vector des Objetkes Semesterverband festgelegt
	 */
	Vector<Semesterverband> svContainer = null;
	Vector<Studiengang> sgContainer = null;

//	Integer id;
	Stundenplan shownSp = null;
	NavTreeViewModel tvm = null;

	  /**
	   * Jedes Formular wird durch einen Konstruktor dargestellt. 
	   * In diesem wird eine Instanz des GWT Widgets Grid erzeugt, dass den Aufbau
	   * des Formulars darstellt.
	   * Durch die Methode <code>add()</code> werden die Widgets dem Panel hinzugefügt.
	   * Durch die Methode <code>setWidget()</code> werden die Widgets 
	   * in den Zeilen und Spalten der Grid hinzugefügt.
	   */
	public StundenplanForm() {
		Grid stGrid = new Grid(5, 2);
		this.add(ueberschrift);
		this.add(stGrid);

		Label lbstudiengang = new Label("Studiengang:");
		stGrid.setWidget(0, 0, lbstudiengang);
		stGrid.setWidget(0, 1, libstudiengang);

		Label lbsemverband = new Label("Semesterverband:");
		stGrid.setWidget(1, 0, lbsemverband);
		stGrid.setWidget(1, 1, libsemverband);

		Label lbhalbjahr = new Label("Studienhalbjahr:");
		stGrid.setWidget(2, 0, lbhalbjahr);
		stGrid.setWidget(2, 1, tbhalbjahr);

		Label lbfunktionen = new Label("Funktionen:");
		stGrid.setWidget(3, 0, lbfunktionen);
		stGrid.setWidget(3, 1, speichern);

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
				changeSelectedHj();
			}
		});
		stGrid.setWidget(4, 1, loeschen);
		
		/**
		 * Beim Betätigen des Lösch-Buttons wird die Methode <code>addClickHandler()</code> 
		 * aufgerufen. Dabei wird ein Interface {@link ClickHandler} erzeugt, 
		 * das durch eine anonyme Klasse implementiert und durch new instantiiert wird. 
		 * Dieses Interface verlangt genau eine Methode <code>onClick()</code>, die 
		 * ein Objekt vom Typ ClickEvent {@link ClickEvent} erzeugt.
		 * 
		 * @param event wird abhängig vom Eventtyp {@link ClickEvent} definiert
		 * 
		 * Anschließend wird festgelegt, was passiert wenn der Lösch-Button gedrückt wurde.
		 */
		loeschen.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				deleteSelectedHj();
			}
		});
		setTvm(tvm);
	}

	public void onLoad() {
//		verwaltungsSvc.getStundenplanById(shownSp.getId(),
//				new AsyncCallback<Stundenplan>() {
//					@Override
//					public void onFailure(Throwable caught) {
//					}
//
//					@Override
//					public void onSuccess(Stundenplan sp) {
//						if (sp != null) {
//							setSelected(sp);
//						}
//					};
//				});

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
		libstudiengang.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				getSemverband();
			}
		});

	}

	public void changeSelectedHj() {

		boolean allFilled = true;

		if (tbhalbjahr.getValue().isEmpty()) {
			allFilled = false;
			Window.alert("Bitte füllen Sie alle Felder aus.");
		}

		if (allFilled == true) {
			shownSp.setStudienhalbjahr(tbhalbjahr.getText().trim());
			
			/**
			 * Immer abfragen, ob der Wert der ListBox ungleich 0 ist, 
			 * da bei keiner Änderung der ListBox dieser nicht gespeichert wird. 
			 */			
			if (libsemverband.getSelectedIndex() != 0)
			shownSp.setSemesterverbandId(svContainer.elementAt(
					libsemverband.getSelectedIndex() - 1).getId());

			verwaltungsSvc.changeStundenplan(shownSp,
					new AsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert(caught.getMessage());
						}

						@Override
						public void onSuccess(Void result) {
							tvm.updateStudienhalbjahr(shownSp);
							Window.alert("Erfolgreich gespeichert.");

						}
					});
		}
	}

	public void deleteSelectedHj() {
		
		verwaltungsSvc.deleteStundenplan(shownSp,
				new AsyncCallback<Void>() {
			
					public void onFailure(Throwable caught) {
						Window.alert("Der Stundenplan konnte nicht gelöscht werden.");
					}

					public void onSuccess(Void result) {
						tvm.deleteStudienhalbjahr(shownSp);
						Window.alert("Erfolgreich gelöscht.");
					}
				});
		this.clearFields();
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
	 * Bevor wir das Objekt bearbeiten fragen wir, 
	 * ob wir einen brauchbaren Wert zurückerhalten haben, 
	 * bevor wir diesen benutzen.
	 * Anschließend definierten wir, was als nächstes zu tun ist.
	 * 
	 * @param sp der Stundenplan der bearbeitet werden soll
	 */
	public void setSelected(Stundenplan sp) {
		if (sp != null) {
			shownSp = sp;
			setFields();
		} else {
			clearFields();
		}
	}

	/**
	 * Ab hier befüllen wir die Widgets mit den Daten des gewählten Stundenplans
	 * Zunächst wird die List Box des Semesterverbands befüllt.
	 * Anschließend holen wir uns den Studiengang der zu diesem Semesterverband 
	 * gehört. Danach werden beide List Boxen wieder mit allen Elementen des Semesterverbandes
	 * sowie des Studiengangs befüllt
	 */
	public void setFields() {
		this.clearFields();
		tbhalbjahr.setText(shownSp.getStudienhalbjahr());
		verwaltungsSvc.getSemesterverbandById(
				shownSp.getSemesterverbandId(),
				new AsyncCallback<Semesterverband>() {
					@Override
					public void onFailure(Throwable caught) {
						caught.getMessage();
					}

					@Override
					public void onSuccess(Semesterverband result) {
						libsemverband.addItem(result.getJahrgang()
								+ ", Semester: "
								+ String.valueOf(result.getSemester()));
						getNextListSg();
					}
				});
	}

	public void getNextListSg() {
		verwaltungsSvc.getStudiengangBySemesterverbandId(
				shownSp.getSemesterverbandId(),
				new AsyncCallback<Studiengang>() {
					@Override
					public void onFailure(Throwable caught) {
						caught.getMessage();
					}

					@Override
					public void onSuccess(Studiengang result) {
						libstudiengang.addItem(result
								.getBezeichnung());
						getStudiengaenge();
					}
				});
	}

	public void getStudiengaenge() {
		verwaltungsSvc
				.getAllStudiengaenge(new AsyncCallback<Vector<Studiengang>>() {
					@Override
					public void onFailure(Throwable caught) {
						caught.getMessage();
					}

					@Override
					public void onSuccess(
							Vector<Studiengang> studiengang) {
						sgContainer = studiengang;
						for (Studiengang sg : studiengang) {
							libstudiengang.addItem(
									sg.getBezeichnung(),
									String.valueOf(sg.getId()));
						}
						getSemverband();
					}
				});
	}

	public void getSemverband() {
		verwaltungsSvc
				.getAllSemesterverbaende(new AsyncCallback<Vector<Semesterverband>>() {
					public void onFailure(Throwable caught) {
						caught.getMessage();
					}

					public void onSuccess(
							Vector<Semesterverband> semesterverband) {
						svContainer = semesterverband;
						for (Semesterverband sv : semesterverband) {
							libsemverband.addItem(sv.getJahrgang()
									+ ", Semester: "
									+ String.valueOf(sv.getSemester()));
						}
					}
				});
	}
	
	/**
	 * Hier löschen wir den Inhalt der Widgets
	 */
	public void clearFields() {
		libstudiengang.clear();
		libsemverband.clear();
		tbhalbjahr.setText("");
	}
}
