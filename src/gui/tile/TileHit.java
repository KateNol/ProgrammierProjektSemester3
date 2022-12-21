package gui.tile;

import javafx.scene.paint.Color;
import logic.Coordinate;

/**
 * @author Stefan
 */
public class TileHit extends Tile {

    /**
     * Create Hit Tile
     * @param coordinate Coordinates where shoot hit
     * @param tileSize TTileSize in Pixel
     */
    public TileHit(Coordinate coordinate, int tileSize) {
        super(coordinate, tileSize);
        setFill(Color.RED);
    }

    /**
     * Create Hit tile
     * @param row from given Coordinate
     * @param col from given Coordinate
     * @param tileSize TileSize in Pixel
     */
    public TileHit(int row, int col, int tileSize) {
        this(new Coordinate(row, col), tileSize);
    }
}
