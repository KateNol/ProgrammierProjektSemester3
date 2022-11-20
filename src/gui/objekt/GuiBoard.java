package gui.objekt;

import gui.GUIPlayer;
import gui.tile.TileBoardText;
import gui.tile.TileDisable;
import gui.tile.TileShip;
import gui.tile.TileWater;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import logic.Alignment;
import logic.Coordinate;
import logic.Ship;

import java.util.ArrayList;


public class GuiBoard {

    private GUIPlayer guiPlayer;
    private GridPane grid;
    private ArrayList<Ship> ships;
    private int boardSize;
    private int tileSize;
    private int shipCount;
    private int shipPlaced = 0;
    private boolean vertical = false;

    /**
     * Gui object for the board
     * @param ships
     * @param boardSize
     * @param tileSize
     * @param guiPlayer
     */
    public GuiBoard(ArrayList<Ship> ships, int boardSize, int tileSize, GUIPlayer guiPlayer) {
        this.guiPlayer = guiPlayer;
        this.ships = ships;
        this.boardSize = boardSize;
        this.tileSize = tileSize;
        this.shipCount = ships.size();
    }

    public void initializeBoard(VBox vboxMiddle){
        grid = new GridPane();
        grid.setTranslateX((380 - (((double)boardSize * tileSize) / 2)));
        for(int row = 0; row < boardSize + 1; row++){
            for (int col = 0; col < boardSize + 1; col++){
                if(row == 0 && col == 0){
                    TileBoardText tbt = new TileBoardText(new Coordinate(row, col),tileSize,"");
                    grid.add(tbt, row, col, 1, 1);
                } else {
                    if(row == 0){
                        TileBoardText tbt = new TileBoardText(new Coordinate(row, col), tileSize,"" + (char) ('A' + col - 1));
                        grid.add(tbt, col, row, 1, 1);
                    } else if (col == 0 && row > 0) {
                        TileBoardText tbt = new TileBoardText(new Coordinate(row, col), tileSize,"" + row);
                        grid.add(tbt, col, row, 1, 1);
                    } else {
                        TileWater tile = new TileWater(new Coordinate(row, col), tileSize);
                        grid.add(tile, row, col, 1, 1);
                    }
                }
            }
        }
        grid.getChildren().forEach(this::setShip);
        vboxMiddle.getChildren().add(grid);
    }

    public void setShip(Node node){
        node.setOnMouseClicked(e -> {
            if(e.getSource() instanceof TileWater){
                if (shipPlaced == shipCount) {
                    node.setDisable(true);
                } else {
                    TileWater tileWater = (TileWater) e.getSource();
                    int row = tileWater.getCoordinate().row();
                    int col = tileWater.getCoordinate().col();
                    Coordinate coordinate = new Coordinate(row, col);
                    Coordinate[] coordinatesVer = guiPlayer.createArray(ships.get(shipPlaced).getSize(), coordinate, Alignment.VERT_DOWN);
                    Coordinate[] coordinatesHor = guiPlayer.createArray(ships.get(shipPlaced).getSize(), coordinate, Alignment.HOR_RIGHT);
                    //Vertical
                    if(vertical && isValidPoint(row, col, ships.get(shipPlaced).getSize(), coordinatesVer)){
                        if(shipPlaced < shipCount) {
                            for (int i = 0; i < ships.get(shipPlaced).getSize(); i++) {
                                grid.add(new TileShip(new Coordinate(row + i, col), tileSize), row, col + i, 1, 1);
                                //Deactivate Tiles clickable function
                                setDisableVertical(row, col);
                            }
                            //Add ship to Board
                            guiPlayer.addShip(ships.get(shipPlaced).getSize(), coordinate, Alignment.VERT_DOWN);
                            shipPlaced++;
                        }
                        //Horizontal
                    } else if (isValidPoint(row, col, ships.get(shipPlaced).getSize(), coordinatesHor)) {
                        if(shipPlaced < shipCount){
                            for(int i = 0; i < ships.get(shipPlaced).getSize(); i++){
                                grid.add(new TileShip(new Coordinate(row, col + 1), tileSize),row + i,col,1,1);
                                //Deactivate Tiles clickable function
                                setDisableHorizontal(row, col);
                            }
                            //Add ship to Board
                            guiPlayer.addShip(ships.get(shipPlaced).getSize(), coordinate, Alignment.HOR_RIGHT);
                            shipPlaced++;
                        }
                    }
                }
            }
        });
    }

    public void setDisableHorizontal(int row, int col){
        //left
        if(row - 1 != 0){
            if(col - 1 != 0){
                //top left
                grid.add(new TileDisable(new Coordinate(row - 1 , col - 1), tileSize), row  - 1 , col - 1, 1, 1);
            }
            //middle left
            grid.add(new TileDisable(new Coordinate(row - 1, col), tileSize), row - 1 , col, 1, 1);
            //bottom left
            if(col + 1 != boardSize + 1){
                grid.add(new TileDisable(new Coordinate(row - 1 , col + 1), tileSize), row - 1 , col + 1, 1, 1);
            }
        }
        //middle
        for(int i = 0; i < ships.get(shipPlaced).getSize(); i++){
            if(col - 1 != 0){
                //top middle
                grid.add(new TileDisable(new Coordinate(row + i, col + 1), tileSize), row + i, col - 1, 1, 1);
            }
            if(col + 1 != boardSize + 1){
                //bottom middle
                grid.add(new TileDisable(new Coordinate(row + i, col + 1), tileSize), row + i, col + 1, 1, 1);
            }
        }
        //right
        if(row + ships.get(shipPlaced).getSize() != boardSize + 1){
            //bottom right
            if(col + 1 != boardSize + 1){
                grid.add(new TileDisable(new Coordinate(row + ships.get(shipPlaced).getSize(), col + 1), tileSize), row + ships.get(shipPlaced).getSize(), col + 1, 1, 1);
            }
            //middle right
            grid.add(new TileDisable(new Coordinate(row + ships.get(shipPlaced).getSize(), col), tileSize), row + ships.get(shipPlaced).getSize(), col, 1, 1);
            //top right
            if(col - 1 != 0){
                grid.add(new TileDisable(new Coordinate(row + ships.get(shipPlaced).getSize(), col - 1), tileSize), row + ships.get(shipPlaced).getSize(), col - 1, 1, 1);
            }
        }
    }

    public void setDisableVertical(int row, int col){
        //top
        if(col - 1 != 0){
            //right top
            if(row + 1 != boardSize + 1){
                grid.add(new TileDisable(new Coordinate(row + 1, col - 1), tileSize), row + 1, col - 1, 1, 1);
            }
            //middle top
            grid.add(new TileDisable(new Coordinate(row, col - 1), tileSize), row, col - 1, 1, 1);
            //left top
            if(row - 1 != 0){
                grid.add(new TileDisable(new Coordinate(row - 1, col - 1), tileSize), row - 1, col - 1, 1, 1);
            }
        }
        //middle
        for(int i = 0; i < ships.get(shipPlaced).getSize(); i++){
            // left middle
            if(row - 1 != 0){
                grid.add(new TileDisable(new Coordinate(row + 1, col + i), tileSize), row - 1, col + i, 1, 1);
            }
            //right middle
            if(row + 1 != boardSize + 1){
                grid.add(new TileDisable(new Coordinate(row + 1, col + i), tileSize), row + 1, col + i, 1, 1);
            }
        }
        //bottom
        if(col + ships.get(shipPlaced).getSize() != boardSize + 1){
            //right bottom
            if(row + 1 != boardSize + 1){
                grid.add(new TileDisable(new Coordinate(row + 1, col + ships.get(shipPlaced).getSize()), tileSize), row + 1, col + ships.get(shipPlaced).getSize(), 1, 1);
            }
            //middle bottom
            grid.add(new TileDisable(new Coordinate(row, col + ships.get(shipPlaced).getSize()), tileSize), row, col + ships.get(shipPlaced).getSize(), 1, 1);
            //left bottom
            if(row - 1 != 0){
                grid.add(new TileDisable(new Coordinate(row - 1, col + ships.get(shipPlaced).getSize()), tileSize), row - 1, col + ships.get(shipPlaced).getSize(), 1, 1);
            }
        }
    }

    public void getInitializedBoard(VBox myBoard){
        myBoard.getChildren().add(grid);
    }

    private boolean isValidPoint(int row, int col, int i, Coordinate[] coordinates) {
        if (vertical && guiPlayer.checkLegal(coordinates)){
            return col + i - 1 <= boardSize;
        } else if (guiPlayer.checkLegal(coordinates)){
            return row + i - 1 <= boardSize;
        }
        return false;
    }

    public boolean isVertical() {
        return vertical;
    }

    public void setVertical(boolean vertical) {
        this.vertical = vertical;
    }
}
