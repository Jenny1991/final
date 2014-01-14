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
public class RaumCell extends AbstractCell<Raum> {
	@Override
    public void render(Context context, Raum value, SafeHtmlBuilder sb) {
      // Value can be null, so do a null check..
      if (value == null) {
        return;
      }

      sb.appendHtmlConstant("<div>");
      sb.appendEscaped(value.getBezeichnung());
      sb.appendHtmlConstant(", ");
      sb.append(value.getKapazitaet());
      sb.appendHtmlConstant("</div>");
    }

}
