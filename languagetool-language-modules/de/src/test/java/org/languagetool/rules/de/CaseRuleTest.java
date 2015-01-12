/* LanguageTool, a natural language style checker 
 * Copyright (C) 2005 Daniel Naber (http://www.danielnaber.de)
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301
 * USA
 */
package org.languagetool.rules.de;

import java.io.IOException;

import junit.framework.TestCase;

import org.languagetool.AnalyzedSentence;
import org.languagetool.JLanguageTool;
import org.languagetool.language.German;

/**
 * @author Daniel Naber
 */
public class CaseRuleTest extends TestCase {

  private CaseRule rule;
  private JLanguageTool langTool;

  @Override
  public void setUp() throws IOException {
    rule = new CaseRule(null, new German());
    langTool = new JLanguageTool(new German());
  }

  public void testRuleActivation() throws IOException {
    CaseRule rule = new CaseRule(null, new German());
    assertTrue(rule.supportsLanguage(new German()));
  }

  public void testRule() throws IOException {

    // correct sentences:
    assertGood("Dem Hund Futter geben");
    assertGood("Heute spricht Frau Stieg.");
    assertGood("Ein einfacher Satz zum Testen.");
    assertGood("Das Laufen fällt mir leicht.");
    assertGood("Das Winseln stört.");
    assertGood("Das schlägt nicht so zu Buche.");
    assertGood("Dirk Hetzel ist ein Name.");
    assertGood("Sein Verhalten war okay.");
    assertGood("Hier ein Satz. \"Ein Zitat.\"");
    assertGood("Hier ein Satz. 'Ein Zitat.'");
    assertGood("Hier ein Satz. «Ein Zitat.»");
    assertGood("Hier ein Satz. »Ein Zitat.«");
    assertGood("Hier ein Satz. (Noch einer.)");
    assertGood("Hier geht es nach Tel Aviv.");
    assertGood("Unser Jüngster ist da.");
    assertGood("Alles Erfundene ist wahr.");
    assertGood("Sie hat immer ihr Bestes getan.");
    assertGood("Er wird etwas Verrücktes träumen.");
    assertGood("Er wird etwas schön Verrücktes träumen.");
    assertGood("Er wird etwas so Verrücktes träumen.");
    assertGood("Tom ist etwas über dreißig.");
    assertGood("Diese Angriffe bleiben im Verborgenen.");
    assertGood("Ihr sollt mich das wissen lassen.");
    assertGood("Wenn er mich das rechtzeitig wissen lässt, gerne.");
    assertBad("Tom ist etwas über Dreißig.");
    // "NIL" reading in Morphy that used to confuse CaseRule:
    assertGood("Ein Menschenfreund.");
    // works only thanks to addex.txt:
    assertGood("Der Nachfahre.");
    // both can be correct:
    assertGood("Hier ein Satz, \"Ein Zitat.\"");
    assertGood("Hier ein Satz, \"ein Zitat.\"");
    // Exception 'Le':
    assertGood("Schon Le Monde schrieb das.");
    // unknown word:
    assertGood("In Blubberdorf macht man das so.");
    
    assertGood("Sie werden im Allgemeinen gefasst.");
    assertGood("Sie werden im allgemeinen Fall gefasst.");
    //assertBad("Sie werden im allgemeinen gefasst.");
    assertBad("Sie werden im Allgemeinen Fall gefasst.");

    // sentences that used to trigger an error because of incorrect compound tokenization:
    assertGood("Das sind Euroscheine.");
    assertGood("John Stallman isst.");
    assertGood("Das ist die neue Gesellschafterin hier.");
    assertGood("Das ist die neue Dienerin hier.");
    assertGood("Das ist die neue Geigerin hier.");
    assertGood("Die ersten Gespanne erreichen Köln.");
    assertGood("Er beschrieb den Angeklagten wie einen Schuldigen");
    assertGood("Er beschrieb den Angeklagten wie einen Schuldigen.");

    assertGood("Das ist das Dümmste, was ich je gesagt habe.");
    assertBad("Das ist das Dümmste Kind.");
    
    assertGood("Man sagt, Liebe mache blind.");
    assertGood("Die Deutschen sind sehr listig.");
    assertGood("Der Lesestoff bestimmt die Leseweise.");
    assertGood("Ich habe nicht viel von einem Reisenden.");
    assertGood("Die Vereinigten Staaten");
    //TODO:
    //assertGood("Der Satz vom ausgeschlossenen Dritten.");
    assertGood("Die Ausgewählten werden gut betreut.");
    assertGood("Die ausgewählten Leute werden gut betreut.");
    //assertBad("Die ausgewählten werden gut betreut.");
    assertBad("Die Ausgewählten Leute werden gut betreut.");

    // used to trigger error because of wrong POS tagging:
    assertGood("Die Schlinge zieht sich zu.");
    assertGood("Die Schlingen ziehen sich zu.");
    
    // used to trigger error because of "abbreviation"
    assertGood("Sie fällt auf durch ihre hilfsbereite Art. Zudem zeigt sie soziale Kompetenz.");
    
    // TODO: nach dem Doppelpunkt wird derzeit nicht auf groß/klein getestet:
    assertGood("Das ist es: kein Satz.");
    assertGood("Das ist es: Kein Satz.");

    assertGood("Das wirklich Wichtige ist dies:");
    assertGood("Das wirklich wichtige Verfahren ist dies:");
    //assertBad("Das wirklich wichtige ist dies:");
    assertBad("Das wirklich Wichtige Verfahren ist dies:");

    // incorrect sentences:
    assertBad("Die Schöne Tür");
    assertBad("Das Blaue Auto.");
    //assertBad("Der Grüne Baum.");
    assertBad("Ein Einfacher Satz zum Testen.");
    assertBad("Das Winseln Stört.");
    assertBad("Sein verhalten war okay.");
    assertEquals(1, langTool.check("Karten werden vom Auswahlstapel gezogen. Auch […] Der Auswahlstapel gehört zum Inhalt.").size());
    //assertEquals(2, langTool.check("Karten werden vom Auswahlstapel gezogen. Auch [...] Der Auswahlstapel gehört zum Inhalt.").size());

    assertEquals(0, langTool.check("Karten werden vom Auswahlstapel gezogen. […] Der Auswahlstapel gehört zum Inhalt.").size());
    //assertEquals(1, langTool.check("Karten werden vom Auswahlstapel gezogen. [...] Der Auswahlstapel gehört zum Inhalt.").size());
    //TODO: error not found:
    //assertBad("So schwer, dass selbst Er ihn nicht hochheben kann.");

    assertGood("Im Norwegischen klingt das schöner.");
    assertGood("Übersetzt aus dem Norwegischen von Ingenieur Frederik Dingsbums.");
    assertGood("Dem norwegischen Ingenieur gelingt das gut.");
    assertBad("Dem Norwegischen Ingenieur gelingt das gut.");
    assertGood("Peter Peterson, dessen Namen auf Griechisch Stein bedeutet.");
    assertGood("Peter Peterson, dessen Namen auf Griechisch gut klingt.");
    //assertBad("Peter Peterson, dessen Namen auf griechisch Stein bedeutet.");
  }

  private void assertGood(String input) throws IOException {
    assertEquals("Did not expect error in: '" + input + "'", 0, rule.match(langTool.getAnalyzedSentence(input)).length);
  }

  private void assertBad(String input) throws IOException {
    assertEquals("Did not find expected error in: '" + input + "'", 1, rule.match(langTool.getAnalyzedSentence(input)).length);
  }

  public void testSubstantivierteVerben() throws IOException {
    // correct sentences:
    assertGood("Das fahrende Auto.");
    assertGood("Das können wir so machen.");
    assertGood("Denn das Fahren ist einfach.");
    assertGood("Das Fahren ist einfach.");
    assertGood("Das Gehen fällt mir leicht.");
    assertGood("Das Ernten der Kartoffeln ist mühsam.");
    assertGood("Entschuldige das späte Weiterleiten.");
    assertGood("Ich liebe das Lesen.");
    assertGood("Das Betreten des Rasens ist verboten.");
    assertGood("Das haben wir aus eigenem Antrieb getan.");
    assertGood("Das haben wir.");
    assertGood("Das haben wir schon.");
    assertGood("Das lesen sie doch sicher in einer Minute durch.");
    assertGood("Das lesen Sie doch sicher in einer Minute durch!");
    assertGood("Formationswasser, das oxidiert war.");

    // Source of the following examples: http://www.canoo.net/services/GermanSpelling/Amtlich/GrossKlein/pgf57-58.html
    assertGood("Das Lesen fällt mir schwer.");
    assertGood("Sie hörten ein starkes Klopfen.");
    assertGood("Wer erledigt das Fensterputzen?");
    assertGood("Viele waren am Zustandekommen des Vertrages beteiligt.");
    assertGood("Die Sache kam ins Stocken.");
    assertGood("Das ist zum Lachen.");
    assertGood("Euer Fernbleiben fiel uns auf.");
    assertGood("Uns half nur noch lautes Rufen.");
    assertGood("Die Mitbewohner begnügten sich mit Wegsehen und Schweigen.");
    assertGood("Sie wollte auf Biegen und Brechen gewinnen.");
    assertGood("Er klopfte mit Zittern und Zagen an.");
    assertGood("Ich nehme die Tabletten auf Anraten meiner Ärztin.");
    assertGood("Sie hat ihr Soll erfüllt.");
    assertGood("Dies ist ein absolutes Muss.");
    assertGood("Das Lesen fällt mir schwer.");

    // incorrect sentences:
    assertBad("Das fahren ist einfach.");
    assertBad("Denn das fahren ist einfach.");
    assertBad("Denn das laufen ist einfach.");
    assertBad("Denn das essen ist einfach.");
    assertBad("Denn das gehen ist einfach.");
    assertBad("Das Große Auto wurde gewaschen.");
    assertBad("Ich habe ein Neues Fahrrad.");
    // TODO: detect all the cases not preceded with 'das'
  }

  public void testPhraseExceptions() throws IOException {
    // correct sentences:
    assertGood("Das gilt ohne Wenn und Aber.");
    assertGood("ohne Wenn und Aber");
    assertGood("Das gilt ohne Wenn und Aber bla blubb.");
    // as long as phrase exception isn't complete, there's no error:
    assertGood("Das gilt ohne wenn");
    assertGood("Das gilt ohne wenn und");
    assertGood("wenn und aber");
    assertGood("und aber");
    assertGood("aber");
    // incorrect sentences:
    // error not found here as it's in the XML rules:
    //assertBad("Das gilt ohne wenn und aber.");
  }
  
  public void testCompareLists() throws IOException {
    AnalyzedSentence sentence1 = langTool.getAnalyzedSentence("Hier ein Test");
    assertTrue(rule.compareLists(sentence1.getTokensWithoutWhitespace(), 0, 2, new String[]{"", "Hier", "ein"}));
    assertTrue(rule.compareLists(sentence1.getTokensWithoutWhitespace(), 1, 2, new String[]{"Hier", "ein"}));
    assertTrue(rule.compareLists(sentence1.getTokensWithoutWhitespace(), 0, 3, new String[]{"", "Hier", "ein", "Test"}));
    assertFalse(rule.compareLists(sentence1.getTokensWithoutWhitespace(), 0, 4, new String[]{"", "Hier", "ein", "Test"}));

    AnalyzedSentence sentence2 = langTool.getAnalyzedSentence("das Heilige Römische Reich");
    assertTrue(rule.compareLists(sentence2.getTokensWithoutWhitespace(), 0, 4, new String[]{"", "das", "Heilige", "Römische", "Reich"}));
    assertFalse(rule.compareLists(sentence2.getTokensWithoutWhitespace(), 8, 11, new String[]{"", "das", "Heilige", "Römische", "Reich"}));
  }
}
