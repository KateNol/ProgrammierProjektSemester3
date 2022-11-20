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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        guiPlayer.setGuiBoard();
        guiPlayer.setGuiHarbour();
        guiPlayer.creatBoard(vBoxMiddle, vboxLeft);
    }

    public void onBack(){
        ViewSwitcher.switchTo(View.Menu);
    }

    public void onStartGame(){
        ViewSwitcher.switchTo(View.Game);
    }

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
