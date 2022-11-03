package logic;

import javax.xml.stream.events.EndElement;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static logic.Util.log_debug;
import static logic.Util.log_stderr;

public class Logic implements Observer {
    enum State {
        Start,
        PlayersReady,
        ShipsPlaced,
        GameReady,
        OurTurn,
        WaitForShotResponse,
        EnemyTurn,
        GameOver
    }

    private Player player = null;
    private State state = null;

    private int semester = 0;
    private boolean gameOver = false;

    private Coordinate shot = new Coordinate(-1, -1);
    private ShotResult shotResult = ShotResult.SUNK;

    private final Object shotLock = new Object();
    private final Object shotResultLock = new Object();

    // FIXME: remove this, only used by gui-player
    public Logic(Player p, Player e) {
        log_stderr("do not use this method");
        System.exit(1);
    }

    public Logic(Player player) {
        this.player = player;
        player.addObserver(this);

        Thread logicThread = new Thread(this::logicGameLoop);
        logicThread.setName("Logic Game Loop");
        logicThread.setDaemon(false);
        logicThread.start();
    }

    private void logicGameLoop() {
        state = State.Start;
        // wait for both players to connect
        while (!player.getIsConnected()) ;
        log_debug("both players connected");
        state = State.PlayersReady;

        // TODO get ships from player
        state = State.GameReady;

        // get info on who begins
        // TODO get actual info, for now server always begins
        if (player.getUsername().equalsIgnoreCase("server")) {
            state = State.OurTurn;
        } else {
            state = State.EnemyTurn;
        }

        while (!gameOver) {
            switch (state) {
                case OurTurn -> {
                    // our turn, ask our player for a move
                    Coordinate coordinate = player.getShot();
                    log_debug("our player wants to shoot at " + coordinate);
                    // TODO check if this move would be legal
                    player.sendShot(coordinate);
                    log_debug("waiting for response");
                    state = State.WaitForShotResponse;
                }
                case WaitForShotResponse -> {
                    try {
                        synchronized (shotResultLock) {
                            shotResultLock.wait();
                        }
                        log_debug("got response " + shotResult);
                        if (shotResult == ShotResult.HIT || shotResult == ShotResult.SUNK) {
                            state = State.OurTurn;
                        } else {
                            state = State.EnemyTurn;
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                case EnemyTurn -> {
                    try {
                        synchronized (shotLock) {
                            shotLock.wait();
                            log_debug("received shot, sending response");
                            // TODO actually evaluate the shot
                            Random random = new Random();
                            ShotResult shotResult = random.nextBoolean() ? ShotResult.HIT : ShotResult.MISS;
                            player.sendShotResponse(shotResult);
                            if (shotResult == ShotResult.HIT || shotResult == ShotResult.SUNK) {
                                state = State.EnemyTurn;
                            } else {
                                state = State.OurTurn;
                            }
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
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
        if (arg instanceof Coordinate recvShot) {
            log_debug("got notified of new shot at " + ((Coordinate) arg).row() + " " + ((Coordinate) arg).col());
            synchronized (shotLock) {
                log_debug("acquired lock for shot");
                shot = recvShot;
                shotLock.notify();
            }
        } else if (arg instanceof ShotResult recvShotResult) {
            log_debug("got notified of ShotResult " + recvShotResult);
            synchronized (shotResultLock) {
                log_debug("acquired lock");
                shotResult = recvShotResult;
                shotResultLock.notify();
            }
        }
    }
}
