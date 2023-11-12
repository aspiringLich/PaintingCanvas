package paintingcanvas.drawable;

import paintingcanvas.InternalCanvas;
import paintingcanvas.animation.Animatable;
import paintingcanvas.animation.AnimationBuilder;
import paintingcanvas.misc.ElementContainer;

import java.awt.*;

/**
 * An interface to connect to any objects that can be considered "painter.drawable.Drawable".
 * Provides various common methods to draw objects to a screen
 */
public interface Drawable<T extends Drawable<T>> extends Animatable {
    void internalSetLayer(int layer);

    /**
     * Get the object's centerpoint
     *
     * <pre>{@code
     * import java.awt.Point;
     *
     * Point p = drawable.getStartpoint();
     * int x = p.x;
     * int y = p.y;
     * }</pre>
     *
     * @param g Graphics context
     * @return the object's center-point
     */
    Point center(Graphics2D g);

    /**
     * Actually render the object itself
     * <p>
     * This calls the draw methods, but does some extra steps beforehand to lay it out correctly
     * <p>
     *
     * @param g the graphics context to draw the object with
     */
    void render(Graphics2D g);

    T getThis();

    /**
     * Hide the Object.
     * <pre>{@code
     * Circle o = new Circle(100, 100, 20);
     * o.hide();
     * }</pre>
     *
     * @return the original object to allow method chaining
     * @see #show()
     */
    T hide();

    /**
     * Show the Object
     * <pre>{@code
     * Circle o = new Circle(100, 100, 20);
     * o.show();
     * }</pre>
     *
     * @return the original object to allow method chaining
     * @see #hide()
     */
    T show();

    /**
     * Gets the current layer of the object.
     * By default, all objects are on layer 0.
     *
     * @return the original object to allow method chaining
     * @see #setLayer(int)
     * @see #bringToFront()
     * @see #sendToBack()
     */
    int getLayer();

    /**
     * Puts the object on a specific layer.
     *
     * @param layer the layer to set the object to
     * @return the original object to allow method chaining
     * @see #getLayer()
     * @see #bringToFront()
     * @see #sendToBack()
     */
    default T setLayer(int layer) {
        ElementContainer.atomic(() -> {
            internalSetLayer(layer);
            InternalCanvas.elements.setDirty();
        });
        return getThis();
    }

    /**
     * Brings the object in front of all other objects.
     *
     * @return the original object to allow method chaining
     * @see #getLayer()
     * @see #setLayer(int)
     * @see #sendToBack()
     */
    default T bringToFront() {
        return this.setLayer(InternalCanvas.elements.getMaxLayer() + 1);
    }

    /**
     * Puts the object behind all other objects.
     *
     * @return the original object to allow method chaining
     * @see #getLayer()
     * @see #setLayer(int)
     * @see #bringToFront()
     */
    default T sendToBack() {
        return this.setLayer(InternalCanvas.elements.getMinLayer() - 1);
    }

    /**
     * Erase this object from the canvas. This object will be gone and cannot* be added back!
     * <p>
     * <sub>* It can be added back just don't tell anyone shhhh</sub>
     * </p>
     */
    default void erase() {
        ElementContainer.atomic(() -> {
            InternalCanvas.elements.remove(this);
            InternalCanvas.elements.setDirty();
        });
    }

    /**
     * Start animating this object.
     *
     * @return An {@link AnimationBuilder} to start animating this object.
     */
    default AnimationBuilder animate() {
        return new AnimationBuilder(this);
    }

    @Override
    default Drawable<?> drawable() {
        return this;
    }
}
