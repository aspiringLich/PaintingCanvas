package paintingcanvas.animation;

import paintingcanvas.drawable.Drawable;

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
    public Easing easing = Easing.linear();

    Animation(int startFrame, int duration, Drawable drawable) {
        this.startFrame = startFrame;
        this.duration = duration;
        this.drawable = drawable;
    }

    public Animation easing(Easing easing) {
        this.easing = easing;
        return this;
    }

    public void update(int frame) {
        if (frame >= startFrame && frame <= this.startFrame + this.duration)
            this.updateAnimation(this.drawable, frame - startFrame, this.duration);
    }

    abstract void updateAnimation(Drawable drawable, int frame, int duration);
}
