package gui.controllers;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class AudioPlayer {

  private static double sfxVolume = 0.5;
  private static double masterVolume = 0.5;
  private static double musicVolume = 0.5;

  private static ControllerSettings cs = ControllerSettings.getInstance();

    public static void playSFX(Audio audio){
        MediaPlayer mediaPlayer = new MediaPlayer(new Media(new File(audio.getPathName()).toURI().toString()));
        mediaPlayer.volumeProperty().bindBidirectional(cs.getSliderSFX().valueProperty());
        mediaPlayer.play();
    }

    public static void playMusic(Audio audio){
        MediaPlayer mediaPlayer = new MediaPlayer(new Media(new File(audio.getPathName()).toURI().toString()));
        mediaPlayer.volumeProperty().bindBidirectional(cs.getSliderVolume().valueProperty());
        mediaPlayer.play();
    }

    public static void setSFXVolume(double sfxVolume){
        AudioPlayer.sfxVolume = sfxVolume;
    }

    public static void setMasterVolume(double masterVolume){
        AudioPlayer.masterVolume = masterVolume;
    }

    public static void setMusicVolume(double musicVolume){
        AudioPlayer.musicVolume = musicVolume;
    }
}