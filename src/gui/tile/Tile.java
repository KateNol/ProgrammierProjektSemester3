package gui.tile;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import logic.Coordinate;

/**
 * @author Stefan
 */
public class Tile extends Rectangle {
    private Coordinate coordinate;
    private int tileSize;

    /**
     * Base Tile is a Rectangle
     * @param tileSize TileSize in Pixel
     */
    public Tile(Coordinate coordinate, int tileSize){
        super(tileSize - 1, tileSize - 1);
        this.tileSize = tileSize;
        this.coordinate = coordinate;
        //Design from Tile
        setStroke(Color.BLACK);
    }

    /**
     * Get Size in Pixel from on Tile
     * @return tileSize
     */
    public int getTileSize(){
        return tileSize;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }
}
