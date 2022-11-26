package gui.controllers;

import gui.GUIPlayer;
import internal.LocalEnemyMode;
import internal.NetworkMode;
import internal.PlayerMode;
import logic.Logic;
import logic.Player;
import network.ServerMode;
import network.debug.ConsolePlayer;
import network.debug.Driver;

import java.io.IOException;

import static network.internal.Util.*;

/**
 * Controller for Network Manager
 */
public class ControllerNetworkManager {

    /**
     * Return to File Manager
     */
    public void onReturn(){
        ViewSwitcher.switchTo(View.FileManager);
    }

    /**
     * Choose SinglePlayer Mode
     */
    public void onSinglePlayer() throws IOException {
        ViewSwitcher.switchTo(View.Lobby);

        LocalEnemyMode localEnemyMode = LocalEnemyMode.COMPUTER;
        Thread enemyThread = new Thread(() -> {
            String[] new_args = new String[]{"player=ai", "server=client", "network=offline"};
            try {
                Driver.main(new_args);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        enemyThread.setDaemon(false);
        enemyThread.setName("Enemy Thread");
        enemyThread.start();


        GUIPlayer.getInstance().establishConnection(ServerMode.SERVER);
    }

    /**
     * Choose MultiPlayer Mode
     */
    public void onMultiPlayer() throws IOException {
        // Dialog Window to enter informations
        ServerMode serverMode = ServerMode.SERVER;
        String address = defaultAddress;
        int port = defaultPort;

        GUIPlayer.getInstance().establishConnection(serverMode, address, port);
    }
}
