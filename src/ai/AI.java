package ai;

import network.NetworkPlayer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AI extends NetworkPlayer {
    protected int[][] board;    //Represents the Board
    List<Point> Moves = new ArrayList<Point>(); //Stores the moves which were already used
    List<Point> HitPoints = new ArrayList<Point>(); //Stores the Points which hit a ship

    //Constructor which initiates the board size
    public AI(i .[][] board) {
        this.board = board;
    }

    /* public int[][] placeShips(int[] listShip) {

    }*/

    public Point shoot() {  //Generates random points to place shots
        Random placeShot = new Random();
        Point bullet = new Point();
        if (Moves.isEmpty()) {  //No shot placed
            bullet.x = placeShot.nextInt(board.length); //length of row
            bullet.y = placeShot.nextInt(board[0].length);  //length of column
        } else {    //Iterate already placed shots to place no shots twice
            do {
                bullet.x = placeShot.nextInt(board.length); //length of row
                bullet.y = placeShot.nextInt(board[0].length);  //length of column
            } while (Moves.contains(bullet));
        }
        Moves.add(bullet);
        return bullet;
    }
}
