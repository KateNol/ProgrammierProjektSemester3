package logic;

/**
 * implements ships with size, exact positions and health
 */
public class Ship {
    private int size; //shipsize, usually between 2 and 7
    private Point pos[]; //individual coordinates of ship
    private int health; //health, max. health equals size

    /**
     *
     * @param size defines shipsize as int
     * @param pivot pivot of ship, works only with alignment
     * @param align alignment, can be HOR_LEFT, HOR_RIGHT, VERT_UP, VERT_DOWN
     */
    public Ship(int size, Point pivot, Alignment align) {
        initShip(size);
        setPos(pivot, align);
    }

    public Ship(int Size) {
        initShip(size);
    }

    private void initShip(int size) {
        this.size = size;
        this.health = this.size;
        this.pos = new Point[size];
    }

    /**
     * sets position of the ship starting with the pivotpoint in the direction handet as Alignment
     * @param pivot pivot of ship, works only with alignment
     * @param align alignment, can be HOR_LEFT, HOR_RIGHT, VERT_UP, VERT_DOWN
     * @return returns true, if set successfull false, if not
     */
    public boolean setPos(Point pivot, Alignment align) {
        for(int i = 0; i < this.size; i++) {
            switch (align) {
                case VERT_UP:
                    //TODO check boardSize
                    //TODO check if touching
                    //TODO check if every coordinate is valid
                    pos[i].x = pivot.x;
                    pos[i].y = pivot.y - i;
                    break;
                case VERT_DOWN:
                    pos[i].x = pivot.x;
                    pos[i].y = pivot.y + i;
                    break;
                case HOR_RIGHT:
                    pos[i].x = pivot.x + i;
                    pos[i].y = pivot.y;
                    break;
                case HOR_LEFT:
                    pos[i].x = pivot.x - i;
                    pos[i].y = pivot.y;
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
     * @return position as Point-Array
     */
    public Point[] getPos() {
        return this.pos;
    }

    /**
     *
     * @param p index starting from pivot, min 0, max size of ship-1
     * @return coordinate of point as Point, if parameter is invalid it returns null
     */
    public Point getPoint(int p) {
        //TODO throw exception if p is invalid
        if(p >= 0 && p < this.size) {
            return pos[p];
        }
        return null;
    }

    public int gotHit() {
        return --this.health;
    }
}
