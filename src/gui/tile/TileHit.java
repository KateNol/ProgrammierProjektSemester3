package gui.tile;

import javafx.scene.paint.Color;

public class TileHit extends Tile {

    public TileHit(int x, int y) {
        super(x, y);
        setFill(Color.RED);
        setStroke(Color.BLACK);
    }
}
