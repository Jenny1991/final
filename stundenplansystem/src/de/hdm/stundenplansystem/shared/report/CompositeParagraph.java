package de.hdm.stundenplansystem.shared.report;

import java.io.Serializable;
import java.util.Vector;

/**
 * Diese Klasse stellt eine Menge einzelner AbsÃ¤tze (
 * <code>SimpleParagraph</code>-Objekte) dar. Diese werden als Unterabschnitte
 * in einem <code>Vector</code> abgelegt verwaltet.
 * 
 * @author Thies
 */
public class CompositeParagraph extends Paragraph implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  /**
   * Speicherort der Unterabschnitte.
   */
  private Vector<SimpleParagraph> subParagraphs = new Vector<SimpleParagraph>();

  /**
   * Einen Unterabschnitt hinzufÃ¼gen.
   * 
   * @param p der hinzuzufÃ¼gende Unterabschnitt.
   */
  public void addSubParagraph(SimpleParagraph p) {
    this.subParagraphs.addElement(p);
  }

  /**
   * Einen Unterabschnitt entfernen.
   * 
   * @param p der zu entfernende Unterabschnitt.
   */
  public void removeSubParagraph(SimpleParagraph p) {
    this.subParagraphs.removeElement(p);
  }

  /**
   * Auslesen sÃ¤mtlicher Unterabschnitte.
   * 
   * @return <code>Vector</code>, der sÃ¤mtliche Unterabschnitte enthÃ¤lt.
   */
  public Vector<SimpleParagraph> getSubParagraphs() {
    return this.subParagraphs;
  }

  /**
   * Auslesen der Anzahl der Unterabschnitte.
   * 
   * @return Anzahl der Unterabschnitte
   */
  public int getNumParagraphs() {
    return this.subParagraphs.size();
  }

  /**
   * Auslesen eines einzelnen Unterabschnitts.
   * 
   * @param i der Index des gewÃ¼nschten Unterabschnitts (0 <= i <n), mit n =
   *          Anzahl der Unterabschnitte.
   * 
   * @return der gewÃ¼nschte Unterabschnitt.
   */
  public SimpleParagraph getParagraphAt(int i) {
    return this.subParagraphs.elementAt(i);
  }

  /**
   * Umwandeln eines <code>CompositeParagraph</code> in einen
   * <code>String</code>.
   */
  public String toString() {
    /*
     * Wir legen einen leeren Buffer an, in den wir sukzessive sÃ¤mtliche
     * String-ReprÃ¤sentationen der Unterabschnitte eintragen.
     */
    StringBuffer result = new StringBuffer();

    // Schleife Ã¼ber alle Unterabschnitte
    for (int i = 0; i < this.subParagraphs.size(); i++) {
      SimpleParagraph p = this.subParagraphs.elementAt(i);

      /*
       * den jew. Unterabschnitt in einen String wandeln und an den Buffer hÃ¤ngen.
       */
      result.append(p.toString() + "\n");
    }

    /*
     * SchlieÃŸlich wird der Buffer in einen String umgewandelt und
     * zurÃ¼ckgegeben.
     */
    return result.toString();
  }
}
