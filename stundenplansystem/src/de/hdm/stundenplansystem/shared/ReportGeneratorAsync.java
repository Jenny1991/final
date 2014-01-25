package de.hdm.stundenplansystem.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.hdm.stundenplansystem.shared.bo.*;
import de.hdm.stundenplansystem.shared.report.*;

/**
 * Das asynchrone Gegenstück des Interface {@link ReportGenerator}. Es wird
 * semiautomatisch durch das Google Plugin erstellt und gepflegt. Daher erfolgt
 * hier keine weitere Dokumentation. Für weitere Informationen siehe das
 * synchrone Interface {@link ReportGenerator}.
 * 
 * @author thies & l.hofmann & holz
 */
public interface ReportGeneratorAsync {

	void init(AsyncCallback<Void> callback);

	void createStundenplanDozentReport(int dozentId,
			AsyncCallback<StundenplanDozentReport> callback);

	void createRaumbelungsReport(int raumId,
			AsyncCallback<RaumbelegungsReport> callback);

	void createStundenplanSemesterverbandReport(
			int semesterverbandId, int stundenplanId,
			AsyncCallback<StundenplanSemesterverbandReport> callback);

	void setRaum(Raum r, AsyncCallback<Void> callback);

	void setDozent(Dozent d, AsyncCallback<Void> callback);

	void setSemesterverband(int sv, AsyncCallback<Void> callback);

}
