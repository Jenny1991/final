/**
 * 
 */
package de.hdm.stundenplansystem.client;

import com.google.gwt.cell.client.AbstractCell;

import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

import de.hdm.stundenplansystem.shared.bo.*;

/**
 * @author C. Rathke, V. Hofmann
 * Diese Klasse StringCell definiert die in den Knoten dargestellte
 * Informationen.
 * Die Methode <code>render</code> übersetzt die Cell als HTML in einen
 * SafeHtmlBuilder, der das Erstellen von XSS (safe HTML) aus Textteilen
 * erleichtert.
 */
public class StringCell extends AbstractCell<String> {
	@Override
	public void render(Context context, String value,
			SafeHtmlBuilder sb) {
		// Value can be null, so do a null check.
		if (value == null) {
			return;
		}

		sb.appendHtmlConstant("<div>");
		sb.appendEscaped(value);
		sb.appendHtmlConstant("</div>");
	}

}
