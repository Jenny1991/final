package de.hdm.stundenplansystem.server.db;

import java.sql.*;
import java.util.Vector;

import de.hdm.stundenplansystem.shared.bo.*;

/**
 * Mapper-Klasse, die <code>Raum</code>-Objekte auf eine relationale
 * Datenbank abbildet. Hierzu wird eine Reihe von Methoden zur VerfÃ¼gung
 * gestellt, mit deren Hilfe z.B. Objekte gesucht, erzeugt, modifiziert und
 * gelÃ¶scht werden kÃ¶nnen. Das Mapping ist bidirektional. D.h., Objekte kÃ¶nnen
 * in DB-Strukturen und DB-Strukturen in Objekte umgewandelt werden.
 * 
 * @see DozentMapper, LehrveranstaltungMapper, SemesterverbandMapper,
 * StudiengangMapper, StundenplaneintragMapper, StundenplanMapper, ZeitslotMapper
 * @author Schmieder, Thies
 */
public class RaumMapper {

  /**
   * Die Klasse RaumMapper wird nur einmal instantiiert. Man spricht hierbei
   * von einem sogenannten <b>Singleton</b>.
   * <p>
   * Diese Variable ist durch den Bezeichner <code>static</code> nur einmal fÃ¼r
   * sÃ¤mtliche eventuellen Instanzen dieser Klasse vorhanden. Sie speichert die
   * einzige Instanz dieser Klasse.
   * 
   * @see raumMapper()
   */
  private static RaumMapper raumMapper = null;

  /**
   * GeschÃ¼tzter Konstruktor - verhindert die MÃ¶glichkeit, mit <code>new</code>
   * neue Instanzen dieser Klasse zu erzeugen.
   */
  protected RaumMapper() {
  }

  /**
   * Diese statische Methode kann aufgrufen werden durch
   * <code>RaumMapper.raumMapper()</code>. Sie stellt die
   * Singleton-Eigenschaft sicher, indem Sie dafÃ¼r sorgt, dass nur eine einzige
   * Instanz von <code>RaumMapper</code> existiert.
   * <p>
   * 
   * <b>Fazit:</b> RaumMapper sollte nicht mittels <code>new</code>
   * instantiiert werden, sondern stets durch Aufruf dieser statischen Methode.
   * 
   * @return DAS <code>RaumMapper</code>-Objekt.
   * @see raumMapper
   */
  public static RaumMapper raumMapper() {
    if (raumMapper == null) {
      raumMapper = new RaumMapper();
    }

    return raumMapper;
  }

  /**
   * Suchen eines Raumes mit vorgegebener id. Da diese eindeutig ist,
   * wird genau ein Objekt zurÃ¼ckgegeben.
   * 
   * @param id PrimÃ¤rschlÃ¼sselattribut (->DB)
   * @return Raum-Objekt, das dem Ã¼bergebenen SchlÃ¼ssel entspricht, null bei
   *         nicht vorhandenem DB-Tupel.
   */
  public Raum findByKey(int id) {
    // DB-Verbindung holen
    Connection con = DBConnection.connection();

    try {
      // Leeres SQL-Statement (JDBC) anlegen
      Statement stmt = con.createStatement();

      // Statement ausfÃ¼llen und als Query an die DB schicken
      ResultSet rs = stmt.executeQuery("SELECT id, kapazitaet, bezeichnung FROM Raum "
          + "WHERE id=" + id);

      /*
       * Da id PrimÃ¤rschlÃ¼ssel ist, kann max. nur ein Tupel zurÃ¼ckgegeben
       * werden. PrÃ¼fe, ob ein Ergebnis vorliegt.
       */
      if (rs.next()) {
        // Ergebnis-Tupel in Objekt umwandeln
        Raum r = new Raum();
        r.setId(rs.getInt("id"));
        r.setBezeichnung(rs.getString("bezeichnung"));
        r.setKapazitaet(rs.getInt("kapazitaet"));
        
        return r;
      }
    }
    catch (SQLException e2) {
      e2.printStackTrace();
      return null;
    }

    return null;
  }

  /**
   * Auslesen aller RÃ¤ume.
   * 
   * @return Ein Vektor mit Raum-Objekten, die sÃ¤mtliche RÃ¤ume
   *         reprÃ¤sentieren. Bei evtl. Exceptions wird ein partiell gefÃ¼llter
   *         oder ggf. auch leerer Vetor zurÃ¼ckgeliefert.
   */
  public Vector<Raum> findAll() {
    Connection con = DBConnection.connection();

    // Ergebnisvektor vorbereiten
    Vector<Raum> result = new Vector<Raum>();

    try {
      Statement stmt = con.createStatement();

      ResultSet rs = stmt.executeQuery("SELECT id, bezeichnung, kapazitaet FROM raum "
          + " ORDER BY id");

      // FÃ¼r jeden Eintrag im Suchergebnis wird nun ein Raum-Objekt erstellt.
      while (rs.next()) {
        Raum r = new Raum();
        r.setId(rs.getInt("id"));
        r.setBezeichnung(rs.getString("bezeichnung"));
        r.setKapazitaet(rs.getInt("kapazitaet"));
        

        // HinzufÃ¼gen des neuen Objekts zum Ergebnisvektor
        result.addElement(r);
      }
    }
    catch (SQLException e2) {
      e2.printStackTrace();
    }

    // Ergebnisvektor zurÃ¼ckgeben
    return result;
  }

  
  /**
   * EinfÃ¼gen eines <code>Raum</code>-Objekts in die Datenbank. Dabei wird
   * auch der PrimÃ¤rschlÃ¼ssel des Ã¼bergebenen Objekts geprÃ¼ft und ggf.
   * berichtigt.
   * 
   * @param r das zu speichernde Objekt
   * @return das bereits Ã¼bergebene Objekt, jedoch mit ggf. korrigierter
   *         <code>id</code>.
   */
  public Raum insert(Raum r) {
    Connection con = DBConnection.connection();

    try {
      Statement stmt = con.createStatement();

      /*
       * ZunÃ¤chst schauen wir nach, welches der momentan hÃ¶chste
       * PrimÃ¤rschlÃ¼sselwert ist.
       */
      ResultSet rs = stmt.executeQuery("SELECT MAX(id) AS maxid "
          + "FROM raum ");

      // Wenn wir etwas zurÃ¼ckerhalten, kann dies nur einzeilig sein
      if (rs.next()) {
        /*
         * r erhÃ¤lt den bisher maximalen, nun um 1 inkrementierten
         * PrimÃ¤rschlÃ¼ssel.
         */
        r.setId(rs.getInt("maxid") + 1);

        stmt = con.createStatement();

        // Jetzt erst erfolgt die tatsÃ¤chliche EinfÃ¼geoperation
        stmt.executeUpdate("INSERT INTO raum (id, bezeichnung, kapazitaet) " + "VALUES ("
            + r.getId() + "," + r.getBezeichnung() + "," + r.getKapazitaet() );
      }
    }
    catch (SQLException e2) {
      e2.printStackTrace();
    }

    /*
     * RÃ¼ckgabe, des evtl. korrigierten Raumes.
     * 
     * HINWEIS: Da in Java nur Referenzen auf Objekte und keine physischen
     * Objekte Ã¼bergeben werden, wÃ¤re die Anpassung des Raum-Objekts auch
     * ohne diese explizite RÃ¼ckgabe auÃŸerhalb dieser Methode sichtbar. Die
     * explizite RÃ¼ckgabe von r ist eher ein Stilmittel, um zu signalisieren,
     * dass sich das Objekt evtl. im Laufe der Methode verÃ¤ndert hat.
     */
    return r;
  }

  /**
   * Wiederholtes Schreiben eines Objekts in die Datenbank.
   * 
   * @param r das Objekt, das in die DB geschrieben werden soll
   * @return das als Parameter Ã¼bergebene Objekt
   */
  public Raum update(Raum r) {
    Connection con = DBConnection.connection();

    try {
      Statement stmt = con.createStatement();

      stmt.executeUpdate("UPDATE raum SET " 
    		  + "bezeichnung=\"" + r.getBezeichnung() + "\", " 
    		  + "kapazitaet=\"" + r.getKapazitaet() + "\", " 
    		  + "WHERE id=" + r.getId());

    }
    catch (SQLException e2) {
      e2.printStackTrace();
    }

    // Um Analogie zu insert(Raum r) zu wahren, geben wir r zurÃ¼ck
    return r;
  }

  /**
   * LÃ¶schen der Daten eines <code>Raum</code>-Objekts aus der Datenbank.
   * 
   * @param r das aus der DB zu lÃ¶schende "Objekt"
   */
  public void delete(Raum r) {
    Connection con = DBConnection.connection();

    try {
      Statement stmt = con.createStatement();

      stmt.executeUpdate("DELETE FROM raum " + "WHERE id=" + r.getId());

    }
    catch (SQLException e2) {
      e2.printStackTrace();
    }
  }

}
