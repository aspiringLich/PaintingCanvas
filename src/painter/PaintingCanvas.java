package painter;

import painter.drawable.Text;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PaintingCanvas extends JPanel {
    public ArrayList<Text> text;

    public PaintingCanvas() {
        text = new ArrayList<Text>();
    }

    public void paint(Graphics g) {
        // draw text
        for (Text t : text) {
            g.setFont(new Font("Bold", 1, t.size));
            g.setColor(t.color);
            g.drawChars(t.text.toCharArray(), 0, t.text.length(), t.x, t.y);
        }
    }
}