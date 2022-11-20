package gui.controllers;

import gui.GUIPlayer;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerLobby implements Initializable {

    @FXML
    private Label alignment;
    @FXML
    private VBox vBoxMiddle;
    @FXML
    private VBox vboxLeft;

    private GUIPlayer guiPlayer = GUIPlayer.getInstance();

    /**
     * Initialize Lobby Screen items(Board, Ship)
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        guiPlayer.creatBoard(vBoxMiddle, vboxLeft);
    }

    /**
     * Return to Screen Menu
     */
    public void onBack(){
        ViewSwitcher.switchTo(View.Menu);
    }

    /**
     * Switch to Screen Game
     */
    public void onStartGame(){
        ViewSwitcher.switchTo(View.Game);
    }

    /**
     * Choose Alignment for placing ships
     */
    public void setMyAlignment(){
        alignment.setOnMouseClicked(mouseEvent -> {
            if(guiPlayer.getGuiBoard().isVertical()){
                alignment.setText("horizontal");
                guiPlayer.getGuiBoard().setVertical(false);
            } else if (!guiPlayer.getGuiBoard().isVertical()) {
                alignment.setText("vertical");
                guiPlayer.getGuiBoard().setVertical(true);
            }
        });
    }
}
