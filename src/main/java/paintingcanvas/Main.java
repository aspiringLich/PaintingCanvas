package paintingcanvas;

import paintingcanvas.painter.App;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.awt.Color.RGBtoHSB;

public class Main extends App {
    public static void main(String[] args) {
        new Main().run();
    }

    @Override
    public void setup() throws IOException {
        var rawSong = Files.readString(Path.of("song.txt"));

        var out = new HashMap<String, Integer>();
        var words = new ArrayList<String>();
        var wordRegex = Pattern.compile("([A-z']+)+");
        var matches = wordRegex.matcher(rawSong);
        while (matches.find()) {
            var word = matches.group(1).toLowerCase(Locale.ROOT);
            var count = out.getOrDefault(word, 0);
            out.put(word, count + 1);
            words.add(word);
        }

        int maxCount = out.values().stream().max(Integer::compareTo).orElseThrow();
        var text = new ArrayList<Text>();
        for (var entry : out.entrySet()
                .stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .collect(Collectors.toList())) {

            var color = Color.getHSBColor((float) Math.random(), (float) (.25 + .70 * Math.random()),
                    (float) (.85 + .10 * Math.random()));
            var fontSize = (float) ((Math.sqrt((float) entry.getValue() / maxCount)) * 100f);
            var x = (int) (Math.random() * width);
            var y = (int) (Math.random() * height);

            var thisText = new Text(x, y, entry.getKey()).setFontSize(fontSize).setColor(color);
            // Animation to move away from center
            // thisText.animate().with(moveTo(width / 2 + (x - width / 2) * 2, height / 2 + (y - height / 2) * 2), 600f);
            text.add(thisText);
        }

        // == Brighten ==
        sleep(3);

        for (var t : text) {
            var oldColor = t.getColor();
            var color = RGBtoHSB(oldColor.getRed(), oldColor.getGreen(), oldColor.getBlue(), null);
            var newColor = Color.getHSBColor(color[0], color[1], out.get(t.getText()) / (float) maxCount);
            t.animate().with(colorTo(newColor), 1);
        }

        // == Sing Song ==
        sleep(3);

        for (var w : words) {
            var t = text.stream().filter(x -> x.getText().equals(w)).findFirst().orElseThrow();
            t.animate().with(colorTo(Color.RED), 0.1f);
            sleep(0.3f);
            t.animate().with(colorTo(t.getColor()), 0.1f);
        }
    }
}