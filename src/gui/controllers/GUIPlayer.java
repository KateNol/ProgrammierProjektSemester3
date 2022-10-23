package gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import logic.Logic;
import logic.Player;
import network.NetworkMode;
import network.NetworkPlayer;
import network.debug.ConsolePlayer;

import java.io.IOException;

public class GUIPlayer extends Player {
    // singleton because I don't know how to change scenes otherwise
    private static GUIPlayer instance;

    private NetworkPlayer networkPlayer;

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

    public void onFire(ActionEvent actionEvent) {
        networkPlayer.sendMessage("Hello from GUI");
    }

}
