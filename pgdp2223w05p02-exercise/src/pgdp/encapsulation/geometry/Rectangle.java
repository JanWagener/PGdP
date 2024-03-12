package pgdp.encapsulation.geometry;

public class Rectangle {
    public Point bottomLeftCorner;
    public int width;
    public int height;

    public Rectangle(Point bottomLeftCorner, int width, int height) {
        this.bottomLeftCorner = bottomLeftCorner;
        this.width = width;
        this.height = height;
    }

    public int getCircumference() {
        return 2 * width + 2 * height;
    }

    public int getArea() {
        return width * height;
    }
}
