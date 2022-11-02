package logic;

import java.util.Observable;
import java.util.Observer;

import static logic.Util.log_debug;
import static logic.Util.log_stderr;

public class Logic implements Observer {
    private Player player = null;

    private int semester = 0;
    private boolean gameOver = false;

    private volatile Coordinate mostRecentShotReceived = null;


    // FIXME: remove this, only used by gui-player
    public Logic(Player p, Player e) {
        log_stderr("do not use this method");
        System.exit(1);
    }

    public Logic(Player player) {
        this.player = player;
        player.addObserver(this);
        Thread gameLoopThread = new Thread(this::gameLoop);
        gameLoopThread.setDaemon(false);
        gameLoopThread.setName("Logic GameLoop Thread");
        gameLoopThread.start();
    }

    private void gameLoop() {
        log_debug("logic thread started");

        // wait until the player has connected to his peer
        // FIXME this is inefficient as this blocks a whole CPU core
        log_debug("waiting for players to connect");
        while (!player.getIsConnected()) ;
        semester = player.getCommonSemester();
        log_debug("semester played is: " + semester);

        // ask our player to place his ships
        // TODO implement

        // wait until enemy has placed ships
        // TODO implement

        // TODO figure out who begins

        while (!gameOver) {
            // wait for shot to arrive
            // FIXME this is inefficient as this blocks a whole CPU core
            while (mostRecentShotReceived == null) Thread.onSpinWait();
            log_debug("we got shot");
            // figure out if the enemy hit
            player.send(ShotResult.HIT);

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
        if (arg instanceof Coordinate) {
            log_debug("got notified of new show at " + ((Coordinate) arg).col() + ((Coordinate) arg).row());
            mostRecentShotReceived = (Coordinate) arg;

        } else if (arg instanceof ShotResult) {
            log_debug("got notified of ShotResult");
        }
    }
}
