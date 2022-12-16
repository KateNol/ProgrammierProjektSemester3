package gui.tile;

import javafx.scene.paint.Color;
import logic.Coordinate;

/**
 * @author Stenfan
 */
public class TileMiss extends Tile{
    /**
     * Create Miss Tile
     * @param coordinate Coordinates where shoot missed
     * @param tileSize TileSize in Pixel
     */
    public TileMiss(Coordinate coordinate, int tileSize) {
        super(coordinate, tileSize);
        setFill(Color.WHITE);
    }
    public TileMiss(int i, int j, int tileSize) {
        this(new Coordinate(i, j), tileSize);
    }
}
