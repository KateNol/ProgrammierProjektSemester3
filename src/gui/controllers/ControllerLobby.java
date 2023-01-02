package gui.controllers;

import gui.GUIPlayer;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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
    private Button alignmentVer;
    @FXML
    private Button alignmentHor;
    @FXML
    private Button random;


    //ships and failLabel
    @FXML
    private VBox vboxLeft;

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
        background.setBackground(Settings.setBackgroundImage("file:src/gui/img/hsh_lernniesche.png"));

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
     * Return to Screen Menu
     */
    public void onBack(){
        resetBoard();
        AudioPlayer.playSFX(Audio.Click);
        ViewSwitcher.switchTo(View.Menu);
    }

    /**
     * Reset Ships placed
     */
    public void resetBoard(){
        guiPlayer.getGuiBoard().resetShipPlaced();
        guiPlayer.confirmShipsPlaced(false);
    }

    /**
     * Switch to Screen Game
     */
    public void onStartGame(){
        guiPlayer.setBegin();
        AudioPlayer.playSFX(Audio.Click);
        ViewSwitcher.switchTo(View.Game);
    }

    /**
     * Choose horizontal Alignment for placing ships
     */
    public void setMyAlignmentHor() {
        AudioPlayer.playSFX(Audio.Click);
        System.out.println("hor");
        guiPlayer.setAlignment(Alignment.HOR_RIGHT);
        alignmentHor.setDisable(true);
        alignmentVer.setDisable(false);
    }

    /**
     * Choose horizontal Alignment for placing ships
     */
    public void setMyAlignmentVer() {
        AudioPlayer.playSFX(Audio.Click);
        System.out.println("ver");
        guiPlayer.setAlignment(Alignment.VERT_DOWN);
        alignmentVer.setDisable(true);
        alignmentHor.setDisable(false);
    }

    /**
     * Set random ships
     */
    public void onRandom(){
        AudioPlayer.playSFX(Audio.Click);
        vboxLeft.setMouseTransparent(true);
        guiPlayer.setRandomShips();
    }

    /**
     * Get RandomButton for ship placement
     * @return button
     */
    public Button getRandomButton(){
        return random;
    }

    /**
     * Get GUIShipBox where Ships are drawn
     * @return vboxLeft
     */
    public VBox getShipBox(){
        return vboxLeft;
    }
}
