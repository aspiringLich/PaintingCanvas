package paintingcanvas.misc;

public enum Anchor {
    TOP_LEFT(-0.5, -0.5),
    TOP_CENTER(0.0, -0.5),
    TOP_RIGHT(0.5, -0.5),
    CENTER_LEFT(-0.5, 0.0),
    CENTER(0.0, 0.0),
    CENTER_RIGHT(0.5, 0.0),
    BOTTOM_LEFT(-0.5, 0.5),
    BOTTOM_CENTER(0.0, 0.5),
    BOTTOM_RIGHT(0.5, 0.5);

    public final double x;
    public final double y;

    Anchor(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
