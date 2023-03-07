package paintingcanvas.canvas;

import paintingcanvas.canvas.Canvas;

public class Event {
    final int time;
    final EventRunner runner;

    public Event(int time, EventRunner runner) {
        this.time = time;
        this.runner = runner;
    }

    public interface EventRunner {
        void run(Canvas canvas);
    }
}
