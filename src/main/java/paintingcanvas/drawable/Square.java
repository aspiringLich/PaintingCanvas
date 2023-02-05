package paintingcanvas.drawable;

import java.awt.*;

/**
 * A Square element.
 * <pre>{@code
 * // Create a new Square at (100, 100) with a size of 30px
 * Square square = new Square(100, 100, 30);
 * }</pre>
 */
public class Square extends Drawable<Square> {
    /**
     * The size / side-length of the square.
     */
    public int size;

    /**
     * Create a new Square element.
     * <pre>{@code
     * // Created a square centered at (100, 100) with a side length of 30px
     * Square square = new Square(100, 100, 30);
     * }</pre>
     *
     * @param centerX The X-position of the square
     * @param centerY The Y-position of the square
     * @param size    The size of the square
     */
    public Square(int centerX, int centerY, int size) {
        super(centerX, centerY, Color.BLACK);
        this.size = size;
    }

    /**
     * Create a new Square element.
     * <pre>{@code
     * // Created a red square centered at (100, 100) with a side length of 30px
     * Square square = new Square(100, 100, 30, new Color(255, 0, 0));
     * }</pre>
     *
     * @param centerX The X-position of the square
     * @param centerY The Y-position of the square
     * @param size    The size of the square
     * @param color   The color of the square
     */
    public Square(int centerX, int centerY, int size, Color color) {
        super(centerX, centerY, color);
        this.size = size;
    }

    @Override
    public void draw(Graphics2D gc) {
        gc.setColor(this.color);
        if (this.filled) gc.fillRect(x - size / 2, y - size / 2, size, size);
        else gc.drawRect(x - size / 2, y - size / 2, size, size);
    }

    @Override
    public Point center(Graphics g) {
        return new Point(x, y);
    }

    @Override
    public Square getThis() {
        return this;
    }

    /**
     * Gets the size of the square.
     *
     * @return The size of the square in pixels
     * @see #setSize(int)
     */
    public int getSize() {
        return this.size;
    }

    /**
     * Sets the size of the square.
     *
     * @param s The new size of the square in pixels
     * @return The original object to allow method chaining
     * @see #getSize()
     */
    public Square setSize(int s) {
        this.size = s;
        return this;
    }
}