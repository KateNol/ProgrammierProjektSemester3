package gui.tile;

import javafx.scene.paint.Color;
import logic.Coordinate;

/**
 * @author Stefan
 */
public class TileShip extends Tile {

    /**
     * Create Ship Tile
     * @param coordinate Coordinates where the Ship is placed
     * @param tileSize TileSize in Pixel
     */
    public TileShip(Coordinate coordinate, int tileSize) {
        super(coordinate, tileSize);
        setFill(Color.GRAY);
    }
}
