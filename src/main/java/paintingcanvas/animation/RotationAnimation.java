package paintingcanvas.animation;

import paintingcanvas.drawable.Drawable;

/**
 * Controls animation that is to do with rotation
 */
public class RotationAnimation extends Animation {
    public double end;
    public Double start = null;
    public boolean relative = false;

    public RotationAnimation(double end) {
        super();
        this.end = end;
    }

    public RotationAnimation relative() {
        relative = true;
        return this;
    }
    
    @Override
    public Animation copy() {
        return new RotationAnimation(end);
    }
    
    @Override
    protected void updateAnimation(Drawable drawable, double progress) {
        var t = (double) easing.ease(progress);
        drawable.rotation = start + (end - start) * t;
    }

    @Override
    protected void initAnimation(Drawable drawable) {
        start = drawable.rotation;
        if (relative) {
            end += start;
        }
    }
}
