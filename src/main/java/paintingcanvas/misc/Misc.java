package paintingcanvas.misc;

public class Misc {
    public static <N extends Number> N clamp(N min, N val, N max) {
        if (val.doubleValue() > max.doubleValue()) return max;
        if (val.doubleValue() < min.doubleValue()) return min;
        return val;
    }

    public static <N extends Number> boolean equality(N a, N b, N error) {
        return Math.max(a.doubleValue(), b.doubleValue()) - error.doubleValue() <= Math.min(a.doubleValue(), b.doubleValue()) + error.doubleValue();
    }
}