package de.hdm.stundenplansystem.client;

import com.google.gwt.cell.client.AbstractCell;

import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

import de.hdm.stundenplansystem.shared.bo.*;

/**
 * @author C. Rathke, V. Hofmann
 *
 */
public class LehrveranstaltungCell extends AbstractCell<Lehrveranstaltung> {
	@Override
    public void render(Context context, Lehrveranstaltung value, SafeHtmlBuilder sb) {
      // Value can be null, so do a null check..
      if (value == null) {
        return;
      }

      sb.appendHtmlConstant("<div>");
      sb.appendEscaped(value.getBezeichnung());
      sb.appendHtmlConstant(", ");
      sb.append(value.getSemester());
      sb.appendHtmlConstant(", ");
      sb.append(value.getUmfang());
      sb.appendHtmlConstant("</div>");
    }

}
