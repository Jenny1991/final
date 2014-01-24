package de.hdm.stundenplansystem.server.db;

import java.sql.*;
import java.util.Vector;

import de.hdm.stundenplansystem.shared.bo.*;

/**
 * Mapper-Klasse, die <code>Semesterverband</code>-Objekte auf eine relationale
 * Datenbank abbildet. Hierzu wird eine Reihe von Methoden zur VerfÃ¼gung
 * gestellt, mit deren Hilfe z.B. Objekte gesucht, erzeugt, modifiziert und
 * gelÃ¶scht werden kÃ¶nnen. Das Mapping ist bidirektional. D.h., Objekte kÃ¶nnen
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
   * Diese Variable ist durch den Bezeichner <code>static</code> nur einmal fÃ¼r
   * sÃ¤mtliche eventuellen Instanzen dieser Klasse vorhanden. Sie speichert die
   * einzige Instanz dieser Klasse.
   * 
   * @see semesterverbandMapper()
   */
  private static SemesterverbandMapper semesterverbandMapper = null;

  /**
   * GeschÃ¼tzter Konstruktor - verhindert die MÃ¶glichkeit, mit <code>new</code>
   * neue Instanzen dieser Klasse zu erzeugen.
   */
  protected SemesterverbandMapper() {
  }

  /**
   * Diese statische Methode kann aufgrufen werden durch
   * <code>SemesterverbandMapper.semesterverbandMapper()</code>. Sie stellt die
   * Singleton-Eigenschaft sicher, indem Sie dafÃ¼r sorgt, dass nur eine einzige
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
   * wird genau ein Objekt zurÃ¼ckgegeben.
   * 
   * @param id PrimÃ¤rschlÃ¼sselattribut (->DB)
   * @return Semesterverband-Objekt, das dem Ã¼bergebenen SchlÃ¼ssel entspricht, null bei
   *         nicht vorhandenem DB-Tupel.
   */
  public Semesterverband findByKey(int id) {
    // DB-Verbindung holen
    Connection con = DBConnection.connection();

    try {
      // Leeres SQL-Statement (JDBC) anlegen
      Statement stmt = con.createStatement();

      // Statement ausfÃ¼llen und als Query an die DB schicken
      ResultSet rs = stmt.executeQuery("SELECT id, semester, studierendenAnzahl, jahrgang, studiengangid, kuerzel FROM semesterverband "
          + "WHERE id=" + id);

      /*
       * Da id PrimÃ¤rschlÃ¼ssel ist, kann max. nur ein Tupel zurÃ¼ckgegeben
       * werden. PrÃ¼fe, ob ein Ergebnis vorliegt.
       */
      if (rs.next()) {
        // Ergebnis-Tupel in Objekt umwandeln
        Semesterverband s = new Semesterverband();
        s.setId(rs.getInt("id"));
        s.setSemester(rs.getInt("semester"));
        s.setStudierendenAnzahl(rs.getInt("studierendenAnzahl"));
        s.setJahrgang(rs.getString("jahrgang"));
        s.setStudiengangId(rs.getInt("studiengangid"));
        s.setKuerzel(rs.getString("kuerzel"));
        
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
   * Suchen eines Semesterverbandes mit vorgegebener id. Da diese eindeutig ist,
   * wird genau ein Objekt zurÃ¼ckgegeben.
   * 
   * @param id PrimÃ¤rschlÃ¼sselattribut (->DB)
   * @return Semesterverband-Objekt, das dem Ã¼bergebenen SchlÃ¼ssel entspricht, null bei
   *         nicht vorhandenem DB-Tupel.
   */
  public Semesterverband findByStundenplanId(int semesterverbandid) {
    // DB-Verbindung holen
    Connection con = DBConnection.connection();

    try {
      // Leeres SQL-Statement (JDBC) anlegen
      Statement stmt = con.createStatement();

      // Statement ausfÃ¼llen und als Query an die DB schicken
      ResultSet rs = stmt.executeQuery("SELECT semesterverband.id, semesterverband.semester, semesterverband.studierendenAnzahl, semesterverband.jahrgang, semesterverband.studiengangid, semesterverband.kuerzel "
      	  + "FROM semesterverband "
      	  + "INNER JOIN stundenplan "
      	  + "ON stundenplan.semesterverbandid = semesterverband.id "
          + "WHERE semesterverbandid =" + semesterverbandid);

      /*
       * Da id PrimÃ¤rschlÃ¼ssel ist, kann max. nur ein Tupel zurÃ¼ckgegeben
       * werden. PrÃ¼fe, ob ein Ergebnis vorliegt.
       */
      if (rs.next()) {
        // Ergebnis-Tupel in Objekt umwandeln
        Semesterverband s = new Semesterverband();
        s.setId(rs.getInt("semesterverband.id"));
        s.setSemester(rs.getInt("semesterverband.semester"));
        s.setStudierendenAnzahl(rs.getInt("semesterverband.studierendenAnzahl"));
        s.setJahrgang(rs.getString("semesterverband.jahrgang"));
        s.setStudiengangId(rs.getInt("semesterverband.studiengangid"));
        s.setKuerzel(rs.getString("semesterverband.kuerzel"));
        
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
   * Auslesen aller SemesterverbÃ¤nde.
   * 
   * @return Ein Vektor mit Semesterverband-Objekten, die sÃ¤mtliche SemesterverbÃ¤nde
   *         reprÃ¤sentieren. Bei evtl. Exceptions wird ein partiell gefÃ¼llter
   *         oder ggf. auch leerer Vetor zurÃ¼ckgeliefert.
   */
  public Vector<Semesterverband> findAll() {
    Connection con = DBConnection.connection();

    // Ergebnisvektor vorbereiten
    Vector<Semesterverband> result = new Vector<Semesterverband>();

    try {
      Statement stmt = con.createStatement();

      ResultSet rs = stmt.executeQuery("SELECT id, semester, studierendenAnzahl, jahrgang, studiengangid, kuerzel FROM semesterverband "
          + " ORDER BY id");

      // FÃ¼r jeden Eintrag im Suchergebnis wird nun ein Semesterverband-Objekt erstellt.
      while (rs.next()) {
        Semesterverband s = new Semesterverband();
        s.setId(rs.getInt("id"));
        s.setSemester(rs.getInt("semester"));
        s.setStudierendenAnzahl(rs.getInt("studierendenAnzahl"));
        s.setJahrgang(rs.getString("jahrgang"));
        s.setStudiengangId(rs.getInt("studiengangid"));
        s.setKuerzel(rs.getString("kuerzel"));

        // HinzufÃ¼gen des neuen Objekts zum Ergebnisvektor
        result.addElement(s);
      }
    }
    catch (SQLException e2) {
      e2.printStackTrace();
    }

    // Ergebnisvektor zurÃ¼ckgeben
    return result;
  }
 
  
  /**
   * Auslesen aller SemesterverbÃ¤nde.
   * 
   * @return Ein Vektor mit Semesterverband-Objekten, die sÃ¤mtliche SemesterverbÃ¤nde
   *         reprÃ¤sentieren. Bei evtl. Exceptions wird ein partiell gefÃ¼llter
   *         oder ggf. auch leerer Vetor zurÃ¼ckgeliefert.
   */
  public Vector<Semesterverband> findByStudiengangId(int studiengangid) {
    Connection con = DBConnection.connection();

    // Ergebnisvektor vorbereiten
    Vector<Semesterverband> result = new Vector<Semesterverband>();

    try {
      Statement stmt = con.createStatement(); 

      ResultSet rs = stmt.executeQuery("SELECT semesterverband.id, semesterverband.semester, semesterverband.studierendenAnzahl, semesterverband.jahrgang, semesterverband.studiengangid, semesterverband.kuerzel"
      		+ " FROM semesterverband"
            + " WHERE studiengangid = " + studiengangid);

      // FÃ¼r jeden Eintrag im Suchergebnis wird nun ein Semesterverband-Objekt erstellt.
      while (rs.next()) {
        Semesterverband s = new Semesterverband();
        s.setId(rs.getInt("semesterverband.id"));
        s.setSemester(rs.getInt("semesterverband.semester"));
        s.setStudierendenAnzahl(rs.getInt("semesterverband.studierendenAnzahl"));
        s.setJahrgang(rs.getString("semesterverband.jahrgang"));
        s.setStudiengangId(rs.getInt("semesterverband.studiengangid"));
        s.setKuerzel(rs.getString("semesterverband.kuerzel"));

        // HinzufÃ¼gen des neuen Objekts zum Ergebnisvektor
        result.addElement(s);
      }
    }
    catch (SQLException e2) {
      e2.printStackTrace();
    }

    // Ergebnisvektor zurÃ¼ckgeben
    return result;
  }
  
  
  /**
   * EinfÃ¼gen eines <code>Semesterverband</code>-Objekts in die Datenbank. Dabei wird
   * auch der PrimÃ¤rschlÃ¼ssel des Ã¼bergebenen Objekts geprÃ¼ft und ggf.
   * berichtigt.
   * 
   * @param s das zu speichernde Objekt
   * @return das bereits Ã¼bergebene Objekt, jedoch mit ggf. korrigierter
   *         <code>id</code>.
   */
  public Semesterverband insert(Semesterverband s) {
    Connection con = DBConnection.connection();

    try {
      Statement stmt = con.createStatement();

      /*
       * ZunÃ¤chst schauen wir nach, welches der momentan hÃ¶chste
       * PrimÃ¤rschlÃ¼sselwert ist.
       */
      ResultSet rs = stmt.executeQuery("SELECT MAX(id) AS maxid "
          + "FROM semesterverband ");

      // Wenn wir etwas zurÃ¼ckerhalten, kann dies nur einzeilig sein
      if (rs.next()) {
        /*
         * s erhÃ¤lt den bisher maximalen, nun um 1 inkrementierten
         * PrimÃ¤rschlÃ¼ssel.
         */
        s.setId(rs.getInt("maxid") + 1);

        stmt = con.createStatement();

        // Jetzt erst erfolgt die tatsÃ¤chliche EinfÃ¼geoperation
        stmt.executeUpdate("INSERT INTO semesterverband (id, semester, studierendenAnzahl, jahrgang, studiengangid, kuerzel) " + "VALUES ("
            + s.getId() + "," + s.getSemester() + "," + s.getStudierendenAnzahl() + ",'" + s.getJahrgang() + "'," + s.getStudiengangId() + ",'" + s.getKuerzel() + "')");
      }
    }
    catch (SQLException e2) {
      e2.printStackTrace();
    }

    /*
     * RÃ¼ckgabe, des evtl. korrigierten Semesterverbandes.
     * 
     * HINWEIS: Da in Java nur Referenzen auf Objekte und keine physischen
     * Objekte Ã¼bergeben werden, wÃ¤re die Anpassung des Semesterverband-Objekts auch
     * ohne diese explizite RÃ¼ckgabe auÃŸerhalb dieser Methode sichtbar. Die
     * explizite RÃ¼ckgabe von s ist eher ein Stilmittel, um zu signalisieren,
     * dass sich das Objekt evtl. im Laufe der Methode verÃ¤ndert hat.
     */
    return s;
  }

  /**
   * Wiederholtes Schreiben eines Objekts in die Datenbank.
   * 
   * @param s das Objekt, das in die DB geschrieben werden soll
   * @return das als Parameter Ã¼bergebene Objekt
   */
  public Semesterverband update(Semesterverband s) {
    Connection con = DBConnection.connection();

    try {
      Statement stmt = con.createStatement();

      stmt.executeUpdate("UPDATE semesterverband SET " 
    		  + "semester='" + s.getSemester()  + "', "
    		  + "jahrgang='" + s.getJahrgang()  + "', "
    		  + "studierendenanzahl= "+ s.getStudierendenAnzahl()  + ", "
    		  + "studiengangid= " + s.getStudiengangId()  + ", "
    		  + "kuerzel= " + "'" + s.getKuerzel() + "'"
              + " WHERE id=" + s.getId());

    }
    catch (SQLException e2) {
      e2.printStackTrace();
    }

    // Um Analogie zu insert(Semesterverband s) zu wahren, geben wir s zurÃ¼ck
    return s;
  }

  /**
   * LÃ¶schen der Daten eines <code>Semesterverband</code>-Objekts aus der Datenbank.
   * 
   * @param s das aus der DB zu lÃ¶schende "Objekt"
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