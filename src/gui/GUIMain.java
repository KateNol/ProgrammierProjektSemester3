package gui;

import gui.controllers.GUIPlayer;
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
import logic.Logic;
import logic.Player;
import network.debug.ConsolePlayer;

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
        stage.setTitle("GUIPlayer");
        stage.setScene(scene);
        stage.show();

        /*
        at this point we create the actual players and logic instances,
        here we could switch over all the parameters and instantiate the correct players,
        but for now we just use a gui player and a console player
        note: if the enemy is a networkplayer, we don't need another logic instance for him (?)
         */
        Player player = GUIPlayer.getInstance();
        Player enemy = new ConsolePlayer(network.NetworkMode.CLIENT);

        Logic playerLogic = new Logic(player, enemy);
        Logic enemyLogic = new Logic(enemy, player);
    }

    public static GUIMain getInstance() {
        return instance;
    }


}
