package gui.controllers;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

import java.net.URL;
import java.util.ResourceBundle;


public class ControllerSettings implements Initializable {


    private final String fullHd = "1920 x 1080";

    private final String hd = "1280 x 720";

    private final String full = "Full Screen";

    private final String window = "Window Screen";

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
        String [] hdMode = {fullHd, hd};
        resolution.getItems().addAll(hdMode);
        resolution.setPromptText("HD Mode");


        String [] screenMode = {full, window};
        windowMode.getItems().addAll(screenMode);
        windowMode.setPromptText("Choose Screen Mode");

        resolution.getSelectionModel().selectedItemProperty().addListener((observableValue, s1, s2) -> {
            if(s2.equals(fullHd)){
                Settings.setFullHDScreen();
            } else if (s2.equals(hd)) {
                Settings.setHDScreen();
            }
        });



        windowMode.getSelectionModel().selectedItemProperty().addListener((observableValue, s1, s2) -> {
            if(s2.equals(full)){
                ViewSwitcher.getStage().setFullScreen(true);
            } else if (s2.equals(window)) {
                ViewSwitcher.getStage().setFullScreen(false);
            }
        });

        sliderSFX.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            double d = Math.round((double) newValue);
            labelSFX.setText(Double.toString(d));
            AudioPlayer.setSFXVolume(d);
        });

        sliderMaster.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            double d = Math.round((double) newValue);
            labelMaster.setText(Double.toString(d));
            AudioPlayer.setMasterVolume(d);

        });

        sliderVolume.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            double d = Math.round((double) newValue);
            labelMusic.setText(Double.toString(d));
            AudioPlayer.setMusicVolume(d);
        });


    }

    /**
     * Return to Scene Menu
     */
    public void onReturn(){
        AudioPlayer.playSFX(Audio.Click);
        ViewSwitcher.switchTo(View.Menu);
    }
}
