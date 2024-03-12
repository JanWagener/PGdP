# PinguPinguLos

*Hinweis:*
Du kannst deine Implementierung von letzter Woche weiterverwenden oder das Template von dieser Aufgabe benutzen. Die Funktionalitäten von letzter Woche werden nicht erneut getestet und bewertet werden.
Allerdings könnten einige der neuen Funktionen auf alten aufbauen/diese erweitern. Außerdem wurde in der Musterlösung von Woche 07 noch eine Änderung eingebaut, welche das Arbeiten mit den `DocumentCollections` erleichtert: `DocumentCollection` implementiert jetzt das `Iterable<Document>`-Interface, was das Verwenden von `DocumentCollection`s in enhanced For-Loops erlaubt. Die Tests dieser Woche verlassen sich darauf. Wenn du also mit deiner eigenen Implementierung weiterarbeiten willst, stelle sicher, dass alle Funktionalitäten aus dem aktuellen Template vorhanden sind.

Für das Lösen der folgenden Aufgaben dürfen lediglich Klassen/Interfaces aus den Packages `java.lang`, `java.util`, `java.util.stream`, `java.io` und die `SSLSocket`- sowie die `SSLSocketFactory-Klasse` aus dem Package `javax.net.ssl` verwendet werden. (Subpackages der angegebenen Packages wie `java.util.function` sind dabei ausgeschlossen, also auch nicht erlaubt). Im JDK mitgelieferte Exception-Klassen dürfen auch verwendet werden. Ein Paar Teilaufgaben schränken das noch weiter ein. Das steht dann aber jeweils dabei.

Da wir letztes Mal am Ende der Star-Wars-Reihe angelangt sind, gibt es diese Woche leider keine hochgradig witzigen Referenzen mehr. Man könnte fast sagen, dass diese Aufgabe unter den Suchmaschinen ein Einzelgänger ist. Einzelgänger Nummer Eins sozusagen (Es könnten ja noch mehr kommen).

##Ziel

Diese Woche wollen wir unsere in den letzten Wochen implementierte `LinkedDocumentCollection` mit "echten" Dokumenten aus dem Internet füllen bzw. mit Seiten von einem von der Rechnerbetriebsgruppe für uns zur Verfügung gestellten Server. Diese Seiten findest du unter [https://pgdp.sse.in.tum.de/](https://pgdp.sse.in.tum.de/). Klick dich da ruhig mal ein bisschen durch, um ein Gespür dafür zu bekommen, wie dieses kleine "Mock-Internet" aufgebaut ist.

Im Folgenden wird dir die Aufgabenstellung erst einmal ein Paar Dinge über das Abfragen von Seiten und deren inneren Aufbau erklären, dann geht es darum, nach und nach automatisiert Seiten vom Server herunterzuladen, in `LinkedDocument`-Objekte umzuwandeln und in unsere `LinkedDocumentCollection` einzufügen.

##Erklärungen einiger für die Aufgabe relevanter Konzepte

###HTTP

Um mit anderen Rechnern zu kommunizieren, ist es wichtig, sich an ein bestimmtes Kommunikationsprotokoll zu halten. Sonst weiß der Rechner, mit dem man Daten austauscht, möglicherweise nicht, wie er die von einem selbst gesandten Daten zu interpretieren hat und umgekehrt. Ein gängiges solches Protokoll heißt `HTTP` (`H`yper`T`ext `T`ransfer `P`rotokoll).
Bei der Kommunikation zwischen zwei Rechnern sendet z.B. Rechner A eine Zeichenkette an Rechner B und B antwortet mit einer anderen Zeichenkette. HTTP gibt dabei Schemata vor, wie diese Zeichenketten jeweils aufgebaut sein sollen bzw. wie sie zu interpretieren/behandeln sind. Eine typische HTTP-Anfrage könnte beispielsweise folgendermaßen aussehen:
```http
GET /rstart.1.html HTTP/1.1
Host: man1.pgdp.sse.in.tum.de
User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:64.0) Gecko/20100101 Firefox/64.0
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8
Accept-Language: de,en;q=0.7,en-US;q=0.3
Accept-Encoding: gzip, deflate, br
DNT: 1
Connection: keep-alive
Upgrade-Insecure-Requests: 1
```
Die erste Zeile enthält die sogenannte "Anfragemethode" GET - dazu gleich mehr - den abgefragten Pfad "rstart.1.html", sowie die Version des Protokolls (hier: 1.1), jeweils mit Leerzeichen getrennt.
Die nächste Zeile enthält den Host, an den die Anfrage geschickt werden soll. Dieser ist in Version 1.1 von HTTP obligatorisch.
Die folgenden Zeilen definieren (in keiner speziellen Reihenfolge) einige andere für den Empfänger (vielleicht) interessante Information. Hier beispielsweise das Betriebssystem und den Browser des Senders, akzeptierte Formate, Sprachen und Kodierungen für die Antwortdaten und mehr.

Eine Antwort auf diese Anfrage könnte folgendermaßen aussehen:
```http
HTTP/1.1 200 OK
Date: Tue, 21 Dec 2021 18:06:31 GMT
Server: Apache/2.4.41 (Ubuntu)
Last-Modified: Tue, 21 Dec 2021 09:38:29 GMT
ETag: "10dc-5d3a4c3154785"
Accept-Ranges: bytes
Content-Length: 4316
Vary: Accept-Encoding
Content-Type: text/html

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
    <HEAD>
        <TITLE>Man page of RSTART</TITLE>
    </HEAD>
    <BODY>
        <H1>RSTART</H1>
        ...
    </BODY>
</HTML>
```

In der ersten Zeile wieder die Version des Protokolls, aber diesmal mit dem sogenannten Statuscode und der zugehörigen Message der Antwort - auch hierzu gleich mehr.
Darauf folgen wieder einige Header-Daten, wie das Datum und die Uhrzeit, zu denen die Antwort verschickt wurde, der Typ des Servers, die Länge der Antwort usw.
Nach dem Header folgt dann der verschickte HTML-Code, der in obigem Request angefordert wurde.

HTTP definiert mehrere sogenannte "Anfragemethoden". Das sind im Prinzip nur Strings, die kennzeichnen, wie der Empfänger die Anfrage zu verstehen hat:
- `GET`: Signalisiert dem Empfänger, dass er dem Sender eine bestimmte Ressource (z.B. eine HTML-Seite) zurücksenden soll. Einen GET-Request sendet man z.B., wenn man eine Seite von einem Server laden will, um sie im lokalen Browser anzuzeigen, oder wenn man eine Datei herunterladen will.
- `POST`: Anfragen, die die POST-Anfragemethode verwenden, werden meist noch weitere Daten beigefügt. Eine POST-Anfrage signalisiert dem Empfänger, dass er die mitgesandten Daten (auf eine je nach Anwendung im Empfänger definierte Weise) weiterverarbeiten soll. Eine POST-Anfrage würde man z.B. verwenden, wenn man sich in einen Host ein- oder ausloggen will, oder man ein Webformular ausgefüllt hat (z.B. eine Umfrage) und dieses absenden will.
- `HEAD`: Signalisiert dem Empfänger, dass er den Header senden soll, den er bei einem entsprechenden GET-Request gesandt hätte, nicht aber den (i.d.R. um ein Vielfaches größeren) Rumpf. Das ist z.B. dann nützlich, wenn man einige im Header enthaltene Daten (den Status der Response, also ob die Anfrage überhaupt geglückt ist, oder die Größe der Datei, die beim entsprechenden GET-Request gesendet werden würde) abfragen möchte, ohne einen womöglich riesigen Rumpf durch die Gegend schicken zu müssen.
- `PUT`: Wird verwendet, um gezielt eine Ressource an den Empfänger hochzuladen.
- usw.

Wir wollen lediglich Seiten holen, daher werden wir in dieser Aufgabe nur `GET`-Requests verschicken. Unser Server reagiert nur auf solche (und auch auf `POST`-Requests, aber so, als wären sie `GET`-Requests). Alle weiteren Anfragemethoden werden mit dem Status "Method Not Allowed" abgeblockt.

HTTP definiert außerdem sogenannte Statuscodes. Dies sind dreistellige Zahlen, die Information repräsentieren, ob eine Anfrage erfolgreich war bzw. wenn nicht, warum nicht, und noch einiges mehr. Gängige Statuscodes sind unter anderem:
- **200 (OK)**: Die Antwort auf eine Anfrage enthält diesen Statuscode, wenn alles so gelaufen ist, wie erwartet und die Antwort das Ergebnis der Anfrage enthält.
- **400 (Bad Request)**: Die Antwort wird diesen Statuscode haben, wenn sie nicht korrekt formatiert war und daher nicht klar war, wie damit umzugehen ist.
- **403 (Forbidden)**: Dieser Statuscode bedeutet, dass der Host zwar existiert und ansprechbar ist, aber der angeforderte Zugriff nicht autorisiert ist.
- **404 (Not Found)**: Dieser Statuscode ist einer der am häufigsten anzutreffen. Er bedeutet, dass die angefragte Ressource (= z.B. Website) nicht existiert.
- **405 (Method Not Allowed)**: Dieser Statuscode bedeutet, dass die verwendete Anfragemethode an der angefragten Ressource nicht erlaubt ist. Er wird z.B. auftreten, wenn du versuchst, einen PUT-Request an irgendeinen Pfad in unserem Server zu senden.
- **408 (Request Timeout)**: Dieser Statuscode bedeutet, dass der Server zu lange auf das Ende der Anfrage gewartet hat (die maximal erlaubte Zeitspanne definiert der Server selbst). Er tritt unter Anderem dann auf, wenn man eine unvollständige Anfrage an einen Server schickt und dieser auf deren Rest wartet.

###HTML

`HTML` (`H`yper`T`ext `M`arkup `L`anguage) ist eine Auszeichnungssprache (engl. Markup Language), also eine computerverständliche Sprache, die anders als Java keine Algorithmen beschreibt, sondern die Formatierung von Seiten und Texten. HTML bildet die Grundlage für so ziemlich jede Website und webbasierte Anwendung heutzutage. HTML-Dokumente können in Webbrowsern dargestellt werden. In dieser Aufgabe wollen wir Seiten von einem Server herunterladen. Diese werden wir von dem Server als HTML-Dokumente erhalten. Um aus der Antwort des Servers die für uns notwendigen Informationen herauslesen zu können, brauchen wir ein Grundverständnis dafür, wie HTML-Dokumente aufgebaut sind. Hier ist ein Beispiel für ein HTML-Dokument:
```html
<html>
    <head>
        <title>Titel der Seite</title>
    </head>
    <body>
        <h1>Eine große Überschrift</h1>
        Etwas Text
        <h2>Eine etwas kleinere Überschrift</h2>
        Etwas mehr Text
        <br>
        <a href="https://www.youtube.com/watch?v=dQw4w9WgXcQ">Ein Link</a>
    </body>
</html>
```
Du kannst [hier](https://jsfiddle.net) online eigenen HTML-Code schreiben und anzeigen lassen. Einrückungen sind - wie in Java - optional (aber sehr angeraten) und dienen nur der Leserlichkeit.

####Tags und Elemente

HTML strukturiert eine Seite mithilfe von Elementen. Eine Überschrift ist ein Element, ein Paragraph ist ein Element, ein Link ist ein Element, ein Zeilenumbruch ist ein Element usw.
Ein Element kann auch weitere Elemente als Kinder haben. Z.B. kann ein Paragraph mehrere Links und Zeilenumbrüche (oder auch weitere Paragraphen) enthalten. Ein HTML-Dokument ist also nichts anderes als ein Baum von solchen HTML-Elementen. Im HTML-Code wird ein Element mit spitzen Klammern definiert. Diese Deklarationen von Elementen nennt man HTML-Tags. `<h1>` definiert eine Überschrift, `<p>` einen Paragraphen, `<a>` einen Link und `<br>` einen Zeilenumbruch. Elemente, die Inhalt haben (also Text oder weitere Elemente, die in ihnen angezeigt werden), brauchen zudem ein zweites Tag, das ihr Ende definiert. Das geschieht durch einen `/` vor dem Tag-Namen: `</h1>`, `</p>` und `</a>`. Elemente, wie der durch `<br>` definierte Zeilenumbruch, die keinen Inhalt haben, brauchen das nicht.

Tags können auch weitere Attribute haben. Z.B. möchte man bei einem Link in der Regel nicht nur definieren, welcher Text angezeigt werden soll, sondern auch eine Adresse angeben, wo der Link hinführt. Das wird im Falle eines Links mit dem `href`-Attribut gemacht, welches man einfach in das Start-Tag miteinfügt: `<a href="https://www.youtube.com/watch?v=WDt24qzK2Ig&t=120s">Text, der angezeigt wird</a>`.
Strings können in HTML - im Gegensatz zu Java - auch mit einfachen Anführungszeichen definiert werden. `<a href='https://www.youtube.com/watch?v=WDt24qzK2Ig&t=120s'>Text, der angezeigt wird</a>` funktioniert genauso. Innerhalb eines Strings kann natürlich auch ein "Fake-Tag" auftauchen, der dann ignoriert wird. Der Link in
```html
<html>
    <body>
        <a href="Kein_gültiger_Link></a></body>">Link-Text</a>
    </body>
</html>
```
führt zwar nirgends hin, das HTML-Dokument kompiliert aber ganz normal. Achte bei deiner Implementierung der folgenden Aufgaben darauf.
Anführungszeichen (ob einfach oder mehrfach) haben in HTML nur innerhalb eines Tags Bedeutung.
```html
<html>
    <body>
        " Text
    </body>
</html>
```
ist wohlgeformt. Du darfst davon ausgehen, dass in unseren Beispielen innerhalb eines Strings in einem Tag keine Leerzeichen vorkommen (auch wenn das normalerweise der Fall sein könnte). Das sollte später das Aufteilen eines Tags erleichtern.

Es gibt auch Kommentare in HTML. Statt mit "<tt style="background-color: #dfdfdf“>//</tt>“ wie in Java werden sie mit "<tt style="background-color: #dfdfdf">\<!-- Hier steht ein Kommentar --></tt>“ markiert. Um HTML-Kommentare brauchst du dich in dieser Aufgabe nicht zu kümmern.

##Die Aufgaben

###1. Eine Seite aus dem Netz holen

Um die Kommunikation mit dem Server, von dem wir die Seiten holen wollen, zu implementieren, wollen wir eine Anfrage an einen fremden Host sowie dessen Antwort je als eine eigene Klasse modellieren. Implementiere also die folgenden beiden Klassen:

[task][HTTPRequest](testHTTPRequestMethodsPublic)
Diese Klasse soll eine Anfrage an einen Host repräsentieren. Da wir nur Seiten holen werden und keine Daten an Hosts verschicken wollen, wird `HTTPRequest` stets einen GET-Request darstellen.
Gib der Klasse ein Attribut `host` für die Domain sowie ein Attribut `path` für den Pfad, auf den zugegriffen werden soll. Für beide Attribute soll es Getter geben. Füge außerdem einen Konstruktor hinzu, der diese beiden Werte entgegennimmt und in die entsprechenden Attribute schreibt.

Zudem soll die Klasse `HTTPRequest` noch eine Methode `HTTPResponse send(int port)` enthalten. Diese soll den durch das Objekt `this` repräsentierten GET-Request über einen neu erstellten `SSLSocket` absenden und aus der erhaltenen Antwort ein neues `HTTPResponse`-Objekt kreieren und dieses zurückgeben.
Achte darauf, dass Requests mit einer Leerzeile enden müssen (es sei denn, sie haben noch einen Body, was unsere Requests allerdings nicht haben).

*Hinweis:*
Beachte nochmals, dass für das Lösen dieser Aufgabe nur Klassen aus den Packages `java.lang`, `java.util`, `java.util.stream`, `java.io`, im JDK mitgelieferte Exception-Klassen und die `SSLSocket`- sowie die `SSLSocketFactory`-Klasse aus dem Package `javax.net.ssl` verwendet werden dürfen. Insbesondere darfst du nicht die Klassen aus `java.net.http` verwenden.

[task][HTTPResponse](testHTTPResponseMethodsPublic)
Diese Klasse soll eine Antwort eines Hosts auf eine `HTTPRequest` darstellen. Dazu soll sie sich in einem Attribut `status` den Status der Response (siehe das im Template mitgelieferte Enum `HTTPStatus`) und in einem weiteren Attribut `html` den "rohen" HTML-Code, der mit der Response geliefert wurde, als `String` abspeichern.
Zu beiden Attributen soll es Getter geben, der Konstruktor soll direkt den Antworttext auf ein GET-Request (also mit den Headern) entgegennehmen und daraus die Werte der eigenen Attribute berechnen.

Du kannst für das HTML-Tag (und nur dieses!) davon ausgehen, dass es in der gesamten Antwort den Text "\<html>" (oder "\<html " + einige Attribute + ">") und den Text "\</html>" je nur einmal gibt und nie innerhalb eines Strings. Dir wird also in unseren Tests kein HTML-Code wie
```
<html>
    <body>
        <a href="</html>">Link</a>
    </body>
</html>
```
vorgesetzt, bei dem ein "Fake-Tag" in einem String versteckt ist.
Achte allerdings darauf, dass das Tag auch in Großbuchstaben ("\<HTML>\</HTML>") ankommen kann und in diesem Falle genauso erkannt werden soll.

###2. Den erhaltenen HTML-Code verarbeiten

Du darfst davon ausgehen, dass sämtlicher in dieser Aufgabe übergebener HTML-Code wohlgeformt ist und keine Kommentare enthält. Genauso darfst du von den Sachen ausgehen, die dir weiter oben für unseren HTML-Code garantiert wurden. Den Methoden, die `List<HTMLToken>`-Objekte entgegennehmen (`HTMLToken` wirst du gleich selbst implementieren), werden wir in den Tests nur solche Token-Listen übergeben, die entweder wohlgeformten HTML-Code repräsentieren oder eine Teilliste einer Liste sind, die wohlgeformten HTML-Code repräsentiert.

[task][HTMLToken](testHTMLTokenMethodsPublic)

Um den vorliegenden "rohen" HTML-Code besser untersuchen zu können, wollen wir ihn erst einmal in logische Einheiten unterteilen. Dafür wählen wir je ein Tag (egal, ob Starttag oder Endtag) als eine logische Einheit und ein Stück Fließtext zwischen zwei (nicht notwendigerweise gleichen) Tags als eine weitere logische Einheit. Der HTML-Code
```
<html>
    <body>
        <h1>Überschrift mit <a href="https://www.youtube.com/watch?v=OJOV1vf1zYk">Link</a></h1>
        Text
        <br>
        Mehr Text
    </body>
</html>
```
würde also in dreizehn solcher logischer Einheiten unterteilt werden, nämlich
```text
Tag: html
Tag: body
Tag: h1
Text: überschrift⎵mit⎵
Tag: a⎵href="https://www.youtube.com/watch?v=OJOV1vf1zYk"
Text: link
Tag: /a
Tag: /h1
Text: text
Tag: br
Text: mehr⎵text
Tag: /body
Tag: /html
```
Genau eine solche logische Einheit soll von der Klasse `HTMLToken` repräsentiert werden. Sie soll sich die Art des Tokens `tokenType` und dessen Inhalt `content` merken. Ersteres soll über ein (in `HTMLToken` intern definiertes) Enum `TokenType` geschehen, das die Werte `TAG` und `TEXT` annehmen kann. `tokenType` soll von diesem Enum-Typen sein. Letzteres soll durch einen `StringBuilder` dargestellt werden, sodass es leicht erweiterbar ist.

Die Klasse `HTMLToken` soll außerdem folgende fünf Methoden enthalten:
- Konstruktor: Nimmt nur den `tokenType` entgegen. Der `content` ist beim Erstellen eines `HTMLToken`-Objektes noch leer.
- Getter für `tokenType`
- `String getContentAsString()`: Gibt das Attribut `content` zurück, aber als `String`
- `void addCharacter(char c)`: Fügt den übergebenen Character hinten an den bisherigen `content` an
- `String toString()`: Gibt "<tt style="background-color: #dfdfdf">Tag:⎵</tt>\<`content` als String>" zurück, falls `this` einen Tag darstellt, "<tt style="background-color: #dfdfdf">Text:⎵</tt>\<`content` als String>", falls es einen Text darstellt.

####HTMLProcessing

In der Klasse `HTMLProcessing` sind die Köpfe von vier statischen Methoden zum Verarbeiten des in der Response mitgesendeten HTML-Codes enthalten. Vervollständige die Rümpfe, sodass die Methoden das hier definierte Verhalten an den Tag legen.

[task][tokenize](testTokenizePublic)
Die Methode `List<HTMLToken> tokenize(String)` soll den übergebenen `String` gemäß den oben beschriebenen Regeln in eine `ArrayList` von `HTMLToken`-Objekten umwandeln. Bei Tags sollen, wie oben gezeigt, die spitzen Klammern nicht mit in den `content` des Tokens aufgenommen werden, alles andere schon. Achte beim Einlesen eines Tags auf Strings, sowohl solche, die mit " beginnen und enden, als auch solche, die mit ' beginnen und enden. Wenn man sich innerhalb eines Strings befindet und auf eine spitze Klammer zu trifft, ist das nicht das Ende des Tags.
<br>
Außerdem solltest du Tag-Namen wie "A", "HTML" und "BODY", Attribut-Bezeichner wie "HREF" und Text auch vollständig in Kleinbuchstaben umwandeln, sodass man später leichter danach suchen kann. Wandle allerdings nicht den gesamten HTML-String in Kleinbuchstaben um, da er auch Komponenten (für uns sind das im Speziellen Links) enthält, bei denen Groß-/Kleinschreibung wichtig ist.

*Hier noch ein Tipp zum Lösen der Aufgabe:*
Man muss den HTML-String nur einmal von vorne bis hinten durchgehen und ihn dabei Buchstabe für Buchstabe ansehen, um ihn in die Token-Liste umzuwandeln. Dabei sollte man sich immer merken, in welchem Zustand man gerade ist: Bin ich in einem Tag? Bin ich in einem String, der mit ' begonnen wurde (also auch damit aufhören muss)? usw... Abhängig von einerseits dem Zustand und andererseits dem aktuell gelesenen Zeichen ergibt sich dann, was man tun muss und ob/wie der Zustand zu ändern ist.
(Das ist nur ein Tipp! Du darfst die Aufgabe natürlich lösen, wie du willst.)

[task][filterLinks](testFilterLinksPublic)
Die Methode `String[] filterLinks(List<HTMLToken>, String)` nimmt eine Liste an `HTMLToken`s und den Namen eines Hosts entgegen. Die Liste an Tokens beschreibt dabei eine HTML-Seite, der Host ist derjenige, von dem die HTML-Seite erhalten wurde. Wenn also von "https://man1.pgdp.sse.in.tum.de/rstart.1.html" die Seite
```html
<HTML>
    <HEAD>
        <TITLE>Man page of RSTART</TITLE>
    </HEAD>
    <BODY>
        <H1>RSTART</H1>
        ...
        <A HREF="https://pgdp.sse.in.tum.de/index.html">Return to Main Contents</A>
        ...
        <A NAME="lbAB">&nbsp;</A>
        ...
        <A HREF="/cgi-bin/man/man2html">man2html</A>
        ...
    </BODY>
</HTML>
```
geholt wurde, wird `filterLinks()` mit der Liste `[Tag: html, Tag: head, Tag: title, Text: man page of rstart, ..., Tag: /html]` (wobei die `HTMLToken`-Objekte hier natürlich durch den Rückgabewert ihrer `toString()`-Methode repräsentiert werden) und dem String "<tt style="background-color: #dfdfdf">man1.pgdp.sse.in.tum.de</tt>“ aufgerufen (vorausgesetzt natürlich `tokenize()` funktioniert wie zuvor beschrieben).

Die Methode soll nun ein Array mit den Adressen aller Seiten (ungleich der aktuellen Seite) erstellen, auf die die aktuelle (durch die `List<HTMLToken>` beschriebene) Seite verlinkt (Auf Selbst-Links wie 127.0.0.1 wird nicht getestet). Die Adresse beinhaltet dabei sowohl Host als auch Pfad, nicht aber das Protokoll. 
Dabei werden dir in unseren Seiten folgende Typen von Links begegnen:

- Das href-Attribut des ersten der drei dargestellten Links verlinkt auf einen anderen Host ("pgdp.sse.in.tum.de" statt "man1.pgdp.sse.in.tum.de"). Die Adressen solcher Links sind stets vollständig mitsamt Protokoll (immer "https://") angegeben. Das Protokoll soll, wie eben gesagt, nicht mit in den Ausgabe-String, alles andere schon.
- Der zweite Link hat gar kein href-Attribut, sondern verlinkt auf einen anderen Abschnitt auf derselben Seite. Solche Links sollen ignoriert werden.
- Das href-Attribut des dritten Links verlinkt auf eine andere Seite auf demselben Host. Bei solchen Links ist nur der Pfad angegeben, nicht Host und Protokoll. Ersterer soll im Ausgabe-String ergänzt werden, das Protokoll aber nicht.

In dem obigen Beispiel sollte also das Array ["<tt style="background-color: #dfdfdf">pgdp.sse.in.tum.de/index.html</tt>“, "<tt style="background-color: #dfdfdf">man1.pgdp.sse.in.tum.de/cgi-bin/man/man2html</tt>“] zurückgegeben werden (plus eventuelle weitere Links, die in den ... stehen könnten).

Du kannst davon ausgehen, dass alle Links, mit denen wir testen, in einer dieser drei Formen ist. Mit allen anderen Links (z.B. `<a href="#index">`) darfst du umgehen, wie du willst/wie es dir sinnvoll erscheint.
Die Links sollen in der gleichen Reihenfolge im zurückgegebenen Array stehen, in der sie auch in der Seite auftauchen. Du musst dir keine Gedanken über mehrfach auftauchende Adressen machen. Das werden wir nicht testen.

Sowohl "a" als auch "href" können im vom Server heruntergeladenen HTML sowohl als Kleinbuchstaben als auch als Großbuchstaben auftauchen. Da du aber bei der Implementierung von `tokenize()` bereits darauf geachtet hast, kannst du jetzt davon ausgehen, dass Tag- und Attribut-Namen nur in Kleinbuchstaben vorliegen. Das Gleiche gilt auch für die nächsten beiden Methoden.

*Hinweis*:
Die Methode `filterLinks()` soll mit Streams und Lambdas implementiert werden. Insbesondere gibt es auf die Lösung dieser Aufgabe nur dann Punkte, wenn keine Kontrollstruktur verwendet wurde. Die Schlüsselwörter `if`, `else`, `for`, `do`, `while`, `switch`, `case`, `break` und `continue` sind innerhalb von `filterLinks()` verboten! Stattdessen soll die Token-Liste sofort in einen Stream umgewandelt und mit Stream-Methoden wie `map()`, `filter()` und `reduce()` weiterverarbeitet werden (es müssen nicht genau diese verwendet werden, das sollen nur Beispiele/Hinweise sein!). Des Weiteren ist auch der ternäre Operator erlaubt. Die Methode muss von folgender Form sein:
```java
public static String[] filterLinks(List<HTMLToken> tokens, String host) {
    return tokens
            .stream()
            ./* usw. */;
}
```
Du darfst außerdem keine eigenen Hilfsmethoden aufrufen/referenzieren, sondern nur die fünf von der Aufgabenstellung für `HTMLToken` geforderten Methoden, sowie die Methoden von Klassen des Packages `java.lang`, die Methode `java.util.Arrays.stream()` und natürlich die Methoden von `java.util.stream.Stream`. Alle anderen Methoden sind verboten. Du darfst auch keine nicht-final statischen Variablen und keine Instanzvariablen (direkt - mittels einer der erlaubten Methoden geht natürlich in Ordnung) referenzieren. `static final`-Variablen, also Konstanten, dürfen referenziert werden, um Code Issues zu vermeiden. Auf die Zustände eines Enums darf auch direkt zugegriffen werden. Die zählen als `static final`.

[task][filterText](testFilterTextPublic)
Die Methode `String filterText(List<HTMLToken>)` soll ähnlich `filterLinks()` die übergebene Token-Liste (die eine HTML-Seite darstellt) untersuchen und Informationen daraus sammeln. Nur soll diesmal der gesamte Text (also alles, was im ursprünglichen HTML-Code nicht innerhalb eines Tags stand) zu einem langen String konkateniert zurückgegeben werden. Füge zudem zwischen zwei durch Tags zuvor getrennte Textstellen stets genau ein Leerzeichen ein. Aus dem HTML
```html
<html>
    <body>
        in sibirien ist es recht kühl<br>schrank und schreibtisch stehen im zimmer
    </body>
</html>
```
soll ein Dokument generiert werden, in dem man nach den Wörtern "kühl" und "schrank", nicht aber dem Wort "kühlschrank" suchen kann. Der Output von `filterText()` sollte hierfür also
<tt style="background-color: #dfdfdf">in⎵sibirien⎵ist⎵es⎵recht⎵kühl⎵schrank⎵und⎵schreibtisch⎵stehen⎵im⎵zimmer</tt> generieren.

*Hinweis*:
Auch die Methode `filterText()` soll mit Streams und Lambdas implementiert werden. Insbesondere gibt es auf die Lösung dieser Aufgabe nur dann Punkte, wenn keine Kontrollstruktur verwendet wurde. Die Schlüsselwörter `if`, `else`, `for`, `do`, `while`, `switch`, `case`, `break` und `continue` sind innerhalb von `filterText()` verboten! Stattdessen soll die Token-Liste sofort in einen Stream umgewandelt und mit Stream-Methoden wie `map()`, `filter()` und `reduce()` weiterverarbeitet werden (es müssen nicht genau diese verwendet werden, das sollen nur Beispiele/Hinweise sein!). Des Weiteren ist auch der ternäre Operator erlaubt. Die Methode muss von folgender Form sein:
```java
public static String filterText(List<HTMLToken> tokens) {
    return tokens
            .stream()
            ./* usw. */;
}
```
Du darfst außerdem keine eigenen Hilfsmethoden aufrufen/referenzieren, sondern nur die fünf von der Aufgabenstellung für `HTMLToken` geforderten Methoden, sowie die Methoden von Klassen des Packages `java.lang`, die Methode `java.util.Arrays.stream()` und natürlich die Methoden von `java.util.stream.Stream`. Alle anderen Methoden sind verboten. Du darfst auch keine nicht-final statischen Variablen und keine Instanzvariablen (direkt - mittels einer der erlaubten Methoden geht natürlich in Ordnung) referenzieren. `static final`-Variablen, also Konstanten, dürfen referenziert werden, um Code Issues zu vermeiden. Auf die Zustände eines Enums darf auch direkt zugegriffen werden. Die zählen als `static final`.

[task][filterTitle](testFilterTitlePublic)
Die Methode `String filterTitle(List<HTMLToken>)` soll aus der übergebenen Token-Liste den Titel eines Dokuments extrahieren. Dieser ist stets der Text, der sich innerhalb des \<title>-Elements befindet, welches Kind des \<head>-Elements ist, welches wiederum direktes Kind des \<html>-Elements (also des Elements auf oberster Ebene ist). Eine typische HTML-Seite mit Titel sieht folgendermaßen aus:
```html
<HTML>
    <HEAD>
        <TITLE>Man page of ISO_8859-1</TITLE>
    </HEAD>
    <BODY>
        ...
    </BODY>
</HTML>
```
Der Text innerhalb des \<title>-Elements soll von der Methode extrahiert werden. Du kannst davon ausgehen, dass es immer nur ein \<title>-Element gibt, dieses sich immer direkt innerhalb des ebenfalls nur einmal vorkommenden \<head>-Elements befindet, welches wiederum direktes Kind von dem obersten Element (\<html>) ist.

*Hinweis*:
`filterTitle()` muss nicht mit Streams und Lambdas implementiert werden. Hier sind wieder alle Schlüsselwörter erlaubt. Du darfst gerne auch diese Methode mit Streams implementieren, sie bietet sich aber dafür bei Weitem nicht so gut an, wie die beiden vorherigen.

###3. Die `LinkedDocumentCollection` mit Seiten aus dem Netz füllen

Nachdem wir nun Code haben, der uns Seiten als HTML aus dem Netz holt und Code, der aus dem erhaltenen HTML Links, Inhalt und Titel extrahiert, können wir uns nun daran machen, für die geholten Seiten je ein neues `LinkedDocument` zu erstellen und in unsere `LinkedDocumentCollection` einzufügen.

1. [task][addToResultCollection updaten](testAddToResultCollectionPublic)
Gehe als erstes in die `LinkedDocumentCollection` und ändere die Methode `addToResultCollection(AbstractLinkedDocument)` so ab, dass sie zusätzlich noch die Outgoing-Links des übergebenen Dokuments als Parameter entgegennimmt. Die Signatur sollte also zu `addToResultCollection(AbstractLinkedDocument, String[])` geändert werden. Im Methodenrumpf sollen nun beim Updaten der Incoming-Links diese nicht länger aus dem `content` des übergebenen `AbstractLinkedDocument` ausgelesen werden (das war nur eine Zwischenlösung für die letzten Wochen), sondern es sollen die übergebenen Outgoing-Links verwendet werden, um zu bestimmen, für welche Dokumente das übergebene `AbstractLinkedDocument` zu den Incoming-Links hinzugefügt werden muss bzw. welche Dummies neu erstellt werden müssen.

2. [task][Eine Seite Crawlen](testCrawlPagePublic)
In der Klasse `PageCrawling` im `networking`-Package findest du eine (noch leere) Methode `crawlPage(LinkedDocumentCollection, String)`. Diese soll die Seite an der übergebenen Adresse (im Format Host/Pfad, z.B. "man7.pgdp.sse.in.tum.de/iso\_8859\_1.7.html") holen, daraus mithilfe der zuvor in `HTMLProcessing` implementierten Methoden ein `LinkedDocument`-Objekt erstellen und dieses in die `LinkedDocumentCollection` einfügen. Die Konstruktorparameter `description`, `releaseDate`, `author` und `collectionSize` von `LinkedDocument` dürfen beliebig gesetzt werden, wobei letzeres logischerweise größer null sein sollte. Die Links sollen alle korrekt in die Collection übertragen werden. Achte also darauf, die richtige Methode zum Einfügen in die Collection zu verwenden und ihr die richtigen Parameter zu übergeben.
<br>
Beim Holen der Seite von der jeweiligen Adresse solltest du die Nachricht an Port 443 senden (das ist der Standard-Port für HTTPS-Verbindungen).
<br>
Wenn der Zugriff auf die übergebene Adresse nicht möglich war (also ein Statuscode ungleich 200 zurückkam) soll nichts in die Collection eingefügt werden und `false` zurückgegeben werden. Wenn der Zugriff erfolgreich war, soll `true` zurückgegeben werden.

3. [task][Eine noch nicht gecrawlte Adresse aus der LinkedDocumentCollection holen](testGetNextUncrawledAddressPublic)
Jetzt können wir eine einzelne Seite für eine vorgegebene Adresse holen. Wir wollen aber viele Seiten aus dem Netz holen und in unser Page Repository eintragen können. Wir könnten einfach für jede geholte Seite deren Outgoing-Links direkt verwenden, um neue Seiten zu finden. Allerdings führt das zu dem Problem, dass Seiten einander im Kreis verlinken können und man dann ewig lange die gleichen Seiten durchsucht. Um das zu verhindern, wollen wir in der `LinkedDocumentCollection` nachsehen, um nur solche Seiten zu holen, die dort noch nicht eingetragen sind. Glücklicherweise bietet uns diese dafür genau die richtige Struktur. Wir haben `LinkedDocument`s in ihr, die Seiten repräsentieren, die bereits gecrawlt wurden und `DummyLinkedDocument`s für Seiten, von deren Existenz wir wissen bzw. deren Adresse wir kennen, deren Inhalt wird aber noch nicht heruntergeladen haben.
<br><br>
Implementiere also in `LinkedDocumentCollection` eine neue Methode `String getNextUncrawledAddress()`, die die Collection nach einem `AbstractLinkedDocument` durchsucht, das noch nicht gecrawlt wurde. Welches derartige `AbstactLinkedDocument` hier genau zurückgegeben wird, spielt keine Rolle, solange es noch nicht gecrawlt wurde und in der Collection ist. Die Adresse dieses Dokuments soll zurückgegeben werden. Wenn bereits alle Dokumente in der Collection gecrawlt wurden, soll `null` zurückgegeben werden.

4. [task][Viele Seiten Crawlen](testCrawlPagesPublic)
Implementiere nun die Methoden `void crawlPages(LinkedDocumentCollection collection, int number, String startingAddress)` und `void crawlPages(LinkedDocumentCollection collection, int number)` in der Klasse `PageCrawling`.
Erstere soll `number` Seiten crawlen und in die `collection` einfügen. Die erste gecrawlte Seite soll dabei die in `startingAddress` sein. Alle weiteren sollen mittels `getNextUncrawledAddress()` ermittelt werden. Wenn eine der dabei gecrawlten Adressen nicht erreichbar ist (also von `crawlPage()` `false` zurückgegeben wurde), soll das entsprechende Dummy-Dokument aus der Collection entfernt werden. Diese Adresse zählt dann **nicht** zu der `number` der zu crawlenden Seiten dazu.
Zweitere Methode soll das gleiche tun, nur dass die erste gecrawlte Seite ebenfalls mittels `getNextUncrawledAddress()` ermittelt werden soll.

##Testing

Teste diese Woche die vier Methoden in `HTMLProcessing`. Teste jede mit mindestens drei verschiedenartigen Eingaben. Schreibe für jede getestete Methode, wie immer, einen Kommentar, der erklärt, warum du die verschiedenen Eingaben so gewählt hast, wie du sie gewählt hast.

[Lösungsvorschlag](https://bitbucket.ase.in.tum.de/projects/PGDP2122W10H01/repos/pgdp2122w10h01-solution/browse)<br>
[Tests](https://bitbucket.ase.in.tum.de/projects/PGDP2122W10H01/repos/pgdp2122w10h01-tests/browse)

# FAQ
### Q: Wofür ist das FAQ da?
A: Wenn es Fragen gibt, die häufiger aufkommen, werden sie hier gepostet und ebenfalls beantwortet werden. Wer sie danach noch auf Zulip postet macht Pinguine traurig!
### Q: Die SSLSocketFactory gibt nur einen normalen Socket zurück, den ich aber nicht benutzen darf. Was soll ich machen?
A: Wenn ihr die Factory richtig konfiguriert (den host und port passend übergebt und den rest auf den Defaulteinstellungen lasst), könnt ihr den zurückgegebenen Socket in einen SSLSocket casten. Ein Blick in die [Dokumentation](https://download.java.net/java/early_access/panama/docs/api/java.base/javax/net/ssl/SSLSocketFactory.html) kann dabei helfen.
### Q: Wird in den Tests HTML-Code verwendet, in dem ein \<a>-Tag mehrere href-Attribute hat?
A: Nein. Du darfst davon ausgehen, dass es nur ein href-Attribut pro \<a>-Tag gibt.
### Q: Ich bekomme beim Senden eines Requests einen "400 Bad Request", obwohl ich meine, dass ich alles (inklusive der notwendigen Leerzeile am Ende des Requests) korrekt verschicke. Ich arbeite auf einem Mac. Was könnte das Problem sein?
A: Probiere, statt `.println(x)` `.print(x + "\r\n")` zu verwenden.
### Q: Wie können die Strings aufgebaut sein, bzw. was werdet ihr da testen?
A: Es kann in den Strings alles vorkommen, was nicht ausgeschlossen wurde. Zudem wird es nicht ```"\""``` geben. ```"'"``` kann aber z.B. vorkommen (einfaches Anführungszeichen in einem String der mit doppelten Anführungszeichen markiert ist).
### Q: Muss ich in tokenize() Zeilenumbrüche, Tabs und Leerzeichen (die z.B. zur Formatierung des Codes enthalten sind, wie das in den in der Angabe aufgeführten Beispielen der Fall ist) entfernen?
Die HTML-Beispiele in der Angabe sind der Leserlichkeit halber formatiert, enthalten also zusätzliche Leerzeichen, Tabs und Zeilenumbrüche. Du musst dich **nicht** darum kümmern, diese zu entfernen. Wenn du es allerdings doch tust (wie in dem Beispiel, das die Tokens erklärt, geschehen), ist das genauso in Ordnung. Wir werden so testen, dass beim Inhalt von Text-Tokens Sequenzen von mehreren Leerzeichen, Tabs und Zeilenumbrüchen immer so wie genau ein Leerzeichen behandelt werden. Außerdem werden wir `tokenize()` auch nicht mit HTML-Code testen, in dem zwischen zwei Tags nur Leerzeichen, Tabs und Zeilenumbrüche vorkommen, sodass unklar wäre, ob dazwischen noch ein Text-Token mit besagten Leerzeichen, Tabs und Zeilenumbrüchen eingefügt werden soll.
Code wie
```html
<html>
    <body>
        Some Text
    </body>
</html>
```
(als String: "\<html>\n⎵⎵⎵⎵\<body>\n⎵⎵⎵⎵⎵⎵⎵⎵Some Text\n⎵⎵⎵⎵\<body>\n\<\html>") wird deiner Methode also nicht übergeben werden, sondern nur
"\<html>\<body>Some Text\</body>\</html>", bei dem klar ist, was zu tun ist.