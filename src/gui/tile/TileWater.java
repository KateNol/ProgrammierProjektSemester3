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

    public TileWater(int i, int j, int tileSize) {
        this(new Coordinate(i, j), tileSize);
    }
}
