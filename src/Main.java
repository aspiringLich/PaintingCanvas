import painter.Painter;

public class Main {
    public static Painter globalPainter;

    /**
     * The entry-point into the application
     *
     * @param args Commands-Line arguments
     */
    public static void main(String[] args) {
        globalPainter = new Painter(1000, 600, "Java thingy ikd");
        painter.App.globalPainter = globalPainter;

        globalPainter.render(args, new App());
    }
}
