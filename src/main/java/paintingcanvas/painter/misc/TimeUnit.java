package paintingcanvas.painter.misc;

import static paintingcanvas.painter.Painter.fps;

/**
 * Some common time units.
 */
public enum TimeUnit {
    Frames(1),
    Milliseconds(fps / 1000f),
    Seconds(fps);

    final float frames;

    TimeUnit(float frames) {
        this.frames = frames;
    }

    /**
     * Get the number of frames in {@code time} units.
     *
     * @param time The number of units.
     * @return The number of frames.
     */
    public int asFrames(float time) {
        return (int) (time * this.frames);
    }
}
