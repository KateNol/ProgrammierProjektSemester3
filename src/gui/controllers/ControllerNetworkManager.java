package gui.controllers;

import gui.GUIPlayer;
import internal.LocalEnemyMode;
import internal.NetworkMode;
import internal.PlayerMode;
import logic.Logic;
import logic.Player;
import network.ServerMode;
import network.debug.ConsolePlayer;

import java.io.IOException;

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
    public void onSinglePlayer(){
        ViewSwitcher.switchTo(View.Lobby);
    }

    /**
     * Choose MultiPlayer Mode
     */
    public void onMultiPlayer() {
        //PlayerMode playerMode, NetworkMode networkMode, ServerMode serverMode, LocalEnemyMode localEnemyMode, String address, String port
        //at this point we create the actual players and logic instances,
        //here we could switch over all the parameters and instantiate the correct players,
        // but for now we just use a gui player and a console player
        // note: if the enemy is a networkplayer, we don't need another logic instance for him (?)

        //Player player = GUIPlayer.getInstance();
        //Player enemy = new ConsolePlayer(null, null, network.ServerMode.CLIENT);

        //Logic playerLogic = new Logic(player, enemy);
        //Logic enemyLogic = new Logic(enemy, player);
    }
}
