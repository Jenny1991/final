package de.hdm.stundenplansystem.client;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import de.hdm.stundenplansystem.shared.bo.*;

/**
 * @author C. Rathke, J.Espich
 *
 */
public class StundenplaneintragCell extends AbstractCell<Stundenplaneintrag> {
	@Override
    public void render(Context context, Stundenplaneintrag value, SafeHtmlBuilder sb) {
      // Value can be null, so do a null check..
      if (value == null) {
        return;
      }

      sb.appendHtmlConstant("<div>");
      sb.append(value.getDozentId());
      sb.appendHtmlConstant(", ");
      sb.append(value.getZeitslotId());
      sb.appendHtmlConstant(", ");
      sb.append(value.getRaumId());
      sb.appendHtmlConstant(", ");
      sb.append(value.getStundenplanId());
      sb.appendHtmlConstant(", ");
      sb.append(value.getSemesterverbandId());
      sb.appendHtmlConstant(", ");
      sb.append(value.getLehrveranstaltungId());
      sb.appendHtmlConstant("</div>");
    }

}