package gui;

import gui.controllers.*;
import gui.objekt.GuiBoard;
import gui.objekt.GuiHarbour;
import javafx.application.Platform;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import logic.*;
import network.NetworkPlayer;
import network.ServerMode;
import network.internal.ChatMsg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import static logic.Util.log_debug;

/**
 * @author Stefan
 * Gui interactive player
 */
public class GUIPlayer extends NetworkPlayer implements Observer {
    //instance
    private static GUIPlayer instance = null;
    private final PlayerConfig playerConfig;

    //tile
    private int tileSize;

    //Board and Ships
    private GuiHarbour guiHarbour;
    private GuiBoard guiBoard;
    private GuiBoard guiEnemyBoard;

    //alignment
    private Alignment alignment = Alignment.HOR_RIGHT;

    //states
    private boolean isShipsPlaced;

    //shotCoordinate
    private Coordinate shotCoordinate = null;

    //lock object
    private final Object lock = new Object();

    /**
     * Create a GuiPlayer
     * @param playerConfig Save file from the player
     */
    public GUIPlayer(PlayerConfig playerConfig) {
        super(playerConfig);
        log_debug("created gui player");
        this.playerConfig = playerConfig;
        this.tileSize = Settings.setTileSize(playerConfig.getMaxSemester());
        instance = this;
    }

    /**
     * Returning the instance of a GuiPlayer
     * @return instance
     */
    public static GUIPlayer getInstance() {
        return instance;
    }

    /**
     * Destroy Player
     */
    @Override
    public void destroy() {
        super.destroy();
        instance = null;
    }

    /**
     * Open end screen if game is over
     * @param winner client or host
     */
    public void onGameOver(String winner) {
        log_debug("gui onGameOver " + winner);
        super.onGameOver(winner);
        Platform.runLater(() -> {
            if (ControllerGame.getInstance() == null) {
                ViewSwitcher.switchTo(View.Game);
            }
            ControllerGame.getInstance().getTurnLabel().setText("GAME OVER");
            ControllerGame.getInstance().openEndScreen();
            if((getServerMode() == ServerMode.SERVER && winner.equalsIgnoreCase("host"))
                    || (getServerMode() == ServerMode.CLIENT && winner.equalsIgnoreCase("client"))){
                ControllerGame.getInstance().getWinnerLabel().setText(getUsername());
                playerConfig.increaseMaxSemester();
                if(getNegotiatedSemester() == 6){
                    ControllerGame.getInstance().playEsterEgg();
                } else {
                    AudioPlayer.playMusic(Audio.Win);
                }
            } else {
                ControllerGame.getInstance().getWinnerLabel().setText(getEnemyUsername());
                playerConfig.decreaseMaxSemester();
                AudioPlayer.playMusic(Audio.Lose);
                playerConfig.decreaseMaxSemester();
            }
            try {
                FileController.updateFile(playerConfig);
                Util.log_debug("finished to update file");
            } catch (IOException e) {
                Util.log_debug("failed to update playerConfig");
            }
        });
    }

    /**
     * place random Ships
     */
    public void setRandomShips() {
        getMyMap().fillWater();
        guiBoard.updateBoard();

        Random c = new Random();
        ArrayList<Ship> ships = getArrayListShips();

        for (Ship ship : ships) {
            boolean placed = true;
            do {
                int x = c.nextInt(myMap.getMapSize() - 1);
                int y = c.nextInt(myMap.getMapSize() - 1);
                Coordinate coordinate = new Coordinate(x, y);
                Alignment alignment = c.nextBoolean() ? Alignment.VERT_DOWN : Alignment.HOR_RIGHT;
                try {
                    placed = addShip(ship, coordinate, alignment);
                } catch (IllegalArgumentException e) {
                    placed = false;
                }
                if (placed) {
                    log_debug("successfully placed ship at " + coordinate + " aligned " + alignment);
                }
            } while (!placed);
        }
        guiBoard.updateBoard();
        confirmShipsPlaced(true);
        ControllerLobby.getInstance().enableStartButton();
    }
    //-------------------methods logic calls-------------------
    /**
     * logic calls for setShips
     */
    @Override
    protected void setShips() {
        while (!GUIPlayer.getInstance().getIsConnectionEstablished()) {
            gui.Util.log_stderr("gui spinning on connection");
        }
        while (!Thread.currentThread().isInterrupted() && (getShips() == null || getShips().size() < GlobalConfig.getShips(getNegotiatedSemester()).size() || !isShipsPlaced));
        addObserver(this);
        ControllerLobby.getInstance().enableStartButton();
    }

    /**
     * logic calls for shot
     * @return shot
     */
    @Override
    public Coordinate getShot() {
        while (!GUIPlayer.getInstance().getIsConnectionEstablished()) {
            gui.Util.log_stderr("gui spinning on connection");
        }

        Platform.runLater(() -> {
            guiBoard.setTopLeftCorner(Color.DARKRED);
            if (guiEnemyBoard != null)
                guiEnemyBoard.setTopLeftCorner(Color.DARKGREEN);
            if (ControllerGame.getInstance() != null) {
                ControllerGame.getInstance().getTurnLabel().setText("It's " + getUsername() + "'s Turn");
            }
        });

        log_debug("getting shot from GUIPlayer");
        Coordinate shotCopy;
        try {
            synchronized (lock) {
                if (shotCoordinate == null) {
                    log_debug("GUIPlayer waiting for shot");
                    lock.wait();
                    log_debug("GUIPlayer got notified of shot");
                }
                shotCopy = new Coordinate(shotCoordinate.row(), shotCoordinate.col());
                shotCoordinate = null;
            }
        } catch (InterruptedException e) {
            return new Coordinate(-1, -1);
            // throw new RuntimeException(e);
        }
        log_debug("got shot from GUIPlayer at " + shotCopy);

        Platform.runLater(() -> {
            if (ControllerGame.getInstance() != null) {
                ControllerGame.getInstance().getTurnLabel().setText("It's " + getEnemyUsername() + "'s Turn");
                guiBoard.setTopLeftCorner(Color.DARKGREEN);
                if (guiEnemyBoard != null)
                    guiEnemyBoard.setTopLeftCorner(Color.DARKRED);
            }
        });

        return shotCopy;
    }

    /**
     * update guiBoard when Map changes in logic
     * @param c Coordinate the shot was set
     * @param res shotResult, the result of the shot (either hit or miss, or sunk)
     */
    @Override
    public void updateMapState(Coordinate c, ShotResult res){
        super.updateMapState(c, res);
        while (guiEnemyBoard == null);
        guiEnemyBoard.updateBoard(res, c);
    }

    /**
     * receiveShot when logic calls
     * @param shot given shot
     * @return shotResult from guiPlayer
     */
    @Override
    protected ShotResult receiveShot(Coordinate shot){
        ShotResult shotResult = super.receiveShot(shot);
        guiBoard.updateBoard(shotResult, shot);
        return shotResult;
    }

    /**
     * Display chat message from enemy
     * @param message incoming message
     */
    @Override
    public void receiveChatMessage(String message) {
        if (ControllerGame.getInstance() == null)
            return;
        log_debug("gui player received msg " + message);
        ControllerGame.getInstance().displayChatMessage("> " + getEnemyUsername() + ": " +message);
    }

    /**
     * Create Gui Objects off GuiBoard and GuiHarbour
     * @param boardPosition   Position on Screen
     * @param harbourPosition Position on Screen
     */
    public void creatBoard(VBox boardPosition, VBox harbourPosition) {
        this.guiBoard = new GuiBoard(tileSize, false);
        this.guiHarbour = new GuiHarbour(tileSize, this.getShips());
        guiBoard.initializeBoard(boardPosition);
        guiHarbour.initializeShip(harbourPosition);
    }

    /**
     * Create enemy Board
     * @param enemyBoard play field from the enemy
     */
    public void createEnemyBoard(VBox enemyBoard){
        guiEnemyBoard = new GuiBoard(tileSize, true);
        guiEnemyBoard.initializeBoard(enemyBoard);
    }

    /**
     * Get guiBoard
     * @return guiBoard
     */
    public GuiBoard getGuiBoard() {
        while (guiBoard == null);
        return guiBoard;
    }

    /**
     * Get guiHarbour
     * @return guiHarbour
     */
    public GuiHarbour getGuiHarbour() {
        return guiHarbour;
    }

    /**
     * Get alignment
     * @return alignment
     */
    public Alignment getAlignment() {
        return alignment;
    }

    /**
     * Set alignment
     * @param alignment direction from the ship
     */
    public void setAlignment(Alignment alignment) {
        this.alignment = alignment;
    }

    /**
     * notify when all Ships are placed or reset
     * @param b if all ships where placed
     */
    public void confirmShipsPlaced(Boolean b){
        this.isShipsPlaced = b;
    }

    /**
     * Set coordinate where guiPlayer clicked on
     * @param shotCoordinate coordinate where guiPlayer clicked on
     */
    public void setShotCoordinate(Coordinate shotCoordinate) {
        this.shotCoordinate = shotCoordinate;
        synchronized (lock) {
            lock.notify();
        }
        log_debug("shot lock notify");
    }

    /**
     * Get tileSize
     * @return tileSize
     */
    public int getTileSize() {
        return tileSize;
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
        if (arg instanceof ChatMsg) {
            receiveChatMessage(((ChatMsg) arg).msg);
        }
    }

    /**
     * Get guiEnemyBoard
     * @return guiEnemyBoard
     */
    public GuiBoard getGuiEnemyBoard(){
        return guiEnemyBoard;
    }
}
