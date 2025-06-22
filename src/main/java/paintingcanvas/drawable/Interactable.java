package paintingcanvas.drawable;

import java.awt.*;

public interface Interactable {
    /**
     * Check if the point intersects with the element.
     * @return true if the point intersects with the element, false otherwise.
     */
    boolean intersects(Point pos);

    /**
     * Check if the element is currently being hovered over by the mouse.
     * @return true if the element is hovered, false otherwise.
     */
    boolean hovered();

    /**
     * Check if the element has been clicked by the mouse.
     * @return true if the element is clicked, false otherwise.
     */
    boolean clicked();
}
