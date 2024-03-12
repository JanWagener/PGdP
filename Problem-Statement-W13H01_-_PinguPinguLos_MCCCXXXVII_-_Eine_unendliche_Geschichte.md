# PinguPinguLos MCCCXXXVII - Eine unendliche Geschichte ?

Für die letzte (versprochen: die letzte) Suchmaschinenaufgabe wollen wir uns zu unserem bisherigen Projekt noch eine kleine GUI entwerfen,
um leichter damit interagieren zu können. Diese soll Suchanfragen entgegennehmen können, die mit den in den vorigen Wochen und Monaten implementierten Methoden berechneten Suchergebnisse anzeigen können und letztlich noch eine "Admin Ansicht" haben, in der man das Page-Repository leicht mit Seiten aus dem Internet aufstocken kann.

Wir werden dabei versuchen, die Präsentation (View) der Daten, das dem zugrunde liegende Modell (Model) und die Steuerung der beiden (Controller) so gut es geht voneinander zu trennen, um am Ende übersichtlichen GUI-Code zu erhalten. Die zu implementierenden (bzw. zu ergänzenden) Klassen sind bereits im Template vorgegeben und in drei entsprechende Packages einsortiert. Deine Hauptaufgabe wird es sein, die Views zu vervollständigen. Außerdem wirst du einige Methoden in den Controllern füllen müssen. Die Modelle sind bereits vollständig implementiert, genauso wie die Klasse `StartGUI`, die die GUI aus den Einzelteilen zusammenbaut und startet.

**Hinweis: Diese Woche werden keine Style Issues bewertet und ihr müsst keine Tests schreiben!**

## Views

Im Package `view` findest du zehn Klassen, die unterschiedliche Komponenten der GUI repräsentieren. Sechs davon musst du noch ergänzen. Dir wird im Folgenden jeweils beschrieben, wie die View aussehen soll und welche Aktionen in ihr welche Events triggern. Wie du das jeweils genau umsetzt, ist dir überlassen. Achte darauf, geeignete Layout-Manager zu verwenden, um das Aussehen der View dem von uns beschriebenen / in Bildern gezeigten anzupassen. Wir verlangen nicht, dass du unsere Views pixelgenau reproduzieren kannst, allerdings ist uns wichtig, *dass* du auf Layouting achtest und deine Komponenten nicht einfach kreuz und quer über die View verteilen lässt.
Wenn wir also zwei Buttons nebeneinander darstellen bzw. das im Text verlangen, dann sollten sie bei dir auch nebeneinander sein.
Wenn wir im Text beschreiben, dass sich eine Komponente dynamisch an die Fensterbreite anpasst, dann sollte sie das bei dir auch tun.
Teils geben wir Tipps, welcher Layout-Manager oder welche Komponente an einer bestimmten Stelle hilfreich sein könnte. Es ist dir überlassen, ob du diese Tipps verwendest oder nicht.

### SearchEngineView

Die Klasse `SearchEngineView` beschreibt das Hauptfenster als Ganzes. In ihr soll oben eine Leiste (= ein Objekt der Klasse `TopBar`) angezeigt werden, welche etwas Text und einige Buttons enthält. Das Attribut `topBar` verweist auf diese Leiste. Darunter soll sich das `JPanel body` befinden, welches je nach Zustand des Programms (was damit gemeint ist, wird hoffentlich im Laufe der Aufgabe klar) entweder ein Objekt der Klasse `AdminView` (also das Attribut `adminView`) oder ein Objekt der Klasse `ResultView` (also das Attribut `resultView`) oder ein Objekt der Klasse `SearchView` (also das Attribut `searchView`) anzeigt.

Deine Aufgabe ist es hier, die Methode `init(SearchEngineController, AdminView, ResultView, SearchView)` zu implementieren, welche
 1. `topBar` mit einem neuen Objekt vom Typen `TopBar` belegt und an den oberen Fensterrand heftet.
 2. `body` mit einem neuen `JPanel` initialisiert und in den Hauptteil legt.
 3. die vier übergebenen Parameter in die vier entsprechenden Attribute von `SearchEngineView` schreibt und
 4. die letzten drei dem `JPanel body` wie oben beschrieben hinzufügt, wobei die `SearchView` als erstes angezeigt werden soll.

Zudem musst du noch die drei Methoden `displayAdminView()`, `displayResultView()` und `displaySearchView()` implementieren, welche im `body` die entsprechende View anzeigen. Achte in diesen Methoden darauf, dass auch die `topBar` bzgl. sowohl ihres Titels als auch der in ihr angezeigten Buttons angepasst werden muss:
 - In der `AdminView` soll der Titel "Admin View" lauten und der Crawl-Button und der Back-to-Search-Button angezeigt werden.
 - In der `ResultView` soll der Titel "Search Results" lauten und der Admin-View-Button sowie der Back-to-Search-Button angezeigt werden.
 - In der `SearchView` soll der Titel "Search" lauten und der Admin-View-Button angezeigt werden.

Der Exit-Button soll in allen drei Views angezeigt werden.

*Tipp:*
Will man mehrere Komponenten in ein Panel einfügen und dabei aber nur eines auf einmal anzeigen (welches dann die volle Größe des Panels ausfüllt), ist es hilfreich, ein `CardLayout` als Layout-Manager zu verwenden.

Hier ein Bild zur Erklärung der verschiedenen Komponenten der `SearchEngineView`. Am Anfang wird im `body` die `searchView` angezeigt:
<br>
<br>
<img src="https://syncandshare.lrz.de/dl/fi7VcdTDzozsHrPweWUJjRP3/PPLSearchEngineViewSearchDisplayed.png?inline" alt="Umrechnung" width="1000px" >
<br>
<br>
Nachdem man auf den Admin-View-Button gedrückt hat, wird im `body` stattdessen die `adminView` angezeigt. Die `topBar` wird dabei separat auch angepasst. Danach sieht das Bild dann also so aus:
<br>
<br>
<img src="https://syncandshare.lrz.de/dl/fi5p4zwErQzmTPHL2KtHDETj/PPLSearchEngineViewAdminDisplayed.png?inline" alt="Umrechnung" width="1000px" >
<br>
<br>
Die `SearchEngineView` beschreibt dabei jeweils das gesamte Fenster

### AdminView

Die `AdminView` stellt die View dar, in der man alle derzeit in der aktuellen `LinkedDocumentCollection` befindlichen Dokumente in einer Übersicht sieht und neue crawlen kann. Dabei besteht die View einerseits aus einer Komponente, die untereinander nach ID sortiert alle (bzw. alle bisher geladenen) Dokumente als `AbstractDocumentPane`-Objekte darstellt. Dabei wird ein `LinkedDocument`-Objekt mittels einer `DocumentPane` und ein `DummyLinkedDocument`-Objekt mittels einer `DummyDocumentPane` dargestellt.
Andererseits soll sich (ganz unten unter allen `AbstractDocumentPane`s) ein Button mit der Aufschrift "Load More" befinden, welcher zehn weitere Dokumente (sofern noch welche übrig sind) als `AbstractDocumentPane`s in die View lädt und unten an die bisherigen anfügt (aber über dem Button).
(Das geht mittels eines Calls einer geeigneten Methode in `adminController`.)

Du wirst der `AdminView` einige Komponenten geben müssen. Außerdem sollst du folgende Methoden implementieren:
 - `AdminView()`:
    Erzeugt eine Komponente, welche die `AbstractDocumentPane`s aufnehmen kann sowie die Load-More-Button.
 - `addDocumentPane(AbstractDocumentPane)`:
    Fügt das übergebene `AbstractDocumentPane`-Objekt unten an die bereits vorhandenen an und updatet dann die Anzeige (mit einem Call der Methode `updateUI()`).
 - `clear()`:
    Löscht alle angezeigten `AbstractDocumentPane`-Objekte aus der View (nicht aber den Load-More-Button).

Die angezeigten `AbstractDocumentPane`s sollten alle immer (auch nach Skalieren des Fensters) mittig angeordnet und gleich breit sein.
Die `AdminView` soll von `JScrollPane` erben und bei Bedarf eine vertikale Scrollbar erzeugen, um alle `ResultPane`s und den Button anzeigen zu können.

Die Admin View erklärt:
<br>
<br>
<img src="https://syncandshare.lrz.de/dl/fiJkLzMVmEXvZn9Zx7HY5Bh1/PPLAdminViewAnnotated.PNG?inline" alt="Umrechnung" width="1000px" >
<br>
<br>
Wenn zu viele Elemente hinzugefügt werden, wird (durch das Erben von `JScrollPane` - du musst das nur noch dem `super()`-Konstruktor mitteilen) automatisch eine Scroll-Bar erzeugt:
<br>
<br>
<img src="https://syncandshare.lrz.de/dl/fiPiadmGBgEACdnabTEFtDPL/PPLAdminViewScroll.PNG?inline" alt="Umrechnung" width="600px" >
<br>
<br>

### ResultView

Die `ResultView` stellt die View dar, in der die Ergebnisse einer Suchanfrage nach Relevanz sortiert aufgelistet werden. Ein einzelnes Ergebnis wird dabei als `ResultPane`-Objekt dargestellt. Die `ResultView` ist sehr ähnlich der `AdminView` organisiert, indes sie aus einer Komponente, welche beliebig viele `ResultPane`s untereinander, mittig und gleich breit anzeigen kann, und darunter noch einem Load-More-Button besteht.

Auch hier sollen einige Methoden implementiert werden:
 - `ResultView()`:
    Erzeugt, wie in `AdminView`, die Komponente, welche die `ResultPane`s aufnimmt, sowie den Load-More-Button.
 - `addResultPane(ResultPane)`:
    Fügt die übergebene `ResultPane` der Komponente, welche die `ResultPane`s aufnimmt, unten an.
 - `clear()`:
    Löscht alle angezeigten `ResultPane`-Objekte aus der View (nicht aber den Load-More-Button).

Auch `ResultView` soll von `JScrollPane` erben und bei Bedarf eine vertikale Scrollbar erzeugen, um alle `ResultPane`s und den Button anzeigen zu können.

Am Ende sollte die Result View in etwa folgendermaßen aussehen:
<br>
<br>
<img src="https://syncandshare.lrz.de/dl/fiVKKs748LSRHfbiLhuK64bF/PPLResultViewAnnotated.PNG?inline" alt="Umrechnung" width="1000px" >
<br>
<br>

### SearchView

Die `SearchView` stellt die View dar, in der man einen Suchbegriff eingeben und die Suche dann abschicken kann. Hier müssen nur der Konstruktor implementiert (und evtl. einige Attribute hinzugefügt) werden. `SearchView` soll, wie im Bild gezeigt, den Text "PinguPinguLos" in fetter Schrift einer großen Schriftgröße (z.B. 36) über dem Textfeld für den Suchbegriff und dem Search-Button anzeigen.
Letztere beide sollen nebeneinander plaziert sein: Das Textfeld links, der Button rechts. Der Text "PinguPinguLos" soll mittig über beiden platziert werden, wie im Bild zu sehen ist.

Hier ein Bild, wie die Search View aussehen könnte:
<br>
<br>
<img src="https://syncandshare.lrz.de/dl/fiGqfEsMuDqzPoyBQfmpfF68/PPLSearchView.PNG?inline" alt="Umrechnung" width="600px" >
<br>
<br>

### TopBar

Die `TopBar` stellt die Leiste mit Titel und Knöpfen oben im Hauptfenster dar. Sie enthält fünf (bereits im Template vorgegebene) Attribute:
 - `title`: Ein Label, das den Titel enthält (wie in `SearchEngineView` beschrieben)
 - `crawlButton`: Ein Button, auf dessen Druck hin ein `CrawlDialog` aufgehen soll
 - `toAdminViewButton`: Ein Button, der zur Admin View wechselt
 - `toSearchViewButton`: Ein Button, der zur Search View wechselt
 - `exitButton`: Ein Button, auf dessen Druck hin das Programm terminieren soll

Methoden, die den Titel setzen bzw. die Buttons sichtbar/unsichtbar machen, sind bereits vorhanden.
Du musst nur noch den Konstruktor implementieren, der die fünf Komponenten zusammensetzt. Sie sollen sich dabei alle in einer Zeile befinden, wobei der Titel linksbündig und die Buttons rechtsbündig sind. Die Buttons werden in der Reihenfolge `crawlButton`, `toAdminViewButton`, `toSearchViewButton`, `exitButton` (von links nach rechts) angezeigt, die unsichtbaren natürlich jeweils ausgenommen.
Zwischen dem Titel und den Buttons soll Platz sein, sonst nirgends (außer ein Paar Pixeln für die Ästhetik, wenn du willst). Das soll auch nach Skalieren des Fensters so bleiben.

*Tipp:*
Um auf die Knopfdrücke hin das richtige Verhalten auszulösen, überlege dir, wie du die Methoden in `SearchEngineController` verwenden kannst.

Hier sind die verschiedenen Teile einer `TopBar` nochmals mittels eines Bildes erklärt:
<br>
<br>
<img src="https://syncandshare.lrz.de/dl/fiGfxoh5egFtfW9wZaGS2613/PPLTopBarExplained.PNG?inline" alt="Umrechnung" width="1000px" >
<br>
<br>
So sehen die `TopBar`s für die verschiedenen Views aus:
<br>
<br>
**Admin View**:
<br>
<br>
<img src="https://syncandshare.lrz.de/dl/fi3eQKBK2oaKRtSYEL5TKEDa/PPLTopBarAdminView.PNG?inline" alt="Umrechnung" width="800px" >
<br>
<br>
**Result View**:
<br>
<br>
<img src="https://syncandshare.lrz.de/dl/fiBwt1mx6PKhH2DA9UwNZZwk/PPLTopBarResultView.PNG?inline" alt="Umrechnung" width="800px" >
<br>
<br>
**Search View**:
<br>
<br>
<img src="https://syncandshare.lrz.de/dl/fiCWbxRpcb1ugQd5j8GeGD2Y/PPLTopBarSearchView.PNG?inline" alt="Umrechnung" width="800px" >
<br>
<br>


### CrawlDialog

Der `CrawlDialog` poppt auf, wenn man auf den (nur in der `AdminView` sichtbaren) Crawl-Button in der Top-Bar drückt. In ihm wird der Nutzer nach einer Zahl (einer Anzahl) und einem String (einer Adresse) gefragt. Dies soll in zwei Feldern untereinander, die oben links das Label "Amount" respektive "Address" tragen, eingegeben werden können. Die Zahl soll dabei zwischen 1 und 10 liegen und ganzzahlig sein.

Unten rechts sollen (von links nach rechts in dieser Reihenfolge) zwei Buttons nebeneinander platziert sein:
 - Cancel: Dieser Button soll mit "Cancel" beschriftet sein und auf Druck den Dialog schließen, ohne dass sonst etwas geschieht.
 - Crawl: Dieser Button soll mit "Crawl" beschriftet sein und auf Druck den Dialog schließen, aber dabei auch auslösen, dass von der eingegebenen
    Adresse aus die eingegebene Zahl an Seiten gecrawlt wird und die in der Admin View gerade angezeigten Dokumente aktualisiert werden (suche hier in `AdminController` nach der passenden Methode).

Der Dialog soll dann in etwa folgendermaßen aussehen:
<br>
<br>
<img src="https://syncandshare.lrz.de/dl/fiP5xMsQi2BmEmRax69GduBF/PPLCrawlDialogExplained.PNG?inline" alt="Umrechnung" width="800px" >
<br>
<br>

### AbstractDocumentPane, DocumentPane, DummyDocumentPane und ResultPane

Diese vier Klassen repräsentieren die Anzeige eines einzelnen Dokumentes (die ersten drei in der Admin View, das Letzte in der Result View). Sie sind bereits vollständig implementiert. Du musst hier nichts weiter hinzufügen (darfst aber natürlich, wenn du willst). Sieh sie dir dennoch an, da du ihre Funktionalität wirst verwenden müssen.



## Controller

Im Package `controller` findest du vier Klassen: `SearchEngineController`, `AdminController`, `ResultController` und `SearchController`. Diese steuern das Verhalten der vier entsprechenden Views. Vervollständige die zehn noch nicht implementierten Methoden in ihnen wie folgt:

### SearchEngineController

 - `processQuery(String)`:
    Arbeitet die übergebene Suchanfrage ab. Teilt dabei dem Result Controller mit, die entsprechenden Ergebnisse zu laden und wechselt danach dann die aktuell angezeigte View von der Search View zur Result View.
 - `crawlButtonPressed()`:
    Erstellt einen neuen `CrawlDialog` und macht ihn sichtbar.

### AdminController

 - `loadDocuments()`:
    Teilt dem `adminModel` mit, dass es alle Dokumente neu laden soll
    und lädt dann das erste Batch (`AdminModel.BATCH_SIZE` Stück) in die Admin View.
 - `loadNextBatch()`:
    Lädt das nächste Batch (die nächsten `AdminModel.BATCH_SIZE` Stück) an Dokumenten in die Admin View.
 - `loadIntoView(List<AbstractLinkedDocument>)`:
    Lädt alle Dokumente in der übergebenen Liste in die Admin View, indem es für jeden davon
    die korrekte `AbstractDocumentPane` erstellt und diese dann in die Admin View einfügt.
 - `crawlButtonPressedForAddress(String)`:
    Crawlt diese Adresse und lädt dann die Admin View neu, da sich die Dokumente ja (eventuell) geändert haben.
    Es sollen genauso viele Dokumente wieder in die Admin View geladen werden, wie davor angezeigt wurden,
    also `adminModel.numberOfLoadedDocuments` Stück.
 - `crawlFromAddress(int, String)`:
    Crawlt von der Adresse `address` aus `amount` Seiten und lädt dann die Dokumente neu. (Wie viele ist egal,
    nur nicht keine.)

### ResultController

 - `loadResultsFor(String)`:
    Löscht alle derzeit in der `resultView` angezeigten `ResultPane`-Objekte,
    lässt das `resultModel` das Ergebnis der Suchanfrage mit dem übergebenen String berechnen
    und lädt letztlich das erste Batch an Ergebnissen (`ResultModel.BATCH_SIZE` Stück) in die Result View.
 - `loadNextBatch()`:
    Lädt das nächste Batch an Ergebnissen (`ResultModel.BATCH_SIZE` Stück) in die Result View.
    Erstellt dabei für jedes dieser Ergebnisse ein geeignetes `ResultPane`-Objekt und fügt es
    dann in die Result View ein.

### SearchController

 - `executeSearch(String)`:
    Teilt dem `mainController` mit, dass eine Suche mit dem übergebenen String ausgeführt werden soll.

## Models

Im Package `model` befinden sich die beiden Klassen `AdminModel` und `ResultModel`. Sie repräsentieren den aktuellen Zustand der Admin View bzw. Result View, also welche Dokumente angezeigt werden können (= alle Dokumente nach ID sortiert im Admin Model und alle Dokumente nach Relevanz bzgl. der letzten Suchanfrage sortiert im Result Model), wieviele davon gerade angezeigt werden, eine Referenz auf die `LinkedDocumentCollection` und Funktionalitäten zum Manipulieren dieses Zustands.

In diesem Package musst du nichts implementieren. Auch hier ist es aber angeraten, dir beide Klassen anzusehen, da du einige ihrer Funktionalitäten wirst verwenden müssen.



## StartGUI

Die Methode `startGUI()` in der Klasse `StartGUI` stellt die GUI zusammen und macht sie sichtbar. Du musst hier nichts ändern. Führe die `main()`-Methode aus, um deinen Code zu testen.



## Weitere Hinweise
 - Unit Tests machen für GUIs nicht viel Sinn. Daher muss diese Woche nicht getestet werden.
 - Das gilt auch von unserer Seite her. Diese Woche wird nicht automatisiert getestet. Erschrecke also nicht, wenn dir die Tests nach der Deadline erstmal 0 Punkte eintragen. Wenn deine GUI funktioniert, wird ein Tutor dir die Punkte geben.
 - Diese Woche sind die Style Issues ausgeschaltet.
 - Wenn du die GUI etwas ästhetisch ansprechender gestalten willst, als die Musterlösung das tut, sehen wir das natürlich gerne. Achte nur darauf, dass die verlangten Komponenten in den Beziehungen zueinander wie im Text beschrieben und/oder in den Bilder gezeigt (nebeneinander vs. untereinander, mit Platz dazwischen vs. ohne Platz dazwischen) erfüllt sind, dann sind dir sonst alle Freiheiten gelassen, noch Dinge hinzuzufügen.
 
 [Musterlösung](https://bitbucket.ase.in.tum.de/projects/PGDP2122W13H01/repos/pgdp2122w13h01-solution/browse)