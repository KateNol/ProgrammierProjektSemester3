package gui.tile;

import javafx.scene.paint.Color;

/**
 * @author Stefan
 */
public class TileShip extends Tile {

    /**
     * Create Ship Tile
     * Coordinates where the Ship is placed
     * @param x Coordinate on the Board
     * @param y Coordinate on the Board
     * @param tileSize tileSize in Pixel
     */
    public TileShip(int x, int y, int tileSize) {
        super(x, y, tileSize);
        setFill(Color.GRAY);
    }
}
