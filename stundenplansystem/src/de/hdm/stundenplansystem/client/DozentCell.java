/**
 * 
 */
package de.hdm.stundenplansystem.client;

import com.google.gwt.cell.client.AbstractCell;

import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

import de.hdm.stundenplansystem.shared.bo.*;

/**
 * @author C. Rathke, V. Hofmann
 *
 */
public class DozentCell extends AbstractCell<Dozent> {
	@Override
    public void render(Context context, Dozent value, SafeHtmlBuilder sb) {
      // Value can be null, so do a null check..
      if (value == null) {
        return;
      }

      sb.appendHtmlConstant("<div>");
      sb.appendEscaped(value.getVorname());
      sb.appendHtmlConstant(", ");
      sb.appendEscaped(value.getNachname());
      sb.appendHtmlConstant("</div>");
    }

}
