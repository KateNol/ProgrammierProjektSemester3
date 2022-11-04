package gui.objekt;

import javafx.scene.layout.Pane;

public class BoardWithShips {
    private Board board;
    private Harbour harbour;

    private int boardSize;
    private int[] sizeShip;
    private int tileSize;
    private int[][] boardArray;
    private Pane pane;

    public BoardWithShips(int[][] boardArray, int[] sizeShip, int boardSize, int tileSize, Pane pane){
        this.boardSize = boardSize;
        this.sizeShip = sizeShip;
        this.tileSize = tileSize;
        this.boardArray = boardArray;
        this.pane = pane;

        this.board = new Board(boardArray, sizeShip, boardSize, tileSize, pane);
        this.harbour = new Harbour(tileSize, sizeShip, pane);

        creatBoard();
    }

    public void creatBoard(){
        board.initializeBoard();
        harbour.initializeShip();
    }

    public Board getBoard() {
        return board;
    }

    public Harbour getHarbour() {
        return harbour;
    }
}
