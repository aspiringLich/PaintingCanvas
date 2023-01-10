package painter;

import javax.swing.*;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Painter {
    // how many frames per second do we want to run at?
    public static final int fps = 24;
    private final JFrame frame;
    public Canvas canvas;

    public Painter(int width, int height, String title) {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.setTitle(title);
        frame.setVisible(true);

        canvas = new Canvas();
        frame.add(canvas);
        frame.addComponentListener(new Canvas.ResizeListener(canvas));
    }

    /**
     * The "render function", this will run the function in app every single frame at the set fps.
     * An alternative to the main function
     *
     * @param app contains the render function to run every frame
     */
    public void render(App app) {
        ScheduledThreadPoolExecutor poolExecutor = new ScheduledThreadPoolExecutor(1);
        poolExecutor.scheduleAtFixedRate(() -> {
            app.render();
            canvas.repaint();
            SwingUtilities.updateComponentTreeUI(frame);
            frame.invalidate();
            frame.validate();
        }, 0, 1000000 / fps, TimeUnit.MICROSECONDS);
    }

    /**
     * This function simply re-renders the canvas every single frame
     */
    public void run() {
        ScheduledThreadPoolExecutor poolExecutor = new ScheduledThreadPoolExecutor(0);
        new Thread(() -> {
            poolExecutor.scheduleAtFixedRate(() -> {
                canvas.repaint();
                SwingUtilities.updateComponentTreeUI(frame);
                frame.invalidate();
                frame.validate();
            }, 0, 1000000 / fps, TimeUnit.MICROSECONDS);
        });
    }
}
