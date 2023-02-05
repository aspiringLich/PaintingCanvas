package paintingcanvas.animation;

import paintingcanvas.drawable.Drawable;

import java.awt.*;

/**
 * Controls animation that is to do with opacity
 */
public class OpacityAnimation extends Animation {
    public int start;
    public int end;

    public OpacityAnimation(int start, int duration, double end, Drawable drawable) {
        super(start, duration, drawable);
        this.end = (int)(end * 255.0);
    }

    @Override
    void updateAnimation(Drawable drawable, double progress) {
        Color color = drawable.color;
        int alpha = (int) (start + (end - start) * easing.ease(progress));
        drawable.color = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    @Override
    void initAnimation(Drawable drawable) {
        this.start = drawable.color.getAlpha();
    }
}
