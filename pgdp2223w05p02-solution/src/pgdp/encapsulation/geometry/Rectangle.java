package pgdp.encapsulation.geometry;

public class Rectangle {
    public Point bottomLeftCorner;
    public Point topRightCorner;

    // Es bleibt nichts anderes übrig, als die alten Attribute beizubehalten.
    public int width;
    public int height;

    public Rectangle(Point bottomLeftCorner, int width, int height) {
        this.bottomLeftCorner = bottomLeftCorner;
        this.width = width;
        this.height = height;

        this.topRightCorner = new Point(bottomLeftCorner.x + width, bottomLeftCorner.y + height);
    }

    public int getCircumference() {
        return 2 * width + 2 * height;
    }

    public int getArea() {
        return width * height;
    }
}
