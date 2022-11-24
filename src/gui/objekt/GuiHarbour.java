package gui.objekt;

import gui.tile.TileShip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import logic.Coordinate;
import logic.Ship;

import java.util.ArrayList;

/**
 * @author Stefan
 */
public class GuiHarbour extends VBox {
    private int tileSize;
    private ArrayList<Ship> ships;

    /**
     * Gui object for a set of ships
     * @param tileSize TileSize in Pixel
     * @param ships Array of ships
     */
    public GuiHarbour(int tileSize, ArrayList<Ship> ships){
        this.tileSize = tileSize;
        this.ships = ships;
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

    public void drawShipOnBoard(GridPane gridPane, int witchShip){
        Coordinate coordinate[] = ships.get(witchShip).getPos();
        for (int i = 0; i < ships.get(witchShip).getPos().length; i++) {
            gridPane.add(new TileShip(coordinate[i], tileSize), coordinate[i].col() + 1, coordinate[i].row() + 1, 1, 1); //FIXME
            printPlaced(coordinate[i]);
        }
    }

    public void printPlaced(Coordinate coordinate){
        System.out.println("drawOnBoard = row: " + coordinate.row() + " col: " + coordinate.col());
    }
}
