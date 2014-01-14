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
   *
  private String reportText = "";

  /**
   * ZurÃ¼cksetzen der Variable <code>reportText</code>.
   *
  public void resetReportText() {
    this.reportText = "";
  }

  /**
   * Header-Text produzieren.
   * 
   * @return Text
   *
  public String getHeader() {
    // Wir benÃ¶tigen fÃ¼r Demozwecke keinen Header.
    return "";
  }

  /**
   * Trailer-Text produzieren.
   * 
   * @return Text
   *
  public String getTrailer() {
    // Wir verwenden eine einfache Trennlinie, um das Report-Ende zu markieren.
    return "___________________________________________";
  }

  /**
   * Prozessieren des Ã¼bergebenen Reports und Ablage im Zielformat. Ein Auslesen
   * des Ergebnisses kann spÃ¤ter mittels <code>getReportText()</code> erfolgen.
   * 
   * @param r der zu prozessierende Report
   *
  public void process(AllAccountsOfCustomerReport r) {

    // ZunÃ¤chst lÃ¶schen wir das Ergebnis vorhergehender Prozessierungen.
    this.resetReportText();

    /*
     * In diesen Buffer schreiben wir wÃ¤hrend der Prozessierung sukzessive
     * unsere Ergebnisse.
     *
    StringBuffer result = new StringBuffer();

    /*
     * Nun werden Schritt fÃ¼r Schritt die einzelnen Bestandteile des Reports
     * ausgelesen und in Text-Form Ã¼bersetzt.
     *
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
     * reportText-Variable zugewiesen. Dadurch wird es mÃ¶glich, anschlieÃŸend das
     * Ergebnis mittels getReportText() auszulesen.
     *
    this.reportText = result.toString();
  

  /**
   * Prozessieren des Ã¼bergebenen Reports und Ablage im Zielformat. Ein Auslesen
   * des Ergebnisses kann spÃ¤ter mittels <code>getReportText()</code> erfolgen.
   * 
   * @param r der zu prozessierende Report
   *
  public void process(AllAccountsOfAllCustomersReport r) {

    // ZunÃ¤chst lÃ¶schen wir das Ergebnis vorhergehender Prozessierungen.
    this.resetReportText();

    /*
     * In diesen Buffer schreiben wir wÃ¤hrend der Prozessierung sukzessive
     * unsere Ergebnisse.
     *
    StringBuffer result = new StringBuffer();

    /*
     * Nun werden Schritt fÃ¼r Schritt die einzelnen Bestandteile des Reports
     * ausgelesen und in Text-Form Ã¼bersetzt.
     *
    result.append("*** " + r.getTitle() + " ***\n\n");

    if (r.getHeaderData() != null)
      result.append(r.getHeaderData() + "\n");

    result.append("Erstellt am: " + r.getCreated().toString() + "\n\n");

    /*
     * Da AllAccountsOfAllCustomersReport ein CompositeReport ist, enthÃ¤lt r
     * eine Menge von Teil-Reports des Typs AllAccountsOfCustomerReport. FÃ¼r
     * jeden dieser Teil-Reports rufen wir processAllAccountsOfCustomerReport
     * auf. Das Ergebnis des jew. Aufrufs fÃ¼gen wir dem Buffer hinzu.
     *
    for (int i = 0; i < r.getNumSubReports(); i++) {
      /*
       * AllAccountsOfCustomerReport wird als Typ der SubReports vorausgesetzt.
       * Sollte dies in einer erweiterten Form des Projekts nicht mehr gelten,
       * so mÃ¼sste hier eine detailliertere Implementierung erfolgen.
       *
      AllAccountsOfCustomerReport subReport = (AllAccountsOfCustomerReport) r
          .getSubReportAt(i);

      this.process(subReport);

      // Ein Form Feed wÃ¤re hier statt der 5 Leerzeilen auch denkbar...
      result.append(this.reportText + "\n\n\n\n\n");

      /*
       * Nach jeder Ãœbersetzung eines Teilreports und anschlieÃŸendem Auslesen
       * sollte die Ergebnisvariable zurÃ¼ckgesetzt werden.
       *
      this.resetReportText();
    }

    /*
     * Zum Schluss wird unser Arbeits-Buffer in einen String umgewandelt und der
     * reportText-Variable zugewiesen. Dadurch wird es mÃ¶glich, anschlieÃŸend das
     * Ergebnis mittels getReportText() auszulesen.
     *
    this.reportText = result.toString();
  }

  /**
   * Auslesen des Ergebnisses der zuletzt aufgerufenen Prozessierungsmethode.
   * 
   * @return ein String bestehend aus einfachem Text
   *
  public String getReportText() {
    return this.getHeader() + this.reportText + this.getTrailer();
  }
  */
}