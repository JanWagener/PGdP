package pgdp.maze;

import java.util.Set;

// TODO: Implementiere diese Klasse, sodass sie eine Sequenz von Directions repräsentiert
public class Path {
    // TODO: Attribut(e)

    // TODO: Soll einen leeren Pfad erzeugen
    public Path() {

    }

    // TODO: Soll ein HashSet<Position> mit allen Positionen zurückgeben, die man beim Ablaufen des Pfades 'this'
    //  besucht, wenn man bei der Position 'start' beginnt (ungeachtet irgendwelcher WALLs o.Ä.)
    public Set<Position> toPositionSet(Position start) {
        return null;
    }

    // TODO: Soll die übergebene Richtung 'direction' vorne in die bisherige Sequenz einfügen
    public void prepend(Direction direction) {

    }

    // TODO: Soll die Richtung am übergebenen 'index' zurückgeben
    public Direction getStep(int index) {
        return null;
    }

    // TODO: Soll eine String-Repräsentation des Pfades 'this' zurückgeben, wie in der Aufgabenstellung beschrieben
    @Override
    public String toString() {
        return null;
    }
}
