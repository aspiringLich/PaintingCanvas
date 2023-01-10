import painter.drawable.Text;
import painter.tween.ColorTween;
import painter.tween.MovementTween;
import painter.tween.RotationTween;

import java.awt.*;

public class App extends painter.App {
    public App() {
        // TODO: i know this is bad, will change
        var start = 300;
        var whee = new Text(100, 100, "whee", 30);
        globalPainter.canvas.elements.add(whee);
        globalPainter.canvas.elements.add(new Text(0, 100, "0", 30));
        globalPainter.canvas.tweens.add(new MovementTween(start, 500, new Point(900, 400), whee));
        globalPainter.canvas.tweens.add(new RotationTween(start, 500, Math.toRadians(360 * 2), whee));
        globalPainter.canvas.tweens.add(new ColorTween(start, 250, Color.green, whee));
        globalPainter.canvas.tweens.add(new ColorTween(start + 250, 250, Color.RED, whee));
    }

    /**
     * This method will be run every frame as a sort of "render function"
     * You don't have to put anything here, it's just an alternative to the draw function.
     *
     * @param args Command-Line arguments
     */
    @Override
    public void render(String[] args) {
        Text t = (Text) globalPainter.canvas.elements.get(0);
        ((Text) globalPainter.canvas.elements.get(1)).text = String.valueOf(t.x);
    }
}