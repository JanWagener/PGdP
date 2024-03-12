package pgdp.maze;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Stream;

import de.tum.in.test.api.dynamic.DynamicClass;
import de.tum.in.test.api.dynamic.DynamicConstructor;
import de.tum.in.test.api.dynamic.DynamicMethod;
import de.tum.in.test.api.jupiter.Hidden;
import de.tum.in.test.api.jupiter.HiddenTest;
import de.tum.in.test.api.jupiter.PublicTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@W08H01
class PathTests {

    private DynamicClass<?> pathClass = DynamicClass.toDynamic("pgdp.maze.Path");
    private DynamicConstructor<?> newPath = pathClass.constructor();

    private DynamicMethod<?> toPositionSet = pathClass.method(Set.class, "toPositionSet", Position.class);
    private DynamicMethod<?> prepend = pathClass.method(void.class, "prepend", Direction.class);
    private DynamicMethod<?> getStep = pathClass.method(Direction.class, "getStep", int.class);
    private DynamicMethod<?> toString = pathClass.method(String.class, "toString");

    // ================================================================================ //
    // --------------------------------- PUBLIC TESTS --------------------------------- //
    // ================================================================================ //

    @PublicTest
    void structureTestsPath() {
        assertAll(
                () -> assertTrue(newPath.exists(),
                        "Der in 'Path' verlangte Konstruktor ohne Parameter existiert nicht."),
                () -> assertTrue(toPositionSet.exists(),
                        "Die in 'Path' verlangte Methode mit Signatur 'toPositionSet(Position)' und Rückgabetyp 'Set<Position>' existiert nicht."),
                () -> assertTrue(prepend.exists(),
                        "Die in 'Path' verlangte Methode mit Signatur 'prepend(Direction)' und Rückgabetyp 'void' existiert nicht."),
                () -> assertTrue(getStep.exists(),
                        "Die in 'Path' verlangte Methode mit Signatur 'getStep(int)' und Rückgabetyp 'Direction' existiert nicht."),
                () -> assertTrue(toString.exists(),
                        "Die in 'Path' verlangte Methode mit Signatur 'toString()' und Rückgabetyp 'String' existiert nicht."));
    }

    @PublicTest
    void testPrependGetStep() {
        testPrependGetStepWith(Direction.UP);
        testPrependGetStepWith(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT);
    }

    private void testPrependGetStepWith(Direction... directions) {
        var path = newPath.newInstance();
        for (int i = directions.length - 1; i >= 0; i--) {
            prepend.invokeOn(path, directions[i]);
        }

        Direction[] directionsFromStudentImplementation = new Direction[directions.length];
        for (int i = 0; i < directions.length; i++) {
            try {
                directionsFromStudentImplementation[i] = (Direction) getStep.invokeOn(path, i);
            } catch (Exception e) {
                assertEquals("No Exception", "Exception",
                        "Beim Aufrufen von prepend() " + directions.length + " mal nacheinander"
                                + " löste der Aufruf von getStep(" + i
                                + ") eine Exception aus. Sicher, dass beim prepend() die übergebene Direction"
                                + " immer korrekt in den Pfad mit aufgenommen wird?");
            }
        }

        boolean indexOutOfBoundsHandled = false;
        try {
            Direction direction = (Direction) getStep.invokeOn(path, directions.length);
            indexOutOfBoundsHandled = direction == null;
        } catch (Exception e) {
            indexOutOfBoundsHandled = true;
        }

        assertTrue(indexOutOfBoundsHandled, "Nach dem " + directions.length
                + "-maligen Aufrufen von prepend() auf einem neuen Path-Objekt sollte getStep(" + directions.length
                + ") keinen Wert zurückgeben, sondern einen Fehler (z.B. eine IndexOutOfBoundsException von der deiner Implementierung"
                + " zu Grunde liegenden Datenstruktur) produzieren oder null zurückgeben.");

        List<Integer> indicesWithErrors = new ArrayList<>();
        for (int i = 0; i < directions.length; i++) {
            if (directions[i] != directionsFromStudentImplementation[i]) {
                indicesWithErrors.add(i);
            }
        }
        assertArrayEquals(directions, directionsFromStudentImplementation,
                "Nach dem Aufrufen von prepend() auf einem leeren Path-Objekt mit "
                        + arrayToString(invertArray(directions), "", "", ", dann ")
                        + " sollte das Path-Objekt den Pfad " + arrayToString(directions, "[", "]", ", ")
                        + " darstellen. Entsprechende Inhalte sollten von"
                        + " getStep() zurückgegeben werden. Allerdings gibt getStep für die Indizes "
                        + indicesWithErrors + " nicht die erwarteten Inhalte zurück."
                        + " Das kann an einem Fehler in prepend() oder getStep() liegen.");
    }

    @PublicTest
    void testPrependToString() {
        testPrependToStringWith(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT);
        testPrependToStringWith(Direction.UP);
        testPrependToStringWith();
    }

    private void testPrependToStringWith(Direction... directions) {
        var path = newPath.newInstance();
        for (int i = directions.length - 1; i >= 0; i--) {
            prepend.invokeOn(path, directions[i]);
        }

        String expected = arrayToString(directions, "[", "]", ", ");
        String actual = (String) toString.invokeOn(path);

        String[] expectedContent = expected.replaceAll("\\[|\\]| ", "").split(",");
        String[] actualContent = actual.replaceAll("\\[|\\]| ", "").split(",");

        String errorMessage = "Die toString()-Methode in Path produziert einen falschen String.";
        errorMessage += Arrays.equals(expectedContent, actualContent)
                ? " Die Directions werden in der richtigen Reihenfolge ausgegeben, achte also auf falsche Klammern, Leerzeichen usw."
                : "";

        assertEquals(expected, actual, errorMessage);
    }

    private String arrayToString(Direction[] directions, String opening, String closing, String interspersed) {
        if (directions.length == 0)
            return opening + closing;

        StringBuilder sb = new StringBuilder();
        sb.append(opening);
        for (int i = 0; i < directions.length - 1; i++) {
            sb.append(directions[i]);
            sb.append(interspersed);
        }
        sb.append(directions[directions.length - 1]);
        sb.append(closing);
        return sb.toString();
    }

    private Direction[] invertArray(Direction[] arr) {
        Direction[] out = new Direction[arr.length];
        for (int i = 0; i < arr.length; i++) {
            out[arr.length - i - 1] = arr[i];
        }
        return out;
    }

    // ================================================================================ //
    // --------------------------------- HIDDEN TESTS --------------------------------- //
    // ================================================================================ //

    @HiddenTest
    void testToPositionSetIsHashSet() {
        Path path = (Path) newPath.newInstance();
        prepend.invokeOn(path, Direction.DOWN);
        prepend.invokeOn(path, Direction.DOWN);
        prepend.invokeOn(path, Direction.DOWN);

        var positionSet = toPositionSet.invokeOn(path, new Position(0, 0));

        assertTrue(positionSet instanceof HashSet, "Die Methode toPositionSet() gibt in deiner Lösung kein HashSet zurück!");
    }

    @ParameterizedTest
    @Hidden
    @MethodSource("provideDirectionsAndPositions")
    void testToPositionSet(Direction[] directions, Position start) {
        Path path = pathFrom(directions);

        Set<Position> correctSet = toPositionSetCorrect(directions, start);
        Set<Position> studentSet = (Set<Position>) toPositionSet.invokeOn(path, start);

        Set<Position> onlyInCorrectSet = new HashSet<>();
        Set<Position> onlyInStudentSet = new HashSet<>();

        for(Position p : correctSet) {
            if(!studentSet.contains(p)) {
                onlyInCorrectSet.add(p);
            }
        }
        for(Position p : studentSet) {
            if(!correctSet.contains(p)) {
                onlyInStudentSet.add(p);
            }
        }

        StringBuilder wrongPositionText = new StringBuilder("");
        if(onlyInCorrectSet.size() > 0) {
            Position first = onlyInCorrectSet.stream().toList().get(0);
            wrongPositionText.append("Folgende Position fehlt in der von dir in Path.toPositionSet(Position) zurückgegebenen Menge, obwohl sie darin sein sollte: (" + first.getI() + ", " + first.getJ() + ").");
        }
        if(onlyInStudentSet.size() > 0) {
            Position first = onlyInStudentSet.stream().toList().get(0);
            wrongPositionText.append("Folgende Position ist in der von dir in Path.toPositionSet(Position) zurückgegebenen Menge, obwohl sie nicht darin sein sollte: (" + first.getI() + ", " + first.getJ() + ").");
        }

        assertEquals(correctSet, studentSet,
                "Die Methode toPositionSet gibt für den Pfad " + Arrays.toString(directions) + " die falsche Menge zurück.");
    }

    @ParameterizedTest
    @Hidden
    @MethodSource("provideDirections")
    void testGetStep(Direction[] directions) {
        Path path = pathFrom(directions);
        for(int i = 0; i < directions.length; i++) {
            assertEquals(directions[i], path.getStep(i), "Die Methode getStep() gibt für den Pfad " + Arrays.toString(directions) +
                    " an Index " + i + " eine falsche Richtung zurück.");
        }
    }

    @ParameterizedTest
    @Hidden
    @MethodSource("provideDirections")
    void testToString(Direction[] directions) {
        Path path = pathFrom(directions);

        String correctString = Arrays.toString(directions);
        String studentString = path.toString();

        if(!correctString.equals(studentString)) {
            if(studentString.charAt(0) != '[' || studentString.charAt(studentString.length() - 1) != ']') {
                fail("Der von toString() zurückgegebene String ist nicht, wie verlangt, von eckigen Klammern '[]' umschlossen.\nGefundener String: " + studentString);
            }
            String[] directionStringsCorrect = correctString.replaceAll("\\[|\\]| \n\t", "").split(",");
            String[] directionStringsStudent = studentString.replaceAll("\\[|\\]| \n\t", "").split(",");
            if(Arrays.equals(directionStringsStudent, directionStringsCorrect)) {
                fail("Die toString()-Methode deiner Path-Klasse gibt für den Pfad " +
                        correctString + " einen falschen Wert zurück. " +
                        "Allerdings tauchen die richtigen Richtungen in der richtigen Reihenfolge auf. " +
                        "Vielleicht hast du irgendwo ein Leerzeichen zu viel eingebaut?\n" +
                        "Korrekter String: " + correctString + "\n" +
                        "Deine Lösung: " + studentString);
            }
        }

        assertEquals(Arrays.toString(directions), path.toString(), "Die toString()-Methode deiner Path-Klasse gibt für den Pfad " +
                Arrays.toString(directions) + " einen falschen Wert zurück.");
    }

    // ----------------------------------------- Helper Methods ----------------------------------------- //

    private static Stream<Arguments> provideDirections() {
        return Stream.of(
                Arguments.of((Object) new Direction[]{}),
                Arguments.of((Object) new Direction[]{ Direction.DOWN }),
                Arguments.of((Object) new Direction[]{ Direction.DOWN, Direction.DOWN }),
                Arguments.of((Object) new Direction[]{ Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT }),
                Arguments.of((Object) new Direction[]{ Direction.RIGHT, Direction.RIGHT, Direction.RIGHT, Direction.RIGHT, Direction.RIGHT,
                        Direction.DOWN, Direction.DOWN, Direction.DOWN, Direction.LEFT, Direction.DOWN,
                        Direction.DOWN, Direction.RIGHT, Direction.RIGHT, Direction.RIGHT, Direction.UP,
                        Direction.UP, Direction.RIGHT, Direction.RIGHT, Direction.RIGHT, Direction.DOWN,
                        Direction.RIGHT, Direction.DOWN, Direction.DOWN, Direction.LEFT, Direction.LEFT,
                        Direction.DOWN, Direction.DOWN, Direction.LEFT, Direction.LEFT, Direction.UP })
        );
    }

    private static Stream<Arguments> provideDirectionsAndPositions() {
        return Stream.of(
                Arguments.of(new Direction[]{}, new Position(0, 0)),
                Arguments.of(new Direction[]{}, new Position(5, 10)),
                Arguments.of(new Direction[]{}, new Position(-5, -10)),
                Arguments.of(new Direction[]{ Direction.DOWN }, new Position(0, 0)),
                Arguments.of(new Direction[]{ Direction.DOWN, Direction.DOWN }, new Position(0, 0)),
                Arguments.of(new Direction[]{ Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT }, new Position(0, 0)),
                Arguments.of(new Direction[]{ Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT }, new Position(5, 10)),
                Arguments.of(new Direction[]{ Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT }, new Position(-5, -10)),
                Arguments.of(new Direction[]{ Direction.RIGHT, Direction.RIGHT, Direction.RIGHT, Direction.RIGHT, Direction.RIGHT,
                        Direction.DOWN, Direction.DOWN, Direction.DOWN, Direction.LEFT, Direction.DOWN,
                        Direction.DOWN, Direction.RIGHT, Direction.RIGHT, Direction.RIGHT, Direction.UP,
                        Direction.UP, Direction.RIGHT, Direction.RIGHT, Direction.RIGHT, Direction.DOWN,
                        Direction.RIGHT, Direction.DOWN, Direction.DOWN, Direction.LEFT, Direction.LEFT,
                        Direction.DOWN, Direction.DOWN, Direction.LEFT, Direction.LEFT, Direction.UP }, new Position(0, 0))
        );
    }



    private Path pathFrom(Direction[] directions) {
        Path path = (Path) newPath.newInstance();
        for(int i = directions.length - 1; i >= 0; i--) {
            prepend.invokeOn(path, directions[i]);
        }
        return path;
    }

    private Set<Position> toPositionSetCorrect(Direction[] directions, Position start) {
        Set<Position> positionSet = new HashSet<>();
        Position current = start;
        for(int i = 0; i < directions.length; i++) {
            positionSet.add(current);
            current = current.getPositionOneTile(directions[i]);
        }
        positionSet.add(current);
        return positionSet;
    }

}
