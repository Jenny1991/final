package de.hdm.stundenplansystem.server.db;

import java.sql.*;
import java.util.Vector;

import de.hdm.stundenplansystem.shared.bo.*;

/**
 * Mapper-Klasse, die <code>Zeitslot</code>-Objekte auf eine relationale
 * Datenbank abbildet. Hierzu wird eine Reihe von Methoden zur Verfügung
 * gestellt, mit deren Hilfe z.B. Objekte gesucht, erzeugt, modifiziert und
 * gelöscht werden können. Das Mapping ist bidirektional. D.h., Objekte können
 * in DB-Strukturen und DB-Strukturen in Objekte umgewandelt werden.
 * 
 * @see DozentMapper, LehrveranstaltungMapper, RaumMapper,
 *      SemesterverbandMapper, StundenplaneintragMapper, StundenplanMapper
 * @author Schmieder, Thies
 */
public class ZeitslotMapper {

	/**
	 * Die Klasse ZeitslotMapper wird nur einmal instantiiert. Man spricht
	 * hierbei von einem sogenannten <b>Singleton</b>.
	 * <p>
	 * Diese Variable ist durch den Bezeichner <code>static</code> nur einmal
	 * für sämtliche eventuellen Instanzen dieser Klasse vorhanden. Sie
	 * speichert die einzige Instanz dieser Klasse.
	 * 
	 * @see zeitslotMapper()
	 */
	private static ZeitslotMapper zeitslotMapper = null;

	/**
	 * Geschützter Konstruktor - verhindert die Möglichkeit, mit
	 * <code>new</code> neue Instanzen dieser Klasse zu erzeugen.
	 */
	protected ZeitslotMapper() {
	}

	/**
	 * Diese statische Methode kann aufgrufen werden durch
	 * <code>ZeitslotMapper.zeitslotMapper()</code>. Sie stellt die
	 * Singleton-Eigenschaft sicher, indem Sie dafür sorgt, dass nur eine
	 * einzige Instanz von <code>ZeitslotMapper</code> existiert.
	 * <p>
	 * 
	 * <b>Fazit:</b> ZeitslotMapper sollte nicht mittels <code>new</code>
	 * instantiiert werden, sondern stets durch Aufruf dieser statischen
	 * Methode.
	 * 
	 * @return DAS <code>ZeitslotMapper</code>-Objekt.
	 * @see zeitslotMapper
	 */
	public static ZeitslotMapper zeitslotMapper() {
		if (zeitslotMapper == null) {
			zeitslotMapper = new ZeitslotMapper();
		}

		return zeitslotMapper;
	}

	/**
	 * Suchen eines Zeitslots mit vorgegebener id. Da diese eindeutig ist, wird
	 * genau ein Objekt zurückgegeben.
	 * 
	 * @param id
	 *            Primärschlüsselattribut (->DB)
	 * @return Zeitslot-Objekt, das dem übergebenen Schlüssel entspricht, null
	 *         bei nicht vorhandenem DB-Tupel.
	 */
	public Zeitslot findByKey(int id) {
		// DB-Verbindung holen
		Connection con = DBConnection.connection();

		try {
			// Leeres SQL-Statement (JDBC) anlegen
			Statement stmt = con.createStatement();

			// Statement ausfüllen und als Query an die DB schicken
			ResultSet rs = stmt
					.executeQuery("SELECT id, wochentag, anfangszeit, endzeit FROM zeitslot "
							+ "WHERE id=" + id);

			/*
			 * Da id Primärschlüssel ist, kann max. nur ein Tupel zurückgegeben
			 * werden. Prüfe, ob ein Ergebnis vorliegt.
			 */
			if (rs.next()) {
				// Ergebnis-Tupel in Objekt umwandeln
				Zeitslot z = new Zeitslot();
				z.setId(rs.getInt("id"));
				z.setWochentag(rs.getString("wochentag"));
				z.setAnfangszeit(rs.getTime("anfangszeit"));
				z.setEndzeit(rs.getTime("endzeit"));

				return z;
			}
		} catch (SQLException e2) {
			e2.printStackTrace();
			return null;
		}

		return null;
	}

	/**
	 * Auslesen aller Zeitslots.
	 * 
	 * @return Ein Vektor mit Zeitslot-Objekten, die sämtliche Zeitslots
	 *         repräsentieren. Bei evtl. Exceptions wird ein partiell gefüllter
	 *         oder ggf. auch leerer Vetor zurückgeliefert.
	 */
	public Vector<Zeitslot> findAll() {
		Connection con = DBConnection.connection();

		// Ergebnisvektor vorbereiten
		Vector<Zeitslot> result = new Vector<Zeitslot>();

		try {
			Statement stmt = con.createStatement();

			ResultSet rs = stmt
					.executeQuery("SELECT id, wochentag, anfangszeit, endzeit"
							+ " FROM zeitslot " + " ORDER BY id");

			// Für jeden Eintrag im Suchergebnis wird nun ein Zeitslot-Objekt
			// erstellt.
			while (rs.next()) {
				Zeitslot z = new Zeitslot();
				z.setId(rs.getInt("id"));
				z.setWochentag(rs.getString("wochentag"));
				z.setAnfangszeit(rs.getTime("anfangszeit"));
				z.setEndzeit(rs.getTime("endzeit"));

				// Hinzufügen des neuen Objekts zum Ergebnisvektor
				result.addElement(z);
			}
		} catch (SQLException e2) {
			e2.printStackTrace();
		}

		// Ergebnisvektor zurückgeben
		return result;
	}

	/**
	 * Auslesen aller freien Zeitlosts. Zuerst werden alle Zeitlots aus 
	 * der Tabelle Zeitslots ermittelt. Im 2. Schritt wird eine Unterabfrage
	 * durchgeführt. Diese ermittelt aus der Tabelle Stundenplaneintrag die 
	 * Zeitslots, welche nicht erneut belegt werden dürfen. Dies ist abhängig
	 * von raumid, dozentid, stundenplanid und studienhalbjahr. Das Ergebnis aus Abfrage 2 wird
	 * mit NOT EXISTS von dem Ergebnis der ersten Abfrage abgezogen.
	 * 
	 * @return Ein Vektor mit Zeitslot-Objekten, die sämtliche Zeitslots
	 *         repräsentieren. Bei evtl. Exceptions wird ein partiell gefüllter
	 *         oder ggf. auch leerer Vetor zurückgeliefert.
	 */
	public Vector<Zeitslot> findFreeZeitslots(int raumid,
			int dozentid, String studienhalbjahr, int stundenplanid) {
		Connection con = DBConnection.connection();

		// Ergebnisvektor vorbereiten
		Vector<Zeitslot> result = new Vector<Zeitslot>();

		try {
			Statement stmt = con.createStatement();
			
			ResultSet rs = stmt
					.executeQuery("SELECT zeitslot.id, zeitslot.wochentag, zeitslot.anfangszeit, zeitslot.endzeit"
							+ " FROM zeitslot"
							+ " WHERE NOT EXISTS"
							+ " (SELECT zeitslot.id"
							+ " FROM stundenplaneintrag, stundenplan"
							+ " WHERE (stundenplaneintrag.zeitslotid = zeitslot.id "
							+ " AND stundenplaneintrag.stundenplanid = stundenplan.id"
							+ " AND stundenplan.studienhalbjahr = '" + studienhalbjahr + "'"
							+ " AND stundenplaneintrag.raumid = " + raumid + ")"
							+ " OR (stundenplaneintrag.zeitslotid = zeitslot.id "
							+ " AND stundenplaneintrag.stundenplanid = stundenplan.id"
							+ " AND stundenplan.studienhalbjahr = '" + studienhalbjahr + "'"
							+ " AND stundenplaneintrag.dozentid = " + dozentid + ")"
							+ " OR (stundenplaneintrag.zeitslotid = zeitslot.id "
							+ " AND stundenplaneintrag.stundenplanid = stundenplan.id"
							+ " AND stundenplan.studienhalbjahr = '" + studienhalbjahr + "'"
							+ " AND stundenplan.id = " + stundenplanid + "))"
						    + " ORDER BY find_in_set(zeitslot.wochentag,'Montag,Dienstag,Mittwoch,Donnerstag,Freitag,Samstag'), zeitslot.anfangszeit");
			
			// Für jeden Eintrag im Suchergebnis wird nun ein Zeitslot-Objekt
			// erstellt.
			while (rs.next()) {
				Zeitslot z = new Zeitslot();
				z.setId(rs.getInt("zeitslot.id"));
				z.setWochentag(rs.getString("zeitslot.wochentag"));
				z.setAnfangszeit(rs.getTime("zeitslot.anfangszeit"));
				z.setEndzeit(rs.getTime("zeitslot.endzeit"));

				// Hinzufügen des neuen Objekts zum Ergebnisvektor
				result.addElement(z);
			}
		} catch (SQLException e2) {
			e2.printStackTrace();
		}

		// Ergebnisvektor zurückgeben
		return result;
	}

}
