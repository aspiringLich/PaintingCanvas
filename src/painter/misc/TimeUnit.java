package painter.misc;

import static painter.Painter.fps;

public enum TimeUnit {
    Frames(1),
    Seconds(fps);

    final int frames;

    TimeUnit(int frames) {
        this.frames = frames;
    }

    public int asFrames(float time) {
        return (int) (time * this.frames);
    }
}
