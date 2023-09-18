package paintingcanvas.misc;

import paintingcanvas.canvas.Canvas;
import paintingcanvas.drawable.Drawable;

import java.util.Comparator;
import java.util.List;
import java.util.Vector;

// TODO: Put drawable sync here
public class ElementContainer {
    final List<Drawable<?>> elements = new Vector<>();
    boolean dirty = false;
    int minLayer = 0;
    int maxLayer = 0;

    public void foreach(DrawableConsumer consumer) {
        prepareRender();
        for (Drawable<?> drawable : elements) {
            consumer.accept(drawable);
        }
    }

    void prepareRender() {
        if (!dirty) return;
        synchronized (Canvas.drawableSync) {
            elements.sort(Comparator.comparingInt(a -> {
                int layer = a.getLayer();
                minLayer = Math.min(minLayer, layer);
                maxLayer = Math.max(maxLayer, layer);
                return layer;
            }));
        }
        dirty = false;
    }

    public void setDirty() {
        this.dirty = true;
    }

    public int getMinLayer() {
        return minLayer;
    }

    public int getMaxLayer() {
        return maxLayer;
    }

    public int size() {
        return elements.size();
    }

    public void add(Drawable<?> drawable) {
        elements.add(drawable);
        dirty = true;
    }

    public void remove(Drawable<?> drawable) {
        elements.remove(drawable);
        dirty = true;
    }

    public static interface DrawableConsumer {
        void accept(Drawable<?> drawable);
    }
}
