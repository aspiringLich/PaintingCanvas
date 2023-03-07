package examples;

import paintingcanvas.Canvas;
import paintingcanvas.drawable.Circle;
import paintingcanvas.drawable.Rectangle;
import paintingcanvas.extensions.FrameCounter;
import paintingcanvas.misc.TimeUnit;

import java.awt.*;

public class Ant {
    static final int PIXEL_SIZE = 10;
    static int tick = 0;

    public static void main(String[] args) {
        System.setProperty("sun.java2d.opengl", "true");
        var canvas = new Canvas(2000, 1000, "Langton's Ant");
        new FrameCounter().line(() -> String.format("Step: %d", tick)).attach();
        var field = new Field(canvas.width() / PIXEL_SIZE, canvas.height() / PIXEL_SIZE);

        while (true) {
            field.step();
            canvas.sleep(1, TimeUnit.Frames);
        }
    }

    static class Field {
        // == Field ==
        boolean[] data;
        Rectangle[] tiles;
        Point size;

        // == Ant ==
        Circle ant;
        Point antPos;
        Direction direction;

        public Field(int x, int y) {
            assert x >= 23 && y >= 30;
            this.data = new boolean[x * y];
            this.tiles = new Rectangle[x * y];
            this.size = new Point(x, y);
            this.antPos = new Point(23, 30);
            this.direction = Direction.Up;

            for (int i = 0; i < x; i++)
                for (int j = 0; j < y; j++)
                    tiles[j * x + i]  = new Rectangle(i * PIXEL_SIZE + PIXEL_SIZE / 2, j * PIXEL_SIZE + PIXEL_SIZE / 2, PIXEL_SIZE, PIXEL_SIZE).setColor(0).setOutline(1, new Color(0x101010));
            this.ant = new Circle(antPos.x * PIXEL_SIZE + PIXEL_SIZE / 2, antPos.y * PIXEL_SIZE + PIXEL_SIZE / 2, PIXEL_SIZE / 2).setColor(0xaa0000);
        }

        private int getIndex() {
            return (this.antPos.y * this.size.x + this.antPos.x) % this.tiles.length;
        }

        private void step() {
            ++tick;

            var index = getIndex();
            this.data[index] ^= true;
            this.tiles[index].setColor(this.data[index] ? 0xffffff : 0);
            this.direction.modify(this.antPos);
            this.ant.setPos(antPos.x * PIXEL_SIZE + PIXEL_SIZE / 2, antPos.y * PIXEL_SIZE + PIXEL_SIZE / 2);

            index = getIndex();
            if (this.data[index]) this.direction = this.direction.left();
            else this.direction = this.direction.right();
        }
    }

    enum  Direction {
        Up, Right, Down, Left;

        public Direction left() {
            return values()[(this.ordinal() + 3) % 4];
        }

        public Direction right() {
            return values()[(this.ordinal() + 1) % 4];
        }

        public void modify(Point point) {
            switch (this) {
                case Up:
                    point.translate(0, -1);
                    break;
                case Down:
                    point.translate(0, 1);
                    break;
                case Left:
                    point.translate(-1, 0);
                    break;
                case Right:
                    point.translate(1, 0);
                    break;
            }
        }
    }
}
