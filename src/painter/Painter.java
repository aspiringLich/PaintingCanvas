package painter;

import javax.swing.*;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Painter {
    private JFrame jFrame;

    // how many frames per second do we want to run at?
    public static final int fps = 24;

    public PaintingCanvas canvas;

    public Painter(int width, int height, String title) {
        jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(width, height);
        jFrame.setTitle(title);

        canvas = new PaintingCanvas();

        jFrame.add(canvas);
        jFrame.setVisible(true);
    }

    /**
     * The "render function", this will run the function in app every single frame at the set fps.
     * An alternative to the main function
     *
     * @param args Command-Line arguments
     * @param app contains the render function to run every frame
     */
    public void render(String[] args, App app) {
        ScheduledThreadPoolExecutor poolExecutor = new ScheduledThreadPoolExecutor(0);
        poolExecutor.scheduleAtFixedRate(() -> {
            app.render(args);
            canvas.repaint();
            SwingUtilities.updateComponentTreeUI(jFrame);
            jFrame.invalidate();
            jFrame.validate();
        }, 0, 1000000 / fps, TimeUnit.MICROSECONDS);
    }

    /**
     * This function simply re-renders the canvas every single frame
     *
     * @param args Command-Line arguments
\     */
    public void run(String[] args) {
        ScheduledThreadPoolExecutor poolExecutor = new ScheduledThreadPoolExecutor(0);
        new Thread(() -> {
            poolExecutor.scheduleAtFixedRate(() -> {
                canvas.repaint();
                SwingUtilities.updateComponentTreeUI(jFrame);
                jFrame.invalidate();
                jFrame.validate();
            }, 0, 1000000 / fps, TimeUnit.MICROSECONDS);
        });
    }
}
