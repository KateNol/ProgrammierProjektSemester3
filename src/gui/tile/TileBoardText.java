package gui.tile;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class TileBoardText extends StackPane {
    private String s;

    public TileBoardText(String s) {
        Rectangle rectangle = new Rectangle();
        rectangle.setHeight(40);
        rectangle.setWidth(40);
        rectangle.setStroke(Color.BLACK);
        rectangle.setFill(Color.WHITE);
        Text text = new Text(s);
        text.setFill(Color.BLACK);
        text.setStyle("-fx-font-size: 15");
        this.getChildren().addAll(rectangle, text);
    }
}
