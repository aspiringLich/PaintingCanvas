package paintingcanvas.painter;

public class Event {
    final int time;
    final boolean repeat;
    final EventRunner runner;

    public Event(int time, EventRunner runner) {
        this.time = time;
        this.repeat = false;
        this.runner = runner;
    }

    public Event(int time, boolean repeat, EventRunner runner) {
        this.time = time;
        this.repeat = repeat;
        this.runner = runner;
    }

    public interface EventRunner {
        void run(Canvas canvas);
    }
}
