package paintingcanvas.animation;

import paintingcanvas.App;
import paintingcanvas.drawable.Drawable;

import java.awt.*;

/**
 * A class that stores information about animations transitions
 */
public abstract class Animation {
    /**
     * The frame at which the animation should start
     */
    public int startFrame;
    /**
     * The length of the animation in frames
     */
    public int duration;
    public Drawable drawable;
    public Easing easing = Easing.linear();

    Animation(int startFrame, int duration, Drawable drawable) {
        this.startFrame = startFrame;
        this.duration = duration;
        this.drawable = drawable;
    }

    public Animation easing(Easing easing) {
        this.easing = easing;
        return this;
    }

    public void update(int frame) {
        if (frame >= startFrame && frame <= this.startFrame + this.duration)
            this.updateAnimation(this.drawable, frame - startFrame, this.duration);
    }

    abstract void updateAnimation(Drawable drawable, int frame, int duration);

    /**
     * Create a new animation to <code>color</code> in <a href="https://en.wikipedia.org/wiki/RGB_color_model">RGB</a>.
     * <pre>{@code
     * Circle o = new Circle(100, 100, 20);
     * o.animate().add(colorTo(255, 0, 0), 1);
     * }</pre>
     *
     * @param r The red component of the color (0-255)
     * @param g The green component of the color (0-255)
     * @param b The blue component of the color (0-255)
     * @return the {@link Animation} object
     */
    public static Animation colorTo(int r, int g, int b) {
        return colorTo(new Color(r, g, b));
    }

    /**
     * Create a new animation to <code>color</code> with a {@link Color} object.
     * <pre>{@code
     * Circle o = new Circle(100, 100, 20);
     * o.animate().add(colorTo(Color.RED), 1);
     * }</pre>
     *
     * @param color The color to animate to
     * @return the {@link Animation} object
     */
    public static Animation colorTo(Color color) {
        return new ColorAnimation(0, color, 0, null);
    }

    /**
     * Create a new animation to <code>color</code> with a <a href="https://en.wikipedia.org/wiki/RGB_color_model#Numeric_representations">8-bit RGB hex literal</a>.
     * <pre>{@code
     * Circle o = new Circle(100, 100, 20);
     * // 0xFF0000 is hex for (255, 0, 0), which is red
     * o.animate().add(colorTo(0xFF0000), 1);
     * }</pre>
     *
     * @param hex The color to animate to as a hex literal
     * @return the {@link Animation} object
     */
    public static Animation colorTo(int hex) {
        return new ColorAnimation(0, new Color(hex >> 16 & 0xff, hex >> 8 & 0xff, hex & 0xff), 0, null);
    }

    /**
     * Creates a new movement animation to <code>(x, y)</code>.
     *
     * @param x The absolute x position to move this element to
     * @param y The absolute y position to move this element to
     * @return the {@link Animation} object
     */
    public static Animation moveTo(int x, int y) {
        return new MovementAnimation(0, 0, new Point(x, y), null);
    }

    /**
     * Creates a new rotation animation to <code>angle°</code>.
     * If you supply an angle {@code > 360} it will make more than one full rotation.
     *
     * @param angle The absolute angle to rotate to in degrees.
     * @return the {@link Animation} object
     */
    public static Animation rotateTo(int angle) {
        return new RotationAnimation(0, 0, Math.toRadians(angle), null);
    }
}
