package logic;

import java.util.ArrayList;
import java.util.List;

/**
 * logic base class
 * this class should contain a thread monitoring the game state.
 * when logic determines it is players turn, it should ask the player for a move
 * when enemy sends a move to player, logic should check that move and respond accordingly
 */
public class Logic { //Logic is subject, Player is observer
    private int level = 1;
    private List<Player> playersList = new ArrayList<Player>();

    /**
     * you can construct the Logic with just one player and add the other later
     */
    public Logic(Player player) throws NullPointerException {
        if (player == null) {
            throw new NullPointerException("Player is null!");
        }
        playersList.add(player);
    }

    /**
     * Adds players to itself for gamemanagement and adds itself to the players.
     *
     * @param player Player 1, mainly local Player
     * @param enemy  Player 2, mainly network player or AI
     * @throws NullPointerException if one of the players is null
     */
    public Logic(Player player, Player enemy) throws NullPointerException {
        if (player == null || enemy == null) {
            throw new NullPointerException("One of the players is null!");
        }
        playersList.add(player);
        playersList.add(enemy);
        player.setLogic(this);
        enemy.setLogic(this);
    }

    /**
     * add a player to the logic and set itself to the player
     *
     * @param p Player
     * @throws NullPointerException if player is null
     */
    public void addPlayer(Player p) throws NullPointerException, IndexOutOfBoundsException {
        if (p == null) {
            throw new NullPointerException("Not able to add player!");
        }
        if (playersList.size() == 2) { throw new IndexOutOfBoundsException("Can't add more than two Players"); }
        p.setLogic(this);
        playersList.add(p);
    }

    /**
     * delete player from the list
     *
     * @param p Player
     */
    public void deletePlayer(Player p) {
        if (playersList.contains(p)) {
            playersList.remove(p);
        }
        //TODO what if player is not in playersList?
    }

    /**
     * procedure of one turn
     */
    private void turn(int playerIndex, int enemyIndex) {
        Coordinate c = playersList.get(playerIndex).getInput(); // wait for the player x to take its shot
        MapState ms = playersList.get(enemyIndex).updateMap(c); // hand over the shotposition to player y and get result
        playersList.get(playerIndex).updateMapState(c, ms); // hand over shotresult to player x
    }

    /**
     * don't know but keep it maybe for later
     */
    public void notifyPlayer() {
    }

    /**
     * gets called if there are no ships left for one player
     *
     * @param p Player
     */
    public void gameOver(Player p) {
        //TODO implement, perhaps let the GUI know to return to the home screen
        playersList.remove(1);
    }
}
