package logic;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;

public abstract class Player extends Observable {

    private final ArrayList<Ship> ships = null; // List of ships the player has
    private final Map myMap = null; // own map, that contains the state of the ships and the shots the enemy took
    private final Map enemyMap = null; // enemy map, contains information about whether the shot was a hit or miss

    public abstract boolean getIsConnected();

    public abstract int getCommonSemester();

    public abstract String getUsername();

    public abstract Coordinate getShot();

    public abstract void sendShot(Coordinate coordinate);

    public abstract void sendShotResponse(ShotResult shotResult);

    public ShotResult receiveShot(Coordinate shot) {
        // TODO look up actual result in map
        Random random = new Random();
        return random.nextBoolean() ? ShotResult.HIT : ShotResult.MISS;
    }
}
