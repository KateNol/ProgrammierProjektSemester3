package gui.tile;

import javafx.scene.paint.Color;

/**
 * @author Stefan
 */
public class TileWater extends Tile {

    /**
     * Create Water Tile
     * Coordinates where water is on the board
     * @param x Coordinate on the Board
     * @param y Coordinate on the Board
     * @param tileSize TileSize in Pixel
     */
    public TileWater(int x, int y, int tileSize) {
        super(x, y, tileSize);
        setFill(Color.AQUA);
    }
}
