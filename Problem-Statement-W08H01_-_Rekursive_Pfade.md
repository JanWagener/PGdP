#Rekursive Pfade

In dieser Aufgabe sollst du einen Algorithmus implementieren, welcher einen Pfad durch ein Labyrinth findet.

##Das Template

Im Template findest du 5 Klassen (von denen du in zweien noch selbst Dinge implementieren musst), sowie ein [Enum](https://docs.oracle.com/javase/tutorial/java/javaOO/enum.html).
Dies umfasst eine Klasse `Maze`, welche ein Labyrinth darstellt, eine Klasse `Position`, die eine Position (in einem Labyrinth z.B.) darstellt, also ein Paar ganzzahliger Koordinaten, eine Klasse `MazeParser`, welche eine statische Methode `parseFromFile(String file)` enthält, um ein Labyrinth aus einer Datei einzulesen, damit du später leicht verschiedene Labyrinthe "eingeben" kannst, indem du sie einfach in eine .txt-Datei schreibst, ein Enum `Direction`, welches die vier Richtungen `UP`, `DOWN`, `LEFT` und `RIGHT` darstellt, sowie die beiden Klassen `Path` und `MazeSolver`, die du selbst implementieren sollst.

Im Folgenden werden die drei bereits implementierten Klassen und die Enum-Klasse vorgestellt und dabei hervorgehoben, welche Funktionalitäten für dich hier interessant sind. Wie die Implementierungen genau funktionieren, ist für dich nicht relevant. Du musst dir nur darüber Gedanken machen, was sie nach außen hin an Funktionalitäten zur Verfügung stellen, um diese dann in deinem Code verwenden zu können. **Ändere an diesen Klassen nichts!**

###Maze

Die Klasse `Maze` repräsentiert ein Labyrinth, also ein 2D-Grid von Tiles. Ein Tile kann dabei eine Wand (`WALL`), ein freies Feld (`SPACE`) oder ein Feld mit einer Markierung (`MARKED`) sein. Außerdem hat ein Labyrinth einen Eingang und einen Ausgang. Diese können irgendwo im Labyrinth sein **(müssen also insbesondere nicht an dessen Rand liegen)**, du kannst aber davon ausgehen, dass der `exit` immer vom `entrance` aus erreichbar ist. Du kannst außerdem davon ausgehen, dass der Eingang und der Ausgang nie auf einem `WALL`-Tile liegen.

In diesem 2D-Grid repräsentiert die erste Koordinate jeweils die Zeile, die zweite Koordinate die Spalte des jeweiligen Tiles. Es wird, wie immer, in beide Dimensionen von 0 an gezählt.

Für dich interessante Methoden sind
- `Position getEntrance()` und `Position getExit()`:
    Selbsterklärend.
- `boolean isEmptyTile(Position position)`:
    Gibt `true` zurück gdw.
     1. die übergebene Position innerhalb der Grenzen des Labyrinths `this` liegt
     2. das Tile an der übergebenen Position ein `SPACE` ist (also weder `WALL` noch `MARKED`)
- `void mark(Position position)`:
    Wenn die übergebene Position (noch) leer ist (nach `isEmptyTile()`), setzt diese Methode das Tile auf `MARKED`

sowie
- `String toString()` und `String toString(Path solution)`:
    Geben eine String-Repräsentation des Labyrinths `this` zurück. Dabei steht
     - W für eine `WALL`
     - ' für ein `MARKED` Tile
     - E für den `entrance`
     - X für den `exit`
     - ein Leerzeichen für einen `SPACE`
    
    und im Falle der zweiten Variante ein * für ein Feld auf dem Pfad `solution`, wenn man ihn vom `entrance` aus abläuft.
    Dabei überschreibt ein E ein X, ein X ein * und ein * ein W, ' oder Leerzeichen.

zum Testen.

Ein Beispiel für eine String-Repräsentation eines ungelösten Labyrinths ist
<pre>
WWWWEWWWW
W     W W
W WWW W W
W W     W
W WWWW WW
W  W    W
WWWW WWWW
W       W
WWWWXWWWW
</pre>

Ein Beispiel für eine String-Repräsentation eines gelösten Labyrinths ist
<pre>
WWWWEWWWW
W'''**W W
W'WWW*W W
W'W''** W
W'WWWW*WW
W''W*** W
WWWW*WWWW
W   *   W
WWWWXWWWW
</pre>

*Hinweis*:
Deine Lösung muss nicht genauso aussehen, wie diese hier. Wenn du die Abzweigungen des Labyrinths in einer anderen Reihenfolge erkundest,
ist das in Ordnung.

Die interne Darstellung des Grids des Labyrinths mit String-Repräsentation
<pre>
WEWWW
W   W
W W W
W   W
WWWXW
</pre>
sieht folgendermaßen aus:
<br>
<br>
<img src="https://syncandshare.lrz.de/dl/fiQ5A8zX365Zyjwk1XASVjw3/Maze_Darstellung.png?inline" alt="Umrechnung" width="800px" >
<br>
<br>
**UML Diagramm für `Maze`:**

@startuml

class Maze {
	- tiles: TileState[][]
	- entrance: Position
	- exit: Position
	+ isEmptyTile(Position): boolean
	+ mark(Position): void
	+ toString(): String
	+ toString(Path): String
}
@enduml

###Position

Die Klasse `Position` stellt ein ganzzahliges Koordinatenpaar dar. Ein Koordinatenpaar hat eine `i`- und eine `j`-Komponente für die Zeile respektive Spalte. Die Klasse hat eine Methode `Position getPositionOneTile(Direction direction)`, die ein neues `Position`-Objekt einen Schritt in die übergebene Richtung von `this` aus zurückgibt.
Für ein `Position`-Objekt `pos` mit Koordinaten $$(4, 6)$$ wird beim Aufruf `pos.getPositionOneTile(Direction.LEFT)` also z.B. ein `Position`-Objekt mit Koordinaten $$(4, 5)$$ zurückgegeben.

###Direction

Die Enum-Klasse `Direction` stellt die vier möglichen Richtungen dar, in die man sich im Labyrinth (potentiell, wenn man nicht durch `WALL`s blockiert wird) bewegen kann: `UP`, `DOWN`, `LEFT` und `RIGHT`.

###MazeParser

Die Klasse `MazeParser` stellt eine statische Methode `static Maze parseFromFile(String filePath)` zur Verfügung. Diese liest die Datei am übergebenen Dateipfad ein und erzeugt daraus, wenn möglich, ein `Maze`-Objekt. Der Dateipfad wird dabei in Relation zum Root-Verzeichnis des ganzen Projektes interpretiert. Die mitgelieferte Datei *Maze.txt* im *resources*-Ordner wird also beispielsweise eingelesen und geparst, wenn man der Methode `parseFromFile()` den Pfad *resources/Maze.txt* übergibt.
Diese Methode muss nicht verwendet werden. Sie ist lediglich dazu da, verschiedene Labyrinthe leichter eingeben zu können. Sie ist also nützlich, um die eigene Implementierung zu testen.

Diese Methode verwendet einige Konzepte, die du im Rahmen von Info I und dem PGdP in diesem Semester noch nicht kennengelernt hast. Wie oben gesagt, du musst dir über die Implementierung von `parseFromFile()` keine Gedanken machen. Du kannst sie verwenden, um deinen Code leichter testen zu können, auch ohne zu verstehen, wie sie funktioniert.

##Die Aufgabe

###Path

Die Klasse `Path` soll einen Pfad durch das Labyrinth beschreiben. `Path` soll dabei nicht die konkreten Positionen der Felder auf dem Pfad abspeichern, sondern nur die Richtungen, in die man sich in jedem Schritt bewegen muss. Der Pfad kann dann in Relation zu einem beliebigen Startpunkt interpretiert werden.

Ein `Path` soll also eine Sequenz von `Direction`s sein.
So soll der Pfad von Eingang zu Ausgang in obigem Beispiel für ein gelöstes Labyrinth beispielsweise durch die Sequenz
`[DOWN, RIGHT, DOWN, DOWN, RIGHT, DOWN, DOWN, LEFT, LEFT, DOWN, DOWN, DOWN]` beschrieben sein.

Wie genau du `Path` intern repräsentierst, ist dir überlassen. Du darfst auch gerne Gebrauch von Javas eigenen Collections machen. Wir geben nur vor, welche Schnittstelle `Path` nach außen hin bereitstellen muss. Diese ist durch folgendes Klassendiagramm beschrieben:

@startuml

class Path {
	+ Path()
	+ toPositionSet(Position): Set<Position>
	+ prepend(Direction): void
	+ getStep(int): Direction
	+ toString(): String
}
@enduml

- Konstruktor:
    Der Konstruktor von `Path` soll ein `Path`-Objekt erzeugen, welches einen leeren Pfad, also einen Pfad mit 0 Schritten, darstellt
- `Set<Position> toPositionSet(Position start)`:
    Diese Methode soll von der Position `start` aus die `Direction`s des Pfades eine nach der anderen verfolgen (unabhängig von einem Labyrinth, also ohne auf `WALL`s etc. zu achten) und alle besuchten Positions - einschließlich von `start` selbst und auch der letzten Position - in einem `HashSet<Position>` ([hier mehr zur Klasse HashSet](https://docs.oracle.com/en/java/javase/16/docs/api/java.base/java/util/HashSet.html)) sammeln und dieses zurückgeben. Also sollte für den Pfad `[DOWN, DOWN, LEFT]` und `start` $$(0, 2)$$ die Menge $$ \\{ (0, 2), (1, 2), (2, 2), (2, 1) \\} $$ zurückgegeben werden, da man von $$(0, 2)$$ nach einem Schritt `DOWN` bei $$(1, 2)$$ landet, von dort aus nach einem weiteren Schritt `DOWN` bei $$(2, 2)$$ und von dort aus nach einem Schritt `LEFT` bei $$(2, 1)$$.
- `void prepend(Direction direction)`:
    Diese Methode soll die übergebene `Direction` vorne **(!!)** an den bisherigen Pfad anfügen. Also wird aus dem `Path path`, der bisher `[LEFT, UP, UP, LEFT]` repräsentierte, nach dem Call `path.prepend(Direction.DOWN)` der Pfad der `[DOWN, LEFT, UP, UP, LEFT]` repräsentiert.
- `Direction getStep(int index)`:
    Diese Methode soll den `index`-ten Schritt im Pfad (wobei der erste wie immer den Index 0 hat) zurückgeben. Für den `Path path`, der die Sequenz `[DOWN, LEFT, UP, UP, LEFT]` repräsentiert, gibt also
    - `path.getStep(0)` die Richtung `DOWN`
    - `path.getStep(1)` die Richtung `LEFT`
    - `path.getStep(2)` die Richtung `UP`
    - `path.getStep(3)` die Richtung `UP` und
    - `path.getStep(4)` die Richtung `LEFT`
    
    zurück.
- `String toString()`:
    Gibt eine String-Repräsentation des Pfades zurück. Diese soll von der Form <tt style="background-color: #dfdfdf">[</tt>\<Pfad-Element 0><tt style="background-color: #dfdfdf">,⎵</tt>\<Pfad-Element 1><tt style="background-color: #dfdfdf">,⎵</tt>\<usw ...><tt style="background-color: #dfdfdf">]</tt> sein.
    Für den `Path path`, der die Sequenz `[DOWN, LEFT, UP, UP, LEFT]` repräsentiert, soll `path.toString()` also beispielsweise den String
    <tt style="background-color: #dfdfdf">[DOWN,⎵LEFT,⎵UP,⎵UP,⎵LEFT]</tt> zurückgeben.
    <br>
    Achte hier wie immer darauf, nirgends überflüssige oder fehlende Leerzeichen einzubauen!

###MazeSolver

In der Klasse `MazeSolver` sollen lediglich zwei statische Methoden implementiert werden:

`static Path solveMaze(Maze maze)`:
Diese Methode soll einen `Path` zurückgeben, welcher, wenn man ihn vom `entrance` des `maze` aus verfolgt, einen zum `exit` des `maze` bringt. Der Pfad muss dabei nicht der kürzestmögliche sein, er sollte aber jedes Tile des Labyrinths maximal einmal besuchen.
Für das Labyrinth
<pre>
WEWWW
W   W
W W W
W   W
WWXWW
</pre>
sind also sowohl der Pfad `[DOWN, DOWN, DOWN, RIGHT, DOWN]`, als auch der Pfad `[DOWN, RIGHT, RIGHT, DOWN, DOWN, LEFT, DOWN]` zulässig, nicht aber der Pfad `[DOWN, RIGHT, RIGHT, LEFT, LEFT, DOWN, DOWN, RIGHT, DOWN]`, da dieser, wenn er auch ebenso vom Eingang zum Ausgang führt, einige Felder mehrfach besucht.
Selbstverständlich darf der zurückgegebene `Path` - vom `entrance` des `maze` aus verfolgt - keine `WALL`s durchqueren und er muss innerhalb der Grenzen des `maze` bleiben. Du kannst davon ausgehen, dass das übergebene `maze` keine `MARKED` Tiles enthält (du darfst Felder in `maze` aber natürlich selbst `MARKED` setzen).

`static Path solveMazeFrom(Maze maze, Position position)`:
Diese Methode soll wieder einen `Path` zurückgeben, der zum `exit` von `maze` führt, diesmal aber, wenn man ihn von `position` aus verfolgt.
Es gelten dieselben Regeln wie zuvor: Der zurückgegebene `Path` muss nicht der kürzestmögliche sein, darf aber kein Feld zweimal durchqueren, darf keine `WALL`s durchqueren und muss innerhalb der Grenzen des `maze` bleiben.
Um zu verhindern, dass man das gleiche Feld mehrfach besucht, kann man bereits besuchte Felder als `MARKED` markieren. Der in dieser Methode zurückgegebene Pfad darf also zusätzlich keine Felder passieren, die bereits beim Aufruf der Methode `MARKED` waren.
Sollte ein solcher Pfad nicht mehr existieren, soll `null` zurückgegeben werden.
<br>
Wenn also folgendes `Maze` übergeben wird, und darin die übergebene `position` mit \# markiert ist
<pre>
WEWWW
W''#W
W W W
W   W
WWXWW
</pre>
dann ist nun der `Path` `[DOWN, DOWN, LEFT, DOWN]` ein möglicher Rückgabewert, der `Path` `[LEFT, LEFT, DOWN, DOWN, RIGHT, DOWN]` aber nicht, da dieser markierte Felder durchqueren würde. Außerdem ist auch `[DOWN, DOWN, LEFT, LEFT, RIGHT, DOWN]` kein gültiger Rückgabewert, da dieser ein Feld (das direkt über dem `exit`) doppelt besucht.
<br>
Wenn das `Maze`
<pre>
WWWWW
W'# W
E'WWW
W  XW
WWWWW
</pre>
übergeben wird und die übergebene `position` darin wieder mit \# markiert ist, sollte `null` zurückgegeben werden, da nun der `exit` nicht mehr erreichbar ist, ohne über ein markiertes Feld (oder durch eine Wand) zu laufen.
<br>
`solveMazeFrom()` **muss rekursiv implementiert werden!** Das heißt insbesondere, dass ein Aufruf von `solveMazeFrom()` nur die direkt benachbarten Positionen der übergebenen `position` und die `position` selbst explizit ansehen darf und nur für diese überprüfen darf, ob sie `WALL`, `SPACE` oder `MARKED` bzw. der `exit` sind. Alles Weitere muss in einem rekursiven Aufruf der Methode geschehen.
Du darfst auch (musst aber nicht) Teile der Methode in Hilfsmethoden ausgliedern, welche dann wiederum `solveMazeFrom()` aufrufen. Es ist also in Ordnung, Methoden `help1()`, `help2()`, ..., `helpn()` zu haben, sodass `solveMazeFrom()` `help1()` aufruft, `help1()` `help2()` aufruft usw. und letztlich `helpn()` wieder `solveMazeFrom()` aufruft. Innerhalb eines solchen Zyklus dürfen aber, wie eben beschrieben, nur die beim Aufruf von `solveMazeFrom()` am Anfang des Zyklus übergebene `position` und deren direkte Nachbarn angeschaut werden. Alles Weitere muss über den rekursiven Call von `solveMazeFrom()` geschehen.
<br>
Alternativ, wenn du mehr Parameter als nur das `Maze` und die `Position` in deinem rekursiven Aufruf verwenden möchtest, darfst du auch anstelle von `solveMazeFrom()` eine andere rekursive Methode `Path helpRec(Maze, Position, ...)` implementieren. Diese muss dann denselben Regeln wie im Abschnitt darüber `solveMazeFrom()` folgen. In diesem Fall muss `solveMazeFrom()` nicht mehr rekursiv sein, sondern kann (auch gerne mit einigen Zwischenschritten) `helpRec()` aufrufen. Die ursprünglich übergebene `position` darf sich dann aber bis zum ersten Aufruf von `helpRec()` nicht ändern und es dürfen keine Felder des `maze` an anderen Positionen betrachtet werden. Die Musterlösung kommt aber mit Obigem aus. Diese Alternative soll nur zusätzliche Freiheiten bieten und kann gerne auch ignoriert werden.

*Hinweis*:
Wir werden eure Lösungen nur mit Labyrinthen mit Seitenlängen <= 20 testen, also maximal mit einem 20x20-Labyrinth.

# FAQ
### Q: Wofür ist das FAQ da?
A: Wenn es Fragen gibt, die häufiger aufkommen, werden sie hier gepostet und ebenfalls beantwortet werden. Wer sie danach noch auf Zulip postet macht Pinguine traurig!
### Q: Muss `solveMaze` auch rekursiv sein?
A: Nein, nur `solveMazeFrom` muss rekursiv sein. Ihr dürft `solveMaze` gerne rekursiv implementieren, es gibt aber eine deutlich einfachere Lösung.
### Q: Wenn `toPositionSet` ein `HashSet` zurückgeben soll, wie soll dann die Ordnung des `Path` beibehalten werden?
A: Soll sie nicht! Das ist der Sinn eines `Set`, dass es keine Ordnung hat. Wer sich dafür interessiert, wozu die Methode `toPositionSet` in diesem Projekt da ist, der sehe sich die Nutzung dieser Methode in `Maze` an. Da wird keine Reihenfolge benötigt.


[Lösungsvorschlag](https://bitbucket.ase.in.tum.de/projects/PGDP2122W08H01/repos/pgdp2122w08h01-solution)

[Tests](https://bitbucket.ase.in.tum.de/projects/PGDP2122W08H01/repos/pgdp2122w08h01-tests)