package gui.tile;

import javafx.scene.paint.Color;

/**
 * @author Stenfan
 */
public class TileMiss extends Tile{
    /**
     * Create Miss Tile
     * Coordinates where shoot missed
     * @param x Coordinate on the Board
     * @param y Coordinate on the Board
     * @param tileSize tileSize in Pixel
     */
    public TileMiss(int x, int y, int tileSize) {
        super(x, y, tileSize);
        setFill(Color.WHITE);
    }
}
