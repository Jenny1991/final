package de.hdm.stundenplansystem.shared.report;

import java.util.Vector;

/**
 * Ein <code>ReportWriter</code>, der Reports mittels Plain Text formatiert. Das
 * im Zielformat vorliegende Ergebnis wird in der Variable
 * <code>reportText</code> abgelegt und kann nach Aufruf der entsprechenden
 * Prozessierungsmethode mit <code>getReportText()</code> ausgelesen werden.
 * 
 * @author Thies
 */
public class PlainTextReportWriter extends ReportWriter {

  /**
   * Diese Variable wird mit dem Ergebnis einer Umwandlung (vgl.
   * <code>process...</code>-Methoden) belegt. Format: Text
   */
  private String reportText = "";

  /**
   * Zur����cksetzen der Variable <code>reportText</code>.
   */
  public void resetReportText() {
    this.reportText = "";
  }

  /**
   * Header-Text produzieren.
   * 
   * @return Text
   */
  public String getHeader() {
    // Wir ben����tigen f����r Demozwecke keinen Header.
    return "";
  }

  /**
   * Trailer-Text produzieren.
   * 
   * @return Text
   */
  public String getTrailer() {
    // Wir verwenden eine einfache Trennlinie, um das Report-Ende zu markieren.
    return "___________________________________________";
  }

  /**
   * Prozessieren des ����bergebenen Reports und Ablage im Zielformat. Ein Auslesen
   * des Ergebnisses kann sp����ter mittels <code>getReportText()</code> erfolgen.
   * 
   * @param r der zu prozessierende Report
   */
  public void process(StundenplanDozentReport r) {

    // Zun����chst l����schen wir das Ergebnis vorhergehender Prozessierungen.
    this.resetReportText();

    /*
     * In diesen Buffer schreiben wir w����hrend der Prozessierung sukzessive
     * unsere Ergebnisse.
     */
    StringBuffer result = new StringBuffer();

    /*
     * Nun werden Schritt f����r Schritt die einzelnen Bestandteile des Reports
     * ausgelesen und in Text-Form ����bersetzt.
     */
    result.append("*** " + r.getTitle() + " ***\n\n");
    result.append(r.getHeaderData() + "\n");
    result.append("Erstellt am: " + r.getCreated().toString() + "\n\n");
    Vector<Row> rows = r.getRows();

    for (Row row : rows) {
      for (int k = 0; k < row.getNumColumns(); k++) {
        result.append(row.getColumnAt(k) + "\t ; \t");
      }

      result.append("\n");
    }

    result.append("\n");
    result.append(r.getImprint() + "\n");

    /*
     * Zum Schluss wird unser Arbeits-Buffer in einen String umgewandelt und der
     * reportText-Variable zugewiesen. Dadurch wird es m����glich, anschlie����end das
     * Ergebnis mittels getReportText() auszulesen.
     */
    this.reportText = result.toString();
  }
  
  /**
   * Das selbe nochmal wie oben, nur für StundenplanSemesterverbandReport
   */
  
  public void process(StundenplanSemesterverbandReport r) {

	    this.resetReportText();

	    StringBuffer result = new StringBuffer();

	    result.append("*** " + r.getTitle() + " ***\n\n");
	    result.append(r.getHeaderData() + "\n");
	    result.append("Erstellt am: " + r.getCreated().toString() + "\n\n");
	    Vector<Row> rows = r.getRows();

	    for (Row row : rows) {
	      for (int k = 0; k < row.getNumColumns(); k++) {
	        result.append(row.getColumnAt(k) + "\t ; \t");
	      }

	      result.append("\n");
	    }

	    result.append("\n");
	    result.append(r.getImprint() + "\n");

	    this.reportText = result.toString();
	  }
  
  /**
   * Das selbe nochmal wie oben, nur für RaumbelegungsReport
   */
  
  public void process(RaumbelegungsReport r) {

	    this.resetReportText();
	    
	    StringBuffer result = new StringBuffer();

	    result.append("*** " + r.getTitle() + " ***\n\n");
	    result.append(r.getHeaderData() + "\n");
	    result.append("Erstellt am: " + r.getCreated().toString() + "\n\n");
	    Vector<Row> rows = r.getRows();

	    for (Row row : rows) {
	      for (int k = 0; k < row.getNumColumns(); k++) {
	        result.append(row.getColumnAt(k) + "\t ; \t");
	      }

	      result.append("\n");
	    }

	    result.append("\n");
	    result.append(r.getImprint() + "\n");

	    this.reportText = result.toString();
	  }

  /**
   * Auslesen des Ergebnisses der zuletzt aufgerufenen Prozessierungsmethode.
   * 
   * @return ein String bestehend aus einfachem Text
   */
  public String getReportText() {
    return this.getHeader() + this.reportText + this.getTrailer();
  }
 
}