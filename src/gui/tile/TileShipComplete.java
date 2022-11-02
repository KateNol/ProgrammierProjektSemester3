package gui.tile;

import javafx.scene.layout.VBox;

public class TileShipComplete extends VBox {
    private int startPositionX;
    private int startPositionY;

    public TileShipComplete(int startPositionX, int startPositionY){
        this.startPositionX = startPositionX;
        this.startPositionY = startPositionY;
    }

    public int getStartPositionX() {
        return startPositionX;
    }

    public int getStartPositionY() {
        return startPositionY;
    }
}

