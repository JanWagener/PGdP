package pgdp.searchengine.networking;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static pgdp.searchengine.utilities.Reflections.TOKEN_TYPE_TAG;
import static pgdp.searchengine.utilities.Reflections.TOKEN_TYPE_TEXT;
import static pgdp.searchengine.utilities.Reflections.filterLinks;
import static pgdp.searchengine.utilities.Reflections.filterText;
import static pgdp.searchengine.utilities.Reflections.filterTitle;
import static pgdp.searchengine.utilities.Reflections.newHtmlToken;
import static pgdp.searchengine.utilities.Reflections.tokenize;
import static pgdp.searchengine.utilities.TestingUtilities.stripToBareMinimum;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;

import de.tum.in.test.api.jupiter.HiddenTest;
import de.tum.in.test.api.jupiter.PublicTest;
import pgdp.searchengine.W10H01;

@W10H01
public class HTMLProcessingTests {

    @DisplayName("- | Test Structure of HTMLProcessing")
    @PublicTest
    void htmlProcessingStructuralTests() {
        assertAll(() -> assertTrue(tokenize.exists(),
                "Die für die Klasse 'HTMLProcessing' geforderte Methode mit Signatur 'List<HTMLToken> tokenize(String)' wurde nicht gefunden."),
                () -> assertTrue(filterLinks.exists(),
                        "Die für die Klasse 'HTMLProcessing' geforderte Methode mit Signatur 'String[] filterLinks(List<HTMLToken>, String)' wurde nicht gefunden."),
                () -> assertTrue(filterText.exists(),
                        "Die für die Klasse 'HTMLProcessing' geforderte Methode mit Signatur 'String filterText(List<HTMLToken>)' wurde nicht gefunden."),
                () -> assertTrue(filterTitle.exists(),
                        "Die für die Klasse 'HTMLProcessing' geforderte Methode mit Signatur 'String filterTitle(List<HTMLToken>)' wurde nicht gefunden."));
    }

    // ================================================================================================ //
    //                                                                                                  //
    // --------------------- Methods, that test one method in HTMLProcessing each --------------------- //
    //                                                                                                  //
    // ================================================================================================ //

    void testTokenize(String rawHTML, List<HTMLToken> expectedTokenList) {
        List<HTMLToken> tokenListFromTokenize = (List<HTMLToken>) tokenize.invokeOn(null, rawHTML);

        boolean listsEqual = tokenListFromTokenize.size() == expectedTokenList.size();
        int i = 0;
        for (i = 0; listsEqual && i < tokenListFromTokenize.size(); i++) {
            listsEqual &= tokensEqual(tokenListFromTokenize.get(i), expectedTokenList.get(i));
        }

        if (!listsEqual) {
            fail("Die Methode 'HTMLProcessing.tokenize()' liefert für den HTML-Code \n" + rawHTML
                    + "\n ein falsches Ergebnis.\n" + "Erwartet wurde:\n" + tokenListToString(expectedTokenList) + "\n"
                    + "Geliefert wurde:\n" + tokenListToString(tokenListFromTokenize));
        }
    }

    void testFilterLinks(List<HTMLToken> tokens, String host, String[] expectedLinks) {
        String[] linksFromFilterLinks = (String[]) filterLinks.invokeOn(null, tokens, host);

        if (!Arrays.equals(expectedLinks, linksFromFilterLinks)) {
            fail("Die Methode 'HTMLProcessing.filterLinks()' liefert für die Token-Liste\n" + tokenListToString(tokens)
                    + "\nund den Host \"" + host + "\" " + "\nnicht die erwartete Sequenz an Links.\nErwartet wurde:\n"
                    + Arrays.toString(expectedLinks) + "\nGeliefert wurde:\n" + Arrays.toString(linksFromFilterLinks));
        }
    }

    void testFilterText(List<HTMLToken> tokens, String expectedText) {
        String textFromFilterText = (String) filterText.invokeOn(null, tokens);

        assertEquals(stripToBareMinimum(expectedText), stripToBareMinimum(textFromFilterText),
                "Die Methode 'HTMLProcessing.filterText()' liefert für die Token-Liste\n" + tokenListToString(tokens)
                        + "\nein falsches Ergebnis.");
    }

    void testFilterTitle(List<HTMLToken> tokens, String expectedTitle) {
        String titleFromFilterTitle = (String) filterTitle.invokeOn(null, tokens);

        assertEquals(stripToBareMinimum(expectedTitle), stripToBareMinimum(titleFromFilterTitle),
                "Die Methode 'HTMLProcessing.filterTitle()' liefert für die Token-Liste\n" + tokenListToString(tokens)
                        + "\nein falsches Ergebnis.");
    }

    // ------------------------------------------- Helpers ------------------------------------------- //

    private boolean tokensEqual(HTMLToken first, HTMLToken second) {
        try {
            Field tokenType = HTMLToken.class.getDeclaredField("tokenType");
            Field content = HTMLToken.class.getDeclaredField("content");
            tokenType.setAccessible(true);
            content.setAccessible(true);
            return tokenType.get(first).equals(tokenType.get(second))
                    && content.get(first).toString().equals(content.get(second).toString());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return false;
        }
    }

    private String tokenToString(HTMLToken token) {
        try {
            Field tokenType = HTMLToken.class.getDeclaredField("tokenType");
            Field content = HTMLToken.class.getDeclaredField("content");
            tokenType.setAccessible(true);
            content.setAccessible(true);
            return (tokenType.get(token) == HTMLToken.TokenType.TAG ? "Tag: " : "Text: ")
                    + content.get(token).toString();
        } catch (NoSuchFieldException e) {
            return "Cannot print token, because not all fields necessary for printing are defined!";
        } catch (IllegalAccessException e) {
            return "Cannot print token, because cannot access one of its fields!";
        }
    }

    private String tokenListToString(List<HTMLToken> tokens) {
        return "[" + tokens.stream().map(this::tokenToString).collect(Collectors.joining(",\n")) + "]";
    }

    // =============================================================================================== //
    //                                                                                                 //
    // ---------------------------- Public Tests for HTMLProcessing Class ---------------------------- //
    //                                                                                                 //
    // =============================================================================================== //

    @DisplayName("- | Public Tests for tokenize()")
    @PublicTest
    void testTokenizePublic() {
        assertAll(
                () -> testTokenize("<html>Some Text</html>",
                        List.of(Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "html")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TEXT, "some text")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/html")))),
                () -> testTokenize("<html><a href=\"add>ress\">Link</a></html>",
                        List.of(Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "html")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "a href=\"add>ress\"")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TEXT, "link")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/a")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/html")))));
    }

    @DisplayName("- | Public Tests for filterLinks()")
    @PublicTest
    void testFilterLinksPublic() {
        assertAll(
                () -> testFilterLinks(
                        List.of(Objects.requireNonNull(
                                tokenOf(TOKEN_TYPE_TAG, "a href='https://man8.pgdp.sse.in.tum.de/halt.8.html'"))),
                        "man5.pgdp.sse.in.tum.de", new String[] { "man8.pgdp.sse.in.tum.de/halt.8.html" }),
                () -> testFilterLinks(List.of(Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "a href='/halt.8.html'"))),
                        "man8.pgdp.sse.in.tum.de", new String[] { "man8.pgdp.sse.in.tum.de/halt.8.html" }));
    }

    @DisplayName("- | Public Tests for filterText()")
    @PublicTest
    void testFilterTextPublic() {
        assertAll(() -> testFilterText(
                List.of(Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "a href=''")),
                        Objects.requireNonNull(tokenOf(TOKEN_TYPE_TEXT, "pinguin huhn kühlschrank")),
                        Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/a")),
                        Objects.requireNonNull(tokenOf(TOKEN_TYPE_TEXT, "tür tisch turnschuh"))),
                "pinguin huhn kühlschrank tür tisch turnschuh"));
    }

    @DisplayName("- | Public Tests for filterTitle()")
    @PublicTest
    void testFilterTitlePublic() {
        assertAll(() -> testFilterTitle(List.of(Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "html")),
                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "head")),
                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "title")),
                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TEXT, "Some Title of Page")),
                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/title")),
                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/head")),
                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/html"))), "Some Title of Page"));
    }

    // =============================================================================================== //
    //                                                                                                 //
    // ---------------------------- Hidden Tests for HTMLProcessing Class ---------------------------- //
    //                                                                                                 //
    // =============================================================================================== //

    @DisplayName("3P | Hidden Tests for tokenize()")
    @HiddenTest
    void testTokenizeHidden() {
        assertAll(
                // Strings
                () -> testTokenize("<html><a href=\"somewhere/over/the/rainbow\">link</a></html>",
                        List.of(Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "html")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "a href=\"somewhere/over/the/rainbow\"")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TEXT, "link")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/a")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/html")))),
                () -> testTokenize("<html><a href=\"somewhere'/over/the/rainbow\">link</a></html>",
                        List.of(Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "html")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "a href=\"somewhere'/over/the/rainbow\"")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TEXT, "link")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/a")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/html")))),
                () -> testTokenize("<html><a href='somewhere\"/over/the/rainbow'>link</a></html>",
                        List.of(Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "html")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "a href='somewhere\"/over/the/rainbow'")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TEXT, "link")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/a")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/html")))),
                () -> testTokenize("<html><a href=\"somewhere</a>/over/the/rainbow\">link</a></html>",
                        List.of(Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "html")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "a href=\"somewhere</a>/over/the/rainbow\"")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TEXT, "link")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/a")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/html")))),
                () -> testTokenize("<html><a href=\"some::where/over_the/r@in-bow\">link</a></html>",
                        List.of(Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "html")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "a href=\"some::where/over_the/r@in-bow\"")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TEXT, "link")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/a")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/html")))),
                () -> testTokenize("<html lang=\"de\">text</html>",
                        List.of(Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "html lang=\"de\"")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TEXT, "text")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/html")))),
                () -> testTokenize("<html>text \"<br>\" more text</html>",
                        List.of(Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "html")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TEXT, "text \"")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "br")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TEXT, "\" more text")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/html")))),
                () -> testTokenize("<html>text '<br>' more text</html>",
                        List.of(Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "html")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TEXT, "text '")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "br")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TEXT, "' more text")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/html")))),
                () -> testTokenize("<html><a style=\"font-size:8px\" href=\"somewhere/over/the/rainbow\">link</a></html>",
                        List.of(Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "html")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "a style=\"font-size:8px\" href=\"somewhere/over/the/rainbow\"")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TEXT, "link")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/a")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/html")))),
                () -> testTokenize("<html><a href=\"somewhere/over/the/rainbow\" style=\"font-size:8px\">link</a></html>",
                        List.of(Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "html")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "a href=\"somewhere/over/the/rainbow\" style=\"font-size:8px\"")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TEXT, "link")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/a")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/html")))),
                () -> testTokenize("<HTML>TeXT<A HREF=\"sOMeWheRe\">Link</A></HTML>",
                        List.of(Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "html")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TEXT, "text")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "a href=\"sOMeWheRe\"")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TEXT, "link")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/a")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/html")))),
                () -> testTokenize("<HTML><head><TITLE>Grosse Testseite</title></head><body><p title=\"hi\">Das ist ein Paragraph.</P>" +
                                "<p title=\"bye\">Das ist noch ein Paragraph,<BR>diesmal sogar mit Zeilenumbruch.</p>" +
                                "<a href='www.google.com' style=\"color:red\">Link</a><p>Text \"<a style='color:green' href='www.in.tum.de'>Lonk</a>\" " +
                                "Text</p></body></HTML>",
                        List.of(Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "html")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "head")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "title")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TEXT, "grosse testseite")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/title")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/head")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "body")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "p title=\"hi\"")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TEXT, "das ist ein paragraph.")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/p")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "p title=\"bye\"")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TEXT, "das ist noch ein paragraph,")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "br")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TEXT, "diesmal sogar mit zeilenumbruch.")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/p")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "a href='www.google.com' style=\"color:red\"")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TEXT, "link")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/a")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "p")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TEXT, "text \"")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "a style='color:green' href='www.in.tum.de'")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TEXT, "lonk")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/a")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TEXT, "\" text")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/p")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/body")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/html"))))
        );
    }

    @DisplayName("3P | Hidden Tests for filterLinks()")
    @HiddenTest
    void testFilterLinksHidden() {
        assertAll(
                () -> testFilterLinks(
                        List.of(Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "a href=\"https://pgdp.sse.in.tum.de/index.html\""))),
                        "man5.pgdp.sse.in.tum.de",
                        new String[] { "pgdp.sse.in.tum.de/index.html" }
                ),
                () -> testFilterLinks(
                        List.of(Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "a href=\"/index.html\""))),
                        "man5.pgdp.sse.in.tum.de",
                        new String[] { "man5.pgdp.sse.in.tum.de/index.html" }
                ),
                () -> testFilterLinks(
                        List.of(Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "a name=\"lbAD\""))),
                        "man5.pgdp.sse.in.tum.de",
                        new String[] {}
                ),
                /*
                () -> testFilterLinks(
                        List.of(Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "a href=\"https://pgdp.sse.in.tum.de/index.html\" style=\"color:red\""))),
                        "man5.pgdp.sse.in.tum.de",
                        new String[] { "pgdp.sse.in.tum.de/index.html" }
                ),
                () -> testFilterLinks(
                        List.of(Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "a style=\"color:red\" href=\"https://pgdp.sse.in.tum.de/index.html\""))),
                        "man5.pgdp.sse.in.tum.de",
                        new String[] { "pgdp.sse.in.tum.de/index.html" }
                ),
                () -> testFilterLinks(
                        List.of(Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "p href=\"https://pgdp.sse.in.tum.de/index.html\""))),
                        "man5.pgdp.sse.in.tum.de",
                        new String[] {}
                ),
                */
                () -> testFilterLinks(
                        List.of(Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "a href='https://pgdp.sse.in.tum.de/index.html'"))),
                        "man5.pgdp.sse.in.tum.de",
                        new String[] { "pgdp.sse.in.tum.de/index.html" }
                ),
                () -> testFilterLinks(
                        List.of(Objects.requireNonNull(tokenOf(TOKEN_TYPE_TEXT, "a href=\"https://pgdp.sse.in.tum.de/index.html\""))),
                        "man5.pgdp.sse.in.tum.de",
                        new String[] {}
                ),
                () -> testFilterLinks(
                        List.of(
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "html")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "head")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "title")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TEXT, "titel der seite")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/title")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/head")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "body")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/a")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "a href=\"https://pgdp.sse.in.tum.de/page.1.html\"")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/a")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TEXT, "a href=\"https://man6.pgdp.sse.in.tum.de/page.fake.html\"")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "br")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TEXT, "noch mehr text")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "a href='https://man4.pgdp.sse.in.tum.de/page/2.html'")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/a")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "a href=\"/page::3.html\"")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/a")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "a name=\"/page4\"")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/a")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "a href=\"https://man7.pgdp.sse.in.tum.de/page@5.html\"")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/a")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "a href='/page++6.html'")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/a")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/body")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/html"))
                        ),
                        "man5.pgdp.sse.in.tum.de",
                        new String[]{
                                "pgdp.sse.in.tum.de/page.1.html",
                                "man4.pgdp.sse.in.tum.de/page/2.html",
                                "man5.pgdp.sse.in.tum.de/page::3.html",
                                "man7.pgdp.sse.in.tum.de/page@5.html",
                                "man5.pgdp.sse.in.tum.de/page++6.html"
                        }
                )
        );
    }

    @DisplayName("2P | Hidden Tests for filterText()")
    @HiddenTest
    void testFilterTextHidden() {
        assertAll(
                () -> testFilterText(
                        List.of(
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "html")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "body")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/body")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/html"))
                        ),
                        ""
                ),
                () -> testFilterText(
                        List.of(
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "html")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "body")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TEXT, "elefant giraffe löwe")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "br /")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TEXT, " hirsch tiger eisbär katze")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "a href='bc.de'")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TEXT, "kuh esel maultier fuchs ")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/a")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "br")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "br")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TEXT, "reh maus schnabeltier krokodil")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/body")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/html"))
                        ),
                        "elefant giraffe löwe  hirsch tiger eisbär katze kuh esel maultier fuchs  reh maus schnabeltier krokodil"
                )
        );
    }

    @DisplayName("1P | Hidden Tests for filterTitle()")
    @HiddenTest
    void testFilterTitleHidden() {
        assertAll(
                () -> testFilterTitle(
                        List.of(
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "html")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "head")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "title")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TEXT, "das ist der titel")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/title")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/head")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/html"))
                        ),
                        "das ist der titel"
                ),
                () -> testFilterTitle(
                        List.of(
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "html lang=\"bg\"")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "head")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "title")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TEXT, "това е заглавието")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/title")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/head")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/html"))
                        ),
                        "това е заглавието"
                ),
                /*
                () -> testFilterTitle(
                        List.of(
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "html")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "head attr='ibute'")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "title")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TEXT, "das ist der titel")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/title")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/head")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/html"))
                        ),
                        "das ist der titel"
                ),
                () -> testFilterTitle(
                        List.of(
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "html")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "head")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "title attri=\"bute\"")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TEXT, "das ist der titel")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/title")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/head")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/html"))
                        ),
                        "das ist der titel"
                ),
                */
                () -> testFilterTitle(
                        List.of(
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "html")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "head")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "title")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TEXT, "das ist der titel")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/title")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/head")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "body")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TEXT, "das ist der inhalt")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "br")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TEXT, "der seite")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/body")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/html"))
                        ),
                        "das ist der titel"
                )
                /*
                () -> testFilterTitle(
                        List.of(
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "html")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "body")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TEXT, "das ist der inhalt")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "br")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TEXT, "der seite")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/body")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "head")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "title")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TEXT, "das ist der titel")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/title")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/head")),
                                Objects.requireNonNull(tokenOf(TOKEN_TYPE_TAG, "/html"))
                        ),
                        "das ist der titel"
                )
                */
        );
    }

    // ------------------------------------------- Helpers
    // ------------------------------------------- //

    private HTMLToken tokenOf(HTMLToken.TokenType tokenType, String contentString) {
        try {
            Field tokenTypeField = HTMLToken.class.getDeclaredField("tokenType");
            Field contentField = HTMLToken.class.getDeclaredField("content");
            tokenTypeField.setAccessible(true);
            contentField.setAccessible(true);

            HTMLToken token = (HTMLToken) newHtmlToken.newInstance(HTMLToken.TokenType.TAG);
            tokenTypeField.set(token, tokenType);
            contentField.set(token, new StringBuilder(contentString));

            return token;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return null;
        }
    }

}
