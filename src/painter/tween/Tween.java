package painter.tween;

import painter.drawable.Drawable;

public abstract class Tween {
    /**
     * The frame at witch the tween should start
     */
    public int startFrame;
    /**
     * The length of the tween in frames
     */
    public int duration;
    public Drawable drawable;

    Tween(int startFrame, int duration, Drawable drawable) {
        this.startFrame = startFrame;
        this.duration = duration;
        this.drawable = drawable;
    }

    public void update(int frame) {
        if (frame >= startFrame && frame <= this.startFrame + this.duration)
            this.updateTween(this.drawable, frame - startFrame, this.duration);
    }

    abstract void updateTween(Drawable drawable, int frame, int duration);
}
