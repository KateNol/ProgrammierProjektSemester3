package gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import gui.tile.TileBoardText;
import gui.tile.TileWater;

import java.io.IOException;


public class controllerLobby {
    private static final int BOARD_SIZE = 19;
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private VBox vBox_Board;

    /**
     * Initialize the visible board with Water Tiles
     * and save the position from the tile
     * in x and y Coordinates
     */
    public void initialize() throws IOException {


        for (int x = 0; x < BOARD_SIZE + 1; x++) {
            HBox row = new HBox();
            for (int y = 0; y < BOARD_SIZE + 1; y++) {
                if (y == 0 && x == 0) {
                    TileBoardText tbt = new TileBoardText("");
                    row.getChildren().add(tbt);
                } else {
                    if (x == 0) {
                        TileBoardText tbt = new TileBoardText("     " + (char) ('A' + y - 1));
                        row.getChildren().add(tbt);
                    } else if (y == 0 && x > 0) {
                        TileBoardText tbt = new TileBoardText("" + x);
                        row.getChildren().add(tbt);
                    } else {
                        TileWater tile = new TileWater(x, y);
                        row.getChildren().add(tile);
                    }

                }
            }
            vBox_Board.getChildren().add(row);
        }
    }

    public void switchToSceneNetworkManager(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/gui/fxml/NetworkManager.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
