package paintingcanvas;

import paintingcanvas.animation.Animation;
import paintingcanvas.canvas.Canvas;
import paintingcanvas.canvas.CanvasOptions;
import paintingcanvas.canvas.CanvasPanel;
import paintingcanvas.canvas.RenderLifecycle;
import paintingcanvas.misc.ElementContainer;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.Vector;

/**
 * <p>
 *     Static class that handles all the internal stuff in the package;
 *     Cleans up documentation for {@link paintingcanvas.canvas.Canvas}
 * </p>
 * <p>
 *     Also yes everything is static under the hood sue me
 * </p>
 * <p>
 *     An instance of the globally accessible canvas object can simply be
 *     accessed with {@link InternalCanvas#canvas}
 * </p>
 */
public class InternalCanvas {
    /**
     * Sync with drawables: Use when modifying a drawable
     */
    public static final Object drawableSync = new Object();
    /**
     * Sync with animations: Notifies on animation finish
     */
    public static final Object animationSync = new Object();
    /**
     * Sync with frame: Notifies on end of frame
     */
    public static final Object frameSync = new Object();
    /**
     * the elements that are currently on the canvas
     */
    public static final ElementContainer elements = new ElementContainer();
    /**
     * the list of animations that are currently running
     */
    public static final List<Animation> animations = new Vector<>();
    /**
     * The current frame
     */
    public static int frame = -1;
    /**
     * The RenderLifecycle: allows you to write code to run before and after a frame is rendered
     */
    public static List<RenderLifecycle> renderLifecycles = new Vector<>();
    /**
     * Whether this has been initialized yet
     */
    public static boolean initialized = false;
    /**
     * the initial size of the Canvas
     */
    public static Dimension startSize = null;
    /**
     * The translation of the canvas, used for centering and panning
     */
    public static Point2D.Float translation = null;
    /**
     * Sync for translation: Use when modifying the translation
     */
    public static final Object translationSync = new Object();
    /**
     * The current mouse position on the canvas, if anything.
     */
    public static Point mousePosition = null;
    /**
     * The options for the behavior of the canvas, see {@link CanvasOptions}
     */
    public static CanvasOptions options = null;
    /**
     * A CanvasComponent, which handles all the rendering n stuff
     */
    public static CanvasPanel panel = null;
    public static Canvas canvas = null;
}
