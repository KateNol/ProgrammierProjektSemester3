package gui.controllers;

import javafx.scene.image.Image;
import javafx.scene.layout.*;

public class Settings {

    public static Background setBackgroundImage(String path) {
        BackgroundSize backgroundSize = new BackgroundSize(1, 1, true, true, false, false);
        BackgroundImage backgroundImage = new BackgroundImage((new Image(path)), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, backgroundSize);
        return new Background(backgroundImage);
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
