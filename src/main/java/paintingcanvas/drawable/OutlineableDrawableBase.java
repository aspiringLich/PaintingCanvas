package paintingcanvas.drawable;

import java.awt.*;

public abstract class OutlineableDrawableBase<T extends Drawable<T>> extends DrawableBase<T> implements Outlineable<T> {
    Stroke outlineStroke = null;
    Color outlineColor = Color.BLACK;
    boolean filled = true;

    public OutlineableDrawableBase(int x, int y, Color color) {
        super(x, y, color);
    }

    @Override
    void draw(Graphics2D g) {
        if (this.filled) {
            g.setColor(this.color);
            this.drawFill(g);
        }
        if (this.outlineStroke != null) {
            g.setStroke(this.outlineStroke);
            g.setColor(this.outlineColor);
            this.drawOutline(g);
        }
    }

    abstract void drawFill(Graphics2D g);

    abstract void drawOutline(Graphics2D g);

    @Override
    public void internalSetOutlineStroke(Stroke stroke) {
        this.outlineStroke = stroke;
    }

    @Override
    public void internalSetOutlineColor(Color color) {
        this.outlineColor = color;
    }

    @Override
    public Color getOutlineColor() {
        return this.outlineColor;
    }

    @Override
    public Stroke getOutlineStroke() {
        return this.outlineStroke;
    }

    @Override
    public void internalSetFilled(boolean filled) {
        this.filled = filled;
    }
}
