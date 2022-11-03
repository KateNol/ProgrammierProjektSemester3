package logic;

import java.util.Observable;

public abstract class Player extends Observable {
    public abstract boolean getIsConnected();

    public abstract int getCommonSemester();

    public abstract String getUsername();

    public abstract Coordinate getShot();

    public abstract void sendShot(Coordinate coordinate);

    public abstract void sendShotResponse(ShotResult shotResult);
}
