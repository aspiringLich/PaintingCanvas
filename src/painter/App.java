package painter;

import painter.drawable.Drawable;
import painter.tween.ColorTween;
import painter.tween.MovementTween;
import painter.tween.RotationTween;
import painter.tween.Tween;

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
    protected static int lastBuilderFrame;

    public void render() {}

    public abstract void setup();

    public void run() {
        // Init global painter
        painter = new Painter(1000, 600, "Java thingy ikd");

        // Init app
        this.setup();
        painter.render(this);
    }

    // == Define tweens ==
    protected Tween colorTo(int r, int g, int b) {
        return colorTo(new Color(r, g, b));
    }

    protected Tween colorTo(Color color) {
        return new ColorTween(builderFrame, color, 0, null);
    }

    protected Tween moveTo(int x, int y) {
        return new MovementTween(builderFrame, 0, new Point(x, y), null);
    }

    protected Tween rotateTo(int angle) {
        return new RotationTween(builderFrame, 0, Math.toRadians(angle), null);
    }

    protected abstract static class SimpleElement<T extends SimpleElement<T>> {
        Drawable _super;

        public SimpleElement(Drawable drawable) {
            this._super = drawable;
            synchronized (painter.canvas.elements) {
                painter.canvas.elements.add(this._super);
            }
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

        public T animate(Tween tween, int duration) {
            tween.drawable = this._super;
            tween.startFrame = builderFrame;
            tween.duration = duration;
            lastBuilderFrame = builderFrame;
            builderFrame += duration;
            synchronized (painter.canvas.tweens) {
                painter.canvas.tweens.add(tween);
            }
            return getThis();
        }

        public T animateWith(Tween tween, int duration) {
            tween.drawable = this._super;
            tween.startFrame = lastBuilderFrame;
            tween.duration = duration;
            synchronized (painter.canvas.tweens) {
                painter.canvas.tweens.add(tween);
            }
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

    protected static class Ellipse extends SimpleElement<Ellipse> {
        public Ellipse(int x, int y, int w, int h) {
            super(new painter.drawable.Ellipse(x, y, w, h));
        }

        @Override
        public Ellipse getThis() {
            return this;
        }
    }

    protected static class Circle extends SimpleElement<Circle> {
        public Circle(int x, int y, int r) {
            super(new painter.drawable.Ellipse(x, y, r, r));
        }

        @Override
        public Circle getThis() {
            return this;
        }
    }

    protected static class Rectangle extends SimpleElement<Rectangle> {
        public Rectangle(int x, int y, int w, int h) {
            super(new painter.drawable.Rectangle(x, y, w, h));
        }

        @Override
        public Rectangle getThis() {
            return this;
        }
    }

    protected static class Square extends SimpleElement<Square> {
        public Square(int x, int y, int s) {
            super(new painter.drawable.Rectangle(x, y, s, s));
        }

        @Override
        public Square getThis() {
            return this;
        }
    }
}
