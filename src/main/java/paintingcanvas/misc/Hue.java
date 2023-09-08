package paintingcanvas.misc;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *     Colors (besides black and white) are taken from the
 *     <a href="https://tailwindcss.com/docs/customizing-colors">Tailwind CSS Color Palette</a>.
 * </p>
 * <p>
 *     Not indented to be constructed directly in the public API. Internally, it's only use is
 *     {@link #getColor(String)}
 * </p>
 * <table>
 *     <caption>{@link Hue} Variants</caption>
 *     <tr><th>Variant</th><th>Color</th></tr>
 *     <tr><td>Black</td><td style="background-color:#000000"></td></tr>
 *     <tr><td>White</td><td style="background-color:#ffffff"></td></tr>
 *     <tr><td>Slate</td><td style="background-color:#94a3b8"></td></tr>
 *     <tr><td>Gray</td><td style="background-color:#9ca3af"></td></tr>
 *     <tr><td>Zinc</td><td style="background-color:#a1a1aa"></td></tr>
 *     <tr><td>Neutral</td><td style="background-color:#a3a3a3"></td></tr>
 *     <tr><td>Stone</td><td style="background-color:#a8a29e"></td></tr>
 *     <tr><td>Red</td><td style="background-color:#f87171"></td></tr>
 *     <tr><td>Orange</td><td style="background-color:#fb923c"></td></tr>
 *     <tr><td>Amber</td><td style="background-color:#fbbf24"></td></tr>
 *     <tr><td>Yellow</td><td style="background-color:#facc15"></td></tr>
 *     <tr><td>Lime</td><td style="background-color:#a3e635"></td></tr>
 *     <tr><td>Green</td><td style="background-color:#4ade80"></td></tr>
 *     <tr><td>Emerald</td><td style="background-color:#4ade80"></td></tr>
 *     <tr><td>Teal</td><td style="background-color:#2dd4bf"></td></tr>
 *     <tr><td>Cyan</td><td style="background-color:#22d3ee"></td></tr>
 *     <tr><td>Sky</td><td style="background-color:#38bdf8"></td></tr>
 *     <tr><td>Blue</td><td style="background-color:#60a5fa"></td></tr>
 *     <tr><td>Indigo</td><td style="background-color:#818cf8"></td></tr>
 *     <tr><td>Purple</td><td style="background-color:#a78bfa"></td></tr>
 *     <tr><td>Fuchsia</td><td style="background-color:#e879f9"></td></tr>
 *     <tr><td>Pink</td><td style="background-color:#f472b6"></td></tr>
 *     <tr><td>Rose</td><td style="background-color:#fb7185"></td></tr>
 * </table>
 */
@SuppressWarnings("unused")
public enum Hue {
    Black("black", 0x000000),
    White("white", 0xffffff),
    Slate("slate", 0x94a3b8),
    Gray("gray", 0x9ca3af),
    Zinc("zinc", 0xa1a1aa),
    Neutral("neutral", 0xa3a3a3),
    Stone("stone", 0xa8a29e),
    Red("red", 0xf87171),
    Orange("orange", 0xfb923c),
    Amber("amber", 0xfbbf24),
    Yellow("yellow", 0xfacc15),
    Lime("lime", 0xa3e635),
    Green("green", 0x4ade80),
    Emerald("emerald", 0x4ade80),
    Teal("teal", 0x2dd4bf),
    Cyan("cyan", 0x22d3ee),
    Sky("sky", 0x38bdf8),
    Blue("blue", 0x60a5fa),
    Indigo("indigo", 0x818cf8),
    Purple("purple", 0xa78bfa),
    Fuchsia("fuchsia", 0xe879f9),
    Pink("pink", 0xf472b6),
    Rose("rose", 0xfb7185);

    private static final Map<String, Hue> BY_NAME = new HashMap<>();
    private static final String NAME_LIST;

    static {
        StringBuilder nameList = new StringBuilder();
        for (Hue e : values()) {
            BY_NAME.put(e.name, e);
            nameList.append(e.name).append(", ");
        }
        NAME_LIST = nameList.substring(0, nameList.length() - 2);
    }

    public final String name;
    public final int hex;

    Hue(String name, int hex) {
        this.name = name;
        this.hex = hex;
    }

    /**
     * Get the associated {@link Color} when given a {@link String} as the name of the color.
     *
     * @param name The name of the color (case-insensitive)
     * @return The color
     * @throws IllegalArgumentException If the color does not exist
     */
    public static Color getColor(String name) {
        Hue h = BY_NAME.get(name.toLowerCase());
        if (h == null) throw new IllegalArgumentException(
                String.format("Color '%s' does not exist, valid colors are '%s'", name, NAME_LIST)
        );
        return new Color(h.hex);
    }

    /**
     * Get the associated {@link Color} for this {@link Hue}.
     *
     * @return The color
     */
    public Color getColor() {
        return new Color(hex);
    }
}
