package gui.tile;

import javafx.scene.paint.Color;

/**
 * @author Stefan
 */
public class TileHit extends Tile {
    /**
     * Create Hit Tile
     * Coordinates where shoot hit
     * @param x Coordinate on the Board
     * @param y Coordinate on the Board
     * @param tileSize tileSize in Pixel
     */
    public TileHit(int x, int y, int tileSize) {
        super(x, y, tileSize);
        setFill(Color.RED);
    }
}
