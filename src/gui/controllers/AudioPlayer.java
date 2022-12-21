package gui.controllers;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class AudioPlayer {

    public static boolean disableSound = false;

    private static double sfxVolume = 0.5;
    private static double masterVolume = 0.5;
    private static double musicVolume = 0.5;

    private static MediaPlayer mediaPlayerMusic = null;

    private static MediaPlayer mediaPlayerSFX = null;

    private static ControllerSettings cs = ControllerSettings.getInstance();

    public static void playSFX(Audio audio){
        if (disableSound)
            return;
        mediaPlayerSFX = new MediaPlayer(new Media(new File(audio.getPathName()).toURI().toString()));
        mediaPlayerSFX.volumeProperty().bindBidirectional(cs.getSliderSFX().valueProperty());
        mediaPlayerSFX.play();
    }

    public static void playMusic(Audio audio){
        if (disableSound)
            return;
        mediaPlayerMusic = new MediaPlayer(new Media(new File(audio.getPathName()).toURI().toString()));
        mediaPlayerMusic.volumeProperty().bindBidirectional(cs.getSliderVolume().valueProperty());
        mediaPlayerMusic.play();
    }

    public static void playMusic(int i){
        Audio audio = null;
        switch (i){
            case 1 -> audio = Audio.BattleMusic1;
            case 2 -> audio = Audio.BattleMusic2;
            case 3 -> audio = Audio.BattleMusic3;
            case 4 -> audio = Audio.BattleMusic4;
            case 5 -> audio = Audio.BattleMusic5;
        }
        if (disableSound)
            return;
        mediaPlayerMusic = new MediaPlayer(new Media(new File(audio.getPathName()).toURI().toString()));
        mediaPlayerMusic.volumeProperty().bindBidirectional(cs.getSliderVolume().valueProperty());
        mediaPlayerMusic.play();
    }

    public static void destroyMusic(){
        if(mediaPlayerMusic != null){
            mediaPlayerMusic.stop();
            mediaPlayerMusic.dispose();
        }
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