package pgdp.maze;

public final class MazeSolver {

    // Man soll von außen keine Objekte der Klasse erzeugen können. Alle Methoden
    // sind static. Daher ist der Konstruktor private
    private MazeSolver() {

    }

    // TODO: Implementiere diese Methode, sodass sie einen Pfad vom Eingang
    // ('entrance') zum Ausgang ('exit')
    // des übergebenen Labyrinths 'maze' zurückgibt.
    public static Path solveMaze(Maze maze) {
        return null;
    }

    // TODO: Implementiere diese Methode, sodass sie einen Pfad vom der übergebenen
    // Position 'position'
    // zum Ausgang ('exit') des übergebenen Labyrinths 'maze' zurückgibt.
    // Sie muss rekursiv implementiert werden.
    public static Path solveMazeFrom(Maze maze, Position position) {
        return null;
    }

    // Zum Testen
    public static void main(String[] args) {
        Maze maze = MazeParser.parseFromFile("resources/Maze.txt");
        if (maze == null) {
            return;
        }

        System.out.println(maze);

        Path path = solveMaze(maze);
        System.out.println(path);
        System.out.println();
        System.out.println(maze.toString(path));
    }

}
