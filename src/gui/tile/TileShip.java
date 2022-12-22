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

    /**
     * Create Ship tile
     * @param row from given Coordinate
     * @param col from given Coordinate
     * @param tileSize TileSize in Pixel
     */
    public TileShip(int row, int col, int tileSize) {
        this(new Coordinate(row, col), tileSize);
    }
}
