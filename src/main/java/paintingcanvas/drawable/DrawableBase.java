package paintingcanvas.drawable;

import paintingcanvas.InternalCanvas;
import paintingcanvas.canvas.CanvasNotInitializedException;
import paintingcanvas.misc.Anchor;
import paintingcanvas.misc.ElementContainer;

import java.awt.*;
import java.awt.geom.AffineTransform;

public abstract class DrawableBase<T extends Drawable<T>> implements Drawable<T>, Positionable<T>, Colorable<T> {
    int layer = 0;
    boolean visible = true;
    double rotation;
    Color color;
    int x;
    int y;

    public DrawableBase(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;

        if (!InternalCanvas.initialized) {
            throw new CanvasNotInitializedException();
        }

        if (InternalCanvas.options.autoAdd) {
            ElementContainer.atomic(() -> InternalCanvas.elements.add(this));
        }
    }

    @Override
    public void internalSetLayer(int layer) {
        this.layer = layer;
    }

    @Override
    public void render(Graphics2D g) {
        if (!this.visible) return;
        var save = g.getTransform();
        var transform = g.getTransform();

        var center = this.center(g);
        transform.rotate(this.rotation, center.x, center.y);
        transform.translate(this.x, this.y);
        g.setTransform(transform);

        this.draw(g);

        g.setTransform(save);
    }

    abstract void draw(Graphics2D g);

    @Override
    public T hide() {
        visible = false;
        return getThis();
    }

    @Override
    public T show() {
        visible = true;
        return getThis();
    }

    @Override
    public int getLayer() {
        return this.layer;
    }

    @Override
    public void internalSetPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public double internalGetRotation() {
        return rotation;
    }

    @Override
    public void internalSetRotation(double rotation) {
        this.rotation = rotation;
    }

    @Override
    public Point getPos() {
        return new Point(x, y);
    }

    @Override
    public double getRotation() {
        return Math.toDegrees(rotation);
    }

    @Override
    public void internalSetColor(Color color) {
        this.color = color;
    }

    @Override
    public Color getColor() {
        return this.color;
    }

    public abstract static class Shape<T extends Drawable<T>> extends OutlineableDrawableBase<T> implements Anchorable<T> {
        Anchor anchor = Anchor.CENTER;

        public Shape(int x, int y, Color color) {
            super(x, y, color);
        }

        @Override
        public void internalSetAnchor(Anchor anchor) {
            this.anchor = anchor;
        }

        @Override
        public Anchor getAnchor() {
            return null;
        }
    }

    public abstract static class OutlineableDrawableBase<T extends Drawable<T>> extends DrawableBase<T> implements Outlineable<T> {
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
}
