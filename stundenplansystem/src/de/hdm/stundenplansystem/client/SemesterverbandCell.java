package de.hdm.stundenplansystem.client;

import java.util.Vector;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.ibm.icu.util.BytesTrie.Result;

import de.hdm.stundenplansystem.shared.Verwaltungsklasse;
import de.hdm.stundenplansystem.shared.VerwaltungsklasseAsync;
import de.hdm.stundenplansystem.shared.bo.*;

/**
 * @author C. Rathke, V. Hofmann
 * Diese Klasse SemesterverbandCell definiert die in den Knoten dargestellte
 * Informationen.
 * Die Methode <code>render</code> übersetzt die Cell als HTML in einen
 * SafeHtmlBuilder, der das Erstellen von XSS (safe HTML) aus Textteilen
 * erleichtert.
 */
public class SemesterverbandCell extends
		AbstractCell<Semesterverband> {

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
