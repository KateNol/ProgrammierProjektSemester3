package gui.objekt;

import gui.tile.TileShip;
import javafx.scene.layout.HBox;

/**
 * @author Stefan
 */
public class HBoxShip extends HBox {

    private int shipLength;
    private int tileSize;
    private int shipSize;
    private int x;
    private int y;


    /**
     * Create a Ship as an HBox
     * @param shipLength The length from the Ship
     * @param tileSize TileSize in Pixel
     */
    public HBoxShip(int shipLength, int tileSize){
        this.shipLength = shipLength;
        this.tileSize = tileSize;
        for(int i = 0; i < shipLength; i++){
            TileShip tileShip = new TileShip(0, 0, tileSize);
            tileShip.setOnMouseClicked(e -> {

            });
            this.getChildren().add(tileShip);
        }
    }

    public int getShipLength(){
        return shipLength;
    }

    public void printShipLength(){
        System.out.println(shipLength);
    }
}

