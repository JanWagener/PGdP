package pgdp.encapsulation.geometry;

public class Rectangle {
    private Point bottomLeftCorner;
    private Point topRightCorner;

    // Die Attribute 'width' und 'height' können restlos entfernt werden,
    // da nach außen hin nur die Getter sichtbar sind, deren Rückgabewert
    // auch aus den neuen Attributen berechnet werden kann.
    //
    // Alles Weitere (Konstruktor und Methoden) können auch leicht angepasst werden.
    // Da der Geometry-User nur die Getter und den Konstruktor verwendet,
    // kompiliert dessen Code noch.

    public Rectangle(Point bottomLeftCorner, int width, int height) {
        this.bottomLeftCorner = bottomLeftCorner;
        this.topRightCorner = new Point(bottomLeftCorner.getX() + width, bottomLeftCorner.getY() + height);
    }

    public Point getBottomLeftCorner() {
        return bottomLeftCorner;
    }

    public int getWidth() {
        return topRightCorner.getX() - bottomLeftCorner.getX();
    }

    public int getHeight() {
        return topRightCorner.getY() - bottomLeftCorner.getY();
    }

    public int getCircumference() {
        return 2 * getWidth() + 2 * getHeight();
    }

    public int getArea() {
        return getWidth() * getHeight();
    }
}
