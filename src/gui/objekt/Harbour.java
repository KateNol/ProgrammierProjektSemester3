package gui.objekt;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * @author Stefan
 */
public class Harbour extends VBox {
    private int[] sizeShip;
    private int tileSize;
    private Pane pane;
    double startx;
    double starty;

    /**
     *
     * @param tileSize
     * @param sizeShip
     * @param pane
     */

    public Harbour(int tileSize, int[] sizeShip, Pane pane){
        this.sizeShip = sizeShip;
        this.tileSize = tileSize;
        this.pane = pane;
    }

    /**
     * Initialize Ships from given Array with ship lengths
     */
    public void initializeShip(){
        for(int y = 0; y < sizeShip.length; y++){
            HBoxShip hBoxShip = new HBoxShip(sizeShip[y], tileSize);
            this.getChildren().add(hBoxShip);
        }
        pane.getChildren().add(this);
        this.getChildren().forEach(this::makeDraggable);
    }

    private void makeDraggable(Node node) {
        node.setOnMousePressed(e -> {
            //calc offeset
            startx = e.getSceneX() - node.getTranslateX();
            starty = e.getSceneY() - node.getTranslateY();
        });

        node.setOnMouseDragged(e -> {
            //set
            node.setTranslateX(e.getSceneX() - startx);
            node.setTranslateY(e.getSceneY() - starty);
        });
    }
}
