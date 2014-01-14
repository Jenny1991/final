package de.hdm.stundenplansystem.client;

import com.google.gwt.cell.client.AbstractCell;

import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

import de.hdm.stundenplansystem.shared.bo.*;

/**
 * @author C. Rathke, V. Hofmann
 *
 */
public class StudiengangCell extends AbstractCell<Studiengang> {
	@Override
    public void render(Context context, Studiengang value, SafeHtmlBuilder sb) {
      // Value can be null, so do a null check..
      if (value == null) {
        return;
      }

      sb.appendHtmlConstant("<div>");
      sb.appendEscaped(value.getBezeichnung());
      sb.appendHtmlConstant("</div>");
    }

}
