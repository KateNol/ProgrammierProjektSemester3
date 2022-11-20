package gui.tile;

import javafx.scene.paint.Color;
import logic.Coordinate;

public class TileDisable extends Tile{
    /**
     * Base Tile is a Rectangle
     * @param tileSize TileSize in Pixel
     */
    public TileDisable(Coordinate coordinate, int tileSize) {
        super(coordinate, tileSize);
        setFill(Color.LIGHTGRAY);
    }
}
