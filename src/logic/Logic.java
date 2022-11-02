package logic;

import static logic.Util.log_debug;
import static logic.Util.log_stderr;

public class Logic {
    private Player player = null;

    private int semester = 0;


    // FIXME: remove this, only used by gui-player
    public Logic(Player p, Player e) {
        log_stderr("do not use this method");
        System.exit(1);
    }

    public Logic(Player player) {
        this.player = player;
        Thread gameLoopThread = new Thread(this::gameLoop);
        gameLoopThread.setDaemon(false);
        gameLoopThread.setName("Logic GameLoop Thread");
        gameLoopThread.start();
    }

    private void gameLoop() {
        log_debug("logic thread started");

        // wait until the player has connected to his peer
        log_debug("waiting for players to connect");
        while (!player.getIsConnected()) ;
        semester = player.getCommonSemester();
        log_debug("semester played is: " + semester);

        // ask our player to place his ships
        log_debug("getting ships");


        // wait until enemy has placed ships

        //
    }
}
