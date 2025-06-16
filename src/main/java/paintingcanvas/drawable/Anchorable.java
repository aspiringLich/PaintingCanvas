package paintingcanvas.drawable;

import paintingcanvas.misc.Anchor;
import paintingcanvas.misc.ElementContainer;

public interface Anchorable<T extends Drawable<T>> extends Drawable<T>, Positionable<T> {
    void internalSetAnchor(Anchor anchor);

    /**
     * Get the current anchor of the element.
     *
     * @return the {@link Anchor} of the element; how its positioned relative to its center
     */
    public Anchor getAnchor();

    /**
     * Set the anchor of the element.
     *
     * @param anchor the {@link Anchor} to set; how its positioned relative to its center
     * @return the original object to allow method chaining
     */
    default T setAnchor(Anchor anchor) {
        ElementContainer.atomic(() -> internalSetAnchor(anchor));
        return getThis();
    }
}
