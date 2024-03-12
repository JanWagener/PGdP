package pgdp.searchengine.utilities;

import de.tum.in.test.api.dynamic.DynamicClass;
import de.tum.in.test.api.dynamic.DynamicConstructor;
import de.tum.in.test.api.dynamic.DynamicField;
import de.tum.in.test.api.dynamic.DynamicMethod;
import pgdp.searchengine.networking.HTMLToken;
import pgdp.searchengine.networking.HTTPResponse;
import pgdp.searchengine.networking.HTTPStatus;
import pgdp.searchengine.pagerepository.*;

import java.util.List;

public class Reflections {

    // -------------------------------- Macros -------------------------------- //

    public static final HTMLToken.TokenType TOKEN_TYPE_TAG = HTMLToken.TokenType.TAG;
    public static final HTMLToken.TokenType TOKEN_TYPE_TEXT = HTMLToken.TokenType.TEXT;

    // ------------------------------- Classes -------------------------------- //

    public static DynamicClass<?> htmlProcessingClass = DynamicClass.toDynamic("pgdp.searchengine.networking.HTMLProcessing");
    public static DynamicClass<?> htmlTokenClass = DynamicClass.toDynamic("pgdp.searchengine.networking.HTMLToken");
    public static DynamicClass<?> tokenTypeEnumClass = DynamicClass.toDynamic("pgdp.searchengine.networking.HTMLToken.TokenType");
    public static DynamicClass<?> httpRequestClass = DynamicClass.toDynamic("pgdp.searchengine.networking.HTTPRequest");
    public static DynamicClass<?> httpResponseClass = DynamicClass.toDynamic("pgdp.searchengine.networking.HTTPResponse");
    public static DynamicClass<?> pageCrawlingClass = DynamicClass.toDynamic("pgdp.searchengine.networking.PageCrawling");
    public static DynamicClass<?> linkedDocumentCollectionClass = DynamicClass.toDynamic("pgdp.searchengine.pagerepository.LinkedDocumentCollection");
    public static DynamicClass<?> abstractLinkedDocumentClass = DynamicClass.toDynamic("pgdp.searchengine.pagerepository.AbstractLinkedDocument");
    public static DynamicClass<?> linkedDocumentClass = DynamicClass.toDynamic("pgdp.searchengine.pagerepository.LinkedDocument");
    public static DynamicClass<?> dummyLinkedDocumentClass = DynamicClass.toDynamic("pgdp.searchengine.pagerepository.DummyLinkedDocument");

    // ----------------------------- Constructors ----------------------------- //

    public static DynamicConstructor<?> newHtmlToken = htmlTokenClass.constructor(HTMLToken.TokenType.class);
    public static DynamicConstructor<?> newHttpRequest = httpRequestClass.constructor(String.class, String.class);
    public static DynamicConstructor<?> newHttpResponse = httpResponseClass.constructor(String.class);
    public static DynamicConstructor<?> newLinkedDocumentCollection = linkedDocumentCollectionClass.constructor(int.class);
    public static DynamicConstructor<?> newLinkedDocument = linkedDocumentClass.constructor(String.class, String.class, String.class, pgdp.searchengine.util.Date.class, Author.class, String.class, int.class);
    public static DynamicConstructor<?> newDummyLinkedDocument = dummyLinkedDocumentClass.constructor(String.class, int.class);

    // -------------------------------- Fields -------------------------------- //

    // HTMLToken
    public static DynamicField<?> tokenType = htmlTokenClass.field(HTMLToken.TokenType.class, "tokenType");
    public static DynamicField<?> content = htmlTokenClass.field(StringBuilder.class, "content");
    // HTTPRequest
    public static DynamicField<?> host = httpRequestClass.field(String.class, "host");
    public static DynamicField<?> path = httpRequestClass.field(String.class, "path");
    // HTTPResponse
    public static DynamicField<?> status = httpResponseClass.field(HTTPStatus.class, "status");
    public static DynamicField<?> html = httpResponseClass.field(String.class, "html");
    // AbstractLinkedDocument
    public static DynamicField<?> incomingLinks = abstractLinkedDocumentClass.field(LinkedDocumentCollection.class, "incomingLinks");
    // LinkedDocument
    public static DynamicField<?> outgoingLinks = linkedDocumentClass.field(LinkedDocumentCollection.class, "outgoingLinks");

    // ---------------------------- Static Methods ---------------------------- //

    // HTMLProcessing
    public static DynamicMethod<?> tokenize = htmlProcessingClass.method(List.class, "tokenize", String.class);
    public static DynamicMethod<?> filterLinks = htmlProcessingClass.method(String[].class, "filterLinks", List.class, String.class);
    public static DynamicMethod<?> filterText = htmlProcessingClass.method(String.class, "filterText", List.class);
    public static DynamicMethod<?> filterTitle = htmlProcessingClass.method(String.class, "filterTitle", List.class);
    // PageCrawling
    public static DynamicMethod<?> crawlPage = pageCrawlingClass.method(boolean.class, "crawlPage", LinkedDocumentCollection.class, String.class);
    public static DynamicMethod<?> crawlPagesWithoutStart = pageCrawlingClass.method(void.class, "crawlPages", LinkedDocumentCollection.class, int.class);
    public static DynamicMethod<?> crawlPagesWithStart = pageCrawlingClass.method(void.class, "crawlPages", LinkedDocumentCollection.class, int.class, String.class);

    // ------------------------------- Methods -------------------------------- //

    // HTMLToken
    public static DynamicMethod<?> getTokenType = htmlTokenClass.method(HTMLToken.TokenType.class, "getTokenType");
    public static DynamicMethod<?> getContentAsString = htmlTokenClass.method(String.class, "getContentAsString");
    public static DynamicMethod<?> addCharacter = htmlTokenClass.method(void.class, "addCharacter", char.class);
    public static DynamicMethod<?> toString = htmlTokenClass.method(String.class, "toString");
    // HTTPRequest
    public static DynamicMethod<?> getHost = httpRequestClass.method(String.class, "getHost");
    public static DynamicMethod<?> getPath = httpRequestClass.method(String.class, "getPath");
    public static DynamicMethod<?> send = httpRequestClass.method(HTTPResponse.class, "send", int.class);
    // HTTPResponse
    public static DynamicMethod<?> getStatus = httpResponseClass.method(HTTPStatus.class, "getStatus");
    public static DynamicMethod<?> getHtml = httpResponseClass.method(String.class, "getHtml");
    // LinkedDocumentCollection
    public static DynamicMethod<?> addToResultCollection = linkedDocumentCollectionClass.method(boolean.class, "addToResultCollection", AbstractLinkedDocument.class, String[].class);
    public static DynamicMethod<?> getNextUncrawledAddress = linkedDocumentCollectionClass.method(String.class, "getNextUncrawledAddress");
    public static DynamicMethod<?> add = linkedDocumentCollectionClass.method(boolean.class, "add", Document.class);

}
