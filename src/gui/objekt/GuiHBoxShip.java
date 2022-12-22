package gui.objekt;

import gui.GUIPlayer;
import gui.tile.TileShip;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import logic.Coordinate;
import logic.Ship;

import java.util.HashSet;
import java.util.Set;

import static gui.Util.log_debug;

/**
 * This class is a Gui Ship
 * @author Stefan
 */
public class GuiHBoxShip extends HBox {

    private final Ship ship;
    Set<TileShip> tiles = new HashSet<>();

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
            tiles.add(tileShip);
        }
        this.setOnMouseClicked(e -> {
            log_debug("Hello");
            GUIPlayer.getInstance().getGuiHarbour().setSelectedShip(ship);
            for (var s : GUIPlayer.getInstance().getGuiHarbour().getGuiHBoxShips().keySet()) {
                if (!GUIPlayer.getInstance().getGuiBoard().getShipsThatHaveBeenSet().contains(s))
                    GUIPlayer.getInstance().getGuiHarbour().getGuiHBoxShips().get(s).changeColor(Color.GRAY);
            }
            if (!GUIPlayer.getInstance().getGuiBoard().getShipsThatHaveBeenSet().contains(ship))
                changeColor(Color.YELLOW);
        });
    }

    /**
     * Set Color when Ship is chosen
     * @param color color from ship
     */
    public void changeColor(Color color) {
        for (var t : tiles) {
            t.setColor(color);
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
