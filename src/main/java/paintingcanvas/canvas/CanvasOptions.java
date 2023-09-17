package paintingcanvas.canvas;

import java.awt.*;

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

    public CanvasOptions() {
    }
}
