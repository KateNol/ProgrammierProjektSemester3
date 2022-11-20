package gui.objekt;

import gui.tile.TileShip;
import javafx.scene.layout.HBox;
import logic.Coordinate;
import logic.Ship;

/**
 * @author Stefan
 */
public class HBoxShip extends HBox {

    private Ship ship;

    public HBoxShip(Ship ship, int tileSize){
        this.ship = ship;
        for(int i = 0; i < ship.getSize(); i++){
            TileShip tileShip = new TileShip(new Coordinate(-1,-1), tileSize);
            this.getChildren().add(tileShip);
        }
    }

    public Ship getShip() {
        return ship;
    }
}

