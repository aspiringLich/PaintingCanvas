package examples;

import paintingcanvas.canvas.Canvas;
import paintingcanvas.animation.Animation;
import paintingcanvas.drawable.Path;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Random;

class Tessellation {
    public static Canvas canvas = new Canvas(960, 640, "");

    public static void main(String[] args) {
        canvas.setTitle("Tessellation project");
        canvas.sleep(0.05);

        int width = canvas.getWidth();
        int height = canvas.getHeight();

        Tessellation tess = new Tessellation(width, height);
        tess.order();
        tess.animateAll(
                Animation.colorTo(Color.getHSBColor(0, 0.55f, 1.0f)),
                0.1,
                2,
                4,
                false
        );
    }

    static final int ySpacing = Const.height * 2;
    static final int xSpacing = Const.size * 3 / 2;
    static Tile[][] tiles;

    Tessellation(int width, int height) {
        int x = width / xSpacing + 2;
        int y = height / ySpacing + 2;

        tiles = new Tile[x][y];
//        tiles = new Tile[2][2];
        this.init();
    }

    public static Tile getTile(int i, int j) {
        if (i < 0 || j < 0 || i >= tiles.length || j >= tiles[i].length) {
            return null;
        }
        return tiles[i][j];
    }

    public void init() {
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                tiles[i][j] = new Tile(i, j);

                tiles[i][j].draw(
                        (int) (Math.random() * Tessellation.canvas.getWidth()),
                        (int) (Math.random() * Tessellation.canvas.getHeight())
                );
                tiles[i][j].rotate((Math.random() - 0.5) * 720);
            }
        }
    }

    public void animateAll(Animation animation, double delay, double dMin, double dMax, boolean outline) {
        for (Tile[] tile : tiles) {
            for (Tile value : tile) {
                value.animate(animation, Math.random() * (dMax - dMin) + dMin, outline);
                Tessellation.canvas.sleep(delay);
            }
        }
        Tessellation.canvas.sleep();
    }

    public void order() {
        for (Tile[] row : tiles) {
            for (Tile tile : row) {
                var pos = tile.getPos();
                tile.animate(
                        Animation.moveTo(pos.x, pos.y),
                        3,
                        true
                );
                tile.animate(
                        Animation.rotateTo(0),
                        3,
                        true
                );
            }
        }
        Tessellation.canvas.sleep();
    }
}

class Tile {
    Edge[] edges = new Edge[6];
    Path path;
    Path outline;
    float hue;
    int i, j;

    Tile(int i, int j) {
        this.i = i;
        this.j = j;

        var tiles = getTiles();
        for (int s = 0; s < tiles.length; s++) {
            var tile = tiles[s];
            if (tile != null) {
                var flip = tile.edges[(s + 3) % 6].flip;
                edges[s] = new Edge(!flip);
            } else {
                edges[s] = new Edge();
            }
        }

        this.setColorRandom();
    }

    public Point getPos() {
        int x = Tessellation.xSpacing * i;
        int y = Tessellation.ySpacing * j - (i % 2 * Const.height);
        return new Point(x, y);
    }

    void animate(Animation animation, double duration, boolean outline) {
        path.animate().with(animation.copy(), duration);
        if (outline) this.outline.animate().with(animation, duration);
    }

    void rotate(double rotation) {
        path.rotate(rotation);
        outline.rotate(rotation);
    }

    void setColorRandom() {
        ArrayList<Float> hues = new ArrayList<>();

        var tiles = getTiles();
        for (int s = 0; s < tiles.length; s++) {
            var tile = tiles[s];
            if (tile != null) {
                hues.add(tile.hue);
            }
        }

        double newHue;
        boolean cont;
        do {
            cont = false;
            newHue = Math.random() * 360;

            for (var h : hues) {
                if (Math.abs(h - newHue) < 50) {
                    cont = true;
                    break;
                }
            }
        } while (cont);
        hue = (float) newHue;
    }

    Tile[] getTiles() {
        final Point[] diff;
        if (i % 2 == 1) {
            diff = new Point[]{
                    new Point(0, -1),
                    new Point(1, -1),
                    new Point(1, 0),
                    new Point(0, 1),
                    new Point(-1, 0),
                    new Point(-1, -1)
            };
        } else {
            diff = new Point[]{
                    new Point(0, -1),
                    new Point(1, 0),
                    new Point(1, 1),
                    new Point(0, 1),
                    new Point(-1, 1),
                    new Point(-1, 0)
            };
        }

        Tile[] out = new Tile[6];
        for (int s = 0; s < 6; s++) {
            var d = diff[s];
            var tile = Tessellation.getTile(i + d.x, j + d.y);
            out[s] = tile;
        }
        return out;
    }

    void setColor() {
        Color c = Color.getHSBColor(this.hue, 0.55f, 1.0f);
        path.setColor(c);
    }

    void colorTo(float hue) {
        this.hue = hue;
        this.path.animate().with(
                Animation.colorTo(Color.getHSBColor(hue, 0.55f, 1.0f)),
                3
        );
    }

    void draw(int x, int y) {
        var size = Const.size;
        var height = Const.height;

        var p1 = new Point(-size / 2, -height);
        var p2 = new Point(size / 2, -height);

        path = new Path().setPos(x, y);
        outline = new Path().setPos(x, y);
        path.cursorTo(p1.x, p1.y);
        outline.cursorTo(p1.x, p1.y);

        this.setColor();

        path.setFilled(true);

        outline.setThickness(5);

        for (int i = 0; i < 6; i++) {
            var tf = AffineTransform.getRotateInstance(
                    Math.PI / 3 * i
            );

            var points = edges[i].get_path(false);
            for (int j = 1; j < points.size() - 1; j += 2) {
                var end = points.get(j + 1);
                var ctrl = points.get(j);
                tf.transform(end, end);
                tf.transform(ctrl, ctrl);
                path.quadTo(ctrl.x, ctrl.y, end.x, end.y);
                outline.quadTo(ctrl.x, ctrl.y, end.x, end.y);
            }
        }
    }
}

class Const {
    static final int size = 80;
    static final int edgePadding = (int) (size * 0.4);
    static final int centerHeight = size / 8;
    static final int height = (int) (size * Math.sqrt(3) / 2);
}

class Edge {
    boolean flip;

    Edge() {
        flip = new Random().nextBoolean();
    }

    Edge(boolean flip) {
        this.flip = flip;
    }

    ArrayList<Point> get_path(boolean flip) {
        ArrayList<Point> out = new ArrayList<>();
        var size = Const.size;
        var height = Const.height;
        var padding = Const.edgePadding;
        var unit = (int) (size / 12 * 1.05);

        var tabHeight = unit * 4;
        var indent = unit;

        out.add(new Point(0, 0));
        out.add(new Point(padding - unit / 2, unit));
        out.add(new Point(padding, 0));
        out.add(new Point(padding + unit / 4, -indent / 2));
        out.add(new Point(padding, -indent));
        out.add(new Point(padding - unit, -tabHeight));
        out.add(new Point(size / 2, -tabHeight));

        if (this.flip) flip = !flip;

        for (Point p : out) {
            if (flip) p.y = -p.y;
            p.x -= size / 2;
            p.y -= height;
        }
        for (int i = out.size() - 2; i >= 0; i--) {
            out.add(new Point(-out.get(i).x, out.get(i).y));
        }
        return out;
    }
}


