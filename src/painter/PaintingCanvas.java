package painter;

import painter.drawable.Drawable;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PaintingCanvas extends JComponent {
    public ArrayList<Drawable> objects;

    public PaintingCanvas() {
        objects = new ArrayList<>();
    }

    /**
     * Paint stuf
     *
     * @param g  the <code>Graphics</code> context in which to paint
     */
    public void paintComponent(Graphics g) {
        for (Drawable object : objects) object.draw(g);
    }
}