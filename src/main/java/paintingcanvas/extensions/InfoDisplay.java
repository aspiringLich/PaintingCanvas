package paintingcanvas.extensions;

import paintingcanvas.canvas.Canvas;
import paintingcanvas.canvas.RenderLifecycle;

import java.awt.*;
import java.awt.image.BufferedImage;

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
    private int fontSize = 12;
    private Font font = new Font(Font.DIALOG, Font.PLAIN, 12);
    ;
    private Color textColor = Color.black;

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
        ;
    }

    public void setFont(Font font) {
        this.font = font;
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

        var mouse = canvas.getMousePos();
        if (mouse == null) {
            return;
        }

        int textWidth = fontSize * 10;
        int textHeight = fontSize * 3;
        int pad = 5;

        int x = 10, y = 30;
        if (mouse.x >= width - textWidth) x = -x - textWidth;
        if (mouse.y >= height - textHeight) y = -textHeight;
        x += mouse.x;
        y += mouse.y;

        var cmp = canvas.panel;
        var img = new BufferedImage(cmp.getWidth(), cmp.getHeight(), BufferedImage.TYPE_INT_RGB);
        var imgGraphics = img.getGraphics();
        cmp.simplePaint(imgGraphics);
        imgGraphics.dispose();

        int hex;
        try {
            hex = img.getRGB(mouse.x, mouse.y);
        } catch (ArrayIndexOutOfBoundsException e) {
            hex = 0xffffffff;
        }
        var color = new Color(hex);
        int red = color.getRed(), green = color.getGreen(), blue = color.getBlue();

        gc.setColor(color);
        gc.setStroke(stroke);
        var size = 40;
        gc.drawOval(mouse.x - size / 2, mouse.y - size / 2, size, size);

        gc.setColor(textColor);
        gc.setStroke(dashStroke);
        gc.drawLine(mouse.x, 0, mouse.x, height);
        gc.drawLine(0, mouse.y, width, mouse.y);

        gc.setColor(new Color(0, 0, 0, 180));
        gc.fillRect(x - pad, y - fontSize - pad, textWidth + pad * 2, textHeight + pad * 2);

        gc.setFont(font);
        gc.setColor(Color.white);
        gc.drawString(String.format("pos: (%d, %d)", mouse.x, mouse.y), x, y);
        gc.drawString(String.format("rgb: (%d, %d, %d)", red, green, blue), x, y + fontSize);
        gc.drawString(String.format("hex: (#%06X)", hex & 0x00ffffff), x, y + fontSize * 2);
    }
}
