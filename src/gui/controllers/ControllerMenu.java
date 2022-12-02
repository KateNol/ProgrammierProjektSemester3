package gui.controllers;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for Menu Screen
 */
public class ControllerMenu implements Initializable {

    @FXML
    private VBox buttonBox;
    Stage stage = ViewSwitcher.getStage();
    private double spacing = 50;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stage.heightProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal.intValue() >= 800 && newVal.intValue() <= 899){
                buttonBox.setSpacing(spacing + 10);
            } else if(newVal.intValue() >= 900 && newVal.intValue() <= 999){
                buttonBox.setSpacing(spacing + 20);
            } else if(newVal.intValue() >= 1000 && newVal.intValue() <= 1080){
                buttonBox.setSpacing(spacing + 30);
            } else {
                buttonBox.setSpacing(spacing);
            }
        });
    }

    /**
     * Switch to Screen Load Game File Manager
     */
    public void onStartGame(){
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
