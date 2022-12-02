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
    private Label selfLabel;
    @FXML
    private Label turnLabel;
    @FXML
    private Label enemyLabel;
    @FXML
    private MenuItem exit;
    @FXML
    private Button ff;
    @FXML
    private VBox enemyBoard;
    @FXML
    private VBox myBoard;

    private GUIPlayer guiPlayer = GUIPlayer.getInstance();

    /**
     * Initialize Game Screen items(Player Board, Enemy Board)
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        guiPlayer.getGuiBoard().getInitializedBoard(myBoard);
        guiPlayer.createEnemyBoard(enemyBoard);
        selfLabel.setText(guiPlayer.getUsername());
        enemyLabel.setText(guiPlayer.getEnemyUsername());
        setTurnLabel();

    }

    public void setTurnLabel(){
        if(guiPlayer.getWeBegin()){
            turnLabel.setText("It's "+ enemyLabel.getText() +"'s Turn");
        } else {
            turnLabel.setText("It's "+ guiPlayer.getUsername() +"'s Turn");
        }
    }

    /**
     * Return to Screen Menu
     */
    public void onExit(){
        ViewSwitcher.switchTo(View.Menu);
    }
}
