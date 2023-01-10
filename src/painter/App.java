package painter;

import painter.drawable.Drawable;
import painter.tween.ColorTween;
import painter.tween.MovementTween;
import painter.tween.RotationTween;

import java.awt.*;

/**
 * an abstract class to allow interfacing with the painter library whilst keeping the painter library distinct
 */
public abstract class App {
    /**
     * The global Painter all Drawables access to add themselves to.
     */
    public static Painter painter;
    protected static int builderFrame;

    public void render() {}

    public abstract void setup();

    public void run() {
        // Init global painter
        painter = new Painter(1000, 600, "Java thingy ikd");

        // Init app
        this.setup();
        painter.render(this);
    }

    protected abstract static class SimpleElement<T extends SimpleElement<T>> {
        Drawable _super;

        public SimpleElement(Drawable drawable) {
            this._super = drawable;
            painter.canvas.elements.add(this._super);
        }

        public abstract T getThis();

        public T hide() {
            this._super.visible = false;
            return getThis();
        }

        public T show() {
            this._super.visible = true;
            return getThis();
        }

        public int getX() {
            return _super.x;
        }

        public T setX(int x) {
            _super.x = x;
            return getThis();
        }

        public int getY() {
            return _super.y;
        }

        public T setY(int y) {
            _super.y = y;
            return getThis();
        }

        public T setColor(int r, int g, int b) {
            this._super.color = new Color(r, g, b);
            return getThis();
        }

        public T setColor(Color color) {
            this._super.color = color;
            return getThis();
        }

        public T moveTo(int x, int y, int duration) {
            painter.canvas.tweens.add(new MovementTween(builderFrame, duration, new Point(x, y), this._super));
            builderFrame += duration;
            return getThis();
        }

        public T rotateTo(int angle, int duration) {
            painter.canvas.tweens.add(new RotationTween(builderFrame, duration, Math.toRadians(angle), this._super));
            builderFrame += duration;
            return getThis();
        }

        public T colorTo(int r, int g, int b, int duration) {
            return colorTo(new Color(r, g, b), duration);
        }

        public T colorTo(Color color, int duration) {
            painter.canvas.tweens.add(new ColorTween(builderFrame, duration, color, this._super));
            builderFrame += duration;
            return getThis();
        }
    }

    protected static class Text extends SimpleElement<Text> {
        public Text(int x, int y, String text) {
            super(new painter.drawable.Text(x, y, text, 30));
        }

        public Text setText(String text) {
            ((painter.drawable.Text) this._super).text = text;
            return this;
        }

        @Override
        public Text getThis() {
            return this;
        }
    }
}
