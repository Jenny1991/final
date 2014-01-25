package de.hdm.stundenplansystem.shared.report;

/**
 * <p>
 * Diese Klasse wird ben����tigt, um auf dem Client die ihm vom Server zur
 * Verf����gung gestellten <code>Report</code>-Objekte in ein menschenlesbares
 * Format zu ����berf����hren.
 * </p>
 * <p>
 * Das Zielformat kann prinzipiell beliebig sein. Methoden zum Auslesen der in
 * das Zielformat ����berf����hrten Information wird den Subklassen
 * ����berlassen. In dieser Klasse werden die Signaturen der Methoden
 * deklariert, die f����r die Prozessierung der Quellinformation zust����ndig
 * sind.
 * </p>
 * 
 * @author Thies
 */
public abstract class ReportWriter {

	/**
	 * Übersetzen eines <code>StundenplanDozentReport</code> in das Zielformat.
	 * 
	 * @param r
	 *            der zu übersetzende Report
	 */
	public abstract void process(StundenplanDozentReport r);

	/**
	 * Übersetzen eines <code>StundenplanSemesterverbandReport</code> in das
	 * Zielformat.
	 * 
	 * @param r
	 *            der zu übersetzende Report
	 */
	public abstract void process(StundenplanSemesterverbandReport r);

	/**
	 * Übersetzen eines <code>RaumbelegungsReport</code> in das Zielformat.
	 * 
	 * @param r
	 *            der zu übersetzende Report
	 */

	public abstract void process(RaumbelegungsReport r);
}