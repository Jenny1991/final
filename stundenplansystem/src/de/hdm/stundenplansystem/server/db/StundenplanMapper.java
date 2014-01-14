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
 * SemesterverbandMapper, StundenplaneintragMapper, ZeitslotMapper
 * @author Schmieder, Thies
 */
public class StundenplanMapper {

  /**
   * Die Klasse StundenplanMapper wird nur einmal instantiiert. Man spricht hierbei
   * von einem sogenannten <b>Singleton</b>.
   * <p>
   * Diese Variable ist durch den Bezeichner <code>static</code> nur einmal für
   * sämtliche eventuellen Instanzen dieser Klasse vorhanden. Sie speichert die
   * einzige Instanz dieser Klasse.
   * 
   * @see stundenplanMapper()
   */
  private static StundenplanMapper stundenplanMapper = null;

  /**
   * Geschützter Konstruktor - verhindert die Möglichkeit, mit <code>new</code>
   * neue Instanzen dieser Klasse zu erzeugen.
   */
  protected StundenplanMapper() {
  }

  /**
   * Diese statische Methode kann aufgrufen werden durch
   * <code>StundenplanMapper.stundenplanMapper()</code>. Sie stellt die
   * Singleton-Eigenschaft sicher, indem Sie dafür sorgt, dass nur eine einzige
   * Instanz von <code>StundenplanMapper</code> existiert.
   * <p>
   * 
   * <b>Fazit:</b> StundenplanMapper sollte nicht mittels <code>new</code>
   * instantiiert werden, sondern stets durch Aufruf dieser statischen Methode.
   * 
   * @return DAS <code>StundenplanMapper</code>-Objekt.
   * @see stundenplanMapper
   */
  public static StundenplanMapper stundenplanMapper() {
    if (stundenplanMapper == null) {
      stundenplanMapper = new StundenplanMapper();
    }

    return stundenplanMapper;
  }

  /**
   * Suchen eines Stundenplanes mit vorgegebener id. Da diese eindeutig ist,
   * wird genau ein Objekt zurückgegeben.
   * 
   * @param id Primärschlüsselattribut (->DB)
   * @return Stundenplan-Objekt, das dem übergebenen Schlüssel entspricht, null bei
   *         nicht vorhandenem DB-Tupel.
   */
  public Stundenplan findByKey(int id) {
    // DB-Verbindung holen
    Connection con = DBConnection.connection();

    try {
      // Leeres SQL-Statement (JDBC) anlegen
      Statement stmt = con.createStatement();

      // Statement ausfüllen und als Query an die DB schicken
      ResultSet rs = stmt.executeQuery("SELECT id, studienhalbjahr FROM Stundenplan "
    		  + "WHERE id=" + id);

      /*
       * Da id Primärschlüssel ist, kann max. nur ein Tupel zurückgegeben
       * werden. Prüfe, ob ein Ergebnis vorliegt.
       */
      if (rs.next()) {
        // Ergebnis-Tupel in Objekt umwandeln
        Stundenplan s = new Stundenplan();
        s.setId(rs.getInt("id"));
        s.setStudienhalbjahr(rs.getString("studienhalbjahr"));
                
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
   * Auslesen aller Stundenpläne.
   * 
   * @return Ein Vektor mit Stundenplan-Objekten, die sämtliche Stundenpläne
   *         repräsentieren. Bei evtl. Exceptions wird ein partiell gefüllter
   *         oder ggf. auch leerer Vetor zurückgeliefert.
   */
  public Vector<Stundenplan> findAll() {
    Connection con = DBConnection.connection();

    // Ergebnisvektor vorbereiten
    Vector<Stundenplan> result = new Vector<Stundenplan>();

    try {
      Statement stmt = con.createStatement();

      ResultSet rs = stmt.executeQuery("SELECT id, studienhalbjahr"
    	+ "FROM stundenplan "
        + " ORDER BY id");

      // Für jeden Eintrag im Suchergebnis wird nun ein Stundenplan-Objekt erstellt.
      while (rs.next()) {
        Stundenplan s = new Stundenplan();
        s.setId(rs.getInt("id"));
        s.setStudienhalbjahr(rs.getString("studienhalbjahr"));

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
   * Einfügen eines <code>Stundenplan</code>-Objekts in die Datenbank. Dabei wird
   * auch der Primärschlüssel des übergebenen Objekts geprüft und ggf.
   * berichtigt.
   * 
   * @param s das zu speichernde Objekt
   * @return das bereits übergebene Objekt, jedoch mit ggf. korrigierter
   *         <code>id</code>.
   */
  public Stundenplan insert(Stundenplan s) {
    Connection con = DBConnection.connection();

    try {
      Statement stmt = con.createStatement();

      /*
       * Zunächst schauen wir nach, welches der momentan höchste
       * Primärschlüsselwert ist.
       */
      ResultSet rs = stmt.executeQuery("SELECT MAX(id) AS maxid "
          + "FROM stundenplan ");

      // Wenn wir etwas zurückerhalten, kann dies nur einzeilig sein
      if (rs.next()) {
        /*
         * s erhält den bisher maximalen, nun um 1 inkrementierten
         * Primärschlüssel.
         */
        s.setId(rs.getInt("maxid") + 1);

        stmt = con.createStatement();

        // Jetzt erst erfolgt die tatsächliche Einfügeoperation
        stmt.executeUpdate("INSERT INTO stundenplan (id, studienhalbjahr FROM Stundenplan) " + "VALUES ("
            + s.getId() + "," + s.getStudienhalbjahr() );
      }
    }
    catch (SQLException e2) {
      e2.printStackTrace();
    }

    /*
     * Rückgabe, des evtl. korrigierten Stundenplanes.
     * 
     * HINWEIS: Da in Java nur Referenzen auf Objekte und keine physischen
     * Objekte übergeben werden, wäre die Anpassung des Stundenplan-Objekts auch
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
  public Stundenplan update(Stundenplan s) {
    Connection con = DBConnection.connection();

    try {
      Statement stmt = con.createStatement();

      stmt.executeUpdate("UPDATE stundenplaneintrag SET " 
    		  + "studienhalbjahr=\"" + s.getStudienhalbjahr() + "\", " 
    		  + "WHERE id=" + s.getId());

    }
    catch (SQLException e2) {
      e2.printStackTrace();
    }

    // Um Analogie zu insert(Stundenplan s) zu wahren, geben wir s zurück
    return s;
  }

  /**
   * Löschen der Daten eines <code>Stundenplan</code>-Objekts aus der Datenbank.
   * 
   * @param s das aus der DB zu löschende "Objekt"
   */
  public void delete(Stundenplan s) {
    Connection con = DBConnection.connection();

    try {
      Statement stmt = con.createStatement();

      stmt.executeUpdate("DELETE FROM stundenplan " + "WHERE id=" + s.getId());

    }
    catch (SQLException e2) {
      e2.printStackTrace();
    }
  }

}

