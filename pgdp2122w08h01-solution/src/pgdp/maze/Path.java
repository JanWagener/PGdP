package pgdp.maze;

import java.util.*;

/** Repräsentiert einen Pfad (durch ein Labyrinth) als eine Sequenz von Richtungen.
 *  Also z.B. [DOWN, DOWN, RIGHT, DOWN, LEFT, LEFT, LEFT, LEFT]
 */
public class Path {
    // Datenstruktur LinkedList erlaubt einfaches Einfügen an vorderster Stelle
    private final LinkedList<Direction> steps;

    public Path() {
        steps = new LinkedList<>();
    }

    public Set<Position> toPositionSet(Position start) {
        Set<Position> positionSet = new HashSet<>();
        Position current = start;
        for(int i = 0; i < steps.size(); i++) {
            positionSet.add(current);
            current = current.getPositionOneTile(steps.get(i));
        }
        positionSet.add(current);
        return positionSet;
    }

    public void prepend(Direction direction) {
        steps.addFirst(direction);
    }

    public Direction getStep(int index) {
        return steps.get(index);
    }

    @Override
    public String toString() {
        return steps.toString();
    }
}
