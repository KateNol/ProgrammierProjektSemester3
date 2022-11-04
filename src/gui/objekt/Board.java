package gui.objekt;

import gui.tile.TileBoardText;
import gui.tile.TileWater;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/**
 * @author Stefan
 */
public class Board extends BoardBase{
    private int[][] board;
    /**
     * create a Board
     * @param board
     * @param boardSize from 14x14 to 19x19 in Tiles
     * @param tileSize Pixel from one Tile
     * @param pane
     */
    public Board(int[][] board, int boardSize, int tileSize, Pane pane) {
        super(boardSize, tileSize, pane);
        this.board = board;
    }

    /**
     * Initialize the visible board with Water Tiles
     * and save the position from the tile
     * in x and y Coordinates
     */
    public void initializeBoard(){
        GridPane gridPane = new GridPane();
        for(int y = 0; y < getBoardSize() + 1; y++){
            for (int x = 0; x < getBoardSize() + 1; x++){
                if(y == 0 && x == 0){
                    TileBoardText tbt = new TileBoardText(x, y, getTileSize(),"");
                    gridPane.add(tbt, x, y, 1, 1);
                } else {
                    if(y == 0){
                        TileBoardText tbt = new TileBoardText(x, y, getTileSize(),"" + (char) ('A' + x - 1));
                        gridPane.add(tbt, x, y, 1, 1);
                    } else if (x == 0 && y > 0) {
                        TileBoardText tbt = new TileBoardText(x, y, getTileSize(),"" + y);
                        gridPane.add(tbt, x, y, 1, 1);
                    } else {
                        TileWater tile = new TileWater(x, y, getTileSize());
                        gridPane.add(tile, x, y, 1, 1);
                        board[x - 1][y - 1] = 0;
                    }
                }
            }
        }
        getPane().getChildren().add(gridPane);
    }

    /**
     * Print board on Console
     */
    public void showBoard(){
        for(int x = 0; x < board.length; x++){
            for(int y = 0; y < board.length; y++){
                System.out.print(board[x][y] + "  ");
            }
            System.out.println();
        }
        System.out.println("----------------------------------------");
    }
}
