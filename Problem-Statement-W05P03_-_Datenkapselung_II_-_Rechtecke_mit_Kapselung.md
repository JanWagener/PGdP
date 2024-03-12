# Vierecke mit Kapselung

Im Template zu dieser Aufgabe findest du wieder die zwei Packages: `pgdp.encapsulation.geometry` und `pgdp.encapsulation.geometryuser`.
Ersteres soll wieder eine von dir und deinem Team entwickelte Geometrie-Bibliothek darstellen, die einige Datentypen wie Punkte und Rechtecke zur Verfügung stellt.
Ein Rechteck wird dabei als linker unterer Punkt `bottomLeftCorner`, sowie Breite (`width`) und Höhe (`height`) dargestellt.
Letzteres Package repräsentiert den Code eines fremden Entwickler-Teams, das eure Bibliothek verwendet (hier: ein Rechteck erstellt und einige Eigenschaften davon auf der Konsole ausgibt).
Diesmal sind aber die Attribute korrekt gekapselt. Darauf hat sich dann auch der Geometry-User eingestellt.

### Aufgabe

Aus internen Gründen wollt ihr nun wieder die innere Darstellung der Rechtecke ändern: Ein Rechteck soll nicht mehr durch den linken unteren Eckpunkt und die beiden Seitenlängen,
sondern durch den linken unteren Eckpunkt (`bottomLeftCorner`) und den oberen rechten Eckpunkt (`topRightCorner`) dargestellt werden.
Der alte Konstuktor und die alten Methoden sollen nach wie vor funktionieren.

Deine Aufgabe ist es nun, die Änderung in der internen Repräsentation vorzunehmen, also `Rectangle` entsprechend anzupassen, sodass der Code in `pgdp.encapsulation.geometryuser` nach wie vor funktioniert.
Letzteren Code darfst du dabei natürlich nicht anfassen, da er außerhalb eures Teams entwickelt wird.

Stößt du immer noch auf dieselben Probleme wie eben?


[Musterlösung](https://bitbucket.ase.in.tum.de/scm/PGDP2223W05P03/pgdp2223w05p03-solution.git)