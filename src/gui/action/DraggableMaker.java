package gui.action;

import gui.objekt.BoardBase;
import gui.objekt.HBoxShip;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class DraggableMaker extends BoardBase {

    private double mousePositionX;
    private double MousePositionY;

    /**
     * The Base parameter for a Board
     *
     * @param boardSize from 14x14 to 19x19 in Tiles
     * @param tileSize  Pixel from one Tile
     * @param pane
     */
    public DraggableMaker(int boardSize, int tileSize, Pane pane) {
        super(boardSize, tileSize, pane);
    }
    public void makeDraggable(HBoxShip reg){
        Node node = reg;
        node.setOnMouseDragged(mouseEvent -> {
            mousePositionX = mouseEvent.getSceneX();
            MousePositionY = mouseEvent.getSceneY();

            int x = (int) ((mousePositionX/getTileSize()) % getBoardSize()) * getTileSize();
            int y = (int) ((MousePositionY/getTileSize()) % getBoardSize()) * getTileSize();

            //node.setLayoutX(x - reg.getStartPositionX());
            //node.setLayoutY(y - reg.getStartPositionY());
        });
    }

    public void makeDraggable(Node node){
        node.setOnMouseReleased(mouseEvent -> {
            mousePositionX = mouseEvent.getSceneX();
            MousePositionY = mouseEvent.getSceneY();

            int x = (int) ((mousePositionX/getTileSize()) % getBoardSize()) * getTileSize();
            int y = (int) ((MousePositionY/getTileSize()) % getBoardSize()) * getTileSize();

            node.setLayoutX(x);
            node.setLayoutY(y);
        });
    }
}
