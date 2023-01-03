package gui.controllers;

import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.awt.*;
import java.io.InputStream;
import java.net.URL;
import java.util.Set;

import static gui.Util.log_stderr;

/**
 * @author Stefan/ Philip
 * This class is setting global changes
 */
public class Settings {

    public static int mode = 1;

    /**
     * Set Background
     * @param path from picture
     * @return background
     */
    public static Background setBackgroundImage(String path) {
        BackgroundSize backgroundSize = new BackgroundSize(1, 1, true, true, false, false);

        InputStream inputStream = Settings.class.getResourceAsStream(path);
        if (inputStream == null) {
            return null;
        }

        Image image = new Image(inputStream);

        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, backgroundSize);
        return new Background(backgroundImage);
    }

    /**
     * sets stage to full window size
     */
    public  static void setFullHDScreen() {
        ViewSwitcher.getStage().setWidth(1920);
        ViewSwitcher.getStage().setHeight(1080);
        mode = 2;
    }

    /**
     * sets stage to size  given as parameter
     */
    public static void setHDScreen() {
        mode = 1;
        ViewSwitcher.getStage().setWidth(1280);
        ViewSwitcher.getStage().setHeight(720);
    }

    /**
     * Resize tiles by maxSemester
     * @param tileSize TileSize in Pixel
     */
    public static int setTileSize(int tileSize) {
        int i = 20;
        if(mode == 1){
            switch (tileSize){
                case 1 ->  i = 29;
                case 2 -> i = 26;
                case 3 -> i = 25;
                case 4 -> i = 24;
                case 5 -> i = 23;
                case 6 -> i = 22;
            }
        } else {
            switch (tileSize){
                case 1 ->  i = 41;
                case 2 -> i = 40;
                case 3 -> i = 39;
                case 4 -> i = 38;
                case 5 -> i = 37;
                case 6 -> i = 36;
            }
        }
        return i;
    }

    /**
     * Set mode by fullHD or HD
     * @param mode fullHD or HD
     */
    public static void setMode(int mode) {
        Settings.mode = mode;
    }
}
