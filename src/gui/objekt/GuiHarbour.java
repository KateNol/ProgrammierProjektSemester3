package gui.objekt;

import javafx.scene.layout.VBox;
import logic.Ship;

import java.util.ArrayList;

/**
 * @author Stefan
 */
public class GuiHarbour extends VBox {
    private ArrayList<Ship> ships;
    private int tileSize;

    /**
     * Gui object for a set of ships
     * @param tileSize TileSize in Pixel
     * @param ships Array of ships
     */
    public GuiHarbour(int tileSize, ArrayList<Ship> ships){
        this.ships = ships;
        this.tileSize = tileSize;
        this.setSpacing(20);
    }

    /**
     * Create gui object harbour
     * @param vBox
     */
    public void initializeShip(VBox vBox){
        for(int i = 0; i < ships.size(); i++){
            GuiHBoxShip hBoxShip = new GuiHBoxShip(ships.get(i), tileSize);
            this.getChildren().add(hBoxShip);
        }
        vBox.getChildren().add(this);
    }
}
