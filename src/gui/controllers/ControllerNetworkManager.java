package gui.controllers;

public class ControllerNetworkManager {

    public void onSinglePlayer(){
        ViewSwitcher.switchTo(View.Lobby);
    }

    public void onReturn(){
        ViewSwitcher.switchTo(View.FileManager);
    }

    public void onMultiPlayer(){

    }
/*
    public void onMultiPlayer(PlayerMode playerMode, NetworkMode networkMode, ServerMode serverMode, LocalEnemyMode localEnemyMode, String address, String port) throws IOException {

        //at this point we create the actual players and logic instances,
        //here we could switch over all the parameters and instantiate the correct players,
       // but for now we just use a gui player and a console player
       // note: if the enemy is a networkplayer, we don't need another logic instance for him (?)

        Player player = GUIPlayer.getInstance();
        Player enemy = new ConsolePlayer(null, null, network.ServerMode.CLIENT);

        Logic playerLogic = new Logic(player, enemy);
        Logic enemyLogic = new Logic(enemy, player);
    }
*/
}
