package pgdp.maze;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Stream;

import de.tum.in.test.api.dynamic.DynamicClass;
import de.tum.in.test.api.dynamic.DynamicConstructor;
import de.tum.in.test.api.dynamic.DynamicField;
import de.tum.in.test.api.dynamic.DynamicMethod;
import de.tum.in.test.api.jupiter.Hidden;
import de.tum.in.test.api.jupiter.HiddenTest;
import de.tum.in.test.api.jupiter.PublicTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import pgdp.maze.Maze.TileState;

import static org.junit.jupiter.api.Assertions.*;

@W08H01
class MazeSolverTests {

    private DynamicClass<?> mazeClass = DynamicClass.toDynamic("pgdp.maze.Maze");
    private DynamicConstructor<?> newMaze = mazeClass.constructor(Maze.TileState[][].class, Position.class,
            Position.class);
    private DynamicField<?> tiles = mazeClass.field(Maze.TileState[][].class, "tiles");
    private DynamicField<?> entrance = mazeClass.field(Position.class, "entrance");
    private DynamicField<?> exit = mazeClass.field(Position.class, "exit");

    private DynamicClass<?> mazeSolverClass = DynamicClass.toDynamic("pgdp.maze.MazeSolver");
    private DynamicMethod<?> solveMaze = mazeSolverClass.method(Path.class, "solveMaze", Maze.class);
    private DynamicMethod<?> solveMazeFrom = mazeSolverClass.method(Path.class, "solveMazeFrom", Maze.class,
            Position.class);

    // ================================================================================ //
    // --------------------------------- PUBLIC TESTS --------------------------------- //
    // ================================================================================ //

    @PublicTest
    void structureTestsMazeSolver() {
        assertAll(
                () -> assertTrue(solveMaze.exists(),
                "Die Methode in 'MazeSolver' mit Signatur 'solveMaze(Maze)' und Rückgabetyp 'Path' existiert nicht."),
                () -> assertTrue(solveMazeFrom.exists(),
                        "Die Methode in 'MazeSolver' mit Signatur 'solveMazeFrom(Maze, Position)' und Rückgabetyp 'Path' existiert nicht.")
        );
    }

    @PublicTest
    void testMazeSolverMazeSimple() {
        Maze maze = (Maze) newMaze.newInstance(
                new Maze.TileState[][] { { Maze.TileState.WALL, Maze.TileState.SPACE, Maze.TileState.WALL },
                        { Maze.TileState.WALL, Maze.TileState.SPACE, Maze.TileState.WALL },
                        { Maze.TileState.WALL, Maze.TileState.SPACE, Maze.TileState.WALL } },
                new Position(0, 1), new Position(2, 1));
        Maze copiedMaze = copyMaze(maze);

        Path solution = (Path) solveMaze.invokeOn(null, maze);

        try {
            boolean firstStepIsDown = solution.getStep(0) == Direction.DOWN;
            boolean secondStepIsDown = solution.getStep(1) == Direction.DOWN;
            boolean onlyTwoSteps = false;
            try {
                Direction direction = solution.getStep(2);
                onlyTwoSteps = direction == null;
            } catch (Exception e) {
                onlyTwoSteps = true;
            }

            String studentPathToStringSentence = "";
            try {
                studentPathToStringSentence = " Du findest aber den Pfad " + solution.toString()
                        + " (deine eigene toString()-Methode!!) der das Labyrinth nicht löst oder das gleiche Feld mehrfach besucht.";
            } catch (Exception e) {

            }

            assertTrue(firstStepIsDown && secondStepIsDown && onlyTwoSteps, "Der einzige Pfad, der das Labyrinth\n"
                    + copiedMaze.toString() + "löst, ist [DOWN, DOWN]." + studentPathToStringSentence);
        } catch (Exception e) {
            assertEquals("No Exception", "Exception",
                    "Deine Lösung für solveMaze() produziert bei Eingabe des Labyrinths\n" + copiedMaze.toString()
                            + "entweder einen Fehler"
                            + " oder sie findet einen Pfad der Länge < 2. Der einzige Pfad, der dieses Labyrinth jedoch löst, ist [DOWN, DOWN].");
        }
    }

    private Maze copyMaze(Maze maze) {
        try {
            Field tilesField = maze.getClass().getDeclaredField("tiles");
            tilesField.setAccessible(true);
            TileState[][] tiles = (TileState[][]) tilesField.get(maze);

            TileState[][] copiedTiles = Arrays.stream(tiles).map(column -> Arrays.copyOf(column, column.length))
                    .toArray(TileState[][]::new);

            return (Maze) newMaze.newInstance(copiedTiles,
                    new Position(maze.getEntrance().getI(), maze.getEntrance().getJ()),
                    new Position(maze.getExit().getI(), maze.getExit().getJ()));
        } catch (Throwable e) {
            fail("Hast du die Mazeklasse verändert?");
            return null;
        }
    }

    // ================================================================================ //
    // --------------------------------- HIDDEN TESTS --------------------------------- //
    // ================================================================================ //

    @ParameterizedTest
    @Hidden
    @MethodSource("provideFiles")
    void testSolveMaze(File file) {
        testSolveMazeHelper(file, "solveMaze");
    }

    @ParameterizedTest
    @Hidden
    @MethodSource("provideFiles")
    void testSolveMazeFrom(File file) {
        testSolveMazeHelper(file, "solveMazeFrom");
    }

    private void testSolveMazeHelper(File file, String methodName) {
        Maze maze = parseFromFile(file.getPath());
        Maze copyOfMaze = (Maze) newMaze.newInstance(tiles.getOf(maze), entrance.getOf(maze), exit.getOf(maze));
        Path solution = (Path) (switch(methodName) {
            case "solveMaze" -> solveMaze.invokeOn(null, maze);
            case "solveMazeFrom" -> solveMazeFrom.invokeOn(null, maze, entrance.getOf(maze));
            default -> null;
        });
        if(solution == null) {
            fail("Das getestete Labyrinth\n" + copyOfMaze + "ist lösbar. Du gibst aber 'null' zurück!");
        }

        int lengthOfSolution = lengthOfSolutionFromToString(solution);

        Position currentPosition = maze.getEntrance();
        for(int i = 0; i < lengthOfSolution; i++) {
            currentPosition = currentPosition.getPositionOneTile(solution.getStep(i));
            if(!isInBounds(currentPosition, copyOfMaze)) {
                fail("Dein Pfad\n" + solution + "\ndurch das Labyrinth\n" + copyOfMaze + "verlässt mit dem " + i + "-ten Schritt die Grenzen desselben " +
                        "und betritt die Position (" + currentPosition.getI() + ", " + currentPosition.getJ() + ").");
            }

            TileState stateAtCurrentPosition = ((TileState[][])tiles.getOf(copyOfMaze))[currentPosition.getI()][currentPosition.getJ()];
            assertNotEquals(stateAtCurrentPosition, TileState.WALL,
                    "Dein Pfad\n" + solution + "\ndurch das Labyrinth\n" + copyOfMaze + "von " + methodName + "() durchquert Wände desselben.\n" +
                            "Hier das Labyrinth und der Pfad, wobei # ein Feld kennzeichnet, das auf dem Pfad liegt und eine WALL ist:\n" +
                            mazeWithPathThroughWallsAsString(copyOfMaze, solution));
        }

        assertEquals(maze.getExit(), currentPosition, "Dein Pfad\n" + solution + "\n durch das Labyrinth\n" + copyOfMaze +
                "von " + methodName + "() führt nicht zum Ausgang desselben:\n" + maze.toString(solution) +
                "Der Ausgang befindet sich an Position (" + copyOfMaze.getExit().getI() + ", " + copyOfMaze.getExit().getJ() + "), " +
                "dein Pfad endet aber an Position (" + currentPosition.getI() + ", " + currentPosition.getJ() + ")");
    }

    // ----------------------------------------- Helper Methods ----------------------------------------- //

    private static Stream<File> provideFiles() {
        File resourceFolder = new File("resources");
        return Stream.of(Objects.requireNonNull(resourceFolder.listFiles(file -> file.getName().endsWith(".txt"))));
    }

    private boolean isInBounds(Position position, Maze maze) {
        int height = ((TileState[][]) tiles.getOf(maze)).length;
        int width = ((TileState[][]) tiles.getOf(maze))[0].length;

        int i = position.getI();
        int j = position.getJ();

        return i >= 0 && i < height && j >= 0 && j < width;
    }

    private int lengthOfSolutionFromToString(Path solution) {
        String s = solution.toString();
        boolean notEmpty = s.contains("U") || s.contains("D") || s.contains("L") || s.contains("R");
        return notEmpty ? solution.toString().split(",").length : 0;
    }

    private String mazeWithPathThroughWallsAsString(Maze maze, Path solution) {
        Set<Position> positionsOnPath = solution == null ? null : solution.toPositionSet(maze.getEntrance());

        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < maze.getHeight(); i++) {
            for(int j = 0; j < maze.getWidth(); j++) {
                if(maze.getEntrance().isAt(i, j)) {
                    sb.append('E');
                } else if(maze.getExit().isAt(i, j)) {
                    sb.append('X');
                } else if(positionsOnPath != null && positionsOnPath.contains(new Position(i, j))) {
                    sb.append(((TileState[][]) tiles.getOf(maze))[i][j] == TileState.WALL ? '#' : '*');
                } else {
                    switch (((TileState[][]) tiles.getOf(maze))[i][j]) {
                        case WALL -> sb.append('W');
                        case SPACE -> sb.append(' ');
                        case MARKED -> sb.append('\'');
                        default -> sb.append('R');
                    }
                }
            }
            sb.append('\n');
        }
        return sb.toString();
    }



    // =============================== COPY OF PARSING CODE =============================== //
    // ----------------------------- Because of access rights ----------------------------- //

    private static Maze parseFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath, Charset.defaultCharset()))) {
            List<String> lines = new ArrayList<>();
            String line = br.readLine();
            while (line != null) {
                lines.add(line);
                line = br.readLine();
            }

            br.close();
            return mazeFromStringArray(lines.toArray(new String[0]));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Maze mazeFromStringArray(String[] lines) {
        if (!allSameLength(lines)) {
            throw new IllegalArgumentException("All lines in input file should be of same length!");
        }

        Maze.TileState[][] tiles = new Maze.TileState[lines.length][lines[0].length()];
        Position entrance = null;
        Position exit = null;
        for (int i = 0; i < lines.length; i++) {
            for (int j = 0; j < lines[i].length(); j++) {
                switch (lines[i].charAt(j)) {
                    case ' ' -> {
                        tiles[i][j] = Maze.TileState.SPACE;
                    }
                    case 'E' -> {
                        tiles[i][j] = Maze.TileState.SPACE;
                        if (entrance != null) {
                            throw new IllegalArgumentException("Input file contains multiple entrances!");
                        }
                        entrance = new Position(i, j);
                    }
                    case 'X' -> {
                        tiles[i][j] = Maze.TileState.SPACE;
                        if (exit != null) {
                            throw new IllegalArgumentException("Input file contains multiple exits!");
                        }
                        exit = new Position(i, j);
                    }
                    case 'W' -> {
                        tiles[i][j] = Maze.TileState.WALL;
                    }
                    case '\'' -> {
                        tiles[i][j] = Maze.TileState.MARKED;
                    }
                    default -> {
                        throw new IllegalArgumentException(
                                "Input file should only contain characters ' ', 'E', 'X', 'W' and '\'' and line breaks!");
                    }
                }
            }
        }
        return new Maze(tiles, entrance, exit);
    }

    private static boolean allSameLength(String[] lines) {
        boolean allSameLength = true;
        int length = lines[0].length();
        for (int i = 1; i < lines.length; i++) {
            allSameLength &= lines[i].length() == length;
        }
        return allSameLength;
    }
}
