import java.awt.*;

public class App extends painter.App {
    public App() {
        var text = new Text(100, 100, "Hello World").setColor(Color.RED);
        text.setText("Hello World!");
    }
}