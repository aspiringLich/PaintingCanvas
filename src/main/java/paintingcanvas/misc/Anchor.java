package paintingcanvas.misc;

/**
 * The Anchor enum represents what point, relative to the center of an element, the element is draw about.
 * All rotations are done around to this anchor point, and its treated as the "center" of the element.
 */
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
