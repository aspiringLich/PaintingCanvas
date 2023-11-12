package paintingcanvas.misc;

import paintingcanvas.drawable.Drawable;

import java.awt.*;

public class Misc {
    public static <N extends Number> N clamp(N min, N val, N max) {
        if (val.doubleValue() > max.doubleValue()) return max;
        if (val.doubleValue() < min.doubleValue()) return min;
        return val;
    }

    public static <N extends Number> boolean equality(N a, N b, N error) {
        var err = error.doubleValue() / 2;
        return Math.max(a.doubleValue(), b.doubleValue()) - err <= Math.min(a.doubleValue(), b.doubleValue()) + err;
    }

    /**
     * Set the color of the object with a certain color by name, or by a <a href="https://en.wikipedia.org/wiki/RGB_color_model">hex code</a>. string.
     *
     * @param name the string describing the hue or the hex code
     * @return the color
     * @see Hue for list of all valid names
     */
    public static Color stringToColor(String name) {
        try {
            // prepend the "#" if necessary, and try to decode it as a hex code
            String colorName = name.startsWith("#") ? name : ("#" + name);
            return Color.decode(colorName);
        } catch (Exception e) {
            // otherwise, try to decode it as a Hue
            // this function will also throw again if it isn't a valid Hue
            return Hue.getColor(name);
        }
    }

    public static <T> T castDrawable(Drawable<?> drawable, Class<T> _class) {
        try {
            return (T) drawable;
        } catch (ClassCastException e) {
            throw new ClassCastException("This drawable does not support " + _class.getName() + " operations");
        }
    }
}