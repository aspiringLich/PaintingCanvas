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
public class InfoDisplay implements RenderLifecycle {
    private final Stroke dashStroke = new BasicStroke(
            2,
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

        // for some reason the graphics bounds and canvas bounds are different??
        // weird.
        var mouse = canvas.getMousePos();
        if (mouse == null) return;
        var scaledMousex = (int)(mouse.getX() * (bounds.getWidth() / (double) canvas.getWidth()));
        var scaledMousey = (int)(mouse.getY() * (bounds.getHeight() / (double) canvas.getHeight()));

        int textWidth = fontSize * 10;
        int textHeight = fontSize * 3;
        int pad = 5;

        int x = 10, y = 30;
        if (scaledMousex >= bounds.width - textWidth) x = -x - textWidth;
        if (scaledMousey >= bounds.height - textHeight) y = -textHeight;
        x += scaledMousex;
        y += scaledMousey;

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
        g.drawOval(scaledMousex - size / 2, scaledMousey - size / 2, size, size);

        g.setColor(textColor);
        g.setStroke(dashStroke);
        g.drawLine(scaledMousex, 0, scaledMousex, bounds.height);
        g.drawLine(0, scaledMousey, bounds.width, scaledMousey);

        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(x - pad, y - fontSize - pad, textWidth + pad * 2, textHeight + pad * 2);

        g.setFont(font);
        g.setColor(Color.white);
        g.drawString(String.format("pos: (%d, %d)", mouse.x, mouse.y), x, y);
        g.drawString(String.format("rgb: (%d, %d, %d)", red, green, blue), x, y + fontSize);
        g.drawString(String.format("hex: (#%06X)", hex & 0x00ffffff), x, y + fontSize * 2);
    }
}
