package logic;

import java.util.Observable;
import java.util.Random;

public abstract class Player extends Observable {
    private String username;
    private int maxSemester;

    private int[] shipSizes;
    private int mapSize;
    private boolean globalConfigLoaded;

    private GlobalConfig globalConfig;

    public Player(PlayerConfig playerConfig, GlobalConfig globalConfig) {
        if (playerConfig != null && globalConfig != null) {
            maxSemester = playerConfig.getMaxSemester();
            username = playerConfig.getUsername();
        }

        this.globalConfig = globalConfig;
        globalConfigLoaded = false;
    }

    public void loadGlobalConfig() {
        shipSizes = globalConfig.getShipSizes(getCommonSemester());
        mapSize = globalConfig.getMapSize(getCommonSemester());

        globalConfigLoaded = true;
    }

    /**
     * returns true when both players are connected
     * implemented by NetworkPlayer
     *
     * @return
     */
    public abstract boolean getIsConnected();

    /**
     * returns the highest semester both players can play in
     * a call to this is only valid if getIsConnected() returns true
     * @return
     */
    public abstract int getCommonSemester();

    /**
     * gets our username
     * implemented by NetworkPlayer
     * @return
     */
    public abstract String getUsername();

    /**
     * requests a coordinate from the player
     * has to be implemented by GUIPlayer/AIPlayer/ConsolePlayer
     * @return the point where your player wants to shoot
     */
    public abstract Coordinate getShot();

    /**
     * asks the player to send our shot to the enemy player
     * implemented by NetworkPlayer
     * @param coordinate
     */
    public abstract void sendShot(Coordinate coordinate);

    /**
     * call when we have evaluated a shot from the enemy (received by notify())
     * sends the result to the enemy shot back to the enemy
     * implemented by NetworkPlayer
     * @param shotResult
     */
    public void sendShotResponse(ShotResult shotResult) {
        // Ewpojwpoegj
    }

    /**
     * this method will be called by Logic when a shot was received via notify()
     * may be overwritten by GUIPlayer/AIPlayer/ConsolePlayer
     * @param shot
     * @return
     */
    public ShotResult receiveShot(Coordinate shot) {
        // TODO look up actual result in map
        Random random = new Random();
        return random.nextBoolean() ? ShotResult.HIT : ShotResult.MISS;
    }
}
