import painter.Painter;
import painter.PaintingCanvas;
import painter.drawable.Text;

public class App implements painter.App {
    private static PaintingCanvas canvas;

    /**
     * This method will be run at startup and thus can be used for initialization
     *
     * @param args Command-Line arguments
     */
    public static void main(String[] args) {
        canvas = Painter.getCanvas();

        canvas.text.add(new Text(100, 100, "whee", 30));
        canvas.text.add(new Text(0, 100,  "0", 30));

        // put code here
        Text t = canvas.text.get(0);
        t.x += 1;

        System.out.printf("%d\n", t.x);

        canvas.text.get(1).text = "" + t.x;
    }

    /**
     * This method will be run every frame as a sort of "render function"
     *
     * @param args Command-Line arguments
     * @param canvas The canvas in which to draw things
     */
    @Override
    public void render(String[] args, PaintingCanvas canvas) {

    }
}