package gui.controllers;

import gui.Util;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import static gui.Util.log_stderr;

/**
 * @author Philip
 * This class handle Audio output for misic and sfx
 */
public class AudioPlayer {

    public static boolean disableSound = false;

    private static double sfxVolume = 0.5;
    private static double masterVolume = 0.5;
    private static double musicVolume = 0.5;

    private static MediaPlayer mediaPlayerMusic = null;

    private static MediaPlayer mediaPlayerSFX = null;

    private static ControllerSettings cs = ControllerSettings.getInstance();

    /**
     * Play SFX
     * @param audio File
     */
    public static void playSFX(Audio audio){
        if (disableSound)
            return;

        try {
            URL url = AudioPlayer.class.getResource(audio.getPathName());

            mediaPlayerMusic = new MediaPlayer(new Media(url.toURI().toString()));
            mediaPlayerMusic.volumeProperty().bindBidirectional(cs.getSliderVolume().valueProperty());
            mediaPlayerMusic.play();
        } catch (Exception e) {
            log_stderr(e.getMessage());
        }
    }

    /**
     * Play Music
     * @param audio File
     */
    public static void playMusic(Audio audio){
        if (disableSound)
            return;
        try {
            URL url = AudioPlayer.class.getResource(audio.getPathName());

            mediaPlayerMusic = new MediaPlayer(new Media(url.toURI().toString()));
            mediaPlayerMusic.volumeProperty().bindBidirectional(cs.getSliderVolume().valueProperty());
            mediaPlayerMusic.play();
        } catch (Exception e) {
            log_stderr(e.getMessage());
        }    }

    /**
     * PLay random Music
     * @param i slot
     */
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
        try {
            //File file = new File(audio.getPathName());
            URL url = AudioPlayer.class.getResource(audio.getPathName());

            mediaPlayerMusic = new MediaPlayer(new Media(url.toURI().toString()));
            mediaPlayerMusic.volumeProperty().bindBidirectional(cs.getSliderVolume().valueProperty());
            mediaPlayerMusic.play();
        } catch (Exception e) {
            log_stderr(e.getMessage());
        }
    }

    /**
     * Destroy MediaPlayer
     */
    public static void destroyMusic(){
        if(mediaPlayerMusic != null){
            mediaPlayerMusic.stop();
            mediaPlayerMusic.dispose();
        } else {
            Util.log_debug("mediaPLayer is null");
        }
    }
}