package paintingcanvas.drawable;

import paintingcanvas.misc.Anchor;

import java.awt.*;

public abstract class Shape<T extends Drawable<T>> extends OutlineableDrawableBase<T> implements Anchorable<T> {
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
