package gui.objekt;

import gui.GUIPlayer;
import gui.controllers.Audio;
import gui.controllers.AudioPlayer;
import gui.controllers.ControllerLobby;
import gui.tile.*;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import logic.*;

import java.util.ArrayList;
import java.util.Random;

/**
 * This class is the gui board for the gui player to play on
 */
public class GuiBoard {

    private GridPane grid;
    private final int tileSize;
    private int shipPlaced = 0;
    private final boolean isEnemyBoard;
    private final GUIPlayer guiPlayer = GUIPlayer.getInstance();

    /**
     * Gui object for the board
     * @param tileSize TileSize in Pixel
     * @param isEnemyBoard is enemy or player board
     */
    public GuiBoard(int tileSize, boolean isEnemyBoard) {
        this.tileSize = tileSize;
        this.isEnemyBoard = isEnemyBoard;
    }

    /**
     * Initialize GuiBoard
     * @param vboxMiddle container for board to fill
     */
    public void initializeBoard(VBox vboxMiddle){
        grid = new GridPane();
        //set Position on Scene
        grid.setAlignment(Pos.CENTER);
        for(int row = 0; row < guiPlayer.getMapSize() + 1; row++){
            for (int col = 0; col < guiPlayer.getMapSize() + 1; col++){
                if(row == 0 && col == 0){
                    TileBoardText tbt = new TileBoardText(new Coordinate(row, col),tileSize,"");
                    grid.add(tbt, row, col, 1, 1);
                    Node node = getNodeByRowColumnIndex(row, col);
                    node.setDisable(true);
                } else {
                    if(row == 0){
                        TileBoardText tbt = new TileBoardText(new Coordinate(row, col), tileSize,"" + (char) ('A' + col - 1));
                        grid.add(tbt, col, row, 1, 1);
                        Node node = getNodeByRowColumnIndex(row, col);
                        node.setDisable(true);
                    } else if (col == 0) {
                        TileBoardText tbt = new TileBoardText(new Coordinate(row, col), tileSize,"" + row);
                        grid.add(tbt, col, row, 1, 1);
                        Node node = getNodeByRowColumnIndex(row, col);
                        node.setDisable(true);
                    } else {
                        TileWater tile = new TileWater(new Coordinate(row, col), tileSize);
                        grid.add(tile,col, row, 1, 1);
                    }
                }
            }
        }
        //add setShip methode to every Water Tile
        grid.getChildren().forEach(this::setShip);
        if(isEnemyBoard){
            grid.getChildren().forEach(this::sendShot);
        }
        vboxMiddle.getChildren().add(grid);
    }

    /**
     * Add Ships when clicked on Tiles on Board
     * @param node object clicked on
     */
    public void setShip(Node node){
        node.setOnMouseClicked(e -> {
            if(e.getSource() instanceof TileWater){
                if (!(shipPlaced == guiPlayer.getShips().size())) {
                    TileWater tileWater = (TileWater) e.getSource();
                    Coordinate coordinate = new Coordinate(tileWater.getCoordinate().row(), tileWater.getCoordinate().col());
                    Coordinate coordinateMap = new Coordinate(tileWater.getCoordinate().row() - 1, tileWater.getCoordinate().col() -1);
                    Util.log_debug("SetShipGUI = row: " + coordinate.row()  + " col: " + coordinate.col() + " " + "SetShipMapLogic = row: " + coordinateMap.row()  + " col: " + coordinateMap.col());
                    if (guiPlayer.addShip(guiPlayer.getShips().get(shipPlaced), coordinateMap, guiPlayer.getAlignment())) {
                        AudioPlayer.playSFX(Audio.PlaceShip);
                        guiPlayer.getGuiHarbour().drawShipOnBoard(grid, shipPlaced);
                        setDisabledTiles(guiPlayer.getAlignment(), coordinate);
                        shipPlaced++;
                        if (shipPlaced == guiPlayer.getShips().size()) {
                            gui.Util.log_debug("all ships are placed");
                            guiPlayer.confirmShipsPlaced(true);
                        }
                    } else {
                        failShipPlaced();
                    }
                } else {
                    gui.Util.log_debug("no more ships left");
                }
            }
        });
    }

    /**
     * notify player if ship placement failed
     */
    public void failShipPlaced(){
        ArrayList<String> failPrompt = new ArrayList<>();
        failPrompt.add("Failed Placing Ships");
        failPrompt.add("You suck at placing Ship's");
        failPrompt.add("Are you serious?");
        failPrompt.add("You are still failing");
        failPrompt.add("What is wrong with you?");
        Random rand = new Random();
        ControllerLobby.getInstance().getFailedShipPlacedLabel().setText(failPrompt.get(rand.nextInt(failPrompt.size())));
    }

    /**
     * send coordinate when shot
     * @param node object clicked on
     */
    private void sendShot(Node node) {
        node.setOnMouseClicked(e -> {
            TileWater tileWater = (TileWater) e.getSource();
            //Util.log_debug("GuiPlayer clicked ON " + tileWater.getCoordinate().row() + " " + tileWater.getCoordinate().col());
            Coordinate coordinate = new Coordinate(tileWater.getCoordinate().row() - 1, tileWater.getCoordinate().col() - 1);
            Util.log_debug("GuiPlayer send shot: row: " + coordinate.row() + " col: " + coordinate.col());
            guiPlayer.setShotCoordinate(coordinate);
            node.setDisable(true);
        });
    }

    /**
     * update board on shot events
     * @param shotResult witch shotResult to update
     * @param coordinate coordinate where to update
     */
    public void updateBoard(ShotResult shotResult, Coordinate coordinate) {
        Platform.runLater(() -> {
            switch (shotResult) {
                case HIT -> {
                    TileHit tileHit = new TileHit(new Coordinate(coordinate.row() + 1, coordinate.col() + 1), tileSize);
                    grid.add(tileHit, coordinate.col() + 1, coordinate.row() + 1, 1, 1);
                    if(isEnemyBoard){
                        AudioPlayer.playSFX(Audio.Hit);
                    }
                }
                case MISS -> {
                    TileMiss tileMiss = new TileMiss(new Coordinate(coordinate.row() + 1, coordinate.col() + 1), tileSize);
                    grid.add(tileMiss, coordinate.col() + 1, coordinate.row() + 1, 1, 1);
                    if(isEnemyBoard){
                        AudioPlayer.playSFX(Audio.Miss);
                    }
                }
                case SUNK -> {
                    if (isEnemyBoard) {
                        AudioPlayer.playSFX(Audio.Sink);
                        MapState[][] map = guiPlayer.getEnemyMap().getMap();
                        for (int row = 0; row < guiPlayer.getMapSize(); row++) {
                            for (int col = 0; col < guiPlayer.getMapSize(); col++) {
                                switch (map[row][col]) {
                                    case W -> {
                                        TileWater tileWater = new TileWater(new Coordinate(row + 1, col + 1), tileSize);
                                        grid.add(tileWater, col + 1, row + 1, 1, 1);
                                    }
                                    case S -> {
                                        TileShip tileShip = new TileShip(new Coordinate(row + 1, col + 1), tileSize);
                                        grid.add(tileShip, col + 1, row + 1, 1, 1);
                                    }
                                    case H, D -> {
                                        TileHit tileHit = new TileHit(new Coordinate(row + 1, col + 1), tileSize);
                                        grid.add(tileHit, col + 1, row + 1, 1, 1);
                                    }
                                    case M -> {
                                        TileMiss tileMiss = new TileMiss(new Coordinate(row + 1, col + 1), tileSize);
                                        grid.add(tileMiss, col + 1, row + 1, 1, 1);
                                    }
                                }
                            }
                        }
                    } else {
                        TileHit tileHit = new TileHit(new Coordinate(coordinate.row() + 1, coordinate.col() + 1), tileSize);
                        grid.add(tileHit, coordinate.col() + 1, coordinate.row() + 1, 1, 1);
                    }
                }
            }
        });
    }

    /**
     * set all tiles around a ship with disabled tiles
     * @param alignment in witch alignment the ship was placed
     * @param coordinate coordinate where ship is placed
     */
    public void setDisabledTiles(Alignment alignment, Coordinate coordinate){
        int shipLength = guiPlayer.getShips().get(shipPlaced).getSize();
        int mapSize = guiPlayer.getMapSize();
        int row = coordinate.row();
        int col = coordinate.col();
        switch (alignment) {
            case HOR_RIGHT -> {
                //left
                if (col - 1 != 0) {
                    //top left
                    if (row - 1 != 0) {
                        grid.add(new TileDisable(new Coordinate(row - 1, col - 1), tileSize), col - 1, row - 1, 1, 1);
                    }
                    //middle left
                    grid.add(new TileDisable(new Coordinate(row, col - 1), tileSize), col - 1, row, 1, 1);
                    //bottom left
                    if (row + 1 != mapSize + 1) {
                        grid.add(new TileDisable(new Coordinate(row + 1, col - 1), tileSize), col - 1, row + 1, 1, 1);
                    }
                }
                //middle
                for (int i = 0; i < shipLength; i++) {
                    if (row - 1 != 0) {
                        //top middle
                        grid.add(new TileDisable(new Coordinate(row - 1, col + i), tileSize), col + i, row - 1, 1, 1);
                    }
                    if (row + 1 != mapSize + 1) {
                        //bottom middle
                        grid.add(new TileDisable(new Coordinate(row + 1, col + i), tileSize), col + i, row + 1, 1, 1);
                    }
                }
                //right
                if (col + shipLength != mapSize + 1) {
                    //bottom right
                    if (row + 1 != mapSize + 1) {
                        grid.add(new TileDisable(new Coordinate(row + 1, col + shipLength), tileSize), col + shipLength, row + 1, 1, 1);
                    }
                    //middle right
                    grid.add(new TileDisable(new Coordinate(row, col + shipLength), tileSize), col + shipLength, row, 1, 1);
                    //top right
                    if (row - 1 != 0) {
                        grid.add(new TileDisable(new Coordinate(row - 1, col + shipLength), tileSize), col + shipLength, row - 1, 1, 1);
                    }
                }
            }
            case HOR_LEFT -> {
                //right
                if (col + 1 != mapSize + 1) {
                    //top right
                    if (row - 1 != 0) {
                        grid.add(new TileDisable(new Coordinate(row - 1, col + 1), tileSize), col + 1, row - 1, 1, 1);
                    }
                    //middle right
                    grid.add(new TileDisable(new Coordinate(row, col + 1), tileSize), col + 1, row, 1, 1);
                    //bottom right
                    if (row + 1 != mapSize + 1) {
                        grid.add(new TileDisable(new Coordinate(row + 1, col + 1), tileSize), col + 1, row + 1, 1, 1);
                    }
                }
                //middle
                for (int i = 0; i < shipLength; i++) {
                    if (row - 1 != 0) {
                        //top middle
                        grid.add(new TileDisable(new Coordinate(row - 1, col - i), tileSize), col - i, row - 1, 1, 1);
                    }
                    if (row + 1 != mapSize + 1) {
                        //bottom middle
                        grid.add(new TileDisable(new Coordinate(row + 1, col - i), tileSize), col - i, row + 1, 1, 1);
                    }
                }
                //left
                if (col - shipLength != 0) {
                    //bottom left
                    if (row + 1 != mapSize + 1) {
                        grid.add(new TileDisable(new Coordinate(row + 1, col - shipLength), tileSize), col - shipLength, row + 1, 1, 1);
                    }
                    //middle left
                    grid.add(new TileDisable(new Coordinate(row, col - shipLength), tileSize), col - shipLength, row, 1, 1);
                    //top left
                    if (row - 1 != 0) {
                        grid.add(new TileDisable(new Coordinate(row - 1, col - shipLength), tileSize), col - shipLength, row - 1, 1, 1);
                    }
                }
            }
            case VERT_DOWN -> {
                //top
                if (row - 1 != 0) {
                    //right top
                    if (col + 1 != mapSize + 1) {
                        grid.add(new TileDisable(new Coordinate(row - 1, col + 1), tileSize), col + 1, row - 1, 1, 1);
                    }
                    //middle top
                    grid.add(new TileDisable(new Coordinate(row - 1, col), tileSize), col, row - 1, 1, 1);
                    //left top
                    if (col - 1 != 0) {
                        grid.add(new TileDisable(new Coordinate(row - 1, col - 1), tileSize), col - 1, row - 1, 1, 1);
                    }
                }
                //middle
                for (int i = 0; i < shipLength; i++) {
                    // left middle
                    if (col - 1 != 0) {
                        grid.add(new TileDisable(new Coordinate(row + i, col - 1), tileSize), col - 1, row + i, 1, 1);
                    }
                    //right middle
                    if (col + 1 != mapSize + 1) {
                        grid.add(new TileDisable(new Coordinate(row + i, col + 1), tileSize), col + 1, row + i, 1, 1);
                    }
                }
                //bottom
                if (row + shipLength != mapSize + 1) {
                    //right bottom
                    if (col + 1 != mapSize + 1) {
                        grid.add(new TileDisable(new Coordinate(row + shipLength, col + 1), tileSize), col + 1, row + shipLength, 1, 1);
                    }
                    //middle bottom
                    grid.add(new TileDisable(new Coordinate(row + shipLength, col), tileSize), col, row + shipLength, 1, 1);
                    //left bottom
                    if (col - 1 != 0) {
                        grid.add(new TileDisable(new Coordinate(row + shipLength, col - 1), tileSize), col - 1, row + shipLength, 1, 1);
                    }
                }
            }
            case VERT_UP -> {
                //bottom
                if (row + 1 != mapSize + 1) {
                    //right bottom
                    if (col + 1 != mapSize + 1) {
                        grid.add(new TileDisable(new Coordinate(row + 1, col + 1), tileSize), col + 1, row + 1, 1, 1);
                    }
                    //middle bottom
                    grid.add(new TileDisable(new Coordinate(row + 1, col), tileSize), col, row + 1, 1, 1);
                    //left bottom
                    if (col - 1 != 0) {
                        grid.add(new TileDisable(new Coordinate(row + 1, col - 1), tileSize), col - 1, row + 1, 1, 1);
                    }
                }
                //middle
                for (int i = 0; i < shipLength; i++) {
                    // left middle
                    if (col - 1 != 0) {
                        grid.add(new TileDisable(new Coordinate(row - i, col - 1), tileSize), col - 1, row - i, 1, 1);
                    }
                    //right middle
                    if (col + 1 != mapSize + 1) {
                        grid.add(new TileDisable(new Coordinate(row - i, col + 1), tileSize), col + 1, row - i, 1, 1);
                    }
                }
                //top
                if (row - shipLength != 0) {
                    //right top
                    if (col + 1 != mapSize + 1) {
                        grid.add(new TileDisable(new Coordinate(row - shipLength, col + 1), tileSize), col + 1, row - shipLength, 1, 1);
                    }
                    //middle top
                    grid.add(new TileDisable(new Coordinate(row - shipLength, col), tileSize), col, row - shipLength, 1, 1);
                    //left top
                    if (col - 1 != 0) {
                        grid.add(new TileDisable(new Coordinate(row - shipLength, col - 1), tileSize), col - 1, row - shipLength, 1, 1);
                    }
                }
            }
        }
    }

    /**
     * Get Node by Row, Column Index
     * @param row row
     * @param column colume
     * @return object
     */
    public Node getNodeByRowColumnIndex (final int row, final int column) {
        Node result = null;
        ObservableList<Node> children = grid.getChildren();

        for (Node node : children) {
            if(GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }
        return result;
    }

    /**
     * get initialized board
     * @param myBoard container where to initialize board
     */
    public void getInitializedBoard(VBox myBoard){
        myBoard.getChildren().add(grid);
    }

    /**
     * reset placed ships
     */
    public void resetShipPlaced() {
        this.shipPlaced = 0;
    }
}
