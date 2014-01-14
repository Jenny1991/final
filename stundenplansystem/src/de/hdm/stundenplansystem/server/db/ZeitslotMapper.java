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
 * SemesterverbandMapper, StundenplaneintragMapper, StundenplanMapper
 * @author Schmieder, Thies
 */
public class ZeitslotMapper {

  /**
   * Die Klasse ZeitslotMapper wird nur einmal instantiiert. Man spricht hierbei
   * von einem sogenannten <b>Singleton</b>.
   * <p>
   * Diese Variable ist durch den Bezeichner <code>static</code> nur einmal für
   * sämtliche eventuellen Instanzen dieser Klasse vorhanden. Sie speichert die
   * einzige Instanz dieser Klasse.
   * 
   * @see zeitslotMapper()
   */
  private static ZeitslotMapper zeitslotMapper = null;

  /**
   * Geschützter Konstruktor - verhindert die Möglichkeit, mit <code>new</code>
   * neue Instanzen dieser Klasse zu erzeugen.
   */
  protected ZeitslotMapper() {
  }

  /**
   * Diese statische Methode kann aufgrufen werden durch
   * <code>ZeitslotMapper.zeitslotMapper()</code>. Sie stellt die
   * Singleton-Eigenschaft sicher, indem Sie dafür sorgt, dass nur eine einzige
   * Instanz von <code>ZeitslotMapper</code> existiert.
   * <p>
   * 
   * <b>Fazit:</b> ZeitslotMapper sollte nicht mittels <code>new</code>
   * instantiiert werden, sondern stets durch Aufruf dieser statischen Methode.
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
   * Suchen eines Zeitslots mit vorgegebener id. Da diese eindeutig ist,
   * wird genau ein Objekt zurückgegeben.
   * 
   * @param id Primärschlüsselattribut (->DB)
   * @return Zeitslot-Objekt, das dem übergebenen Schlüssel entspricht, null bei
   *         nicht vorhandenem DB-Tupel.
   */
  public Zeitslot findByKey(int id) {
    // DB-Verbindung holen
    Connection con = DBConnection.connection();

    try {
      // Leeres SQL-Statement (JDBC) anlegen
      Statement stmt = con.createStatement();

      // Statement ausfüllen und als Query an die DB schicken
      ResultSet rs = stmt.executeQuery("SELECT id, wochentag, anfangszeit, endzeit FROM Zeitslot "
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
        z.setAnfangszeit(rs.getDouble("anfangszeit"));
        z.setEndzeit(rs.getDouble("endzeit"));
                
        return z;
      }
    }
    catch (SQLException e2) {
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

      ResultSet rs = stmt.executeQuery("SELECT id, wochentag, anfangszeit, endzeit"
    	+ "FROM zeitslot "
        + " ORDER BY id");

      // Für jeden Eintrag im Suchergebnis wird nun ein Zeitslot-Objekt erstellt.
      while (rs.next()) {
        Zeitslot z = new Zeitslot();
        z.setId(rs.getInt("id"));
        z.setWochentag(rs.getString("wochentag"));
        z.setAnfangszeit(rs.getDouble("anfangszeit"));
        z.setEndzeit(rs.getDouble("endzeit"));

        // Hinzufügen des neuen Objekts zum Ergebnisvektor
        result.addElement(z);
      }
    }
    catch (SQLException e2) {
      e2.printStackTrace();
    }

    // Ergebnisvektor zurückgeben
    return result;
  }

  
  /**
   * Einfügen eines <code>Zeitslot</code>-Objekts in die Datenbank. Dabei wird
   * auch der Primärschlüssel des übergebenen Objekts geprüft und ggf.
   * berichtigt.
   * 
   * @param z das zu speichernde Objekt
   * @return das bereits übergebene Objekt, jedoch mit ggf. korrigierter
   *         <code>id</code>.
   */
  public Zeitslot insert(Zeitslot z) {
    Connection con = DBConnection.connection();

    try {
      Statement stmt = con.createStatement();

      /*
       * Zunächst schauen wir nach, welches der momentan höchste
       * Primärschlüsselwert ist.
       */
      ResultSet rs = stmt.executeQuery("SELECT MAX(id) AS maxid "
          + "FROM zeitslot ");

      // Wenn wir etwas zurückerhalten, kann dies nur einzeilig sein
      if (rs.next()) {
        /*
         * s erhält den bisher maximalen, nun um 1 inkrementierten
         * Primärschlüssel.
         */
        z.setId(rs.getInt("maxid") + 1);

        stmt = con.createStatement();

        // Jetzt erst erfolgt die tatsächliche Einfügeoperation
        stmt.executeUpdate("INSERT INTO zeitslot (id, wochentag, anfangszeit, endzeit"
        		+ " FROM Zeitslot) " 
        		+ "VALUES (" + z.getId() + "," + z.getWochentag() + "," + z.getAnfangszeit() + "," + z.getEndzeit() );
      }
    }
    catch (SQLException e2) {
      e2.printStackTrace();
    }

    /*
     * Rückgabe, des evtl. korrigierten Zeitslotes.
     * 
     * HINWEIS: Da in Java nur Referenzen auf Objekte und keine physischen
     * Objekte übergeben werden, wäre die Anpassung des Zeitslot-Objekts auch
     * ohne diese explizite Rückgabe außerhalb dieser Methode sichtbar. Die
     * explizite Rückgabe von s ist eher ein Stilmittel, um zu signalisieren,
     * dass sich das Objekt evtl. im Laufe der Methode verändert hat.
     */
    return z;
  }

  /**
   * Wiederholtes Schreiben eines Objekts in die Datenbank.
   * 
   * @param s das Objekt, das in die DB geschrieben werden soll
   * @return das als Parameter übergebene Objekt
   */
  public Zeitslot update(Zeitslot z) {
    Connection con = DBConnection.connection();

    try {
      Statement stmt = con.createStatement();

      stmt.executeUpdate("UPDATE zeitslot SET " 
    		  + "endzeit=\"" + z.getEndzeit() + "\", " 
    		  + "anfangszeit=\"" + z.getAnfangszeit() + "\", " 
    		  + "wochentag=\"" + z.getWochentag() + "\", "
    		  + "WHERE id=" + z.getId());

    }
    catch (SQLException e2) {
      e2.printStackTrace();
    }

    // Um Analogie zu insert(Zeitslot z) zu wahren, geben wir z zurück
    return z;
  }

  /**
   * Löschen der Daten eines <code>Zeitslot</code>-Objekts aus der Datenbank.
   * 
   * @param z das aus der DB zu löschende "Objekt"
   */
  public void delete(Zeitslot z) {
    Connection con = DBConnection.connection();

    try {
      Statement stmt = con.createStatement();

      stmt.executeUpdate("DELETE FROM zeitslot " + "WHERE id=" + z.getId());

    }
    catch (SQLException e2) {
      e2.printStackTrace();
    }
  }

}

