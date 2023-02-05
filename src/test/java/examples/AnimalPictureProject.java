package examples;

import java.awt.Color;

import java.util.*;

import paintingcanvas.Canvas;
import paintingcanvas.animation.*;
import paintingcanvas.drawable.*;

public class AnimalPictureProject {
    // 0 -> None; 1 -> black; 2 -> white; 3 -> color;
    // totally dident steal this idea from breon
    // @formatter:off
    final static int[][] mushroom = {
            {0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, 1, 1, 2, 3, 3, 2, 1, 1, 1, 0, 0, 0},
            {0, 0, 1, 1, 2, 2, 2, 3, 3, 2, 2, 2, 1, 1, 0, 0},
            {0, 1, 1, 3, 2, 2, 3, 3, 3, 3, 2, 2, 3, 1, 1, 0},
            {0, 1, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 1, 0},
            {1, 1, 2, 2, 3, 3, 2, 2, 2, 2, 3, 3, 2, 2, 1, 1},
            {1, 2, 2, 2, 3, 2, 2, 2, 2, 2, 2, 3, 2, 2, 2, 1},
            {1, 2, 2, 2, 3, 2, 2, 2, 2, 2, 2, 3, 2, 2, 2, 1},
            {1, 2, 2, 2, 3, 2, 2, 2, 2, 2, 2, 3, 2, 2, 2, 1},
            {1, 3, 3, 3, 3, 3, 2, 2, 2, 2, 3, 3, 3, 3, 3, 1},
            {1, 3, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 3, 1},
            {1, 1, 1, 1, 2, 2, 1, 2, 2, 1, 2, 2, 1, 1, 1, 1},
            {0, 1, 1, 2, 2, 2, 1, 2, 2, 1, 2, 2, 2, 1, 1, 0},
            {0, 0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0},
            {0, 0, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 0, 0},
            {0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0},
    };
    // @formatter:on

    public static void main(String[] args) {
        Canvas canvas = new Canvas();
        canvas.setTitle("Animal Picture Projects");

        var xm = canvas.width() / 2 - mushroom.length * 10 + 5;
        var ym = canvas.height() / 2 - mushroom[0].length * 10 + 5;
        var colored = new ArrayList<Rectangle>();

        // == Mushroom ==
        for (int y = 0; y < mushroom.length; y++) {
            for (int x = 0; x < mushroom[y].length; x++) {
                var color = Color.WHITE;
                var rawColor = mushroom[y][x];
                if (rawColor == 0) continue;
                else if (rawColor == 1) color = Color.BLACK;
                else if (rawColor == 3) color = new Color(0xFF0000);

                var rect = new Rectangle(xm + x * 20, ym + y * 20, 20, 20).setColor(color);
                if (rawColor == 3) colored.add(rect);
            }
        }

        System.out.print("e");

        // == Animation ==
        while (true) {
            for (var i : new int[]{
                    0xFF0000,
                    0xFFC90E,
                    0x97C115,
                    0x00A2E8,
                    0x9D26E5,
            }) {
                for (var rect : colored)
                    rect.animate().with(Animation.colorTo(i), 2);
                canvas.sleep(7);
            }
        }
    }
}
