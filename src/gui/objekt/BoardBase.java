package gui.objekt;

import javafx.scene.layout.Pane;

/**
 *  @author Stefan
 */
public class BoardBase {
    private int boardHeight;
    private int boardWidth;
    private int boardSize;
    private int tileSize;
    private Pane pane;

    /**
     * The Base parameter for a Board
     * @param boardSize from 14x14 to 19x19 in Tiles
     * @param tileSize Pixel from one Tile
     * @param pane
     */
    public BoardBase(int boardSize, int tileSize, Pane pane){
        this.boardSize = boardSize;
        this.tileSize = tileSize;
        this.pane = pane;
        this.boardHeight = boardSize * tileSize;
        boardWidth = boardSize * tileSize;
    }

    /**
     * Get the height from the board
     * @return boardHeight
     */
    public int getBoardHeight() {
        return boardHeight;
    }

    /**
     * Get the width from the board
     * @return boardWidth
     */
    public int getBoardWidth() {
        return boardWidth;
    }

    /**
     * Get the amount of Tiles in horizontal and vertical
     * @return boardSize
     */
    public int getBoardSize() {
        return boardSize;
    }

    /**
     * Get Pixel from one Tile
     * @return tileSize
     */
    public int getTileSize() {
        return tileSize;
    }

    /**
     * Get
     * @return pane
     */
    public Pane getPane() {
        return pane;
    }
}
