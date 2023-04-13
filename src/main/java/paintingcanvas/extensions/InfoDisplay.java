package paintingcanvas.extensions;

import paintingcanvas.canvas.Canvas;
import paintingcanvas.canvas.RenderLifecycle;

import java.awt.*;
import java.awt.image.ColorModel;

/**
 * Displays some information on the screen, such as
 * the mouse position and color of the hovered pixel.
 */
public class InfoDisplay implements RenderLifecycle {
    private static final Stroke stroke = new BasicStroke(
            2,
            BasicStroke.CAP_ROUND,
            BasicStroke.JOIN_ROUND,
            1f,
            new float[]{10f, 10f},
            0f
    );
    private static final Font font = new Font("", Font.BOLD, 16);

    public InfoDisplay() {

    }

    public void attach() {
        Canvas.getGlobalInstance().renderLifecycles.add(this);
    }

    @Override
    public void renderEnd(Graphics g) {
        var gc = (Graphics2D) g;
        var canvas = Canvas.getGlobalInstance();
        var width = canvas.getWidth();
        var height = canvas.getHeight();

        var frame = canvas.panel.jframe;
        var mouse = frame.getMousePosition();

        if (mouse == null) {
            return;
        }
        mouse.y -= frame.getRootPane().getY();
        mouse.x -= frame.getRootPane().getX();
        gc.setColor(Color.black);
        gc.setStroke(stroke);
        gc.drawLine(mouse.x, 0, mouse.x, height);
        gc.drawLine(0, mouse.y, width, mouse.y);

        gc.setFont(font);
        int x = 10, y = 20;
        if (mouse.x >= width - 100) x = -100;
        if (mouse.y >= height - 20) y = -10;

        gc.drawString(String.format("(%d, %d)", mouse.x, mouse.y), mouse.x + x, mouse.y + y);
    }
}
