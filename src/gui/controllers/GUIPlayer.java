package gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import logic.Logic;
import logic.Player;
import network.NetworkMode;
import network.NetworkPlayer;
import network.debug.ConsolePlayer;

import java.io.IOException;

/**
 * controller class for "player" gui
 * javafx controller class, reacts to gui input
 * this class cant extend the network player directly because we cant invoke its ctor directly
 */
public class GUIPlayer extends Player {
    // singleton because I don't know how to change scenes otherwise
    private static GUIPlayer instance;

    private NetworkPlayer networkPlayer;

    /**
     * this method gets called just after the ctor, so we use this to build the player
     *
     * @throws IOException
     */
    @FXML
    private void initialize() throws IOException {
        instance = this;
        networkPlayer = new NetworkPlayer(NetworkMode.SERVER) {
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
