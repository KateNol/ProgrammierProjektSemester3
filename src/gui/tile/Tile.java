package gui.tile;

import javafx.scene.shape.Rectangle;

/**
 * @author Stefan
 */
public class Tile extends Rectangle {
    public int x, y;
    public int tileSize = 40;

    /**
     *
     * @param x Coordinate on the Board
     * @param y Coordinate on the Board
     */
    public Tile(int x, int y){
        super(40 - 1, 40 - 1);
        this.x = x;
        this.y = y;
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
