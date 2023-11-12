package paintingcanvas.extensions;

import paintingcanvas.InternalCanvas;
import paintingcanvas.canvas.RenderLifecycle;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * A system to add a debug overlay showing the FPS,
 * frame count, Element count and frame time graph.
 * <pre>{@code
 * // Create and attach a new FrameCounter with default settings
 * new FrameCounter().attach();
 * }</pre>
 */
@SuppressWarnings("unused")
public class FrameCounter implements RenderLifecycle {
    final Vector<Long> frameTimes = new Vector<>();
    boolean enabled = true;
    boolean frameChart = true;
    int dataPoints = 200;
    List<GetLines> lines = new ArrayList<>();
    long lastFrame = System.currentTimeMillis();
    Font font;

    /**
     * Create a new FrameCounter with default settings:
     * <ul>
     *     <li>Enabled: true</li>
     *     <li>FrameChart: true</li>
     *     <li>DataPoints: 100</li>
     *     <li>Lines: {}</li>
     * </ul>
     */
    public FrameCounter() {
        this.font = new Font(Font.DIALOG, Font.PLAIN, 12);
    }

    /**
     * Enabled by default.
     *
     * @param enabled To enable or disable the overlay
     * @return `this` for method chaining
     */
    public FrameCounter enabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    /**
     * Enabled by default.
     *
     * @param enabled To enable or disable the frame time graph
     * @return `this` for method chaining
     */
    public FrameCounter frameChart(boolean enabled) {
        this.frameChart = enabled;
        return this;
    }

    /**
     * The number of data points used for the FPS and FrameTime text / FrameTime graph.
     * 100 by default.
     *
     * @param dataPoints The number of data-points to use
     * @return `this` for method chaining
     */
    public FrameCounter dataPoints(int dataPoints) {
        this.dataPoints = dataPoints;
        this.frameTimes.clear();
        for (int i = 0; i < dataPoints; i++) frameTimes.add(0L);
        return this;
    }

    /**
     * The function you supply will be run every frame.
     * Empty by default.
     *
     * @param lines Function to get extra lines.
     * @return `this` for method chaining
     */
    public FrameCounter lines(GetLines lines) {
        this.lines.add(lines);
        return this;
    }

    /**
     * The function you supply will be run every frame.
     * Empty by default.
     *
     * @param line The function to get an extra line
     * @return `this` for method chaining
     */
    public FrameCounter line(GetLine line) {
        this.lines.add(() -> new String[]{line.getLine()});
        return this;
    }

    /**
     * Sets the font used by the overlay UI.
     * 12pt Dialog is the default.
     *
     * @param font The font to use
     * @return `this` for method chaining
     */
    public FrameCounter font(Font font) {
        this.font = font;
        return this;
    }

    @Override
    public void renderEnd(Graphics2D g) {
        // Update frame times
        var now = System.currentTimeMillis();
        frameTimes.add(now - lastFrame);
        while (frameTimes.size() > dataPoints) frameTimes.remove(0);
        lastFrame = now;
        if (!enabled) return;

        // Get average & max / min time
        var sum = 0;
        var max = 0;
        var min = Integer.MAX_VALUE;
        for (var i : frameTimes) {
            sum += i;
            max = (int) Math.max(i, max);
            min = (int) Math.min(i, min);
        }
        var avg = (float) (sum) / frameTimes.size();


        // Draw UI
        g.setFont(font);
        g.setTransform(new AffineTransform());
        var fh = g.getFontMetrics().getHeight();
        var text = new ArrayList<String>();
        text.add(String.format("FPS: %d", (int) (1000 / avg)));
        text.add(String.format("FrameTime: %.1f", avg));
        text.add(String.format("Elements: %d", InternalCanvas.elements.size()));
        for (var i : this.lines) text.addAll(List.of(i.getLines()));

        g.setColor(new Color(0, 0, 0, 180));
        var maxText = 0;
        for (var e : text) maxText = Math.max(maxText, g.getFontMetrics().stringWidth(e));
        g.fillRect(5, 5, maxText + 10, fh * text.size() + 40);

        g.setColor(Color.WHITE);
        var i = 0;
        for (var e : text) g.drawString(e, 10, 20 + (i++ * fh));

        // Frame time graph
        if (!frameChart || max <= 0) return;
        var size = frameTimes.size();
        var frameX = new int[size];
        var frameY = new int[size];
        for (var inc = 0; inc < size; inc++) {
            frameX[inc] = inc * maxText / size + 10;
            var per = (frameTimes.get(inc).intValue() - min) / (float) (max - min);
            frameY[inc] = (int) (per * -30 + 40 + i * fh);
        }

        var stroke = g.getStroke();
        g.setStroke(new BasicStroke(BasicStroke.JOIN_ROUND));
        g.drawPolyline(frameX, frameY, size);
        g.setStroke(stroke);
    }

    public interface GetLines {
        String[] getLines();
    }

    public interface GetLine {
        String getLine();
    }
}