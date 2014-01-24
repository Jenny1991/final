package de.hdm.stundenplansystem.server.report;

import de.hdm.stundenplansystem.shared.bo.*;
import de.hdm.stundenplansystem.shared.report.*;
import de.hdm.stundenplansystem.shared.*;
import de.hdm.stundenplansystem.server.*;


import java.util.Date;


import com.google.gwt.user.server.rpc.RemoteServiceServlet;
/**
 * @author l.hofmann & thies & holz
 */
@SuppressWarnings("serial")
public class ReportGeneratorImpl extends RemoteServiceServlet
    implements ReportGenerator {

  /**
   * Ein ReportGenerator benötigt Zugriff auf die Verwaltungsklasse, da diese die
   * essentiellen Methoden für die Koexistenz von Datenobjekten (vgl.
   * bo-Package) bietet.
   */
  private Verwaltungsklasse verwaltung = null;

  /**
   * <p>
   * Ein <code>RemoteServiceServlet</code> wird unter GWT mittels
   * <code>GWT.create(Klassenname.class)</code> Client-seitig erzeugt. Hierzu
   * ist ein solcher No-Argument-Konstruktor anzulegen. Ein Aufruf eines anderen
   * Konstruktors ist durch die Client-seitige Instantiierung durch
   * <code>GWT.create(Klassenname.class)</code> nach derzeitigem Stand nicht
   * möglich.
   * </p>
   * <p>
   * Es bietet sich also an, eine separate Instanzenmethode zu erstellen, die
   * Client-seitig direkt nach <code>GWT.create(Klassenname.class)</code>
   * aufgerufen wird, um eine Initialisierung der Instanz vorzunehmen.
   * </p>
   */
  public ReportGeneratorImpl() throws IllegalArgumentException {
  }

  /**
   * Initialsierungsmethode. Siehe dazu Anmerkungen zum No-Argument-Konstruktor.
   * 
   * @see #ReportGeneratorImpl()
   */
  public void init() throws IllegalArgumentException {
    /*
     * Ein ReportGeneratorImpl-Objekt instantiiert für seinen Eigenbedarf eine
     * VerwaltungklasseImpl-Instanz.
     */
    VerwaltungsklasseImpl a = new VerwaltungsklasseImpl();
    a.init();
    this.verwaltung = a;
  }

  /**
   * Auslesen der zugehörigen Verwaltungsklasse (interner Gebrauch).
   * 
   * @return das Verwaltungsklassenobjekt
   */
  protected Verwaltungsklasse getVerwaltungsklasse() {
    return this.verwaltung;
  }

  /**
   * Setzen des zugehörigen Dozenten-Objekts.
   */
  public void setDozent(Dozent d) {
    this.verwaltung.setDozent(d);
  }

  /**
   * Hinzufügen des Report-Impressums. Diese Methode ist aus den
   * <code>create...</code>-Methoden ausgegliedert, da jede dieser Methoden
   * diese Tatigkeiten redundant auszuführen hatte. Stattdessen rufen die
   * <code>create...</code>-Methoden diese Methode auf.
   * 
   * @param r der um das Impressum zu erweiternde Report.
   */
  protected void addImprint(Report r) {
    /*
     * Das Imressum soll mehrzeilig sein.
     */
    CompositeParagraph imprint = new CompositeParagraph();

    imprint.addSubParagraph(new SimpleParagraph("Hochschule der Medien"));
    imprint.addSubParagraph(new SimpleParagraph("Stuttgart"));

    // Das eigentliche Hinzufügen des Impressums zum Report.
    r.setImprint(imprint);

  }

  /**
   * Erstellen von <code>StundenplanDozentReport</code>-Objekten.
   * 
   * @param d das Dozentenobjekt bzgl. dessen der Report erstellt werden soll.
   * @return der fertige Report
   */
  public StundenplanDozentReport createStundenplanDozentReport(int dozentId) 
		  throws IllegalArgumentException {
	  
	  Dozent d = verwaltung.getDozentById(dozentId);

    if (this.getVerwaltungsklasse() == null)
      return null;

    /*
     * Zunächst legen wir uns einen leeren Report an.
     */
    StundenplanDozentReport result = new StundenplanDozentReport();

    // Jeder Report hat einen Titel (Bezeichnung / Überschrift).
    result.setTitle("Stundenplan des Dozenten");

    // Imressum hinzufügen
    this.addImprint(result);

    /*
     * Datum der Erstellung hinzufügen. new Date() erzeugt autom. einen
     * "Timestamp" des Zeitpunkts der Instantiierung des Date-Objekts.
     */
    result.setCreated(new Date());

    /*
     * Ab hier erfolgt die Zusammenstellung der Kopfdaten (die Dinge, die oben
     * auf dem Report stehen) des Reports. Die Kopfdaten sind mehrzeilig, daher
     * die Verwendung von CompositeParagraph.
     */
    CompositeParagraph header = new CompositeParagraph();

    // Name und Vorname des Dozenten aufnehmen
    header.addSubParagraph(new SimpleParagraph(d.getVorname() + ", "
        + d.getNachname()));

    // Hinzufügen der zusammengestellten Kopfdaten zu dem Report
    result.setHeaderData(header);

    /*
     * Ab hier erfolgt ein zeilenweises Hinzufügen von Stundenplaneintrag-Informationen.
     */
    
    /*
     * Zunächst legen wir eine Kopfzeile für die Stundenplaneintrag-Tabelle an.
     */
    Row headline = new Row();

    /*
     * Erzeugen einer StundenplanTabelle mit 6 Spalten für jeden Wochentag.
     */
    headline.addColumn(new Column("Montag"));
    headline.addColumn(new Column("Dienstag"));
    headline.addColumn(new Column("Mittwoch"));
    headline.addColumn(new Column("Donnerstag"));
    headline.addColumn(new Column("Freitag"));
    headline.addColumn(new Column("Samstag"));

    // Hinzufügen der Kopfzeile
    result.addRow(headline);
    
    Row accountRow = new Row();
    
    /*
     * Nun werden sämtliche Stundenplaneintraege des Dozenten ausgelesen und in die Tabelle eingetragen.
     */ 
    for(int i = 1; i < 37; i++){
    	
    	Stundenplaneintrag aktuell = this.verwaltung.getStundenplaneintragByDozentAndZeitslot(d.getId(), i);
    	
    	if(aktuell != null){
    		accountRow.addColumn(new Column(verwaltung.getZeitslotById(aktuell.getZeitslotId()).toString()+ "/n"+
    		verwaltung.getLehrveranstaltungById(aktuell.getLehrveranstaltungId()).toString()+ "/n"+ 
    		verwaltung.getRaumById(aktuell.getRaumId()).toString()));
    	} else {
			accountRow.addColumn(new Column("----"));
		}
    	
    	if (i == 6 | i == 12 | i == 18 | i == 24 | i == 30)
    		result.addRow(accountRow);
    		accountRow = new Row();
    }
    /*
     * Zum Schluss müssen wir noch den fertigen Report zurückgeben.
     */
    return result;
  }

public void setRaum(Raum r) throws IllegalArgumentException {
	this.verwaltung.setRaum(r);
}

/**
 * Erstellen von <code>RaumbelegungsReport</code>-Objekten.
 * 
 * @param r das Raumobjekt bzgl. dessen der Report erstellt werden soll.
 * @return der fertige Report
 */
public RaumbelegungsReport createRaumbelungsReport(int raumId) 
		throws IllegalArgumentException {

	Raum r = verwaltung.getRaumById(raumId);


  if (this.getVerwaltungsklasse() == null)
    return null;

  /*
   * Zunächst legen wir uns einen leeren Report an.
   */
  RaumbelegungsReport result = new RaumbelegungsReport();

  // Jeder Report hat einen Titel (Bezeichnung / Überschrift).
  result.setTitle("Raumbelegungsplan");

  // Imressum hinzufügen
  this.addImprint(result);

  /*
   * Datum der Erstellung hinzufügen. new Date() erzeugt autom. einen
   * "Timestamp" des Zeitpunkts der Instantiierung des Date-Objekts.
   */
  result.setCreated(new Date());

  /*
   * Ab hier erfolgt die Zusammenstellung der Kopfdaten (die Dinge, die oben
   * auf dem Report stehen) des Reports. Die Kopfdaten sind mehrzeilig, daher
   * die Verwendung von CompositeParagraph.
   */
  CompositeParagraph header = new CompositeParagraph();

  // Bezeichnung und Kapazität des Raumes aufnehmen
  header.addSubParagraph(new SimpleParagraph(r.getBezeichnung() + ", Kapatitüt: "
      + r.getKapazitaet()));

  // Hinzufügen der zusammengestellten Kopfdaten zu dem Report
  result.setHeaderData(header);

  /*
   * Ab hier erfolgt ein zeilenweises Hinzufügen von Stundenplaneintrag-Informationen.
   */
  
  /*
   * Zunächst legen wir eine Kopfzeile für die Stundenplaneintrag-Tabelle an.
   */
  Row headline = new Row();

  /*
   * Erzeugen einer StundenplanTabelle mit 6 Spalten für jeden Wochentag.
   */
  headline.addColumn(new Column("Montag"));
  headline.addColumn(new Column("Dienstag"));
  headline.addColumn(new Column("Mittwoch"));
  headline.addColumn(new Column("Donnerstag"));
  headline.addColumn(new Column("Freitag"));
  headline.addColumn(new Column("Samstag"));

  // Hinzufügen der Kopfzeile
  result.addRow(headline);
  
  Row accountRow = new Row();
  
  /*
   * Nun werden sämtliche Stundenplaneintraege des Raumes ausgelesen und in die Tabelle eingetragen.
   */ 
  for(int i = 1; i < 37; i++){
  	
  	Stundenplaneintrag aktuell = this.verwaltung.getStundenplaneintragByRaumAndZeitslot(r.getId(), i);
  	
  	if(aktuell != null){
  		accountRow.addColumn(new Column(verwaltung.getZeitslotById(aktuell.getZeitslotId()).toString()+ "/n"+
  		verwaltung.getLehrveranstaltungById(aktuell.getLehrveranstaltungId()).toString()+ "/n"+ 
  		verwaltung.getRaumById(aktuell.getRaumId()).toString()));
  	} else {
			accountRow.addColumn(new Column("----"));
		}
  	
  	if (i == 6 | i == 12 | i == 18 | i == 24 | i == 30)
  		result.addRow(accountRow);
  		accountRow = new Row();
  }
  /*
   * Zum Schluss müssen wir noch den fertigen Report zurückgeben.
   */
  return result;
}



public void setSemesterverband(int sv)
		throws IllegalArgumentException {
	this.setSemesterverband(sv);
}

/**
 * Erstellen von <code>StundenplanSemesterverbandReport</code>-Objekten.
 * 
 * @param sv das Semesterverbandobjekt bzgl. dessen der Report erstellt werden soll.
 * @return der fertige Report
 */

public StundenplanSemesterverbandReport createStundenplanSemesterverbandReport(
	int semesterverbandId, int stundenplanId) throws IllegalArgumentException {

		Semesterverband sv = verwaltung.getSemesterverbandById(semesterverbandId);
		Stundenplan sp = verwaltung.getStundenplanById(stundenplanId);
		
	  if (this.getVerwaltungsklasse() == null)
	    return null;

	  /*
	   * Zunächst legen wir uns einen leeren Report an.
	   */
	  StundenplanSemesterverbandReport result = new StundenplanSemesterverbandReport();

	  // Jeder Report hat einen Titel (Bezeichnung / Überschrift).
	  result.setTitle("Stundenplan des Semesterverbandes");

	  // Imressum hinzufügen
	  this.addImprint(result);

	  /*
	   * Datum der Erstellung hinzufügen. new Date() erzeugt autom. einen
	   * "Timestamp" des Zeitpunkts der Instantiierung des Date-Objekts.
	   */
	  result.setCreated(new Date());

	  /*
	   * Ab hier erfolgt die Zusammenstellung der Kopfdaten (die Dinge, die oben
	   * auf dem Report stehen) des Reports. Die Kopfdaten sind mehrzeilig, daher
	   * die Verwendung von CompositeParagraph.
	   */
	  CompositeParagraph header = new CompositeParagraph();

	  // Bezeichnung des Semesterverbandes aufnehmen
	  header.addSubParagraph(new SimpleParagraph(sv.getKuerzel() + ", "
	      + sv.getSemester()));

	  // Hinzufügen der zusammengestellten Kopfdaten zu dem Report
	  result.setHeaderData(header);

	  /*
	   * Ab hier erfolgt ein zeilenweises Hinzufügen von Stundenplaneintrag-Informationen.
	   */
	  
	  /*
	   * Zunächst legen wir eine Kopfzeile für die Stundenplaneintrag-Tabelle an.
	   */
	  Row headline = new Row();

	  /*
	   * Erzeugen einer StundenplanTabelle mit 6 Spalten für jeden Wochentag.
	   */
	  headline.addColumn(new Column("      "));
	  headline.addColumn(new Column("Montag"));
	  headline.addColumn(new Column("Dienstag"));
	  headline.addColumn(new Column("Mittwoch"));
	  headline.addColumn(new Column("Donnerstag"));
	  headline.addColumn(new Column("Freitag"));
	  headline.addColumn(new Column("Samstag"));

	  // Hinzufügen der Kopfzeile
	  result.addRow(headline);
	  
	  Row accountRow = new Row();

	  accountRow.addColumn(new Column(verwaltung.getZeitslotById(1).toString()));

	  /*
	  * Nun werden sämtliche Stundenplaneintraege des Semsterverbandes ausgelesen und in die Tabelle eingetragen.
	  */ 

	  for(int i = 1; i < 37; i++){

		  Stundenplaneintrag aktuell = this.verwaltung.getStundenplaneintragBySemesterverbandAndZeitslot(sv.getId(), i, sp.getId());

		  if(aktuell != null){
			  accountRow.addColumn(new Column(

					  verwaltung.getLehrveranstaltungById(aktuell.getLehrveranstaltungId()).toString()+ 
					  verwaltung.getRaumById(aktuell.getRaumId()).toString()));

		  } 
		  else {
			  		accountRow.addColumn(new Column("----"));
		  }
		  	
		  int hilfsZeitslotId = 1;

		  if (i == 6 | i == 12 | i == 18 | i == 24 | i == 30){
			  
			  result.addRow(accountRow);
			  accountRow = new Row();
			  accountRow.addColumn(new Column(verwaltung.getZeitslotById(hilfsZeitslotId).toString()));
			  hilfsZeitslotId = hilfsZeitslotId + 6;
			  }
		  }
	  /*
	   * Zum Schluss müssen wir noch den fertigen Report zurückgeben.
	   */  
	 return result;
	 }
}


