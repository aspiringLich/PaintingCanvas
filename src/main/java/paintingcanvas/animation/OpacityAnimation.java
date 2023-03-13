package paintingcanvas.animation;

import paintingcanvas.drawable.Drawable;
import paintingcanvas.misc.Misc;

import java.awt.*;

/**
 * Controls animation that is to do with opacity
 */
public class OpacityAnimation extends Animation {
    public final int end;
    public int start;
    public int outlineStart;

    public OpacityAnimation(double end) {
        super();
        this.end = (int) (end * 255.0);
    }

    public OpacityAnimation(int end) {
        super();
        this.end = end;
    }

    @Override
    public Animation copy() {
        var out = new OpacityAnimation(this.end);
        out.copy(this);
        return out;
    }

    @Override
    protected void updateAnimation(Drawable<? extends Drawable<?>> drawable, double progress) {
        var t = easing.ease(progress);

        Color color = drawable.color;
        int alpha = (int) (start + (end - start) * t);
//        System.err.println(end);
        alpha = Misc.clamp(0, alpha, 255);
        drawable.color = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);

        Color outlineColor = drawable.outlineColor;
        int outlineAlpha = (int) (start + (end - start) * t);
        outlineAlpha = Misc.clamp(0, outlineAlpha, 255);
        drawable.outlineColor = new Color(outlineColor.getRed(), outlineColor.getGreen(), outlineColor.getBlue(), outlineAlpha);
    }

    @Override
    protected void initAnimation(Drawable<? extends Drawable<?>> drawable) {
        this.start = drawable.color.getAlpha();
//        System.out.println(start);
        this.outlineStart = drawable.outlineColor.getAlpha();
    }
}
