package gui.controllers;

import gui.GUIPlayer;

import gui.Util;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import logic.Alignment;

import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

public class ControllerLobby implements Initializable {

    @FXML
    private HBox background;
    @FXML
    private Button alignment;
    @FXML
    private VBox vBoxMiddle;
    @FXML
    private VBox vboxLeft;
    @FXML
    private Button startGame;
    @FXML
    private Label failedshipPlacedLabel;

    private GUIPlayer guiPlayer = GUIPlayer.getInstance();

    private int alignmentCounter = 1;

    /**
     * Initialize Lobby Screen items(Board, Ship)
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        background.setBackground(Settings.setBackgroundImage("file:src/gui/img/setShip.jfif"));
        if(guiPlayer.getWeBegin()){
            guiPlayer.setTurn(true);
        } else {
            guiPlayer.setTurn(false);
        }

        startGame.setDisable(true);
        guiPlayer.creatBoard(startGame, vBoxMiddle, vboxLeft, startGame, failedshipPlacedLabel);
    }

    /**
     * Return to Screen Menu
     */
    public void onBack(){
        guiPlayer.getGuiBoard().setShipPlaced();
        guiPlayer.confirmShipsPlaced(false);
        AudioPlayer.playSFX(Audio.Click);
        ViewSwitcher.switchTo(View.Menu);
    }

    /**
     * Switch to Screen Game
     */
    public void onStartGame(){
        AudioPlayer.playSFX(Audio.Click);
        ViewSwitcher.switchTo(View.Game);
    }

    /**
     * Choose Alignment for placing ships
     */

    public void setMyAlignment(){
        alignment.setOnMouseClicked(actionEvent -> {
            AudioPlayer.playSFX(Audio.Click);
            switch (alignmentCounter) {
                case 0 -> {
                    Util.log_debug("Horizontal_Right");
                    alignment.setText("Horizontal_Right");
                    guiPlayer.setAlignment(Alignment.HOR_RIGHT);
                    alignmentCounter++;
                }
                case 1 -> {
                    Util.log_debug("Horizontal_Left");
                    alignment.setText("Horizontal_Left");
                    guiPlayer.setAlignment(Alignment.HOR_LEFT);
                    alignmentCounter++;
                }
                case 2 -> {
                    Util.log_debug("Vertical_Down");
                    alignment.setText("Vertical_Down");
                    guiPlayer.setAlignment(Alignment.VERT_DOWN);
                    alignmentCounter++;
                }
                case 3 -> {
                    Util.log_debug("Vertical_Up");
                    alignment.setText("Vertical_Up");
                    guiPlayer.setAlignment(Alignment.VERT_UP);
                    alignmentCounter = 0;
                }
            }
        });
    }
}
