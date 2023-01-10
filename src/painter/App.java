package painter;

/**
 * an abstract class to allow interfacing with the painter library whilst keeping the painter library distinct
 */
public abstract class App {
    /** The global Painter all Drawables access to add themselves to. */
    public static Painter globalPainter;

    /**
     * The render function run every frame
     *
     * @param args Command line arguments
     */
    public void render(String[] args) {}
}
