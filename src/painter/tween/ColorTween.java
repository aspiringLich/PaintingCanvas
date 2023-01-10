package painter.tween;

import painter.drawable.Drawable;

import java.awt.*;

public class ColorTween extends Tween {
    private final Color end;
    private Color start;

    public ColorTween(int start, int duration, Color end, Drawable drawable) {
        super(start, duration, drawable);
        this.end = end;
    }

    @Override
    void updateTween(Drawable drawable, int frame, int duration) {
        if (frame == 0) this.start = drawable.color;
        drawable.color = lerpColor(start, end, frame / (float) duration);
    }

    Color lerpColor(Color _a, Color _b, float delta) {
        var r = _a.getRed() + (_b.getRed() - _a.getRed()) * delta;
        var g = _a.getGreen() + (_b.getGreen() - _a.getGreen()) * delta;
        var b = _a.getBlue() + (_b.getBlue() - _a.getBlue()) * delta;
        return new Color((int) r, (int) g, (int) b);
    }
}
