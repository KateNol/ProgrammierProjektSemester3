package gui.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
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
        //Set Background
        BackgroundSize backgroundSize = new BackgroundSize(1, 1, true, true, false, false);
        BackgroundImage backgroundImage = new BackgroundImage((new Image("file:src/gui/img/Menu_Background.jpg")), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,backgroundSize);
        background.setBackground(new Background(backgroundImage));
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
