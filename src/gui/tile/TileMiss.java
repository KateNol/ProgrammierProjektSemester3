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

    /**
     * Create Miss tile
     * @param row from given Coordinate
     * @param col from given Coordinate
     * @param tileSize TileSize in Pixel
     */
    public TileMiss(int row, int col, int tileSize) {
        this(new Coordinate(row, col), tileSize);
    }
}
