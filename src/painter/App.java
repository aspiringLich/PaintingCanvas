package painter;

import painter.drawable.Drawable;

import java.awt.*;

/**
 * an abstract class to allow interfacing with the painter library whilst keeping the painter library distinct
 */
public abstract class App {
    /**
     * The global Painter all Drawables access to add themselves to.
     */
    public static Painter painter;

    public void render() {}

    protected abstract static class SimpleElement<T> {
        Drawable _super;

        public SimpleElement(Drawable drawable) {
            this._super = drawable;
            painter.canvas.elements.add(this._super);
        }

        public T hide() {
            this._super.visible = false;
            return (T) this;
        }

        public T show() {
            this._super.visible = true;
            return (T) this;
        }

        public int getX() {
            return _super.x;
        }

        public T setX(int x) {
            _super.x = x;
            return (T) this;
        }

        public int getY() {
            return _super.y;
        }

        public T setY(int y) {
            _super.y = y;
            return (T) this;
        }

        public T setColor(int r, int g, int b) {
            this._super.color = new Color(r, g, b);
            return (T) this;
        }

        public T setColor(Color color) {
            this._super.color = color;
            return (T) this;
        }
    }

    protected static class Text extends SimpleElement<Text> {
        public Text(int x, int y, String text) {
            super(new painter.drawable.Text(x, y, text, 30));
        }

        public Text setText(String text) {
            ((painter.drawable.Text) this._super).text = text;
            return this;
        }
    }
}
