package gui.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.util.HashMap;
import java.util.Map;

public class ViewSwitcher {
    private static Map<View, Parent> cache = new HashMap<>();
    private static Scene scene;

    public static void setScene(Scene scene){
        ViewSwitcher.scene = scene;
    }

    public static void switchTo(View view)  {

        if(scene == null){
            System.out.println("No Scene was set");
            return;
        }

        try {
            Parent root;
            if(cache.containsKey(view)){
                root = cache.get(view);
                //System.out.println("Loading from cache");
            } else {
                if(view == View.Lobby || view == View.Game){
                    root = FXMLLoader.load(ViewSwitcher.class.getResource(view.getFileName()));
                } else {
                    root = FXMLLoader.load(ViewSwitcher.class.getResource(view.getFileName()));
                    cache.put(view, root);
                    //System.out.println("Loading from FXML");
                }
            }
            scene.setRoot(root);
        } catch (Exception e){
            e.getMessage();
        }
    }

    public static Scene getScene() {
        return scene;
    }
}
