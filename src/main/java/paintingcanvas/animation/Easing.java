package paintingcanvas.animation;

/**
 * Special easing modes for animations.
 */
public interface Easing {
    /**
     * Linear easing - Go here to here.
     *
     * @return an {@code Easing} object representing this easing.
     */
    static Easing linear() {
        return t -> t;
    }

    /**
     * Ease in - Go from here to here, but slow down at the end
     *
     * @param n the degree of slowdown
     * @return an {@code Easing} object representing this easing.
     */
    static Easing inOutNth(double n) {
        return t -> {
            if (t < 0.5) return Math.pow(2 * t, n) / 2;
            else return 1 - Math.pow(2 * (1 - t), n) / 2;
        };
    }

    double ease(double t);
}
