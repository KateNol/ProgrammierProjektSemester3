package gui.controllers;

import gui.GUIPlayer;
import gui.Util;
import gui.objekt.GuiBoard;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import logic.PlayerConfig;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerGame implements Initializable {

    @FXML
    private HBox gamefield;
    @FXML
    private VBox gameEnd;
    @FXML
    private StackPane background;
    @FXML
    private Label selfLabel;
    @FXML
    private Label turnLabel;
    @FXML
    private Label enemyLabel;
    @FXML
    private VBox enemyBoard;
    @FXML
    private VBox myBoard;
    @FXML
    private Label winnerLabel;

    private static ControllerGame instance = null;

    private final GUIPlayer guiPlayer = GUIPlayer.getInstance();

    /**
     * Initialize Game Screen items(Player Board, Enemy Board)
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        AudioPlayer.playMusic(Audio.BattleMusic2);

        instance = this;
        background.setBackground(Settings.setBackgroundImage("file:src/gui/img/game.jpg"));

        //Set Boards
        guiPlayer.getGuiBoard().getInitializedBoard(myBoard);
        guiPlayer.createEnemyBoard(enemyBoard);

        //Set Usernames over Board
        selfLabel.setText(guiPlayer.getUsername());
        enemyLabel.setText(guiPlayer.getEnemyUsername());

        if(guiPlayer.getWeBegin()){
            turnLabel.setText("It's " + guiPlayer.getUsername() + "'s Turn");
        } else {
            turnLabel.setText("It's " + guiPlayer.getEnemyUsername() + "'s Turn");
        }

        //Control if Game over
        turnLabel.textProperty().addListener((observableValue, oldVal, newVal) -> {
            if(false){
                Util.log_debug("game over");
                openEndScreen();
            }
        });
    }

    public static ControllerGame getInstance(){
        return instance;
    }

    public Label getTurnLabel(){
        return turnLabel;
    }

    public Label getWinnerLabel(){
        return winnerLabel;
    }

    /**
     * Return to Screen Menu
     */
    public void onExit(){
        AudioPlayer.playSFX(Audio.Click);
        ViewSwitcher.switchTo(View.Menu);
    }

    public void openEndScreen() {
        gamefield.setMouseTransparent(true);
        gameEnd.setVisible(true);
    }

    /**
     * Return to Menu Scene when you want to stop play
     */
    public void onSurrender(){
        guiPlayer.abortEstablishConnection();
        AudioPlayer.playSFX(Audio.Click);
        ViewSwitcher.switchTo(View.Menu);
    }

    public void onPlayAgain(){
        AudioPlayer.playSFX(Audio.Click);
        ViewSwitcher.switchTo(View.Lobby);
    }

    public void onSwitchToMenu(){
        AudioPlayer.playSFX(Audio.Click);
        ViewSwitcher.switchTo(View.Menu);
    }
}
