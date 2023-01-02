package gui.controllers;

import gui.GUIPlayer;
import gui.Util;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static gui.Util.log_debug;

public class ViewSwitcher {

    //Caching for Screens
    private static final Map<View, Parent> cache = new HashMap<>();
    private static Scene scene;
    private static Stage stage;
    private static View lastView = null;

    /**
     * Set scene
     * @param scene scene you want to set
     */
    public static void setScene(Scene scene) {
        ViewSwitcher.scene = scene;
    }

    /**
     * set application stage
     * @param stage application stage
     */
    public static void setStage(Stage stage){
        ViewSwitcher.stage = stage;
    }

    /**
     * Manage switching between scenes
     * @param view enum from View
     */
    public static void switchTo(View view) {
        if (scene == null) {
            System.out.println("No Scene was set");
            return;
        }

        if (lastView != null)
            log_debug("switching view from " + lastView.name() + " to " + view.name());

        // if switching from a game view to a non-game view, destroy the player
        if ((lastView == View.NetworkManager || lastView == View.Lobby || lastView == View.Game)
            && (view == View.Menu || view == View.Rules || view == View.Settings || view == View.FileManager)) {
            GUIPlayer.getInstance().destroy();
        }

        try {
            Parent root;
            if (cache.containsKey(view)) {
                root = cache.get(view);
                Util.log_debug("Loading from cache");
            } else {
                FXMLLoader loader = new FXMLLoader();
                log_debug(String.valueOf(loader.getLocation()));
                loader.setLocation(ViewSwitcher.class.getResource(view.getFileName()));
                log_debug(String.valueOf(loader.getLocation()));

                //Filer Lobby and Game Scene
                if (view == View.Lobby || view == View.Game) {
                    root = loader.load();
                } else {
                    root = loader.load();
                    cache.put(view, root);
                    Util.log_debug("Loading from FXML");
                }
            }
            scene.setRoot(root);
            lastView = view;
        } catch (Exception e){
            log_debug("failed to Switch to " + view.toString());
            log_debug(e.getMessage());
        }
    }

    public static void setCache(View view) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        log_debug(String.valueOf(loader.getLocation()));
        loader.setLocation(ViewSwitcher.class.getResource(view.getFileName()));
        log_debug(String.valueOf(loader.getLocation()));

        Parent root = loader.load();
        cache.put(view, root);
    }

    /**
     * Get scene
     * @return scene
     */
    public static Scene getScene() {
        return scene;
    }

    public static Stage getStage() {
        return stage;
    }
}
