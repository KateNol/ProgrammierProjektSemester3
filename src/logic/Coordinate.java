package logic;

/**
 * represents a 2DPoint with x and y coordinate
 * very basic and low functionality
 */
public class Coordinate {
    private int row;
    private int col;

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

    public void setRow(final int row) {
        this.row = row;
    }

    public void setCol(final int col) {
        this.col = col;
    }

    public boolean isEqual(Coordinate c) {
        if(this.row == c.row && this.col == c.col) {
            return true;
        }
        return false;
    }
}
