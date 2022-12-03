package gui.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.stage.Stage;

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

        try {
            Parent root;
            if (cache.containsKey(view)) {
                root = cache.get(view);
                //System.out.println("Loading from cache");
            } else {
                //Filer Lobby and Game Scene
                if (view == View.Lobby || view == View.Game) {
                    root = FXMLLoader.load(Objects.requireNonNull(ViewSwitcher.class.getResource(view.getFileName())));
                } else {
                    root = FXMLLoader.load(Objects.requireNonNull(ViewSwitcher.class.getResource(view.getFileName())));
                    cache.put(view, root);
                    //System.out.println("Loading from FXML");
                }
            }
            scene.setRoot(root);
            lastView = view;
        } catch (Exception e){
            log_debug("failed to Switch to " + view.toString());
        }
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
