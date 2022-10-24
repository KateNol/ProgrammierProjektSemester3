package logic;

/**
 * logic base class
 * this class should contain a thread monitoring the game state.
 * when logic determines it is players turn, it should ask the player for a move
 * when enemy sends a move to player, logic should check that move and respond accordingly
 */
public class Logic {
    Player player;
    Player enemy;

    public Logic(Player player, Player enemy) {
        this.player = player;
        this.enemy = enemy;
    }
}
