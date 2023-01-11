package painter.animation;

import painter.drawable.Drawable;

/**
 * Controls animation that is to do with rotation
 */
public class RotationAnimation extends Animation {
    private final double end;
    private double start;

    public RotationAnimation(int start, int duration, double end, Drawable drawable) {
        super(start, duration, drawable);
        this.end = end;
    }

    @Override
    void updateAnimation(Drawable drawable, int frame, int duration) {
        if (frame == 0) start = drawable.rotation;
        drawable.rotation = start + (end - start) * (frame / (float) duration);
    }
}
