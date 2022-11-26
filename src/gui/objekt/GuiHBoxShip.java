package gui.objekt;

import gui.tile.TileShip;
import gui.tile.TileWater;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import logic.Alignment;
import logic.Coordinate;
import logic.Ship;

/**
 * @author Stefan
 */
public class GuiHBoxShip extends HBox {

    private Ship ship;
    private int tileSize;

    /**
     * Gui object for a ship
     * @param ship Ships with size, exact positions on board and health
     * @param tileSize TileSize in Pixel
     */
    public GuiHBoxShip(Ship ship, int tileSize){
        this.ship = ship;
        this.tileSize = tileSize;
        for(int i = 0; i < ship.getSize(); i++){
            TileShip tileShip = new TileShip(new Coordinate(-1,-1), tileSize);
            this.getChildren().add(tileShip);
        }
    }

    /**
     * Get ship
     * @return ship
     */
    public Ship getShip() {
        return ship;
    }
}

