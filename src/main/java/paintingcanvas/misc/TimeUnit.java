package paintingcanvas.misc;

import paintingcanvas.InternalCanvas;
import paintingcanvas.canvas.Canvas;

/**
 * Some common time units.
 */
public enum TimeUnit {
    Frames(1),
    Milliseconds(InternalCanvas.options.fps / 1000f),
    Seconds(InternalCanvas.options.fps);

    final double frames;

    TimeUnit(double frames) {
        this.frames = frames;
    }

    /**
     * Get the number of frames in {@code time} units.
     *
     * @param time The number of units.
     * @return The number of frames.
     */
    public int asFrames(double time) {
        return (int) (time * this.frames);
    }
}
