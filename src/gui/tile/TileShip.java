package gui.tile;

import javafx.scene.paint.Color;

/**
 * @author Stefan
 */
public class TileShip extends Tile {

    /**
     * Coordinates where the Ship is placed
     * @param x Coordinate on the Board
     * @param y Coordinate on the Board
     */
    public TileShip(int x, int y) {
        super(x, y);
        setFill(Color.GRAY);
        setStroke(Color.BLACK);
    }
}
