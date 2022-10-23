package gui;

import internal.LocalEnemyMode;
import internal.NetworkMode;
import internal.PlayerMode;
import internal.ServerMode;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.Player;

import java.awt.event.ActionEvent;
import java.io.IOException;

public class GUIMain extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    private Parent root;
    private Scene scene;
    private Stage stage;

    private static GUIMain instance = null;

    @Override
    public void start(Stage stage) throws IOException {
        instance = this;

        root = FXMLLoader.load(getClass().getResource("fxml/gamemode.fxml"));
        //Parent second = FXMLLoader.load(getClass().getResource("fxml/game.fxml"));

        scene = new Scene(root);

        this.stage = stage;
        this.stage.setTitle("Game Select");
        this.stage.setScene(scene);
        this.stage.show();
    }

    public void changeSceneToGame(PlayerMode playerMode, NetworkMode networkMode, ServerMode serverMode, LocalEnemyMode localEnemyMode, String address, String port) throws IOException {
        root = FXMLLoader.load(getClass().getResource("fxml/game.fxml"));
        scene = new Scene(root);
        stage.setTitle("Game");
        stage.setScene(scene);
        stage.show();


    }

    public static GUIMain getInstance() {
        return instance;
    }


}
