package gui.tile;

import javafx.scene.paint.Color;
import logic.Coordinate;

/**
 * @author Stenfan
 */
public class TileMiss extends Tile{
    /**
     * Create Miss Tile
     * Coordinates where shoot missed
     * @param tileSize tileSize in Pixel
     */
    public TileMiss(Coordinate coordinate, int tileSize) {
        super(coordinate, tileSize);
        setFill(Color.WHITE);
    }
}
