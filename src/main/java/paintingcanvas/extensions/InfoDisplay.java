package paintingcanvas.extensions;

import paintingcanvas.InternalCanvas;
import paintingcanvas.canvas.RenderLifecycle;

import java.awt.*;

/**
 * Displays some information on the screen, such as
 * the mouse position and color of the hovered pixel.
 * <pre>{@code
 * // Create and attach a new InfoDisplay with default settings
 * new InfoDisplay().attach();
 * }</pre>
 */
@SuppressWarnings("unused")
public class InfoDisplay implements RenderLifecycle {
    private final Stroke dashStroke = new BasicStroke(
            2f,
            BasicStroke.CAP_ROUND,
            BasicStroke.JOIN_ROUND,
            1f,
            new float[]{10f, 10f},
            0f
    );
    private final Stroke stroke = new BasicStroke(10);
    private final Color textColor = Color.BLACK;
    private int fontSize = 12;
    private Font font = new Font(Font.DIALOG, Font.PLAIN, 12);

    public InfoDisplay() {
    }

    /**
     * Set the font size of the displayed text
     *
     * @param fontSize The new font size
     */
    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
        this.font = new Font(Font.DIALOG, Font.PLAIN, fontSize);
    }

    public void setFont(Font font) {
        this.font = font;
    }

    @Override
    public void renderEnd(Graphics2D g) {
        var canvas = InternalCanvas.canvas;
        var bounds = g.getClipBounds();

        var mouse = canvas.getMousePos();
        if (mouse == null) return;

        int textWidth = fontSize * 10;
        int textHeight = fontSize * 3;
        int pad = 5;

        int x = 10, y = 30;
        if (mouse.x >= bounds.width - textWidth) x = -x - textWidth;
        if (mouse.y >= bounds.height - textHeight) y = -textHeight;
        x += mouse.x;
        y += mouse.y;

        var cmp = InternalCanvas.panel;

        int hex;
        try {
            hex = cmp.image.getRGB(mouse.x, mouse.y);
        } catch (ArrayIndexOutOfBoundsException e) {
            hex = 0xffffffff;
        }
        var color = new Color(hex);
        int red = color.getRed(), green = color.getGreen(), blue = color.getBlue();

        g.setColor(color);
        g.setStroke(stroke);
        var size = 40;
        g.drawOval(mouse.x - size / 2, mouse.y - size / 2, size, size);

        g.setColor(textColor);
        g.setStroke(dashStroke);
        g.drawLine(mouse.x, 0, mouse.x, bounds.height);
        g.drawLine(0, mouse.y, bounds.width, mouse.y);

        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(x - pad, y - fontSize - pad, textWidth + pad * 2, textHeight + pad * 2);

        g.setFont(font);
        g.setColor(Color.white);
        g.drawString(String.format("pos: (%d, %d)", mouse.x, mouse.y), x, y);
        g.drawString(String.format("rgb: (%d, %d, %d)", red, green, blue), x, y + fontSize);
        g.drawString(String.format("hex: (#%06X)", hex & 0x00ffffff), x, y + fontSize * 2);
    }
}
