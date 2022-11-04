package gui.controllers;

import gui.objekt.Board;
import gui.objekt.Harbour;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;


import java.io.IOException;


public class ControllerLobby extends Controller {
    private static final int BOARD_SIZE = 14;

    @FXML
    private Pane boardPane;

    public void initialize() throws IOException {
        Board playerBoard = new Board(BOARD_SIZE, 40, boardPane);
        playerBoard.initializeBoard();
        action.DraggableMaker draggableMaker = new action.DraggableMaker(14,40, boardPane);

        Harbour harbour = new Harbour(boardPane, draggableMaker);
        harbour.makeShip();
    }
}
