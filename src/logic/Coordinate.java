package logic;

/**
 * represents a 2DPoint with x and y coordinate
 * very basic and low functionality
 */
public class Coordinate {
    public int row;
    public int col;

    public Coordinate(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return this.row;
    }

    public int getCol() {
        return this.col;
    }
}
