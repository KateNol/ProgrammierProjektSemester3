package logic;

import network.NetworkPlayer;
import network.ServerMode;
import shared.Notification;

import java.util.Deque;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ConcurrentLinkedDeque;

import static logic.Util.*;

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

    /*
     * these 2 Stacks hold the incoming updates we receive from the network.
     * by using a >Concurrent< LinkedDeque we save ourselves from using thread locks and further synchronisation
     * because input is not guaranteed to be received in the correct order, using 2 separate stacks is easier
     * FIXME: if any one stack at any point holds more than 1 item that (probably) means one client is out of sync, should we check for this?
     * TODO: look for a better, for thread friendly, method that while(stack.isEmpty()) to wait for input
     */
    private final Deque<Coordinate> shotStack = new ConcurrentLinkedDeque<>();
    private final Deque<ShotResult> shotResultStack = new ConcurrentLinkedDeque<>();


    private int semester = 0;

    private Coordinate shot = new Coordinate(-1, -1);
    private ShotResult shotResult = ShotResult.SUNK;

    private final Thread logicThread;

    public Logic(NetworkPlayer player) {
        this.player = player;
        player.addObserver(this);

        logicThread = new Thread(() -> {
            try {
                logicGameLoop();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        logicThread.setName("Logic Game Loop");
        logicThread.setDaemon(true);
        logicThread.start();
    }


    /**
     * main logic thread containing the game loop
     */
    private void logicGameLoop() throws InterruptedException {
        switchState(State.Start);
        // wait for both players to connect
        while (!logicThread.isInterrupted() && !player.getIsConnectionEstablished()) ;
        log_debug("both players connected");
        log_debug("the game will be played in semester " + player.getNegotiatedSemester());
        player.loadGlobalConfig();
        player.setMaxSemester(player.getNegotiatedSemester());
        switchState(State.PlayersReady);

        while(!logicThread.isInterrupted() && !player.getStart());
        player.setShips();
        switchState(State.GameReady);
        player.setReadyToBegin();
        while (!logicThread.isInterrupted() && !player.getEnemyReadyToBegin()) ;

        // get info on who begins
        // TODO get actual info, for now server always begins
        if (player.getWeBegin()) {
            switchState(State.OurTurn);
        } else {
            switchState(State.EnemyTurn);
        }

        String winner = player.getServerMode() != ServerMode.SERVER ? "host" : "client";

        // begin loop
        while (!logicThread.isInterrupted() && state != State.GameOver) {
            switch (state) {
                case OurTurn -> {
                    // our turn, ask our player for a move
                    shot = player.getShot();
                    // TODO check if this move would be legal
                    player.sendShot(shot);
                    switchState(State.WaitForShotResponse);
                }
                case WaitForShotResponse -> {
                    // we just shot somewhere, now we wait for a response, this means
                    // we have to wait for notify() to get called
                    // while (!logicThread.isInterrupted() && shotResultStack.isEmpty());
                    while (shotResultStack.isEmpty()) {
                        synchronized (logicThread) {
                            logicThread.wait();
                        }
                    }
                    shotResult = shotResultStack.pop();
                    //TODO update enemyMap with shotResponse. Get coordinate somewhere
                    player.updateMapState(shot, shotResult);
                    // if we hit/sunk, its our turn again, else its the enemies turn next
                    if (shotResult == ShotResult.HIT || shotResult == ShotResult.SUNK) {
                        switchState(State.OurTurn);
                    } else {
                        switchState(State.EnemyTurn);
                    }
                }
                case EnemyTurn -> {
                    // its the enemies turn, wait until notify() tells us where the enemy shot
                    // while (!logicThread.isInterrupted() && shotStack.isEmpty());
                    while (shotStack.isEmpty()) {
                        synchronized (logicThread) {
                            logicThread.wait();
                        }
                    }
                    shot = shotStack.pop();
                    assert shot != null;
                    ShotResult shotResult = player.receiveShot(shot);
                    log_debug("received shot " + shot + ", sending response: " + shotResult);
                    // send the result to the other player
                    boolean gameOver = player.noShipsLeft();
                    player.sendShotResponse(shotResult, gameOver);
                    if (gameOver) {
                        log_debug("game over, we lost!");
                        winner = player.getServerMode() == ServerMode.SERVER ? "client" : "host";
                        switchState(State.GameOver);
                    } else if (shotResult == ShotResult.HIT || shotResult == ShotResult.SUNK) {
                        switchState(State.EnemyTurn);
                    } else {
                        switchState(State.OurTurn);
                    }
                }
            }
        }
        player.onGameOver(winner);
        log_debug("logic thread ending");
    }

    private synchronized void switchState(State newState) {
        if (state == State.GameOver)
            return;
        log_debug("switching state: " + state + " -> " + newState);
        state = newState;
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
        if (arg instanceof Notification argNotification) {
            switch (argNotification) {
                case GameOver -> {
                    log_debug("got notified of GAME OVER, we seem to have won");
                    switchState(State.GameOver);
                }
                case PeerDisconnected -> {
                    log_debug("got notified of peer disconnect, trying to end thread");
                    switchState(State.GameOver);
                    logicThread.interrupt();
                }
                case SelfDestruct -> {
                    log_debug("got notified of player self destruct, trying to end thread");
                    logicThread.interrupt();
                }
            }
        } else if (arg instanceof ShotResult recvShotResult) {
            shotResultStack.push(recvShotResult);
            synchronized (logicThread) {
                logicThread.notify();
            }
        } else if (arg instanceof Coordinate recvShot) {
            shotStack.push(recvShot);
            synchronized (logicThread) {
                logicThread.notify();
            }
        }

    }
}
