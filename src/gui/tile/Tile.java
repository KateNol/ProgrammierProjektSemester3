package gui.tile;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import logic.Coordinate;

/**
 * @author Stefan
 */
public class Tile extends Rectangle {
    private final Coordinate coordinate;
    private final int tileSize;

    /**
     * Base Tile is a Rectangle
     * @param coordinate Coordinate row, column
     * @param tileSize TileSize in Pixel
     */
    public Tile(Coordinate coordinate, int tileSize){
        this(coordinate, tileSize, Color.BLACK);
    }

    public Tile(Coordinate coordinate, int tileSize, Paint paint) {
        super(tileSize - 1, tileSize - 1);
        this.tileSize = tileSize;
        this.coordinate = coordinate;
        setStroke(Color.BLACK);
        setFill(paint);
    }

    public void setColor(Color color) {
        setFill(color);
    }

    /**
     * Get Size in Pixel from on Tile
     * @return tileSize
     */
    public int getTileSize(){
        return tileSize;
    }

    /**
     * Get coordinate from Tile
     * @return coordinate
     */
    public Coordinate getCoordinate() {
        return coordinate;
    }
}
