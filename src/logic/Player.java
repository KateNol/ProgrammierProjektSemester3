package logic;

import Logic_demo.Logic;

/**
 * abstract player class
 * this should be extended by NetworkPlayer, GUIPlayer, AI
 * this class should contain method declarations for all players,
 * logic needs to call these methods to interact with the players
 */
public abstract class Player {
    public void setLogic(Logic logic) {
    }
}
