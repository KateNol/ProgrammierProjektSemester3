package gui.controllers;

import gui.GUIPlayer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerGame implements Initializable {

    @FXML
    private MenuItem exit;

    @FXML
    private Button ff;

    @FXML
    private VBox enemyBoard;

    @FXML
    private VBox menuBar;

    @FXML
    private VBox myBoard;

    @FXML
    private Label turn;

    private GUIPlayer guiPlayer = GUIPlayer.getInstance();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        guiPlayer.getGuiBoard().getInitializedBoard(myBoard);
        //guiPlayer.getGuiEnemyBoard().getInitializedBoard(enemyBoard);
    }

    public void onExit(){
        ViewSwitcher.switchTo(View.Menu);
    }
}
