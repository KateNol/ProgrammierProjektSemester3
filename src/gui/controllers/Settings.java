package gui.controllers;

import javafx.scene.image.Image;
import javafx.scene.layout.*;

public class Settings {

    public static Background setBackgroundImage(String path){
        BackgroundSize backgroundSize = new BackgroundSize(1, 1, true, true, false, false);
        BackgroundImage backgroundImage = new BackgroundImage((new Image(path)), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,backgroundSize);
        return new Background(backgroundImage);
    }
}
