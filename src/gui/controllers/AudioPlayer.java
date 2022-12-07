package gui.controllers;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class AudioPlayer {

  private static double sfxVolume = 1.0;
  private static double masterVolume = 1.0;
  private static double musicVolume = 1.0;

    public static void playSFX(Audio audio){
        Media sound = new Media(new File(audio.getPathName()).toURI().toString());
        MediaPlayer player = new MediaPlayer(sound);
        player.setVolume(sfxVolume * masterVolume);
        player.play();
    }

    public static void playMusic(Audio audio){
        Media sound = new Media(new File(audio.getPathName()).toURI().toString());
        MediaPlayer player = new MediaPlayer(sound);
        player.setVolume(musicVolume * masterVolume);
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
