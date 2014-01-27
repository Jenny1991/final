package de.hdm.stundenplansystem.server.db;

import java.sql.*;
import java.util.Vector;

import de.hdm.stundenplansystem.shared.bo.*;

/**
 * Mapper-Klasse, die <code>Lehrveranstaltung</code>-Objekte auf eine
 * relationale Datenbank abbildet. Hierzu wird eine Reihe von Methoden zur
 * Verfügung gestellt, mit deren Hilfe z.B. Objekte gesucht, erzeugt,
 * modifiziert und gelöscht werden können. Das Mapping ist bidirektional.
 * D.h., Objekte können in DB-Strukturen und DB-Strukturen in Objekte
 * umgewandelt werden.
 * 
 * @see DozentMapper, RaumMapper, SemesterverbandMapper, StudiengangMapper,
 *      StundenplaneintragMapper, StundenplanMapper, ZeitslotMapper
 * @author Schmieder, Thies
 */
public class LehrveranstaltungMapper {

	/**
	 * Die Klasse LehrveranstaltungMapper wird nur einmal instantiiert. Man
	 * spricht hierbei von einem sogenannten <b>Singleton</b>.
	 * <p>
	 * Diese Variable ist durch den Bezeichner <code>static</code> nur einmal
	 * für sämtliche eventuellen Instanzen dieser Klasse vorhanden. Sie
	 * speichert die einzige Instanz dieser Klasse.
	 * 
	 * @see lehrveranstaltungMapper()
	 */
	private static LehrveranstaltungMapper lehrveranstaltungMapper = null;

	/**
	 * Geschützter Konstruktor - verhindert die Möglichkeit, mit
	 * <code>new</code> neue Instanzen dieser Klasse zu erzeugen.
	 */
	protected LehrveranstaltungMapper() {
	}

	/**
	 * Diese statische Methode kann aufgrufen werden durch
	 * <code>LehrveranstaltungMapper.lehrveranstaltungMapper()</code>. Sie
	 * stellt die Singleton-Eigenschaft sicher, indem Sie dafür sorgt, dass nur
	 * eine einzige Instanz von <code>LehrveranstaltungMapper</code> existiert.
	 * <p>
	 * 
	 * <b>Fazit:</b> LehrveranstaltungMapper sollte nicht mittels
	 * <code>new</code> instantiiert werden, sondern stets durch Aufruf dieser
	 * statischen Methode.
	 * 
	 * @return DAS <code>LehrveranstaltungMapper</code>-Objekt.
	 * @see lehrveranstaltungMapper
	 */
	public static LehrveranstaltungMapper lehrveranstaltungMapper() {
		if (lehrveranstaltungMapper == null) {
			lehrveranstaltungMapper = new LehrveranstaltungMapper();
		}

		return lehrveranstaltungMapper;
	}

	/**
	 * Suchen einer Lehrveranstaltung mit vorgegebener id. Da diese eindeutig
	 * ist, wird genau ein Objekt zurückgegeben.
	 * 
	 * @param id
	 *            Primärschlüsselattribut (->DB)
	 * @return Lehrveranstaltung-Objekt, das dem übergebenen Schlüssel
	 *         entspricht, null bei nicht vorhandenem DB-Tupel.
	 */
	public Lehrveranstaltung findByKey(int id) {
		// DB-Verbindung holen
		Connection con = DBConnection.connection();

		try {
			// Leeres SQL-Statement (JDBC) anlegen
			Statement stmt = con.createStatement();

			// Statement ausfüllen und als Query an die DB schicken
			ResultSet rs = stmt
					.executeQuery("SELECT id, bezeichnung, semester, umfang FROM lehrveranstaltung "
							+ "WHERE id=" + id);

			/*
			 * Da id Primärschlüssel ist, kann max. nur ein Tupel
			 * zurückgegeben werden. Prüfe, ob ein Ergebnis vorliegt.
			 */
			if (rs.next()) {
				// Ergebnis-Tupel in Objekt umwandeln
				Lehrveranstaltung l = new Lehrveranstaltung();
				l.setId(rs.getInt("id"));
				l.setBezeichnung(rs.getString("bezeichnung"));
				l.setSemester(rs.getInt("semester"));
				l.setUmfang(rs.getInt("umfang"));

				return l;
			}
		} catch (SQLException e2) {
			e2.printStackTrace();
			return null;
		}

		return null;
	}

	/**
	 * Auslesen aller Lehrveranstaltungen.
	 * 
	 * @return Ein Vektor mit Lehrveranstaltung-Objekten, die sämtliche
	 *         Lehrveranstaltungen repräsentieren. Bei evtl. Exceptions wird
	 *         ein partiell gefüllter oder ggf. auch leerer Vetor
	 *         zurückgeliefert.
	 */
	public Vector<Lehrveranstaltung> findAll() {
		Connection con = DBConnection.connection();

		// Ergebnisvektor vorbereiten
		Vector<Lehrveranstaltung> result = new Vector<Lehrveranstaltung>();

		try {
			Statement stmt = con.createStatement();

			ResultSet rs = stmt
					.executeQuery("SELECT id, bezeichnung, semester, umfang FROM lehrveranstaltung "
							+ " ORDER BY id");

			// Für jeden Eintrag im Suchergebnis wird nun ein
			// Lehrveranstaltung-Objekt erstellt.
			while (rs.next()) {
				Lehrveranstaltung l = new Lehrveranstaltung();
				l.setId(rs.getInt("id"));
				l.setBezeichnung(rs.getString("bezeichnung"));
				l.setSemester(rs.getInt("semester"));
				l.setUmfang(rs.getInt("umfang"));

				// Hinzufügen des neuen Objekts zum Ergebnisvektor
				result.addElement(l);
			}
		} catch (SQLException e2) {
			e2.printStackTrace();
		}

		// Ergebnisvektor zurückgeben
		return result;
	}

	/**
	 * Einfügen eines <code>Lehrveranstaltung</code>-Objekts in die Datenbank.
	 * Dabei wird auch der Primärschlüssel des übergebenen Objekts geprüft
	 * und ggf. berichtigt.
	 * 
	 * @param l
	 *            das zu speichernde Objekt
	 * @return das bereits übergebene Objekt, jedoch mit ggf. korrigierter
	 *         <code>id</code>.
	 */
	public Lehrveranstaltung insert(Lehrveranstaltung l) {
		Connection con = DBConnection.connection();

		try {
			Statement stmt = con.createStatement();

			/*
			 * Zunächst schauen wir nach, welches der momentan höchste
			 * Primärschlüsselwert ist.
			 */
			ResultSet rs = stmt
					.executeQuery("SELECT MAX(id) AS maxid "
							+ "FROM lehrveranstaltung ");

			// Wenn wir etwas zurückerhalten, kann dies nur einzeilig sein
			if (rs.next()) {
				/*
				 * l erhält den bisher maximalen, nun um 1 inkrementierten
				 * Primärschlüssel.
				 */
				l.setId(rs.getInt("maxid") + 1);

				stmt = con.createStatement();

				// Jetzt erst erfolgt die tatsächliche Einfügeoperation
				stmt.executeUpdate("INSERT INTO lehrveranstaltung (id, bezeichnung, semester, umfang) "
						+ "VALUES ("
						+ l.getId()
						+ ",'"
						+ l.getBezeichnung()
						+ "','"
						+ l.getSemester()
						+ "','"
						+ l.getUmfang()
						+ "')");
			}
		} catch (SQLException e2) {
			e2.printStackTrace();
		}

		/*
		 * Rückgabe, der evtl. korrigierten Lehrveranstaltung.
		 * 
		 * HINWEIS: Da in Java nur Referenzen auf Objekte und keine physischen
		 * Objekte übergeben werden, wäre die Anpassung des
		 * Lehrveranstaltung-Objekts auch ohne diese explizite Rückgabe
		 * außerhalb dieser Methode sichtbar. Die explizite Rückgabe von l
		 * ist eher ein Stilmittel, um zu signalisieren, dass sich das Objekt
		 * evtl. im Laufe der Methode verändert hat.
		 */
		return l;
	}

	/**
	 * Wiederholtes Schreiben eines Objekts in die Datenbank.
	 * 
	 * @param l
	 *            das Objekt, das in die DB geschrieben werden soll
	 * @return das als Parameter übergebene Objekt
	 */
	public Lehrveranstaltung update(Lehrveranstaltung l) {
		Connection con = DBConnection.connection();

		try {
			Statement stmt = con.createStatement();

			stmt.executeUpdate("UPDATE lehrveranstaltung SET "
					+ "umfang='" + l.getUmfang() + "', "
					+ "semester= " + "'" + l.getSemester() + "', "
					+ "bezeichnung= " + "'" + l.getBezeichnung()
					+ "' " + "WHERE id=" + l.getId());

		} catch (SQLException e2) {
			e2.printStackTrace();
		}

		// Um Analogie zu insert(Lehrveranstaltung l) zu wahren, geben wir l
		// zurück
		return l;
	}

	/**
	 * Löschen der Daten eines <code>Lehrveranstaltung</code>-Objekts aus der
	 * Datenbank.
	 * 
	 * @param l
	 *            das aus der DB zu löschende "Objekt"
	 */
	public void delete(Lehrveranstaltung l) {
		Connection con = DBConnection.connection();

		try {
			Statement stmt = con.createStatement();

			stmt.executeUpdate("DELETE FROM lehrveranstaltung "
					+ "WHERE id=" + l.getId());

		} catch (SQLException e2) {
			e2.printStackTrace();
		}
	}

}
