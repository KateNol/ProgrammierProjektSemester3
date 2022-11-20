package gui;

import gui.objekt.Board;
import gui.objekt.Harbour;
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

    //replace start
    private int[][] board;
    //replace end

    private Board guiBoard;
    private Harbour guiHarbour;

    private Board guiEnemyBoard;

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
        this.board = new int[this.getMapSize()][this.getMapSize()];
    }

    public GUIPlayer(PlayerConfig playerConfig, GlobalConfig globalConfig) {
        super(playerConfig, globalConfig);
        instance = this;
        this.board = new int[this.getMapSize()][this.getMapSize()];
    }

    public static GUIPlayer getInstance() {
        return instance;
    }

    @Override
    protected void setShips() {

    }

    @Override
    public Coordinate getShot() {
        return null;
    }

    //------------------------------------------------------

    public void creatBoard(VBox vboxMiddle, VBox vBoxLeft){
        guiBoard.initializeBoard(vboxMiddle);
        guiHarbour.initializeShip(vBoxLeft);
    }

    public void setGuiBoard() {
        this.guiBoard = new Board(board, this.getShipSizes(), this.getMapSize(), tileSize);
    }

    public void setGuiHarbour() {
        this.guiHarbour = new Harbour(tileSize, this.getShipSizes());
    }

    public Board getGuiBoard() {
        return guiBoard;
    }

    public Board getGuiEnemyBoard() {
        return guiEnemyBoard;
    }

    public int getTileSize() {
        return tileSize;
    }

}
