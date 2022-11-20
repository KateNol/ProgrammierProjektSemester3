package gui.tile;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import logic.Coordinate;

/**
 * @author Stefan
 */
public class TileBoardText extends StackPane {
    private Tile tile;

    /**
     * Outer Tile for labeling the board
     * @param tileSize TileSize in Pixel
     * @param s Labeling from the board
     */
    public TileBoardText(Coordinate coordinate, int tileSize, String s) {
        this.tile = new Tile(coordinate, tileSize);
        tile.setFill(Color.WHITE);

        Text text = new Text(s);
        text.setFill(Color.BLACK);
        text.setStyle("-fx-font-size: 15");

        this.getChildren().addAll(tile, text);
    }
}