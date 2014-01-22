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
 *
 */
public class SemesterverbandCell extends AbstractCell<Semesterverband> {
	
	String bezeichnung;
    final VerwaltungsklasseAsync verwaltungsSvc = GWT.create(Verwaltungsklasse.class);

	public void render(Context context, final Semesterverband value, final SafeHtmlBuilder sb) {
	      // Value can be null, so do a null check..
	      if (value == null) {
	        return;
	      }
	      
			verwaltungsSvc.getStudiengangById(value.getStudiengangId(), new AsyncCallback<Studiengang>() {
				@Override
				  public void onFailure (Throwable caught) {
				  }

				@Override
				public void onSuccess(Studiengang result) {
					bezeichnung = result.getBezeichnung();
				}
				
			});
			
		    sb.appendHtmlConstant("<div>");     
			sb.appendEscapedLines(bezeichnung);
			sb.appendHtmlConstant(", ");
		    sb.append(value.getSemester());
		    sb.appendHtmlConstant(", ");
		    sb.append(value.getStudierendenAnzahl());
		    sb.appendHtmlConstant(", ");
		    sb.appendEscaped(value.getJahrgang());
		    sb.appendHtmlConstant("</div>");     
				      				
		}
}
		
	




