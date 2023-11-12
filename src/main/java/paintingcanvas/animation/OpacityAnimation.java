package paintingcanvas.animation;

import paintingcanvas.drawable.Colorable;
import paintingcanvas.drawable.Drawable;
import paintingcanvas.drawable.Outlineable;
import paintingcanvas.misc.Misc;

import java.awt.*;

/**
 * Controls animation that is to do with opacity
 */
@SuppressWarnings("unused")
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
        var c = Misc.castDrawable(drawable, Colorable.class);

        Color color = c.getColor();
        int alpha = Misc.clamp(0, (int) (start + (end - start) * t), 255);
        c.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha));

        try {
            var o = (Outlineable<?>)drawable;
            Color outlineColor = o .getOutlineColor();
            int outlineAlpha = Misc.clamp(0, (int) (outlineStart + (end - outlineStart) * t), 255);
            o .setOutline(new Color(outlineColor.getRed(), outlineColor.getGreen(), outlineColor.getBlue(), outlineAlpha));
        } catch (ClassCastException ignored) {}
    }

    @Override
    protected void initAnimation(Drawable<? extends Drawable<?>> drawable) {
        var c = Misc.castDrawable(drawable, Colorable.class);
        this.start = c.getColor().getAlpha();

        try {
            var o = (Outlineable<?>)drawable;
            this.outlineStart = o.getOutlineColor().getAlpha();
        } catch (ClassCastException ignored) {}
    }
}
