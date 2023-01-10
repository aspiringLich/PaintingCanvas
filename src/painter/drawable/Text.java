package painter.drawable;

import java.awt.*;
import javax.swing.*;
import java.util.*;

/**
 * painter.drawable.Text
 */
public class Text {
    public int x;
    public int y;
    public String text;
    public Color color;
    public int size;

    public Text(int x, int y, String text, int size) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.color = new Color(
                (float) Math.random(),
                (float) Math.random(),
                (float) Math.random());
        this.size = size;
    }

    public Text(int x, int y, String text, int size, int r, int g, int b) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.color = new Color(r, g, b);
        this.size = size;
    }
}