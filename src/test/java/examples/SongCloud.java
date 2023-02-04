package examples;

import java.awt.Color;

import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

import paintingcanvas.Canvas;
import paintingcanvas.animation.*;
import paintingcanvas.drawable.*;
import java.nio.file.Path;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.awt.Color.RGBtoHSB;

public class SongCloud {
    public static void main(String[] args) throws IOException {
        Canvas canvas = new Canvas();
        canvas.setTitle("Song Cloud");

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
            var x = (int) (Math.random() * canvas.width());
            var y = (int) (Math.random() * canvas.height());

            var thisText = new Text(x, y, entry.getKey()).setFontSize(fontSize).setColor(color);
            text.add(thisText);
        }

        // == Brighten ==
        canvas.sleep(3);

        for (var t : text) {
            var oldColor = t.getColor();
            var color = RGBtoHSB(oldColor.getRed(), oldColor.getGreen(), oldColor.getBlue(), null);
            var newColor = Color.getHSBColor(color[0], color[1], out.get(t.getText()) / (float) maxCount);
            t.getAnimationbuilder().with(Animation.colorTo(newColor), 1).with(Animation.rotateTo((int)(Math.random() * 100.0) - 50), 1);
        }

        // == Sing Song ==
        canvas.sleep(3);

        for (var w : words) {
            var t = text.stream().filter(x -> x.getText().equals(w)).findFirst().orElseThrow();
            var oldColor = t.getColor();

            t.setColor(0xFF0000);
            canvas.sleep(0.3);
            t.setColor(oldColor);
        }
    }
}