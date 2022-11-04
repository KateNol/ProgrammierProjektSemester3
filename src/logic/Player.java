package logic;

import java.util.Observable;
import java.util.Random;

public abstract class Player extends Observable {
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
