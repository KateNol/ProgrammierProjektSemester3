package gui.objekt;

import gui.tile.TileShip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import logic.Coordinate;
import logic.Ship;
import logic.Util;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is a VBox for a List of Gui Ships
 * @author Stefan
 */
public class GuiHarbour extends VBox {
    private final int tileSize;
    private final ArrayList<Ship> ships;
    private Ship selectedShip = null;
    private HashMap<Ship, GuiHBoxShip> guiHBoxShips = new HashMap<>();

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
     * @param vBox where to fill ships
     */
    public void initializeShip(VBox vBox){
        for (Ship ship : ships) {
            GuiHBoxShip hBoxShip = new GuiHBoxShip(ship, tileSize);
            this.getChildren().add(hBoxShip);
            guiHBoxShips.put(ship, hBoxShip);
        }
        vBox.getChildren().add(this);
    }

    /**
     * Draw Ship on Board
     * @param gridPane where to fill
     * @param witchShip with witch tiles
     */
    public void drawShipOnBoard(GridPane gridPane, int witchShip){
        Coordinate[] coordinate = ships.get(witchShip).getPos();
        for (int i = 0; i < ships.get(witchShip).getPos().length; i++) {
            Coordinate c = new Coordinate(coordinate[i].row() + 1, coordinate[i].col() + 1);
            gridPane.add(new TileShip(c, tileSize), c.col(), c.row(), 1, 1);
            Util.log_debug(printPlaced(coordinate[i], c));
        }
    }

    /**
     * Message
     * @param coordinate where ship is supposed to be
     * @param c coordinate clicked on
     * @return prompt
     */
    public String printPlaced(Coordinate coordinate, Coordinate c){
        return "drawOnBoardLogic = row: " + coordinate.row() + " col: " + coordinate.col() + " " + "drawOnBoardGUI = row: " + c.row() + " col: " + c.col();
    }

    /**
     * Get selectedShip
     * @return selectedShip
     */
    public Ship getSelectedShip() {
        return selectedShip;
    }

    /**
     * Set selectedShip
     * @param s ship
     */
    public void setSelectedShip(Ship s) {
        selectedShip = s;
    }

    /**
     * Get guiHBoxShips
     * @return guiHBoxShips
     */
    public HashMap<Ship, GuiHBoxShip> getGuiHBoxShips() {
        return guiHBoxShips;
    }
}
