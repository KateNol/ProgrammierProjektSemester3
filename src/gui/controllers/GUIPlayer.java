package gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import logic.Coordinate;
import logic.GlobalConfig;
import logic.Player;
import logic.PlayerConfig;
import network.ServerMode;
import network.NetworkPlayer;

import java.io.IOException;

/**
 * controller class for "player" gui
 * javafx controller class, reacts to gui input
 * this class cant extend the network player directly because we cant invoke its ctor directly
 */
public abstract class GUIPlayer extends Player {
    // singleton because I don't know how to change scenes otherwise
    private static GUIPlayer instance;

    private NetworkPlayer networkPlayer;

    public GUIPlayer(PlayerConfig playerConfig, GlobalConfig globalConfig) {
        super(playerConfig, globalConfig);
    }

    /**
     * this method gets called just after the ctor, so we use this to build the player
     *
     * @throws IOException
     */
    @FXML
    private void initialize() throws IOException {
        instance = this;
        networkPlayer = new NetworkPlayer(null, null, ServerMode.SERVER) {


            /**
             * @return
             */
            @Override
            public Coordinate getShot() {
                return null;
            }

            /**
             * @param msg
             */
            @Override
            public void sendMessage(String msg) {
                super.sendMessage(msg);
            }
        };
    }

    // singleton because I don't know how to change scenes otherwise
    public static GUIPlayer getInstance() {
        return instance;
    }

    /**
     * reacy to "FIRE" button, for now just sends a "hello" message to the other player
     *
     * @param actionEvent
     */
    public void onFire(ActionEvent actionEvent) {
        networkPlayer.sendMessage("Hello from GUI");
    }

}
