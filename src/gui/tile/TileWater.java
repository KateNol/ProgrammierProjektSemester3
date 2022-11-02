package gui.tile;

import javafx.scene.paint.Color;

/**
 * @author Stefan
 */
public class TileWater extends Tile {

    /**
     * Coordinates where water is on the board
     * @param x Coordinate on the Board
     * @param y Coordinate on the Board
     */
    public TileWater(int x, int y) {
        super(x, y);
        setFill(Color.AQUA);
        setStroke(Color.BLACK);
    }
}
