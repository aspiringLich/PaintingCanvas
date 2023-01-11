package painter.animation;

import painter.drawable.Drawable;

/**
 * Controls animation that is to do with rotation
 */
public class RotationAnimation extends Animation {
    private final double end;
    private Double start = null;

    public RotationAnimation(int start, int duration, double end, Drawable drawable) {
        super(start, duration, drawable);
        this.end = end;
    }

    @Override
    void updateAnimation(Drawable drawable, int frame, int duration) {
        if (frame == 0 || start == null) start = drawable.rotation;
        drawable.rotation = start + (end - start) * (frame / (float) duration);
    }
}
