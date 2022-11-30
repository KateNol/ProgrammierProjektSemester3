package logic;

import gui.controllers.View;
import gui.controllers.ViewSwitcher;
import network.NetworkPlayer;

import java.util.Deque;
import java.util.Observable;
import java.util.Observer;
import java.util.Stack;
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
        switchState(State.Start);

        // wait for both players to connect
        while (!player.getIsConnectionEstablished()) ;
        log_debug("both players connected");
        switchState(State.PlayersReady);

        // TODO get ships from player
        player.setShips();
        switchState(State.GameReady);
        player.setReadyToBegin(true);
        while (!player.getEnemyReadyToBegin());

        // get info on who begins
        // TODO get actual info, for now server always begins
        if (player.getUsername().equalsIgnoreCase("server")) {
            switchState(State.OurTurn);
        } else {
            switchState(State.EnemyTurn);
        }

        String winner = player.getUsername().equalsIgnoreCase("host") ? "host" : "client";

        // begin loop
        while (state != State.GameOver) {
            switch (state) {
                case OurTurn -> {
                    // our turn, ask our player for a move
                    shot = player.getShot();
                    log_debug("our player wants to shoot at " + shot);
                    // TODO check if this move would be legal
                    player.sendShot(shot);
                    log_debug("waiting for response");
                    switchState(State.WaitForShotResponse);
                }
                case WaitForShotResponse -> {
                    // we just shot somewhere, now we wait for a response, this means
                    // we have to wait for notify() to get called
                    while (shotResultStack.isEmpty());
                    shotResult = shotResultStack.pop();
                    log_debug("got response " + shotResult);
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
                    while (shotStack.isEmpty()) ;
                    shot = shotStack.pop();
                    assert shot != null;
                    ShotResult shotResult = player.receiveShot(shot);
                    log_debug("received shot " + shot + ", sending response: " + shotResult);
                    // send the result to the other player
                    boolean gameOver = player.noShipsLeft();
                    player.sendShotResponse(shotResult, gameOver);
                    if (gameOver) {
                        log_debug("game over, we lost!");
                        winner = player.getUsername().equalsIgnoreCase("host") ? "host" : "client";
                        switchState(State.GameOver);
                        ViewSwitcher.switchTo(View.Menu);
                    } else if (shotResult == ShotResult.HIT || shotResult == ShotResult.SUNK) {
                        switchState(State.EnemyTurn);
                    } else {
                        switchState(State.OurTurn);
                    }
                }
            }
        }
        player.sendEnd(winner);
        player.sendBye();
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
        if (arg instanceof String argStr) {
            log_debug("got notified of game over, we seem to have won");
            if (argStr.equalsIgnoreCase("game over") || argStr.equalsIgnoreCase("gameover"))
                switchState(State.GameOver);
        } else if (arg instanceof ShotResult recvShotResult) {
            log_debug("got notified of ShotResult " + recvShotResult);
            shotResultStack.push(recvShotResult);
        } else if (arg instanceof Coordinate recvShot) {
            log_debug("got notified new shot at " + ((Coordinate) arg).col() + " " + ((Coordinate) arg).row());
            shotStack.push(recvShot);
        }

    }
}
