package logic;

import java.util.Observable;
import java.util.Observer;

public abstract class Player extends Observable {
    public abstract boolean getIsConnected();

    public abstract int getCommonSemester();

    public void send(ShotResult hit) {
    }
}
