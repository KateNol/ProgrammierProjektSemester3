package gui.controllers;

import gui.GUIPlayer;

import gui.Util;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import logic.Alignment;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Stefan
 * Controller for lobby scene
 */
public class ControllerLobby implements Initializable {

    @FXML
    private HBox background;

    //alignment
    @FXML
    private Button alignment;
    private int alignmentCounter = 1;

    //ships and failLabel
    @FXML
    private VBox vboxLeft;
    @FXML
    private Label failedShipPlacedLabel;

    //board
    @FXML
    private VBox vBoxMiddle;

    //start game
    @FXML
    private Button startGame;

    private static ControllerLobby instance = null;
    private final GUIPlayer guiPlayer = GUIPlayer.getInstance();

    /**
     * Initialize Lobby Screen items(Board, Ship)
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instance = this;
        background.setBackground(Settings.setBackgroundImage("file:src/gui/img/setShip.jfif"));

        //set true if the first turn is our
        guiPlayer.setTurn(guiPlayer.getWeBegin());

        //disable startButton until all ships are placed
        startGame.setDisable(true);
        //create our board
        guiPlayer.creatBoard(vBoxMiddle, vboxLeft);
    }

    /**
     * Returning the instance of a ControllerLobby
     * @return instance
     */
    public static ControllerLobby getInstance(){
        return instance;
    }

    /**
     * Enable start button
     */
    public void enableStartButton(){
        startGame.setDisable(false);
    }

    /**
     * Get label for display failed attempted to place a ship
     * @return failedShipPlacedLabel
     */
    public Label getFailedShipPlacedLabel(){
        return failedShipPlacedLabel;
    }

    /**
     * Return to Screen Menu
     */
    public void onBack(){
        resetBoard();
        AudioPlayer.playSFX(Audio.Click);
        ViewSwitcher.switchTo(View.Menu);
    }

    public void resetBoard(){
        guiPlayer.getGuiBoard().resetShipPlaced();
        guiPlayer.confirmShipsPlaced(false);
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
