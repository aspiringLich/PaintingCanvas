package paintingcanvas.drawable;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * <p>
 * Draws an image from the specified path.
 * </p>
 * <p>
 * TODO: The color should be used as a tint for the image
 * </p>
 *
 * <pre>{@code
 * // get the working directory of the program / where it is looking for your image
 * System.out.println(System.getProperty("user.dir"));
 * // create a new image object
 * Image img = new Image(100, 100, "path/to/image.png");
 * }</pre>
 */
public class Image extends DrawableBase.Shape<Image> {
    BufferedImage image;
    int width, height;

    /**
     * Create a new Image element.
     *
     * @param x   The X-position of the image
     * @param y   The Y-position of the image
     * @param src The path to the image file
     */
    public Image(int x, int y, String src) {
        super(x, y, Color.BLACK);
        try {
            image = ImageIO.read(new File(src));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        width = image.getWidth();
        height = image.getHeight();
    }

    /**
     * Set the scale of the image to the specified multiplier.
     *
     * <pre>{@code
     * Image img = new Image(0, 0, "image.png");
     * img.setScale(2.0, 1.0); // the image is now stretched in the x direction
     * }</pre>
     *
     * @param x The multiplier to scale x by
     * @param y The multiplier to scale y by
     * @return The original object to allow method chaining
     */
    public Image setScale(double x, double y) {
        this.width = (int) (image.getWidth() * x);
        this.height = (int) (image.getHeight() * y);
        return this;
    }

    @Override
    void drawFill(Graphics2D g) {
        g.drawImage(
                image,
                (int) (width * (-0.5 - anchor.x)),
                (int) (height * (-0.5 - anchor.y)),
                width,
                height,
                null
        );
    }

    @Override
    protected void drawOutline(Graphics2D g) {
        g.drawRect(
                (int) (width * (-0.5 - anchor.x)),
                (int) (height * (-0.5 - anchor.y)),
                width,
                height
        );
    }

    @Override
    public Point center(Graphics2D g) {
        return getPos();
    }

    @Override
    public Image getThis() {
        return this;
    }
}
