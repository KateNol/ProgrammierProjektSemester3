package gui.objekt;

import gui.GUIPlayer;
import gui.tile.*;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import logic.Alignment;
import logic.Coordinate;
import logic.ShotResult;

public class GuiBoard {

    private GridPane grid;
    private int tileSize;
    private int shipPlaced = 0;
    private boolean isEnemyBoard;
    private GUIPlayer guiPlayer = GUIPlayer.getInstance();

    /**
     * Gui object for the board
     * @param tileSize
     * @param isEnemyBoard
     */
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
        //set Position on Scene
        grid.setTranslateX((380 - (((double)guiPlayer.getMapSize() * tileSize) / 2)));
        for(int row = 0; row < guiPlayer.getMapSize() + 1; row++){
            for (int col = 0; col < guiPlayer.getMapSize() + 1; col++){
                if(row == 0 && col == 0){
                    TileBoardText tbt = new TileBoardText(new Coordinate(row, col),tileSize,"");
                    grid.add(tbt, row, col, 1, 1);
                } else {
                    if(row == 0){
                        TileBoardText tbt = new TileBoardText(new Coordinate(row, col), tileSize,"" + (char) ('A' + col - 1));
                        grid.add(tbt, col, row, 1, 1);
                    } else if (col == 0) {
                        TileBoardText tbt = new TileBoardText(new Coordinate(row, col), tileSize,"" + row);
                        grid.add(tbt, col, row, 1, 1);
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
                    node.setDisable(true);
                } else {
                    TileWater tileWater = (TileWater) e.getSource();
                    Coordinate coordinate = new Coordinate(tileWater.getCoordinate().row(), tileWater.getCoordinate().col());
                    Coordinate coordinateMap = new Coordinate(tileWater.getCoordinate().row() - 1, tileWater.getCoordinate().col() -1);
                    System.out.println("SetShip = row: " + coordinate.row()  + " col: " + coordinate.col());
                    System.out.println("SetShipMap = row: " + coordinateMap.row()  + " col: " + coordinateMap.col());
                    if(guiPlayer.addShip1(guiPlayer.getShips().get(shipPlaced), coordinateMap, guiPlayer.getAlignment())){
                        guiPlayer.getGuiHarbour().drawShipOnBoard(grid, shipPlaced);
                        setDisabledTiles(guiPlayer.getAlignment(), coordinate);
                        guiPlayer.printBothMaps();
                        shipPlaced++;
                        if (shipPlaced == guiPlayer.getShips().size()) {
                            guiPlayer.confirmShipsPlaced(true);
                        }
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
            guiPlayer.setShotCoordinate(tileWater.getCoordinate());
            node.setDisable(true);
        });
    }

    public void updateBoard(ShotResult shotResult, Coordinate coordinate) {
        Platform.runLater(() -> {
            switch (shotResult) {
                case HIT: {
                    TileHit tileHit = new TileHit(coordinate, tileSize);
                    grid.add(tileHit, coordinate.col(), coordinate.row(), 1, 1);
                }
                case MISS: {
                    TileMiss tileMiss = new TileMiss(coordinate, tileSize);
                    grid.add(tileMiss, coordinate.col(), coordinate.row(), 1, 1);
                }
                case SUNK: {
                    //TODO
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
     * @param gridPane
     * @return
     */
    public Node getNodeByRowColumnIndex (final int row, final int column, GridPane gridPane) {
        Node result = null;
        ObservableList<Node> childrens = gridPane.getChildren();

        for (Node node : childrens) {
            if(gridPane.getRowIndex(node) == row && gridPane.getColumnIndex(node) == column) {
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
