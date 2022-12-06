package gui.objekt;

import gui.GUIPlayer;
import gui.controllers.ControllerGame;
import gui.tile.*;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import logic.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class GuiBoard {
    private Label failLabel;
    private GridPane grid;
    private final int tileSize;
    private int shipPlaced = 0;
    private final boolean isEnemyBoard;
    private final GUIPlayer guiPlayer = GUIPlayer.getInstance();
    private ControllerGame controllerGame = null;
    private Label turnLabel;

    /**
     * Gui object for the board
     * @param tileSize
     * @param isEnemyBoard
     */
    public GuiBoard(int tileSize, boolean isEnemyBoard, Label failedLabel) {
        this.failLabel = failedLabel;
        this.tileSize = tileSize;
        this.isEnemyBoard = isEnemyBoard;
    }

    public GuiBoard(int tileSize, boolean isEnemyBoard) {
        this.tileSize = tileSize;
        this.isEnemyBoard = isEnemyBoard;
    }

    /**
     * Initialize GuiBoard
     * @param vboxMiddle
     */
    public void initializeBoard(VBox vboxMiddle){
        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        //set Position on Scene
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
     * @param node
     */
    public void setShip(Node node){
        node.setOnMouseClicked(e -> {
            if(e.getSource() instanceof TileWater){
                if (shipPlaced == guiPlayer.getShips().size()) {
                    //send ship placed but not start game clicked
                    node.setDisable(true);
                } else {
                    TileWater tileWater = (TileWater) e.getSource();
                    Coordinate coordinate = new Coordinate(tileWater.getCoordinate().row(), tileWater.getCoordinate().col());
                    Coordinate coordinateMap = new Coordinate(tileWater.getCoordinate().row() - 1, tileWater.getCoordinate().col() -1);
                    Util.log_debug("SetShipGUI = row: " + coordinate.row()  + " col: " + coordinate.col() + " " + "SetShipMapLogic = row: " + coordinateMap.row()  + " col: " + coordinateMap.col());
                    if (guiPlayer.addShip(guiPlayer.getShips().get(shipPlaced), coordinateMap, guiPlayer.getAlignment())) {
                        guiPlayer.getGuiHarbour().drawShipOnBoard(grid, shipPlaced);
                        setDisabledTiles(guiPlayer.getAlignment(), coordinate);
                        shipPlaced++;
                        if (shipPlaced == guiPlayer.getShips().size()) {
                            guiPlayer.confirmShipsPlaced(true);
                        }
                    } else {
                        ArrayList<String> failPrompt = new ArrayList<>();
                        failPrompt.add("Failed Placing Ships");
                        failPrompt.add("You suck at placing Ship's");
                        failPrompt.add("Are you serious?");
                        failPrompt.add("You are still failing");
                        failPrompt.add("What is wrong with you?");
                        Random rand = new Random();
                        failLabel.setText(failPrompt.get(rand.nextInt(failPrompt.size())));
                    }
                }
            }
        });
    }

    /**
     *
     * @param node
     */
    private void sendShot(Node node) {
        node.setOnMouseClicked(e -> {
            TileWater tileWater = (TileWater) e.getSource();
            Util.log_debug("GuiPlayer clicked ON " + tileWater.getCoordinate().row() + " " + tileWater.getCoordinate().col());
            Coordinate coordinate = new Coordinate(tileWater.getCoordinate().row() - 1, tileWater.getCoordinate().col() - 1);
            Util.log_debug("GuiPlayer send shot " + coordinate.row() + " " + coordinate.col());
            guiPlayer.setShotCoordinate(coordinate);
            node.setDisable(true);
        });
    }

    public void setControllerGame(){
        if(controllerGame == null){
            controllerGame = ControllerGame.getInstance();
            turnLabel = controllerGame.getTurnLabel();
        }
    }

    public void setTurn(){
        setControllerGame();
        if(GUIPlayer.getInstance().getTurn()){
            turnLabel.setText("It's " + guiPlayer.getEnemyUsername() + "'s Turn");
            guiPlayer.setTurn(false);
        } else {
            turnLabel.setText("It's " + guiPlayer.getUsername() + "'s Turn");
            guiPlayer.setTurn(true);
        }
    }

    public void updateBoard(ShotResult shotResult, Coordinate coordinate) {
        Platform.runLater(() -> {
            switch (shotResult) {
                case HIT -> {
                    TileHit tileHit = new TileHit(new Coordinate(coordinate.row() + 1, coordinate.col() + 1), tileSize);
                    grid.add(tileHit, coordinate.col() + 1, coordinate.row() + 1, 1, 1);
                    setTurn();
                }
                case MISS -> {
                    TileMiss tileMiss = new TileMiss(new Coordinate(coordinate.row() + 1, coordinate.col() + 1), tileSize);
                    grid.add(tileMiss, coordinate.col() + 1, coordinate.row() + 1, 1, 1);
                    setTurn();
                }
                case SUNK -> {
                    if (isEnemyBoard) {
                        setTurn();
                        MapState[][] map = guiPlayer.getEnemyMap().getMap();
                        for (int row = 0; row < guiPlayer.getMapSize(); row++) {
                            for (int col = 0; col < guiPlayer.getMapSize(); col++) {
                                switch (map[row][col]) {
                                    case W -> {
                                        TileWater tileWater = new TileWater(new Coordinate(row + 1, col + 1), tileSize);
                                        grid.add(tileWater, col + 1, row + 1, 1, 1);
                                        this.sendShot(tileWater);
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
                        setTurn();
                    }
                }
            }
        });
    }

    /**
     *
     * @param alignment
     * @param coordinate
     */
    public void setDisabledTiles(Alignment alignment, Coordinate coordinate){
        int shipLength = guiPlayer.getShips().get(shipPlaced).getSize();
        int mapSize = guiPlayer.getMapSize();
        int row = coordinate.row();
        int col = coordinate.col();
        switch (alignment) {
            case HOR_RIGHT: {
                //left
                if(col - 1 != 0){
                    //top left
                    if(row - 1 != 0){
                        grid.add(new TileDisable(new Coordinate(row - 1 , col - 1), tileSize), col  - 1 , row - 1, 1, 1);
                    }
                    //middle left
                    grid.add(new TileDisable(new Coordinate(row, col - 1), tileSize), col - 1, row, 1, 1);
                    //bottom left
                    if(row + 1 != mapSize + 1){
                        grid.add(new TileDisable(new Coordinate(row + 1, col - 1), tileSize), col - 1, row + 1, 1, 1);
                    }
                }
                //middle
                for(int i = 0; i < shipLength; i++){
                    if(row - 1 != 0){
                        //top middle
                        grid.add(new TileDisable(new Coordinate(row - 1, col + i), tileSize), col + i, row - 1, 1, 1);
                    }
                    if(row + 1 != mapSize + 1){
                        //bottom middle
                        grid.add(new TileDisable(new Coordinate(row + 1, col + i), tileSize), col + i, row + 1, 1, 1);
                    }
                }
                //right
                if(col + shipLength != mapSize + 1){
                    //bottom right
                    if(row + 1 != mapSize + 1){
                        grid.add(new TileDisable(new Coordinate(row + 1, col + shipLength), tileSize), col + shipLength, row + 1, 1, 1);
                    }
                    //middle right
                    grid.add(new TileDisable(new Coordinate(row, col + shipLength), tileSize), col + shipLength, row, 1, 1);
                    //top right
                    if(row - 1 != 0){
                        grid.add(new TileDisable(new Coordinate(row - 1, col + shipLength), tileSize), col + shipLength, row - 1, 1, 1);
                    }
                }
                break;
            }
            case HOR_LEFT: {
                //right
                if(col + 1 != mapSize + 1){
                    //top right
                    if(row - 1 != 0){
                        grid.add(new TileDisable(new Coordinate(row - 1 , col + 1), tileSize), col  + 1 , row - 1, 1, 1);
                    }
                    //middle right
                    grid.add(new TileDisable(new Coordinate(row, col + 1), tileSize), col + 1, row, 1, 1);
                    //bottom right
                    if(row + 1 != mapSize + 1){
                        grid.add(new TileDisable(new Coordinate(row + 1, col + 1), tileSize), col + 1, row + 1, 1, 1);
                    }
                }
                //middle
                for(int i = 0; i < shipLength; i++){
                    if(row - 1 != 0){
                        //top middle
                        grid.add(new TileDisable(new Coordinate(row - 1, col - i), tileSize), col - i, row - 1, 1, 1);
                    }
                    if(row + 1 != mapSize + 1){
                        //bottom middle
                        grid.add(new TileDisable(new Coordinate(row + 1, col - i), tileSize), col - i, row + 1, 1, 1);
                    }
                }
                //left
                if(col - shipLength != 0){
                    //bottom left
                    if(row + 1 != mapSize + 1){
                        grid.add(new TileDisable(new Coordinate(row + 1, col - shipLength), tileSize), col - shipLength, row + 1, 1, 1);
                    }
                    //middle left
                    grid.add(new TileDisable(new Coordinate(row, col - shipLength), tileSize), col - shipLength, row, 1, 1);
                    //top left
                    if(row - 1 != 0){
                        grid.add(new TileDisable(new Coordinate(row - 1, col - shipLength), tileSize), col - shipLength, row - 1, 1, 1);
                    }
                }
                break;
            }
            case VERT_DOWN: {
                //top
                if(row - 1 != 0){
                    //right top
                    if(col + 1 != mapSize + 1){
                        grid.add(new TileDisable(new Coordinate(row - 1, col + 1), tileSize), col + 1, row - 1, 1, 1);
                    }
                    //middle top
                    grid.add(new TileDisable(new Coordinate(row -1, col), tileSize), col, row - 1, 1, 1);
                    //left top
                    if(col - 1 != 0){
                        grid.add(new TileDisable(new Coordinate(row - 1, col - 1), tileSize), col - 1, row - 1, 1, 1);
                    }
                }
                //middle
                for(int i = 0; i < shipLength; i++){
                    // left middle
                    if(col - 1 != 0){
                        grid.add(new TileDisable(new Coordinate(row + i, col -1), tileSize), col - 1, row + i, 1, 1);
                    }
                    //right middle
                    if(col + 1 != mapSize + 1){
                        grid.add(new TileDisable(new Coordinate(row + i, col + 1), tileSize), col + 1, row + i, 1, 1);
                    }
                }
                //bottom
                if(row + shipLength != mapSize + 1){
                    //right bottom
                    if(col + 1 != mapSize + 1){
                        grid.add(new TileDisable(new Coordinate(row + shipLength, col + 1), tileSize), col + 1, row + shipLength, 1, 1);
                    }
                    //middle bottom
                    grid.add(new TileDisable(new Coordinate(row + shipLength, col), tileSize), col, row + shipLength, 1, 1);
                    //left bottom
                    if(col - 1 != 0){
                        grid.add(new TileDisable(new Coordinate(row + shipLength, col - 1), tileSize), col - 1, row + shipLength, 1, 1);
                    }
                }
                break;
            }
            case VERT_UP: {
                //bottom
                if(row + 1 != mapSize + 1){
                    //right bottom
                    if(col + 1 != mapSize + 1){
                        grid.add(new TileDisable(new Coordinate(row + 1, col + 1), tileSize), col + 1, row + 1, 1, 1);
                    }
                    //middle bottom
                    grid.add(new TileDisable(new Coordinate(row + 1, col), tileSize), col, row + 1, 1, 1);
                    //left bottom
                    if(col - 1 != 0){
                        grid.add(new TileDisable(new Coordinate(row + 1, col - 1), tileSize), col - 1, row + 1, 1, 1);
                    }
                }
                //middle
                for(int i = 0; i < shipLength; i++){
                    // left middle
                    if(col - 1 != 0){
                        grid.add(new TileDisable(new Coordinate(row - i, col -1), tileSize), col - 1, row - i, 1, 1);
                    }
                    //right middle
                    if(col + 1 != mapSize + 1){
                        grid.add(new TileDisable(new Coordinate(row - i, col + 1), tileSize), col + 1, row - i, 1, 1);
                    }
                }
                //top
                if(row - shipLength != 0){
                    //right top
                    if(col + 1 != mapSize + 1){
                        grid.add(new TileDisable(new Coordinate(row - shipLength, col + 1), tileSize), col + 1, row - shipLength, 1, 1);
                    }
                    //middle top
                    grid.add(new TileDisable(new Coordinate(row - shipLength, col), tileSize), col, row - shipLength, 1, 1);
                    //left top
                    if(col - 1 != 0){
                        grid.add(new TileDisable(new Coordinate(row - shipLength, col - 1), tileSize), col - 1, row - shipLength, 1, 1);
                    }
                }
            }
        }
    }

    /**
     * Get Node by Row, Column Index
     * @param row
     * @param column
     * @return
     */
    public Node getNodeByRowColumnIndex (final int row, final int column) {
        Node result = null;
        ObservableList<Node> childrens = grid.getChildren();

        for (Node node : childrens) {
            if(grid.getRowIndex(node) == row && grid.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }
        return result;
    }

    public void getInitializedBoard(VBox myBoard){
        myBoard.getChildren().add(grid);
    }

    public void setShipPlaced() {
        this.shipPlaced = 0;
    }
}
