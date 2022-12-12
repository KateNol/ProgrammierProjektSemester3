package gui.tile;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import logic.Coordinate;

/**
 * @author Stefan
 */
public class TileBoardText extends StackPane {

    /**
     * Outer Tile for labeling the board
     * @param coordinate Coordinates where the labeling is
     * @param tileSize TileSize in Pixel
     * @param s Labeling from the board
     */
    public TileBoardText(Coordinate coordinate, int tileSize, String s) {
        this(coordinate, tileSize, s, Color.BLACK);
    }

    public TileBoardText(Coordinate coordinate, int tileSize, String s, Color color) {
        Tile tile = new Tile(coordinate, tileSize);
        tile.setFill(Color.WHITE);

        Text text = new Text(s);
        text.setFill(color);
        text.setStyle("-fx-font-size: 15");
        text.setStyle("-fx-font-weight: bold;");

        this.getChildren().addAll(tile, text);
    }

}
