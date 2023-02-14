package examples.tessellation;

import paintingcanvas.animation.Animation;


public class Tessellation {
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
                        (int) (Math.random() * Main.canvas.width()),
                        (int) (Math.random() * Main.canvas.height())
                );
                tiles[i][j].rotate((Math.random() - 0.5) * 720);
            }
        }
    }

    public void animateAll(Animation animation, double delay, double dMin, double dMax, boolean outline) {
        for (Tile[] tile : tiles) {
            for (Tile value : tile) {
                value.animate(animation, Math.random() * (dMax - dMin) + dMin, outline);
                Main.canvas.sleep(delay);
            }
        }
        Main.canvas.sleep();
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
        Main.canvas.sleep();
    }
}