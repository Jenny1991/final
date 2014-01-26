package de.hdm.stundenplansystem.client;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import de.hdm.stundenplansystem.shared.bo.*;

/**
 * Diese Klasse SemesterverbandCell definiert die in den Knoten dargestellte
 * Informationen.
 * 
 * @author C. Rathke, V. Hofmann
 * @version 1.0
 */
public class SemesterverbandCell extends
		AbstractCell<Semesterverband> {
	/**
	 * Die Methode <code>render</code> übersetzt die Cell als HTML in einen
	 * SafeHtmlBuilder, der das Erstellen von XSS (safe HTML) aus Textteilen
	 * erleichtert.
	 */
	public void render(Context context, final Semesterverband value,
			final SafeHtmlBuilder sb) {
		// Value can be null, so do a null check..
		if (value == null) {
			return;
		}

		sb.appendHtmlConstant("<div>");
		sb.appendEscapedLines(value.getKuerzel());
		sb.appendHtmlConstant(", Semester: ");
		sb.append(value.getSemester());
		sb.appendHtmlConstant("</div>");

	}
}
