package gui.tile;

import javafx.scene.paint.Color;
import logic.Coordinate;

/**
 * @author Stefan
 */
public class TileWater extends Tile {

    /**
     * Create Water Tile
     * Coordinates where water is on the board

     * @param tileSize TileSize in Pixel
     */
    public TileWater(Coordinate coordinate, int tileSize) {
        super(coordinate, tileSize);
        setFill(Color.AQUA);
    }
}
