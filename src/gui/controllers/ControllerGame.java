package gui.controllers;

import gui.GUIPlayer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Stefan
 * Controller for game scene
 */
public class ControllerGame implements Initializable {

    //background
    @FXML
    private StackPane background;

    //game scene
    @FXML
    private HBox gameField;

    //end scene
    @FXML
    private VBox gameEnd;
    @FXML
    private Label winnerLabel;

    //names display
    @FXML
    private Label selfLabel;
    @FXML
    private Label enemyLabel;

    //turn display
    @FXML
    private Label turnLabel;

    //boards
    @FXML
    private VBox enemyBoard;
    @FXML
    private VBox myBoard;


    private static ControllerGame instance = null;
    private final GUIPlayer guiPlayer = GUIPlayer.getInstance();

    /**
     * Initialize Game Screen items(Player Board, Enemy Board)
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instance = this;

        //background music & picture
        AudioPlayer.playMusic(Audio.BattleMusic2);
        background.setBackground(Settings.setBackgroundImage("file:src/gui/img/game.jpg"));

        //Set Boards
        guiPlayer.getGuiBoard().getInitializedBoard(myBoard);
        guiPlayer.createEnemyBoard(enemyBoard);

        //Set Usernames over Board
        selfLabel.setText(guiPlayer.getUsername());
        enemyLabel.setText(guiPlayer.getEnemyUsername());

        if(guiPlayer.getWeBegin()){
            turnLabel.setText("It's " + guiPlayer.getUsername() + "'s Turn");
            guiPlayer.setTurn(true);
        } else {
            turnLabel.setText("It's " + guiPlayer.getEnemyUsername() + "'s Turn");
            guiPlayer.setTurn(false);
        }
    }

    /**
     * Returning the instance of ControllerGame
     * @return instance
     */
    public static ControllerGame getInstance(){
        return instance;
    }

    /**
     * Get turnLabel to set player turn
     * @return turnLabel
     */
    public Label getTurnLabel(){
        return turnLabel;
    }

    /**
     * Get winnerLabel to set who won
     * @return winnerLabel
     */
    public Label getWinnerLabel(){
        return winnerLabel;
    }

    /**
     * Open endScreen after game complete
     */
    public void openEndScreen() {
        gameField.setMouseTransparent(true);
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

    /**
     * to play with same player again
     */
    public void onPlayAgain(){
        AudioPlayer.playSFX(Audio.Click);
        ViewSwitcher.switchTo(View.Lobby);
    }

    /**
     * finish to player with player
     */
    public void onSwitchToMenu(){
        AudioPlayer.playSFX(Audio.Click);
        ViewSwitcher.switchTo(View.Menu);
    }
}
