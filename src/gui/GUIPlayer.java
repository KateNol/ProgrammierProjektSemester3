package gui;

import gui.objekt.GuiBoard;
import gui.objekt.GuiHarbour;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import logic.Coordinate;
import logic.GlobalConfig;
import logic.PlayerConfig;
import network.NetworkPlayer;
import network.ServerMode;

import java.io.IOException;

public class GUIPlayer extends NetworkPlayer {
    private static GUIPlayer instance;
    private int tileSize = 40;
    private GuiBoard guiBoard;
    private GuiHarbour guiHarbour;
    private GuiBoard guiEnemyBoard;

    /**
     * Create a GuiPlayer
     * @param playerConfig maxSemester & userName
     * @param globalConfig mapSize & ship Arraylist
     * @param serverMode
     * @param address
     * @param port
     * @throws IOException
     */
    public GUIPlayer(PlayerConfig playerConfig, GlobalConfig globalConfig, ServerMode serverMode, String address, int port) throws IOException {
        super(playerConfig, globalConfig, serverMode, address, port);
        instance = this;
    }

    public GUIPlayer(PlayerConfig playerConfig, GlobalConfig globalConfig, ServerMode serverMode, String address) throws IOException {
        super(playerConfig, globalConfig, serverMode, address);
        instance = this;
    }

    public GUIPlayer(PlayerConfig playerConfig, GlobalConfig globalConfig, ServerMode serverMode, int port) throws IOException {
        super(playerConfig, globalConfig, serverMode, port);
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
     * @return instance
     */
    public static GUIPlayer getInstance() {
        return instance;
    }

    //------------------------------------------------------
    @Override
    protected void setShips() {

    }

    @Override
    public Coordinate getShot() {
        return null;
    }
    //------------------------------------------------------

    /**
     * Create Gui Objects off GuiBoard and GuiHarbour
     * @param vboxMiddle Position on Screen
     * @param vBoxLeft Position on Screen
     */
    public void creatBoard(VBox vboxMiddle, VBox vBoxLeft){
        this.guiBoard = new GuiBoard(this.getShips(), this.getMapSize(), tileSize, false, this);
        this.guiHarbour = new GuiHarbour(tileSize, this.getShips());
        guiBoard.initializeBoard(vboxMiddle);
        guiHarbour.initializeShip(vBoxLeft);
    }

    public void createEnemyBoard(VBox enemyBoard, Label label){
        guiEnemyBoard = new GuiBoard(this.getMapSize(), tileSize, true, label);
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
     * Get tileSize
     * @return tileSize
     */
    public int getTileSize() {
        return tileSize;
    }
}
