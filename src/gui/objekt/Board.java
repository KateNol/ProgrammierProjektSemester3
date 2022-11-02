package gui.objekt;

import gui.tile.TileBoardText;
import gui.tile.TileHit;
import gui.tile.TileMiss;
import gui.tile.TileWater;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/**
 * @author Stefan
 */
public class Board extends BoardBase{
    /**
     * create a Board
     * @param boardSize from 14x14 to 19x19 in Tiles
     * @param tileSize Pixel from one Tile
     * @param pane
     */
    public Board(int boardSize, int tileSize, Pane pane) {
        super(boardSize, tileSize, pane);
    }

    /**
     * Initialize the visible board with Water Tiles
     * and save the position from the tile
     * in x and y Coordinates
     */
    public void initializeBoard(){
        GridPane gridPane = new GridPane();
        //gridPane.setTranslateX(500);
        for(int y = 0; y < getBoardSize() + 1; y++){
            for (int x = 0; x < getBoardSize() + 1; x++){
                if(y == 0 && x == 0){
                    TileBoardText tbt = new TileBoardText("");
                    gridPane.add(tbt, x, y, 1, 1);
                } else {
                    if(y == 0){
                        TileBoardText tbt = new TileBoardText("" + (char) ('A' + x - 1));
                        gridPane.add(tbt, x, y, 1, 1);
                    } else if (x == 0 && y > 0) {
                        TileBoardText tbt = new TileBoardText("" + y);
                        gridPane.add(tbt, x, y, 1, 1);
                    } else {
                        TileWater tile = new TileWater(x, y);
                        tile.setOnMouseClicked(mouseEvent -> {

                            TileMiss tileHit = new TileMiss(tile.getCoordinateX(), tile.getCoordinateY());
                            gridPane.add(tileHit, tile.getCoordinateX(), tile.getCoordinateY(), 1, 1);
                        });
                        gridPane.add(tile, x, y, 1, 1);
                    }
                }
            }
        }
        getPane().getChildren().add(gridPane);
    }
}
