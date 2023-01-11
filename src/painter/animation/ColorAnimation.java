package painter.animation;

import painter.drawable.Drawable;

import java.awt.*;

/**
 * Controls animation that is to do with colors
 */
public class ColorAnimation extends Animation {
    private final Color end;
    private Color start;

    public ColorAnimation(int start, Color end, int duration, Drawable drawable) {
        super(start, duration, drawable);
        this.end = end;
    }

    @Override
    void updateAnimation(Drawable drawable, int frame, int duration) {
        if (frame == 0 || start == null) this.start = drawable.color;
        drawable.color = lerpColor(start, end, frame / (float) duration);
    }

    /**
     * Linearly interpolate from one color to another
     *
     * @param _a    Color to interpolate from
     * @param _b    Color to interpolate to
     * @param delta How much to interpolate (0.0 - 1.0)
     * @return The color at that point in the interpolation
     */
    Color lerpColor(Color _a, Color _b, float delta) {
        var r = _a.getRed() + (_b.getRed() - _a.getRed()) * delta;
        var g = _a.getGreen() + (_b.getGreen() - _a.getGreen()) * delta;
        var b = _a.getBlue() + (_b.getBlue() - _a.getBlue()) * delta;
        return new Color((int) r, (int) g, (int) b);
    }
}
