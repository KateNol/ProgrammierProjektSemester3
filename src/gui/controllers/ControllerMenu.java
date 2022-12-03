package gui.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for Menu Screen
 */
public class ControllerMenu implements Initializable {

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
        ViewSwitcher.switchTo(View.FileManager);
    }

    /**
     * Switch to Screen Rule
     */
    public void onRules(){
        ViewSwitcher.switchTo(View.Rules);
    }

    /**
     * Switch to Screen Settings
     */
    public void onSettings(){
        ViewSwitcher.switchTo(View.Settings);
    }

    /**
     * Close Game
     */
    public void onExit(){
        Platform.exit();
    }
}
