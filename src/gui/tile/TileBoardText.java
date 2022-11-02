package gui.tile;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * @author Stefan
 */
public class TileBoardText extends StackPane {
    public int tileSize = 40;

    /**
     *
     * @param s Labeling from the board
     */
    public TileBoardText(String s) {
        Rectangle rectangle = new Rectangle(tileSize - 1,tileSize - 1, Color.WHITE);
        rectangle.setStroke(Color.BLACK);

        Text text = new Text(s);
        text.setFill(Color.BLACK);
        text.setStyle("-fx-font-size: 15");
        this.setAlignment(text, Pos.CENTER);

        this.getChildren().addAll(rectangle, text);
    }

}
