package gui.tile;

import javafx.scene.paint.Color;
import logic.Coordinate;

/**
 * @author Stefan
 */
public class TileWater extends Tile {

    /**
     * Create Water Tile
     * @param coordinate Coordinates where water is on the board
     * @param tileSize TileSize in Pixel
     */
    public TileWater(Coordinate coordinate, int tileSize) {
        super(coordinate, tileSize);
        setFill(Color.CADETBLUE);
    }

    /**
     * Create Water tile
     * @param row from given Coordinate
     * @param col from given Coordinate
     * @param tileSize TileSize in Pixel
     */
    public TileWater(int row, int col, int tileSize) {
        this(new Coordinate(row, col), tileSize);
    }
}
