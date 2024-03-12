package pgdp.searchengine.networking;

import de.tum.in.test.api.jupiter.HiddenTest;
import de.tum.in.test.api.jupiter.PublicTest;
import org.junit.jupiter.api.DisplayName;
import pgdp.searchengine.W10H01;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static pgdp.searchengine.utilities.Reflections.*;

@W10H01
public class HTMLTokenTests {

    @DisplayName("- | Test Structure of HTMLToken-Class")
    @PublicTest
    void htmlTokenStructuralTests() {
        assertAll(
                () -> assertTrue(newHtmlToken.exists(), "Der für die Klasse 'HTMLToken' geforderte Konstruktor 'HTMLToken(TokenType)' wurde nicht gefunden."),
                () -> assertTrue(tokenType.exists(), "Das für die Klasse 'HTMLToken' geforderte Attribut 'TokenType tokenType' wurde nicht gefunden."),
                () -> assertTrue(content.exists(), "Das für die Klasse 'HTMLToken' geforderte Attribut 'StringBuilder content' wurde nicht gefunden."),
                () -> assertTrue(getTokenType.exists(), "Die für die Klasse 'HTMLToken' geforderte Methode mit Signatur 'TokenType getTokenType()' wurde nicht gefunden."),
                () -> assertTrue(getContentAsString.exists(), "Die für die Klasse 'HTMLToken' geforderte Methode mit Signatur 'String getContentAsString()' wurde nicht gefunden."),
                () -> assertTrue(addCharacter.exists(), "Die für die Klasse 'HTMLToken' geforderte Methode mit Signatur 'void addCharacter(char c)' wurde nicht gefunden."),
                () -> assertTrue(toString.exists(), "Die für die Klasse 'HTMLToken' geforderte Methode mit Signatur 'String toString()' wurde nicht gefunden.")
        );
    }

    // ================================================================================================= //
    // ------------------------ Methods, that test one method in HTMLToken each ------------------------ //
    // ================================================================================================= //

    void testHtmlTokenConstructor(HTMLToken.TokenType inputTokenType) {
        var newHtmlTokenObject = newHtmlToken.newInstance(inputTokenType);

        HTMLToken.TokenType actualTokenType = (HTMLToken.TokenType) tokenType.getOf(newHtmlTokenObject);
        StringBuilder actualContent = (StringBuilder) content.getOf(newHtmlTokenObject);

        assertAll(
                () -> assertEquals(inputTokenType, actualTokenType,
                        "Beim Aufrufen des Konstruktors von 'HTMLToken' mit einem 'TokenType' wird dieser falsch/nicht in das Attribut 'tokenType' übernommen."),
                () -> assertEquals("", actualContent.toString(),
                        "Beim Erzeugen eines neuen 'HTMLToken'-Objektes sollte der 'content' auf das 'StringBuilder'-Äquivalent des leeren Strings gesetzt werden.")
        );
    }

    void testGetTokenType(HTMLToken.TokenType tokenTypeInAttribute) {
        var htmlTokenObject = newHtmlToken.newInstance(HTMLToken.TokenType.TEXT);

        Field tokenTypeField = tokenType.toField();
        tokenTypeField.setAccessible(true);
        try {
            tokenTypeField.set(htmlTokenObject, tokenTypeInAttribute);
        } catch(IllegalAccessException e) {
            e.printStackTrace();
        }
        HTMLToken.TokenType tokenTypeFromGetter = (HTMLToken.TokenType) getTokenType.invokeOn(htmlTokenObject);
        assertEquals(tokenTypeInAttribute, tokenTypeFromGetter, "Der Getter für 'tokenType' in 'HTMLToken' liest das Attribut falsch aus.");
    }

    void testGetContentAsString(StringBuilder contentInAttribute) {
        var htmlTokenObject = newHtmlToken.newInstance(HTMLToken.TokenType.TEXT);

        Field contentField = content.toField();
        contentField.setAccessible(true);
        try {
            contentField.set(htmlTokenObject, new StringBuilder(contentInAttribute));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        String contentStringFromGetter = (String) getContentAsString.invokeOn(htmlTokenObject);
        assertEquals(contentInAttribute.toString(), contentStringFromGetter,
                "Die Methode 'getContentAsString()' gibt nicht den korrekten Inhalt des vorliegenden 'HTMLToken'-Objektes zurück.");
    }

    void testAddCharacter(StringBuilder contentBeforeAdding, char characterToAdd) {
        var htmlTokenObject = newHtmlToken.newInstance(HTMLToken.TokenType.TEXT);

        Field contentField = content.toField();
        contentField.setAccessible(true);
        try {
            contentField.set(htmlTokenObject, new StringBuilder(contentBeforeAdding));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        addCharacter.invokeOn(htmlTokenObject, characterToAdd);
        StringBuilder contentAfterAdding = (StringBuilder) content.getOf(htmlTokenObject);
        assertEquals(contentBeforeAdding.toString() + characterToAdd, contentAfterAdding.toString(),
                "Wenn auf einem 'HTMLToken'-Objekt mit 'content' \"" + contentBeforeAdding + "\" die Methode 'addCharacter' mit '" +
                characterToAdd + "' aufgerufen wird, sollte der 'content' danach \"" + contentBeforeAdding + characterToAdd + "\" sein. " +
                "Er war aber \"" + contentBeforeAdding + "\".");
    }

    void testToString(HTMLToken.TokenType tokenTypeInAttribute, StringBuilder contentInAttribute) {
        var htmlTokenObject = newHtmlToken.newInstance(HTMLToken.TokenType.TEXT);

        Field tokenTypeField = tokenType.toField();
        Field contentField = content.toField();
        tokenTypeField.setAccessible(true);
        contentField.setAccessible(true);
        try {
            tokenTypeField.set(htmlTokenObject, tokenTypeInAttribute);
            contentField.set(htmlTokenObject, new StringBuilder(contentInAttribute));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        String expectedReturnValueFromToString = (tokenTypeInAttribute == HTMLToken.TokenType.TAG ? "Tag: " : "Text: ") + contentInAttribute.toString();
        String actualReturnValueFromToString = (String) toString.invokeOn(htmlTokenObject);
        assertEquals(expectedReturnValueFromToString, actualReturnValueFromToString,
                "Die Methode 'toString()' gibt für ein 'HTMLToken'-Objekt mit 'tokenType' " + tokenTypeInAttribute +
                        " und 'content' \"" + contentInAttribute + "\" nicht den korrekten Wert zurück. " +
                        "Achte auch auf kleine Fehler wie z.B. fehlende Leerzeichen!");
    }

    // ================================================================================================ //
    // ------------------------------- Public Tests for HTMLToken Class ------------------------------- //
    // ================================================================================================ //

    @DisplayName("- | Public Tests for Methods in HTMLToken")
    @PublicTest
    void testHTMLTokenMethodsPublic() {
        assertAll(
                () -> testHtmlTokenConstructor(HTMLToken.TokenType.TAG),
                () -> testGetTokenType(HTMLToken.TokenType.TAG),
                () -> testGetContentAsString(new StringBuilder("html lang=\"en_us\"")),
                () -> testAddCharacter(new StringBuilder("body style=\"background-color:blu"), 'e'),
                () -> testToString(HTMLToken.TokenType.TEXT, new StringBuilder("fghi // \" jklm"))
        );
    }

    // ================================================================================================ //
    // ------------------------------- Hidden Tests for HTMLToken Class ------------------------------- //
    // ================================================================================================ //

    @DisplayName("1P | Hidden Tests for Methods in HTMLToken")
    @HiddenTest
    void testHTMLTokenMethodsHidden() {
        assertAll(
                // Test Constructor
                () -> testHtmlTokenConstructor(HTMLToken.TokenType.TAG),
                () -> testHtmlTokenConstructor(HTMLToken.TokenType.TEXT),

                // Test getTokenType()
                () -> testGetTokenType(HTMLToken.TokenType.TAG),
                () -> testGetTokenType(HTMLToken.TokenType.TEXT),

                // Test getContentAsString()
                () -> testGetContentAsString(new StringBuilder()),
                () -> testGetContentAsString(new StringBuilder("abc")),
                () -> testGetContentAsString(new StringBuilder("@_::c")),
                () -> testGetContentAsString(new StringBuilder("\"'+")),
                () -> testGetContentAsString(new StringBuilder("a \nb \tc>>")),

                // Test addCharacter()
                () -> testAddCharacter(new StringBuilder(), '@'),
                () -> testAddCharacter(new StringBuilder("a style='background-color:re"), 'd'),
                () -> testAddCharacter(new StringBuilder("\" das ist ein text "), '\"'),
                () -> testAddCharacter(new StringBuilder("\" das ist ein text"), ' '),
                () -> testAddCharacter(new StringBuilder("pinguine sind flugunfähige vögel"), '\n'),

                // Test toString()
                () -> testToString(HTMLToken.TokenType.TAG, new StringBuilder()),
                () -> testToString(HTMLToken.TokenType.TEXT, new StringBuilder()),
                () -> testToString(HTMLToken.TokenType.TAG, new StringBuilder("p style=\"color:yellow\"")),
                () -> testToString(HTMLToken.TokenType.TEXT, new StringBuilder("this would make for some hard to read text")),
                () -> testToString(HTMLToken.TokenType.TAG, new StringBuilder("a href='man3.pgdp.sse.in.tum.de/some::path_to*some++lo-cation.html'")),
                () -> testToString(HTMLToken.TokenType.TEXT, new StringBuilder("this would make for some hard to read text")),
                () -> testToString(HTMLToken.TokenType.TAG, new StringBuilder("\" @abc\ndef \"")),
                () -> testToString(HTMLToken.TokenType.TEXT, new StringBuilder("\" @abc\ndef \""))
        );
    }

}
