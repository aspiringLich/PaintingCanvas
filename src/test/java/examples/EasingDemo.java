package examples;

import paintingcanvas.animation.Animation;
import paintingcanvas.animation.Easing;
import paintingcanvas.canvas.Canvas;
import paintingcanvas.drawable.Square;
import paintingcanvas.drawable.Text;
import paintingcanvas.extensions.FrameCounter;

import java.awt.*;

public class EasingDemo {
    static final Easing[] easings = {
            Easing.easeInSine(),
            Easing.easeOutSine(),
            Easing.easeInOutSine(),
            Easing.easeInQuad(),
            Easing.easeOutQuad(),
            Easing.easeInOutQuad(),
            Easing.easeInCubic(),
            Easing.easeOutCubic(),
            Easing.easeInOutCubic(),
            Easing.easeInQuart(),
            Easing.easeOutQuart(),
            Easing.easeInOutQuart(),
            Easing.easeInQuint(),
            Easing.easeOutQuint(),
            Easing.easeInOutQuint(),
            Easing.easeInExpo(),
            Easing.easeOutExpo(),
            Easing.easeInOutExpo(),
            Easing.easeInCirc(),
            Easing.easeOutCirc(),
            Easing.easeInOutCirc(),
            Easing.easeInBack(),
            Easing.easeOutBack(),
            Easing.easeInOutBack(),
            Easing.easeInElastic(),
            Easing.easeOutElastic(),
            Easing.easeInOutElastic(),
            Easing.easeInBounce(),
            Easing.easeOutBounce(),
            Easing.easeInOutBounce(),
            Easing.linear(),
            Easing.linear(),
            Easing.linear()
    };
    static final String[] names = {
            "easeInSine",
            "easeOutSine",
            "easeInOutSine",
            "easeInQuad",
            "easeOutQuad",
            "easeInOutQuad",
            "easeInCubic",
            "easeOutCubic",
            "easeInOutCubic",
            "easeInQuart",
            "easeOutQuart",
            "easeInOutQuart",
            "easeInQuint",
            "easeOutQuint",
            "easeInOutQuint",
            "easeInExpo",
            "easeOutExpo",
            "easeInOutExpo",
            "easeInCirc",
            "easeOutCirc",
            "easeInOutCirc",
            "easeInBack",
            "easeOutBack",
            "easeInOutBack",
            "easeInElastic",
            "easeOutElastic",
            "easeInOutElastic",
            "easeInBounce",
            "easeOutBounce",
            "easeInOutBounce",
            "linear",
            "linear",
            "linear",
    };

    public static void main(String[] args) {
        // variables used to setup the layout of the demo
        int height = 40;
        int width = 400;
        int squareWidth = 35;
        int columns = 3;

        Canvas canvas = new Canvas(width * columns + squareWidth - 5, easings.length * height / columns, "Easing Demo");
        new FrameCounter().line(() -> String.format("Frame: %d", canvas.getFrame())).attach();
//        new Recorder().record(Path.of("rec"), "png").attach();
        Square[] squares = new Square[easings.length];
        Text[] text = new Text[easings.length];

        // create all the objects
        for (int i = 0; i < easings.length; i++) {
            var color = Color.getHSBColor((float) (i / columns) / (easings.length / (float) columns), 1, 1);
            int y = i / columns * height + height / 2;
            squares[i] = new Square(squareWidth / 2 + width * (i % columns), y, 35, color);
            text[i] = new Text(width / 2 + width * (i % columns), y, names[i]).setFontSize(20);
        }

        canvas.sleep(1);
        for (int itr = 0; ; itr++) {
            // animate all the objects
            for (int i = 0; i < easings.length; i++) {
                Easing easing = easings[i];
                Square square = squares[i];

                var a1 = Animation.moveTo(
                        text[i].getX() + (itr % 2 == 0 ? 1 : -1) * width / 2 + squareWidth / 2,
                        square.getY()
                ).easing(easing);
                var a2 = Animation.rotateTo(itr % 2 == 0 ? (int) Math.toDegrees(360.0) : 0).easing(easing);
                square.animate().with(a1, 3).with(a2, 3);
            }
            // sleep
            canvas.sleep(4);
            // repeat
        }
    }
}
