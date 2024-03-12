package pgdp.maze;

public class MazeSolver {

    private MazeSolver() {
    }

    /** Findet einen Pfad vom Eingang zum Ausgang des übergebenen Labyrinths
     *
     * @param maze Ein Labyrinth
     * @return Ein Pfad vom Eingang zum Ausgang
     */
    public static Path solveMaze(Maze maze) {
        return solveMazeFrom(maze, maze.getEntrance());
    }

    /** Findet einen Pfad von der Position 'position' zum Ausgang des übergebenen Labyrinths
     *
     * @param maze Ein Labyrinth
     * @param position Die Startposition
     * @return Ein Pfad von 'position' zum Ausgang des Labyrinths
     */
    public static Path solveMazeFrom(Maze maze, Position position) {
        if(maze.getExit().equals(position)) {
            return new Path();
        }

        maze.mark(position);

        // Versuche den Ausgang zu finden, in dem man einen Schritt nach oben läuft
        // und von dort aus rekursiv einen Pfad zum Ausgang sucht
        Path up = solveMazeFromBySteppingIn(maze, position, Direction.UP);
        if(up != null) {
            return up;
        }

        // Versuche den Ausgang zu finden, in dem man einen Schritt nach unten läuft
        // und von dort aus rekursiv einen Pfad zum Ausgang sucht
        Path down = solveMazeFromBySteppingIn(maze, position, Direction.DOWN);
        if(down != null) {
            return down;
        }

        // Versuche den Ausgang zu finden, in dem man einen Schritt nach links läuft
        // und von dort aus rekursiv einen Pfad zum Ausgang sucht
        Path left = solveMazeFromBySteppingIn(maze, position, Direction.LEFT);
        if(left != null) {
            return left;
        }

        // Versuche den Ausgang zu finden, in dem man einen Schritt nach rechts läuft
        // und von dort aus rekursiv einen Pfad zum Ausgang sucht
        Path right = solveMazeFromBySteppingIn(maze, position, Direction.RIGHT);
        if(right != null) {
            return right;
        }

        // Wenn keine der vier Richtungen einen Pfad zum Ausgang findet, dann gibt es keinen: Rückgabewert 'null'
        return null;
    }

    // Hilfsmethode, die einen Pfad im übergebenen Labyrinth 'maze' von der übergebenen Position 'position' zum Ausgang findet,
    // wobei der erste Schritt im Pfad in die übergebene Richtung 'direction' gehen muss
    private static Path solveMazeFromBySteppingIn(Maze maze, Position position, Direction direction) {
        Position oneStepInDirection = position.getPositionOneTile(direction);
        if(maze.isEmptyTile(oneStepInDirection)) {
            Path path = solveMazeFrom(maze, oneStepInDirection);
            if(path != null) {
                path.prepend(direction);
                return path;
            }
        }

        return null;
    }

    // Nur zum Ausprobieren
    public static void main(String... args) {
        Maze maze = MazeParser.parseFromFile("resources/Maze.txt");
        if(maze == null) {
            return;
        }

        System.out.println(maze);

        Path path = solveMaze(maze);
        System.out.println(path);
        System.out.println();

        System.out.println(maze.toString(path));
    }

}
