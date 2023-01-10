import painter.drawable.Text;

public class App extends painter.App {
    public App() {
        // TODO: i know this is bad, will change
        globalPainter.canvas.objects.add(new Text(100, 100, "whee", 30));
        globalPainter.canvas.objects.add(new Text(0, 100,  "0", 30));
    }

    /**
     * This method will be run every frame as a sort of "render function"
     * You don't have to put anything here, it's just an alternative to the draw function.
     *
     * @param args Command-Line arguments
     */
    @Override
    public void render(String[] args) {
        Text t = (Text) globalPainter.canvas.objects.get(0);
        t.x += 1;
        ((Text) globalPainter.canvas.objects.get(1)).text = "" + t.x;
    }
}