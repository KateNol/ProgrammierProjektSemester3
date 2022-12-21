package gui.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
        background.setBackground(Settings.setBackgroundImage("file:src/gui/img/Menu_Background.jpg"));
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
}
