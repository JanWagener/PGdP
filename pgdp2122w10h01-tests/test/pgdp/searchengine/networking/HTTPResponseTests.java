package pgdp.searchengine.networking;

import de.tum.in.test.api.jupiter.HiddenTest;
import de.tum.in.test.api.jupiter.PublicTest;
import org.junit.jupiter.api.DisplayName;
import pgdp.searchengine.W10H01;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

import static pgdp.searchengine.utilities.Reflections.*;

@W10H01
public class HTTPResponseTests {

    @DisplayName("- | Test Structure of HTTPResponse-Class")
    @PublicTest
    void httpResponseStructuralTests() {
        assertAll(
                () -> assertTrue(newHttpResponse.exists(), "Der für die Klasse 'HTTPResponse' geforderte Konstruktor 'HTTPRequest(String)' wurde nicht gefunden."),
                () -> assertTrue(status.exists(), "Das für die Klasse 'HTTPResponse' geforderte Attribut 'HTTPStatus status' wurde nicht gefunden."),
                () -> assertTrue(html.exists(), "Das für die Klasse 'HTTPResponse' geforderte Attribut 'String html' wurde nicht gefunden."),
                () -> assertTrue(getStatus.exists(), "Die für die Klasse 'HTTPResponse' geforderte Methode mit Signatur 'HTTPStatus getStatus()' wurde nicht gefunden."),
                () -> assertTrue(getHtml.exists(), "Die für die Klasse 'HTTPResponse' geforderte Methode mit Signatur 'String getHtml()' wurde nicht gefunden.")
        );
    }

    // ================================================================================================ //
    // ---------------------- Methods, that test one method in HTTPResponse each ---------------------- //
    // ================================================================================================ //

    void testHTTPResponseConstructor(String responseText, HTTPStatus expectedStatus, String expectedHtml) {
        var newHttpResponseObject = newHttpResponse.newInstance(responseText);

        HTTPStatus actualStatus = (HTTPStatus) status.getOf(newHttpResponseObject);
        String actualHtml = (String) html.getOf(newHttpResponseObject);

        assertAll(
                () -> assertEquals(expectedStatus, actualStatus,
                        "Beim Erzeugen eines 'HTTPResponse'-Objektes mit dem String\n" + responseText + "\nwurde der Status falsch ausgelesen."),
                () -> {
                    if(expectedHtml == null)
                        return;
                    assertEquals(expectedHtml, actualHtml,
                            "Beim Erzeugen eines 'HTTPResponse'-Objektes mit dem String\n" + responseText + "\nwurde der HTML-Code falsch ausgelesen.");
                }
        );
    }

    void testGetStatus(HTTPStatus statusInAttribute) {
        var httpResponseObject = newHttpResponse.newInstance("HTTP/1.1 200 OK\r\n<HTML><HEAD></HEAD><BODY>Dummy Response</BODY></HTML>");

        Field statusField = status.toField();
        statusField.setAccessible(true);
        try {
            statusField.set(httpResponseObject, statusInAttribute);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        HTTPStatus statusFromGetStatus = (HTTPStatus) getStatus.invokeOn(httpResponseObject);
        assertEquals(statusInAttribute, statusFromGetStatus,
                "Die Methode 'getStatus()' in 'HTTPResponse' liest den Status des Objektes falsch aus.");
    }

    void testGetHtml(String htmlInAttribute) {
        var httpResponseObject = newHttpResponse.newInstance("HTTP/1.1 200 OK\r\n<HTML><HEAD><TITLE>Dummy Title</TITLE></HEAD><BODY>Dummy Response</BODY></HTML>");

        Field htmlField = html.toField();
        htmlField.setAccessible(true);
        try {
            htmlField.set(httpResponseObject, htmlInAttribute);
        } catch(IllegalAccessException e) {
            e.printStackTrace();
        }
        String htmlFromGetHtml = (String) getHtml.invokeOn(httpResponseObject);
        assertEquals(htmlInAttribute, htmlFromGetHtml,
                "Die Methode 'getHtml()' in 'HTTPResponse' liest den HTML-Code des Objektes falsch aus.");
    }

    // ================================================================================================= //
    // ------------------------------ Public Tests for HTTPResponse Class ------------------------------ //
    // ================================================================================================= //

    @DisplayName("- | Public Tests for Methods in HTTPResponse")
    @PublicTest
    void testHTTPResponseMethodsPublic() {
        assertAll(
                () -> testHTTPResponseConstructor(
                        "HTTP/1.1 200 OK\r\n\r\n<HTML><HEAD><TITLE>Title</TITLE></HEAD><BODY>Some Text</BODY></HTML>",
                        HTTPStatus.OK,
                        "<HTML><HEAD><TITLE>Title</TITLE></HEAD><BODY>Some Text</BODY></HTML>"
                        ),
                () -> testGetStatus(HTTPStatus.NOT_FOUND),
                () -> testGetHtml("<HTML><HEAD><TITLE>\"Real\" Title</TITLE></HEAD><BODY>\"Real\" Response</BODY></HTML>")
        );
    }

    // ================================================================================================= //
    // ------------------------------ Hidden Tests for HTTPResponse Class ------------------------------ //
    // ================================================================================================= //

    @DisplayName("1P | Hidden Tests for Methods in HTTPResponse")
    @HiddenTest
    void testHTTPResponseMethodsHidden() {
        assertAll(
                // Constructor
                () -> testHTTPResponseConstructor(
                        "HTTP/1.1 200 OK\r\n\r\n<HTML><HEAD><TITLE>Title</TITLE></HEAD><BODY>Some Text</BODY></HTML>",
                        HTTPStatus.OK,
                        "<HTML><HEAD><TITLE>Title</TITLE></HEAD><BODY>Some Text</BODY></HTML>"
                ),
                () -> testHTTPResponseConstructor(
                        "HTTP/1.1 400 Bad Request\r\n\r\n",
                        HTTPStatus.BAD_REQUEST,
                        null
                ),
                () -> testHTTPResponseConstructor(
                        "HTTP/1.1 403 Forbidden\r\n\r\n",
                        HTTPStatus.FORBIDDEN,
                        null
                ),
                () -> testHTTPResponseConstructor(
                        "HTTP/1.1 404 Not Found\r\n\r\n",
                        HTTPStatus.NOT_FOUND,
                        null
                ),
                () -> testHTTPResponseConstructor(
                        "HTTP/1.1 405 Method Not Allowed\r\n\r\n",
                        HTTPStatus.METHOD_NOT_ALLOWED,
                        null
                ),
                () -> testHTTPResponseConstructor(
                        "HTTP/1.1 408 Request Timeout\r\n\r\n",
                        HTTPStatus.REQUEST_TIMEOUT,
                        null
                ),
                () -> testHTTPResponseConstructor(
                        "HTTP/1.1 200 OK\r\n\r\n<HTML LANG=\"de\"><HEAD><TITLE>Title</TITLE></HEAD><BODY>Some Text</BODY></HTML>",
                        HTTPStatus.OK,
                        "<HTML LANG=\"de\"><HEAD><TITLE>Title</TITLE></HEAD><BODY>Some Text</BODY></HTML>"
                ),
                () -> testHTTPResponseConstructor(
                        "HTTP/1.1 200 OK\r\nDate: Mon, 17 Jan 2022 00:05:30 GMT\r\nServer: Apache/2.4.41 (Ubuntu)\r\n\r\n<HTML><HEAD><TITLE>Title</TITLE></HEAD><BODY>Some Text</BODY></HTML>",
                        HTTPStatus.OK,
                        "<HTML><HEAD><TITLE>Title</TITLE></HEAD><BODY>Some Text</BODY></HTML>"
                ),

                // Getter
                () -> testGetStatus(HTTPStatus.OK),
                () -> testGetStatus(HTTPStatus.BAD_REQUEST),
                () -> testGetStatus(HTTPStatus.FORBIDDEN),
                () -> testGetStatus(HTTPStatus.NOT_FOUND),
                () -> testGetStatus(HTTPStatus.METHOD_NOT_ALLOWED),
                () -> testGetStatus(HTTPStatus.REQUEST_TIMEOUT),

                () -> testGetHtml("<HTML></HTML>"),
                () -> testGetHtml("<html><head><title>\"Real\" Title</title></head><BODY>\"Real\" Response</body></html>"),
                () -> testGetHtml("<HTML LANG=\"de\"><HEAD><TITLE>\"Real\" Title</TITLE></HEAD><BODY>\"Real\" Response</BODY></HTML>")
        );
    }

}
