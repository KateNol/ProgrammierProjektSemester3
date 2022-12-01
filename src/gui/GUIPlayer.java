package gui;

import gui.objekt.GuiBoard;
import gui.objekt.GuiHarbour;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import logic.*;
import network.NetworkPlayer;

import static logic.Util.log_debug;

public class GUIPlayer extends NetworkPlayer {
    private static GUIPlayer instance = null;
    private int tileSize;
    private GuiBoard guiBoard;
    private GuiHarbour guiHarbour;
    private GuiBoard guiEnemyBoard;
    private Button startButton;

    private Alignment alignment = Alignment.HOR_RIGHT;
    private boolean shipsPlaced = false;
    private Coordinate shotCoordinate = null;

    private final Object lock = new Object();

    /**
     * Create a GuiPlayer
     * @param playerConfig
     */
    public GUIPlayer(PlayerConfig playerConfig) {
        super(playerConfig);
        setTileSize(playerConfig.getMaxSemester());
        instance = this;
    }

    /**
     * Returning the instance of a GuiPlayer
     *
     * @return instance
     */
    public static GUIPlayer getInstance() {
        return instance;
    }

    //------------------------------------------------------
    @Override
    protected void setShips() {
        while (getShips() == null || getShips().size() < globalConfig.getShips(1).size() || !shipsPlaced);
        startButton.setDisable(false);
    }

    @Override
    public Coordinate getShot() {
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

        return shotCopy;
    }

    @Override
    public void updateMapState(Coordinate c, ShotResult res){
        super.updateMapState(c, res);
        guiEnemyBoard.updateBoard(res, c);
    }

    @Override
    protected ShotResult receiveShot(Coordinate shot){
        ShotResult shotResult = super.receiveShot(shot);
        guiBoard.updateBoard(shotResult, shot);
        return shotResult;
    }
    //------------------------------------------------------

    /**
     * Create Gui Objects off GuiBoard and GuiHarbour
     *
     * @param boardPosition   Position on Screen
     * @param harbourPosition Position on Screen
     */
    public void creatBoard(Button startButton, VBox boardPosition, VBox harbourPosition, Button button) {
        this.startButton = startButton;
        this.guiBoard = new GuiBoard(tileSize, false);
        this.guiHarbour = new GuiHarbour(tileSize, this.getShips());
        this.startButton = startButton;
        guiBoard.initializeBoard(boardPosition);
        guiHarbour.initializeShip(harbourPosition);
    }

    /**
     * Create enemy Board
     * @param enemyBoard
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
     * Get guiEnemyBoard
     * @return guiEnemyBoard
     */
    public GuiBoard getGuiEnemyBoard() {
        return guiEnemyBoard;
    }

    /**
     * Get guiHarbour
     * @return guiHarbour
     */
    public GuiHarbour getGuiHarbour() {
        return guiHarbour;
    }

    /**
     * Get tileSize
     * @return tileSize
     */
    public int getTileSize() {
        return tileSize;
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
     * @param alignment
     */
    public void setAlignment(Alignment alignment) {
        this.alignment = alignment;
    }

    /**
     * notify when all Ships are placed or reset
     * @param b
     */
    public void confirmShipsPlaced(Boolean b){
        this.shipsPlaced = b;
    }

    public void setShotCoordinate(Coordinate shotCoordinate) {
        this.shotCoordinate = shotCoordinate;
        synchronized (lock) {
            lock.notify();
        }
        log_debug("shot lock notify");
    }

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
