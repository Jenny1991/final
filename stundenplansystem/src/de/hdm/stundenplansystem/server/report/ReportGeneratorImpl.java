package de.hdm.stundenplansystem.server.report;

import de.hdm.stundenplansystem.shared.bo.*;
import de.hdm.stundenplansystem.shared.report.*;
import de.hdm.stundenplansystem.shared.*;
import de.hdm.stundenplansystem.server.*;
import de.hdm.stundenplansystem.server.db.StundenplaneintragMapper;

import java.util.Date;
import java.util.Vector;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
/**
 * @author hofmann & thies & holz
 */
@SuppressWarnings("serial")
public class ReportGeneratorImpl extends RemoteServiceServlet
    implements ReportGenerator {

  /**
   * Ein ReportGenerator benötigt Zugriff auf die BankAdministration, da diese die
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
     * Ein ReportGeneratorImpl-Objekt instantiiert f������r seinen Eigenbedarf eine
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
   * diese Tätigkeiten redundant auszuführen hätte. Stattdessen rufen die
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
   * @param c das Kundenobjekt bzgl. dessen der Report erstellt werden soll.
   * @return der fertige Report
   */
  public StundenplanDozentReport createStundenplanDozentReport(Dozent d) 
		  throws IllegalArgumentException {

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

    // Hinzufügen der Kopfzeile
    result.addRow(headline);
    
    Row accountRow;
    
    for(int i = 0; i < 30; i++){
    	
    	Stundenplaneintrag aktuell = this.verwaltung.getStundenplaneintragByDozentAndZeitslot(d.getId(), i);
    	
    	if(aktuell != null){
    		accountRow.addColumn(new Column(verwaltung.getZeitslotById(aktuell.getZeitslotId()).toString())+ "/n"+
    		verwaltung.getLehrveranstaltungById(aktuell.getLehrveranstaltungId()).toString()+ "/n"+ 
    		verwaltung.getRaumById(aktuell.getRaumId()).toString());
    	}
    	
    	Row accountRow = new Row();
    }
    
    
    /*
     * Nun werden sämtliche Stundenplaneintraege des Dozenten ausgelesen und in die Tabelle eingetragen.
     */
    
 /**
  *    Vector<Stundenplaneintrag> vecMo = new Vector<Stundenplaneintrag> ();
  *
    Vector<Stundenplaneintrag> vecDi = new Vector<Stundenplaneintrag> ();
    Vector<Stundenplaneintrag> vecMi = new Vector<Stundenplaneintrag> ();
    Vector<Stundenplaneintrag> vecDo = new Vector<Stundenplaneintrag> ();
    Vector<Stundenplaneintrag> vecFr = new Vector<Stundenplaneintrag> ();
    Vector<Stundenplaneintrag> vecDef = new Vector<Stundenplaneintrag> ();
    
    for (Stundenplaneintrag a : stundenplaneintraege) {
    	
    	switch (a.getZeitslot().getWochentag()) {
        case "Montag" : vecMo.add(a);
                 break;
        case "Dienstag": vecDi.add(a) ;
                 break;
        case "Mittwoch": vecMi.add(a) ;
                 break;
        case "Donnerstag": vecDo.add(a) ;
                 break;
        case "Freitag": vecFr.add(a) ;
                 break;
        default: vecDef.add(a) ;
                 break;
    }
}
    	
    	for ( int i = 0; i<6; i++){
    		Row accountRow = new Row();
    	
    		if (vecMo.elementAt(i) != null){
    			accountRow.addColumn(new Column(vecMo.elementAt(i).toString()));}
    		else {
    			accountRow.addColumn(new Column("----"));
    		}
    		
    		if (vecDi.elementAt(i) != null){
    			accountRow.addColumn(new Column(vecMo.elementAt(i).toString()));}
    		else {
    			accountRow.addColumn(new Column("----"));
    		}
    		
    		if (vecMi.elementAt(i) != null){
    			accountRow.addColumn(new Column(vecMo.elementAt(i).toString()));}
    		else {
    			accountRow.addColumn(new Column("----"));
    		}
    		
    		if (vecDo.elementAt(i) != null){
    			accountRow.addColumn(new Column(vecMo.elementAt(i).toString()));}
    		else {
    			accountRow.addColumn(new Column("----"));
    		}
    		
    		if (vecFr.elementAt(i) != null){
    			accountRow.addColumn(new Column(vecMo.elementAt(i).toString()));}
    		else {
    			accountRow.addColumn(new Column("----"));
    		}
    		
    		// und schließlich die Zeile dem Report hinzufügen.
            result.addRow(accountRow);
    		
    		}
    		*/
    /*
     * Zum Schluss müssen wir noch den fertigen Report zurückgeben.
     */
    return result;
  }

@Override
public void setRaum(Raum r) throws IllegalArgumentException {
	// TODO Auto-generated method stub
	
}

@Override
public void setSemesterverband(Semesterverband sv)
		throws IllegalArgumentException {
	// TODO Auto-generated method stub
	
}

@Override
public RaumbelegungsReport createRaumbelungsReport(Raum r)
		throws IllegalArgumentException {
	// TODO Auto-generated method stub
	return null;
}

@Override
public StundenplanSemesterverbandReport createStundenplanSemesterverbandReport(
		Semesterverband sv) throws IllegalArgumentException {
	// TODO Auto-generated method stub
	return null;
}

}
