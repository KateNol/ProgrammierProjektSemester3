package gui.tile;

import javafx.scene.paint.Color;
import logic.Coordinate;

/**
 * @author Stefan
 */
public class TileDisable extends Tile{

    /**
     * Tile where Ships placement is not allowed
     * @param coordinate Coordinates where Ship is not allowed to place
     * @param tileSize TileSize in Pixel
     */
    public TileDisable(Coordinate coordinate, int tileSize) {
        super(coordinate, tileSize);
        setFill(Color.LIGHTGRAY);
    }
}
