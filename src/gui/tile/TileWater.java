package gui.tile;


import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class TileWater extends Rectangle {
    public int x, y;
    public String name;

    public TileWater(int x, int y) {
        super(40, 40);
        this.name = name;
        this.setWidth(40);
        this.setHeight(40);
        setFill(Color.AQUA);
        setStroke(Color.BLACK);
    }

}
