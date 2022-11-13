package logic;

import network.NetworkPlayer;

import java.util.Observable;
import java.util.Observer;

import static logic.Util.log_debug;
import static logic.Util.log_stderr;

/**
 * the logic class contains a thread with the game loop
 * the logic is an overserver of the player, the player will notify the logic of shot and shotresult events
 */
public class Logic implements Observer {

    /**
     * the game loop is implemented as a finite state machine, these are the states
     */
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

    private NetworkPlayer player = null;
    private State state = null;

    private int semester = 0;

    private Coordinate shot = new Coordinate(-1, -1);
    private ShotResult shotResult = ShotResult.SUNK;

    private final Object shotLock = new Object();
    private final Object shotResultLock = new Object();


    public Logic(NetworkPlayer player) {
        this.player = player;
        player.addObserver(this);

        Thread logicThread = new Thread(this::logicGameLoop);
        logicThread.setName("Logic Game Loop");
        logicThread.setDaemon(false);
        logicThread.start();
    }


    /**
     * main logic thread containing the game loop
     */
    private void logicGameLoop() {
        state = State.Start;

        // wait for both players to connect
        while (!player.getIsConnectionEstablished()) ;
        log_debug("both players connected");
        state = State.PlayersReady;

        // TODO get ships from player
        player.setShips();
        state = State.GameReady;
        player.setReadyToBegin(true);
        while (!player.getEnemyReadyToBegin()) ;

        // get info on who begins
        // TODO get actual info, for now server always begins
        if (player.getUsername().equalsIgnoreCase("server")) {
            state = State.OurTurn;
        } else {
            state = State.EnemyTurn;
        }

        // begin loop
        while (state != State.GameOver) {
            switch (state) {
                case OurTurn -> {
                    // our turn, ask our player for a move
                    /*Coordinate coordinate = player.getShot();
                    log_debug("our player wants to shoot at " + coordinate);
                    // TODO check if this move would be legal
                    player.sendShot(coordinate);*/
                    shot = player.getShot();
                    log_debug("our player wants to shoot at " + shot);
                    // TODO check if this move would be legal
                    player.sendShot(shot);
                    log_debug("waiting for response");
                    state = State.WaitForShotResponse;
                }
                case WaitForShotResponse -> {
                    // we just shot somewhere, now we wait for a response, this means
                    // we have to wait for notify() to get called
                    try {
                        synchronized (shotResultLock) {
                            shotResultLock.wait();
                        }
                        log_debug("got response " + shotResult);
                        //TODO update enemyMap with shotResponse. Get coordinate somewhere
                        player.updateMapState(shot, shotResult);
                        // if we hit/sunk, its our turn again, else its the enemies turn next
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
                    // its the enemies turn, wait until notify() tells us where the enemy shot
                    try {
                        synchronized (shotLock) {
                            shotLock.wait();
                            log_debug("received shot, sending response");
                            ShotResult shotResult = player.receiveShot(shot);
                            // send the result to the other player
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
     * this method will get triggert by the player
     * for more info visit observer-pattern
     *
     * @param arg arg will be a Coordinate (enemy wants to shoot there)
     *            or a ShotResult (we shot somewhere and this is the result)
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
        } else if (arg instanceof String argStr) {
            if (argStr.equalsIgnoreCase("game over"))
                state = State.GameOver;
        }
    }
}
