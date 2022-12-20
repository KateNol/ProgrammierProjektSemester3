package gui;

import gui.controllers.AudioPlayer;
import gui.controllers.View;
import gui.controllers.ViewSwitcher;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * main gui entry point
 * also creates the logic/player instances
 * <p>
 * to use javafx, add jvm args
 * --module-path lib/lib --add-modules javafx.controls,javafx.fxml,javafx.media
 */
public class GUIMain extends Application  {

    public static final String GITLAB_URL = "https://lab.it.hs-hannover.de/f4-informatik/prgprj/programmier-projekte-ws22-23/gruppe07";
    private static final String ICON_PATH = "file:src/gui/img/Icon.png";

    public static void main(String[] args) {
        for (String arg : args) {
            if (arg.startsWith("nosound")) {
                AudioPlayer.disableSound = true;
            }
        }
        launch(args);
    }

    private static HostServices hostServices = null;

    @Override
    public void start(Stage stage) throws IOException {
        stage.setAlwaysOnTop(true);
        stage.setOnCloseRequest(t -> {
            if (GUIPlayer.getInstance() != null)
                GUIPlayer.getInstance().destroy();
            Platform.exit();
            System.exit(0);
        });
        ViewSwitcher.setStage(stage);
        Scene scene = new Scene(new Pane());
        //Load Scene
        ViewSwitcher.setCache(View.Settings);
        ViewSwitcher.setScene(scene);
        ViewSwitcher.switchTo(View.Menu);

        stage.setTitle("Game Select");
        stage.setScene(scene);

        //Set icon and tile
        Image icon = new Image(ICON_PATH);
        stage.getIcons().add(icon);
        stage.setTitle("Battleship");

        //stage Property's
        stage.setMinWidth(1280);
        stage.setMinHeight(760);

        //Start application
        stage.show();

        hostServices = getHostServices();
    }

    public static HostServices getFXHostServices() {
        return hostServices;
    }

}
