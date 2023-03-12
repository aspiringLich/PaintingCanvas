package paintingcanvas.misc;

public class Misc {
    public static <N extends Number> N clamp(N val, N min, N max) {
        if (val.doubleValue() > max.doubleValue()) return max;
        if (val.doubleValue() < min.doubleValue()) return min;
        return val;
    }
}
