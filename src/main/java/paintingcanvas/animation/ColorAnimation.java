package paintingcanvas.animation;

import paintingcanvas.drawable.Drawable;

import java.awt.*;

/**
 * Controls animation that is to do with colors
 */
public class ColorAnimation extends Animation {
    private final Color end;
    private Color start;

    public ColorAnimation(int start, int duration, Color end, Drawable drawable) {
        super(start, duration, drawable);
        this.end = end;
    }

    @Override
    void updateAnimation(Drawable drawable, double progress) {
        drawable.color = lerpColor(start, end, (double) easing.ease(progress));
    }

    @Override
    void initAnimation(Drawable drawable) {
        this.start = drawable.color;
    }

    /**
     * Linearly interpolate from one color to another
     *
     * @param _a    Color to interpolate from
     * @param _b    Color to interpolate to
     * @param delta How much to interpolate (0.0 - 1.0)
     * @return The color at that point in the interpolation
     */
    Color lerpColor(Color _a, Color _b, double delta) {
        var r = _a.getRed() + (_b.getRed() - _a.getRed()) * delta;
        var g = _a.getGreen() + (_b.getGreen() - _a.getGreen()) * delta;
        var b = _a.getBlue() + (_b.getBlue() - _a.getBlue()) * delta;
        var a = _a.getAlpha() + (_b.getAlpha() - _a.getAlpha()) * delta;
        return new Color((int) r, (int) g, (int) b, (int) a);
    }
}
