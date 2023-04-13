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

        Color color = drawable.getColor();
        int alpha = Misc.clamp(0, (int) (start + (end - start) * t), 255);
//        System.err.println(end);
        drawable.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha));

        Color outlineColor = drawable.getOutlineColor();
        int outlineAlpha = Misc.clamp(0, (int) (outlineStart + (end - outlineStart) * t), 255);
        drawable.setOutline(new Color(outlineColor.getRed(), outlineColor.getGreen(), outlineColor.getBlue(), outlineAlpha));
    }

    @Override
    protected void initAnimation(Drawable<? extends Drawable<?>> drawable) {
        this.start = drawable.getColor().getAlpha();
        this.outlineStart = drawable.getOutlineColor().getAlpha();
    }
}
