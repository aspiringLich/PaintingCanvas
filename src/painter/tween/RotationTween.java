package painter.tween;

import painter.drawable.Drawable;

public class RotationTween extends Tween {
    private final double end;
    private Double start = null;

    public RotationTween(int start, int duration, double end, Drawable drawable) {
        super(start, duration, drawable);
        this.end = end;
    }

    @Override
    void updateTween(Drawable drawable, int frame, int duration) {
        if (frame == 0 || start == null) start = drawable.rotation;
        drawable.rotation = start + (end - start) * (frame / (float) duration);
    }
}
