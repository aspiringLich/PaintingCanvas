package paintingcanvas.canvas;

import java.awt.*;

// TODO: Make options private as they have getters and setters

/**
 * A list of options that control the behavior of the canvas
 */
public class CanvasOptions {
    /**
     * Whether to automatically add {@link paintingcanvas.drawable.Drawable Drawable}'s
     * upon construction / instantiation.
     * <p>
     * default: {@code true}
     */
    public boolean autoAdd = true;
    /**
     * Whether to automatically center what's on screen whenever the screen is resized
     * <p>
     * default: {@code true}
     */
    public boolean autoCenter = true;
    /**
     * Whether to do antialiasing when drawing shapes
     * <p>
     * default: {@code true}
     */
    public boolean antiAlias = true;

    /**
     * The background color of the canvas
     * <p>
     * default: {@link Color#WHITE}
     */
    public Color backgroundColor = Color.WHITE;
    /**
     * The FPS to run rendering at
     * <p>
     * default: {@code 30}
     */
    public int fps = 30;

    public CanvasOptions() {
    }

    /**
     * @return Weather {@link paintingcanvas.drawable.Drawable Drawable}s are automatically added to the canvas upon construction
     * @see #autoAdd(boolean)
     */
    public boolean isAutoAdd() {
        return autoAdd;
    }

    /**
     * Default: {@code true}
     *
     * @param autoAdd Whether to automatically add {@link paintingcanvas.drawable.Drawable Drawable}'s upon construction / instantiation.
     * @return This object for chaining
     * @see #isAutoAdd()
     */
    public CanvasOptions autoAdd(boolean autoAdd) {
        this.autoAdd = autoAdd;
        return this;
    }

    /**
     * @return Whether to automatically center what's on screen whenever the screen is resized
     * @see #autoCenter(boolean)
     */
    public boolean isAutoCenter() {
        return autoCenter;
    }

    /**
     * Default: {@code true}
     *
     * @param autoCenter Whether to automatically center what's on screen whenever the screen is resized
     * @return This object for chaining
     * @see #isAutoCenter()
     */
    public CanvasOptions autoCenter(boolean autoCenter) {
        this.autoCenter = autoCenter;
        return this;
    }

    /**
     * @return Whether to use antialiasing
     * @see #antiAlias(boolean)
     */
    public boolean isAntiAlias() {
        return antiAlias;
    }

    /**
     * Default: {@code true}
     *
     * @param antiAlias Whether to use antialiasing
     * @return This object for chaining
     * @see #isAntiAlias()
     */
    public CanvasOptions antiAlias(boolean antiAlias) {
        this.antiAlias = antiAlias;
        return this;
    }

    /**
     * @return The background color of the canvas
     * @see #backgroundColor(Color)
     */
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * Default: {@link Color#WHITE}
     *
     * @param backgroundColor The background color of the canvas
     * @return This object for chaining
     * @see #getBackgroundColor()
     */
    public CanvasOptions backgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }
}
