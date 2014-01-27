package de.hdm.stundenplansystem.shared;

import de.hdm.stundenplansystem.shared.bo.*;
import de.hdm.stundenplansystem.shared.report.*;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * @author thies & l.hofmann & holz
 */
@RemoteServiceRelativePath("reportgenerator")
public interface ReportGenerator extends RemoteService {

	/**
	 * Initialisierung des Objekts. Diese Methode ist vor dem Hintergrund von
	 * GWT RPC zustzlich zum No Argument Constructor der implementierenden
	 * Klasse VerwaltungsklasseImpl} notwendig. Bitte diese Methode direkt nach
	 * der Instantiierung aufrufen.
	 * 
	 * @throws IllegalArgumentException
	 */
	public void init() throws IllegalArgumentException;

	/**
	 * Setzen des zugeordneten Raums.
	 * 
	 * @para Raum-Objekt
	 * @throws IllegalArgumentException
	 */
	public void setRaum(Raum r) throws IllegalArgumentException;

	public void setDozent(Dozent d) throws IllegalArgumentException;

	public void setSemesterverband(int sv)
			throws IllegalArgumentException;

	/**
	 * Erstellen eines <code>RaumbelegungsReport</code>-Reports. Dieser
	 * Report-Typ stellt sämtliche Raume und ihre Belegungen dar.
	 * 
	 * @param r eine Referenz auf das Raumobjekt bzgl. dessen der Report
	 *        erstellt werden soll.
	 * @return das fertige Reportobjekt
	 * @throws IllegalArgumentException
	 * @see RaumbelegungsReport
	 * 
	 */
	public RaumbelegungsReport createRaumbelungsReport(int raumId, Stundenplan stundenplan)
			throws IllegalArgumentException;

	/**
	 * Erstellen eines <code>StundenplanDozentReport</code>-Reports. Dieser
	 * Report-Typ stellt den Stundenplan eines Dozenten dar.
	 * 
	 * @param d eine Referenz auf das Dozentenobjekt bzgl. dessen der Report
	 *            erstellt werden soll.
	 * @return das fertige Reportobjekt
	 * @throws IllegalArgumentException
	 * @see StundenplanDozentReport
	 */
	public StundenplanDozentReport createStundenplanDozentReport(
			int dozentId, Stundenplan stundenplan) throws IllegalArgumentException;

	/**
	 * Erstellen eines <code>StundenplanSemesterverbandReport</code>-Reports.
	 * Dieser Report-Typ stellt den Stundenplan eine dar.
	 * 
	 * @param semesterverbandId die Id der Referenz auf das Semesterverbandobjekt 
	 * 		  bzgl. dessen der Report erstellt werden soll.
	 * @param stundenplanId die Id der Referenz auf das Stundenplanobjekt 
	 * 		  bzgl. dessen der Report erstellt werden soll.
	 * @return das fertige Reportobjekt
	 * @throws IllegalArgumentException
	 * @see StundenplanSemesterverbandReport
	 */

	public StundenplanSemesterverbandReport createStundenplanSemesterverbandReport(
			int semesterverbandId, int stundenplanId)
			throws IllegalArgumentException;
}
