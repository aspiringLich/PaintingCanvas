package paintingcanvas.painter.animation;

public interface Easing {
    static Easing linear() {
        return t -> t;
    }

    static Easing inOutNth(int n) {
        return t -> {
            if (t < 0.5) return Math.pow(2 * t, n) / 2;
            else return 1 - Math.pow(2 * (1 - t), n) / 2;
        };
    }

    double ease(double t);
}
