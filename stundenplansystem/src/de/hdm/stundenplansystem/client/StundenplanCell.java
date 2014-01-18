package de.hdm.stundenplansystem.client;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

import de.hdm.stundenplansystem.shared.bo.Stundenplan;

public class StundenplanCell  extends AbstractCell<Stundenplan> {
	@Override
    public void render(Context context, Stundenplan value, SafeHtmlBuilder sb) {
      // Value can be null, so do a null check..
      if (value == null) {
        return;
      }

      sb.appendHtmlConstant("<div>");
      sb.appendEscaped(value.getStudienhalbjahr());
      sb.appendHtmlConstant("</div>");
    }

}