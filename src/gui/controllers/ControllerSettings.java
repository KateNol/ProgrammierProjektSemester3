package gui.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import network.ServerMode;

import java.net.URL;
import java.util.ResourceBundle;


public class ControllerSettings implements Initializable {


    private String full = "1920 x 1080";

    private String norm = " 1280 x 720";

    @FXML
    private Label labelMaster;

    @FXML
    private Label labelMusic;

    @FXML
    private Label labelSFX;

    @FXML
    private ComboBox<String> resolution;

    @FXML
    private Slider sliderMaster;

    @FXML
    private Slider sliderSFX;

    @FXML
    private Slider sliderVolume;

    @FXML
    private ComboBox<String> windowMode;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String [] screenMode = {"full screen", "normal screen"};
        resolution.getItems().addAll(screenMode);
        windowMode.setPromptText("Choose Screen Mode");

        windowMode.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s1, String s2) {
                if(s2.equals(full)){
                    Settings.setFullHDScreen();
                } else if (s2.equals(norm)) {
                    Settings.setHDScreen();

                }

            }
        });

    }

    /**
     * Return to Scene Menu
     */
    public void onReturn(){
        ViewSwitcher.switchTo(View.Menu);
    }
}
