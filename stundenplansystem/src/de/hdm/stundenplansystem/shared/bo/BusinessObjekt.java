package de.hdm.stundenplansystem.shared.bo;
import java.io.Serializable;

/**
 * <p>
 * Die Klasse <code>BusinessObjekt</code> stellt die Basisklasse aller in diesem
 * Projekt für die Umsetzung des Fachkonzepts relevanten Klassen dar.
 * </p>
 * <p>
 * Zentrales Merkmal ist, dass jedes <code>BusinessObjekt</code> eine Nummer
 * besitzt, die man in einer relationalen Datenbank auch als Primärschlüssel
 * bezeichnen würde. Fernen ist jedes <code>BusinessObjekt</code> als
 * {@link Serializable} gekennzeichnet. Durch diese Eigenschaft kann jedes
 * <code>BusinessObjekt</code> automatisch in eine textuelle Form überführt und
 * z.B. zwischen Client und Server transportiert werden. Bei GWT RPC ist diese
 * textuelle Notation in JSON (siehe http://www.json.org/) kodiert.
 * </p>
 * 
 * @author thies
 * @version 1.0
 */

//author:thies & holz & hofmann

public class BusinessObjekt implements Serializable {

	private static final long serialVersionUID = 1L;
	
	 /**
	   * Die eindeutige Identifikationsnummer einer Instanz dieser Klasse.
	   */
	private int id;

	 /**
	   * Auslesen der ID.
	   */
	public int getId() {
		return this.id;
	}

	/**
	   * Setzen der ID
	   */
	public void setId(int id) {
		this.id = id;
	}

	/**
	   * Erzeugen einer einfachen textuellen Darstellung der jeweiligen Instanz.
	   * Dies kann selbstverständlich in Subklassen überschrieben werden.
	   */
	public String toString() {
		/*
	     * Wir geben den Klassennamen gefolgt von der ID des Objekts zurück.
	     */
		return this.getClass().getName() + "_" + this.id;
	}

	/**
	   * <p>
	   * Feststellen der <em>inhaltlichen</em> Gleichheit zweier
	   * <code>BusinessObjekt</code>-Objekte. Die Gleichheit wird in diesem Beispiel auf eine
	   * identische ID beschränkt.
	   * In unseren eigenen Klassen können wir diese Methode überschreiben und ihr
	   * mehr Intelligenz verleihen.
	   * </p>
	   */
	public boolean equals(Object o) {
		/*
	     * Abfragen, ob ein Objekt ungleich NULL ist und ob ein Objekt gecastet
	     * werden kann, sind immer wichtig!
	     */
		if (o != null && o instanceof BusinessObjekt) {
			BusinessObjekt a = (BusinessObjekt) o;
			try {
				if (a.getId() == this.id)
					return true;
			} catch (IllegalArgumentException e) {
				/*
		         * Wenn irgendetwas schief geht, dann geben wir sicherheitshalber false
		         * zurück.
		         */
				return false;
			}
		}
		/*
	     * Wenn bislang keine Gleichheit bestimmt werden konnte, dann müssen
	     * schließlich false zurückgeben.
	     */
		return false;
	}

	/**
	   * <p>
	   * Erzeugen einer ganzen Zahl, die für das <code>BusinessObjekt</code> charakteristisch ist.
	   * </p>
	   * <p>
	   * Zusammen mit <code>equals</code> sollte diese Methode immer definiert werden. Manche Java-Klassen
	   * verwendenden <code>hashCode</code>, um initial ein Objekt (z.B. in einer Hashtable) zu identifizieren. Erst danach
	   * würde mit <code>equals</code> festgestellt, ob es sich tatsächlich um das gesuchte Objekt handelt.
	   */
	public int hashCode() {
		return this.id;
	}

}
