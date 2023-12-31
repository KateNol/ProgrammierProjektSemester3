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
        this(coordinate, tileSize, s, Color.WHITE);
    }

    /**
     * Outer Tile for labeling the board with Color
     * @param coordinate Coordinates where the labeling is
     * @param tileSize TileSize in Pixel
     * @param s Labeling from the board
     * @param color color from Tile
     */
    public TileBoardText(Coordinate coordinate, int tileSize, String s, Color color) {
        Tile tile = new Tile(coordinate, tileSize);
        tile.setFill(color);

        Text text = new Text(s);
        text.setFill(color == Color.WHITE ? Color.BLACK : Color.WHITE);
        text.setStyle("-fx-font-size: 15");
        text.setStyle("-fx-font-weight: bold;");

        this.getChildren().addAll(tile, text);
    }
}
