package gui;

import gui.controllers.GUIPlayer;
import internal.LocalEnemyMode;
import internal.NetworkMode;
import internal.PlayerMode;
import internal.ServerMode;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import logic.Logic;
import logic.Player;
import network.debug.ConsolePlayer;

import java.io.IOException;

/**
 * main gui entry point
 * also creates the logic/player instances
 */
public class GUIMain extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    private Parent root;
    private Scene scene;
    private Stage stage;

    private static GUIMain instance = null;
    private static final String ICON_PATH = "/gui/img/Icon.png";

    @Override
    public void start(Stage stage) throws IOException {
        instance = this;

        root = FXMLLoader.load(getClass().getResource("/gui/fxml/Menu.fxml"));
        scene = new Scene(root);

        this.stage = stage;
        this.stage.setTitle("Game Select");
        this.stage.setScene(scene);
        System.out.println("fuck");

        //Set icon and tile
        Image icon = new Image(ICON_PATH);
        stage.getIcons().add(icon);
        stage.setTitle("Battleship");

        //Start application
        this.stage.show();
    }

    /**
     * this method gets called when the user presses "start" in the first gui
     * here we now all necessary info to create logic/player instances
     *
     * @param playerMode
     * @param networkMode
     * @param serverMode
     * @param localEnemyMode
     * @param address
     * @param port
     * @throws IOException
     */
    public void changeSceneToGame(PlayerMode playerMode, NetworkMode networkMode, ServerMode serverMode, LocalEnemyMode localEnemyMode, String address, String port) throws IOException {
        root = FXMLLoader.load(getClass().getResource("rest/game.fxml"));
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
        Player enemy = new ConsolePlayer(network.ServerMode.CLIENT);

        Logic playerLogic = new Logic(player, enemy);
        Logic enemyLogic = new Logic(enemy, player);
    }

    public static GUIMain getInstance() {
        return instance;
    }


}
