package logic;
public class Player {
    int maxLevel = 1;
    String name = null;
    Ship[] ships = null;

    /**
     * Initialize player with name and empty ships for level 1 in an array
     * @param name Name of the player
     */
    public Player(String name) {
        this.name = name;
        //Initialize shipsizes for Level 1 fixed
        int[] shipSizes = {2,2,2,2,4,6};
        for (int i = 0; i < shipSizes.length; i++) {
            ships[i] = new Ship(shipSizes[i]);
        }
    }

    /**
     *
     * @param shipNumber index of the ship in the ships array
     * @param c coordinate of the pivotpoint
     * @param a alignment of the ship
     * @see Alignment
     */
    public void setShip(int shipNumber, Coordinate c, Alignment a) {
        if(shipNumber < 6 && shipNumber >= 0) {
            ships[shipNumber].setPos(c, a);
        }
    }

    public MapStates getShot()
}
