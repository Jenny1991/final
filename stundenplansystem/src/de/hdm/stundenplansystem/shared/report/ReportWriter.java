package de.hdm.stundenplansystem.shared.report;

/**
 * <p>
 * Diese Klasse wird benÃ¶tigt, um auf dem Client die ihm vom Server zur
 * VerfÃ¼gung gestellten <code>Report</code>-Objekte in ein menschenlesbares
 * Format zu Ã¼berfÃ¼hren.
 * </p>
 * <p>
 * Das Zielformat kann prinzipiell beliebig sein. Methoden zum Auslesen der in
 * das Zielformat Ã¼berfÃ¼hrten Information wird den Subklassen Ã¼berlassen. In
 * dieser Klasse werden die Signaturen der Methoden deklariert, die fÃ¼r die
 * Prozessierung der Quellinformation zustÃ¤ndig sind.
 * </p>
 * 
 * @author Thies
 */
public abstract class ReportWriter {

	
  /**
   * Ãœbersetzen eines <code>AllAccountsOfCustomerReport</code> in das
   * Zielformat.
   * 
   * @param r der zu Ã¼bersetzende Report
   *
  public abstract void process(AllAccountsOfCustomerReport r);

  /**
   * Ãœbersetzen eines <code>AllAccountsOfAllCustomersReport</code> in das
   * Zielformat.
   * 
   * @param r der zu Ã¼bersetzende Report
   *
  public abstract void process(AllAccountsOfAllCustomersReport r);
*/
}