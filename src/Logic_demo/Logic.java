package Logic_demo;

import java.util.ArrayList;
import java.util.List;

/**
 * logic base class
 * this class should contain a thread monitoring the game state.
 * when logic determines it is players turn, it should ask the player for a move
 * when enemy sends a move to player, logic should check that move and respond accordingly
 */
public class Logic { //Logic is subject, Player is observer
    //private logic.Player player;
    //private logic.Player enemy;
    private int level = 1;
    //private List<Observer> observerList;
    private List<Player> playersList = new ArrayList<Player>();

    public Logic() {

    }

    public Logic(Player player, Player enemy) throws NullPointerException {
        if(player == null || enemy == null) { throw new NullPointerException("One of the players is null!");}
        playersList.add(player);
        playersList.add(enemy);
        player.setLogic(this);
        //register((Observer) player);
        //register((Observer) enemy);
    }

    public void addPlayer(Player p) throws NullPointerException {
        if(p == null) { throw new NullPointerException("Not able to add player!");}
        playersList.add(p);
    }

    public void deletePlayer(Player p) {
        if(playersList.contains(p)) {
            playersList.remove(p);
        }
        //TODO what if player is not in playersList?
    }

    /**
     *
     */
    private void turn(int playerIndex, int enemyIndex) {
        Coordinate c = playersList.get(playerIndex).getInput();
        MapState ms = playersList.get(enemyIndex).updateMap(c);
        playersList.get(playerIndex).updateMapState(c, ms);
    }

    public void notifyPlayer() {
    }

    public Player getUpdate(Player p) {
        return null;
    }
}
