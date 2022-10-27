package ai;

import java.util.*;
import java.math.*;
import java.awt.Point;

public class AI extends logic.Player {
    protected int[][] board;    //Represents the Board
    List<Point> Moves = new ArrayList<Point>(); //Stores the moves which were already used
    List<Point> HitPoints = new ArrayList<Point>(); //Stores the Points which hit a ship

    //Constructor which initiates the board size
    public AI (int[][] board) {
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
            } while(Moves.contains(bullet));
        }
        Moves.add(bullet);
        return bullet;
    }
}
