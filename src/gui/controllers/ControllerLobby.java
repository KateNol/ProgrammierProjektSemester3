package gui.controllers;

import gui.GUIPlayer;

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
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        BackgroundSize backgroundSize = new BackgroundSize(1, 1, true, true, false, false);
        BackgroundImage backgroundImage = new BackgroundImage((new Image("file:src/gui/img/setShip.jfif")), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,backgroundSize);
        background.setBackground(new Background(backgroundImage));

        startGame.setDisable(true);
        guiPlayer.creatBoard(startGame, vBoxMiddle, vboxLeft, startGame, failedshipPlacedLabel);
    }

    /**
     * Return to Screen Menu
     */
    public void onBack(){
        guiPlayer.getGuiBoard().setShipPlaced();
        guiPlayer.confirmShipsPlaced(false);
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
        alignment.setOnMouseClicked(actionEvent -> {
            switch (alignmentCounter) {
                case 0: {
                    System.out.println("Horizontal_Right");
                    alignment.setText("Horizontal_Right");
                    guiPlayer.setAlignment(Alignment.HOR_RIGHT);
                    alignmentCounter++;
                    break;
                }
                case 1: {
                    System.out.println("Horizontal_Left");
                    alignment.setText("Horizontal_Left");
                    guiPlayer.setAlignment(Alignment.HOR_LEFT);
                    alignmentCounter++;
                    break;
                }
                case 2: {
                    System.out.println("Vertical_Down");
                    alignment.setText("Vertical_Down");
                    guiPlayer.setAlignment(Alignment.VERT_DOWN);
                    alignmentCounter++;
                    break;
                }
                case 3: {
                    System.out.println("Vertical_Up");
                    alignment.setText("Vertical_Up");
                    guiPlayer.setAlignment(Alignment.VERT_UP);
                    alignmentCounter = 0;
                }
            }
        });
    }
}
