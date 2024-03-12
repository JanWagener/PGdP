package pgdp.encapsulation.geometry;

public class Rectangle {
    private Point bottomLeftCorner;
    private int width;
    private int height;

    public Rectangle(Point bottomLeftCorner, int width, int height) {
        this.bottomLeftCorner = bottomLeftCorner;
        this.width = width;
        this.height = height;
    }

    public Point getBottomLeftCorner() {
        return bottomLeftCorner;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getCircumference() {
        return 2 * width + 2 * height;
    }

    public int getArea() {
        return width * height;
    }
}
