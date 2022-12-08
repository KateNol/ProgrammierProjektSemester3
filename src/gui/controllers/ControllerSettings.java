package gui.controllers;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

import java.net.URL;
import java.util.ResourceBundle;


public class ControllerSettings implements Initializable {

    private static ControllerSettings instance = null;

    private final String fullHd = "1920 x 1080";
    private final String hd = "1280 x 720";
    private final String full = "Full Screen";
    private final String window = "Window Screen";

    @FXML
    private ComboBox<String> windowMode;
    @FXML
    private ComboBox<String> resolution;

    @FXML
    private Slider sliderMaster, sliderSFX, sliderVolume;
    @FXML
    private Label labelMaster, labelMusic, labelSFX;

    public static ControllerSettings getInstance(){
        return instance;
    }

    public Slider getSliderMaster() {
        return sliderMaster;
    }

    public Slider getSliderSFX() {
        return sliderSFX;
    }

    public Slider getSliderVolume() {
        return sliderVolume;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instance= this;
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
            double d = (double) newValue;
            int volume = (int) (Math.round(d * Math.pow(10, 2)));
            labelSFX.setText(Integer.toString(volume));
            //AudioPlayer.setSFXVolume((double) volume/100);
        });

        sliderMaster.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            double d = (double) newValue;
            int volume = (int) (Math.round(d * Math.pow(10, 2)));
            labelMaster.setText(Integer.toString(volume));
            //AudioPlayer.setMasterVolume((double) volume/100);

        });

        sliderVolume.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            double d = (double) newValue;
            int volume = (int) (Math.round(d * Math.pow(10, 2)));
            labelMusic.setText(Integer.toString(volume));
            //AudioPlayer.setMasterVolume((double) volume/100);
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
