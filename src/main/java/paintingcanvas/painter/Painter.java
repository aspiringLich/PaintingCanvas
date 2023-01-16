package paintingcanvas.painter;

import javax.swing.*;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Painter {
    // how many frames per second do we want to run at?
    public static final int fps = 30;
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

    public void setTitle(String title) {
        frame.setTitle(title);
    }

    /**
     * The "render function", this will run the function in app every single frame at the set fps.
     * An alternative to the "run" function
     *
     * @param app contains the render function to run every frame
     */
    public void render(App app) {
        // TODO: Account for the time it takes to run the render function
        // (Implement the run with a loop and thread::sleep)
        ScheduledThreadPoolExecutor poolExecutor = new ScheduledThreadPoolExecutor(1);
        poolExecutor.scheduleAtFixedRate(() -> {
            app._render();
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
        ScheduledThreadPoolExecutor poolExecutor = new ScheduledThreadPoolExecutor(1);
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
