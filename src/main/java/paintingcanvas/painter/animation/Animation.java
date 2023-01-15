package paintingcanvas.painter.animation;

import paintingcanvas.painter.App;
import paintingcanvas.painter.drawable.Drawable;

/**
 * A class that stores information about animations transitions
 */
public abstract class Animation {
    /**
     * The frame at which the animation should start
     */
    public int startFrame;
    /**
     * The length of the animation in frames
     */
    public int duration;
    public Drawable drawable;

    Animation(int startFrame, int duration, Drawable drawable) {
        this.startFrame = startFrame;
        this.duration = duration;
        this.drawable = drawable;
    }

    public void update(int frame) {
        if (frame >= startFrame && frame <= this.startFrame + this.duration)
            this.updateAnimation(this.drawable, frame - startFrame, this.duration);

        if (frame == this.startFrame + this.duration)
            this.endAnimation(this.drawable);
    }

    abstract void updateAnimation(Drawable drawable, int frame, int duration);

    void endAnimation(Drawable drawable) {
        synchronized (App.syncObject) {
            App.syncObject.notify();
        }
    }
}
