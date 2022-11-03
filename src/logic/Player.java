package logic;

import java.util.Observable;
import java.util.Observer;

public abstract class Player extends Observable {
    public abstract boolean getIsConnected();

    public abstract int getCommonSemester();

    public abstract void send(ShotResult hit);

    public abstract String getUsername();

    public abstract void getShot();
}
