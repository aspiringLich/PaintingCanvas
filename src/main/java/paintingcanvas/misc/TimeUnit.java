package paintingcanvas.misc;

import paintingcanvas.Canvas;

/**
 * Some common time units.
 */
public enum TimeUnit {
    Frames(1),
    Milliseconds(Canvas.fps / 1000f),
    Seconds(Canvas.fps);

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
