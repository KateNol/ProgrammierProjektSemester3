package gui.controllers;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class AudioPlayer {

    public static void playIntro(){
        System.out.println(Audio.Intro.getPathName());
        Media sound = new Media(new File(Audio.Intro.getPathName()).toURI().toString());
        MediaPlayer player = new MediaPlayer(sound);
        player.play();
    }
}
