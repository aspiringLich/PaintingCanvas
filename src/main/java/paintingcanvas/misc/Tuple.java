package paintingcanvas.misc;

public class Tuple<X, Y> {
    public X first;
    public Y second;

    public Tuple(X first, Y second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public String toString() {
        return "Tuple{" +
                "x=" + first +
                ", y=" + second +
                '}';
    }
}
