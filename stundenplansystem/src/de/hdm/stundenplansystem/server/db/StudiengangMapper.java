package de.hdm.stundenplansystem.server.db;

import java.sql.*;
import java.util.Vector;

import de.hdm.stundenplansystem.shared.bo.*;

/**
 * Mapper-Klasse, die <code>Stundenplan</code>-Objekte auf eine relationale
 * Datenbank abbildet. Hierzu wird eine Reihe von Methoden zur Verfügung
 * gestellt, mit deren Hilfe z.B. Objekte gesucht, erzeugt, modifiziert und
 * gelöscht werden können. Das Mapping ist bidirektional. D.h., Objekte können
 * in DB-Strukturen und DB-Strukturen in Objekte umgewandelt werden.
 * 
 * @see DozentMapper, LehrveranstaltungMapper, RaumMapper, 
 * SemesterverbandMapper, StundenplanMapper, StundenplaneintragMapper, ZeitslotMapper
 * @author Schmieder, Thies
 */
public class StudiengangMapper {

  /**
   * Die Klasse StudiengangMapper wird nur einmal instantiiert. Man spricht hierbei
   * von einem sogenannten <b>Singleton</b>.
   * <p>
   * Diese Variable ist durch den Bezeichner <code>static</code> nur einmal für
   * sämtliche eventuellen Instanzen dieser Klasse vorhanden. Sie speichert die
   * einzige Instanz dieser Klasse.
   * 
   * @see studiengangMapper()
   */
  private static StudiengangMapper studiengangMapper = null;

  /**
   * Geschützter Konstruktor - verhindert die Möglichkeit, mit <code>new</code>
   * neue Instanzen dieser Klasse zu erzeugen.
   */
  protected StudiengangMapper() {
  }

  /**
   * Diese statische Methode kann aufgrufen werden durch
   * <code>StudiengangMapper.studiengangMapper()</code>. Sie stellt die
   * Singleton-Eigenschaft sicher, indem Sie dafür sorgt, dass nur eine einzige
   * Instanz von <code>StudiengangMapper</code> existiert.
   * <p>
   * 
   * <b>Fazit:</b> StudiengangMapper sollte nicht mittels <code>new</code>
   * instantiiert werden, sondern stets durch Aufruf dieser statischen Methode.
   * 
   * @return DAS <code>StudiengangMapper</code>-Objekt.
   * @see studiengangMapper
   */
  public static StudiengangMapper studiengangMapper() {
    if (studiengangMapper == null) {
      studiengangMapper = new StudiengangMapper();
    }

    return studiengangMapper;
  }

  /**
   * Suchen eines Studienganges mit vorgegebener id. Da diese eindeutig ist,
   * wird genau ein Objekt zurückgegeben.
   * 
   * @param id Primärschlüsselattribut (->DB)
   * @return Studiengang-Objekt, das dem übergebenen Schlüssel entspricht, null bei
   *         nicht vorhandenem DB-Tupel.
   */
  public Studiengang findByKey(int id) {
    // DB-Verbindung holen
    Connection con = DBConnection.connection();

    try {
      // Leeres SQL-Statement (JDBC) anlegen
      Statement stmt = con.createStatement();

      // Statement ausfüllen und als Query an die DB schicken
      ResultSet rs = stmt.executeQuery("SELECT id, bezeichnung FROM studiengang "
    		  + "WHERE id=" + id);

      /*
       * Da id Primärschlüssel ist, kann max. nur ein Tupel zurückgegeben
       * werden. Prüfe, ob ein Ergebnis vorliegt.
       */
      if (rs.next()) {
        // Ergebnis-Tupel in Objekt umwandeln
        Studiengang s = new Studiengang();
        s.setId(rs.getInt("id"));
        s.setBezeichnung(rs.getString("bezeichnung"));
                
        return s;
      }
    }
    catch (SQLException e2) {
      e2.printStackTrace();
      return null;
    }

    return null;
  }

  /**
   * Auslesen aller Studiengänge.
   * 
   * @return Ein Vektor mit Studiengang-Objekten, die sämtliche Studiengänge 
   *         repräsentieren. Bei evtl. Exceptions wird ein partiell gefüllter
   *         oder ggf. auch leerer Vetor zurückgeliefert.
   */
  public Vector<Studiengang> findAll() {
    Connection con = DBConnection.connection();

    // Ergebnisvektor vorbereiten
    Vector<Studiengang> result = new Vector<Studiengang>();

    try {
      Statement stmt = con.createStatement();

      ResultSet rs = stmt.executeQuery("SELECT id, bezeichnung"
    	+ "FROM studiengang "
        + " ORDER BY id");

      // Für jeden Eintrag im Suchergebnis wird nun ein Studiengang-Objekt erstellt.
      while (rs.next()) {
        Studiengang s = new Studiengang();
        s.setId(rs.getInt("id"));
        s.setBezeichnung(rs.getString("bezeichnung"));

        // Hinzufügen des neuen Objekts zum Ergebnisvektor
        result.addElement(s);
      }
    }
    catch (SQLException e2) {
      e2.printStackTrace();
    }

    // Ergebnisvektor zurückgeben
    return result;
  }

  
  /**
   * Einfügen eines <code>Studiengang</code>-Objekts in die Datenbank. Dabei wird
   * auch der Primärschlüssel des übergebenen Objekts geprüft und ggf.
   * berichtigt.
   * 
   * @param s das zu speichernde Objekt
   * @return das bereits übergebene Objekt, jedoch mit ggf. korrigierter
   *         <code>id</code>.
   */
  public Studiengang insert(Studiengang s) {
    Connection con = DBConnection.connection();

    try {
      Statement stmt = con.createStatement();

      /*
       * Zunächst schauen wir nach, welches der momentan höchste
       * Primärschlüsselwert ist.
       */
      ResultSet rs = stmt.executeQuery("SELECT MAX(id) AS maxid "
          + "FROM studiengang ");

      // Wenn wir etwas zurückerhalten, kann dies nur einzeilig sein
      if (rs.next()) {
        /*
         * s erhält den bisher maximalen, nun um 1 inkrementierten
         * Primärschlüssel.
         */
        s.setId(rs.getInt("maxid") + 1);

        stmt = con.createStatement();

        // Jetzt erst erfolgt die tatsächliche Einfügeoperation
        stmt.executeUpdate("INSERT INTO studiengang (id, bezeichnung)" + "VALUES ("
            + s.getId() + "," + s.getBezeichnung() );
      }
    }
    catch (SQLException e2) {
      e2.printStackTrace();
    }

    /*
     * Rückgabe, des evtl. korrigierten Studienganges.
     * 
     * HINWEIS: Da in Java nur Referenzen auf Objekte und keine physischen
     * Objekte übergeben werden, wäre die Anpassung des Studiengang-Objekts auch
     * ohne diese explizite Rückgabe außerhalb dieser Methode sichtbar. Die
     * explizite Rückgabe von s ist eher ein Stilmittel, um zu signalisieren,
     * dass sich das Objekt evtl. im Laufe der Methode verändert hat.
     */
    return s;
  }

  /**
   * Wiederholtes Schreiben eines Objekts in die Datenbank.
   * 
   * @param s das Objekt, das in die DB geschrieben werden soll
   * @return das als Parameter übergebene Objekt
   */
  public Studiengang update(Studiengang s) {
    Connection con = DBConnection.connection();

    try {
      Statement stmt = con.createStatement();

      stmt.executeUpdate("UPDATE studiengang SET " 
    		  + "bezeichnung=\"" + s.getBezeichnung() + "\", " 
    		  + "WHERE id=" + s.getId());

    }
    catch (SQLException e2) {
      e2.printStackTrace();
    }

    // Um Analogie zu insert(Studiengang s) zu wahren, geben wir s zurück
    return s;
  }

  /**
   * Löschen der Daten eines <code>Studiengang</code>-Objekts aus der Datenbank.
   * 
   * @param s das aus der DB zu löschende "Objekt"
   */
  public void delete(Studiengang s) {
    Connection con = DBConnection.connection();

    try {
      Statement stmt = con.createStatement();

      stmt.executeUpdate("DELETE FROM studiengang " + "WHERE id=" + s.getId());

    }
    catch (SQLException e2) {
      e2.printStackTrace();
    }
  }

}
