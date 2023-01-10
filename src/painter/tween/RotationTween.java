package painter.tween;

import painter.drawable.Drawable;

public class RotationTween extends Tween {
    private final double end;
    private double start;

    public RotationTween(int start, int duration, double end, Drawable drawable) {
        super(start, duration, drawable);
        this.start = drawable.rotation;
        this.end = end;
    }

    @Override
    void updateTween(Drawable drawable, int frame, int duration) {
        if (frame == 0) start = drawable.rotation;
        drawable.rotation = start + (end - start) * (frame / (float) duration);
    }
}
