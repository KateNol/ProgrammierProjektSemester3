package gui.controllers;

import gui.objekt.Board;
import gui.objekt.Harbour;
import gui.tile.TileShipComplete;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;


public class controllerLobby {
    private static final int BOARD_SIZE = 14;
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private Pane boardPane;

    public void initialize() throws IOException {
        Board playerBoard = new Board(BOARD_SIZE, 40, boardPane);
        playerBoard.initializeBoard();
        action.DraggableMaker draggableMaker = new action.DraggableMaker(14,40, boardPane);

        Harbour harbour = new Harbour(boardPane, draggableMaker);
        harbour.makeShip();


    }

    public void switchToSceneNetworkManager(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/gui/fxml/NetworkManager.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
