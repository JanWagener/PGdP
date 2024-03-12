package pgdp.searchengine.networking;

import de.tum.in.test.api.jupiter.HiddenTest;
import de.tum.in.test.api.jupiter.PublicTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import pgdp.searchengine.W10H01;
import pgdp.searchengine.pagerepository.AbstractLinkedDocument;
import pgdp.searchengine.pagerepository.LinkedDocumentCollection;
import pgdp.searchengine.utilities.TestingUtilities;

import static org.junit.jupiter.api.Assertions.*;
import static pgdp.searchengine.utilities.Reflections.*;
import static pgdp.searchengine.utilities.TestingUtilities.*;

@W10H01
public class PageCrawlingTests {

    @DisplayName("- | Structure Tests for 'PageCrawling'")
    @PublicTest
    void pageCrawlingStructuralTests() {
        assertAll(
                () -> assertTrue(crawlPage.exists(), "In 'PageCrawling' fehlt die statische Methode 'crawlPage(LinkedDocumentCollection, String)'"),
                () -> assertTrue(crawlPagesWithoutStart.exists(), "In 'PageCrawling' fehlt die statische Methode 'crawlPages(LinkedDocumentCollection, int)'"),
                () -> assertTrue(crawlPagesWithStart.exists(), "In 'PageCrawling' fehlt die statische Methode 'crawlPages(LinkedDocumentCollection, int, String)'")
        );
    }

    // ================================================================================================ //
    // ---------------- Methods, that test one method in LinkedDocumentCollection each ---------------- //
    // ================================================================================================ //

    void testCrawlPage(LinkedDocumentCollection originalCollection, String address, String[] links, String title, String content) {
        LinkedDocumentCollection resultCollection = TestingUtilities.copyLinkedDocumentCollection(originalCollection);
        crawlPage.invokeOn(null, resultCollection, address);

        assertAll(
                () -> assertParametersSetProperly(resultCollection.find(address), title, content, address),
                () -> assertAddingWorkedCorrectly(originalCollection, resultCollection, address, links, "'crawlPage()'")
        );
    }

    void testCrawlPagesWithoutStart(LinkedDocumentCollection originalCollection, int number) {

    }

    void testCrawlPagesWithStart(LinkedDocumentCollection originalCollection, int number, String address) {

    }

    // ------------------------------------------- Helpers ------------------------------------------- //

    private void assertParametersSetProperly(AbstractLinkedDocument document, String title, String content, String address) {
        assertEquals(stripToBareMinimum(title), stripToBareMinimum(document.getTitle()), "Das Attribut 'title' des Dokuments mit Adresse \"" + address + "\" wurde beim Aufruf von 'crawlPage()' nicht korrekt gesetzt.");
        assertEquals(stripToBareMinimum(content), stripToBareMinimum(document.getContent()), "Das Attribut 'content' des Dokuments mit Adresse \"" + address + "\" wurde beim Aufruf von 'crawlPage()' nicht korrekt gesetzt.");
        assertEquals(stripToBareMinimum(address), stripToBareMinimum(document.getAddress()), "Das Attribut 'address' des Dokuments mit Adresse \"" + address + "\" wurde beim Aufruf von 'crawlPage()' nicht korrekt gesetzt.");
    }

    // =============================================================================================== //
    // ----------------------- Public Tests for LinkedDocumentCollection Class ----------------------- //
    // =============================================================================================== //

    @DisplayName("- | Public Tests for Method 'crawlPage()'")
    @PublicTest
    void testCrawlPagePublic() {
        LinkedDocumentCollection originalCollection = new LinkedDocumentCollection(4);
        String address = "man1.pgdp.sse.in.tum.de/rstart.1.html";
        String[] links = {"pgdp.sse.in.tum.de/index.html", "man1.pgdp.sse.in.tum.de/cgi-bin/man/man2html"};
        String title = "man page of rstart";
        String content = "man page of rstart rstart section: user commands  (1) updated: rstart 1.0.5 index return to main contents &nbsp; name rstart - a sample implementation of a remote start client &nbsp; synopsis rstart [-c  context ][-g][-l  username ][-v] hostname command args ... &nbsp; description rstart  is a simple implementation of a remote start client asdefined in &quot;a flexible remote execution protocol based on  rsh &quot;.it uses  rsh  as its underlying remote execution mechanism. &nbsp; options -c  context this option specifies the  context  in which the command is to berun.  a  context  specifies a general environment the program is tobe run in.  the details of this environment are host-specific; theintent is that the client need not know how the environment must beconfigured.  if omitted, the context defaults to  x .  this shouldbe suitable for running x programs from the host's &quot;usual&quot; xinstallation. -g interprets  command  as a  generic command , as discussedin the protocol document.  this is intended to allow common applicationsto be invoked without knowing what they are called on the remote system.currently, the only generic commands defined are  terminal , loadmonitor ,  listcontexts , and  listgenericcommands . -l  username this option is passed to the underlying  rsh ; it requests thatthe command be run as the specified user. -v this option requests that  rstart  be verbose in its operation.without this option,  rstart  discards output from the remote's rstart  helper, and directs the  rstart  helper to detachthe program from the  rsh  connection used to start it.  withthis option, responses from the helper are displayed and the resultingprogram is not detached from the connection. &nbsp; notes this is a trivial implementation.  far more sophisticated implementationsare possible and should be developed. error handling is nonexistent.  without  -v , error reports fromthe remote are discarded silently.  with  -v , error reports aredisplayed. the $display environment variable is passed.  if it starts with a colon,the local hostname is prepended.  the local domain name should be appendedto unqualified host names, but isn't. the $session_manager environment variable should be passed, but isn't. x11 authority information is passed for the current display. ice authority information should be passed, but isn't.  it isn'tcompletely clear how  rstart  should select what ice authorityinformation to pass. even without  -v , the sample  rstart  helper will leave ashell waiting for the program to complete.  this causes no real harmand consumes relatively few resources, but if it is undesirableit can be avoided by explicitly specifying the &quot;exec&quot; command to theshell, eg rstart somehost exec xterm this is obviously dependent on the command interpreter being used onthe remote system; the example given will work for the bourne and c shells. &nbsp; see also rsh (1), a flexible remote execution protocol based on  rsh &nbsp; author jordan brown, quarterdeck office systems &nbsp; index name synopsis description options notes see also author this document was created by man2html ,using the manual pages. time: 08:55:10 gmt, december 16, 2021 ";

        assertTrue(crawlPage.exists());
        // testCrawlPage(originalCollection, address, links, title, content);
    }

    @DisplayName("- | Public Tests for Methods 'crawlPages()'")
    @PublicTest
    void testCrawlPagesPublic() {
        assertAll(
                () -> assertTrue(crawlPagesWithStart.exists()),
                () -> assertTrue(crawlPagesWithoutStart.exists())
        );
    }

    // =============================================================================================== //
    // ----------------------- Hidden Tests for LinkedDocumentCollection Class ----------------------- //
    // =============================================================================================== //

    @DisplayName("2P | Points reserved for crawlPage(s)()-methods")
    @HiddenTest
    void reservePointsForPageCrawling() {
        fail("Dieser Test schlägt immer fehl. Er reserviert die Punkte für die crawlPage()- und die beiden crawlPages()-Methoden. " +
                "Ein Tutor wird diese manuell testen und dann eventuell die Punkte darauf vergeben.");
    }

    @DisplayName("-P | Hidden Tests for Method 'crawlPage()'")
    @HiddenTest
    @Disabled
    void testCrawlPageHidden() {
        LinkedDocumentCollection originalCollection = new LinkedDocumentCollection(4);
        String address = "man1.pgdp.sse.in.tum.de/rstart.1.html";
        String[] links = {"pgdp.sse.in.tum.de/index.html", "man1.pgdp.sse.in.tum.de/cgi-bin/man/man2html"};
        String title = "man page of rstart";
        String content = "man page of rstart rstart section: user commands  (1) updated: rstart 1.0.5 index return to main contents &nbsp; name rstart - a sample implementation of a remote start client &nbsp; synopsis rstart [-c  context ][-g][-l  username ][-v] hostname command args ... &nbsp; description rstart  is a simple implementation of a remote start client asdefined in &quot;a flexible remote execution protocol based on  rsh &quot;.it uses  rsh  as its underlying remote execution mechanism. &nbsp; options -c  context this option specifies the  context  in which the command is to berun.  a  context  specifies a general environment the program is tobe run in.  the details of this environment are host-specific; theintent is that the client need not know how the environment must beconfigured.  if omitted, the context defaults to  x .  this shouldbe suitable for running x programs from the host's &quot;usual&quot; xinstallation. -g interprets  command  as a  generic command , as discussedin the protocol document.  this is intended to allow common applicationsto be invoked without knowing what they are called on the remote system.currently, the only generic commands defined are  terminal , loadmonitor ,  listcontexts , and  listgenericcommands . -l  username this option is passed to the underlying  rsh ; it requests thatthe command be run as the specified user. -v this option requests that  rstart  be verbose in its operation.without this option,  rstart  discards output from the remote's rstart  helper, and directs the  rstart  helper to detachthe program from the  rsh  connection used to start it.  withthis option, responses from the helper are displayed and the resultingprogram is not detached from the connection. &nbsp; notes this is a trivial implementation.  far more sophisticated implementationsare possible and should be developed. error handling is nonexistent.  without  -v , error reports fromthe remote are discarded silently.  with  -v , error reports aredisplayed. the $display environment variable is passed.  if it starts with a colon,the local hostname is prepended.  the local domain name should be appendedto unqualified host names, but isn't. the $session_manager environment variable should be passed, but isn't. x11 authority information is passed for the current display. ice authority information should be passed, but isn't.  it isn'tcompletely clear how  rstart  should select what ice authorityinformation to pass. even without  -v , the sample  rstart  helper will leave ashell waiting for the program to complete.  this causes no real harmand consumes relatively few resources, but if it is undesirableit can be avoided by explicitly specifying the &quot;exec&quot; command to theshell, eg rstart somehost exec xterm this is obviously dependent on the command interpreter being used onthe remote system; the example given will work for the bourne and c shells. &nbsp; see also rsh (1), a flexible remote execution protocol based on  rsh &nbsp; author jordan brown, quarterdeck office systems &nbsp; index name synopsis description options notes see also author this document was created by man2html ,using the manual pages. time: 08:55:10 gmt, december 16, 2021 ";

        // testCrawlPage(originalCollection, address, links, title, content);
    }

    @DisplayName("-P | Hidden Tests for Methods 'crawlPages()'")
    @HiddenTest
    @Disabled
    void testCrawlPagesHidden() {
        assertAll(
                () -> assertTrue(crawlPagesWithStart.exists()),
                () -> assertTrue(crawlPagesWithoutStart.exists())
        );
    }

    // =================================================================================================== //
    //                                                                                                     //
    // ------------------------------ Corrector Tests for HTTPRequest Class ------------------------------ //
    //                                                                                                     //
    // =================================================================================================== //

    public static void main(String... args) {
        LinkedDocumentCollection collection1 = (LinkedDocumentCollection) newLinkedDocumentCollection.newInstance(7);
        crawlPage.invokeOn(null, collection1, "man7.pgdp.sse.in.tum.de/iso-8859-15.7.html");
        System.out.println(linkedDocumentCollectionAsString(collection1));
        int putBreakpointHere = 0;

        System.out.println("\n\n\n=================================\n\n\n");

        LinkedDocumentCollection collection2 = (LinkedDocumentCollection) newLinkedDocumentCollection.newInstance(7);
        crawlPagesWithStart.invokeOn(null, collection2, 2, "man7.pgdp.sse.in.tum.de/iso-8859-15.7.html");
        System.out.println(linkedDocumentCollectionAsString(collection2));
        int putBreakpointHere2 = 0;

        System.out.println("\n\n\n=================================\n\n\n");

        LinkedDocumentCollection collection3 = (LinkedDocumentCollection) newLinkedDocumentCollection.newInstance(7);
        add.invokeOn(collection3, newDummyLinkedDocument.newInstance("man7.pgdp.sse.in.tum.de/iso-8859-15.7.html", 5));
        crawlPagesWithoutStart.invokeOn(null, collection3, 2);
        System.out.println(linkedDocumentCollectionAsString(collection3));
        int putBreakpointHere3 = 0;
    }

}
