package paintingcanvas.canvas;

public class CanvasNotInitializedException extends RuntimeException {
    public CanvasNotInitializedException() {}

    @Override
    public String toString() {
        return "Canvas not initialized! Construct an instance of the Canvas object first.";
    }
}
