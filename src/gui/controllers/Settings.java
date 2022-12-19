package gui.controllers;

import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.File;
import java.net.MalformedURLException;

public class Settings {

    public static MediaPlayer mediaPlayer = null;

    public static Background setBackgroundImage(String path) {
        BackgroundSize backgroundSize = new BackgroundSize(1, 1, true, true, false, false);
        BackgroundImage backgroundImage = new BackgroundImage((new Image(path)), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, backgroundSize);
        return new Background(backgroundImage);
    }

    public static MediaView setEsterEgg() {
        File mediaFile = new File("src/gui/video/Video.mp4");
        Media media = null;
        try {
            media = new Media(mediaFile.toURI().toURL().toString());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        mediaPlayer = new MediaPlayer(media);

        MediaView mediaView = new MediaView(mediaPlayer);
        mediaPlayer.play();
        return mediaView;
    }

    /**
     * sets stage to full window size
     */
    public  static void setFullHDScreen() {
        ViewSwitcher.getStage().setWidth(1920);
        ViewSwitcher.getStage().setHeight(1080);
    }

    /**
     * sets stage to size  given as parameter
     */
    public static void setHDScreen() {
        ViewSwitcher.getStage().setWidth(1280);
        ViewSwitcher.getStage().setHeight(720);
    }
}
