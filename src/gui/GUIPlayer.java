package gui;

import gui.controllers.ControllerGame;
import gui.controllers.ControllerLobby;
import gui.controllers.FileController;
import gui.objekt.GuiBoard;
import gui.objekt.GuiHarbour;
import javafx.application.Platform;
import javafx.scene.layout.VBox;
import logic.*;
import network.NetworkPlayer;
import network.ServerMode;

import java.io.IOException;

import static logic.Util.log_debug;

/**
 * @author Stefan
 * Gui interactive player
 */
public class GUIPlayer extends NetworkPlayer {
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
    private boolean turn;

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
        this.playerConfig = playerConfig;
        setTileSize(playerConfig.getMaxSemester());
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
     * set turn
     * @param turn to shoot
     */
    public void setTurn(boolean turn){
        this.turn = turn;
    }

    /**
     * Open end screen if game is over
     * @param winner client or host
     */
    public void onGameOver(String winner) {
        super.onGameOver(winner);
        Platform.runLater(() -> {
            ControllerGame.getInstance().getTurnLabel().setText("GAME OVER");
            ControllerGame.getInstance().openEndScreen();
            if((getServerMode() == ServerMode.SERVER && winner.equalsIgnoreCase("host"))
                    || (getServerMode() == ServerMode.CLIENT && winner.equalsIgnoreCase("client"))){
                ControllerGame.getInstance().getWinnerLabel().setText(getUsername());
                playerConfig.increaseMaxSemester();
            } else {
                ControllerGame.getInstance().getWinnerLabel().setText(getEnemyUsername());
                if(playerConfig.getMaxSemester() - 1 != 0){
                    playerConfig.decreaseMaxSemester();
                }
            }
            try {
                FileController.updateFile(playerConfig);
            } catch (IOException e) {
                Util.log_debug("failed to update playerConfig");
            }
        });
    }

    //-------------------methods logic calls-------------------
    /**
     * logic calls for setShips
     */
    @Override
    protected void setShips() {
        if (!getIsConnectionEstablished())
            System.exit(1);
        while (!Thread.currentThread().isInterrupted() && (getShips() == null || getShips().size() < GlobalConfig.getShips(getNegotiatedSemester()).size() || !isShipsPlaced)) ;
        ControllerLobby.getInstance().enableStartButton();
    }

    /**
     * logic calls for shot
     * @return shot
     */
    @Override
    public Coordinate getShot() {
        Platform.runLater(() -> {
            if (ControllerGame.getInstance() != null) {
                //TODO
                if (turn) {
                    ControllerGame.getInstance().getTurnLabel().setText("It's " + getUsername() + "'s Turn");
                    turn = false;
                }
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
            throw new RuntimeException(e);
        }
        log_debug("got shot from GUIPlayer at " + shotCopy);

        Platform.runLater(() -> {
            if (ControllerGame.getInstance() != null) {
                //TODO
                if (!turn) {
                    ControllerGame.getInstance().getTurnLabel().setText("It's " + getEnemyUsername() + "'s Turn");
                    turn = false;
                }
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
    //--------------------------------------

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
     * Resize tiles by maxSemester
     * @param tileSize TileSize in Pixel
     */
    public void setTileSize(int tileSize) {
        switch (tileSize){
            case 1 -> this.tileSize = 29;
            case 2 -> this.tileSize = 26;
            case 3 -> this.tileSize = 25;
            case 4 -> this.tileSize = 24;
            case 5 -> this.tileSize = 23;
            case 6 -> this.tileSize = 22;
        }
    }
}
