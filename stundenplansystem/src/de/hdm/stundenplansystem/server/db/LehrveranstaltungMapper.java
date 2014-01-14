package de.hdm.stundenplansystem.server.db;

import java.sql.*;
import java.util.Vector;

import de.hdm.stundenplansystem.shared.bo.*;

/**
 * Mapper-Klasse, die <code>Lehrveranstaltung</code>-Objekte auf eine relationale
 * Datenbank abbildet. Hierzu wird eine Reihe von Methoden zur VerfÃ¼gung
 * gestellt, mit deren Hilfe z.B. Objekte gesucht, erzeugt, modifiziert und
 * gelÃ¶scht werden kÃ¶nnen. Das Mapping ist bidirektional. D.h., Objekte kÃ¶nnen
 * in DB-Strukturen und DB-Strukturen in Objekte umgewandelt werden.
 * 
 * @see DozentMapper, RaumMapper, SemesterverbandMapper, StudiengangMapper, 
 * StundenplaneintragMapper, StundenplanMapper, ZeitslotMapper
 * @author Schmieder, Thies
 */
public class LehrveranstaltungMapper {

  /**
   * Die Klasse LehrveranstaltungMapper wird nur einmal instantiiert. Man spricht hierbei
   * von einem sogenannten <b>Singleton</b>.
   * <p>
   * Diese Variable ist durch den Bezeichner <code>static</code> nur einmal fÃ¼r
   * sÃ¤mtliche eventuellen Instanzen dieser Klasse vorhanden. Sie speichert die
   * einzige Instanz dieser Klasse.
   * 
   * @see lehrveranstaltungMapper()
   */
  private static LehrveranstaltungMapper lehrveranstaltungMapper = null;

  /**
   * GeschÃ¼tzter Konstruktor - verhindert die MÃ¶glichkeit, mit <code>new</code>
   * neue Instanzen dieser Klasse zu erzeugen.
   */
  protected LehrveranstaltungMapper() {
  }

  /**
   * Diese statische Methode kann aufgrufen werden durch
   * <code>LehrveranstaltungMapper.lehrveranstaltungMapper()</code>. Sie stellt die
   * Singleton-Eigenschaft sicher, indem Sie dafÃ¼r sorgt, dass nur eine einzige
   * Instanz von <code>LehrveranstaltungMapper</code> existiert.
   * <p>
   * 
   * <b>Fazit:</b> LehrveranstaltungMapper sollte nicht mittels <code>new</code>
   * instantiiert werden, sondern stets durch Aufruf dieser statischen Methode.
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
   * Suchen einer Lehrveranstaltung mit vorgegebener id. Da diese eindeutig ist,
   * wird genau ein Objekt zurÃ¼ckgegeben.
   * 
   * @param id PrimÃ¤rschlÃ¼sselattribut (->DB)
   * @return Lehrveranstaltung-Objekt, das dem Ã¼bergebenen SchlÃ¼ssel entspricht, null bei
   *         nicht vorhandenem DB-Tupel.
   */
  public Lehrveranstaltung findByKey(int id) {
    // DB-Verbindung holen
    Connection con = DBConnection.connection();

    try {
      // Leeres SQL-Statement (JDBC) anlegen
      Statement stmt = con.createStatement();

      // Statement ausfÃ¼llen und als Query an die DB schicken
      ResultSet rs = stmt.executeQuery("SELECT id, bezeichnung, semester, umfang FROM Lehrveranstaltung "
          + "WHERE id=" + id);

      /*
       * Da id PrimÃ¤rschlÃ¼ssel ist, kann max. nur ein Tupel zurÃ¼ckgegeben
       * werden. PrÃ¼fe, ob ein Ergebnis vorliegt.
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
    }
    catch (SQLException e2) {
      e2.printStackTrace();
      return null;
    }

    return null;
  }

  /**
   * Auslesen aller Lehrveranstaltungen.
   * 
   * @return Ein Vektor mit Lehrveranstaltung-Objekten, die sÃ¤mtliche Lehrveranstaltungen
   *         reprÃ¤sentieren. Bei evtl. Exceptions wird ein partiell gefÃ¼llter
   *         oder ggf. auch leerer Vetor zurÃ¼ckgeliefert.
   */
  public Vector<Lehrveranstaltung> findAll() {
    Connection con = DBConnection.connection();

    // Ergebnisvektor vorbereiten
    Vector<Lehrveranstaltung> result = new Vector<Lehrveranstaltung>();

    try {
      Statement stmt = con.createStatement();

      ResultSet rs = stmt.executeQuery("SELECT id, bezeichnung, semester, umfang FROM lehrveranstaltung "
          + " ORDER BY id");

      // FÃ¼r jeden Eintrag im Suchergebnis wird nun ein Lehrveranstaltung-Objekt erstellt.
      while (rs.next()) {
        Lehrveranstaltung l = new Lehrveranstaltung();
        l.setId(rs.getInt("id"));
        l.setBezeichnung(rs.getString("bezeichnung"));
        l.setSemester(rs.getInt("semester"));
        l.setUmfang(rs.getInt("umfang"));

        // HinzufÃ¼gen des neuen Objekts zum Ergebnisvektor
        result.addElement(l);
      }
    }
    catch (SQLException e2) {
      e2.printStackTrace();
    }

    // Ergebnisvektor zurÃ¼ckgeben
    return result;
  }

  
  /**
   * EinfÃ¼gen eines <code>Lehrveranstaltung</code>-Objekts in die Datenbank. Dabei wird
   * auch der PrimÃ¤rschlÃ¼ssel des Ã¼bergebenen Objekts geprÃ¼ft und ggf.
   * berichtigt.
   * 
   * @param l das zu speichernde Objekt
   * @return das bereits Ã¼bergebene Objekt, jedoch mit ggf. korrigierter
   *         <code>id</code>.
   */
  public Lehrveranstaltung insert(Lehrveranstaltung l) {
    Connection con = DBConnection.connection();

    try {
      Statement stmt = con.createStatement();

      /*
       * ZunÃ¤chst schauen wir nach, welches der momentan hÃ¶chste
       * PrimÃ¤rschlÃ¼sselwert ist.
       */
      ResultSet rs = stmt.executeQuery("SELECT MAX(id) AS maxid "
          + "FROM lehrveranstaltung ");

      // Wenn wir etwas zurÃ¼ckerhalten, kann dies nur einzeilig sein
      if (rs.next()) {
        /*
         * l erhÃ¤lt den bisher maximalen, nun um 1 inkrementierten
         * PrimÃ¤rschlÃ¼ssel.
         */
        l.setId(rs.getInt("maxid") + 1);

        stmt = con.createStatement();

        // Jetzt erst erfolgt die tatsÃ¤chliche EinfÃ¼geoperation
        stmt.executeUpdate("INSERT INTO lehrveranstaltung (id, bezeichnung, semester, umfang) " + "VALUES ("
            + l.getId() + "," + l.getBezeichnung() + "," + l.getSemester() + "," + l.getUmfang() );
      }
    }
    catch (SQLException e2) {
      e2.printStackTrace();
    }

    /*
     * RÃ¼ckgabe, der evtl. korrigierten Lehrveranstaltung.
     * 
     * HINWEIS: Da in Java nur Referenzen auf Objekte und keine physischen
     * Objekte Ã¼bergeben werden, wÃ¤re die Anpassung des Lehrveranstaltung-Objekts auch
     * ohne diese explizite RÃ¼ckgabe auï¿½erhalb dieser Methode sichtbar. Die
     * explizite RÃ¼ckgabe von l ist eher ein Stilmittel, um zu signalisieren,
     * dass sich das Objekt evtl. im Laufe der Methode verÃ¤ndert hat.
     */
    return l;
  }

  /**
   * Wiederholtes Schreiben eines Objekts in die Datenbank.
   * 
   * @param l das Objekt, das in die DB geschrieben werden soll
   * @return das als Parameter Ã¼bergebene Objekt
   */
  public Lehrveranstaltung update(Lehrveranstaltung l) {
    Connection con = DBConnection.connection();

    try {
      Statement stmt = con.createStatement();

      stmt.executeUpdate("UPDATE stundenplaneintrag SET " 
    		  + "umfang=\"" + l.getUmfang() + "\", " 
    		  + "semester=\"" + l.getSemester() + "\", " 
    		  + "bezeichnung=\"" + l.getBezeichnung() + "\", "
              + "WHERE id=" + l.getId());

    }
    catch (SQLException e2) {
      e2.printStackTrace();
    }

    // Um Analogie zu insert(Lehrveranstaltung l) zu wahren, geben wir l zurÃ¼ck
    return l;
  }

  /**
   * LÃ¶schen der Daten eines <code>Lehrveranstaltung</code>-Objekts aus der Datenbank.
   * 
   * @param l das aus der DB zu lÃ¶schende "Objekt"
   */
  public void delete(Lehrveranstaltung l) {
    Connection con = DBConnection.connection();

    try {
      Statement stmt = con.createStatement();

      stmt.executeUpdate("DELETE FROM lehrveranstaltung " + "WHERE id=" + l.getId());

    }
    catch (SQLException e2) {
      e2.printStackTrace();
    }
  }

}
