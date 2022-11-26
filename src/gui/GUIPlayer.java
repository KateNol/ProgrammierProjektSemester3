package gui;

import gui.objekt.GuiBoard;
import gui.objekt.GuiHarbour;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import logic.*;
import network.NetworkPlayer;
import network.ServerMode;

import java.io.IOException;

public class GUIPlayer extends NetworkPlayer {
    private static GUIPlayer instance = null;
    private int tileSize = 40;
    private GuiBoard guiBoard;
    private GuiHarbour guiHarbour;
    private GuiBoard guiEnemyBoard;

    private Alignment alignment = Alignment.HOR_RIGHT;
    private boolean shipsPlaced = false;
    private Coordinate  shotCoordinate = null;
    private Button button;

    /**
     * Create a GuiPlayer
     *
     * @param playerConfig maxSemester & userName
     * @param globalConfig mapSize & ship Arraylist
     * @param serverMode
     * @param address
     * @param port
     * @throws IOException
     */
    public GUIPlayer(PlayerConfig playerConfig, ServerMode serverMode, String address, int port) throws IOException {
        super(playerConfig, serverMode, address, port);
        instance = this;
    }

    public GUIPlayer(PlayerConfig playerConfig, GlobalConfig globalConfig, ServerMode serverMode, String address) throws IOException {
        super(playerConfig, globalConfig, serverMode, address);
        instance = this;
    }

    public GUIPlayer(PlayerConfig playerConfig, ServerMode serverMode, int port) throws IOException {
        super(playerConfig, serverMode, port);
        instance = this;
    }

    public GUIPlayer(PlayerConfig playerConfig, GlobalConfig globalConfig, ServerMode serverMode) throws IOException {
        super(playerConfig, globalConfig, serverMode);
        instance = this;
    }

    public GUIPlayer(PlayerConfig playerConfig, GlobalConfig globalConfig) {
        super(playerConfig, globalConfig);
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
        button.setDisable(false);
    }

    @Override
    public Coordinate getShot() {
        while (shotCoordinate == null);
        return shotCoordinate;
    }

    @Override
    public void updateMapState(Coordinate c, ShotResult res){
        super.updateMapState(c, res);
        //TODO update in GuiBoard
    }

    @Override
    protected ShotResult receiveShot(Coordinate shot){
        ShotResult shotResult = super.receiveShot(shot);
        guiEnemyBoard.updateBoard(shotResult, shot);
        return shotResult;
    }
    //------------------------------------------------------

    /**
     * Create Gui Objects off GuiBoard and GuiHarbour
     * @param boardPosition Position on Screen
     * @param harbourPosition Position on Screen
     */
    public void creatBoard(VBox boardPosition, VBox harbourPosition, Button button){
        this.guiBoard = new GuiBoard(tileSize, false);
        this.guiHarbour = new GuiHarbour(tileSize, this.getShips());
        this.button = button;
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
    }
}
