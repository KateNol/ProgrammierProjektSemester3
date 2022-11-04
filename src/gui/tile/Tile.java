package gui.tile;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * @author Stefan
 */
public class Tile extends Rectangle {
    private int x, y;
    private int tileSize;

    /**
     * Base Tile is a Rectangle
     * @param x Coordinate on the Board
     * @param y Coordinate on the Board
     * @param tileSize TileSize in Pixel
     */
    public Tile(int x, int y, int tileSize){
        super(tileSize - 1, tileSize - 1);
        this.tileSize = tileSize;
        this.x = x;
        this.y = y;
        //Design from Tile
        setStroke(Color.BLACK);
    }

    /**
     * Coordinate
     * @return x
     */
    public int getCoordinateX(){
        return x;
    }

    /**
     * Coordinate
     * @return y
     */
    public int getCoordinateY(){
        return y;
    }

    /**
     * Get Size in Pixel from on Tile
     * @return tileSize
     */
    public int getTileSize(){
        return tileSize;
    }

    /**
     * print Coordinates
     */
    public void getCoordinates(){
        System.out.println(x + " " + y);
    }
}
