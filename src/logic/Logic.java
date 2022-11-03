package logic;

import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static logic.Util.log_debug;
import static logic.Util.log_stderr;

public class Logic implements Observer {
    private enum State {
        Start,
        PlayersReady,
        ShipsPlaced,
        GameReady,
        OurTurn,
        EnemyTurn,
        GameOver
    }
    private Player player = null;
    private State state = null;

    private int semester = 0;
    private boolean gameOver = false;
    ShotResult shotResult = null;
    private Lock lock = new ReentrantLock();

    // FIXME: remove this, only used by gui-player
    public Logic(Player p, Player e) {
        log_stderr("do not use this method");
        System.exit(1);
    }

    public Logic(Player player) {
        this.player = player;
        player.addObserver(this);

        state = State.Start;
        while (!player.getIsConnected());
        state = State.PlayersReady;
        // TODO implement get ships
        // while(!player.getShips());
        state = State.ShipsPlaced;
        // TODO implement choose starting player
        if (player.getUsername().equalsIgnoreCase("server")) {
            state = State.OurTurn;
        } else {
            state = State.EnemyTurn;
        }
        log_debug("state: " + state);
    }

    private void handleReceiveShot(Coordinate coordinate) {
        if (state != State.EnemyTurn) {
            log_debug("not in state EnemyTurn");
            return;
        }

        // TODO determine if this was a hit
        Random random = new Random();
        ShotResult shotResult = random.nextBoolean() ? ShotResult.HIT : ShotResult.MISS;
        player.send(shotResult);

        switch (shotResult) {
            case MISS:
                state = State.OurTurn;
                handleTurn();
                break;
            case HIT:
            case SUNK: {
                state = State.EnemyTurn;
                break;
            }
        }
    }

    private void handleTurn() {
        if (state != State.OurTurn) {
            log_debug("not in state OutTurn");
            return;
        }
        player.getShot();

        while(true) {
            while (!lock.tryLock());
            if (shotResult != null)
                break;
        }


        switch (shotResult) {
            case MISS:
                state = State.EnemyTurn;
                handleTurn();
                break;
            case HIT:
            case SUNK: {
                state = State.OurTurn;
                break;
            }
        }

        shotResult = null;
        lock.unlock();
    }


    /**
     * This method is called whenever the observed object is changed. An
     * application calls an {@code Observable} object's
     * {@code notifyObservers} method to have all the object's
     * observers notified of the change.
     *
     * @param o   the observable object.
     * @param arg an argument passed to the {@code notifyObservers}
     *            method.
     */
    @Override
    public void update(Observable o, Object arg) {
        log_debug("notify got something");
        if (arg instanceof Coordinate) {
            log_debug("got notified of new show at " + ((Coordinate) arg).col() + " " + ((Coordinate) arg).row());
            handleReceiveShot((Coordinate) arg);
        } else if (arg instanceof ShotResult) {
            log_debug("got notified of ShotResult");

            while(!lock.tryLock());
            shotResult = (ShotResult) arg;
            lock.unlock();
        }
    }
}
