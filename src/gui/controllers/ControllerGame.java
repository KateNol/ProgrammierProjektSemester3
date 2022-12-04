package gui.controllers;

import gui.GUIPlayer;
import gui.Util;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

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


    private boolean myTurn;

    private GUIPlayer guiPlayer = GUIPlayer.getInstance();

    /**
     * Initialize Game Screen items(Player Board, Enemy Board)
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        BackgroundSize backgroundSize = new BackgroundSize(1, 1, true, true, false, false);
        BackgroundImage backgroundImage = new BackgroundImage((new Image("file:src/gui/img/game.jpg")), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,backgroundSize);
        background.setBackground(new Background(backgroundImage));

        guiPlayer.getGuiBoard().getInitializedBoard(myBoard);
        guiPlayer.createEnemyBoard(enemyBoard);
        selfLabel.setText(guiPlayer.getUsername());
        enemyLabel.setText(guiPlayer.getEnemyUsername());

        if (guiPlayer.getWeBegin()) {
            turnLabel.setText("It's "+ enemyLabel.getText() +"'s Turn");
            myTurn = false;
        } else {
            turnLabel.setText("It's "+ guiPlayer.getUsername() +"'s Turn");
        }

        turnLabel.textProperty().addListener((observableValue, oldVal, newVal) -> {
            Util.log_debug(oldVal);
            Util.log_debug(newVal);
            if(guiPlayer.isGameOver()){
                openEndScreen();
            }
        });
    }

    public void setTurnLabel(){
        if(myTurn){
            turnLabel.setText("It's "+ enemyLabel.getText() +"'s Turn");
            myTurn = false;
        } else {
            turnLabel.setText("It's "+ guiPlayer.getUsername() +"'s Turn");
        }
        if(guiPlayer.isGameOver()){
            Util.log_debug("game over set in label");
            turnLabel.setText("game over");
            openEndScreen();
        }
    }

    /**
     * Return to Screen Menu
     */
    public void onExit(){
        ViewSwitcher.switchTo(View.Menu);
    }

    public void openEndScreen(){
        gamefield.setMouseTransparent(true);
        gameEnd.setVisible(true);
    }

    /**
     * Return to Menu Scene when you want to stop play
     */
    public void onSurrender(){
        guiPlayer.abortEstablishConnection();
        ViewSwitcher.switchTo(View.Menu);
    }

    public void onPlayAgain(){
        ViewSwitcher.switchTo(View.Lobby);
    }

    public void onSwitchToMenu(){
        ViewSwitcher.switchTo(View.Menu);
    }
}
