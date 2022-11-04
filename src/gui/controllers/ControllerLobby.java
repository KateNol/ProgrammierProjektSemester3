package gui.controllers;

import gui.objekt.BoardWithShips;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.io.IOException;


public class ControllerLobby extends Controller {
    private static final int BOARD_SIZE = 14;
    private static final int[] SIZE_SHIP = {2, 2, 2, 2, 4, 6};
    private static final int TILE_SIZE = 40;
    private static final int[][] BOARD = new int[BOARD_SIZE][BOARD_SIZE];

    @FXML
    private Pane boardPane;
    @FXML
    private HBox hbox;

    public void initialize() throws IOException {
        BoardWithShips playerBoard = new BoardWithShips(BOARD,SIZE_SHIP,BOARD_SIZE,TILE_SIZE,hbox);

        playerBoard.getBoard().printBoard();

    }
}
