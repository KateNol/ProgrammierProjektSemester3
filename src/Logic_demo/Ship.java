package Logic_demo;

/**
 * implements ships with size, exact positions and health
 */
public class Ship {
    private int size; //shipsize, usually between 2 and 7
    private Coordinate pos[]; //individual coordinates of ship
    private int health; //health, max. health equals size

    /**
     *
     * @param size defines shipsize as int
     * @param pivot pivot of ship, works only with alignment
     * @param align alignment, can be HOR_LEFT, HOR_RIGHT, VERT_UP, VERT_DOWN
     */
    public Ship(int size, Coordinate pivot, Alignment align) {
        initShip(size);
        setPos(pivot, align);
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
     * sets position of the ship starting with the pivotpoint in the direction handet as Alignment
     * @param pivot pivot of ship, works only with alignment
     * @param align alignment, can be HOR_LEFT, HOR_RIGHT, VERT_UP, VERT_DOWN
     * @return returns true, if set successfull false, if not
     */
    public boolean setPos(Coordinate pivot, Alignment align) {
        for(int i = 0; i < this.size; i++) {
            switch (align) {
                case VERT_UP:
                    //TODO check boardSize
                    //TODO check if touching
                    //TODO check if every coordinate is valid
                    pos[i].setRow(pivot.getRow());
                    pos[i].setCol(pivot.getCol()-i);
                    break;
                case VERT_DOWN:
                    pos[i].setRow(pivot.getRow());
                    pos[i].setCol(pivot.getCol()+i);
                    break;
                case HOR_RIGHT:
                    pos[i].setRow(pivot.getRow()+i);
                    pos[i].setCol(pivot.getCol());
                    break;
                case HOR_LEFT:
                    pos[i].setRow(pivot.getRow()-i);
                    pos[i].setCol(pivot.getCol());
                    break;
                default:
                    return false;
            }
        }
        return true;
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
     * checks if ship got hit
     * @param c Coordinate
     * @return true if hit, false if not
     */
    public boolean checkIfHit(Coordinate c) {
        for(Coordinate p: pos) {
            if(c.isEqual(p)) {
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
