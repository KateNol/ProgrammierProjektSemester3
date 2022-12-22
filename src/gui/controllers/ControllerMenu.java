package gui.controllers;

import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.*;

import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

import static gui.GUIMain.GITLAB_URL;
import static gui.GUIMain.getFXHostServices;

/**
 * @author Stefan
 * Controller for Menu Screen
 */
public class ControllerMenu implements Initializable {

    //background
    @FXML
    private VBox background;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        background.setBackground(Settings.setBackgroundImage("file:src/gui/img/FakIV.png"));

        Random rand = new Random();
        AudioPlayer.playMusic(rand.nextInt(5) + 1);
    }

    /**
     * Switch to Screen Load Game File Manager
     */
    public void onPlay(){
        if(!FileController.checkIfFolderExists()){
            try {
                FileController.createFolder();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        FileController.checkIfFileExists();
        AudioPlayer.playSFX(Audio.Click);
        ViewSwitcher.switchTo(View.FileManager);
    }

    /**
     * Switch to Screen Rule
     */
    public void onRules(){
        AudioPlayer.playSFX(Audio.Click);
        ViewSwitcher.switchTo(View.Rules);
    }

    /**
     * Switch to Screen Settings
     */
    public void onSettings(){
        AudioPlayer.playSFX(Audio.Click);
        ViewSwitcher.switchTo(View.Settings);
    }

    /**
     * Close Game
     */
    public void onExit(){
        AudioPlayer.playSFX(Audio.Click);
        Platform.exit();
    }

    /**
     * Opens project GibLab URL
     */
    public void onGitlab() {
        HostServices hostServices = getFXHostServices();
        hostServices.showDocument(GITLAB_URL);
    }
}
