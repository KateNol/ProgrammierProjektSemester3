package gui;

import gui.controllers.View;
import gui.controllers.ViewSwitcher;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.GlobalConfig;
import logic.PlayerConfig;

import java.io.IOException;


/**
 * main gui entry point
 * also creates the logic/player instances
 */
public class GUIMain extends Application  {
    public static void main(String[] args) {
        launch(args);
    }

    private Scene scene;
    private static final String ICON_PATH = "file:src/gui/img/Icon.png";

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(new Pane());
        //Load Scene
        ViewSwitcher.setScene(scene);
        ViewSwitcher.switchTo(View.Menu);

        stage.setTitle("Game Select");
        stage.setScene(scene);

        //Set icon and tile
        Image icon = new Image(ICON_PATH);
        stage.getIcons().add(icon);
        stage.setTitle("Battleship");

        stage.setFullScreen(true);

        //Start application
        stage.show();
    }

}
