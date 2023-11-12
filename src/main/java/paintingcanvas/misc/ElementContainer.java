package paintingcanvas.misc;

import paintingcanvas.drawable.Drawable;

import java.util.Comparator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Callable;

// TODO: Put drawable sync here
public class ElementContainer {
    static final Object drawableSync = new Object();
    final List<Drawable<?>> elements = new Vector<>();
    boolean dirty = false;
    int minLayer = 0;
    int maxLayer = 0;

    /**
     * ALL MODIFICATIONS TO DRAWABLES MUST BE DONE THROUGH THIS METHOD
     *
     * @param callable The code to run
     */
    public static <T> T atomic(Callable<T> callable) {
        synchronized (drawableSync) {
            try {
                return callable.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * ALL MODIFICATIONS TO DRAWABLES MUST BE DONE THROUGH THIS METHOD
     *
     * @param runnable The code to run
     */
    public static void atomic(Runnable runnable) {
        synchronized (drawableSync) {
            runnable.run();
        }
    }

    public void foreach(DrawableConsumer consumer) {
        synchronized (drawableSync) {
            if (dirty) {
                elements.sort(Comparator.comparingInt(a -> {
                    int layer = a.getLayer();
                    minLayer = Math.min(minLayer, layer);
                    maxLayer = Math.max(maxLayer, layer);
                    return layer;
                }));
                dirty = false;
            }

            for (Drawable<?> drawable : elements) {
                consumer.accept(drawable);
            }
        }
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
        synchronized (drawableSync) {
            elements.add(drawable);
        }
        dirty = true;
    }

    public void remove(Drawable<?> drawable) {
        synchronized (drawableSync) {
            elements.remove(drawable);
        }
        dirty = true;
    }

    public interface DrawableConsumer {
        void accept(Drawable<?> drawable);
    }

    @FunctionalInterface
    public interface AtomicExecutor {
        void execute();
    }
}
