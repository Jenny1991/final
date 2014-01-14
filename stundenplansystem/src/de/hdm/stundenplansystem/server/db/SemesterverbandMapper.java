package de.hdm.stundenplansystem.server.db;

import java.sql.*;
import java.util.Vector;

import de.hdm.stundenplansystem.shared.bo.*;

/**
 * Mapper-Klasse, die <code>Semesterverband</code>-Objekte auf eine relationale
 * Datenbank abbildet. Hierzu wird eine Reihe von Methoden zur Verfügung
 * gestellt, mit deren Hilfe z.B. Objekte gesucht, erzeugt, modifiziert und
 * gelöscht werden können. Das Mapping ist bidirektional. D.h., Objekte können
 * in DB-Strukturen und DB-Strukturen in Objekte umgewandelt werden.
 * 
 * @see DozentMapper, LehrveranstaltungMapper, RaumMapper, 
 * StudiengangMapper, StundenplaneintragMapper, StundenplanMapper, ZeitslotMapper
 * @author Schmieder, Thies
 */
public class SemesterverbandMapper {

  /**
   * Die Klasse SemesterverbandMapper wird nur einmal instantiiert. Man spricht hierbei
   * von einem sogenannten <b>Singleton</b>.
   * <p>
   * Diese Variable ist durch den Bezeichner <code>static</code> nur einmal für
   * sämtliche eventuellen Instanzen dieser Klasse vorhanden. Sie speichert die
   * einzige Instanz dieser Klasse.
   * 
   * @see semesterverbandMapper()
   */
  private static SemesterverbandMapper semesterverbandMapper = null;

  /**
   * Geschützter Konstruktor - verhindert die Möglichkeit, mit <code>new</code>
   * neue Instanzen dieser Klasse zu erzeugen.
   */
  protected SemesterverbandMapper() {
  }

  /**
   * Diese statische Methode kann aufgrufen werden durch
   * <code>SemesterverbandMapper.semesterverbandMapper()</code>. Sie stellt die
   * Singleton-Eigenschaft sicher, indem Sie dafür sorgt, dass nur eine einzige
   * Instanz von <code>SemesterverbandMapper</code> existiert.
   * <p>
   * 
   * <b>Fazit:</b> SemesterverbandMapper sollte nicht mittels <code>new</code>
   * instantiiert werden, sondern stets durch Aufruf dieser statischen Methode.
   * 
   * @return DAS <code>SemesterverbandMapper</code>-Objekt.
   * @see semesterverbandMapper
   */
  public static SemesterverbandMapper semesterverbandMapper() {
    if (semesterverbandMapper == null) {
      semesterverbandMapper = new SemesterverbandMapper();
    }

    return semesterverbandMapper;
  }

  /**
   * Suchen eines Semesterverbandes mit vorgegebener id. Da diese eindeutig ist,
   * wird genau ein Objekt zurückgegeben.
   * 
   * @param id Primärschlüsselattribut (->DB)
   * @return Semesterverband-Objekt, das dem übergebenen Schlüssel entspricht, null bei
   *         nicht vorhandenem DB-Tupel.
   */
  public Semesterverband findByKey(int id) {
    // DB-Verbindung holen
    Connection con = DBConnection.connection();

    try {
      // Leeres SQL-Statement (JDBC) anlegen
      Statement stmt = con.createStatement();

      // Statement ausfüllen und als Query an die DB schicken
      ResultSet rs = stmt.executeQuery("SELECT id, semester, studierendenAnzahl, jahrgang, stundenplanid, studiengangid FROM Semesterverband "
          + "WHERE id=" + id);

      /*
       * Da id Primärschlüssel ist, kann max. nur ein Tupel zurückgegeben
       * werden. Prüfe, ob ein Ergebnis vorliegt.
       */
      if (rs.next()) {
        // Ergebnis-Tupel in Objekt umwandeln
        Semesterverband s = new Semesterverband();
        s.setId(rs.getInt("id"));
        s.setSemester(rs.getInt("semester"));
        s.setStudierendenAnzahl(rs.getInt("studierendenAnzahl"));
        s.setJahrgang(rs.getString("jahrgang"));
        s.setStundenplanId(rs.getInt("stundenplanid"));
        s.setStudiengangId(rs.getInt("studiengangid"));
        
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
   * Auslesen aller Semesterverbände.
   * 
   * @return Ein Vektor mit Semesterverband-Objekten, die sämtliche Semesterverbände
   *         repräsentieren. Bei evtl. Exceptions wird ein partiell gefüllter
   *         oder ggf. auch leerer Vetor zurückgeliefert.
   */
  public Vector<Semesterverband> findAll() {
    Connection con = DBConnection.connection();

    // Ergebnisvektor vorbereiten
    Vector<Semesterverband> result = new Vector<Semesterverband>();

    try {
      Statement stmt = con.createStatement();

      ResultSet rs = stmt.executeQuery("SELECT id, semester, studierendenAnzahl, jahrgang, stundenplanid, studiengangid FROM semesterverband "
          + " ORDER BY id");

      // Für jeden Eintrag im Suchergebnis wird nun ein Semesterverband-Objekt erstellt.
      while (rs.next()) {
        Semesterverband s = new Semesterverband();
        s.setId(rs.getInt("id"));
        s.setSemester(rs.getInt("semester"));
        s.setStudierendenAnzahl(rs.getInt("studierendenAnzahl"));
        s.setJahrgang(rs.getString("jahrgang"));
        s.setStundenplanId(rs.getInt("stundenplanid"));
        s.setStudiengangId(rs.getInt("studiengangid"));

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
   * Einfügen eines <code>Semesterverband</code>-Objekts in die Datenbank. Dabei wird
   * auch der Primärschlüssel des übergebenen Objekts geprüft und ggf.
   * berichtigt.
   * 
   * @param s das zu speichernde Objekt
   * @return das bereits übergebene Objekt, jedoch mit ggf. korrigierter
   *         <code>id</code>.
   */
  public Semesterverband insert(Semesterverband s) {
    Connection con = DBConnection.connection();

    try {
      Statement stmt = con.createStatement();

      /*
       * Zunächst schauen wir nach, welches der momentan höchste
       * Primärschlüsselwert ist.
       */
      ResultSet rs = stmt.executeQuery("SELECT MAX(id) AS maxid "
          + "FROM semesterverband ");

      // Wenn wir etwas zurückerhalten, kann dies nur einzeilig sein
      if (rs.next()) {
        /*
         * s erhält den bisher maximalen, nun um 1 inkrementierten
         * Primärschlüssel.
         */
        s.setId(rs.getInt("maxid") + 1);

        stmt = con.createStatement();

        // Jetzt erst erfolgt die tatsächliche Einfügeoperation
        stmt.executeUpdate("INSERT INTO semesterverband (id, semester, studierendenAnzahl, jahrgang, stundenplanid, studiengangid) " + "VALUES ("
            + s.getId() + "," + s.getSemester() + "," + s.getStudierendenAnzahl() + "," + s.getJahrgang() + "," + s.getStudenplanId() + "," + s.getStudiengangId() );
      }
    }
    catch (SQLException e2) {
      e2.printStackTrace();
    }

    /*
     * Rückgabe, des evtl. korrigierten Semesterverbandes.
     * 
     * HINWEIS: Da in Java nur Referenzen auf Objekte und keine physischen
     * Objekte übergeben werden, wäre die Anpassung des Semesterverband-Objekts auch
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
  public Semesterverband update(Semesterverband s) {
    Connection con = DBConnection.connection();

    try {
      Statement stmt = con.createStatement();

      stmt.executeUpdate("UPDATE semesterverband SET " 
    		  + "semester=\"" + s.getSemester() + "\", " 
    		  + "jahrgang=\"" + s.getJahrgang() + "\", " 
    		  + "studierendenanzahl=\"" + s.getStudierendenAnzahl() + "\", "
    		  + "stundenplanid=\"" +  s.getStudenplanId() + "\", "
    		  + "studiengangid=\"" + s.getStudiengangId() + "\", "
              + "WHERE id=" + s.getId());

    }
    catch (SQLException e2) {
      e2.printStackTrace();
    }

    // Um Analogie zu insert(Semesterverband s) zu wahren, geben wir s zurück
    return s;
  }

  /**
   * Löschen der Daten eines <code>Semesterverband</code>-Objekts aus der Datenbank.
   * 
   * @param s das aus der DB zu löschende "Objekt"
   */
  public void delete(Semesterverband s) {
    Connection con = DBConnection.connection();

    try {
      Statement stmt = con.createStatement();

      stmt.executeUpdate("DELETE FROM semesterverband " + "WHERE id=" + s.getId());

    }
    catch (SQLException e2) {
      e2.printStackTrace();
    }
  }

}

