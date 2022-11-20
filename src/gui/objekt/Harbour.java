package gui.objekt;

import javafx.scene.layout.VBox;
import logic.Ship;

/**
 * @author Stefan
 */
public class Harbour extends VBox {
    private int[] ships;
    private int tileSize;

    public Harbour(int tileSize, int[] ships){
        this.ships = ships;
        this.tileSize = tileSize;
        this.setSpacing(20);
    }

    public void initializeShip(VBox vBox){
        for(int y = 0; y < ships.length; y++){
            HBoxShip hBoxShip = new HBoxShip(new Ship(ships[y]), tileSize);

            this.getChildren().add(hBoxShip);
        }
        vBox.getChildren().add(this);
    }
}
