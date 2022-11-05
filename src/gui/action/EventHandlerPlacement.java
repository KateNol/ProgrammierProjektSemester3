package gui.action;

import gui.objekt.HBoxShip;
import gui.tile.TileWater;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;

public class EventHandlerPlacement implements EventHandler<ActionEvent> {
    private int ShipLength;
    private int x;
    private int y;
    private boolean horizontal = true;

    @Override
    public void handle(ActionEvent event) {
        setOnMouseClicked(event);
    }

    private void setOnMouseClicked(Event e) {
        Node node = (Node) e.getSource();
        if(node instanceof HBoxShip){
            HBoxShip hBoxShip = (HBoxShip) e.getSource();
            ShipLength = hBoxShip.getShipLength();
        } else if (node instanceof TileWater){
            TileWater tileWater = (TileWater) e.getSource();
            x = tileWater.getCoordinateX();
            y = tileWater.getCoordinateY();
        }
        System.out.println(ShipLength + " " + x + " " + y);
    }
}
