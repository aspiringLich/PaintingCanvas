package paintingcanvas.painter.misc;

import static paintingcanvas.painter.Painter.fps;

public enum TimeUnit {
    Frames(1),
    Milliseconds(fps / 1000f),
    Seconds(fps);

    final float frames;

    TimeUnit(float frames) {
        this.frames = frames;
    }

    public int asFrames(float time) {
        return (int) (time * this.frames);
    }
}
