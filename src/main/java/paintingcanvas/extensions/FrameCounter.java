package paintingcanvas.extensions;

import paintingcanvas.App;
import paintingcanvas.Canvas;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class FrameCounter implements Canvas.CanvasComponent.RenderLifecycle {
    boolean enabled = true;
    boolean frameChart = true;
    int dataPoints = 100;
    GetLines lines = () -> new String[]{};
    long lastFrame = System.currentTimeMillis();
    Vector<Long> frameTimes = new Vector<>();

    public FrameCounter() {
        for (int i = 0; i < dataPoints; i++) frameTimes.add(0L);
    }

    public FrameCounter enabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public FrameCounter frameChart(boolean enabled) {
        this.frameChart = enabled;
        return this;
    }

    public FrameCounter dataPoints(int dataPoints) {
        this.dataPoints = dataPoints;
        return this;
    }

    public FrameCounter lines(GetLines lines) {
        this.lines = lines;
        return this;
    }

    public void attach() {
        App.canvas.canvas.renderLifecycle = this;
    }

    @Override
    public void renderEnd(Graphics g) {
        // Update frame times
        var now = System.currentTimeMillis();
        frameTimes.add(now - lastFrame);
        while (frameTimes.size() > dataPoints) frameTimes.remove(0);
        lastFrame = now;
        if (!enabled) return;

        // Get average & max time
        var sum = 0;
        var max = 0;
        for (var i : frameTimes) {
            sum += i;
            max = (int) Math.max(i, max);
        }
        var avg = (float) (sum) / frameTimes.size();


        // Draw UI
        var gc = (Graphics2D) g;
        var fh = gc.getFontMetrics().getHeight();
        var text = new ArrayList<>(List.of(this.lines.getLines()));
        text.add(0, String.format("FPS: %d", (int) (1000 / avg)));
        text.add(1, String.format("FrameTime: %.1f", avg));
        text.add(2, String.format("Elements: %d", App.canvas.canvas.elements.size()));

        gc.setColor(new Color(0, 0, 0, 180));
        var maxText = 0;
        for (var e : text) maxText = Math.max(maxText, gc.getFontMetrics().stringWidth(e));
        gc.fillRect(5, 5, maxText + 10, fh * text.size() + 40);

        gc.setColor(Color.WHITE);
        var i = 0;
        for (var e : text) gc.drawString(e, 10, 20 + (i++ * fh));

        // Frame time graph
        if (!frameChart || max <= 0) return;
        var size = frameTimes.size();
        var frameX = new int[size];
        var frameY = new int[size];
        for (var inc = 0; inc < size; inc++) {
            frameX[inc] = inc * maxText / size + 10;
            frameY[inc] = (int) ((max - frameTimes.get(inc).intValue()) / (float) max * 30 + 10 + i * fh);
        }

        gc.drawPolyline(frameX, frameY, size);
    }

    public interface GetLines {
        String[] getLines();
    }
}