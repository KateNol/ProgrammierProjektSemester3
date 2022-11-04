package gui.objekt;

import gui.tile.TileShip;
import gui.tile.TileShipComplete;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class Harbour {
    private int[] sizeShip = {2, 2, 2, 2, 4, 6};
    Pane pane;

    int startx = 700;
    int starty = 0;

   action.DraggableMaker draggableMaker;

    public Harbour(Pane pane, action.DraggableMaker draggableMaker){
        this.pane = pane;
        this.draggableMaker = draggableMaker;
    }

    /**
     * Initialize Ships
     */
    public void inizializeShips(){

        int count = 0;
        int last_number = sizeShip[0];

        for(int y = 0; y < sizeShip.length; y++){
            TileShipComplete tileShipComplete = new TileShipComplete(100,100);
            if(last_number != sizeShip[y]){
                count = 0;
            }
            count++;
            last_number = sizeShip[y];
            for(int x = 0; x < sizeShip[y]; x++){
                TileShip ts = new TileShip(x, y, draggableMaker.getTileSize());
                tileShipComplete.getChildren().add(ts);
            }
            Label lblCount = new Label(Integer.toString(count));
            pane.getChildren().addAll(tileShipComplete, lblCount);
        }
    }

    public void makeShip(){
        for(int y = 0; y < sizeShip.length; y++){
            TileShipComplete tsc = new TileShipComplete(startx, starty);
            tsc.setTranslateX(700+ (y * 50));
            draggableMaker.makeDraggable(tsc);
            for( int x = 0; x < sizeShip[y]; x++){
                TileShip tileShip = new TileShip(x, y, draggableMaker.getTileSize());
                tsc.getChildren().add(tileShip);
            }
            pane.getChildren().add(tsc);
        }

    }
}
