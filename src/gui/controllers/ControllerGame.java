package gui.controllers;

import gui.GUIPlayer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import logic.GUIAIPlayer;
import logic.Util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

import static gui.Util.log_stderr;

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
    MediaPlayer mediaPlayer = null;
    @FXML
    private Pane easterEggPane;
    @FXML
    private Button playAgain;

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

    @FXML
    private TextArea chatArea;
    @FXML
    private TextField chatInputField;

    private static ControllerGame instance = null;
    private final GUIPlayer guiPlayer = GUIPlayer.getInstance();

    /**
     * Initialize Game Screen items(Player Board, Enemy Board)
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        while (!GUIPlayer.getInstance().getIsConnectionEstablished()) {
            log_stderr("gui spinning on connection");
        }

        instance = this;
        if(guiPlayer.getNegotiatedSemester() == 6){
            easterEggPane.getChildren().add(setEsterEgg());
        }

        //background picture
        background.setBackground(Settings.setBackgroundImage("/img/hsh_audimax_innen.png"));

        //Set Boards
        guiPlayer.getGuiBoard().getInitializedBoard(myBoard);
        guiPlayer.createEnemyBoard(enemyBoard);

        //Set Usernames over Board
        selfLabel.setText(guiPlayer.getUsername());
        enemyLabel.setText(guiPlayer.getEnemyUsername());

        if(guiPlayer instanceof GUIAIPlayer){
            Util.log_debug("no need for turn and color update at begin");
        } else {
            if(guiPlayer.getWeBegin()){
                turnLabel.setText("It's " + guiPlayer.getUsername() + "'s Turn");
                guiPlayer.getGuiBoard().setTopLeftCorner(Color.DARKRED);
                guiPlayer.getGuiEnemyBoard().setTopLeftCorner(Color.DARKGREEN);
            } else {
                turnLabel.setText("It's " + guiPlayer.getEnemyUsername() + "'s Turn");
                guiPlayer.getGuiBoard().setTopLeftCorner(Color.DARKGREEN);
                guiPlayer.getGuiEnemyBoard().setTopLeftCorner(Color.DARKRED);
            }
        }

        chatArea.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                chatArea.setScrollTop(Double.MAX_VALUE);
            }
        });
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
        AudioPlayer.destroyMusic();
        gameField.setMouseTransparent(true);
        gameEnd.setStyle("-fx-background-color: #EAEAEA");
        gameEnd.setVisible(true);
    }

    /**
     * Return to Menu Scene when you want to stop play
     */
    public void onSurrender(){
        guiPlayer.abortEstablishConnection();
        AudioPlayer.playSFX(Audio.Click);
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }
        ViewSwitcher.switchTo(View.Menu);
    }

    /**
     * to play with same player again
     */
    public void onPlayAgain(){
        playAgain.setText("Coming soon!");
        /*
        ControllerLobby.getInstance().resetBoard();
        AudioPlayer.playSFX(Audio.Click);
        ViewSwitcher.switchTo(View.Lobby);
         */
    }

    /**
     * finish to player with player
     */
    public void onSwitchToMenu(){
        ControllerFileManager.getInstance().setFileNamesOnButton();
        AudioPlayer.playSFX(Audio.Click);
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }
        try {
            AudioPlayer.destroyMusic();
        } catch (Exception e){
            gui.Util.log_debug("destroy failed");
        }

        easterEggPane.setVisible(false);
        gameField.setMouseTransparent(false);
        ViewSwitcher.switchTo(View.Menu);
        Random rand = new Random();
        AudioPlayer.playMusic(rand.nextInt(5) + 1);
    }

    /**
     * Send message in chat
     */
    public void onSend() {
        String message = chatInputField.getText();
        chatInputField.clear();
        displayChatMessage("> " + guiPlayer.getUsername() + ": " + message);
        guiPlayer.sendChatMessage(message);
    }

    /**
     * Display ChatMessage
     * @param message ChatMessage
     */
    public void displayChatMessage(String message) {
        chatArea.setText(chatArea.getText() + message + "\n");
        chatArea.appendText("");
    }

    /**
     * Set EasterEgg
     * @return mediaView
     */
    public MediaView setEsterEgg() {
        File mediaFile = new File("src/gui/video/Video.mp4");
        Media media = null;
        try {
            media = new Media(mediaFile.toURI().toURL().toString());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        mediaPlayer = new MediaPlayer(media);

        MediaView mediaView = new MediaView(mediaPlayer);
        mediaView.fitWidthProperty().bind(ViewSwitcher.getStage().widthProperty());
        mediaView.fitHeightProperty().bind(ViewSwitcher.getStage().heightProperty());

        return mediaView;
    }

    /**
     * Play EasterEgg
     */
    public void playEsterEgg(){
        gameEnd.setStyle("-fx-background-color: transparent");
        gameEnd.setStyle("-fx-border-color: transparent");
        easterEggPane.setVisible(true);
        mediaPlayer.play();
    }
}
