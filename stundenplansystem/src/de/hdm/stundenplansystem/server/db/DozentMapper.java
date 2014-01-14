package de.hdm.stundenplansystem.server.db;

import java.sql.*;
import java.util.Vector;

import de.hdm.stundenplansystem.shared.bo.*;

/**
 * Mapper-Klasse, die <code>Dozent</code>-Objekte auf eine relationale
 * Datenbank abbildet. Hierzu wird eine Reihe von Methoden zur Verfügung
 * gestellt, mit deren Hilfe z.B. Objekte gesucht, erzeugt, modifiziert und
 * gelöscht werden können. Das Mapping ist bidirektional. D.h., Objekte können
 * in DB-Strukturen und DB-Strukturen in Objekte umgewandelt werden.
 * 
 * @see LehrveranstaltungMapper, RaumMapper, SemesterverbandMapper, StudiengangMapper,
 * StundenplaneintragMapper, StundenplanMapper, ZeitslotMapper
 * @author Schmieder, Thies
 */
public class DozentMapper {

  /**
   * Die Klasse DozentMapper wird nur einmal instantiiert. Man spricht hierbei
   * von einem sogenannten <b>Singleton</b>.
   * <p>
   * Diese Variable ist durch den Bezeichner <code>static</code> nur einmal für
   * sämtliche eventuellen Instanzen dieser Klasse vorhanden. Sie speichert die
   * einzige Instanz dieser Klasse.
   * 
   * @see dozentMapper()
   */
  private static DozentMapper dozentMapper = null;

  /**
   * Geschätzter Konstruktor - verhindert die Möglichkeit, mit <code>new</code>
   * neue Instanzen dieser Klasse zu erzeugen.
   */
  protected DozentMapper() {
  }

  /**
   * Diese statische Methode kann aufgrufen werden durch
   * <code>DozentMapper.dozentMapper()</code>. Sie stellt die
   * Singleton-Eigenschaft sicher, indem Sie dafür sorgt, dass nur eine einzige
   * Instanz von <code>DozentMapper</code> existiert.
   * <p>
   * 
   * <b>Fazit:</b> DozentMapper sollte nicht mittels <code>new</code>
   * instantiiert werden, sondern stets durch Aufruf dieser statischen Methode.
   * 
   * @return DAS <code>DozentMapper</code>-Objekt.
   * @see dozentMapper
   */
  public static DozentMapper dozentMapper() {
    if (dozentMapper == null) {
      dozentMapper = new DozentMapper();
    }

    return dozentMapper;
  }

  /**
   * Suchen eines Dozenten mit vorgegebener id. Da diese eindeutig ist,
   * wird genau ein Objekt zurückgegeben.
   * 
   * @param id Primarschlüsselattribut (->DB)
   * @return Dozent-Objekt, das dem übergebenen Schlüssel entspricht, null bei
   *         nicht vorhandenem DB-Tupel.
   */
  public Dozent findByKey(int id) {
    // DB-Verbindung holen
    Connection con = DBConnection.connection();

    try {
      // Leeres SQL-Statement (JDBC) anlegen
      Statement stmt = con.createStatement();

      // Statement ausfüllen und als Query an die DB schicken
      ResultSet rs = stmt.executeQuery("SELECT id, vorname, nachname FROM Dozent "
          + "WHERE id=" + id);

      /*
       * Da id Primarschlüssel ist, kann max. nur ein Tupel zurückgegeben
       * werden. Prüfe, ob ein Ergebnis vorliegt.
       */
      if (rs.next()) {
        // Ergebnis-Tupel in Objekt umwandeln
        Dozent d = new Dozent();
        d.setId(rs.getInt("id"));
        d.setNachname(rs.getString("nachname"));
        d.setVorname(rs.getString("vorname"));
        return d;
      }
    }
    catch (SQLException e2) {
      e2.printStackTrace();
      return null;
    }

    return null;
  }

  /**
   * Auslesen aller Dozenten.
   * 
   * @return Ein Vektor mit Dozent-Objekten, die samtliche Dozenten
   *         repräsentieren. Bei evtl. Exceptions wird ein partiell gefällter
   *         oder ggf. auch leerer Vetor zurückgeliefert.
   */
  public Vector<Dozent> findAll() {
    Connection con = DBConnection.connection();

    // Ergebnisvektor vorbereiten
    Vector<Dozent> result = new Vector<Dozent>();

    try {
      Statement stmt = con.createStatement();

      ResultSet rs = stmt.executeQuery("SELECT id, nachname, vorname FROM dozent "
          + " ORDER BY id");

      // Für jeden Eintrag im Suchergebnis wird nun ein Dozent-Objekt erstellt.
      while (rs.next()) {
        Dozent d = new Dozent();
        d.setId(rs.getInt("id"));
        d.setNachname(rs.getString("nachname"));
        d.setVorname(rs.getString("vorname"));

        // Hinzufügen des neuen Objekts zum Ergebnisvektor
        result.addElement(d);
      }
    }
    catch (SQLException e2) {
      e2.printStackTrace();
    }

    // Ergebnisvektor zurückgeben
    return result;
  }

  
  /**
   * Einfügen eines <code>Dozent</code>-Objekts in die Datenbank. Dabei wird
   * auch der Primärschlüssel des übergebenen Objekts geprüft und ggf.
   * berichtigt.
   * 
   * @param d das zu speichernde Objekt
   * @return das bereits übergebene Objekt, jedoch mit ggf. korrigierter
   *         <code>id</code>.
   */
  public Dozent insert(Dozent d) {
    Connection con = DBConnection.connection();

    try {
      Statement stmt = con.createStatement();

      /*
       * Zunachst schauen wir nach, welches der momentan höchste
       * Primarschlüsselwert ist.
       */
      ResultSet rs = stmt.executeQuery("SELECT MAX(id) AS maxid "
          + "FROM dozent ");

      // Wenn wir etwas zurückerhalten, kann dies nur einzeilig sein
      if (rs.next()) {
        /*
         * d erhalt den bisher maximalen, nun um 1 inkrementierten
         * Primarschlüssel.
         */
        d.setId(rs.getInt("maxid") + 1);

        stmt = con.createStatement();

        // Jetzt erst erfolgt die tatsachliche Einfügeoperation
        stmt.executeUpdate("INSERT INTO dozent (id, nachname, vorname) " + "VALUES ("
            + d.getId() + "," + d.getNachname() + "," + d.getVorname() );
      }
    }
    catch (SQLException e2) {
      e2.printStackTrace();
    }

    /*
     * Rückgabe, des evtl. korrigierten Dozents.
     * 
     * HINWEIS: Da in Java nur Referenzen auf Objekte und keine physischen
     * Objekte übergeben werden, ware die Anpassung des Dozent-Objekts auch
     * ohne diese explizite Rückgabe auï¿½erhalb dieser Methode sichtbar. Die
     * explizite Rückgabe von d ist eher ein Stilmittel, um zu signalisieren,
     * dass sich das Objekt evtl. im Laufe der Methode verandert hat.
     */
    return d;
  }

  /**
   * Wiederholtes Schreiben eines Objekts in die Datenbank.
   * 
   * @param d das Objekt, das in die DB geschrieben werden soll
   * @return das als Parameter übergebene Objekt
   */
  public Dozent update(Dozent d) {
    Connection con = DBConnection.connection();

    try {
      Statement stmt = con.createStatement();

      stmt.executeUpdate("UPDATE dozent " + "\" " + "WHERE id=" + d.getId());

    }
    catch (SQLException e2) {
      e2.printStackTrace();
    }

    // Um Analogie zu insert(Dozent d) zu wahren, geben wir d zurück
    return d;
  }

  /**
   * Löschen der Daten eines <code>Dozent</code>-Objekts aus der Datenbank.
   * 
   * @param d das aus der DB zu löschende "Objekt"
   */
  public void delete(Dozent d) {
    Connection con = DBConnection.connection();

    try {
      Statement stmt = con.createStatement();

      stmt.executeUpdate("DELETE FROM dozent " + "WHERE id=" + d.getId());

    }
    catch (SQLException e2) {
      e2.printStackTrace();
    }
  }

}

