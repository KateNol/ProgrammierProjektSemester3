package gui.objekt;

import gui.tile.TileShip;
import javafx.scene.layout.HBox;
import logic.Coordinate;
import logic.Ship;

/**
 * @author Stefan
 */
public class GuiHBoxShip extends HBox {

    private Ship ship;

    /**
     * Gui object for a ship
     * @param ship Ships with size, exact positions on board and health
     * @param tileSize TileSize in Pixel
     */
    public GuiHBoxShip(Ship ship, int tileSize){
        this.ship = ship;
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

