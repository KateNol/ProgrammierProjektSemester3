package gui.controllers;

import gui.GUIPlayer;
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
        BackgroundSize backgroundSize = new BackgroundSize(1, 1, true, true, false, false);
        BackgroundImage backgroundImage = new BackgroundImage((new Image("file:src/gui/img/game.jpg")), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,backgroundSize);
        background.setBackground(new Background(backgroundImage));

        guiPlayer.getGuiBoard().getInitializedBoard(myBoard);
        guiPlayer.createEnemyBoard(enemyBoard);
        selfLabel.setText(guiPlayer.getUsername());
        enemyLabel.setText(guiPlayer.getEnemyUsername());
        setTurnLabel();

        turnLabel.textProperty().addListener((observableValue, s1, s2) -> {
            if(guiPlayer.isGameOver()) {
                openEndScreen();
            }
        });
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

    public void openEndScreen(){
        gamefield.setMouseTransparent(true);
        gameEnd.setVisible(true);
    }

    public void onPlayAgain(){
        ViewSwitcher.switchTo(View.Lobby);
    }

    public void onSwitchToMenu(){
        ViewSwitcher.switchTo(View.Menu);
    }
}
