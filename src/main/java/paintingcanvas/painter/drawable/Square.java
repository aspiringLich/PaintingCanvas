package paintingcanvas.painter.drawable;

import java.awt.*;

/**
 * A Square element.
 * <pre>{@code
 * // Create a new Square at (100, 100) with a size of 30px
 * Square square = new Square(100, 100, 30);
 * }</pre>
 */
public class Square extends Drawable<Square> {
    int size;
    /**
     * Create a new Square element.
     * <pre>{@code
     * Square square = new Square(100, 100, 30);
     * }</pre>
     *
     * @param x The X-position of the square
     * @param y The Y-position of the square
     * @param s The size of the square
     */
    public Square(int x, int y, int s) {
        super(x, y, Color.BLACK);
        this.size = s;
    }
    
    @Override
    public void draw(Graphics2D gc) {
        gc.setColor(this.color);
        if (this.filled) gc.fillRect(x, y, size, size);
        else gc.drawRect(x, y, size, size);
    }
    
    @Override
    public Point center(Graphics g) {
        return new Point(x + size / 2, y + size / 2);
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