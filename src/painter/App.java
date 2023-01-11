package painter;

import painter.drawable.Drawable;
import painter.animation.ColorAnimation;
import painter.animation.MovementAnimation;
import painter.animation.RotationAnimation;
import painter.animation.Animation;

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

    protected abstract static class SimpleElement<T extends SimpleElement<T>> {
        Drawable _super;

        public SimpleElement(Drawable drawable) {
            this._super = drawable;
            painter.canvas.elements.add(this._super);
        }

        public abstract T getThis();

        /**
         * Hide the Object
         * @return The original object to allow method chaining
         */
        public T hide() {
            this._super.visible = false;
            return getThis();
        }

        /**
         * Show the Object
         * @return The original object to allow method chaining
         */
        public T show() {
            this._super.visible = true;
            return getThis();
        }

        /**
         * @return the X-position of the Object
         */
        public int getX() {
            return _super.x;
        }

        /**
         * Set the X-position of the object
         * @param x the new X-position of the Object
         * @return The original object to allow method chaining
         */
        public T setX(int x) {
            _super.x = x;
            return getThis();
        }

        /**
         * @return the Y-position of the object
         */
        public int getY() {
            return _super.y;
        }

        /**
         * Set the Y-position of the object
         * @param y the new Y-position of the Object
         * @return The original object to allow method chaining
         */
        public T setY(int y) {
            _super.y = y;
            return getThis();
        }

        /**
         * Set the color of the object with rgb
         * @param r red (0 - 255)
         * @param g green (0 - 255)
         * @param b blue (0 - 255)
         * @return The original object to allow method chaining
         */
        public T setColor(int r, int g, int b) {
            this._super.color = new Color(r, g, b);
            return getThis();
        }

        /**
         * Set the color of the object with a Color object
         * @param color color.
         * @return The original object to allow method chaining
         */
        public T setColor(Color color) {
            this._super.color = color;
            return getThis();
        }

        /**
         * Animate the object, TODO: this
         * @param tween
         * @param duration
         * @return
         */
        public T animate(Animation tween, int duration) {
            tween.drawable = this._super;
            tween.startFrame = builderFrame;
            tween.duration = duration;
            lastBuilderFrame = builderFrame;
            builderFrame += duration;
            painter.canvas.tweens.add(tween);
            return getThis();
        }

        /**
         * Animate the object, TODO: this
         * @param tween
         * @param duration
         * @return
         */
        public T animateWith(Animation tween, int duration) {
            tween.drawable = this._super;
            tween.startFrame = lastBuilderFrame;
            tween.duration = duration;
            painter.canvas.tweens.add(tween);
            return getThis();
        }

        // TODO: ScheduleAnimation (i think that's a good name) methods to schedule an animation (wow)

        /**
         * Rotate this object dRotation degrees
         * @param dRotation change in rotation.
         * @return The original object to allow method chaining
         */
        public T rotate(double dRotation) {
            this._super.rotation += dRotation;
            return getThis();
        }

        /**
         * Rotate this object to rotation degrees
         * @param rotation rotation to rotate to
         * @return The original object to allow method chaining
         */
        public T rotateTo(double rotation) {
            this._super.rotation = rotation;
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

    protected Animation colorTo(int r, int g, int b) {
        return colorTo(new Color(r, g, b));
    }

    protected Animation colorTo(Color color) {
        return new ColorAnimation(builderFrame, color, 0, null);
    }

    protected Animation moveTo(int x, int y) {
        return new MovementAnimation(builderFrame, 0, new Point(x, y), null);
    }

    protected Animation rotateTo(int angle) {
        return new RotationAnimation(builderFrame, 0, Math.toRadians(angle), null);
    }
}
