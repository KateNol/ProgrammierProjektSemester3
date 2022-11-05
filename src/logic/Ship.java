package logic;

import java.io.IOException;

/**
 * implements ships with size, exact positions and health
 */
public class Ship {
    private int size; //shipsize, usually between 2 and 7
    private Coordinate pos[]; //individual coordinates of ship
    private int health; //health, max. health equals size

    /**
     *
     * @param pos Array containing the Coordinates of the ship
     */
    public Ship(Coordinate[] pos) {
        initShip(pos.length);
        this.pos = pos;
    }

    /**
     * initialize an empty ship, can be positioned later
     * @param size int
     */
    public Ship(int size) {
        initShip(size);
    }

    /**
     * initialises the basic shipattributes without the position
     * @param size int
     */
    private void initShip(int size) {
        this.size = size;
        this.health = this.size;
        this.pos = new Coordinate[size];
    }

    /**
     *
     * @return size as int
     */
    public int getSize() {
        return this.size;
    }

    /**
     *
     * @return position as Coordinate-Array
     */
    public Coordinate[] getPos() {
        return this.pos;
    }

    /**
     *
     * @param p index starting from pivot, min 0, max size of ship-1
     * @return coordinate of point as Coordinate, if parameter is invalid it returns null
     */
    public Coordinate getPoint(int p) throws IllegalArgumentException {

        if(p < 0 && p >= this.size) {
            throw new IllegalArgumentException("Point not in range!");
        }
        return this.pos[p];
    }

    /**
     * Set the distinct Coordinates of the ship
     * @param pos Coordinate array containing the Coordinates of the ship
     */
    protected void setPos(Coordinate[] pos) throws IndexOutOfBoundsException{
        if(pos.length > this.size) { throw new IndexOutOfBoundsException("Shipsize doesn't match the handed Size");}
        this.pos = pos;
    }

    /**
     * checks if ship got hit
     * @param c Coordinate
     * @return true if hit, false if not
     */
    public boolean checkIfHit(Coordinate c) {
        for(Coordinate p: pos) {
            if(c.row() == p.row() && c.col() == p.col()) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @return health decreased by one
     */
    public int decreaseHealth() {
        return --this.health;
    }
}
