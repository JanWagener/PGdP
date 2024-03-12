package pgdp.searchengine.networking;

import static org.junit.jupiter.api.Assertions.*;
import static pgdp.searchengine.utilities.Reflections.getHost;
import static pgdp.searchengine.utilities.Reflections.getHtml;
import static pgdp.searchengine.utilities.Reflections.getPath;
import static pgdp.searchengine.utilities.Reflections.getStatus;
import static pgdp.searchengine.utilities.Reflections.host;
import static pgdp.searchengine.utilities.Reflections.newHttpRequest;
import static pgdp.searchengine.utilities.Reflections.path;
import static pgdp.searchengine.utilities.Reflections.send;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;

import de.tum.in.test.api.AllowLocalPort;
import de.tum.in.test.api.AllowThreads;
import de.tum.in.test.api.jupiter.HiddenTest;
import de.tum.in.test.api.jupiter.PublicTest;
import org.junit.jupiter.api.Test;
import pgdp.searchengine.W10H01;
import pgdp.searchengine.utilities.LocalServer;

@W10H01
@AllowThreads
@AllowLocalPort
class HTTPRequestTests {

    @DisplayName("- | Test Structure of HTTPRequest-Class")
    @PublicTest
    void httpRequestStructuralTests() {
        assertAll(() -> assertTrue(newHttpRequest.exists(),
                "Der für die Klasse 'HTTPRequest' geforderte Konstruktor 'HTTPRequest(String, String)' wurde nicht gefunden."),
                () -> assertTrue(host.exists(),
                        "Das für die Klasse 'HTTPRequest' geforderte Attribut 'String host' wurde nicht gefunden."),
                () -> assertTrue(path.exists(),
                        "Das für die Klasse 'HTTPRequest' geforderte Attribut 'String path' wurde nicht gefunden."),
                () -> assertTrue(getHost.exists(),
                        "Die für die Klasse 'HTTPRequest' geforderte Methode mit Signatur 'String getHost()' wurde nicht gefunden."),
                () -> assertTrue(getPath.exists(),
                        "Die für die Klasse 'HTTPRequest' geforderte Methode mit Signatur 'String getPath()' wurde nicht gefunden."),
                () -> assertTrue(send.exists(),
                        "Die für die Klasse 'HTTPRequest' geforderte Methode mit Signatur 'HTTPResponse send(int)' wurde nicht gefunden."));
    }

    // ================================================================================================= //
    //                                                                                                   //
    // ----------------------- Methods, that test one method in HTTPRequest each ----------------------- //
    //                                                                                                   //
    // ================================================================================================= //

    void testHTTPRequestConstructor(String hostString, String pathString) {
        var newHttpRequestObject = newHttpRequest.newInstance(hostString, pathString);

        String inAttributeHost = (String) host.getOf(newHttpRequestObject);
        String inAttributePath = (String) path.getOf(newHttpRequestObject);

        assertAll(
                () -> assertEquals(hostString, inAttributeHost,
                        "Nachdem dem Konstruktor von 'HTTPRequest' der String \"" + hostString
                                + "\" als erster Parameter übergeben wird, "
                                + "sollte dieser auch im Attribut 'host' stehen. Stattdessen stand dort \""
                                + inAttributeHost + "\"."),
                () -> assertEquals(pathString, inAttributePath,
                        "Nachdem dem Konstruktor von 'HTTPRequest' der String \"" + hostString
                                + "\" als zweiter Parameter übergeben wird, "
                                + "sollte dieser auch im Attribut 'path' stehen. Stattdessen stand dort \""
                                + inAttributePath + "\"."));
    }

    void testGetHostPublic(String hostString) {
        var httpRequestObject = newHttpRequest.newInstance("DummyHost", "DummyPath");

        Field hostField = host.toField();
        hostField.setAccessible(true);
        try {
            hostField.set(httpRequestObject, hostString);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        String gottenFromGetHost = (String) getHost.invokeOn(httpRequestObject);
        assertEquals(hostString, gottenFromGetHost,
                "Die Methode 'getHost()' auf einem 'HTTPRequest'-Objekt mit 'host' \"" + hostString + "\" aufzurufen "
                        + "sollte diesen String auch zurückgeben. Stattdessen wurde \"" + gottenFromGetHost
                        + "\" zurückgegeben.");
    }

    void testGetPathPublic(String pathString) {
        var httpRequestObject = newHttpRequest.newInstance("DummyHost", "DummyPath");

        Field pathField = path.toField();
        pathField.setAccessible(true);
        try {
            pathField.set(httpRequestObject, pathString);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        String gottenFromGetPath = (String) getPath.invokeOn(httpRequestObject);
        assertEquals(pathString, gottenFromGetPath,
                "Die Methode 'getPath()' auf einem 'HTTPRequest'-Objekt mit 'path' \"" + pathString + "\" aufzurufen "
                        + "sollte diesen String auch zurückgeben. Stattdessen wurde \"" + gottenFromGetPath
                        + "\" zurückgegeben.");
    }

    void testSendPublic() {
        if (true) {
            return;
        }

        int port = 8000;
        String serverHost = "localhost:8080";
        String requestedPath = "somewhere/over/the/rainbow";

        LocalServer server = new LocalServer();
        server.setPort(port);
        server.setHost(serverHost);
        server.setPath(requestedPath);
        server.start();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        HTTPRequest request = (HTTPRequest) newHttpRequest.newInstance(serverHost, requestedPath);
        HTTPResponse response = (HTTPResponse) send.invokeOn(request, port);

        assertEquals(HTTPStatus.OK, getStatus.invokeOn(response), "Status not okay");
        assertEquals("<html>Hoi</html>", getHtml.invokeOn(response), "HTML not okay");

        send.exists();
    }

    // ================================================================================================ //
    //                                                                                                  //
    // ------------------------------ Public Tests for HTTPRequest Class ------------------------------ //
    //                                                                                                  //
    // ================================================================================================ //

    @DisplayName("- | Public Tests for Methods in HTTPRequest")
    @PublicTest
    void testHTTPRequestMethodsPublic() {
        assertAll(() -> testHTTPRequestConstructor("man3.pgdp.sse.in.tum.de", "File::MimeInfo.3pm.html"),
                () -> testGetHostPublic("man7.pgdp.sse.in.tum.de"), () -> testGetPathPublic("cgi-bin/man/man2html"),
                () -> send.exists());
    }

    // ================================================================================================ //
    //                                                                                                  //
    // ------------------------------ Hidden Tests for HTTPRequest Class ------------------------------ //
    //                                                                                                  //
    // ================================================================================================ //

    @DisplayName("0.5P | Hidden Tests for Constructor and Getters of HTTPRequest")
    @HiddenTest
    void testHTTPRequestConstructorAndGettersHidden() {
        assertAll(
                // Constructor
                () -> testHTTPRequestConstructor("man1.pgdp.sse.in.tum.de", "index.html"),
                () -> testHTTPRequestConstructor("www.google.com", "maps/@48.1786576,11.5525174,14z"),
                () -> testHTTPRequestConstructor("pinguin.fantasy-url.aq", "amundsen/scott::southpole"),

                // Getter
                () -> testGetHostPublic("man1.pgdp.sse.in.tum.de"),
                () -> testGetHostPublic("www.google.com"),
                () -> testGetHostPublic("pinguin.fantasy-url.aq"),

                () -> testGetPathPublic("index.html"),
                () -> testGetPathPublic("maps/@48.1786576,11.5525174,14z"),
                () -> testGetPathPublic("amundsen/scott::southpole")
        );
    }

    @DisplayName("2.5P | Points reserved for send()-method")
    @HiddenTest
    void reservePointsForSend() {
        fail("Dieser Test schlägt immer fehl. Er reserviert die Punkte für die 'send()'-Methode. " +
                "Ein Tutor wird diese manuell testen und dann eventuell die Punkte darauf vergeben.");
    }

    // =================================================================================================== //
    //                                                                                                     //
    // ------------------------------ Corrector Tests for HTTPRequest Class ------------------------------ //
    //                                                                                                     //
    // =================================================================================================== //

    public static void main(String... args) {
        // Should work
        testSendWith("pgdp.sse.in.tum.de", "index.html");
        testSendWith("man7.pgdp.sse.in.tum.de", "iso-8859-15.7.html");

        // Not found
        testSendWith("pgdp.sse.in.tum.de", "pinguine.html");
    }

    static void testSendWith(String host, String path) {
        HTTPRequest request = (HTTPRequest) newHttpRequest.newInstance(host, path);
        HTTPResponse response = (HTTPResponse) send.invokeOn(request, 443);
        int putBreakpointHere = 0;
    }
}
