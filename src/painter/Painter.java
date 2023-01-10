package painter;

import javax.swing.*;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Painter {
    private static JFrame jFrame;

    /**
     * The frames per second we want the application to run at
     */
    public static final int fps = 24;

    private static PaintingCanvas canvas;

    /**
     * Gets the inner held PainingCanvas for use in other methods
     *
     * Do NOT run without first constructing an instance of Painter
     *
     * @return the globally shared PaintingCanvas object
     */
    public static PaintingCanvas getCanvas() {
        return canvas;
    }

    private static void createWindow() {
        jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(1920, 1200);
        jFrame.setTitle("Java graphics thingy idk");

        canvas = new PaintingCanvas();

        jFrame.add(canvas);
        jFrame.setVisible(true);
    }

    /**
     * The "render function", this will run the function in app every single frame at the set fps.
     *
     * An alternative to the main function
     *
     * @param args Command-Line arguments
     * @param app contains the render function to run every frame
     */
    public static void render(String[] args, App app) {
        createWindow();

        ScheduledThreadPoolExecutor poolExecutor = new ScheduledThreadPoolExecutor(0);
        poolExecutor.scheduleAtFixedRate(() -> {
            app.render(args, canvas);
            canvas.repaint();
            SwingUtilities.updateComponentTreeUI(jFrame);
            jFrame.invalidate();
            canvas.invalidate();
        }, 0, 1000000 / fps, TimeUnit.MICROSECONDS);
    }

    /**
     * This function simply re-renders the canvas every single frame
     *
     *
     *
     * @param args Command-Line arguments
     * @param app contains the render function to run every frame
     */
    public static void main(String[] args, App app) {

    }
}
