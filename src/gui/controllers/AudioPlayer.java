package gui.controllers;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class AudioPlayer {

  private static double sfxVolume;
  private static double masterVolume;
  private static double musicVolume;

    public static void playAudio(Audio audio){
        Media sound = new Media(new File(audio.getPathName()).toURI().toString());
        MediaPlayer player = new MediaPlayer(sound);
        player.play();
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
